package io.renren.modules.admin.service.impl;

import io.renren.common.utils.DateUtils;
import io.renren.modules.admin.dao.TaskListDao;
import io.renren.modules.admin.entity.GoldEntity;
import io.renren.modules.admin.entity.TaskListEntity;
import io.renren.modules.admin.entity.TaskRecordEntity;
import io.renren.modules.admin.service.GoldService;
import io.renren.modules.admin.service.TaskListService;
import io.renren.modules.admin.service.TaskRecordService;
import io.renren.modules.api.entity.UserEntity;
import io.renren.modules.invite.entity.InviteRecordEntity;
import io.renren.modules.invite.service.InviteRecordService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.*;

@CacheConfig(cacheNames = "taskList")
@Service("taskListService")
public class TaskListServiceImpl implements TaskListService {
	@Autowired
	private TaskListDao taskListDao;

	@Autowired
	private TaskRecordService taskRecordService;

	@Autowired
	private GoldService goldService;

	@Autowired
	private InviteRecordService inviteRecordService;

	@Override
	@Cacheable(key = "'task:'+#id")
	public TaskListEntity queryObject(Integer id){
		return taskListDao.queryObject(id);
	}
	
	@Override
	public List<TaskListEntity> queryList(Map<String, Object> map){
		return taskListDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return taskListDao.queryTotal(map);
	}
	
	@Override
	@CacheEvict(allEntries = true)
	public void save(TaskListEntity taskList){
		taskList.setCreateTime(new Date());
		taskList.setModifyTime(new Date());
		taskList.setDelFlag(0);
		taskListDao.save(taskList);
	}

	@Override
	@CacheEvict(allEntries = true)
	public void update(TaskListEntity taskList){
		taskList.setModifyTime(new Date());
		taskListDao.update(taskList);
	}

	@Override
	@CacheEvict(allEntries = true)
	public void delete(Integer id){
		taskListDao.delete(id);
	}

	@Override
	@CacheEvict(allEntries = true)
	public void deleteBatch(Integer[] ids){
		taskListDao.deleteBatch(ids);
	}

	@Override
	@Cacheable(key = "'queryDailyList'")
	public List<TaskListEntity> queryDailyList() {
		return taskListDao.queryDailyList();
	}

	@Override
	@Cacheable(key = "'queryNoviceList'")
	public List<TaskListEntity> queryNoviceList() {
		return taskListDao.queryNoviceList();
	}

	@Override
	public boolean isReceive(TaskListEntity taskEntity, UserEntity user) {
		String event = taskEntity.getEvent();
		if("bindWeChat".equals(event)){
			return !StringUtils.isBlank(user.getWeixinBindOpenid());
		}else if("bindPhone".equals(event)){
			return !StringUtils.isBlank(user.getPhone());
		}else if("bindAlipay".equals(event)){
			return !StringUtils.isBlank(user.getAlipayBindOpenid());
		}else if("fullInfo".equals(event)){
			return !(//StringUtils.isBlank(user.getWeixinBindOpenid()) ||
//					StringUtils.isBlank(user.getAlipayBindOpenid()) ||
					StringUtils.isBlank(user.getPhone()) ||
					StringUtils.isBlank(user.getBirthday()) ||
					StringUtils.isBlank(user.getAvatar()) ||
					StringUtils.isBlank(user.getUsername()));
		}else if("readNews".equals(event)){
			//从阅读任务记录中统计出时间
			Integer duration = taskEntity.getDuration();
			//时间小于等于0的任务可立即领取
			if(duration <= 0){
				return true;
			}
			int count = (int) Math.ceil(duration / 30.0);
			List<TaskRecordEntity> taskRecords = taskRecordService.queryByUser(user.getUserId(),33, 1, count);
			Date todayZero = DateUtils.getToday();
			if("daily".equals(taskEntity.getType())){
				//条数住够，List中最后（最早）一条的时间是今天
				return taskRecords.size() >= count && taskRecords.get(taskRecords.size() - 1).getId().after(todayZero);
			}
			return taskRecords.size() >= count;
		}else if("watchVideo".equals(event)){
			//从看视频任务记录中统计出时间
			Integer duration = taskEntity.getDuration();
			//时间小于等于0的任务可立即领取
			if(duration <= 0){
				return true;
			}
			int count = (int) Math.ceil(duration / 30.0);
			List<TaskRecordEntity> taskRecords = taskRecordService.queryByUser(user.getUserId(),34, 1, count);
			Date todayZero = DateUtils.getToday();
			if("daily".equals(taskEntity.getType())){
				//条数住够，List中最后（最早）一条的时间是今天
				return taskRecords.size() >= count && taskRecords.get(taskRecords.size() - 1).getId().after(todayZero);
			}
			return taskRecords.size() >= count;
		}else if("withdrawals".equals(event)){
			GoldEntity goldEntity = goldService.queryObject(user.getUserId());
			return !StringUtils.isBlank(goldEntity.getLastExchange());
		}else if("invitation".equals(event)){
			//获取最后一条收徒记录
			List<InviteRecordEntity> records = inviteRecordService.pageByMaster(user.getUserId(), 1, 1,null);
			if(records.size() == 0){
				return false;
			}
			//日常任务
			if("daily".equals(taskEntity.getType())){
				return records.get(0).getCreateTime().after(DateUtils.getToday());
			}
			return true;
		}
		return false;
	}

}
