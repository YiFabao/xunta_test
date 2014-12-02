package so.xunta.people.relevant;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.lucene.document.Document;
import org.apache.lucene.search.IndexSearcher;

import com.aigine.common.*;
import so.xunta.response.PeopleSearchVars;

//根据搜索结果及当前显示页号, 生成当前html页面所需要的直接可用数据.
//2013.6.5 modified. use groupingtest_collector_xu.getSearcher(). dont create anymore. keep one Searcher alive.
//2013.6.5: 把pagedata中调用docData前创建的Searcher取消了.全部不再close reader. 永远保持一个Searcher有效. 正文不显的情况不知怎么回事,又好了.可能是每次都创建searcher时出现的这个问题.现在不创建了,所以就好了.
//2013.6.28:
public class PageData_Relevant {

	public String title;// html页面的title.
	int docLines_perAuthor = 5;// 搜索结果中,每个作者首先显示多少个文档标题与摘要.
	public List<AuthorInfo_Relevant> authorInfo_List = new ArrayList<AuthorInfo_Relevant>();

	boolean hasPrev = true;
	boolean hasNext = true;
	int totalAuthor;// 全部作者数.
	int totalDocs;// 全部文档数.
	public int totalPageCount;// 全部页数;
	int currentPageNo;// 当前页的页号.
	int pageListLength = 20;// 这里设每页的作者数为20.
	List<String> PageNoList = new ArrayList<String>();// 用于准备 底部翻页数据.
	String searchKeywords = new String();// 保留搜索关键词,显示于搜索结果页面的搜索框里.
	String searchMode = "relevant";

	public PageData_Relevant(PeopleSearchVars sV) throws IOException {// 构造时即生成所需的数据.
		this.totalDocs = sV.totalDocs;// 总作者数.
		this.totalAuthor = sV.totalAuthors;// 总作者数.
		this.totalPageCount = this.totalAuthor / pageListLength + 1;// 总页面数.
		this.currentPageNo = Integer.parseInt(sV.pageNo);// 请求时传递过来的当前页号.

		if (currentPageNo == 1) {
			hasPrev = false;
		}// 如果页号为1则没有 上一页.
		if (currentPageNo == this.totalPageCount) {
			hasNext = false;
		}// 如果页号为尾号,则没有 下一页.
		
		long t1=System.currentTimeMillis();
		createPageNoList();// 在总页数和当前页号确定后,创建页面翻页中的页号列表.
		long t2=System.currentTimeMillis();
		System.out.println("createPageNoList()用时："+(t2-t1));
		// prt("PageData totalAuthor:"+ totalAuthor );
		// prt("PageData totalPageCount:"+ totalPageCount );
		// prt("PageData currentPageNo:"+ currentPageNo );

		/*
		 * for (AuthorInfo
		 * perauthor:GroupingTest_Collector_Xu.getAuthorRanking()){//临时打印所有作者.
		 * prt(perauthor.author); }
		 */

		// 获取当前页的作者列表:
		// AuthorRanking是TreeSet,有排序但没有index定位.所以要转变成list.
		// 但以后可以通过循环取出AuthorRanking的中间一段数据,省略这个list.
		
		authorInfo_List.addAll(sV.authorRanking_Relevant);// 获取搜索结果的所有数据.//这个addall实际上进行了一次排序.
		long t3=System.currentTimeMillis();
		System.out.println("authorInfo_List.addAll(sV.authorRanking_Relevant):"+(t3-t2));
		
		int fromIndex = (currentPageNo - 1) * pageListLength;// 被截取的列表起始处位置.
		int toIndex = currentPageNo * pageListLength;// 被截取的列表终止处位置.
		if (toIndex > totalAuthor) {
			toIndex = totalAuthor;
		}// 终止位置不能大于总作者数,否则报错.
		// prt("fromIndex:"+fromIndex);
		// prt("toIndex:"+toIndex);
		authorInfo_List = authorInfo_List.subList(fromIndex, toIndex);// 取当前页面数据的子集.
																		// 头尾index可能不对,需要测试.
																		// //第二个index是不包含的!
		long sumTime=0;
		long sumTime2=0;
		long sumTime3=0;
		int count=0;
		for (int i = 0; i < authorInfo_List.size(); i++) {
			// prt("pagedata 77line i="+i);
			HashMap<Integer, Float> docId_Score_Map = authorInfo_List.get(i).docIDs;// 获得第i个作者的AI,拿出其中的docIDs(docid与得分的对应Map).
			authorInfo_List.get(i).totalAuthorDocs = docId_Score_Map.size();// 根据docIDs的长度获得i作者的总文档数,赋给totalAuthorDocs.
			//authorInfo_List.get(i).authorDocData_Set.clear();// 先清零authorDocData_Set(存放后期获取数据的treeset,包括标题/时间/正文.

			// 需要先对docId_Score_Map进行一次排序,否则排在最前面的可能不是得分最高的.
			TreeMap<Float, Integer> score_DocId_Map = new TreeMap<Float, Integer>(new Comparator_Float_Xu()); // 临时定义一个有序map,并使用自定义的Float排序.解决key相同时,"键值对"加入时会被拒绝的问题.
			for (Map.Entry<Integer, Float> docId_score : docId_Score_Map.entrySet()) {// 通过转入一个有序map,进行一次全部文档的排序.
				// prt("PageData_Relevant-对某作者的全部文档进行一次排序:"+docId_score.getKey()+"|"+docId_score.getValue());//TODO
				score_DocId_Map.put(docId_score.getValue(), docId_score.getKey());
			}

		
			// 为了计算原创度等指标, 以下代码有修改. 2014.9.15
			Document doc = null;
			int docId;
			int j = 0;
			//定用统计重复贴子数，头贴数;跟贴数＝总贴子数-头贴数
			int duplicatePost=0;//重复贴
			int leadPost=0;//头贴
			Set<String> md5Set=new HashSet<String>();
			
			
			
			for (Map.Entry<Float, Integer> score_docid : score_DocId_Map.entrySet()) {
				// 为每个作者添加页面上要显示的文档内容
				docId = score_docid.getValue();
				long t10=System.currentTimeMillis();
				IndexSearcher indexsearcher=sV.searcher;
				long t11=System.currentTimeMillis();
				sumTime3+=t11-t10;
				long t9=System.currentTimeMillis();
				//doc = sV.searcher.doc(docId);
			/*	doc=indexsearcher.doc(docId);
				long s=System.currentTimeMillis()-t9;
				sumTime+=s;*/
				
				
				count++;
				j++;
				if (j <= (docLines_perAuthor - 1)) {
					DocData_Relevant docData = new DocData_Relevant(score_docid.getValue(), score_docid.getKey(), sV.searcher, sV.query);
					authorInfo_List.get(i).authorDocData_Set.add(docData);// 将刚生成的docdata放入每个作者的authordocdata-set.
				}
				// 提取该作者所有搜索结果中文档的楼层号及个数,用以计算原创度/参与度/灌水度指标:
				/*long t7=System.currentTimeMillis();
				String level = doc.get("index_level");// 从索引中提取每个文档的楼层数.
				if (level != null && level.trim().equals("0")) {
					leadPost++;
				}
				String md5 = doc.get("md5");
				long t8=System.currentTimeMillis();
				sumTime2+=(t8-t7);
				if (md5Set.contains(md5)) {
					duplicatePost++;
				} else {
					md5Set.add(md5);
				// (这里计算原创度等指标.)
				if (j == authorInfo_List.get(i).getTotalAuthorDocs())// 当遍历到最后一个元素时，计算其原创度等指标
				{
					// 最后形成的几个指标放在这些变量中:
					authorInfo_List.get(i).leadPosters = leadPost;// 头贴数
					authorInfo_List.get(i).followPosters = j - leadPost;// 跟贴数
					authorInfo_List.get(i).duplicatePosters = duplicatePost;// 重复贴;
				}
				}*/
			}
			//System.out.println("j:"+j);
		}
		long t4=System.currentTimeMillis();
		System.out.println("for (int i = 0; i < authorInfo_List.size(); i++):"+(t4-t3));
		System.out.println("=====sumTime:"+sumTime+" count:"+count+" sumetime2:"+sumTime2+"  sumetime3:"+sumTime3);
	}

