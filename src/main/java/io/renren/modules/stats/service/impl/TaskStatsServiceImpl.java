package io.renren.modules.stats.service.impl;

import io.renren.modules.admin.entity.TaskRecordEntity;
import io.renren.modules.stats.entity.TaskActive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import io.renren.modules.stats.dao.TaskStatsDao;
import io.renren.modules.stats.entity.TaskStatsEntity;
import io.renren.modules.stats.service.TaskStatsService;



@Service("taskStatsService")
public class TaskStatsServiceImpl implements TaskStatsService {
	@Autowired
	private TaskStatsDao taskStatsDao;
	
	@Override
	public TaskStatsEntity queryObject(Integer id){
		return taskStatsDao.queryObject(id);
	}
	
	@Override
	public List<TaskStatsEntity> queryList(Map<String, Object> map){
		return taskStatsDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return taskStatsDao.queryTotal(map);
	}
	
	@Override
	public void save(TaskStatsEntity taskStats){
		taskStatsDao.save(taskStats);
	}
	
	@Override
	public void update(TaskStatsEntity taskStats){
		taskStatsDao.update(taskStats);
	}
	
	@Override
	public void delete(Integer id){
		taskStatsDao.delete(id);
	}
	
	@Override
	public void deleteBatch(Integer[] ids){
		taskStatsDao.deleteBatch(ids);
	}

	@Override
	public List<TaskActive> queryTaskTypeRecordByTime(Date a, Date b) {
		return taskStatsDao.queryTaskTypeRecordByTime(a,b);
	}

	@Override
	public List<TaskActive> sumTaskActiveFromStats(String type,Date a,Date b) {
		return taskStatsDao.sumTaskActiveFromStats(type,a,b);
	}

	@Override
	public List<TaskStatsEntity> RangeByCustom(String type, String s, String s1, String file) {
		return taskStatsDao.RangeByCustom(type,s,s1,file);
	}

	@Override
	public List<TaskStatsEntity> RangeByHour(String type, String s, String s1, int hours, int hours1) {
		return taskStatsDao.RangeByHour(type,s,s1,hours,hours1);
	}

}
