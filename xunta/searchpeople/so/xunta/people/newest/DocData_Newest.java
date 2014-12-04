package so.xunta.people.newest;

import java.io.IOException;
import org.apache.lucene.document.Document;
import org.apache.lucene.search.Query;

import so.xunta.response.SearchMethods;
//2013.6.10 在构造方法中增加了解释得分的explanation()测试语句.
public class DocData_Newest implements Comparable{
	int docID;
	String title;
	String content;
	String highlightedContent;
	String url;
	//int level;//楼层数.2014.9.11
	long timeStamp;
	String Date;
	float score;

	//取消一些数据,看速度是否提高:
	public DocData_Newest(int docId,float score,Document doc,Query query) throws IOException{
		//Document doc=searcher.doc(docId);
		this.content=doc.get("index_content");
		
		//this.highlightedContent=SearchMethods.highLighter(this.content,query,15,8);
		
		this.highlightedContent=SearchMethods.highLighter(this.content,query,15,8);//query参数仅用于高亮显示.

		timeStamp=Long.parseLong(doc.get("index_publishtimestamp"));//把文档中字符型时间戳转换成long型. 
		//有人提建议说  yyyy/MM/dd HH:mm:ss格式的日期不好 改yyyy-MM-dd HH:mm:ss 2014/11/10 易
		this.Date=new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date(timeStamp));
		
		this.url=doc.get("index_url");
		this.title=doc.get("index_title");
		//this.level=Integer.parseInt(doc.get("index_level"));//取出楼层以后,要转换成int,以方便后面的原创度等指标的计算.2014.9.11 
		this.docID=docId;
		this.score=score;
		
		//打印遍历的文档内容:
		//prt("Docdata inti.. docID"+docID+"|score: "+score+"|title:"+" "+title+"|date:"+Date+"|content:"+content+"|url:"+url);//TODO
		//用explain临时测看得分的构成:
		//Explanation expln=GroupingTest_Collector_Xu.searcher.explain(GroupingTest_Collector_Xu.query, docId); 
		//prt("得分Explanation:"+expln);//TODO 
	}
	
		//易
		public DocData_Newest(int docId,float score,Document doc,Query query,String flag) throws IOException{
			//Document doc=searcher.doc(docId);
			this.content=doc.get("index_content");
			
			this.highlightedContent=this.content;
	
			
			timeStamp=Long.parseLong(doc.get("index_publishtimestamp"));//把文档中字符型时间戳转换成long型. 
			//有人提建议说  yyyy/MM/dd HH:mm:ss格式的日期不好 改yyyy-MM-dd HH:mm:ss 2014/11/10 易
			this.Date=new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date(timeStamp));
			
			this.url=doc.get("index_url");
			this.title=doc.get("index_title");
			//this.level=Integer.parseInt(doc.get("index_level"));//取出楼层以后,要转换成int,以方便后面的原创度等指标的计算.2014.9.11 
			this.docID=docId;
			this.score=score;
			
			//打印遍历的文档内容:
			//prt("Docdata inti.. docID"+docID+"|score: "+score+"|title:"+" "+title+"|date:"+Date+"|content:"+content+"|url:"+url);//TODO
			//用explain临时测看得分的构成:
			//Explanation expln=GroupingTest_Collector_Xu.searcher.explain(GroupingTest_Collector_Xu.query, docId); 
			//prt("得分Explanation:"+expln);//TODO 
		}
		
	
	//一个参数不同的创建方法,用于getmore过程中,DocData在collector中的创建:collector只用它排序,并只记录docid和得分.其它文档数据只在提交到前台的文档上才需要获取. 
	//public DocData_Newest(int docId,float score,IndexSearcher searcher,boolean useTrue) throws IOException{//这个useTrue仅用于实现多载.
	public DocData_Newest(int docId,float score,boolean useTrue) throws IOException{//这个useTrue仅用于实现多载,没有具体用途.
		this.docID=docId;
		this.score=score;
	}
	
	
	@Override
	public int compareTo(Object o) {
		DocData_Newest dd=(DocData_Newest) o;
		if (this.score<dd.score){//set默认为升序排序.这里将返回为1用于"小于",这将改变set的排序方式为降序.
			return 1;
		}else if(this.score==dd.score){
			return 1;
		}else{
			return -1;
		}
	}
	
	public int getDocID() {
		return docID;
	}
	public void setDocID(int docID) {
		this.docID = docID;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getContent() {//到底被哪里调用了? 
			return this.content;
	}
	
	public String getHighlightedContent() {//提供高亮显示正文.
		return highlightedContent;
	}
	
	public void setContent(String content) {
		this.content = content;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getDate() {
		return Date;
	}
	public void setDate(String date) {
		Date = date;
	}
	public float getScore() {
		return score;
	}
	public void setScore(float score) {
		this.score = score;
	}
	
	void prt(Object o){System.out.println(o);}
	
}
