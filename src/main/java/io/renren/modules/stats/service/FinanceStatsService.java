package io.renren.modules.stats.service;

import io.renren.modules.stats.entity.FinanceStatsEntity;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 财务支出情况
 * 
 * @author itmx
 * @email itmx@foxmail.com
 * @date 2018-02-05 11:20:29
 */
public interface FinanceStatsService {
	
	FinanceStatsEntity queryObject(Integer id);
	
	List<FinanceStatsEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(FinanceStatsEntity financeStats);
	
	void update(FinanceStatsEntity financeStats);
	
	void delete(Integer id);
	
	void deleteBatch(Integer[] ids);

    Integer sumRmbByTime(Date a, Date b);

	Integer sumRmbFromStats(Date a, Date b, String type);

    List<FinanceStatsEntity> RangeByCustom(String type, String s, String s1, String file);

	List<FinanceStatsEntity> RangeByCustom(String type, Date s, Date s1, String file);

	List<FinanceStatsEntity> RangeByHour(String type, String s, String s1, int hours, int hours1);

}
