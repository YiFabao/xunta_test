package so.xunta.topic;

import java.util.ArrayList;
import java.util.List;

public class Thesuraus {
	
	public String word;
	public List<String> theseuras=new ArrayList<String>();
	
	public Thesuraus(String word2) {
		this.word=word2;
	}

	public void addTheseurasWord(String word)
	{
		theseuras.add(word);
	}
}
