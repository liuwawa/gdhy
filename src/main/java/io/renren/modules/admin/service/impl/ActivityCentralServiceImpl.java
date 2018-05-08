package io.renren.modules.admin.service.impl;

import io.renren.modules.admin.dao.ActivityCentralDao;
import io.renren.modules.admin.entity.ActivityCentralEntity;
import io.renren.modules.admin.service.ActivityCentralService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;



@Service("activityCentralService")
public class ActivityCentralServiceImpl implements ActivityCentralService {
	@Autowired
	private ActivityCentralDao activityCentralDao;
	
	@Override
	public ActivityCentralEntity queryObject(Long id){
		return activityCentralDao.queryObject(id);
	}
	
	@Override
	public List<ActivityCentralEntity> queryList(Map<String, Object> map){
		return activityCentralDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return activityCentralDao.queryTotal(map);
	}
	
	@Override
	public void save(ActivityCentralEntity activityCentral){
		activityCentralDao.save(activityCentral);
	}
	
	@Override
	public void update(ActivityCentralEntity activityCentral){
		activityCentralDao.update(activityCentral);
	}
	
	@Override
	public void delete(Long id){
		activityCentralDao.delete(id);
	}
	
	@Override
	public void deleteBatch(Long[] ids){
		activityCentralDao.deleteBatch(ids);
	}
	
}
