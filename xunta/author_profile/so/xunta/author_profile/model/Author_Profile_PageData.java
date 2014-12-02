package so.xunta.author_profile.model;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.lucene.search.IndexSearcher;

import com.aigine.common.*;

import so.xunta.people.newest.AuthorInfo_Newest;
import so.xunta.people.newest.DocData_Newest;
import so.xunta.people.newest.SearchPeople_Newest;

public class Author_Profile_PageData {
		public String title;//html页面的title.
		public String basePath="localhost:8888\\/";//一些链接的bathPath;
		
		//int	docLines_perAuthor=5;//每页显示的文档数.
		//public int totalPageCount;//全部页数;
		public int totalDocs;//全部文档数.
		public int getTotalDocs() {
			return totalDocs;
		}


		//int currentPageNo;//当前页的页号.
		//List<String> PageNoList=new ArrayList<String>();//用于准备 底部翻页数据.
		public String searchMode=new String();
		public String searchKeywords=new String();//保留搜索关键词,显示于搜索结果页面的搜索框里.
		public String searchKeywords_urlencoded=new String();//保留搜索关键词,显示于搜索结果页面的搜索框里.
		public String author=new String();
		public String site=new String();
		//public String searchResultFileIncluded=new String();
		//IndexSearcher indexSearcher;
		
		//public String getSearchResultFileIncluded() {
			//return searchResultFileIncluded;
		//}


		public Author_Profile_PageData(String searchMode,String searchKeywords,String author,String site) throws IOException {//构造时即生成所需的数据.
			this.searchMode=searchMode;
			this.searchKeywords=searchKeywords;
			this.author=author;
			this.site=site; 
			
			//this.totalDocs=SearchPeople_Newest.totalDocs;//总作者数.
			//this.totalAuthor=SearchPeople_Newest.totalAuthors;//总作者数.
			//this.totalPageCount=this.totalAuthor/pageListLength+1;//总页面数.
			//this.currentPageNo=currentPageNo;//请求时传递过来的当前页号.
			
			

			
			//SearchPeople_Newest.reader.close();//2013.6.5:dont close it anymore.
			//prt("PageData()-reader被关闭."); 
		}
		

		public String getTitle() {
			return title;
		}


		public String getBasePath() {
			return basePath;
		}


		public String getSearchMode() {
			return searchMode;
		}


		public String getSearchKeywords_urlencoded() {
			return searchKeywords_urlencoded;
		}


		public String getAuthor() {
			return author;
		}


		public String getSite() {
			return site;
		}


		public String getSearchKeywords() {
			return searchKeywords;
		}


		public static void prt(Object o){System.out.println(o);}

	
	
	
}
