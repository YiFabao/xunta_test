package so.xunta.topic;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.highlight.Fragmenter;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.search.highlight.SimpleSpanFragmenter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.wltea.analyzer.lucene.IKAnalyzer;

import so.xunta.localcontext.LocalContext;
import so.xunta.utils.DateTimeUtils;
import so.xunta.utils.HibernateUtils;

public class TopicManagerImpl implements TopicManager {
	static Analyzer analyzer = new IKAnalyzer();// IK分词器
	Directory directory =null;
	@Override
	public  List<Topic> matchMyTopic(String mytopic) {
		List<Topic> topicList=new ArrayList<>();
		try {
			List<String> q=showTerms(mytopic,analyzer);
			Map<String,Thesuraus> map=FileUtil.getInstance().map;
			BooleanQuery query=new BooleanQuery();
			for(String t:q)
			{
				//1.获取每个词的同义词
				Thesuraus thesuraus=map.get(t);
				//获取每个词的词关系
				if(thesuraus!=null)
				{
					List<String> list=thesuraus.theseuras;
					TermQuery tq1=new TermQuery(new Term("topic",t));
			
					BooleanQuery booleanQuery=new BooleanQuery();
					booleanQuery.add(tq1, Occur.SHOULD);
					for(String s:list)
					{
						TermQuery tq2=new TermQuery(new Term("topic",s));
						booleanQuery.add(tq2,Occur.SHOULD);
					}
					query.add(booleanQuery,Occur.SHOULD);
				}
				else
				{
					//没有同义词
					TermQuery tq1=new TermQuery(new Term("topicContent",t));
					query.add(tq1,Occur.SHOULD);
				}
			}
			if(directory==null)
			{
				directory = FSDirectory.open(new File(LocalContext.indexFilePath));
			}
		    DirectoryReader ireader = DirectoryReader.open(directory);
		    IndexSearcher searcher = new IndexSearcher(ireader);
			ScoreDoc[] hits=searcher.search(query,100).scoreDocs;
			
			for (int i = 0; i < hits.length; i++) {
				int docID = hits[i].doc;
				//话题唯一id
				String topicId=searcher.doc(docID).get("topicId");
				//匹配的话题
				String topicContent = searcher.doc(docID).get("topicContent");
				//作者名
				String topicAuthor = searcher.doc(docID).get("topicAuthorName");
				//作者id
				String topicAuthorId = searcher.doc(docID).get("authorId");
				
				//日期
				String datetime = searcher.doc(docID).get("topicCreatetime");
				//匹配的话题高亮
				String hightLightTopic = highLighter(topicContent, query, analyzer, 10, 10);
				//(String topicId,String authorId,String hightLightTopic, String authorName,String topicCreatetime) 
				Topic topicObj = new Topic(topicId,topicAuthorId,hightLightTopic,topicAuthor,datetime);
				
				topicList.add(topicObj);
			}
			ireader.close();//关闭ireader
		} catch (IOException e) {
			e.printStackTrace();
		}
		Collections.sort(topicList);
		return topicList;
	}

	/**
	 * 分词
	 * @param topic
	 * @param analyzer
	 * @return
	 * @throws IOException
	 */
	private static List<String> showTerms(String topic, Analyzer analyzer) throws IOException {
		TokenStream tokenStream = analyzer.tokenStream("topic", new StringReader(topic));

		CharTermAttribute term = tokenStream.addAttribute(CharTermAttribute.class);

		tokenStream.reset();
		
		List<String> q=new ArrayList<String>();
		while (tokenStream.incrementToken()) {
			System.out.print(term + "\t");
			q.add(term.toString());
		}
		return q;
	}
	/**
	 * 高度
	 * @param textToBeHighLighted
	 * @param query
	 * @param analyzer
	 * @param numOfHighlightWords
	 * @param numOfHightlightedKeywords
	 * @return
	 */
	public static String highLighter(String textToBeHighLighted, Query query, Analyzer analyzer, int numOfHighlightWords, int numOfHightlightedKeywords) {
		QueryScorer scorer = new QueryScorer(query);
		Fragmenter fragmenter = new SimpleSpanFragmenter(scorer, numOfHighlightWords);// 后一个参数是加亮前后的保留字数
		SimpleHTMLFormatter formatter = new SimpleHTMLFormatter("<font color='red'>", "</font>");
		Highlighter hig = new Highlighter(formatter, scorer);
		hig.setTextFragmenter(fragmenter);
		TokenStream tokens = null;
		String highterResult = null;
		try {
			tokens = analyzer.tokenStream("index_content", new StringReader(textToBeHighLighted));
			highterResult = hig.getBestFragments(tokens, textToBeHighLighted, numOfHightlightedKeywords, "...");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InvalidTokenOffsetsException e) {
			e.printStackTrace();
		}
		return highterResult;
	}

