package io.renren.modules.admin.service;

import io.renren.modules.admin.entity.AppLogEntity;

import java.util.List;
import java.util.Map;

/**
 * 用户日志
 * 
 * @author itmx
 * @email itmx@foxmail.com
 * @date 2017-12-19 14:46:43
 */
public interface AppLogService {
	
	AppLogEntity queryObject(String id);
	
	List<AppLogEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(AppLogEntity userLog);
	
	void update(AppLogEntity userLog);
	
	void delete(String id);
	
	void deleteBatch(String[] ids);
}
