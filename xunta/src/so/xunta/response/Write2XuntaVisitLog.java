package so.xunta.response;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.junit.Test;

import so.xunta.ipseeker.IPSeeker;
import so.xunta.localcontext.LocalContext;
import so.xunta.utils.HibernateUtils;

import com.aigine.common.MyTimeClass;
import com.aigine.common.p;

public class Write2XuntaVisitLog{
	public static void addLog(String ip,String keywords, String searchmode,String codeName,int usedTime)
	{   p.prt("addLog() - ip:"+ip); 
		//指定纯真数据库的文件名，所在文件夹  
        IPSeeker ipSeeker=new IPSeeker("QQWry.Dat",LocalContext.IPSeekerLibLocation);  
		String country = ipSeeker.getIPLocation(ip).getCountry();
		String region = ipSeeker.getIPLocation(ip).getArea();
		System.out.println(country+":"+region); 
		//Configuration －－>读取hibernate.cfg.xml配置文件的类
		//Configuration cfg=new Configuration().configure();
		//SessionFactory -->一个这样的类对应一个数据库
		//SessionFactory sessonFactory=cfg.buildSessionFactory();
		//Session -->与数据库的一个连接,有增删 改查的方法
		Session session=HibernateUtils.openSession();
		//Session session=sessonFactory.openSession();
		//Transaction -->事务的管理
		XunTaVisitLog visitLog=new XunTaVisitLog();
		Transaction tran=null;
		try
		{
			tran=session.beginTransaction();//开启事务
			//数据的增删改查
			visitLog.setIp(ip);
			visitLog.setCountry(country);
			visitLog.setRegion(region);
			visitLog.setKeywords(keywords);
			visitLog.setSearchmode(searchmode);
			visitLog.setDate(MyTimeClass.intCurrentDate());
			visitLog.setTime(MyTimeClass.intCurrentTime());
			visitLog.setCode(codeName);
			visitLog.setUsedtime(usedTime);
			session.save(visitLog);
			tran.commit();
		}catch(HibernateException e){
			tran.rollback();
		}finally{
			session.close();
		}
	}
	
	@Test
	public void createTable()
	{
		//Configuration －－>读取hibernate.cfg.xml配置文件的类
		Configuration cfg=new Configuration().configure();
		//SchemaExport
		SchemaExport se=new SchemaExport(cfg);
		se.create(true, true);
		System.out.println("ok");
	}
}
