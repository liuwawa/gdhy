package io.renren.modules.invite.service.impl;

import io.renren.common.utils.DateUtils;
import io.renren.common.utils.RedisCacheUtils;
import io.renren.modules.admin.entity.GoldEntity;
import io.renren.modules.admin.entity.TaskListEntity;
import io.renren.modules.admin.entity.TaskRecordEntity;
import io.renren.modules.admin.service.GoldService;
import io.renren.modules.admin.service.TaskListService;
import io.renren.modules.admin.service.TaskRecordService;
import io.renren.modules.api.entity.UserEntity;
import io.renren.modules.api.utils.AppLogUtils;
import io.renren.modules.invite.dao.InviteRecordDao;
import io.renren.modules.invite.entity.InviteRecordEntity;
import io.renren.modules.invite.entity.InviteRewardRecordEntity;
import io.renren.modules.invite.entity.InviteRewardRuleEntity;
import io.renren.modules.invite.service.InviteRecordService;
import io.renren.modules.invite.service.InviteRewardRecordService;
import io.renren.modules.invite.service.InviteRewardRuleService;
import io.renren.modules.invite.utils.InviteRewardRuleUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service("inviteRecordService")
public class InviteRecordServiceImpl implements InviteRecordService {
	@Autowired
	private InviteRecordDao inviteRecordDao;
	@Autowired
	private InviteRecordService inviteRecordService;
	@Autowired
	private GoldService goldService;
	@Autowired
	private InviteRewardRecordService inviteRewardRecordService;
	@Autowired
	private InviteRewardRuleService inviteRewardRuleService;
	@Autowired
	private RedisCacheUtils redisCacheUtils;
	@Autowired
	private TaskListService taskListService;
	@Autowired
	private TaskRecordService taskRecordService;
	@Autowired
	private AppLogUtils appLogUtils;

	@Override
	public InviteRecordEntity queryObject(Long master,Long apprentice){
		return inviteRecordDao.queryOne(master,apprentice);
	}
	
	@Override
	public List<InviteRecordEntity> queryList(Map<String, Object> map){
		return inviteRecordDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return inviteRecordDao.queryTotal(map);
	}
	
	@Override
	public void save(InviteRecordEntity inviteRecord){
		inviteRecord.setCreateTime(new Date());
		inviteRecord.setDelFlag(0);
		inviteRecordDao.save(inviteRecord);
	}
	
	@Override
	public void update(InviteRecordEntity inviteRecord){
		inviteRecordDao.update(inviteRecord);
	}
	
	@Override
	public void delete(Long master){
		inviteRecordDao.delete(master);
	}
	
	@Override
	public void deleteBatch(Long[] apprentice){
		inviteRecordDao.deleteBatch(apprentice);
	}

	@Override
	public List<InviteRecordEntity> pageByMaster(Long master, Integer page, Integer limit,String time) {
		if(page!=null && limit!=null){
			return inviteRecordDao.pageByMaster(master,(page - 1)*limit,limit,time);
		}
		return inviteRecordDao.pageByMaster(master,page,limit,time);

	}

	@Override
	public Integer totalProfit(Long master) {
		return inviteRecordDao.totalProfit(master);
	}

	@Override
	public InviteRecordEntity queryByApprentice(Long apprentice) {
		return inviteRecordDao.queryByApprentice(apprentice);
	}

