package io.renren.modules.admin.service.impl;

import io.renren.modules.admin.dao.GoldExchangeDao;
import io.renren.modules.admin.entity.GoldExchangeEntity;
import io.renren.modules.admin.service.GoldExchangeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;



@Service("goldExchangeService")
public class GoldExchangeServiceImpl implements GoldExchangeService {
	@Autowired
	private GoldExchangeDao goldExchangeDao;
	
	@Override
	public GoldExchangeEntity queryObject(Integer rmb){
		return goldExchangeDao.queryObject(rmb);
	}
	
	@Override
	public List<GoldExchangeEntity> queryList(Map<String, Object> map){
		return goldExchangeDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return goldExchangeDao.queryTotal(map);
	}
	
	@Override
	public void save(GoldExchangeEntity goldExchange){
		goldExchange.setCreateTime(new Date());
		goldExchange.setDelFlag(0);
		goldExchangeDao.save(goldExchange);
	}
	
	@Override
	public void update(GoldExchangeEntity goldExchange){
		goldExchange.setModifyTime(new Date());
		goldExchangeDao.update(goldExchange);
	}
	
	@Override
	public void delete(Integer rmb){
		goldExchangeDao.delete(rmb);
	}
	
	@Override
	public void deleteBatch(Integer[] rmbs){
		goldExchangeDao.deleteBatch(rmbs);
	}
	
}
