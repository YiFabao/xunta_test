package so.xunta.author.evaluation;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.apache.lucene.index.AtomicReaderContext;
import org.apache.lucene.index.BinaryDocValues;
import org.apache.lucene.search.Collector;
import org.apache.lucene.search.FieldCache;
import org.apache.lucene.search.Scorer;
import org.apache.lucene.util.BytesRef;

public class Collector_Author_Evaluation extends Collector {
	private int current_DocId;
	private int docBase;
	private AtomicReaderContext arc;
	//总贴子数　
	private int totalDocs=0;
	//头贴数　
	private int firstPost=0;
	//重复贴
	private int repeatPost=0;
	
	
	public int getTotalDocs() {
		return totalDocs;
	}

	public void setTotalDocs(int totalDocs) {
		this.totalDocs = totalDocs;
	}

	public int getFirstPost() {
		return firstPost;
	}

	public void setFirstPost(int firstPost) {
		this.firstPost = firstPost;
	}

	public int getRepeatPost() {
		return repeatPost;
	}

	public void setRepeatPost(int repeatPost) {
		this.repeatPost = repeatPost;
	}

	//md5用来判断重复文档
	Set<String> md5Set=new HashSet<String>();
	private BinaryDocValues md5_DocBvalues;
	private BytesRef md5_BytesRef=new BytesRef();
	private String md5;
	
	private BinaryDocValues level_DocBvalues;
	private BytesRef level_BytesRef=new BytesRef();
	private String level;
	
	@Override
	public boolean acceptsDocsOutOfOrder() {
		return true;
	}

	@Override
	public void collect(int docId) throws IOException {
		this.current_DocId=docId;
		md5_DocBvalues.get(current_DocId, md5_BytesRef);
		level_DocBvalues.get(current_DocId, level_BytesRef);
		
		md5=md5_BytesRef.utf8ToString();// 取出md5
		level=level_BytesRef.utf8ToString();// 取出level
		
		//统计重复贴
		if(md5Set.contains(md5))
		{
			repeatPost++;
		}
		else
		{
			md5Set.add(md5);
		}
		//统计头帖数
		if(level!=null&&level.trim().equals("0"))
		{
			firstPost++;
		}
		//统计总数
		totalDocs++;;
	}

	@Override
	public void setNextReader(AtomicReaderContext arc) throws IOException {
		this.arc=arc;
		this.docBase=arc.docBase; //docBase是通过arc传进来的.
		//lucene41用这个:authors_DocTerms=FieldCache.DEFAULT.  getTerms(arc.reader(), "index_author");//作者名是得分累计时必须的//首次读入内存后可以一直使用.提高速度.
		//lucene41用这个:site_DocTerms=FieldCache.DEFAULT.getTerms(arc.reader(), "index_sitename");//网站名称也是得分累计时必须的.作者需要用网站名来实现唯一化(不同网站的作者可能同名.//首次读入内存后可以一直使用.提高速度.
		
		//在用lucene43时,这个返回结果为BinaryDocValues:
		md5_DocBvalues=FieldCache.DEFAULT.getTerms(arc.reader(), "md5");
		level_DocBvalues=FieldCache.DEFAULT.getTerms(arc.reader(), "index_level");
	}

	@Override
	public void setScorer(Scorer scorer) throws IOException {

	}

}
