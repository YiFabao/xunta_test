package so.xunta.topic.index;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.lucene.index.AtomicReaderContext;
import org.apache.lucene.search.Collector;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Scorer;

/**
 * 
 * @author Thinkpad
 * 该类在搜索后，会统计 满足搜索结果的所有话题的userList以及topicIdList
 */
public class SearchTopicCollector extends Collector{
	private IndexSearcher searcher;
	
	public Set<String> userIdSet = new HashSet<>();//保存用户id
	public Set<String> topicIdSet = new HashSet<>(); //保存话题id
	
	public Set<String> getUserIdSet() {
		return userIdSet;
	}
	public void setUserIdSet(Set<String> userIdSet) {
		this.userIdSet = userIdSet;
	}
	public Set<String> getTopicIdSet() {
		return topicIdSet;
	}
	public void setTopicIdSet(Set<String> topicIdSet) {
		this.topicIdSet = topicIdSet;
	}
	private int docBase;
	public SearchTopicCollector() {
		super();
		// TODO Auto-generated constructor stub
	}
	public SearchTopicCollector(IndexSearcher searcher) {
		this.searcher = searcher;
	}

	@Override
	public boolean acceptsDocsOutOfOrder() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void collect(int doc) throws IOException {
		int docId =doc+docBase;
		//获取userid
		String userId = searcher.doc(docId).get("userId");
		String topicId = searcher.doc(docId).get("topicId");
		userIdSet.add(userId);
		topicIdSet.add(topicId);
		
	}

	@Override
	public void setNextReader(AtomicReaderContext arc) throws IOException {
		this.docBase = arc.docBase;
		
	}

	@Override
	public void setScorer(Scorer arg0) throws IOException {
		// TODO Auto-generated method stub
		
	}

}
