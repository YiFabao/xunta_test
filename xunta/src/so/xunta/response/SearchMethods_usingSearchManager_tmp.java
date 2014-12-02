package so.xunta.response;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.Collector;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.SearcherFactory;
import org.apache.lucene.search.SearcherManager;
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
import org.apache.lucene.util.Version;
import org.wltea.analyzer.lucene.IKAnalyzer;
import com.aigine.common.p;
import so.xunta.localcontext.LocalContext;

//与搜索相关的标准方法都放在这里.所有方法都是静态的,以节省空间. 几个不变的搜索变量也放在这里. 如果变量与某次访问有关, 则放在SearchVars中, 如query.
public class SearchMethods_usingSearchManager_tmp {
//	static Similarity similarity=new DefaultSimilarity(); //在creatSearcher里直接引用。
//	static Similarity similarity=new Similarity_Xu(); 
//	static Similarity similarity=new BM25Similarity(); 
//	static Similarity similarity=new LMDirichletSimilarity(); 
	// "\\\\192.168.1.103\\e$\\home\\lucene\\bbs_index_single";
	
	public static IndexReader reader1;
	public static DirectoryReader dirReader1;
	public static IndexSearcher searcher1;
	public static IndexReader reader2;
	public static DirectoryReader dirReader2;
	public static IndexSearcher searcher2;
	public static IndexSearcher searcher_tmp;
	public static byte currentSearcherNo=1;//等于1或2. //上面这些变量用于分别创建两套reader和searcher,以方便分别关闭和创建.2014.7.22
	
	public static Analyzer analyzer = new IKAnalyzer();//无参或为false时，为最细粒度切分．true时，为智能切分．
														//2014.7.17建索引时,有true参数,表示为智能分词.但搜索时不要加,否则大词包小词时则不会高亮显示,如搜辽阳时,辽阳市和辽阳县会不显.
	static String searchField = "index_content";								
	public static SearcherManager searcherManager = null;
	static File indexFile = new File(LocalContext.indexFilePath);
	static SearcherFactory searcherFactory = new SearcherFactory();

	static long lastTimeofSearcher = System.currentTimeMillis();//上次创建searcher的时间.
	static int intervalOfIndexUpdate = 15000;//检查索引并重建searcher的间隔,单位ms. 
	
	//searcherManager = new SearcherManager(FSDirectory.open(indexFile) , searcherFactory);
		
		//dirReader = reader.open(FSDirectory.open(indexFile)); 
		//((DirectoryReader) reader).isCurrent();
		//DirectoryReader.openIfChanged((DirectoryReader) reader);//当
		//一个查询发生时, 如果reader是closed的,则打开,并生成searcher;
		//每个查询结束后, 看是否过了一分钟, 如果超了,则启用 另一个高优先级的线程-重新打开reader及searcher并临时赋值给另一个变量暂存.
											//关闭原来的searcher(用searchermanager生成及关闭,这个关闭是否等待线程结束?,但不结束也无所谓, 下一步就直接启用新reader了.)
		//判断是否关闭,如果关闭了,则将暂存的新reader/searcher赋给原来的reader/searcher.  是否关闭这个判断要测试一下.
		//用两个reader,并用一个开关变量表示当前使用的reader. 当打开新的searcher时,马上转变开关,新的搜索就将使用新searcher. 然后再关闭旧的searcher. 
		//下面的几个方法是searcher1和searcher2通用的, 用currentSearcherNo这个开关决定对哪个searcher进行操作.//2014.7.23
	
	public static IndexSearcher getOneSearcherofTwo(){
			return createSearcherIfNullorClosed();
	}
	
