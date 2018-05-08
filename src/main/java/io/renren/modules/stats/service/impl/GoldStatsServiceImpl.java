package io.renren.modules.stats.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import io.renren.modules.stats.dao.GoldStatsDao;
import io.renren.modules.stats.entity.GoldStatsEntity;
import io.renren.modules.stats.service.GoldStatsService;

@Service("goldStatsService")
public class GoldStatsServiceImpl implements GoldStatsService {
	@Autowired
	private GoldStatsDao goldStatsDao;
	
	@Override
	public GoldStatsEntity queryObject(Integer id){
		return goldStatsDao.queryObject(id);
	}
	
	@Override
	public List<GoldStatsEntity> queryList(Map<String, Object> map){
		return goldStatsDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return goldStatsDao.queryTotal(map);
	}
	
	@Override
	public void save(GoldStatsEntity goldStats){
		goldStatsDao.save(goldStats);
	}
	
	@Override
	public void update(GoldStatsEntity goldStats){
		goldStatsDao.update(goldStats);
	}
	
	@Override
	public void delete(Integer id){
		goldStatsDao.delete(id);
	}
	
	@Override
	public void deleteBatch(Integer[] ids){
		goldStatsDao.deleteBatch(ids);
	}

	@Override
	public Integer sumGoldByTime(Date a, Date b) {
		return goldStatsDao.sumGoldByTime(a,b);
	}

	@Override
	public GoldStatsEntity preHour() {
		return goldStatsDao.preHour();
	}

	@Override
	public Integer sumGoldFromStats(Date a, Date b, String type) {
		return goldStatsDao.sumGoldFromStats(a,b,type);
	}

	@Override
	public List<GoldStatsEntity> queryByRange(String type, Date a, Date b) {
		return goldStatsDao.queryByRange(type,a,b);
	}

	@Override
	public List<GoldStatsEntity> RangeByHour(String type, String s, String s1, int hours, int hours1) {
		return goldStatsDao.RangeByHour(type,s,s1,hours,hours1);
	}

	@Override
	public List<GoldStatsEntity> RangeByCustom(String type, String s, String s1, String file) {
		return goldStatsDao.RangeByCustom(type,s,s1,file);
	}

}
