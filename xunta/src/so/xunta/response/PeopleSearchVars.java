package so.xunta.response;

import java.util.Calendar;
import java.util.TreeSet;

import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;

import so.xunta.people.newest.AuthorInfo_Newest;
import so.xunta.people.newest.PageData_Newest;
import so.xunta.people.relevant.AuthorInfo_Relevant;
import so.xunta.people.relevant.PageData_Relevant;

public class PeopleSearchVars {
	
	public IndexReader reader;
	public DirectoryReader dirReader;
	public IndexSearcher searcher;//这几个动态变量用于分别指向SearchMethods中的两个reader和searcher,以方便分别关闭和创建.2014.7.22
	
	public String searchKeywords;
	public String searchMode;
	public String pageNo;
	public String IP;
	public long beginTime;
	
	public Query query;
	
	public int	totalDocs;//存放搜索结果的全部文档数.
	public int totalAuthors;//存放搜索结果的全部作者数.
	
	public TreeSet<AuthorInfo_Newest> authorRanking_Newest = new TreeSet<AuthorInfo_Newest>();//new一下,否则在第一次清空时报错.//searcharthor的数据容器.顺序是按总得分数排序的.重写了AuthorInfo的比较大小的方法.
	public TreeSet<AuthorInfo_Relevant> authorRanking_Relevant = new TreeSet<AuthorInfo_Relevant>();;//new一下,否则在第一次清空时报错.//searcharthor的数据容器.顺序是按总得分数排序的.重写了AuthorInfo的比较大小的方法.
	
	public PageData_Newest pageData_newest;
	public PageData_Relevant pageData_relavent;
	
	public String timepoint;//搜索最旧的时间点

	
}
