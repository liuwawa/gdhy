package io.renren.modules.admin.service.impl;

import io.renren.modules.admin.dao.DataDictionariesDao;
import io.renren.modules.admin.entity.DataDictionariesEntity;
import io.renren.modules.admin.service.DataDictionariesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@CacheConfig(cacheNames = "dict")
@Service("dataDictionariesService")
public class DataDictionariesServiceImpl implements DataDictionariesService {
	@Autowired
	private DataDictionariesDao dataDictionariesDao;
	
	@Override
	public DataDictionariesEntity queryObject(Long id){
		return dataDictionariesDao.queryObject(id);
	}
	
	@Override
	public List<DataDictionariesEntity> queryList(Map<String, Object> map){
		return dataDictionariesDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return dataDictionariesDao.queryTotal(map);
	}
	
	@Override
	@CacheEvict(allEntries = true)
	public void save(DataDictionariesEntity dataDictionaries){
		dataDictionariesDao.save(dataDictionaries);
	}

	@Override
	@CacheEvict(allEntries = true)
	public void update(DataDictionariesEntity dataDictionaries){
		dataDictionariesDao.update(dataDictionaries);
	}

	@Override
	@CacheEvict(allEntries = true)
	public void delete(Long id){
		dataDictionariesDao.delete(id);
	}

	@Override
	@CacheEvict(allEntries = true)
	public void deleteBatch(Long[] ids){
		dataDictionariesDao.deleteBatch(ids);
	}

	@Override
	@Cacheable(key = "'dict:'+#key+':'+#version")
	public String queryByKey(String key, String version) {
		return dataDictionariesDao.queryByKey(key,version);
	}

}