	@Override
	public void additional(UserEntity user, TaskRecordEntity readTaskRecord) {

		if(StringUtils.isBlank(user.getInviteCode())) {
			return;
		}

		//获取收徒记录
		InviteRecordEntity inviteRecordEntity = inviteRecordService.queryByApprentice(user.getUserId());
		if(inviteRecordEntity == null) {
			return;
		}

		//今天剩余的秒数
		long surplusSecond = DateUtils.getToday().getTime() / 1000 + 86400 - System.currentTimeMillis() / 1000;

		//获取奖励规则
		InviteRewardRuleEntity inviteRewardRuleEntity = inviteRewardRuleService.queryObject(inviteRecordEntity.getRewardRule());

		//邀请奖励
		Integer flag = redisCacheUtils.get("INVITE_REWARD_" + user.getUserId());
		flag = flag == null ? 0 : flag;
		// -1为已发放金币，不为-1表示没有发放记录
		if(flag != -1){
			//累加阅读金币
			flag += readTaskRecord.getReward();

			//小于发放条件就继续缓存该值
			if(flag < inviteRewardRuleEntity.getThreshold()){
				redisCacheUtils.set("INVITE_REWARD_" + user.getUserId(),flag,surplusSecond);
			}else {
				// 检查是否有待发放奖励(nextReward=-1时为奖励发放完毕)，并且最后一次奖励小于今天（也就是今天还没发放过奖励）
				if (inviteRecordEntity.getNextReward() >= 0 && (inviteRecordEntity.getLastRewardDate() == null || inviteRecordEntity.getLastRewardDate().before(DateUtils.getToday()))) {

					//新增发放记录
					InviteRewardRecordEntity inviteRewardRecordEntity = new InviteRewardRecordEntity();
					inviteRewardRecordEntity.setMaster(inviteRecordEntity.getMaster());
					inviteRewardRecordEntity.setApprentice(inviteRecordEntity.getApprentice());
					inviteRewardRecordEntity.setDay(inviteRecordEntity.getNextReward());
					inviteRewardRecordEntity.setRewardRule(inviteRewardRuleEntity.getId());
					inviteRewardRecordEntity.setGold(InviteRewardRuleUtils.getRewardByDay(inviteRewardRuleEntity,inviteRecordEntity.getNextReward()));
					inviteRewardRecordEntity.setTitle(inviteRecordEntity.getTitle());
					inviteRewardRecordEntity.setDescribe("邀请收益：<font color=\"red\">+" + inviteRewardRecordEntity.getGold() + "</font>金币");
					inviteRewardRecordService.save(inviteRewardRecordEntity);

					//给师傅新增金币
					TaskRecordEntity taskRecordEntity = new TaskRecordEntity();
					taskRecordEntity.setId(new Date());
					taskRecordEntity.setUserId(inviteRewardRecordEntity.getMaster());
					taskRecordEntity.setReward(inviteRewardRecordEntity.getGold());
					taskRecordEntity.setDescribe("邀请收益" + taskRecordEntity.getReward() + "金币");
					taskRecordEntity.setType(37);
					int result = taskRecordService.saveBaseTask(taskRecordEntity, 0);
					if (result == 1) {
						appLogUtils.i(AppLogUtils.LOG_TYPE.INVITE, AppLogUtils.OPERATION.INVITE_REWARD, "发放邀请奖励" + taskRecordEntity.getReward() + "金币", taskRecordEntity.getUserId());
					} else {
						appLogUtils.e(AppLogUtils.LOG_TYPE.INVITE, AppLogUtils.OPERATION.INVITE_REWARD, "发放邀请奖励失败" + taskRecordService.translateErr(result), taskRecordEntity.getUserId(), String.valueOf(result));
					}

					//更新邀请记录
					if (inviteRecordEntity.getNextReward() == 7) {
						inviteRecordEntity.setNextReward(-1);
					} else {
						inviteRecordEntity.setNextReward(inviteRecordEntity.getNextReward() + 1);
					}
					inviteRecordEntity.setLastRewardDate(new Date());
					inviteRecordEntity.setWaitGive(inviteRecordEntity.getWaitGive() - taskRecordEntity.getReward());
					inviteRecordEntity.setGave(inviteRecordEntity.getGave() + taskRecordEntity.getReward());
					inviteRecordEntity.setProfit(inviteRecordEntity.getProfit() + taskRecordEntity.getReward());
					inviteRecordService.update(inviteRecordEntity);

				}
				//标记为已发放
				redisCacheUtils.set("INVITE_REWARD_" + user.getUserId(), -1, surplusSecond);
			}
		}

		Integer summary = redisCacheUtils.get("GOLD_SUMMARY_" + user.getUserId());

		//临时计数器
		summary = summary == null ? readTaskRecord.getReward():summary + readTaskRecord.getReward();

		//提成奖励
		if(summary >= 100){
			//获取提成任务
			TaskListEntity task = taskListService.queryObject(25);
			Integer additional;
			if(!Objects.equals(task.getGoldMini(), task.getGoldMax())){
				additional = (new Random().nextInt(task.getGoldMax() - task.getGoldMini()) + task.getGoldMini());
			}else{
				additional = task.getGoldMini();
			}

			//新增收益记录
			InviteRewardRecordEntity inviteRewardRecordEntity = new InviteRewardRecordEntity();
			inviteRewardRecordEntity.setMaster(inviteRecordEntity.getMaster());
			inviteRewardRecordEntity.setApprentice(inviteRecordEntity.getApprentice());
			inviteRewardRecordEntity.setDay(-1);
			inviteRewardRecordEntity.setGold(additional);
			inviteRewardRecordEntity.setRewardRule(inviteRecordEntity.getRewardRule());
			inviteRewardRecordEntity.setTitle(inviteRecordEntity.getTitle());
			inviteRewardRecordEntity.setDescribe("提成收益：<font color=\"red\">+" + inviteRewardRecordEntity.getGold() + "</font>金币");
			inviteRewardRecordService.save(inviteRewardRecordEntity);

			//给师傅加金币
			TaskRecordEntity taskRecordEntity = new TaskRecordEntity();
			taskRecordEntity.setId(new Date(System.currentTimeMillis() + 1000));
			taskRecordEntity.setUserId(inviteRewardRecordEntity.getMaster());
			taskRecordEntity.setReward(inviteRewardRecordEntity.getGold());
			taskRecordEntity.setDescribe("提成收益" + taskRecordEntity.getReward() + "金币");
			taskRecordEntity.setType(task.getId());
            int result = taskRecordService.saveBaseTask(taskRecordEntity, 0);
            if(result == 1) {
                appLogUtils.i(AppLogUtils.LOG_TYPE.INVITE, AppLogUtils.OPERATION.GOLD_READ,"获得阅读提成" + taskRecordEntity.getReward() + "金币",taskRecordEntity.getUserId());
            }else{
                appLogUtils.e(AppLogUtils.LOG_TYPE.INVITE, AppLogUtils.OPERATION.GOLD_READ,"发放提成奖励失败" + taskRecordService.translateErr(result),taskRecordEntity.getUserId(), String.valueOf(result));
            }

            //更新邀请记录
			inviteRecordEntity.setProfit(inviteRecordEntity.getProfit() + taskRecordEntity.getReward());
			//记录每次徒弟阅读获得100金币时获得30%的提成收益
			inviteRecordEntity.setReadProfit(inviteRecordEntity.getReadProfit()+taskRecordEntity.getReward());
			inviteRecordService.update(inviteRecordEntity);
			summary = summary -100;
		}
		redisCacheUtils.set("GOLD_SUMMARY_" + user.getUserId(),summary,surplusSecond);
	}

