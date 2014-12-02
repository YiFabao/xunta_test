package so.xunta.ipseeker;

import so.xunta.localcontext.LocalContext;
    
public class IPSeeker_Main_Test {
    

        
  public static void main(String arg[]){  
                  //指定纯真数据库的文件名，所在文件夹  
          IPSeeker ip=new IPSeeker("QQWry.Dat",LocalContext.IPSeekerLibLocation);  
           //测试IP 58.20.43.13  //:127.0.0.1 //42.121.136.225
  //System.out.println(ip.getIPLocation("58.20.43.13").getCountry()+":"+ip.getIPLocation("58.20.43.13").getArea());  
  System.out.println(ip.getIPLocation("127.0.0.1").getCountry()+":"+ip.getIPLocation("42.121.136.225").getArea());  
    
  }  
    


}  


    
    
    
