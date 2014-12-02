package so.xunta.people.relevant;

import java.io.IOException;
import org.apache.lucene.document.Document;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;

import so.xunta.response.SearchMethods;

//2013.6.10 在构造方法中增加了解释得分的explanation()测试语句.
public class DocData_Relevant implements Comparable{
	int docID;
	String title;
	String content;
	String highlightedContent;
	String url;
	long timeStamp;
	String Date;
	float score;
	//Query query;
	
	
	
	//取消一些数据,看速度是否提高:
	public DocData_Relevant(int docId,float score,IndexSearcher searcher,Query query) throws IOException{
		//this.query=query; 
		Document doc=searcher.doc(docId);
		this.content=doc.get("index_content");
		this.highlightedContent = SearchMethods.highLighter(this.content,query,15,8);
		//暂时不用这个,用下面的timestamp以显示到秒的时间, 以查看是否是重复文件. 2014.7.25 this.Date=doc.get("index_publishdate");//在最相关搜索中用这个: 只显示日期即可.
		timeStamp=Long.parseLong(doc.get("index_publishtimestamp"));//把文档中字符型时间戳转换成long型. 
		this.Date=new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date(timeStamp));
		
		this.url=doc.get("index_url");
		this.title=doc.get("index_title");
		this.docID=docId;
		this.score=score;
		
		//打印遍历的文档内容:
		//prt("Docdata inti.. docID"+docID+"|score: "+score+"|title:"+" "+title+"|date:"+Date+"|content:"+content+"|url:"+url);//TODO
		//用explain临时测看得分的构成:
		//Explanation expln=GroupingTest_Collector_Xu.searcher.explain(GroupingTest_Collector_Xu.query, docId); 
		//prt("得分Explanation:"+expln);//TODO 
	}
	
	//一个参数不同的创建方法,用于getmore过程中,DocData在collector中的创建:collector只用它排序,并只记录docid和得分.其它文档数据只在提交到前台的文档上才需要获取. 
	//public DocData_Relevant(int docId,float score,IndexSearcher searcher,boolean useTrue) throws IOException{//这个useTrue仅用于实现多载.
	public DocData_Relevant(int docId,float score,boolean useTrue) throws IOException{//这个useTrue仅用于实现多载.
		this.docID=docId;
		this.score=score;
	}
	
	
	@Override
	public int compareTo(Object o) {
		DocData_Relevant dd=(DocData_Relevant) o;
		if (this.score<dd.score){//set默认为升序排序.这里将返回为1用于"小于",这将改变set的排序方式为降序.
			return 1;
		}else if(this.score==dd.score){//相等时的排序应该与搜人时pagedata重新排序时的自定义比较器一致.否则在文档评分相等时,顺序会不一致.(搜人结果与getmore的结果).
			return -1;
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
	
	public String getContent() {//这里要加上高亮显示
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
