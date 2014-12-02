package so.xunta.people.relevant;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.apache.lucene.search.Collector;
import com.aigine.common.p;

import so.xunta.people.newest.AuthorInfo_Newest;
import so.xunta.people.newest.Collector_Newest;
import so.xunta.people.newest.Collector_try;
import so.xunta.response.SearchMethods;
import so.xunta.response.PeopleSearchVars;

//该grouping使用了我自定义的collector类.
//原来走通了,后来分解成多个方法,就报错了.15JAN2013
//apr13:authorRanking()中增加了过滤标题为空的文档的判断条件:title.matches("doesNotMatchAnyCell_Xu")
//增加了conditioningSearchKeywords()方法.
//apr17://增加了一个同名同参的用标准TopDocs实现搜索和排序的searchPeople()方法.原来的方法暂时注释掉.
//增加了相对应的搜索调用方法search2TopDocs()和排序方法authorRanking_viaTopDocs.
//apr22:重回自定义collector类,采用了fieldcache后速度明显提高.
//may27: 每次搜索都重建indexwriter,以及时反应索引的新增文档,达到实时性.但重建会占用几十MS时间,以后再改进.
//翻页时不再接着翻页,而是重新搜索一下.这样不同用户之间不会再"串"数据了.
//2013.6.5//每次搜索不再重建indexsearcher(only searchPeople() modified around writer create and close). 建索引时只要commit,indexreader不用重建就可以读到. 速度重回几十ms的速度.
//2013.6.5//连续换页时, 会出现作者与内容错搭的情况.以前出现过,忘了怎么解决的了.//好象是readersearcher的segment区块指向乱了.
//2013.6.22-增加了:IndexSearcher createSearcher(Similarity sim){//创建索引时指定计分算法
//2013.6.28-注释掉了利用topN搜索的几个方法.
//注释掉了authorIdMap变量.
//增设了两个变量的赋值:totalDocs=collector_xu.getTotalDocs();totalAuthors=collector_xu.getTotalAuthors(); 
//2013.9.7-

public class SearchPeople_Relevant {

