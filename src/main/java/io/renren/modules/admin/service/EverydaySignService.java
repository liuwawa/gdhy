package io.renren.modules.admin.service;

import io.renren.modules.admin.entity.EverydaySignEntity;
import io.renren.modules.api.entity.SignRecord;

import java.util.List;
import java.util.Map;

/**
 * 签到奖励
 * 
 * @author itmx
 * @email itmx@foxmail.com
 * @date 2018-01-04 10:36:14
 */
public interface EverydaySignService {
	
	EverydaySignEntity queryObject(Integer days);
	
	List<EverydaySignEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(EverydaySignEntity everydaySign);
	
	void update(EverydaySignEntity everydaySign);
	
	void delete(Integer days);
	
	void deleteBatch(Integer[] dayss);

    void createNewEntity(Long userId);

    SignRecord querySignRecord(Long userId);

}