	void createPageNoList() {// 创建一个翻页用的页号列表.
		PageNoList.add("" + 1);// 先加个1作为首页.
		for (int i = 2; i <= totalPageCount;) {
			if (i < (currentPageNo - 5)) {
				PageNoList.add("...");// 如果从2开始,离当前页的距离大于5,则加...号,并把i直接跳加到小于5的位置.
				i = currentPageNo - 5;
			} else if (i == totalPageCount) {
				PageNoList.add("" + totalPageCount);// 如果到了最后一页,则加该页号后退出结束.
				i = totalPageCount + 1;
			} else if (i > (currentPageNo + 4)) {
				PageNoList.add("...");// 如果i值大于当前页加4,而加...号,并直接跳加到最后页.
				i = totalPageCount;
			} else {
				PageNoList.add("" + i);// 如果i在中间显示的十个页号范围内,而链接当前页号.
				i++;
			}
			/*
			 * 打印测试是否正确. for (String pageno:PageNoList){
			 * System.out.print(pageno); } prt("");
			 */
		}
	}

	public void currentAuthorDocDataList(HashSet<Integer> docIDs) {
	}

	public String getSearchKeywords() {
		return searchKeywords;
	}

	public void setSearchKeywords(String searchKeywords) {
		this.searchKeywords = searchKeywords;
	}

	public String getSearchMode() {
		return searchMode;
	}

	public void setSearchMode(String searchMode) {
		this.searchMode = searchMode;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public boolean isHasPrev() {
		return hasPrev;
	}

	public void setHasPrev(boolean hasPrev) {
		this.hasPrev = hasPrev;
	}

	public boolean isHasNext() {
		return hasNext;
	}

	public void setHasNext(boolean hasNext) {
		this.hasNext = hasNext;
	}

	public int getTotalAuthor() {
		return totalAuthor;
	}

	public void setTotalAuthor(int totalAuthor) {
		this.totalAuthor = totalAuthor;
	}

	public int getTotalPageCount() {
		return totalPageCount;
	}

	public void setTotalPageCount(int totalPageCount) {
		this.totalPageCount = totalPageCount;
	}

	public int getCurrentPageNo() {
		return currentPageNo;
	}

	public void setCurrentPageNo(int currentPageNo) {
		this.currentPageNo = currentPageNo;
	}

	public int getPageListLength() {
		return pageListLength;
	}

	public List<AuthorInfo_Relevant> getAuthorList() {
		return this.authorInfo_List;
	}

	public List<String> getPageNoList() {
		return PageNoList;
	}

	public void setPageNoList(List<String> pageNoList) {
		PageNoList = pageNoList;
	}

	public static void prt(Object o) {
		System.out.println(o);
	}

}
