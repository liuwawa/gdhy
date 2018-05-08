package io.renren.modules.invite.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import io.renren.modules.invite.dao.InviteRewardRuleDao;
import io.renren.modules.invite.entity.InviteRewardRuleEntity;
import io.renren.modules.invite.service.InviteRewardRuleService;


@CacheConfig(cacheNames = "IRRule")
@Service("inviteRewardRuleService")
public class InviteRewardRuleServiceImpl implements InviteRewardRuleService {
	@Autowired
	private InviteRewardRuleDao inviteRewardRuleDao;
	
	@Override
	@Cacheable(key = "'IRRule:'+#id")
	public InviteRewardRuleEntity queryObject(Integer id){
		return inviteRewardRuleDao.queryObject(id);
	}
	
	@Override
	public List<InviteRewardRuleEntity> queryList(Map<String, Object> map){
		return inviteRewardRuleDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return inviteRewardRuleDao.queryTotal(map);
	}
	
	@Override
	public void save(InviteRewardRuleEntity inviteRewardRule){
		inviteRewardRule.setCreateTime(new Date());
		inviteRewardRuleDao.save(inviteRewardRule);
	}

	@Override
	public InviteRewardRuleEntity getCurrentRule() {
		return inviteRewardRuleDao.getCurrentRule();
	}

}
