package so.xunta.topic.utils;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import so.xunta.utils.HibernateUtils;

public class HibernateUtils_TOPIC {
	private static SessionFactory sessionFactory_TOPIC=null;
	static{
		Configuration cfg = new Configuration().configure("server3_hibernate.cfg.xml");
		sessionFactory_TOPIC=cfg.buildSessionFactory();
	}
	public static SessionFactory getSessionFactory() {
		return sessionFactory_TOPIC;
	}
	public static void setSessionFactory(SessionFactory sessionFactory) {
		HibernateUtils_TOPIC.sessionFactory_TOPIC = sessionFactory;
	}
	public static Session openSession()
	{
		return sessionFactory_TOPIC.openSession();
	}
}
