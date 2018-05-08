package io.renren.modules.stats.service;

import io.renren.modules.stats.entity.GoldStatsEntity;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 金币领取情况
 * 
 * @author itmx
 * @email itmx@foxmail.com
 * @date 2018-02-05 11:20:29
 */
public interface GoldStatsService {
	
	GoldStatsEntity queryObject(Integer id);
	
	List<GoldStatsEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(GoldStatsEntity goldStats);
	
	void update(GoldStatsEntity goldStats);
	
	void delete(Integer id);
	
	void deleteBatch(Integer[] ids);

    Integer sumGoldByTime(Date a, Date b);

	GoldStatsEntity preHour();

    Integer sumGoldFromStats(Date a, Date b, String type);

    List<GoldStatsEntity> queryByRange(String type, Date a, Date b);

	List<GoldStatsEntity> RangeByHour(String type, String s, String s1, int hours, int hours1);

	List<GoldStatsEntity> RangeByCustom(String type, String s, String s1, String file);
}
