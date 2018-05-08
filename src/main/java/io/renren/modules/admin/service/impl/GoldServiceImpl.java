package io.renren.modules.admin.service.impl;

import io.renren.common.utils.DateUtils;
import io.renren.modules.admin.dao.GoldDao;
import io.renren.modules.admin.entity.GoldEntity;
import io.renren.modules.admin.service.GoldService;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("goldService")
public class GoldServiceImpl implements GoldService {
	@Autowired
	private GoldDao goldDao;

	@Override
	public GoldEntity queryObject(Long id){
		return goldDao.queryObject(id);
	}
	
	@Override
	public List<GoldEntity> queryList(Map<String, Object> map){
		return goldDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return goldDao.queryTotal(map);
	}

	@Override
	public void createNewEntity(Long userId) {
		GoldEntity goldEntity = new GoldEntity();
		goldEntity.setId(userId);
		goldEntity.setToday(0);
		goldEntity.setSurplus(0);
		goldEntity.setTotal(0);
		goldEntity.setModifyTime(new Date());
		goldEntity.setTodayRead(0);
		goldEntity.setTodayVideo(0);
		goldEntity.setYesterdayRead(0);
		goldEntity.setYesterdayVideo(0);
		goldDao.save(goldEntity);
	}

	@Override
	public int resetToday() {
		return goldDao.resetToday();
	}

	@Override
	public int modifySurplusGold(Long userId, Integer increase, String tip) {

		Map<String,Object> map = new HashMap<>();
		map.put("id", DateUtils.format(new Date()));
		map.put("userId", userId);
		map.put("type", 0);
		map.put("reward", increase);
		map.put("describe", tip);
		map.put("result",0);
		goldDao.modifySurplusGold(map);
		return MapUtils.getInteger(map, "result", 0);
	}
	/**
	 *  向金币表中的今日阅读添加金币
	 * @param userId
	 * @param reward
	 */
	@Override
	public void updateTodayReadByUserId(Long userId, Integer reward) {
		goldDao.updateTodayReadByUserId(userId,reward);
	}
	/**
	 *  向金币表中的今日视频添加金币
	 * @param userId
	 * @param reward
	 */
	@Override
	public void updateTodayVedioByUserId(Long userId, Integer reward) {
		goldDao.updateTodayVedioByUserId(userId,reward);
	}
}
