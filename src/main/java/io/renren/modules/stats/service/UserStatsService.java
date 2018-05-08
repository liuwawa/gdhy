package io.renren.modules.stats.service;

import io.renren.modules.stats.entity.UserStatsEntity;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 用户增长情况
 * 
 * @author itmx
 * @email itmx@foxmail.com
 * @date 2018-02-05 11:20:29
 */
public interface UserStatsService {
	
	UserStatsEntity queryObject(Integer id);
	
	List<UserStatsEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(UserStatsEntity userStats);
	
	void update(UserStatsEntity userStats);
	
	void delete(Integer id);
	
	void deleteBatch(Integer[] ids);

    UserStatsEntity preHour();

	int countAll();

	Integer countUserByTime(Date a, Date b);

	List<UserStatsEntity> queryByRange(String type, Date a, Date b);

    List<UserStatsEntity> RangeByHour(String type, String s, String s1, int hours, int hours1);

	List<UserStatsEntity> RangeByCustom(String type, String s, String s1, String field);

	List<UserStatsEntity> RangeByCustom(String type, Date s, Date s1, String field);

}
