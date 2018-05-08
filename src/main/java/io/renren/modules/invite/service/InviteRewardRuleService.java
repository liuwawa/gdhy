package io.renren.modules.invite.service;

import io.renren.modules.invite.entity.InviteRewardRuleEntity;

import java.util.List;
import java.util.Map;

/**
 * 奖励规则
 * 
 * @author itmx
 * @email itmx@foxmail.com
 * @date 2018-03-19 14:27:31
 */
public interface InviteRewardRuleService {
	
	InviteRewardRuleEntity queryObject(Integer id);
	
	List<InviteRewardRuleEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(InviteRewardRuleEntity inviteRewardRule);

    InviteRewardRuleEntity getCurrentRule();
}
