package so.xunta.topic.model.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import so.xunta.topic.entity.SysMessage;
import so.xunta.topic.entity.TopicRequestMsg;
import so.xunta.topic.entity.TopicRequestMsgPlusTopicDetail;
import so.xunta.topic.model.MsgManager;
import so.xunta.utils.HibernateUtils;

public class MsgManagerImpl implements MsgManager{

	@Override
	public void addMsg(SysMessage sysMsg) {
		Session session = HibernateUtils.openSession();
		try {
			session.beginTransaction();
			session.save(sysMsg);//保存
			session.getTransaction().commit();
		} catch (RuntimeException e) {
			session.getTransaction().rollback();
			throw e;
		} finally {
			session.close();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SysMessage> findSysMsgByUserId(String userId) {
		Session session = HibernateUtils.openSession();
		String hql = "from SysMessage as msg where msg.toUserId=? order by msg.dateTime desc";
		Query query = session.createQuery(hql).setString(0, userId);
		return query.list();
	}

	@Override
	public void updateSysMsgToHandledByUserId(String userId) {
		Session session = HibernateUtils.openSession();
		try {
			session.beginTransaction();
			String hql = "update SysMessage as msg set msg.isHandle=1 where msg.toUserId=?";
			Query query = session.createQuery(hql).setString(0, userId);
			query.executeUpdate();
			session.getTransaction().commit();
		} catch (RuntimeException e) {
			session.getTransaction().rollback();
			throw e;
		} finally {
			session.close();
		}
	}

	@Override
	public void addTopicRequestMsg(TopicRequestMsg topicRequestMsg) {
		Session session = HibernateUtils.openSession();
		try {
			session.beginTransaction();
			session.save(topicRequestMsg);//保存
			session.getTransaction().commit();
		} catch (RuntimeException e) {
			session.getTransaction().rollback();
			throw e;
		} finally {
			session.close();
		}
		
	}

	//两表连查
	@Override
	public List<TopicRequestMsgPlusTopicDetail> findTopicRequestMsgByUserId(String toUserId) {
		Session session = HibernateUtils.openSession();
		String sql = "SELECT trm.fromUserId,trm.toUserId,trm.fromUserName,trm.toUserName,trm.topicId,trm.dateTime,trm.isAgree,topic.topicName,topic.topicContent FROM topic_request_msg as trm LEFT JOIN topic ON trm.topicId =topic.topicId WHERE trm.toUserId=? order by trm.dateTime DESC";
		Query query = session.createSQLQuery(sql).setString(0, toUserId);
		List<Object[]>  objList = query.list();
		List<TopicRequestMsgPlusTopicDetail> topicRequestMsgPlusTopicDetailList=new ArrayList<TopicRequestMsgPlusTopicDetail>();
		for(Object[] o:objList)
		{
			TopicRequestMsgPlusTopicDetail td=new TopicRequestMsgPlusTopicDetail(o[0]+"", o[1]+"", o[2]+"", o[3]+"", o[4]+"", o[5]+"", o[6]+"", o[7]+"", o[8]+"");
			topicRequestMsgPlusTopicDetailList.add(td);
		}
		return topicRequestMsgPlusTopicDetailList;
	}

	@Override
	public void updateTopicRequestMsgHandledState(String toUserId,String topicId, String state) {
		Session session = HibernateUtils.openSession();
		try {
			session.beginTransaction();
			String hql = "update TopicRequestMsg as msg set msg.isAgree=? where msg.toUserId=? and msg.topicId=?";
			Query query = session.createQuery(hql).setString(0,state).setString(1,toUserId).setString(2, topicId);
			query.executeUpdate();
			session.getTransaction().commit();
		} catch (RuntimeException e) {
			session.getTransaction().rollback();
			throw e;
		} finally {
			session.close();
		}
	}

	@Override
	public long findUnreadSysMsgNum(String toUserId) {
		Session session = HibernateUtils.openSession();
		try {
			
			session.beginTransaction();
			String hql="select count(*) from SysMessage m where m.toUserId=? and m.isHandle=0";
			org.hibernate.Query query = session.createQuery(hql);
			query.setString(0, toUserId);
			long num=(Long)query.uniqueResult();
			session.getTransaction().commit();
			return num;
		} catch (RuntimeException e) {
			session.getTransaction().rollback();
			throw e;
		} finally {
			session.close();
		}

	}

	@Override
	public long findUnreadTopicRequestMsgNum(String toUserId) {
		Session session = HibernateUtils.openSession();
		try {
			session.beginTransaction();
			String hql="select count(*) from TopicRequestMsg m where m.toUserId=? and m.isHandle='0'";
			org.hibernate.Query query = session.createQuery(hql);
			query.setString(0, toUserId);
			long num=(Long)query.uniqueResult();
			session.getTransaction().commit();
			return num;
		} catch (RuntimeException e) {
			session.getTransaction().rollback();
			throw e;
		} finally {
			session.close();
		}
	}

	@Override
	public void updateTopicRequestMsgReaded(String toUserId) {
		Session session = HibernateUtils.openSession();
		try {
			session.beginTransaction();
			String hql = "update TopicRequestMsg as msg set msg.isHandle='1' where msg.toUserId=?";
			Query query = session.createQuery(hql).setString(0,toUserId);
			query.executeUpdate();
			session.getTransaction().commit();
		} catch (RuntimeException e) {
			session.getTransaction().rollback();
			throw e;
		} finally {
			session.close();
		}
	}
	
}
