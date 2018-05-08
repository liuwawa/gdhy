package io.renren.modules.admin.service.impl;

import io.renren.modules.admin.dao.FaqTypeDao;
import io.renren.modules.admin.entity.FaqTypeEntity;
import io.renren.modules.admin.service.FaqTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

@CacheConfig(cacheNames = "faqType")
@Service("faqTypeService")
public class FaqTypeServiceImpl implements FaqTypeService {
	@Autowired
	private FaqTypeDao faqTypeDao;
	
	@Override
	public FaqTypeEntity queryObject(Integer id){
		return faqTypeDao.queryObject(id);
	}
	
	@Override
	@Cacheable(key = "'queryList_'+#map")
	public List<FaqTypeEntity> queryList(Map<String, Object> map){
		return faqTypeDao.queryList(map);
	}

	@Override
	@Cacheable(key = "'queryTotal_'+#map")
	public int queryTotal(Map<String, Object> map){
		return faqTypeDao.queryTotal(map);
	}
	
	@Override
	@CacheEvict(allEntries = true)
	public void save(FaqTypeEntity faqType){
		faqType.setCreateTime(new Date());
		faqType.setModifyTime(new Date());
		faqType.setDelFlag(0);
		faqTypeDao.save(faqType);
	}

	@Override
	@CacheEvict(allEntries = true)
	public void update(FaqTypeEntity faqType){
		faqType.setModifyTime(new Date());
		faqTypeDao.update(faqType);
	}

	@Override
	@CacheEvict(allEntries = true)
	public void delete(Integer id){
		faqTypeDao.delete(id);
	}

	@Override
	@CacheEvict(allEntries = true)
	public void deleteBatch(Integer[] ids){
		faqTypeDao.deleteBatch(ids);
	}

}
