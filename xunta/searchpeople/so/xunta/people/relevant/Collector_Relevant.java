package so.xunta.people.relevant;

import java.io.IOException;
import java.util.HashMap;
import org.apache.lucene.index.AtomicReaderContext;
import org.apache.lucene.index.BinaryDocValues;
import org.apache.lucene.search.Collector;
import org.apache.lucene.search.FieldCache;
import org.apache.lucene.search.Scorer;
//import org.apache.lucene.search.FieldCache.DocTerms;//lucene41时用这个.
import org.apache.lucene.util.BytesRef;

//2013年4月15日.
//2013.6.9:对于得分相同的文档,其得分将被忽略.
public class Collector_Relevant extends Collector {
	//List<ScoreDoc> docs=new ArrayList<ScoreDoc>();
	
	// 测试用时时间
	public double sumTime1 = 0;
	public double sumTime2 = 0;
	public double sumTime3 = 0;
	public double sumTime4 = 0;
	
	private Scorer scorer;
	private int docBase;
	private AtomicReaderContext arc;//有warning，但是不能注释掉。
	private int current_DocId;
	private float current_Score;
	
	//private DocTerms authors_DocTerms;//lucene41,在L43时,改为下面两个变量类型. 
	//private DocTerms site_DocTerms;//lucene41,在L43时,改为下面两个变量类型.
	
	private	BinaryDocValues authors_DocBValues;//lucene43
	private BinaryDocValues site_DocValues;//lucene43
	
	//private DocTerms title_DocTerms;//TODO 临时测试用
	//private DocTerms content_DocTerms;//TODO 临时测试用
	
	private BytesRef author_BytesRef=new BytesRef(); 
	private BytesRef site_BytesRef=new BytesRef();
	
	//private BytesRef title_BytesRef=new BytesRef();//TODO 临时测试用
	//private BytesRef content_BytesRef=new BytesRef();//TODO 临时测试用
	
	private String author;
	private String site;
	private String authorPLUSsite;
	
	private int totalDocs;
	private int totalAuthors;
	AuthorInfo_Relevant new_ai;
	AuthorInfo_Relevant old_ai;
	
	

	private HashMap<String,AuthorInfo_Relevant> authorScoreMap=new HashMap<String,AuthorInfo_Relevant>();// 作者ID与对应的作者信息类对象.用于遍历文章时积累各个作者的得分等数据.
	//private static TreeSet<AuthorInfo_small> authorRanking = new TreeSet<AuthorInfo_small>();// 最后的搜人数据容器.顺序是按总得分数排序的.重写了AuthorInfo的比较大小的方法.
	
	public boolean acceptsDocsOutOfOrder(){
		return true;
	}
	
	public void setScorer(Scorer scorer){
		this.scorer=scorer;//传入当前的scorer.
	}

	/*4.0版本以后使用下面的AtomicReaderContext方法参数.
	public void setNextReader(IndexReader reader, int docBase){
		this.docBase=docBase;
	}*/
	public void setNextReader(AtomicReaderContext arc) throws IOException{
		this.arc=arc;
		this.docBase=arc.docBase; //docBase是通过arc传进来的.
		//lucene41用这个:authors_DocTerms=FieldCache.DEFAULT.  getTerms(arc.reader(), "index_author");//作者名是得分累计时必须的//首次读入内存后可以一直使用.提高速度.
		//lucene41用这个:site_DocTerms=FieldCache.DEFAULT.getTerms(arc.reader(), "index_sitename");//网站名称也是得分累计时必须的.作者需要用网站名来实现唯一化(不同网站的作者可能同名.//首次读入内存后可以一直使用.提高速度.
		

		
		//在用lucene43时,这个返回结果为BinaryDocValues:
		authors_DocBValues=FieldCache.DEFAULT.  getTerms(arc.reader(), "index_author");//作者名是得分累计时必须的//首次读入内存后可以一直使用.提高速度.
		site_DocValues=FieldCache.DEFAULT.getTerms(arc.reader(), "index_sitename");//网站名称也是得分累计时必须的.作者需要用网站名来实现唯一化(不同网站的作者可能同名.//首次读入内存后可以一直使用.提高速度.
		
		
		//title_DocTerms=FieldCache.DEFAULT.getTerms(arc.reader(), "index_title");//TODO 临时测试用
		//content_DocTerms=FieldCache.DEFAULT.getTerms(arc.reader(), "index_content");//TODO 临时测试用
		//prt("authors_DocTerms.size():"+authors_DocTerms.size());
	}

