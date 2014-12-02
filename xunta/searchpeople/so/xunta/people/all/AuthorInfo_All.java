package so.xunta.people.all;
import java.util.HashMap;
import java.util.HashSet;
import java.util.TreeSet;

import so.xunta.people.newest.AuthorInfo_Newest;
import so.xunta.people.newest.DocData_Newest;


public class AuthorInfo_All implements Comparable{
	public String author;
	public String site;
 	public float totalScore;// 一个作者的总得分.
 	public int totalAuthorDocs;//该作者所有符合条件的文档. 	
//	public int docNum;
	public HashMap<Integer,Float> docIDs=new HashMap<Integer,Float>();
	public TreeSet<DocData_Newest> authorDocData_Set=new TreeSet<DocData_Newest>();
	
	public TreeSet<DocData_Newest> getAuthorDocData_Set() {
		return authorDocData_Set;
	}
	//构造方法:
	public AuthorInfo_All(String author,String site,float firstscore){
		this.author=author;
		this.site=site;
		this.totalScore=firstscore;
	//	this.docNum=1;
	}
	@Override
	public int compareTo(Object o) {
		AuthorInfo_Newest ai=(AuthorInfo_Newest) o;
		if (this.totalScore<ai.totalScore){//set默认为升序排序.这里将返回为1用于"小于",这将改变set的排序方式为降序.
			return 1;
		}else if(this.totalScore==ai.totalScore){
			return 1;
		}else{
			return -1;
		}
	}
	
	public String getAuthor(){
		return this.author;
	}
	
	public float getTotalScore(){
		return this.totalScore;
	}
	
	public String getSite() {
		return site;
	}
	public void setSite(String site) {
		this.site = site;
	}
	public HashMap<Integer,Float> getDocList() {
		return docIDs;
	}
	public int getTotalAuthorDocs() {
		return totalAuthorDocs;
	}
	
}
