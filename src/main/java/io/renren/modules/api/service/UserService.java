package io.renren.modules.api.service;


import io.renren.modules.api.entity.UserEntity;

import java.util.List;
import java.util.Map;

/**
 * 用户
 */
public interface UserService {

	UserEntity queryObject(Long userId);
	
	List<UserEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(UserEntity userEntity);
	
	void update(UserEntity user);
	
	void delete(Long userId);
	
	void deleteBatch(Long[] userIds);

	UserEntity queryByPhone(String phone);

	/**
	 * 检查用户的状态，用于登录前
	 * @param userEntity
	 */
    void checkStatus(UserEntity userEntity);

	/**
	 * 检查用户的密码是否正确，用于登录
	 * @param userEntity
	 * @param password
	 */
	void checkPwd(UserEntity userEntity, String password);
	/**
	 * 修改密码
	 * @param userEntity
	 * @param password
	 */
    void changePwd(UserEntity userEntity, String password);

	/**
	 * 修改手机
	 * @param userEntity
	 * @param phone
	 */
	void changePhone(UserEntity userEntity, String phone);

	/**
	 * 通过微信openid查询出用户
	 * @param openid
	 * @return
	 */
    UserEntity queryByWeChat(String openid);

	/**
	 * 通过支付宝用户id查出用户
	 * @param userId
	 * @return
	 */
	UserEntity queryByAlipay(String userId);
}