	public void collect(int docId) throws IOException{

		current_DocId=docId;//当前文档的id.
		current_Score=scorer.score();//获得当前文档的具体得分.
		
		current_Score=current_Score+((float)current_DocId)/100000000;//把id号附加到得分的尾部,解决得分相同时 排序不固定,无法对比校验的问题.
		//在lucene41时用这两个:
		//authors_DocTerms.getTerm(current_DocId, author_BytesRef);//按当前的docId从内存中获得author字段的数据,通过bytesref参数返回.
		//site_DocTerms.getTerm(current_DocId, site_BytesRef);//与上同.取出当前文档的网站名称.
		//在lucene43时用这两个:
		authors_DocBValues.get(current_DocId, author_BytesRef);//按当前的docId从内存中获得author字段的数据,通过bytesref参数返回.
		site_DocValues.get(current_DocId, site_BytesRef);//与上同.取出当前文档的网站名称.
		
		author=author_BytesRef.utf8ToString();//取出具体的作者名称.
		site=site_BytesRef.utf8ToString();//与上同.取出当前文档的网站名称.
		//String site1=site_BytesRef.toString();
		
		//title_DocTerms.getTerm(current_DocId, title_BytesRef);//TODO 临时测试用
		//String title=title_BytesRef.utf8ToString();//TODO 临时测试用
		//String title1=site_BytesRef.toString();//TODO 临时测试用
		
		//content_DocTerms.getTerm(current_DocId, content_BytesRef);//TODO 临时测试用
		//String content=site_BytesRef.toString();//TODO 临时测试用
		//prt("-------------------------------current_DocId"+current_DocId);
		//prt("relevant作者:"+author+"\t"+current_Score);//这个打印中间加了制表符,可以拷到excel表格中作原始验证.
		
		
		//System.out.println("author_BytesRef:"+author_BytesRef.utf8ToString() );
		authorPLUSsite=author+"-0419pls-"+site;//author+site做成一个临时的authorid..................
		try {
			authorScoreAddup();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	void authorScoreAddup() throws Exception {
		//int countingDocs=0;//TODO 要删掉
		//int countingAuthors=0;//TODO 要删掉
			//DocData docData=new DocData(scoreDoc,searcher);//生成一个含有必要文档信息的docData对象,并填入当前doc的数据.
			
			/*
			GroupingTest_Collector_Xu.end = System.currentTimeMillis();//TODO 要删掉.
			System.out.println("AuthorRanking-get(index_author)之前:" + (GroupingTest_Collector_Xu.end - GroupingTest_Collector_Xu.begin) + "ms");
			//String author=searcher.doc(scoreDoc.doc).get("index_author");
			String author="A+tmp"+countingDocs;//searcher.doc(scoreDoc.doc).get("index_author");
			GroupingTest_Collector_Xu.end = System.currentTimeMillis();//TODO 要删掉.
			System.out.println("AuthorRanking-get(index_author)之后:" + (GroupingTest_Collector_Xu.end - GroupingTest_Collector_Xu.begin) + "ms");
			
			String title="title+amp";//searcher.doc(scoreDoc.doc).get("index_title");
			String site="site+amp";//searcher.doc(scoreDoc.doc).get("index_siteid");
			String authorId=author+"_"+site;
			countingDocs++;//TODO 要删掉
			*/
			totalDocs++;
			if (author.matches("doesNotMatchAnyCell_Xu")){//该作者名是无效情况下的记录,需过滤掉.//标题中的这种情况也应过滤掉.
				//prt("作者名或标题中出现了 doesNotMatchAnyCell_Xu");
			}else if (!authorScoreMap.containsKey(authorPLUSsite)){//如果该作者不存在...
				new_ai=new AuthorInfo_Relevant(author,site,current_Score,null);
				//prt("Collector_Xu-往新AuthorInfo里加author:"+author);
				new_ai.docIDs.put(current_DocId+docBase,current_Score);//将该文档的全局id和得分放入docID(一个docid-得分的map对:HashMap<Integer,Float>).
				authorScoreMap.put(authorPLUSsite,new_ai);//将新作者放入authorScoreMap(作者名-AuthorInfo_small的map对. 后者中有总得分.)
				totalAuthors++;//TODO 要删掉
			}else{//如果该作者已经存在...
				old_ai=authorScoreMap.get(authorPLUSsite);//从authorScoreMap中取出作者的AuthorInfo_small
				
				
				if (!old_ai.docIDs.containsValue(current_Score)){//如果该文档的分数原来不存在相同的分数,则累加.如果存在,则跳过累加.这是为了过滤掉相同的文档分数.
				old_ai.totalScore=old_ai.totalScore+current_Score;//将分数累加.
				}
				old_ai.docIDs.put(current_DocId+docBase,current_Score);//将该文档的全局id和得分放入docID(一个docid-得分的map对:HashMap<Integer,Float>).
				//old_ai.docNum++;
			}
			
		//prt("Collector_Xu - current_Score: "+current_Score);
	
		//prt("authorScoreAddup中的当前累计文档数-totalDocs:"+totalDocs+"个");//TODO 要删掉
	//	prt("authorScoreAddup中的当前累计作者数-totalAuthors:"+ totalAuthors+"个");//TODO 要删掉
		
		//authorIdMap只是积累了作者名及其文档的所有数据,需要在数据转至AuthorRanking的过程中实现作者排序.
		//AuthorRanking的大小比较被重写,按得分来排序.
	//	prt("从authorIdMap看作者总数:-----------authorIdMap.size():"+authorScoreMap.size()+"个");//TODO 要删掉
			
	}
	
	public void clear(){
		authorScoreMap.clear();//清空
		//authorRanking.clear();//清空
		totalDocs=0;//清空.
		totalAuthors=0;//清空.
	}

	//public static TreeSet<AuthorInfo_small> getAuthorRanking(){
		//return authorRanking;
	//}
	
	void prt(Object o){
		System.out.println(o);
	}
	
	//getter　setter：
	public int getTotalDocs() {
		return totalDocs;
	}
	public int getTotalAuthors() {
		return totalAuthors;
	}
	public HashMap<String, AuthorInfo_Relevant> getAuthorScoreMap() {
		return authorScoreMap;
	}
}





