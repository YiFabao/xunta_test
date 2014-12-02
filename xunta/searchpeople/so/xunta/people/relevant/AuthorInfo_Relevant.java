package so.xunta.people.relevant;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

public class AuthorInfo_Relevant implements Comparable<Object>{
	public String author;
	public String site;
 	public float totalScore;// 一个作者的总得分.
 	public int totalAuthorDocs;//该作者所有符合条件的文档. 	
 	
 	public Set<String> md5Set=new HashSet<String>();//存放作者的所有文档内容相关的md5值,作者：如果某个文档的md5在些集合中存在，表明该文档是该作者的文档内容重复不加分
 	
 	public int leadPosters;//原创度指标.
 	public int followPosters;//参与度指标.
 	public int duplicatePosters;//灌水度指标.
 	
	public HashMap<Integer,Float> docIDs=new HashMap<Integer,Float>();
	public TreeSet<DocData_Relevant> authorDocData_Set=new TreeSet<DocData_Relevant>();
	
	public TreeSet<DocData_Relevant> getAuthorDocData_Set() {
		return authorDocData_Set;
	}
	
	//构造方法:
	public AuthorInfo_Relevant(String author,String site,float firstscore,String md5){
		this.author=author;
		this.site=site;
		this.totalScore=firstscore;
		this.md5Set.add(md5);
	}
	
	@Override
	public int compareTo(Object o) {
		AuthorInfo_Relevant ai=(AuthorInfo_Relevant) o;
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
