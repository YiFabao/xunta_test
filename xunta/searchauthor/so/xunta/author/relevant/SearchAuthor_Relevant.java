package so.xunta.author.relevant;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.lucene.search.Collector;
import so.xunta.response.AuthorProfileVars;
import so.xunta.response.SearchMethods;

public class SearchAuthor_Relevant {
	
	//通过自定义collector实现的搜索:
	public static void searchAuthor(AuthorProfileVars apV) throws IOException, Exception{
		//apV.query=SearchMethods.createQuery_Author(apV.searchKeywords,apV.author,apV.site);//应该使用与SearchPeople_Relevant.creatQuery()中一样的query创建方法，但不知怎么在上面叠加author与site的附加条件。
																//暂时创建一个专用的，但基本方法须与SearchPeople_Relevant中的方法保持一致。
		
		
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

/*		//生成collector
		Collector collector_author_relevant=new Collector_Author_Relevant();//这个collector不能设为静态属性,否则出现底层错误.
		((Collector_Author_Relevant) collector_author_relevant).clear();//为什么要先用这个clear? 忘了.这个clear是我定义的,将一些变量清空.//哦,因为这些变量是静态的,new一下不改变它们的值.		
		collector_author_relevant=SearchMethods.search2C(apV.searcher,apV.query,collector_author_relevant);//采用搜人时所使用的相同collector搜索调用，但collector重写方法是不同的。
		//SearchPeople_Relevant.searchKeywords=queryString;//2014.6.16将搜索词统一同步到searchpeople中,后面highlighter中会用到这个变量.如果不同步,要么会报
		apV.totalDocs=((Collector_Author_Relevant)collector_author_relevant).getTotalDocs();//2014.6.14
		apV.docdata_set_relevant=((Collector_Author_Relevant) collector_author_relevant).docdata_set;*/
		
		//生成collector
		Collector collector_author_relevant=new Collector_Author_Relevant_yi();//这个collector不能设为静态属性,否则出现底层错误.
		collector_author_relevant=SearchMethods.search2C(apV.searcher,apV.query,collector_author_relevant);//采用搜人时所使用的相同collector搜索调用，但collector重写方法是不同的。
		//SearchPeople_Relevant.searchKeywords=queryString;//2014.6.16将搜索词统一同步到searchpeople中,后面highlighter中会用到这个变量.如果不同步,要么会报
		apV.totalDocs=((Collector_Author_Relevant_yi)collector_author_relevant).getTotalDocs();//2014.6.14
		apV.docdata_set_relevant=((Collector_Author_Relevant_yi) collector_author_relevant).docdata_set;
	}
	
	//作者个人主页
	public static void searchAuthorAll(AuthorProfileVars apV) throws IOException, Exception{
		//apV.query=SearchMethods.createQuery_Author(apV.searchKeywords,apV.author,apV.site);//应该使用与SearchPeople_Relevant.creatQuery()中一样的query创建方法，但不知怎么在上面叠加author与site的附加条件。
																//暂时创建一个专用的，但基本方法须与SearchPeople_Relevant中的方法保持一致。
		
		
		apV.query=SearchMethods.createQuery(apV.searchKeywords, apV.author, apV.site);

/*		//生成collector
		Collector collector_author_relevant=new Collector_Author_Relevant();//这个collector不能设为静态属性,否则出现底层错误.
		((Collector_Author_Relevant) collector_author_relevant).clear();//为什么要先用这个clear? 忘了.这个clear是我定义的,将一些变量清空.//哦,因为这些变量是静态的,new一下不改变它们的值.		
		collector_author_relevant=SearchMethods.search2C(apV.searcher,apV.query,collector_author_relevant);//采用搜人时所使用的相同collector搜索调用，但collector重写方法是不同的。
		//SearchPeople_Relevant.searchKeywords=queryString;//2014.6.16将搜索词统一同步到searchpeople中,后面highlighter中会用到这个变量.如果不同步,要么会报
		apV.totalDocs=((Collector_Author_Relevant)collector_author_relevant).getTotalDocs();//2014.6.14
		apV.docdata_set_relevant=((Collector_Author_Relevant) collector_author_relevant).docdata_set;*/
		
		//生成collector
		Collector collector_author_relevant=new Collector_Author_Relevant_yi();//这个collector不能设为静态属性,否则出现底层错误.
		collector_author_relevant=SearchMethods.search2C(apV.searcher,apV.query,collector_author_relevant);//采用搜人时所使用的相同collector搜索调用，但collector重写方法是不同的。
		//SearchPeople_Relevant.searchKeywords=queryString;//2014.6.16将搜索词统一同步到searchpeople中,后面highlighter中会用到这个变量.如果不同步,要么会报
		apV.totalDocs=((Collector_Author_Relevant_yi)collector_author_relevant).getTotalDocs();//2014.6.14
		apV.docdata_set_relevant=((Collector_Author_Relevant_yi) collector_author_relevant).docdata_set;
	}
	
}