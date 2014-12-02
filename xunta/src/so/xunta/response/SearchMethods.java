package so.xunta.response;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.text.AttributeSet.CharacterAttribute;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Collector;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.SearcherManager;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TermRangeQuery;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.highlight.Fragmenter;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.search.highlight.SimpleSpanFragmenter;
import org.apache.lucene.search.similarities.DefaultSimilarity;
import org.apache.lucene.store.AlreadyClosedException;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.LockObtainFailedException;
import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.Version;
import org.wltea.analyzer.lucene.IKAnalyzer;

import so.xunta.localcontext.LocalContext;

import com.aigine.common.p;

//与搜索相关的标准方法都放在这里.所有方法都是静态的,以节省空间. 几个不变的搜索变量也放在这里. 如果变量与某次访问有关, 则放在SearchVars中, 如query.
public class SearchMethods {
	// static Similarity similarity=new DefaultSimilarity();
	// //在creatSearcher里直接引用。
	// static Similarity similarity=new Similarity_Xu();
	// static Similarity similarity=new BM25Similarity();
	// static Similarity similarity=new LMDirichletSimilarity();
	// "\\\\192.168.1.103\\e$\\home\\lucene\\bbs_index_single";

	public static IndexReader reader1;
	public static DirectoryReader dirReader1;
	public static IndexSearcher searcher1;
	public static IndexReader reader2;
	public static DirectoryReader dirReader2;
	public static IndexSearcher searcher2;
	public static IndexReader reader_tmp;
	public static DirectoryReader dirReader_tmp;
	public static IndexSearcher searcher_tmp;
	public static byte currentSearcherNo = 1;// 等于1或2.
	// //上面这些变量用于分别创建两套reader和searcher,以方便分别关闭和创建.2014.7.22

	public static Analyzer analyzer = new IKAnalyzer();// 无参或为false时，为最细粒度切分．true时，为智能切分．
	// 2014.7.17建索引时,有true参数,表示为智能分词.但搜索时不要加,否则大词包小词时则不会高亮显示,如搜辽阳时,辽阳市和辽阳县会不显.
	static String searchField = "index_content";
	public static SearcherManager searcherManager = null;
	static File indexFile = new File(LocalContext.indexFilePath);
	// static SearcherFactory searcherFactory = new SearcherFactory();

	static long lastTimeofSearcher = System.currentTimeMillis();// 上次创建searcher的时间.
	static int INTERVAL_INDEXCHECK = 15000;// 检查索引并重建searcher的间隔,单位ms.
	static int DELAYEDTIME_READERCLOSE = 10000;// 在关闭reader时故意拖延的一小段时间,以等待正在使用它的线程结束.

	// 这个变量应该是通常充许的访问失败的timeout时间,同时,它应该比INTERVAL_INDEXCHECK少一两秒.

	// searcherManager = new SearcherManager(FSDirectory.open(indexFile) ,
	// searcherFactory);

	// dirReader = reader.open(FSDirectory.open(indexFile));
	// ((DirectoryReader) reader).isCurrent();
	// DirectoryReader.openIfChanged((DirectoryReader) reader);//当
	// 一个查询发生时, 如果reader是closed的,则打开,并生成searcher;
	// 每个查询结束后, 看是否过了一分钟, 如果超了,则启用 另一个高优先级的线程-重新打开reader及searcher并临时赋值给另一个变量暂存.
	// 关闭原来的searcher(用searchermanager生成及关闭,这个关闭是否等待线程结束?,但不结束也无所谓,
	// 下一步就直接启用新reader了.)
	// 判断是否关闭,如果关闭了,则将暂存的新reader/searcher赋给原来的reader/searcher. 是否关闭这个判断要测试一下.
	// 用两个reader,并用一个开关变量表示当前使用的reader. 当打开新的searcher时,马上转变开关,新的搜索就将使用新searcher.
	// 然后再关闭旧的searcher.
	// 下面的几个方法是searcher1和searcher2通用的,
	// 用currentSearcherNo这个开关决定对哪个searcher进行操作.//2014.7.23

