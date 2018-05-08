package io.renren.modules.invite.service;

import io.renren.modules.invite.entity.InviteCodeEntity;

import java.util.List;
import java.util.Map;

/**
 * 邀请码
 * 
 * @author itmx
 * @email itmx@foxmail.com
 * @date 2018-03-18 16:28:29
 */
public interface InviteCodeService {
	
	InviteCodeEntity queryObject(Integer inviteCode);
	
	List<InviteCodeEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(InviteCodeEntity inviteCode);
	
	void update(InviteCodeEntity inviteCode);
	
	void delete(Integer inviteCode);

    void disableBatch(Integer[] inviteCodes);

	void abandonBatch(Integer[] inviteCodes);

    void enableBatch(Integer[] inviteCodes);

    InviteCodeEntity getInviteCode(Long userId);
}
