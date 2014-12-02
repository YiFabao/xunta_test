package so.xunta.author.newest;

import java.io.IOException;
import java.util.TreeSet;
import org.apache.lucene.index.AtomicReaderContext;
import org.apache.lucene.index.BinaryDocValues;
import org.apache.lucene.search.Collector;
import org.apache.lucene.search.FieldCache;
import org.apache.lucene.search.Scorer;
import org.apache.lucene.util.BytesRef;
import so.xunta.people.newest.DocData_Newest;


//2013.11.09:创建.只是简单积累文档ID及对应的得分.仍然使用这个collector类,是为了将来便于嵌入其它算法.

public class Collector_Author_Newest extends Collector {
	@SuppressWarnings("unused")
	private Scorer scorer;
	private int docBase;
	@SuppressWarnings("unused")
	private AtomicReaderContext arc;//有warning，但是不能注释掉。
	private int current_DocId;
	private float current_Score;
	private int totalDocs;
	
	private BinaryDocValues timestamp_DocBvalues;//lucene43
	private BytesRef timestamp_BytesRef=new BytesRef();
	
	//PeopleSearchVars sV;
	
	//public static HashMap<Integer,Float> docIDs=new HashMap<Integer,Float>();//用来存放搜索结果的所有 "docID-得分"值对.
	//public static HashMap<Integer,Float> score_DocID_Map=new HashMap<Integer,Float>();//用来存放搜索结果的所有 "docID-得分"值对.//直接排序了.
					//如果直接存放DocData,并利利它的比较方法来排序,必须同时读出标题等内容,否则要另建一个生成方法.
					//把docid放在前面,可以防止因key值相同而
	public TreeSet<DocData_Newest> docdata_set=new TreeSet<DocData_Newest>(); //干脆直接用docdata排序,采用一个多载的创建方法,以避免创建docdata时获取暂时不需要的文档数据.
	
	//public static TreeSet<DocData_Newest> docData_HashSet=new TreeSet<DocData_Newest>();//不用上面这个临时的容器了.直接排序存放DocData类. 
	/*
	public Collector_Author_Newest (PeopleSearchVars sV){//这个构造方法只用于传进来一个searcher(在sV中).
		this.sV=sV; 
	}*/
	
	
	public boolean acceptsDocsOutOfOrder(){
		return true;
	}
	
	public int getTotalDocs(){
		return docdata_set.size(); 
	}
	
	public void setScorer(Scorer scorer){
		this.scorer=scorer;//传入当前的scorer.
	}

	public void setNextReader(AtomicReaderContext arc) throws IOException{
		this.arc=arc;
		this.docBase=arc.docBase; //docBase是通过arc传进来的.
		timestamp_DocBvalues=FieldCache.DEFAULT.getTerms(arc.reader(), "index_publishtimestamp");

	}

	public void collect(int docId) throws IOException{
		current_DocId=docId;//当前文档的id.
		
		timestamp_DocBvalues.get(current_DocId,timestamp_BytesRef);
		current_Score=Long.parseLong(timestamp_BytesRef.utf8ToString());//将时间戳数值后，直接当作当前文档的得分．（authorScoreAddup中，得分不再相加，而是用大值取代小值．）
		current_Score=current_Score+((float)current_DocId)/1000000000;//把id号附加到得分的尾部,解决得分相同时 排序有差异的问题.//注意:这里的得分是数值化的时间值.
			//在Collector_Newest里也加了这一句.
		
		try {
			docsAddup();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	void docsAddup() throws Exception {
		//prt("测试搜索过程Collector_Author_Newest:"+current_Score+"|"+(docBase+current_DocId));
		//score_DocID_Map.put(current_DocId+docBase,current_Score);//将该文档的全局id和得分放入docID(一个docid-得分的map对:HashMap<Integer,Float>).
		//这个类的一个多载创建方法:DocData_Newest(int docId,float score,IndexSearcher searcher,boolean useTrue)
		docdata_set.add(new DocData_Newest(docBase+current_DocId,current_Score,true));
		//prt("在collector中直接打印结果集长度: "+docdata_set.size());
	}
	
	public void clear(){
		//score_DocID_Map.clear();//清空
		docdata_set.clear(); 
	}
	
	void prt(Object o){
		System.out.println(o);
	}
}





