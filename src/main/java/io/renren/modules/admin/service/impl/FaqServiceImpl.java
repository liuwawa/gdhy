package io.renren.modules.admin.service.impl;

import io.renren.modules.admin.dao.FaqDao;
import io.renren.modules.admin.entity.FaqEntity;
import io.renren.modules.admin.service.FaqService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;



@Service("faqService")
public class FaqServiceImpl implements FaqService {
	@Autowired
	private FaqDao faqDao;
	
	@Override
	public FaqEntity queryObject(Integer id){
		return faqDao.queryObject(id);
	}
	
	@Override
	public List<FaqEntity> queryList(Map<String, Object> map){
		return faqDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return faqDao.queryTotal(map);
	}
	
	@Override
	public void save(FaqEntity faq){
		faq.setCreateTime(new Date());
		faq.setDelFlag(0);
		faqDao.save(faq);
	}
	
	@Override
	public void update(FaqEntity faq){
		faqDao.update(faq);
	}
	
	@Override
	public void delete(Integer id){
		faqDao.delete(id);
	}
	
	@Override
	public void deleteBatch(Integer[] ids){
		faqDao.deleteBatch(ids);
	}

	@Override
	public List<FaqEntity> queryByType(Integer type, Integer page, Integer limit) {
		return faqDao.queryByType(type,(page - 1) * limit,limit);
	}

}