	static IndexSearcher createSearcherIfNullorClosed(){
		p.prt("searcher1="+searcher1);
		p.prt("searcher2="+searcher2);
		if (currentSearcherNo==1){//用这个开关量来决定返回哪个searcher, 不再用形参来决定.
			searcher_tmp=searcher1;
			p.prt("为S1...");
		}else{
			searcher_tmp=searcher2;
			p.prt("为S2...");
		}
		if(searcher_tmp!=null){
			p.prt("s有效.");
			try{
				searcher_tmp.getIndexReader().getContext();//ensureOpen()方法不是public的,不可用. 暂时用getcontext来判断是否关闭. 
				p.prt("s没有关闭.直接返回s");
				return searcher_tmp;
			}catch (AlreadyClosedException e) {//如果searcher已关闭,则重新创建.
				p.prt("s已关闭.重新创建");
				return createSearcher_BySearcherManager();
			}
		}else{//如果searcher==null,则创建.
			p.prt("s无效,再创建");
			return createSearcher_BySearcherManager();
		}
	}
	
	static IndexSearcher createSearcher_BySearcherManager(){
		if (searcherManager==null){
			p.prt("smanager=null,new了一下.");
			newSearcherManager(); 
		}
		
		try {
			p.prt("acquire出一个s");
			searcher_tmp = searcherManager.acquire();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (currentSearcherNo==1){//用这个开关量来决定返回哪个searcher, 不再用形参来决定.
			searcher1=searcher_tmp;
			p.prt("指针给了S1...");
		}else{
			searcher2=searcher_tmp;
			p.prt("指针给了S2...");
		}
		//reader = searcher.getIndexReader();//前面没有用到reader, 这里反过来从searcher获得一个.
		//dirReader = (DirectoryReader) reader;
		//reader = dirReader;		
		p.prt("searcher1:" + searcher1);
		p.prt("searcher2:" + searcher2);
		return searcher_tmp;
	}

	static SearcherManager newSearcherManager(){
		try {
			//searcherManager = new SearcherManager(FSDirectory.open(indexFile) , searcherFactory);//An optional SearcherFactory. Pass null if you don't require the searcher to be warmed before going live or other custom behavior.
			searcherManager = new SearcherManager(FSDirectory.open(indexFile) , null);//An optional SearcherFactory. Pass null if you don't require the searcher to be warmed before going live or other custom behavior.
		p.prt("创建后的searcherManager="+searcherManager);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		//reader1.
	//return searcherManager;
		return searcherManager;
	}
	
	
	public static void trySwapSearcher(){
		if (System.currentTimeMillis()-lastTimeofSearcher > intervalOfIndexUpdate){   //时间到了否?
			p.prt("s更新到时间了");
			lastTimeofSearcher=System.currentTimeMillis();
			try {
				if (!searcherManager.isSearcherCurrent()){	//发生变化了吗?
					p.prt("s有变化");
					if (currentSearcherNo==1){
						p.prt("s=1, 创建s2, 改s=2,再release(s1)");
						currentSearcherNo=2;//改变currentSearcherNo,它决定下面的创建是给了s1还是给了s2. 
						createSearcher_BySearcherManager(); //创建另一个searcher
						searcherManager.close();
						searcherManager.release(searcher1);//关掉原来的searcher;
						searcher1=null;
						p.prt("searcher1:" + searcher1);
						p.prt("searcher2:" + searcher2);
					}else{
						p.prt("s=2, 创建s1, 改s=1,再release(s2)");
						currentSearcherNo=1;//改变currentSearcherNo,它决定下面的创建是给了s1还是给了s2. 
						createSearcher_BySearcherManager(); //创建另一个searcher
						searcherManager.close();
						searcherManager.release(searcher2);//关掉原来的searcher;
						searcher2=null;
						p.prt("searcher1:" + searcher1);
						p.prt("searcher2:" + searcher2);
					}
				}else{
					p.prt("s无变化,无动作.");
					p.prt("searcher1="+searcher1);
					p.prt("searcher2="+searcher2);
					
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		}else{
			p.prt("trySwapSearcher():s更新时间没到.");

		}
	}
	

	

	

	


}
