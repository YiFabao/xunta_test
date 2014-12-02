package so.xunta.response;

/**
 * @hibernate.class table="xunta_visitlog"
 * @author Xu
 */
public class XunTaVisitLog {
	//@hibernate.id generator-class="assigned"
	private int id; // 用户ID 手动分配
	//@hibernate.property not-null="true" length="20"
	private String ip; // 昵称
	//@hibernate.property not-null="true" length="50"
	private String country;// 用户密码
	//@hibernate.property not-null="true" length="50"
	private String region;
	//@hibernate.property not-null="true" length="100"
	private String keywords;//搜索关键词组合
	//@hibernate.property length="20"
	private String searchmode;//搜索模式
	private int date;//搜索发生日期
	private int time;//搜索发生时刻
	private String code;//所在代码方法的名称.
	private int usedtime;//所在代码方法的名称.
	
	
	public int getUsedtime() {
		return usedtime;
	}

	public void setUsedtime(int usedtime) {
		this.usedtime = usedtime;
	}


	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	public String getSearchmode() {
		return searchmode;
	}

	public void setSearchmode(String searchmode) {
		this.searchmode = searchmode;
	}
	
	public int getDate() {
		return date;
	}

	public void setDate(int date) {
		this.date = date;
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
}
