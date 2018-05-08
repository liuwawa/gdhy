package io.renren.modules.stats.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import io.renren.modules.stats.dao.FinanceStatsDao;
import io.renren.modules.stats.entity.FinanceStatsEntity;
import io.renren.modules.stats.service.FinanceStatsService;



@Service("financeStatsService")
public class FinanceStatsServiceImpl implements FinanceStatsService {
	@Autowired
	private FinanceStatsDao financeStatsDao;

	@Override
	public FinanceStatsEntity queryObject(Integer id){
		return financeStatsDao.queryObject(id);
	}
	
	@Override
	public List<FinanceStatsEntity> queryList(Map<String, Object> map){
		return financeStatsDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return financeStatsDao.queryTotal(map);
	}
	
	@Override
	public void save(FinanceStatsEntity financeStats){
		financeStatsDao.save(financeStats);
	}
	
	@Override
	public void update(FinanceStatsEntity financeStats){
		financeStatsDao.update(financeStats);
	}
	
	@Override
	public void delete(Integer id){
		financeStatsDao.delete(id);
	}
	
	@Override
	public void deleteBatch(Integer[] ids){
		financeStatsDao.deleteBatch(ids);
	}

	@Override
	public Integer sumRmbByTime(Date a, Date b) {
		return financeStatsDao.sumRmbByTime(a,b);
	}

	@Override
	public Integer sumRmbFromStats(Date a, Date b, String type) {
		return financeStatsDao.sumRmbFromStats(a,b,type);
	}

	@Override
	public List<FinanceStatsEntity> RangeByCustom(String type, String s, String s1, String file) {
		return financeStatsDao.RangeByCustom(type,s,s1,file);
	}

	@Override
	public List<FinanceStatsEntity> RangeByCustom(String type, Date s, Date s1, String file) {
		return financeStatsDao.RangeByCustom(type,s,s1,file);
	}

	@Override
	public List<FinanceStatsEntity> RangeByHour(String type, String s, String s1, int hours, int hours1) {
		return financeStatsDao.RangeByHour(type,s,s1,hours,hours1);
	}

}
