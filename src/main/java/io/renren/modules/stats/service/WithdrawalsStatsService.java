package io.renren.modules.stats.service;

import io.renren.modules.stats.entity.WithdrawalsStatsEntity;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 提现统计
 * 
 * @author itmx
 * @email itmx@foxmail.com
 * @date 2018-02-05 11:20:29
 */
public interface WithdrawalsStatsService {
	
	WithdrawalsStatsEntity queryObject(Integer id);
	
	List<WithdrawalsStatsEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(WithdrawalsStatsEntity withdrawalsStats);
	
	void update(WithdrawalsStatsEntity withdrawalsStats);
	
	void delete(Integer id);
	
	void deleteBatch(Integer[] ids);

    Long sumGoldByTime(Date a, Date b);

	Long sumGoldFromStats(Date a, Date b, String type);

    List<WithdrawalsStatsEntity> RangeByHour(String type, String s, String s1, int hours, int hours1);

	List<WithdrawalsStatsEntity> RangeByCustom(String type, String s, String s1, String file);

    Long sumOrderStatsByStatus(Date date, Date date1, int o);
}