	@Override
	public void createTopicIndex(String topicId, String topicContent, String authorId,String topicAuthorName, String topicCreatetime) {
		try {
			if(directory==null)
			{
				directory = FSDirectory.open(new File(LocalContext.indexFilePath));
			}
		    IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_43, analyzer);
		    IndexWriter iwriter = new IndexWriter(directory, config);
		    
		    Document doc = new Document();
		    
		    FieldType fieldType1=new FieldType();
		    fieldType1.setIndexed(true);
		    fieldType1.setStored(true);
		    fieldType1.storeTermVectors();
		    fieldType1.storeTermVectorPositions();
		    fieldType1.storeTermVectorPayloads();
		    
		    FieldType fieldType2=new FieldType();
		    fieldType2.setIndexed(true);
		    fieldType2.setStored(true);
		    fieldType2.setTokenized(false);
		    
		    doc.add(new Field("topicId",topicId,fieldType2));
		    doc.add(new Field("authorId",authorId,fieldType2));
		    doc.add(new Field("topicCreatetime",topicCreatetime,fieldType1));
		    doc.add(new Field("topicAuthorName",topicAuthorName,fieldType1));
		    doc.add(new Field("topicContent",topicContent,fieldType1));
		    iwriter.addDocument(doc);
		    iwriter.commit();
		    iwriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	public static void main(String[] args) {
		TopicManager topicmanager=new TopicManagerImpl();
/*		topicmanager.createTopicIndex("1","黄山哪好玩","001","Candy","2014-1-1");
		topicmanager.createTopicIndex("2","黄山登山谁去","001","Candy","2014-1-2");
		topicmanager.createTopicIndex("3","过年大家都在做什么？","001","Candy","2014-1-2");
		List<Topic> list=topicmanager.matchMyTopicByUserId("001");
		for(Topic t:list)
		{
			System.out.println("话题ID:"+t.topicId);
			System.out.println("话题内容:"+t.topicContent);
			System.out.println("话题创建时间:"+t.topicCreatetime);
			System.out.println("话题作者昵称:"+t.authorName);
			System.out.println("话题作者id:"+t.authorId);
			System.out.println("===========================");
		}*/
		Topic topic=new Topic("topic1","001","今天想吃点什么","易发宝","2015-1-9 16:46:08");
		topicmanager.saveTopic(topic);
		System.out.println("ok");
	}

	@Override
	public List<Topic> matchMyTopicByUserId(String authorId) {
		List<Topic> topicList=new ArrayList<>();
		try {
			TermQuery query=new TermQuery(new Term("authorId",authorId));
			if(directory==null)
			{
				directory = FSDirectory.open(new File(LocalContext.indexFilePath));
			}
		    DirectoryReader ireader = DirectoryReader.open(directory);
		    IndexSearcher searcher = new IndexSearcher(ireader);
			ScoreDoc[] hits=searcher.search(query,100).scoreDocs;
			
			for (int i = 0; i < hits.length; i++) {
				int docID = hits[i].doc;
				//话题唯一id
				String topicId=searcher.doc(docID).get("topicId");
				//匹配的话题
				String topicContent = searcher.doc(docID).get("topicContent");
				//作者名
				String topicAuthor = searcher.doc(docID).get("topicAuthorName");
				//作者id
				String topicAuthorId = searcher.doc(docID).get("authorId");
				
				//日期
				String datetime = searcher.doc(docID).get("topicCreatetime");

				//(String topicId,String authorId,String hightLightTopic, String authorName,String topicCreatetime) 
				Topic topicObj = new Topic(topicId,topicAuthorId,topicContent,topicAuthor,datetime);
				
				topicList.add(topicObj);
			}
			ireader.close();//关闭ireader
		} catch (IOException e) {
			e.printStackTrace();
		}
		Collections.sort(topicList);
		return topicList;
	}


	@Override
	public void saveTopic(Topic topic) {
		Session session = HibernateUtils.openSession();
		try {
			session.beginTransaction();
			session.save(topic);
			session.getTransaction().commit();
		} catch (RuntimeException e) {
			session.getTransaction().rollback();
			throw e;
		} finally {
			session.close();
		}
	}
	@Override
	public void saveTopicMember(TopicMember topicMember) {
		Session session = HibernateUtils.openSession();
		try {
			session.beginTransaction();
			session.save(topicMember);
			session.getTransaction().commit();
		} catch (RuntimeException e) {
			session.getTransaction().rollback();
			throw e;
		} finally {
			session.close();
		}
	}

	@Override
	public List<MessageAlert> searchMyMessage(String authorId) {
		//TODO 从数据库中查询出自己的消息
		System.out.println("从数据库中查询出自己的消息(方法未具体实现)");
		MessageAlert m1=new MessageAlert("8","test1","10","6EDDD8B52589CFF90723C6E579355AC8","",DateTimeUtils.getCurrentTimeStr());
		MessageAlert m2=new MessageAlert("8","test1","10","6EDDD8B52589CFF90723C6E579355AC8","",DateTimeUtils.getCurrentTimeStr());
		m2.setIsHandle(1);
		List<MessageAlert> l=new ArrayList<MessageAlert>();
		l.add(m1);
		l.add(m2);
		Collections.sort(l);
		return l;
		
	}

	@Override
	public void addMessageAlert(MessageAlert messageAlert) {
		System.out.println("添加消息提醒");
		Session session = HibernateUtils.openSession();
		try {
			session.beginTransaction();
			session.save(messageAlert);
			session.getTransaction().commit();
		} catch (RuntimeException e) {
			session.getTransaction().rollback();
			throw e;
		} finally {
			session.close();
		}
	}

	@Override
	public void addTopicHistory(TopicHistory topicHistory) {
		System.out.println("添加话题历史");
		Session session = HibernateUtils.openSession();
		try {
			session.beginTransaction();
			session.save(topicHistory);
			session.getTransaction().commit();
		} catch (RuntimeException e) {
			session.getTransaction().rollback();
			throw e;
		} finally {
			session.close();
		}
	}

	@Override
	public int searchNotReadmessageNum(String authorId) {
		System.out.println("查询未读消息数");
		return 0;
	}

	@Override
	public boolean checkIsTopicMember(String memberId, String topicId) {
		Session session = HibernateUtils.openSession();
		try {
			session.beginTransaction();
			org.hibernate.Query query=session.createQuery("from TopicMember as tm where tm.topic_member_id=? and tm.topic_id=?");
			query.setString(0,memberId);
			query.setString(1,topicId);
			List<TopicMember> topicMemberList=query.list();
			session.getTransaction().commit();
			if(topicMemberList.size()>0)
			{
				return true;
			}
		} catch (RuntimeException e) {
			session.getTransaction().rollback();
			throw e;
		} finally {
			session.close();
		}
		return false;
	}

	@Override
	public List<TopicMember> searchTopicMemberList(String topicId) {
		
		List<TopicMember> topicMembers=new ArrayList<TopicMember>();
		Session session = HibernateUtils.openSession();
		try {
			session.beginTransaction();
			String hql="from TopicMember tm where tm.topic_id=?";
			org.hibernate.Query query=session.createQuery(hql);
			topicMembers=query.setString(0,topicId).list();
			session.getTransaction().commit();
		} catch (RuntimeException e) {
			session.getTransaction().rollback();
			throw e;
		} finally {
			session.close();
		}
		return topicMembers;
	}

	@Override
	public String searchTopicContent(String topicId) {
		Session session = HibernateUtils.openSession();
		try {
			session.beginTransaction();
			String hql="select topicContent from Topic t where t.topicId=?";
			org.hibernate.Query query=session.createQuery(hql);
			query.setString(0, topicId);
			String topicContent=(String) query.uniqueResult();
			session.getTransaction().commit();
			return topicContent;
		} catch (RuntimeException e) {
			session.getTransaction().rollback();
			throw e;
		} finally {
			session.close();
		}
	}

	@Override
	public Topic searchLatestTopic(String authorId) {
		Session session = HibernateUtils.openSession();
		try {
			session.beginTransaction();
			String hql="from Topic t where t.authorId=? order by t.topicCreatetime desc";
			org.hibernate.Query query=session.createQuery(hql);
			query.setString(0, authorId);
			query.setMaxResults(1);
			Topic topic=(Topic)query.uniqueResult();
			return topic;
		} catch (RuntimeException e) {
			session.getTransaction().rollback();
			throw e;
		} finally {
			session.close();
		}
	}

	@Override
	public List<Topic> searchTopicHistory(String authorId) {
		Session session = HibernateUtils.openSession();
		try {
			session.beginTransaction();
			String sql="select topic.* from topic,topichistory where topic.authorId=? and topic.topicId=topichistory.topicId ";
			SQLQuery sqlquery=session.createSQLQuery(sql);
			sqlquery.addEntity(Topic.class);
			sqlquery.setString(0,authorId);
			List<Topic> topicList=sqlquery.list();
			return topicList;
			
		} catch (RuntimeException e) {
			session.getTransaction().rollback();
			throw e;
		} finally {
			session.close();
		}
	}
}
