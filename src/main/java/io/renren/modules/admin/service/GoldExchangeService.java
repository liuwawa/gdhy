package io.renren.modules.admin.service;

import io.renren.modules.admin.entity.GoldExchangeEntity;

import java.util.List;
import java.util.Map;

/**
 * 金币兑换
 * 
 * @author itmx
 * @email itmx@foxmail.com
 * @date 2018-01-08 09:09:41
 */
public interface GoldExchangeService {
	
	GoldExchangeEntity queryObject(Integer rmb);
	
	List<GoldExchangeEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(GoldExchangeEntity goldExchange);
	
	void update(GoldExchangeEntity goldExchange);
	
	void delete(Integer rmb);
	
	void deleteBatch(Integer[] rmbs);
}
