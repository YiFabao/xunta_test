package so.xunta.topic;

public class TopicMember {
	public int id;
	public String topic_id;
	public String topic_member_id;
	public String topic_member_name;
	public String join_datetime;
	public int _exit;
	public String exit_datetime;
	
	public TopicMember() {
		super();
		// TODO Auto-generated constructor stub
	}
	public TopicMember(String topic_id,String topic_member_id,String topic_member_name,String join_datetime,int _exit,String exit_datetime) {
		this.topic_id=topic_id;
		this.topic_member_id=topic_member_id;
		this.topic_member_name=topic_member_name;
		this.join_datetime=join_datetime;
		this._exit=_exit;
		this.exit_datetime=exit_datetime;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public String getTopic_member_name() {
		return topic_member_name;
	}
	public void setTopic_member_name(String topic_member_name) {
		this.topic_member_name = topic_member_name;
	}
	public String getTopic_id() {
		return topic_id;
	}
	public void setTopic_id(String topic_id) {
		this.topic_id = topic_id;
	}
	public String getTopic_member_id() {
		return topic_member_id;
	}
	public void setTopic_member_id(String topic_member_id) {
		this.topic_member_id = topic_member_id;
	}
	public String getJoin_datetime() {
		return join_datetime;
	}
	public void setJoin_datetime(String join_datetime) {
		this.join_datetime = join_datetime;
	}
	
	public int get_exit() {
		return _exit;
	}
	public void set_exit(int _exit) {
		this._exit = _exit;
	}
	public String getExit_datetime() {
		return exit_datetime;
	}
	public void setExit_datetime(String exit_datetime) {
		this.exit_datetime = exit_datetime;
	}
}
