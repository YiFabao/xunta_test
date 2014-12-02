package so.xunta.author.newest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.lucene.search.Query;
import so.xunta.people.newest.DocData_Newest;
import so.xunta.response.AuthorProfileVars;
import so.xunta.response.SearchMethods;
//2013.11.12:创建.
public class DocData_Newest_GetMore {
	
	//2014.6.14 这个变量是否没用上? public TreeMap<Integer,Float> docIDs_TreeMap=new TreeMap<Integer,Float>();//创建一个有序的map,接收collector的无序docIDs集合.
	public List<DocData_Newest> docData_List =new ArrayList<DocData_Newest>();//根据getmore所需的范围(从第几个文档到第几个文档),存入所请求的文档信息,供getmore的jsp返回文档 使用.
	//int moreDocsFetchNum=2;
	//public int totalDocs;
	public DocData_Newest_GetMore(AuthorProfileVars apV) throws IOException {//构造时即生成所需的数据.
		int rank=0;
		//apV.totalDocs=Collector_Author_Newest.docdata_set.size(); 
		//p.prt("下面将打印搜索结果集中是否有内容:DocData_Newest_GetMore"+"collector中的结果集长度: "+apV.totalDocs);
		//p.prt( "apV.docNumShown:"+apV.docNumShown+" apV.moreDocsFetchNum:"+apV.moreDocsFetchNum); 
		Query query=SearchMethods.createQuery(apV.searchKeywords);//搜作者的query中包含了作者名和网站名,它们会出现在高亮词中.因此这里专门生成一个只有搜索词的query,传递到DocData_Newest.2014.7.27
		for(DocData_Newest docData:apV.docdata_set_newest){
			//p.prt("DocData_Newest_GetMore - 得分:"+ docData.getScore()+"|"+"docID:"+docData.getDocID());
			rank++;
			   //rank>docNumShown && 
			if( rank>apV.docNumShown && rank<=(apV.docNumShown+apV.moreDocsFetchNum)){//docNumShown+moreDocsFetchNum这个终点值大于最后一个文档也没关系,因为一旦遍历完就自然结束了.
				//p.prt("DocData_Newest_GetMore - 得分:符合getmore条件的文档:"+ docData.getScore()+"|"+"docID:"+docData.getDocID());
				docData_List.add(new DocData_Newest(docData.getDocID(),docData.getScore(),apV.searcher.doc(docData.getDocID()),query)); //在collector里排序的docdata_set就不再往下传了,另写入一个新的docData_List.
			}
		}
	}
	
	public List<DocData_Newest> getDocData_List(){
		return this.docData_List;
	}

}
