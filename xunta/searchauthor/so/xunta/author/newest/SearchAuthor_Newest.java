package so.xunta.author.newest;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.lucene.search.Collector;

import so.xunta.response.AuthorProfileVars;
import so.xunta.response.SearchMethods;

public class SearchAuthor_Newest {
	// 下面的静态变量供搜索过程全程使用:
	// static String indexDir = SearchPeople_Newest.getIndexDir();
	// public static IndexReader reader =
	// null;这里的reader没用上.直接通过searcher创建在SearchPeople_Relevant里了.
	// static IndexSearcher searcher;
	// static Query query;
	// public static int totalDocs;//存放搜索结果的全部文档数.
	// static String queryString;
	// static String author;
	// static String site;

	// 通过自定义collector实现的搜索:
	public static void searchAuthor(AuthorProfileVars apV) throws IOException, Exception {
		//apV.query = SearchMethods.createQuery_Author(apV.searchKeywords, apV.author, apV.site);// 应该使用与SearchPeople_Relevant.creatQuery()中一样的query创建方法，但不知怎么在上面叠加author与site的附加条件。
		if(apV.timepoint==null||"".equals(apV.timepoint))
		{
			apV.query=SearchMethods.createQuery(apV.searchKeywords, apV.author, apV.site);
		}
		else
		{
			Date date=new Date();
			SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd");
			String endpublishDate=sdf.format(date);
			apV.query=SearchMethods.createQuery(apV.searchKeywords, apV.author, apV.site,apV.timepoint,endpublishDate);
		}
		
		// 暂时创建一个专用的，但基本方法须与SearchPeople_Relevant中的方法保持一致。
		// 生成collector
		Collector collector_author_newest = new Collector_Author_Newest();// 这个collector不能设为静态属性,否则出现底层错误.
		collector_author_newest = SearchMethods.search2C(apV.searcher, apV.query, collector_author_newest);// 采用搜人时所使用的相同collector搜索调用，但collector重写方法是不同的。
		apV.totalDocs = ((Collector_Author_Newest) collector_author_newest).getTotalDocs();// 2014.6.14
		apV.docdata_set_newest = ((Collector_Author_Newest) collector_author_newest).docdata_set;
		//apV.query=SearchMethods.createQuery(apV.searchKeywords, apV.author, apV.site);
	}
	
	//作者个人主页
	public static void searchAuthorAll(AuthorProfileVars apV) throws IOException, Exception {
		//apV.query = SearchMethods.createQuery_Author(apV.searchKeywords, apV.author, apV.site);// 应该使用与SearchPeople_Relevant.creatQuery()中一样的query创建方法，但不知怎么在上面叠加author与site的附加条件。
		
		apV.query=SearchMethods.createQuery(apV.searchKeywords, apV.author, apV.site);//创建query ，只是在搜索更新的query对象上，增加了两个域限制条件：网站名和作者名
		// 暂时创建一个专用的，但基本方法须与SearchPeople_Relevant中的方法保持一致。
		// 生成collector
		Collector collector_author_newest = new Collector_Author_Newest();// 这个collector不能设为静态属性,否则出现底层错误.

		//((Collector_Author_Newest) collector_author_newest).clear();// 为什么要先用这个clear?
		// 忘了.这个clear是我定义的,将一些变量清空.//哦,因为这些变量是静态的,new一下不改变它们的值.
		collector_author_newest = SearchMethods.search2C(apV.searcher, apV.query, collector_author_newest);// 采用搜人时所使用的相同collector搜索调用，但collector重写方法是不同的。
		apV.totalDocs = ((Collector_Author_Newest) collector_author_newest).getTotalDocs();// 2014.6.14

		apV.docdata_set_newest = ((Collector_Author_Newest) collector_author_newest).docdata_set;
		//apV.query=SearchMethods.createQuery(apV.searchKeywords, apV.author, apV.site);
	}

	static void prt(Object o) {
		System.out.println(o);
	}

}// end of class GroupingTest_Collector_Xu

