package io.renren.modules.admin.service;

import io.renren.modules.admin.entity.FaqTypeEntity;

import java.util.List;
import java.util.Map;

/**
 * 问题分类
 * 
 * @author itmx
 * @email itmx@foxmail.com
 * @date 2017-12-20 16:43:30
 */
public interface FaqTypeService {
	
	FaqTypeEntity queryObject(Integer id);
	
	List<FaqTypeEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(FaqTypeEntity faqType);
	
	void update(FaqTypeEntity faqType);
	
	void delete(Integer id);
	
	void deleteBatch(Integer[] ids);

}
