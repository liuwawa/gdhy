package io.renren.modules.admin.service;

import io.renren.modules.api.entity.UserEntity;
import net.sf.json.JSONObject;

import java.util.List;
import java.util.Map;

/**
 * 用户
 * 
 * @author itmx
 * @email itmx@foxmail.com
 * @date 2017-12-16 15:08:17
 */
public interface ManagerUserService {
	
	UserEntity queryObject(Long userId);
	
	List<UserEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(UserEntity user);
	
	void update(UserEntity user);
	
	void delete(Long userId);
	
	void deleteBatch(Long[] userIds);

	void updatedisable(JSONObject dailydata);
}
