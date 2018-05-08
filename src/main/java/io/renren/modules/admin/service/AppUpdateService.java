package io.renren.modules.admin.service;

import io.renren.modules.admin.entity.AppUpdateEntity;

import java.util.List;
import java.util.Map;

/**
 * 版本更新
 * 
 * @author itmx
 * @email itmx@foxmail.com
 * @date 2018-02-07 11:06:17
 */
public interface AppUpdateService {
	
	AppUpdateEntity queryObject(Integer id);
	
	List<AppUpdateEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(AppUpdateEntity appUpdate);
	
	void update(AppUpdateEntity appUpdate);
	
	void delete(Integer id);
	
	void deleteBatch(Integer[] ids);

    AppUpdateEntity getLastVersion(String appId);
}
