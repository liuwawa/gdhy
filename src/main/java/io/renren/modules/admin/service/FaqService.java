package io.renren.modules.admin.service;

import io.renren.modules.admin.entity.FaqEntity;

import java.util.List;
import java.util.Map;

/**
 * 问题列表
 * 
 * @author itmx
 * @email itmx@foxmail.com
 * @date 2017-12-20 16:43:30
 */
public interface FaqService {
	
	FaqEntity queryObject(Integer id);
	
	List<FaqEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(FaqEntity faq);
	
	void update(FaqEntity faq);
	
	void delete(Integer id);
	
	void deleteBatch(Integer[] ids);

	List<FaqEntity> queryByType(Integer type, Integer page, Integer limit);
}
