package io.renren.modules.invite.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import io.renren.modules.invite.dao.InviteCodeDao;
import io.renren.modules.invite.entity.InviteCodeEntity;
import io.renren.modules.invite.service.InviteCodeService;

@Service("inviteCodeService")
public class InviteCodeServiceImpl implements InviteCodeService {
	@Autowired
	private InviteCodeDao inviteCodeDao;
	
	@Override
	public InviteCodeEntity queryObject(Integer inviteCode){
		return inviteCodeDao.queryObject(inviteCode);
	}
	
	@Override
	public List<InviteCodeEntity> queryList(Map<String, Object> map){
		return inviteCodeDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return inviteCodeDao.queryTotal(map);
	}
	
	@Override
	public void save(InviteCodeEntity inviteCode){
		inviteCodeDao.save(inviteCode);
	}
	
	@Override
	public void update(InviteCodeEntity inviteCode){
		inviteCodeDao.update(inviteCode);
	}
	
	@Override
	public void delete(Integer inviteCode){
		inviteCodeDao.delete(inviteCode);
	}

	@Override
	public void abandonBatch(Integer[] inviteCodes){
		inviteCodeDao.abandonBatch(inviteCodes);
	}

	@Override
	public void enableBatch(Integer[] inviteCodes) {
		inviteCodeDao.enableBatch(inviteCodes);
	}

	@Override
	public InviteCodeEntity getInviteCode(Long userId) {
		InviteCodeEntity inviteCodeEntity = inviteCodeDao.queryByUser(userId);
		if(inviteCodeEntity == null){
			//生成新邀请码
			inviteCodeEntity = new InviteCodeEntity();
			inviteCodeEntity.setUserId(userId);
			inviteCodeEntity.setState(0);
			inviteCodeEntity.setCreateTime(new Date());
			save(inviteCodeEntity);
		}
		return inviteCodeEntity;
	}

	@Override
	public void disableBatch(Integer[] inviteCodes){
		inviteCodeDao.disableBatch(inviteCodes);
	}

}