	public static IndexSearcher getOneSearcherofTwo() {
		//p.prt("searcher1=" + searcher1);
		//p.prt("searcher2=" + searcher2);

		if (currentSearcherNo == 1) {// 用这个开关量来决定返回哪个searcher, 不再用形参来决定.
			return createSearcher1_IfNullorClosed();
		} else {
			return createSearcher2_IfNullorClosed();
		}
	}

	static IndexSearcher createSearcher1_IfNullorClosed() {
		if (searcher1 != null) {// 以下的null或关闭的判断及处理是为了应付第一次使用的预热,以及运行中的预防意外失效或关闭.
			//p.prt("s1有效.");
			try {
				if (dirReader1 == null) {
					p.prt("r1本该有效,却==null.创建r1再创建s1...");
					dirReader1 = createDirReader(indexFile);
					searcher1 = createSearcher(dirReader1);
					return searcher1;
				} else {
					dirReader1.getContext();// ensureOpen()方法不是public的,不可用.
					// 暂时用getcontext来判断是否关闭.
					p.prt("r1仍然打开.直接返回s");
					return searcher1;
				}
			} catch (AlreadyClosedException e) {// 如果searcher已关闭,则重新创建.
			//	p.prt("r1本该有效,却已关闭.创建r1再创建s1...");
				dirReader1 = createDirReader(indexFile);
				searcher1 = createSearcher(dirReader1);
				return searcher1;
			}
		} else {// 如果searcher1==null,则创建.
			p.prt("S1本该有效,但发现为null,创建...");
			try {
				if (dirReader1 == null) {
				//	p.prt("r1本该有效,却==null.创建r1再创建s1...");
					dirReader1 = createDirReader(indexFile);
					searcher1 = createSearcher(dirReader1);
					return searcher1;
				} else {
					dirReader1.getContext();// ensureOpen()方法不是public的,不可用.
					// 暂时用getcontext来判断是否关闭.
				//	p.prt("r1仍然打开.从r1创建s1");
					searcher1 = createSearcher(dirReader1);
					return searcher1;
				}
			} catch (AlreadyClosedException e) {// 如果searcher已关闭,则重新创建.
			//	p.prt("r1本该有效,却已关闭.创建r1再创建s1...");
				dirReader1 = createDirReader(indexFile);
				searcher1 = createSearcher(dirReader1);
				return searcher1;
			}
		}
	}

	static IndexSearcher createSearcher2_IfNullorClosed() {
		if (searcher2 != null) {
			//p.prt("s2有效.");
			try {
				if (dirReader2 == null) {
				//	p.prt("r2本该有效,却==null.创建r2再创建s2...");
					dirReader2 = createDirReader(indexFile);
					searcher2 = createSearcher(dirReader1);
					return searcher2;
				} else {
					dirReader2.getContext();// ensureOpen()方法不是public的,不可用.
					// 暂时用getcontext来判断是否关闭.
				//	p.prt("r2仍然打开.直接返回s");
					return searcher2;
				}
			} catch (AlreadyClosedException e) {// 如果searcher已关闭,则重新创建.
				p.prt("r2本该有效,却已关闭.创建r2再创建s2...");
				dirReader2 = createDirReader(indexFile);
				searcher2 = createSearcher(dirReader2);
				return searcher2;
			}
		} else {// 如果searcher2==null,则创建.
			//p.prt("S2本该有效,但发现为null,创建...");
			try {
				if (dirReader2 == null) {
			//		p.prt("r2本该有效,却==null.创建r2再创建s2...");
					dirReader2 = createDirReader(indexFile);
					searcher2 = createSearcher(dirReader2);
					return searcher2;
				} else {
					dirReader2.getContext();// ensureOpen()方法不是public的,不可用.
					// 暂时用getcontext来判断是否关闭.
				//	p.prt("r2仍然打开.从r2创建s2");
					searcher2 = createSearcher(dirReader2);
					return searcher2;
				}
			} catch (AlreadyClosedException e) {// 如果searcher已关闭,则重新创建.
			//	p.prt("r1本该有效,却已关闭.创建r1再创建s1...");
				dirReader2 = createDirReader(indexFile);
				searcher2 = createSearcher(dirReader2);
				return searcher2;
			}
		}
	}

