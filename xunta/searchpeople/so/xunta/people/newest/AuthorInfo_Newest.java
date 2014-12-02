package so.xunta.people.newest;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.TreeSet;

public class AuthorInfo_Newest implements Comparable{
	public String author;
	public String site;
 	public float totalScore;// 一个作者的总得分.
 	public int totalAuthorDocs;//该作者所有符合条件的文档. 
 	public int leadPosters;//原创度指标.
 	public int followPosters;//参与度指标.
 	public int duplicatePosters;//灌水度指标.
 	
	//public HashMap<Integer,Float> docIDs=new HashMap<Integer,Float>();//key为doc的id, value为文档的得分.
 	//public HashMap<Integer,Float> docIDs=new HashMap<Integer,Float>();//key为doc的id, value为文档的得分.将HashMap改为TreeMap.解决文档分数相同时,排序不是按文档id排序的.2014.7.31
	public TreeMap<Integer,Float> docIDs=new TreeMap<Integer,Float>();//key为doc的id, value为文档的得分.将HashMap改为TreeMap.解决文档分数相同时,排序不是按文档id排序的.2014.7.31
	public TreeSet<DocData_Newest> authorDocData_Set=new TreeSet<DocData_Newest>();
	
	public TreeSet<DocData_Newest> getAuthorDocData_Set() {
		return authorDocData_Set;
	}
	
	//构造方法:
	public AuthorInfo_Newest(String author,String site,float firstscore){
		this.author=author;
		this.site=site;
		this.totalScore=firstscore;
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
	public TreeMap<Integer,Float> getDocList() {
		return docIDs;
	}
/*	public TreeMap<Integer,Float> getDocList() {
		return docIDs;
	}
*/	public int getTotalAuthorDocs() {
		return totalAuthorDocs;
	}

	public float getLeadPosters() {
		return leadPosters;
	}

	public void setLeadPosters(int leadPosters) {
		this.leadPosters = leadPosters;
	}

	public float getFollowPosters() {
		return followPosters;
	}

	public void setFollowPosters(int followPosters) {
		this.followPosters = followPosters;
	}

	public float getDuplicatePosters() {
		return duplicatePosters;
	}

	public void setDuplicatePosters(int duplicatePosters) {
		this.duplicatePosters = duplicatePosters;
	}
	
	
	
}
