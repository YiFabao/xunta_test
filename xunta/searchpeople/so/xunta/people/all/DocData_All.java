package so.xunta.people.all;

import java.io.IOException;
import java.io.StringReader;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;

import org.apache.lucene.document.Document;
//import org.apache.lucene.index.TermFreqVector;
//import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.Explanation;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.util.Version;
//引用IKAnalyzer3.0的类
import org.wltea.analyzer.lucene.IKAnalyzer;

import org.apache.lucene.search.highlight.Fragmenter;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.search.highlight.SimpleSpanFragmenter;

import so.xunta.people.newest.DocData_Newest;
import so.xunta.people.newest.SearchPeople_Newest;
import so.xunta.response.SearchMethods;
import so.xunta.response.PeopleSearchVars;
//2013.6.10 在构造方法中增加了解释得分的explanation()测试语句.
public class DocData_All implements Comparable{
	int docID;
	String title;
	String content;
	String highlightedContent;
	String url;
	long timeStamp;
	String Date;
	float score;
	Query query; 
	//PeopleSearchVars sV;
	

/*原先的构造方法暂时注释掉:
	public DocData(ScoreDoc scoreDoc,IndexSearcher searcher) throws IOException{
		this.content=searcher.doc(scoreDoc.doc).get("index_content");;
		this.Date=searcher.doc(scoreDoc.doc).get("index_publishtime");
		//prt("docDate.Date:"+this.Date);
		this.url=searcher.doc(scoreDoc.doc).get("index_url");
		this.title=searcher.doc(scoreDoc.doc).get("index_title");
		
		this.docID=scoreDoc.doc;
		this.score=scoreDoc.score;
	}*/
	//取消一些数据,看速度是否提高:
	public DocData_All(int docID,float score,IndexSearcher searcher,Query query) throws IOException{
		this.query=query;
		Document doc=searcher.doc(docID);
		content=doc.get("index_content");
		
		highlightedContent=SearchMethods.highLighter(content,query,30,20);//高亮词前后保留30个字,最多高亮显示20个关键词.
		if(highlightedContent.length()==0){//如果返回的高亮文本为零,则显示原始内容的前200个字:
			if (content.length() <= 200){//如果小于200字,则全部显示出来.
				highlightedContent=content;
			} if (content.length()>200 && content.length()<=250){//中间长度,则中间打省略号.
				highlightedContent=content.substring(0, content.length()-80)+"...  ..."+content.substring(content.length()-40, content.length());
			}  if (content.length()>250){//如果太长,则中间标出省略了多少字.
				highlightedContent=content.substring(0, 200)+"  <font color=\"red\">...(省略"+(content.length()-200-40)+"字)...</font>   "+content.substring(content.length()-40, content.length());
			}
		}
		
		timeStamp=Long.parseLong(doc.get("index_publishtimestamp"));//把文档中字符型时间戳转换成long型. 
		this.Date=new java.text.SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new java.util.Date(timeStamp));
		//原来的日期: this.Date=doc.get("index_publishdate");
		//prt("docDate.Date:"+this.Date);
		this.url=doc.get("index_url");
		this.title=doc.get("index_title");
		this.docID=docID;
		this.score=score;
	
	}
	
	//一个参数不同的创建方法,用于getmore过程中,DocData在collector中的创建:collector只用它排序,并只记录docid和得分.其它文档数据只在提交到前台的文档上才需要获取. 
	public DocData_All(int docID,float score,boolean useTrue) throws IOException{//这个useTrue仅用于实现多载.
		//Document doc=searcher.doc(docId);
		this.docID=docID;
		this.score=score;
	}
	
	
	@Override
	public int compareTo(Object o) {
		DocData_All	dd=(DocData_All) o;
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
