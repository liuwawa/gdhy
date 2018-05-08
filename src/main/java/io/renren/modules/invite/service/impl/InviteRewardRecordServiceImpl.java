package io.renren.modules.invite.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import io.renren.modules.invite.dao.InviteRewardRecordDao;
import io.renren.modules.invite.entity.InviteRewardRecordEntity;
import io.renren.modules.invite.service.InviteRewardRecordService;



@Service("inviteRewardRecordService")
public class InviteRewardRecordServiceImpl implements InviteRewardRecordService {
	@Autowired
	private InviteRewardRecordDao inviteRewardRecordDao;
	
	@Override
	public InviteRewardRecordEntity queryObject(Long master,Long apprentice){
		return inviteRewardRecordDao.queryOne(master,apprentice);
	}
	
	@Override
	public List<InviteRewardRecordEntity> queryList(Map<String, Object> map){
		return inviteRewardRecordDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return inviteRewardRecordDao.queryTotal(map);
	}
	
	@Override
	public void save(InviteRewardRecordEntity inviteRewardRecord){
		inviteRewardRecord.setCreateTime(new Date());
		inviteRewardRecordDao.save(inviteRewardRecord);
	}

	@Override
	public List<InviteRewardRecordEntity> pageByMaster(Long master, Integer page, Integer limit) {
			return inviteRewardRecordDao.pageByMaster(master,(page - 1) * limit,limit);
	}

}
