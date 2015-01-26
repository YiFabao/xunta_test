package so.xunta.manager;

import java.util.List;

import so.xunta.entity.User;

public interface UserManager {
	/**
	 * 利用第三方 API获取第三方用户信息
	 * @param token
	 * @param mark 需要指定mark=qq|wb
	 * @return
	 */
	public abstract User getUser(String token,String mark);
	
	/**
	 * 添加用户
	 * @param user 添加用户，将user保存到数据库
	 */
	public abstract void addUser(User user);
	
	/**
	 * 更新用户
	 * @param user
	 */
	public abstract void updateUser(User user);

	/**
	 * 检查用户是否存在
	 */
	public User checkRegisterUserExist(String xunta_username,String password);

	/**
	 * 查询用户是否存在
	 * 
	 */
	public User findUser(String xunta_username);
	
	/**
	 * 查询email是否存在
	 */
	public User findUserByEmail(String email);
	
	/**
	 * 查询数据库中qq_openId是否存在
	 * 
	 */
	 public User findUserbyQQOpenId(String qq_openId);
	
	 /**
	  * 查询数据库中weibo_uid是否存在
	  */
	 public User findUserByWeiboUid(String weibo_uid);
	
	//根据List<userID> 查询出List<User>
	public List<User> findUserListByUserIdList(List<Long> userIdList);
	//根据用户ID查询出用户
	public User findUserById(int userId);
	
}
