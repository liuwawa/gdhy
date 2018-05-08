package io.renren.modules.admin.service;

import io.renren.modules.admin.entity.ActivityCentralEntity;

import java.util.List;
import java.util.Map;

/**
 * 活动中心
 * 
 * @author itmx
 * @email itmx@foxmail.com
 * @date 2018-01-11 17:47:17
 */
public interface ActivityCentralService {
	
	ActivityCentralEntity queryObject(Long id);
	
	List<ActivityCentralEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(ActivityCentralEntity activityCentral);
	
	void update(ActivityCentralEntity activityCentral);
	
	void delete(Long id);
	
	void deleteBatch(Long[] ids);
}
