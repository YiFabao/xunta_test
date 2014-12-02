package so.xunta.response;

import java.util.TreeSet;

import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;

import so.xunta.author.all.DocData_All_GetMore;
import so.xunta.author.newest.DocData_Newest_GetMore;
import so.xunta.author.relevant.DocData_Relevant_GetMore;
import so.xunta.people.newest.AuthorInfo_Newest;
import so.xunta.people.newest.PageData_Newest;
import so.xunta.people.relevant.AuthorInfo_Relevant;
import so.xunta.people.relevant.PageData_Relevant;

public class GetMoreVars {

	public IndexReader reader;
	public DirectoryReader dirReader;
	public IndexSearcher searcher;//这几个动态变量用于分别指向SearchMethods中的两个reader和searcher,以方便分别关闭和创建.2014.7.22
	
	public String searchKeywords;;//从页面传来过的用户搜索字串.在searchPeople中,这个变量用的是searchKeywords,不够恰当.
	public String searchMode;//从页面传过来的,指示  最新 or 最相关.
	
	public String pageNo;
	public String IP;
	public long beginTime;
	
	public Query query;
	
	public String author;//当前作者名。
	public String site;//当前作者所在的网站名。
	public int	totalDocs;//存放搜索结果的全部文档数.

	public DocData_Relevant_GetMore getMore_docsdata_relevant;//获取 更多文档  数据的java处理代码的对象-最相关.
	public DocData_Newest_GetMore getMore_docsdata_newest;////获取 更多文档  数据的java处理代码的对象-最新.
	public DocData_All_GetMore getMore_docsdata_all;////获取 更多文档  数据的java处理代码的对象-最新.
	
	//int thisAuthorRank;//"更多"点击操作时,所在楼层的作者在当面页上的排序序号.//由于作全新搜索,这个参数不需要了,暂留.
	public int docNumShown;//该作者已显示在页面上的楼层数.新索取的文档应该从这个序号加1后开始算.
	public int moreDocsFetchNum;//每次增加的文档数量.//这个变量移动docs_data_newest里了.
	
	
	

	
	
}