	public static void IndexCheckTimer() {// 该方法应在tomcat启动后自动开始.目前暂时在peoplesearchresponse的servlet的init()方法启动的新线程中调用.
		while (true) {
			try {
				Thread.sleep(INTERVAL_INDEXCHECK);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// p.prt("checking index是否变化...|"+MyTimeClass.getCurrentTime());
			// lastTimeofSearcher=System.currentTimeMillis();
			try {
				swapSearcherIfChanged();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public static void swapSearcherIfChanged() throws InterruptedException {
		if (currentSearcherNo == 1) {
			try {
				dirReader2 = DirectoryReader.openIfChanged(SearchMethods.dirReader1);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			if (dirReader2 == null) {// 如果索引没变化,则无动作返回;
				// p.prt("s=1,r1索引无变化.无动作.");
				return;
			} else {// 如果索引有变化:
				p.prt("s=1,r1索引有变化,创建s2,改s=2. 等10秒后关闭s1)");
				searcher2 = createSearcher(dirReader2); // 创建另一个searcher
				currentSearcherNo = 2;// 改变currentSearcherNo,它决定下面的创建是给了s1还是给了s2.
				Thread.sleep(DELAYEDTIME_READERCLOSE);
				try {
					dirReader1.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				searcher1 = null;
				p.prt("searcher1:" + searcher1);
				p.prt("searcher2:" + searcher2);
			}
		} else {// 如果currentSearcherNo==2
			try {
				dirReader1 = DirectoryReader.openIfChanged(SearchMethods.dirReader2);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (dirReader1 == null) {// 如果索引没变化,则无动作返回;
				// p.prt("s=2,r2索引无变化.无动作.");
				return;
			} else {// 如果索引有变化:
				p.prt("s=2,r2索引有变化,创建s1,改s=1. 等10秒后关闭s2)");
				searcher1 = createSearcher(dirReader1); // 创建另一个searcher
				currentSearcherNo = 1;// 改变currentSearcherNo,它决定下面的创建是给了s1还是给了s2.
				Thread.sleep(DELAYEDTIME_READERCLOSE);
				try {
					dirReader2.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				searcher2 = null;
				p.prt("searcher1:" + searcher1);
				p.prt("searcher2:" + searcher2);
			}
		}
	}

	public static DirectoryReader createDirReader(File indexFile) {
		DirectoryReader dirReader = null;
		try {
			dirReader = DirectoryReader.open(FSDirectory.open(indexFile));
		} catch (CorruptIndexException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return dirReader;
	}

	public static IndexSearcher createSearcher(DirectoryReader dirReader) {
		IndexSearcher searcher = new IndexSearcher(dirReader);
		searcher.setSimilarity(new DefaultSimilarity());
		return searcher;
	}

	public static IndexWriter getIndexWriter(String indexPath) throws IOException, LockObtainFailedException {
		Directory dir = FSDirectory.open(new File(indexPath));
		//Analyzer analyzer = new IKAnalyzer();// StandardAnalyzer(Version.LUCENE_31);//是否要加true参数呢?
		// 2014.7.17
		IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_43, analyzer);
		// iwc.setOpenMode(OpenMode.CREATE);//重建索引，删除所有先前文档。
		iwc.setOpenMode(OpenMode.CREATE_OR_APPEND);// 接建索引。
		// increase the RAM buffer. But if you do this, increase the max heap
		// size to the JVM (eg add -Xmx512m or -Xmx1g):
		// iwc.setRAMBufferSizeMB(256.0);
		IndexWriter writer = new IndexWriter(dir, iwc);
		return writer;
	}

	public static String highLighter(String textToBeHighLighted, Query query, int numOfTextBeforeAfter, int numOfHightlightedKeywords) {
		/*
		 * QueryParser parser = new QueryParser(Version.LUCENE_43, searchField,
		 * SearchMethods.analyzer); Query query1=null; try { query1 =
		 * parser.parse("辽阳"); } catch (ParseException e2) { // TODO
		 * Auto-generated catch block e2.printStackTrace(); }
		 */

		QueryScorer scorer = new QueryScorer(query);
		Fragmenter fragmenter = new SimpleSpanFragmenter(scorer, numOfTextBeforeAfter);// 后一个参数是加亮前后的保留字数
		// Fragmenter fragmenter= new SimpleFragmenter(10);

		// SimpleHTMLFormatter formatter = new
		// SimpleHTMLFormatter("<span class=\"highlight\">","</span>");//这个需要在网页中先声明一个CSS标签.
		SimpleHTMLFormatter formatter = new SimpleHTMLFormatter("<font color=\"red\">", "</font>");

		Highlighter hig = new Highlighter(formatter, scorer);
		hig.setTextFragmenter(fragmenter);
		// TokenStream
		// tokens=TokenSources.getAnyTokenStream(searcher.getIndexReader(),sd.doc,"index_content",doc,analyzer);
		TokenStream tokens = null;
		try {
			tokens = analyzer.tokenStream(searchField, new StringReader(textToBeHighLighted));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			String highterResult = hig.getBestFragments(tokens, textToBeHighLighted, numOfHightlightedKeywords, "...");// 最多显示几个高亮关键词.
			// prt("高亮后的content: "+highterResult);
			return highterResult;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidTokenOffsetsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "content高亮处理出错.";
	}

	public static Query createQuery(String searchString) {
		// StandardAnalyzer(Version.LUCENE_31);

		// Analyzer analyzer = new Analyzer();
		// String searchField = "index_content";
		// QueryParser parser = new QueryParser(Version.LUCENE_41,
		// searchField,analyzer);
		QueryParser parser = new QueryParser(Version.LUCENE_43, searchField, analyzer);

		parser.setDefaultOperator(QueryParser.AND_OPERATOR);

		// 已将词之间的关系默认为与的关系，这个处理暂时不需要了．apr26.
		// conditioned_SearchKeywords=conditioningSearchKeywords(searchString);//对用户输入的关键词进行整理.加上双引号及加号.
		// prt("用户输入的关键词:"+searchString);
		// prt("整理之后的关键词:"+conditioned_SearchKeywords);
		Query query = null;
		try {
			query = parser.parse(searchString);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		p.prt("SearchMethods().createQuery():" + query.toString());// TODO
		return query;
	}

	/**
	 * 创建查询,解决"小南国"
	 * 
	 * @author yifabao
	 * @param searchString
	 * @param flag
	 *            该参数没有实质意义，主要表示该方法是由谁写的
	 * @return
	 */
	public static Query createQuery(String searchString, String flag) {
		// BooleanQuery booleanQuery=new BooleanQuery();
		String[] arr = searchString.split(" ");
		String field = "index_content";
		BooleanQuery booleanQuery = new BooleanQuery();

		for (int i = 0; i < arr.length; i++) {
			if (!arr[i].trim().equals("")) {
				Analyzer analyzer = new IKAnalyzer(true);
				try {

					TokenStream stream = analyzer.tokenStream(field, new StringReader(arr[i]));
					PhraseQuery phraseQuery = new PhraseQuery();
					while (stream.incrementToken()) {
						CharTermAttribute ct = stream.getAttribute(CharTermAttribute.class);
						phraseQuery.add(new Term(field, ct.toString()));
					}
					booleanQuery.add(phraseQuery, Occur.MUST);
					
					stream.close();
					analyzer.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		//p.prt("SearchMethods().createQuery()" + booleanQuery.toString());// TODO
		return booleanQuery;
	}
	
	//分词后boolean 或
/*	public static Query createOrQuery(String searchString)
	{
		String[] arr = searchString.split(" ");//后期替换成语义分析断句
		String field = "index_content";
		BooleanQuery booleanQuery = new BooleanQuery();

		for (int i = 0; i < arr.length; i++) {
			if (!arr[i].trim().equals("")) {
				Analyzer analyzer = new IKAnalyzer();
				try {
					TokenStream stream = analyzer.tokenStream("index_content", new StringReader(arr[i]));
					while (stream.incrementToken()) {
						CharTermAttribute ct = stream.getAttribute(CharTermAttribute.class);
						Query termquery=new TermQuery(new Term(field, ct.toString()));
						booleanQuery.add(termquery,Occur.SHOULD);
					}
					stream.close();
					analyzer.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return booleanQuery;
	}*/
	
	
	//带有时间范围限制的query
	public static Query createQuery(String searchString,String beginpublishDate,String endpublishDate,String flag) {

		String[] arr = searchString.split(" ");//后期替换成语义分析断句
		String field = "index_content";
		BooleanQuery booleanQuery = new BooleanQuery();

		for (int i = 0; i < arr.length; i++) {
			if (!arr[i].trim().equals("")) {
				Analyzer analyzer = new IKAnalyzer(true);
				try {

					TokenStream stream = analyzer.tokenStream(field, new StringReader(arr[i]));
					PhraseQuery phraseQuery = new PhraseQuery();
					while (stream.incrementToken()) {
						CharTermAttribute ct = stream.getAttribute(CharTermAttribute.class);
						phraseQuery.add(new Term(field, ct.toString()));
					}
					booleanQuery.add(phraseQuery, Occur.MUST);
					
					//日期范围限制
					TermRangeQuery termRangeQuery = new TermRangeQuery("index_publishdate", new BytesRef(beginpublishDate.getBytes()),new BytesRef(endpublishDate.getBytes()),false, true);
					booleanQuery.add(termRangeQuery, Occur.MUST);

					stream.close();
					analyzer.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return booleanQuery;
	}

	
	

	/**
	 * 获取更多，要与搜索更新的口径一致，只是在搜索更新的查询条件上，增加一域限制条析：网站名，作者
	 * 
	 * @param searchKeywords
	 * @param author
	 * @param site
	 * @return
	 * @author yifabao
	 */
	public static Query createQuery(String searchKeywords, String author, String site) {
		// Query
		// query_SearchPeople_Newest=SearchPeople_Newest.getQuery();//先取得搜人时的静态query.
		Query query_SearchPeople = SearchMethods.createQuery(searchKeywords, "yifabao");// 不直接取原来的query,而是重新生成.原来的query只是记忆早先的搜索,会引起后续搜索的混乱,尤其是getmore时.
		// 2014.6.16

		// QueryParser parser_author = new QueryParser(Version.LUCENE_43,
		// "index_author",SearchPeople_Relevant.getAnalyzer());
		// Query query_author = parser_author.parse(author);
		// QueryParser parser_site = new QueryParser(Version.LUCENE_43,
		// "index_site",SearchPeople_Relevant.getAnalyzer());
		// Query query_site =
		// parser_site.parse(site);//分别针对author和site创建两个query.

		Query query_author = new TermQuery(new Term("index_author", author));// 上面的方法会对名字进行解析.所以改用TermQuery.
		Query query_site = new TermQuery(new Term("index_sitename", site));
		query_author.setBoost(0f);
		query_site.setBoost(0f);

		BooleanQuery query = new BooleanQuery();// 创建一个空的booleanquery,用于把三个query合并起来.
		query.add(query_SearchPeople, BooleanClause.Occur.valueOf("MUST"));// lucene43的add方法有改变.
		query.add(query_author, BooleanClause.Occur.valueOf("MUST"));
		query.add(query_site, BooleanClause.Occur.valueOf("MUST"));
		return query;
	}
	
	//加了时间范围，相应搜索更多也要加上时间范围
	public static Query createQuery(String searchKeywords, String author, String site,String beginpublishDate,String endpublishDate) {
		// Query
		// query_SearchPeople_Newest=SearchPeople_Newest.getQuery();//先取得搜人时的静态query.
		Query query_SearchPeople = SearchMethods.createQuery(searchKeywords, "yifabao");// 不直接取原来的query,而是重新生成.原来的query只是记忆早先的搜索,会引起后续搜索的混乱,尤其是getmore时.
		// 2014.6.16

		// QueryParser parser_author = new QueryParser(Version.LUCENE_43,
		// "index_author",SearchPeople_Relevant.getAnalyzer());
		// Query query_author = parser_author.parse(author);
		// QueryParser parser_site = new QueryParser(Version.LUCENE_43,
		// "index_site",SearchPeople_Relevant.getAnalyzer());
		// Query query_site =
		// parser_site.parse(site);//分别针对author和site创建两个query.

		Query query_author = new TermQuery(new Term("index_author", author));// 上面的方法会对名字进行解析.所以改用TermQuery.
		Query query_site = new TermQuery(new Term("index_sitename", site));
		query_author.setBoost(0f);
		query_site.setBoost(0f);

		BooleanQuery query = new BooleanQuery();// 创建一个空的booleanquery,用于把三个query合并起来.
		query.add(query_SearchPeople, BooleanClause.Occur.valueOf("MUST"));// lucene43的add方法有改变.
		query.add(query_author, BooleanClause.Occur.valueOf("MUST"));
		query.add(query_site, BooleanClause.Occur.valueOf("MUST"));
		
		//日期范围限制
		TermRangeQuery termRangeQuery = new TermRangeQuery("index_publishdate", new BytesRef(beginpublishDate.getBytes()),new BytesRef(endpublishDate.getBytes()),false, true);
		query.add(termRangeQuery, Occur.MUST);
		return query;
	}


	/**
	 * 查询某作者的总贴子数,头贴数,跟贴数
	 * 
	 * @author yifabao
	 * @param author
	 * @param site
	 * @return Query对象
	 */
	public static Query createEvaluationQuery(String author, String site) {
		BooleanQuery booleanQuery = new BooleanQuery();
		Query query_author = new TermQuery(new Term("index_author", author));
		Query query_site = new TermQuery(new Term("index_sitename", site));
		booleanQuery.add(query_author, BooleanClause.Occur.valueOf("MUST"));
		booleanQuery.add(query_site, BooleanClause.Occur.valueOf("MUST"));
		return booleanQuery;
	}

	public static Query createQuery_Author(String queryString, String author, String site) throws ParseException {
		// Query
		// query_SearchPeople_Newest=SearchPeople_Newest.getQuery();//先取得搜人时的静态query.
		Query query_SearchPeople = SearchMethods.createQuery(queryString);// 不直接取原来的query,而是重新生成.原来的query只是记忆早先的搜索,会引起后续搜索的混乱,尤其是getmore时.
		// 2014.6.16

		// QueryParser parser_author = new QueryParser(Version.LUCENE_43,
		// "index_author",SearchPeople_Relevant.getAnalyzer());
		// Query query_author = parser_author.parse(author);
		// QueryParser parser_site = new QueryParser(Version.LUCENE_43,
		// "index_site",SearchPeople_Relevant.getAnalyzer());
		// Query query_site =
		// parser_site.parse(site);//分别针对author和site创建两个query.

		Query query_author = new TermQuery(new Term("index_author", author));// 上面的方法会对名字进行解析.所以改用TermQuery.
		Query query_site = new TermQuery(new Term("index_sitename", site));

		BooleanQuery query = new BooleanQuery();// 创建一个空的booleanquery,用于把三个query合并起来.
		query.add(query_SearchPeople, BooleanClause.Occur.valueOf("MUST"));// lucene43的add方法有改变.
		query.add(query_author, BooleanClause.Occur.valueOf("MUST"));
		query.add(query_site, BooleanClause.Occur.valueOf("MUST"));

		//p.prt("SearchMethods().createQuery_Author(): " + query.toString());// TODO
		return query;
	}

	public static Query createQuery_Author_All(String author, String site) throws ParseException {
		Query query_author = new TermQuery(new Term("index_author", author));// 上面的方法会对名字进行解析.所以改用TermQuery.
		Query query_site = new TermQuery(new Term("index_sitename", site));

		BooleanQuery query = new BooleanQuery();// 创建一个空的booleanquery,用于把三个query合并起来.
		query.add(query_author, BooleanClause.Occur.valueOf("MUST"));
		query.add(query_site, BooleanClause.Occur.valueOf("MUST"));

		//p.prt("SearchMethods().createQuery_Author()_All:" + query.toString());// TODO
		return query;
	}

	// 利用自定义的collector实现一次搜索:
	// 2014.6.14为了和searchpeople_newest中的同名方法一致,增加一个searcher参数.//static Collector
	// search2C(Query query,Collector collector) throws ParseException,
	// Exception,IOException {
	public static Collector search2C(IndexSearcher searcher, Query query, Collector collector) throws ParseException, Exception, IOException {

		/*
		 * boolean cacheScores = true; double maxCacheRAMMB = 4.0;
		 * CachingCollector cachedCollector =
		 * CachingCollector.create(collector_xu, cacheScores, maxCacheRAMMB);
		 * try { searcher.search(query, cachedCollector); } catch (IOException
		 * e) { // TODO Auto-generated catch block e.printStackTrace(); }
		 */

	//	p.prt("searchmethods-search2c-searcher:" + searcher + "|query:" + query + "|collector:" + collector);

		searcher.search(query, collector);// 执行了上面的cachedCollector,collector_xu就已经被调用过一遍了.
		// searcher.search(new TermQuery(new Term("index_content",
		// "ublox")),collector_xu);

		return collector;
	}
	


	// 在Query中设置了默认为与的查询,下面这个方法就不用了:
	public static String conditioningSearchKeywords(String searchString) {
		String[] splitKeywords = searchString.split(" +");// 把用户输入的关键词字串按(连续)空格拆开.
		String conditionedKeywords = "";// 先定义一个空字串,用于下面循环中的串接.
		for (int i = 0; i < splitKeywords.length; i++) {// 遍历拆分后的关键词数组.
			// splitKeywords[i]="+\""+splitKeywords[i]+"\"";//前后都加上双引号,前面加一加号.
			conditionedKeywords = conditionedKeywords + "+" + splitKeywords[i] + " ";// 串接起来.//只加加号,不加双引了.加双引有时搜不出结果?.
		}
		return conditionedKeywords;
	}

	void happyBirthday2Xiangyi(HttpServletResponse response, PeopleSearchVars sV) {
		if (sV.searchKeywords.contains("徐相宜") || sV.searchKeywords.contains("xiangyi")) {
			// pSearchResultPage="http:\\/\\/www.zhufucn.cn\\/zf\\/birthday\\/happybirthday.asp?send=%u7238%u7238&rece=%u5F90%u76F8%u5B9C";
			try {
				response.sendRedirect("http://www.zhufucn.cn/zf/birthday/happybirthday.asp?send=%u7238%u7238&rece=%u5F90%u76F8%u5B9C");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	// 遍历请求中的所有信息,只用于测试:
	public static void printRequestInfo(HttpServletRequest request) {
		// 遍历HTTP请求中所有头信息
		System.out.println("遍历HTTP请求中的所有头信息:");
		Enumeration<String> enumeration1 = request.getHeaderNames();
		while (enumeration1.hasMoreElements()) {
			String key = enumeration1.nextElement();
			// System.out.println("头信息的key:" + key);
			Enumeration<String> enumeration2 = request.getHeaders(key);
			while (enumeration2.hasMoreElements()) {
				String value = enumeration2.nextElement();
				System.out.println(key + ":" + value);
			}
		}
		System.out.println("遍历页面请求中的所有参数信息:");
		Enumeration pNames = request.getParameterNames();// 获取所有参数数据.
		for (; pNames.hasMoreElements();) {
			String pName = (String) pNames.nextElement();
			System.out.println(pName + "=" + request.getParameter(pName));
		}
		// System.out.println("--遍历结束 -遍历HTTP所有信息--");
	}
	
	public static void main(String[] args) throws IOException {
		String ss="动物园";
		IKAnalyzer analyzer=new IKAnalyzer(true);
		TokenStream stream = analyzer.tokenStream("ss",new StringReader(ss));
		while(stream.incrementToken())
		{
			System.out.println(stream.getAttribute(CharTermAttribute.class));
		}
		
		
	}

}
