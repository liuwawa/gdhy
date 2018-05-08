package io.renren.modules.stats.service;

import io.renren.modules.stats.entity.TaskActive;
import io.renren.modules.stats.entity.TaskStatsEntity;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 任务活跃情况
 * 
 * @author itmx
 * @email itmx@foxmail.com
 * @date 2018-02-05 11:20:29
 */
public interface TaskStatsService {
	
	TaskStatsEntity queryObject(Integer id);
	
	List<TaskStatsEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(TaskStatsEntity taskStats);
	
	void update(TaskStatsEntity taskStats);
	
	void delete(Integer id);
	
	void deleteBatch(Integer[] ids);

	List<TaskActive> queryTaskTypeRecordByTime(Date a, Date b);

	List<TaskActive> sumTaskActiveFromStats(String type, Date a, Date b);

    List<TaskStatsEntity> RangeByCustom(String type, String s, String s1, String file);

	List<TaskStatsEntity> RangeByHour(String type, String s, String s1, int hours, int hours1);
}
