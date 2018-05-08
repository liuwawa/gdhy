package io.renren.modules.admin.service.impl;

import io.renren.modules.admin.dao.EverydaySignDao;
import io.renren.modules.admin.entity.EverydaySignEntity;
import io.renren.modules.admin.service.EverydaySignService;
import io.renren.modules.api.entity.SignRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;



@Service("everydaySignService")
public class EverydaySignServiceImpl implements EverydaySignService {
	@Autowired
	private EverydaySignDao everydaySignDao;
	
	@Override
	public EverydaySignEntity queryObject(Integer days){
		return everydaySignDao.queryObject(days);
	}
	
	@Override
	public List<EverydaySignEntity> queryList(Map<String, Object> map){
		return everydaySignDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return everydaySignDao.queryTotal(map);
	}
	
	@Override
	public void save(EverydaySignEntity everydaySign){
		everydaySignDao.save(everydaySign);
	}
	
	@Override
	public void update(EverydaySignEntity everydaySign){
		everydaySign.setModifyTime(new Date());
		everydaySignDao.update(everydaySign);
	}
	
	@Override
	public void delete(Integer days){
		everydaySignDao.delete(days);
	}
	
	@Override
	public void deleteBatch(Integer[] dayss){
		everydaySignDao.deleteBatch(dayss);
	}

	@Override
	public void createNewEntity(Long userId) {
		everydaySignDao.createNewEntity(userId);
	}

	@Override
	public SignRecord querySignRecord(Long userId) {
		return everydaySignDao.querySignRecord(userId);
	}

}
