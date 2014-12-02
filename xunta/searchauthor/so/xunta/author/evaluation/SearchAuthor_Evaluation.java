package so.xunta.author.evaluation;

import java.io.IOException;
import java.text.DecimalFormat;

import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.Collector;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.json.JSONObject;

import so.xunta.response.SearchMethods;

public class SearchAuthor_Evaluation {
	public static JSONObject searchAuthor(String author,String site) throws ParseException, IOException, Exception
	{
		//生成query
		Query query=SearchMethods.createEvaluationQuery(author,site);
		// 生成collector
		Collector collector_Author_Evaluation=new Collector_Author_Evaluation();
		//获得searcher
		IndexSearcher searcher=SearchMethods.getOneSearcherofTwo();
		
		collector_Author_Evaluation = SearchMethods.search2C(searcher,query, collector_Author_Evaluation);
		
		//总文档数
		double totalDocs = ((Collector_Author_Evaluation) collector_Author_Evaluation).getTotalDocs();
		//重复贴
		double repeatPost=((Collector_Author_Evaluation) collector_Author_Evaluation).getRepeatPost();
		//头贴数
		double firstPost=((Collector_Author_Evaluation) collector_Author_Evaluation).getRepeatPost();
		//跟贴数
		double comment=totalDocs-firstPost;
		//System.out.println("重复贴："+repeatPost+"\t"+"头贴数:"+firstPost+"\t跟贴数:"+comment+"\t总贴子数:"+totalDocs);
		DecimalFormat df  = new DecimalFormat("######0.0000");   
		
		//原创度
		double source_degree=Math.atan(Math.pow(firstPost, 2)/totalDocs)*2/Math.PI;
		//灌水度
		double warter_degree=Math.atan(Math.pow(repeatPost, 2)/totalDocs)*2/Math.PI;
		//参与度
		double participation_degree=Math.atan(Math.pow(comment, 2)/totalDocs)*2/Math.PI;
		
		System.out.println(firstPost+"|"+repeatPost+"|"+comment+"原创度："+df.format(source_degree)+"\t\t灌水度："+df.format(warter_degree)+"\t\t参与度:"+df.format(participation_degree));
		
		
		JSONObject jsonObj=new JSONObject();
		jsonObj.append("original_degree",df.format(source_degree))
				.append("participation_degree",df.format(participation_degree))
				.append("water_degree",df.format(warter_degree));
		return jsonObj;
		
	}
}
