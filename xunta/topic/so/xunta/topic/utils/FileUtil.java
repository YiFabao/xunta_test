package so.xunta.topic.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import so.xunta.topic.Thesuraus;

public class FileUtil {

	public static Map<String, Thesuraus> map = new HashMap<String, Thesuraus>();

	private FileUtil (){};
	
	private static FileUtil instance=null;
	
	public static FileUtil getInstance()
	{
		if(instance==null)
		{
			instance=new FileUtil();
			instance.readTheseurasTxt();
		}
		return instance;
	}
	
	public void readTheseurasTxt() {
		File file = new File("c:\\Users\\ThinkPad\\DeskTop\\Thesuraus.txt");
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String tempString = null;
			Thesuraus thesuraus=null;
			while ((tempString = reader.readLine()) != null) {
				if(tempString.charAt(0)=='#')
				{
					if(thesuraus!=null)
					{
						map.put(thesuraus.word,thesuraus);
					}
					thesuraus=new Thesuraus(tempString.substring(1));
				}
				else
				{
					thesuraus.addTheseurasWord(tempString);
				}			
			}


		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		
		FileUtil fu=new FileUtil();
		fu.readTheseurasTxt();
		Map<String,Thesuraus> map=fu.map;
		Thesuraus t=map.get("年关");
		if(t==null)return;
		for(String s:t.theseuras)
		{
			System.out.println(s);
		}
	}
}
