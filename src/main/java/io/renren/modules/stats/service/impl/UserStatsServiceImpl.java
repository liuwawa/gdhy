package io.renren.modules.stats.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import io.renren.modules.stats.dao.UserStatsDao;
import io.renren.modules.stats.entity.UserStatsEntity;
import io.renren.modules.stats.service.UserStatsService;



@Service("userStatsService")
public class UserStatsServiceImpl implements UserStatsService {
	@Autowired
	private UserStatsDao userStatsDao;
	
	@Override
	public UserStatsEntity queryObject(Integer id){
		return userStatsDao.queryObject(id);
	}
	
	@Override
	public List<UserStatsEntity> queryList(Map<String, Object> map){
		return userStatsDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return userStatsDao.queryTotal(map);
	}
	
	@Override
	public void save(UserStatsEntity userStats){
		userStatsDao.save(userStats);
	}
	
	@Override
	public void update(UserStatsEntity userStats){
		userStatsDao.update(userStats);
	}
	
	@Override
	public void delete(Integer id){
		userStatsDao.delete(id);
	}
	
	@Override
	public void deleteBatch(Integer[] ids){
		userStatsDao.deleteBatch(ids);
	}

	@Override
	public UserStatsEntity preHour() {
		return userStatsDao.preHour();
	}

	@Override
	public int countAll() {
		return userStatsDao.countAll();
	}

	@Override
	public Integer countUserByTime(Date a, Date b) {
		return userStatsDao.countUserByTime(a,b);
	}

	@Override
	public List<UserStatsEntity> queryByRange(String type, Date a, Date b) {
		return userStatsDao.queryByRange(type,a,b);
	}

	@Override
	public List<UserStatsEntity> RangeByHour(String type, String s, String c, int hours, int hours1) {
		return userStatsDao.RangeByHour(type,s,c,hours,hours1);
	}

	@Override
	public List<UserStatsEntity> RangeByCustom(String type, String s, String s1, String field) {
		return userStatsDao.RangeByCustom(type,s,s1,field);
	}

	@Override
	public List<UserStatsEntity> RangeByCustom(String type, Date s, Date s1, String field) {
		return userStatsDao.RangeByCustom(type,s,s1,field);
	}
}
