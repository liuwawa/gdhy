package io.renren.modules.invite.service;

import io.renren.modules.invite.entity.InviteRewardRecordEntity;

import java.util.List;
import java.util.Map;

/**
 * 发放记录
 * 
 * @author itmx
 * @email itmx@foxmail.com
 * @date 2018-03-20 10:14:28
 */
public interface InviteRewardRecordService {
	
	InviteRewardRecordEntity queryObject(Long master,Long apprentice);
	
	List<InviteRewardRecordEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(InviteRewardRecordEntity inviteRewardRecord);

	List<InviteRewardRecordEntity> pageByMaster(Long master, Integer page, Integer limit);
}
