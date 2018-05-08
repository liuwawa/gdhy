package io.renren.modules.admin.service;

import io.renren.modules.admin.entity.DataDictionariesEntity;

import java.util.List;
import java.util.Map;

/**
 * 数据字典
 * 
 * @author itmx
 * @email itmx@foxmail.com
 * @date 2018-01-13 15:01:38
 */
public interface DataDictionariesService {
	
	DataDictionariesEntity queryObject(Long id);
	
	List<DataDictionariesEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(DataDictionariesEntity dataDictionaries);
	
	void update(DataDictionariesEntity dataDictionaries);
	
	void delete(Long id);
	
	void deleteBatch(Long[] ids);

    String queryByKey(String key, String version);
}