	// 通过自定义collector实现的搜索:
	public static void searchPeople(PeopleSearchVars sV) throws IOException, Exception {

		Map<String, AuthorInfo_Relevant> map = new HashMap<String, AuthorInfo_Relevant>();

		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		Calendar rightnow = Calendar.getInstance();
		rightnow.setTime(date);//当前时间
		//确定搜索的始末时间
		String endPublishDate = sdf.format(date);//当前时间
		rightnow.add(Calendar.MONTH, -3);
		String beginPublishDate=sdf.format(rightnow.getTime());//开始时间
		
	
		System.out.println("第1次搜索,试搜一个月的文档数");
		long t1=System.currentTimeMillis();
		Collector_try collector_try=new Collector_try();//生成只用于记录文档数的Collector_try
		//生成query
		sV.query = SearchMethods.createQuery(sV.searchKeywords, beginPublishDate, endPublishDate, "yifabao");
		
		collector_try = (Collector_try) SearchMethods.search2C(sV.searcher, sV.query, collector_try);//一次试搜
		sV.totalDocs = collector_try.totalDocs;// 总的文档数
		long t2=System.currentTimeMillis();
		System.out.print("第一次试搜用时："+(t2-t1));
		System.out.println("总文档数："+sV.totalDocs);
		
		//根据总文档数/6个月==文档数/月
		int average=sV.totalDocs/3+1;
		System.out.println("平均文档数："+average);
		//average 可能有两种情况:1.为0　2.为非0
		if(sV.totalDocs==0)
		{
			//System.out.println("总文档数为0，此时默认为搜索全部");
			sV.query = SearchMethods.createQuery(sV.searchKeywords);
		}
		else
		{
			//average为非数，算出要搜索得出不超过最大文档数的月数
			int numMonths=(int) Math.ceil((double)100000/average);
			//numMonths可能会非常大，如果numMonths/12>100,令numMonths=12*100
			System.out.println("numMonths:"+numMonths);
			if(numMonths>1200)
			{
				numMonths=1200;
			}

			//确定搜索的时间范围　
			rightnow.add(Calendar.MONTH, -(numMonths-3));
			beginPublishDate=sdf.format(rightnow.getTime());
			//生成时间范围的query
			sV.query = SearchMethods.createQuery(sV.searchKeywords, beginPublishDate, endPublishDate, "yifabao");
		}
		
		// 生成collector

		Collector_Relevant_yi collector_relevant = new Collector_Relevant_yi();
		collector_relevant = (Collector_Relevant_yi) SearchMethods.search2C(sV.searcher, sV.query, collector_relevant);
		
		double sumtime1=collector_relevant.sumTime1;
		double sumtime2=collector_relevant.sumTime2;
		double sumtime3=collector_relevant.sumTime3;
		double sumtime5=collector_relevant.sumTime5;
		System.out.println("执行collector 中setNextReader()方法总用时:"+sumtime1+" collector中获取具体作者名称，网站名称用时:"+sumtime2+ "  authorScoreAddup()用时:"+sumtime3);
		System.out.println("sumTime5:"+sumtime5);
		
		sV.totalDocs = ((Collector_Relevant_yi) collector_relevant).getTotalDocs();// 总的文档数
		sV.totalAuthors = ((Collector_Relevant_yi) collector_relevant).getTotalAuthors();// 总的作者数
		
		if(sV.totalDocs==0)
		{
			sV.query = SearchMethods.createQuery(sV.searchKeywords);
			collector_relevant = (Collector_Relevant_yi) SearchMethods.search2C(sV.searcher, sV.query, collector_relevant);
			sV.totalDocs = ((Collector_Relevant_yi) collector_relevant).getTotalDocs();// 总的文档数
			sV.totalAuthors = ((Collector_Relevant_yi) collector_relevant).getTotalAuthors();// 总的作者数
		}
		map.putAll(((Collector_Relevant_yi) collector_relevant).getAuthorScoreMap());

		long t4 = System.currentTimeMillis();
		for (Map.Entry<String, AuthorInfo_Relevant> authorId_Info : map.entrySet()) {
			// prt("转到authorrank过程中:"+authorId_Info.getValue().totalScore+"|"
			// +authorId_Info.getKey());
			sV.authorRanking_Relevant.add(authorId_Info.getValue());// 这个过程进行了一次排序.
		}
		long t5 = System.currentTimeMillis();
		System.out.println("for (Map.Entry<String, AuthorInfo_Relevant> authorId_Info : map.entrySet()) 排序用时:" + (t5 - t4));

		p.prt("空闲内存freeMemeory：" + Runtime.getRuntime().freeMemory() / 1048576 + "M\t最大内存maxMemory：" + Runtime.getRuntime().maxMemory() / 1048576
				+ "M\t总内存totalMemory:" + Runtime.getRuntime().totalMemory() / 1048576 + "M");
		p.prt("作者总数-totalAuthors:" + sV.totalAuthors + "个");// TODO 要删掉
		p.prt("文档总数-totalDocs:" + sV.totalDocs + "个");// TODO 要删掉
		
		if(average!=0)
		{
			sV.query = SearchMethods.createQuery(sV.searchKeywords, "yifabao");
			sV.timepoint=beginPublishDate;
		}
	}

	public static void main(String[] args) throws Exception {
		long begin = System.currentTimeMillis();
		long end;
		// searcher=createSearcher();
		// query=createQuery("msp430*");
		// 生成collector
		// Collector collector_relevant=new
		// Collector_Relevant();//这个collector不能设为静态属性,否则出现底层错误.
		// collector_relevant=search2C(searcher,query,collector_relevant);
		end = System.currentTimeMillis();
		System.out.println("搜索之后:" + (end - begin) + "ms");
		// collector排序
		// authorRanking(collector_xu);
		end = System.currentTimeMillis();
		System.out.println("排序之后 :" + (end - begin) + "ms");
		// 打印结果.
		// outputAuthorRanking();
		end = System.currentTimeMillis();
		System.out.println("输出之后 :" + (end - begin) + "ms");
		// reader.close();//如果关闭,则无法翻页了.
	}

}
