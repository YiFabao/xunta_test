package so.xunta.people.newest;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.lucene.index.Term;
import org.apache.lucene.search.Collector;
import org.apache.lucene.search.TermQuery;

import so.xunta.response.PeopleSearchVars;
import so.xunta.response.SearchMethods;

public class SearchPeopSpecify {
	// 通过自定义collector实现的搜索:
		public static void searchPeople(PeopleSearchVars sV) throws IOException, Exception {
		
			Map<String, AuthorInfo_Newest> map = new HashMap<String, AuthorInfo_Newest>();
	
			// 生成collector
			Collector collector_newest = new Collector_Newest();	
			//创建一个只根据url查询的query
			sV.query =new TermQuery(new Term("index_url",sV.post_url));
			collector_newest = SearchMethods.search2C(sV.searcher, sV.query, collector_newest);
			sV.totalDocs = ((Collector_Newest) collector_newest).getTotalDocs();// 总的文档数
			sV.totalAuthors = ((Collector_Newest) collector_newest).getTotalAuthors();// 总的作者数

			map.putAll(((Collector_Newest) collector_newest).getAuthorScoreMap());

			for (Map.Entry<String, AuthorInfo_Newest> authorId_Info : map.entrySet()) {
				sV.authorRanking_Newest.add(authorId_Info.getValue());
			}

		}
}
