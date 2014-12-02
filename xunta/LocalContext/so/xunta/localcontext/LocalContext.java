package so.xunta.localcontext;

public class LocalContext {
	public static String jspFilePath="/jsp/search_resultpage/";
	
/*	//本地测试的本地索引文件路径
	public static String root="d:/";
	public static String indexFilePath="D:\\data\\luceneIndex_new\\travel";*/
	
	//上传到阿里云服务器时，用这个路径
	public static String root="/";
	public static String indexFilePath=root+"data/luceneIndex/travel";
	
	
	public static final String IPSeekerLibLocation=root+"data/qqwry";
	//保存的文件夹  
	public static final String INSTALL_DIR=root+"data/qqwry";
    //纯真IP数据库名  
    public static final String IP_FILE="QQWry.dat";  
    
}
