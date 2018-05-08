package io.renren.modules.admin.service.impl;

import io.renren.common.utils.DateUtils;
import io.renren.modules.admin.dao.TaskRecordDao;
import io.renren.modules.admin.entity.TaskRecordEntity;
import io.renren.modules.admin.service.TaskRecordService;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("taskRecordService")
public class TaskRecordServiceImpl implements TaskRecordService {
	@Autowired
	private TaskRecordDao taskRecordDao;

	@Override
	public TaskRecordEntity queryObject(Date id,Long userId){
		return taskRecordDao.queryObject(id,userId);
	}
	
	@Override
	public List<TaskRecordEntity> queryList(Map<String, Object> map){
		return taskRecordDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return taskRecordDao.queryTotal(map);
	}

	@Override
	public int saveBaseTask(TaskRecordEntity entity, int timeLimit) {
		Map<String,Object> map = new HashMap<>();
		map.put("id", DateUtils.format(entity.getId()));
		map.put("userId", entity.getUserId());
		map.put("type", entity.getType());
		map.put("reward", entity.getReward());
		map.put("describe", entity.getDescribe());
		map.put("timeLimit",new Date(entity.getId().getTime() - timeLimit * 1000));
		map.put("result",0);
		taskRecordDao.saveBaseTask(map);
		return MapUtils.getInteger(map, "result", 0);
	}

	@Override
	public int saveDailyTask(TaskRecordEntity entity) {
		//会额外创造一个日常任务记录
		Map<String,Object> map = new HashMap<>();
		map.put("id", DateUtils.format(entity.getId()));
		map.put("userId", entity.getUserId());
		map.put("type", entity.getType());
		map.put("reward", entity.getReward());
		map.put("describe", entity.getDescribe());
		map.put("result",0);
		taskRecordDao.saveDailyTask(map);
		return MapUtils.getInteger(map, "result", 0);
	}

	@Override
	public int saveNoviceTask(TaskRecordEntity entity) {
		//会额外创造一个新手任务记录
		Map<String,Object> map = new HashMap<>();
		map.put("id", DateUtils.format(entity.getId()));
		map.put("userId", entity.getUserId());
		map.put("type", entity.getType());
		map.put("reward", entity.getReward());
		map.put("describe", entity.getDescribe());
		map.put("result",0);
		taskRecordDao.saveNoviceTask(map);
		return MapUtils.getInteger(map, "result", 0);
	}

	@Override
	public int saveSignTask(TaskRecordEntity entity) {
		//会额外修改一个签到记录
		Map<String,Object> map = new HashMap<>();
		map.put("id", DateUtils.format(entity.getId()));
		map.put("userId", entity.getUserId());
		map.put("type", entity.getType());
		map.put("reward", entity.getReward());
		map.put("describe", entity.getDescribe());
		map.put("result",0);
		taskRecordDao.saveSignTask(map);
		return MapUtils.getInteger(map, "result", 0);
	}

	/**
	 * 翻译存储过程的错误代码
	 * @param result
	 * @return
	 */
	@Override
	public String translateErr(int result) {
		String err = "";
		switch (result){
			case -100:
				err = "距离上次领取时间过短";
				break;
			case -200:
			case -300:
				err = "插入记录不成功";
				break;
			case -400:
			case -500:
				err = "用户金币账户变动失败";
				break;
			case -600:
			case -700:
				err = "插入日常任务记录失败";
				break;
			case -800:
				err = "今日已签到";
				break;
			case -900:
			case -1000:
				err = "修改签到记录失败";
				break;
		}
		return err;
	}

	@Override
	public List<TaskRecordEntity> queryByUser(Long userId,int typeId, int page, int limit) {
		return taskRecordDao.queryByUser(userId,typeId,(page - 1) * limit,limit);
	}

	@Override
	public List<Integer> dailyFinishIdList(Long userId) {
		return taskRecordDao.dailyFinishIdList(userId);
	}

	@Override
	public List<Integer> noviceFinishIdList(Long userId) {
		return taskRecordDao.noviceFinishIdList(userId);
	}

	@Override
	public Integer  querySumReward(Long userid, int[] types, String time,String timeB) {
		return taskRecordDao.querySumReward( userid, types,  time,timeB);
	}
}
