package io.renren.modules.stats.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import io.renren.modules.stats.dao.WithdrawalsStatsDao;
import io.renren.modules.stats.entity.WithdrawalsStatsEntity;
import io.renren.modules.stats.service.WithdrawalsStatsService;



@Service("withdrawalsStatsService")
public class WithdrawalsStatsServiceImpl implements WithdrawalsStatsService {
	@Autowired
	private WithdrawalsStatsDao withdrawalsStatsDao;
	
	@Override
	public WithdrawalsStatsEntity queryObject(Integer id){
		return withdrawalsStatsDao.queryObject(id);
	}
	
	@Override
	public List<WithdrawalsStatsEntity> queryList(Map<String, Object> map){
		return withdrawalsStatsDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return withdrawalsStatsDao.queryTotal(map);
	}
	
	@Override
	public void save(WithdrawalsStatsEntity withdrawalsStats){
		withdrawalsStatsDao.save(withdrawalsStats);
	}
	
	@Override
	public void update(WithdrawalsStatsEntity withdrawalsStats){
		withdrawalsStatsDao.update(withdrawalsStats);
	}
	
	@Override
	public void delete(Integer id){
		withdrawalsStatsDao.delete(id);
	}
	
	@Override
	public void deleteBatch(Integer[] ids){
		withdrawalsStatsDao.deleteBatch(ids);
	}

	@Override
	public Long sumGoldByTime(Date a, Date b) {
		return withdrawalsStatsDao.sumGoldByTime(a,b);
	}

	@Override
	public Long sumGoldFromStats(Date a, Date b, String type) {
		return withdrawalsStatsDao.sumGoldFromStats(a,b,type);
	}

	@Override
	public List<WithdrawalsStatsEntity> RangeByHour(String type, String s, String s1, int hours, int hours1) {
		return withdrawalsStatsDao.RangeByHour(type,s,s1,hours,hours1);
	}

	@Override
	public List<WithdrawalsStatsEntity> RangeByCustom(String type, String s, String s1, String file) {
		return withdrawalsStatsDao.RangeByCustom(type,s,s1,file);
	}

	@Override
	public Long sumOrderStatsByStatus(Date date, Date date1, int o) {
		return withdrawalsStatsDao.sumOrderStatsByStatus(date,date1,o);
	}

}
