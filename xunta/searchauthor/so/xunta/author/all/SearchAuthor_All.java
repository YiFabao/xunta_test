package so.xunta.author.all;

import java.io.IOException;
import org.apache.lucene.search.Collector;

import so.xunta.author.relevant.Collector_Author_Relevant;
import so.xunta.response.AuthorProfileVars;
import so.xunta.response.SearchMethods;

public class SearchAuthor_All {

	//通过自定义collector实现的搜索:
	public static void searchAuthor(AuthorProfileVars apV) throws IOException, Exception{
		apV.query=SearchMethods.createQuery_Author_All(apV.author,apV.site);//应该使用与SearchPeople_Relevant.creatQuery()中一样的query创建方法，但不知怎么在上面叠加author与site的附加条件。
																//暂时创建一个专用的，但基本方法须与SearchPeople_Relevant中的方法保持一致。
		//生成collector
		Collector collector_author_all=new Collector_Author_All();//这个collector不能设为静态属性,否则出现底层错误.
		((Collector_Author_All) collector_author_all).clear();//为什么要先用这个clear? 忘了.这个clear是我定义的,将一些变量清空.//哦,因为这些变量是静态的,new一下不改变它们的值.		
		collector_author_all=SearchMethods.search2C(apV.searcher,apV.query,collector_author_all);//采用搜人时所使用的相同collector搜索调用，但collector重写方法是不同的。
		apV.totalDocs=((Collector_Author_All) collector_author_all).getTotalDocs();//2014.6.14
		apV.docdata_set_all=((Collector_Author_All) collector_author_all).docdata_set;
	}
}//end of class GroupingTest_Collector_Xu


