package so.xunta.people.newest;

import java.io.IOException;

import org.apache.lucene.index.AtomicReaderContext;
import org.apache.lucene.search.Collector;
import org.apache.lucene.search.Scorer;

public class Collector_try  extends Collector{
	
	public int totalDocs;

	@Override
	public boolean acceptsDocsOutOfOrder() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void collect(int arg0) throws IOException {
		// TODO Auto-generated method stub
		totalDocs++;
	}

	@Override
	public void setNextReader(AtomicReaderContext arg0) throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setScorer(Scorer arg0) throws IOException {
		// TODO Auto-generated method stub
		
	}

}
