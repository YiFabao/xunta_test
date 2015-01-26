package so.xunta.entity;

import java.util.Date;

public class User {
	public long id;//用户全局标识；主键
	public String xunta_username;
	public String password;
	public String email;
	public String qq_openId;
	public String qq_accessToken;
	public String weibo_uid;
	public String weibo_accessToken;
	public Date createTime; //寻他账号创建时间
	public String latestLoginTime;//最后一次登录时间
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getXunta_username() {
		return xunta_username;
	}
	public void setXunta_username(String xuntaUsername) {
		xunta_username = xuntaUsername;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getQq_openId() {
		return qq_openId;
	}
	public void setQq_openId(String qqOpenId) {
		qq_openId = qqOpenId;
	}
	public String getQq_accessToken() {
		return qq_accessToken;
	}
	public void setQq_accessToken(String qqAccessToken) {
		qq_accessToken = qqAccessToken;
	}
	public String getWeibo_accessToken() {
		return weibo_accessToken;
	}
	public void setWeibo_accessToken(String weiboAccessToken) {
		weibo_accessToken = weiboAccessToken;
	}
	
	public String getWeibo_uid() {
		return weibo_uid;
	}
	public void setWeibo_uid(String weiboUid) {
		weibo_uid = weiboUid;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getLatestLoginTime() {
		return latestLoginTime;
	}
	public void setLatestLoginTime(String latestLoginTime) {
		this.latestLoginTime = latestLoginTime;
	}
	
}
