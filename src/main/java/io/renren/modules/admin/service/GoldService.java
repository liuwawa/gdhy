package io.renren.modules.admin.service;

import io.renren.modules.admin.entity.GoldEntity;

import java.util.List;
import java.util.Map;

/**
 * 用户金币
 * 
 * @author itmx
 * @email itmx@foxmail.com
 * @date 2017-12-24 17:59:05
 */
public interface GoldService {
	
	GoldEntity queryObject(Long id);
	
	List<GoldEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);

    void createNewEntity(Long userId);

    int resetToday();

    int modifySurplusGold(Long userId, Integer increase, String tip);

   
    void updateTodayReadByUserId(Long userId, Integer reward);

    void updateTodayVedioByUserId(Long userId, Integer reward);
}
