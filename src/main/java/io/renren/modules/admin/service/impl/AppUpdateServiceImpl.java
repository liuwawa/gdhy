package io.renren.modules.admin.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import io.renren.modules.admin.dao.AppUpdateDao;
import io.renren.modules.admin.entity.AppUpdateEntity;
import io.renren.modules.admin.service.AppUpdateService;



@Service("appUpdateService")
public class AppUpdateServiceImpl implements AppUpdateService {
	@Autowired
	private AppUpdateDao appUpdateDao;
	
	@Override
	public AppUpdateEntity queryObject(Integer id){
		return appUpdateDao.queryObject(id);
	}
	
	@Override
	public List<AppUpdateEntity> queryList(Map<String, Object> map){
		return appUpdateDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return appUpdateDao.queryTotal(map);
	}
	
	@Override
	public void save(AppUpdateEntity appUpdate){
		appUpdate.setCreateTime(new Date());
		appUpdate.setDelFlag(0);
		appUpdateDao.save(appUpdate);
	}
	
	@Override
	public void update(AppUpdateEntity appUpdate){
		appUpdate.setModifyTime(new Date());
		appUpdateDao.update(appUpdate);
	}
	
	@Override
	public void delete(Integer id){
		appUpdateDao.delete(id);
	}
	
	@Override
	public void deleteBatch(Integer[] ids){
		appUpdateDao.deleteBatch(ids);
	}

	@Override
	public AppUpdateEntity getLastVersion(String appId) {
		return appUpdateDao.getLastVersion(appId);
	}

}