	/**
	 * 邀请收徒的立即奖励
	 * @param user
	 */
	@Override
	public void firstReward(UserEntity user){

		//没有师傅
		if(StringUtils.isBlank(user.getInviteCode())){
			return;
		}

		//已经发放过奖励
		InviteRecordEntity inviteRecordEntity = inviteRecordService.queryByApprentice(user.getUserId());
		if(inviteRecordEntity == null || inviteRecordEntity.getNextReward() != 0){
			return;
		}

		//取出奖励规则
		InviteRewardRuleEntity inviteRewardRuleEntity = inviteRewardRuleService.queryObject(inviteRecordEntity.getRewardRule());

		//做金币发放记录
		InviteRewardRecordEntity inviteRewardRecordEntity = new InviteRewardRecordEntity();
		inviteRewardRecordEntity.setMaster(inviteRecordEntity.getMaster());
		inviteRewardRecordEntity.setApprentice(inviteRecordEntity.getApprentice());
		inviteRewardRecordEntity.setDay(0);
		inviteRewardRecordEntity.setRewardRule(inviteRecordEntity.getRewardRule());
		inviteRewardRecordEntity.setGold(inviteRewardRuleEntity.getFirstReward());
		inviteRewardRecordEntity.setTitle(inviteRecordEntity.getTitle());
		inviteRewardRecordEntity.setDescribe("邀请收益：<font color=\"red\">+" + inviteRewardRecordEntity.getGold() + "</font>金币");
		inviteRewardRecordService.save(inviteRewardRecordEntity);

		//更新邀请记录
		inviteRecordEntity.setProfit(inviteRewardRuleEntity.getFirstReward());
		inviteRecordEntity.setGave(inviteRewardRuleEntity.getFirstReward());
		inviteRecordEntity.setWaitGive(InviteRewardRuleUtils.getTotal(inviteRewardRuleEntity) - inviteRewardRuleEntity.getFirstReward());
		inviteRecordEntity.setNextReward(1);
		inviteRecordEntity.setLastRewardDate(new Date());
		inviteRecordService.update(inviteRecordEntity);

		//给老用户加金币
		TaskRecordEntity entity = new TaskRecordEntity();
		entity.setId(new Date());
		entity.setUserId(inviteRewardRecordEntity.getMaster());
		entity.setReward(inviteRewardRecordEntity.getGold());
		entity.setDescribe("邀请收益" + inviteRewardRecordEntity.getGold() + "金币");
		entity.setType(37);

		int result = taskRecordService.saveBaseTask(entity, 0);
		if(result == 1) {
			appLogUtils.i(AppLogUtils.LOG_TYPE.INVITE, AppLogUtils.OPERATION.INVITE_REWARD,"发放邀请奖励" + entity.getReward() + "金币",entity.getUserId());
		}else{
			appLogUtils.e(AppLogUtils.LOG_TYPE.INVITE, AppLogUtils.OPERATION.INVITE_REWARD,"发放邀请奖励失败" + taskRecordService.translateErr(result),entity.getUserId(), String.valueOf(result));
		}
	}

	@Override
	public int SummaryInvitationsNum(String startTime, String endTime) {
		return inviteRecordDao.SummaryInvitationsNum(startTime,endTime);
	}
}
