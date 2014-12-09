package so.xunta.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

public class TextUtil {
	public static List<JSONObject> readFileMethod()
	{
		List<JSONObject> ip_searchkeyword=new ArrayList<JSONObject>();
		try {
			BufferedReader bf=new BufferedReader(new FileReader(new File("/data/lvyouSearchWord.txt")));
			//BufferedReader bf=new BufferedReader(new FileReader(new File("C:/Users/Thinkpad/Desktop/lvyouSearchWord.txt")));
			String line=bf.readLine();
			while(line!=null)
			{
				if(!"".equals(line.trim()))
				{
					String[] ss=line.split(",");
					//System.out.println(ss[0]+"==>"+ss[1]);
					JSONObject json=new JSONObject();
					json.append("ipaddress", ss[0]);
					json.append("searchKeywords",ss[1]);
					
					ip_searchkeyword.add(json);
				}
				line=bf.readLine();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ip_searchkeyword;
	}
	
	public static void main(String[] args) throws JSONException {
		List<JSONObject> json_list=readFileMethod();
		for(JSONObject j:json_list)
		{
			System.out.println(j);
		}
	}


}
