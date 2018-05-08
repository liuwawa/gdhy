package io.renren.modules.admin.service.impl;

import io.renren.datasources.DataSourceNames;
import io.renren.datasources.annotation.DataSource;
import io.renren.modules.admin.dao.AppLogDao;
import io.renren.modules.admin.entity.AppLogEntity;
import io.renren.modules.admin.service.AppLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service("appLogService")
public class AppLogServiceImpl implements AppLogService {
	@Autowired
	private AppLogDao appLogDao;

	@Override
	@DataSource(name = DataSourceNames.SECOND)
	public AppLogEntity queryObject(String id){
		return appLogDao.queryObject(id);
	}

	@Override
	@DataSource(name = DataSourceNames.SECOND)
	public List<AppLogEntity> queryList(Map<String, Object> map){
		return appLogDao.queryList(map);
	}

	@Override
	@DataSource(name = DataSourceNames.SECOND)
	public int queryTotal(Map<String, Object> map){
		return appLogDao.queryTotal(map);
	}

	@Override
	@DataSource(name = DataSourceNames.SECOND)
	public void save(AppLogEntity userLog){
		userLog.setId(UUID.randomUUID().toString().replace("-",""));
		userLog.setCreateTime(new Date());
		appLogDao.save(userLog);
	}

	@Override
	@DataSource(name = DataSourceNames.SECOND)
	public void update(AppLogEntity userLog){
		appLogDao.update(userLog);
	}

	@Override
	@DataSource(name = DataSourceNames.SECOND)
	public void delete(String id){
		appLogDao.delete(id);
	}

	@Override
	@DataSource(name = DataSourceNames.SECOND)
	public void deleteBatch(String[] ids){
		appLogDao.deleteBatch(ids);
	}

}
