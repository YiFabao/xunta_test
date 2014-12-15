package so.xunta.topic;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.highlight.Fragmenter;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.search.highlight.SimpleSpanFragmenter;
import org.wltea.analyzer.lucene.IKAnalyzer;

import so.xunta.response.SearchMethods;

public class TopicManagerImpl implements TopicManager {
	IndexSearcher searcher=SearchMethods.getOneSearcherofTwo();
	static Analyzer analyzer = new IKAnalyzer();// IK分词器

	@Override
	public  List<Topic> matchMyTopic(String mytopic) {
		List<Topic> topicList=new ArrayList<>();
		try {
			List<String> q=showTerms(mytopic,analyzer);
			Map<String,Thesuraus> map=FileUtil.getInstance().map;
			BooleanQuery query=new BooleanQuery();
			for(String t:q)
			{
				
				//1.获取每个词的同义词
				Thesuraus thesuraus=map.get(t);
				//获取每个词的词关系
				if(thesuraus!=null)
				{
					List<String> list=thesuraus.theseuras;
					TermQuery tq1=new TermQuery(new Term("topic",t));
			
					BooleanQuery booleanQuery=new BooleanQuery();
					booleanQuery.add(tq1, Occur.SHOULD);
					for(String s:list)
					{
						TermQuery tq2=new TermQuery(new Term("topic",s));
						booleanQuery.add(tq2,Occur.SHOULD);
					}
					query.add(booleanQuery,Occur.SHOULD);
				}
				else
				{
					//没有同义词
					TermQuery tq1=new TermQuery(new Term("index_title",t));
					if(t.equals("黄山"))
					{
						System.out.println("sdkfdf");						
					}
					query.add(tq1,Occur.SHOULD);
				}
			}
			
			ScoreDoc[] hits=searcher.search(query,100).scoreDocs;
			
			for (int i = 0; i < hits.length; i++) {
				int docID = hits[i].doc;
				//匹配的话题
				String topic = searcher.doc(docID).get("index_title");
				//作者名
				String authorname = searcher.doc(docID).get("index_author");
				//日期
				String datetime = searcher.doc(docID).get("index_publishdate");
				//匹配的话题高亮
				String hightLightTopic = highLighter(topic, query, analyzer, 10, 10);
				
				Topic topicObj = new Topic(hightLightTopic,authorname,datetime);
				
				topicList.add(topicObj);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return topicList;
	}

	/**
	 * 分词
	 * @param topic
	 * @param analyzer
	 * @return
	 * @throws IOException
	 */
	private static List<String> showTerms(String topic, Analyzer analyzer) throws IOException {
		TokenStream tokenStream = analyzer.tokenStream("topic", new StringReader(topic));

		CharTermAttribute term = tokenStream.addAttribute(CharTermAttribute.class);

		tokenStream.reset();
		
		List<String> q=new ArrayList<String>();
		while (tokenStream.incrementToken()) {
			System.out.print(term + "\t");
			q.add(term.toString());
		}
		System.out.println();
		return q;
	}
	/**
	 * 高度
	 * @param textToBeHighLighted
	 * @param query
	 * @param analyzer
	 * @param numOfHighlightWords
	 * @param numOfHightlightedKeywords
	 * @return
	 */
	public static String highLighter(String textToBeHighLighted, Query query, Analyzer analyzer, int numOfHighlightWords, int numOfHightlightedKeywords) {
		QueryScorer scorer = new QueryScorer(query);
		Fragmenter fragmenter = new SimpleSpanFragmenter(scorer, numOfHighlightWords);// 后一个参数是加亮前后的保留字数
		SimpleHTMLFormatter formatter = new SimpleHTMLFormatter("<font color='red'>", "</font>");
		Highlighter hig = new Highlighter(formatter, scorer);
		hig.setTextFragmenter(fragmenter);
		TokenStream tokens = null;
		String highterResult = null;
		try {
			tokens = analyzer.tokenStream("index_content", new StringReader(textToBeHighLighted));
			highterResult = hig.getBestFragments(tokens, textToBeHighLighted, numOfHightlightedKeywords, "...");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InvalidTokenOffsetsException e) {
			e.printStackTrace();
		}
		return highterResult;
	}
}
