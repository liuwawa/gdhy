package io.renren.modules.invite.service;

import io.renren.modules.admin.entity.TaskRecordEntity;
import io.renren.modules.api.entity.UserEntity;
import io.renren.modules.invite.entity.InviteRecordEntity;

import java.util.List;
import java.util.Map;

/**
 * 收徒记录
 * 
 * @author itmx
 * @email itmx@foxmail.com
 * @date 2018-03-19 21:46:21
 */
public interface InviteRecordService {
	
	InviteRecordEntity queryObject(Long master,Long apprentice);
	
	List<InviteRecordEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(InviteRecordEntity inviteRecord);
	
	void update(InviteRecordEntity inviteRecord);
	
	void delete(Long master);
	
	void deleteBatch(Long[] apprentice);

	List<InviteRecordEntity> pageByMaster(Long master, Integer page, Integer limit,String time);

    Integer totalProfit(Long userId);

	InviteRecordEntity queryByApprentice(Long apprentice);

	void additional(UserEntity user, TaskRecordEntity taskRecordEntity);

	int SummaryInvitationsNum(String startTime, String endTime);

    void firstReward(UserEntity user);
}
