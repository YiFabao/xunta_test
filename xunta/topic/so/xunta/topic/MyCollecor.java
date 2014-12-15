package so.xunta.topic;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.index.AtomicReader;
import org.apache.lucene.index.AtomicReaderContext;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.Collector;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Scorer;

public class MyCollecor extends Collector {
	public Map<Integer,Double> map=new HashMap<>(); //文档id，分数
	
	public IndexSearcher isearcher ;
	public Analyzer analyzer;
	public List<String> q;

	public MyCollecor(IndexSearcher isearcher,Analyzer analyzer,List<String> q) {
		this.isearcher=isearcher;
		this.analyzer=analyzer;
		this.q=q;
	}

	@Override
	public boolean acceptsDocsOutOfOrder() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void collect(int doc) throws IOException {
		String topic=isearcher.doc(doc).get("topic");
		TokenStream tokenStream = analyzer.tokenStream("topic", new StringReader(topic));
		CharTermAttribute term = tokenStream.addAttribute(CharTermAttribute.class);
		
		tokenStream.reset();
		
		//将搜索出的语句，分词后，计算每个词与查询式中的词的相关度R
		while (tokenStream.incrementToken()) {
			for(String t:q)
			{
				System.out.print(term+"==>"+t+"\t");
			}
		}
		System.out.println();

	}

	@Override
	public void setNextReader(AtomicReaderContext context) throws IOException {
		System.out.println("setNextReader()");
		
	}

	@Override
	public void setScorer(Scorer scorer) throws IOException {

	}

}
