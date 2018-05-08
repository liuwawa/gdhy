package io.renren.modules.api.controller;

import io.renren.common.utils.DateUtils;
import io.renren.common.utils.MsgStatus;
import io.renren.common.utils.R;
import io.renren.modules.admin.entity.EverydaySignEntity;
import io.renren.modules.admin.entity.TaskListEntity;
import io.renren.modules.admin.entity.TaskRecordEntity;
import io.renren.modules.admin.service.EverydaySignService;
import io.renren.modules.admin.service.GoldService;
import io.renren.modules.admin.service.TaskListService;
import io.renren.modules.admin.service.TaskRecordService;
import io.renren.modules.api.annotation.Login;
import io.renren.modules.api.annotation.LoginUser;
import io.renren.modules.api.entity.SignRecord;
import io.renren.modules.api.entity.SignRecordStatus;
import io.renren.modules.api.entity.UserEntity;
import io.renren.modules.api.service.UserService;
import io.renren.modules.api.utils.AppLogUtils;
import io.renren.modules.invite.service.InviteRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * Created by ITMX on 2017/12/24.
 */
@RestController
@RequestMapping("/api/task")
public class ApiTaskController {

    @Autowired
    private TaskRecordService taskRecordService;
    @Autowired
    private AppLogUtils appLogUtils;
    @Autowired
    private TaskListService taskListService;
    @Autowired
    private UserService userService;
    @Autowired
    private EverydaySignService everydaySignService;
    @Autowired
    private InviteRecordService inviteRecordService;
    @Autowired
    private GoldService goldService;

    @Value("${private.termOfValidity}")
    private Integer termOfValidity;

    /**
     * 获取任务中心任务列表
     * @param user
     * @return
     */
    //@Login 允许匿名访问
    @PostMapping("list")
    public R list(@LoginUser UserEntity user){
        //获取新手任务
        List<TaskListEntity> novice = taskListService.queryNoviceList();
        if(novice == null){
            novice = taskListService.queryNoviceList();
            if(novice == null){
                novice = new ArrayList<>();
            }
        }

        //获取日常任务
        List<TaskListEntity> daily = taskListService.queryDailyList();
        if(daily == null){
            daily = taskListService.queryDailyList();
            if(daily == null){
                daily = new ArrayList<>();
            }
        }

        if(user == null){
            return R.ok().put("daily",daily).put("novice",novice);
        }

        //检查是否已完成新手任务
        if(user.getNoviceTask() == 0) {
            //查询新手任务，过滤掉已完成的，标记可领取的
            List<Integer> noviceFinishIdList = taskRecordService.noviceFinishIdList(user.getUserId());
            for (int i = 0; i < novice.size(); i++) {
                TaskListEntity taskEntity = novice.get(i);
                if (noviceFinishIdList.contains(taskEntity.getId())) {
                    novice.remove(taskEntity);
                    i--;
                }else{
                    //检查是否可领取
                    boolean receive = taskListService.isReceive(taskEntity, user);
                    if (receive) {
                        taskEntity.setReceive(1);
                    }
                }
            }
            //没有新手任务了，记录一下
            if(novice.size() == 0){
                UserEntity userEntity = new UserEntity();
                userEntity.setUserId(user.getUserId());
                userEntity.setNoviceTask(1);
                userEntity.setFinishNoviceTime(new Date());
                userService.update(userEntity);
            }
        }else{
            novice = Collections.emptyList();
        }

        //查询日常任务的记录标记为可领取，已领取
        List<Integer> dailyFinishIdList = taskRecordService.dailyFinishIdList(user.getUserId());
        for (TaskListEntity t : daily) {
            if (dailyFinishIdList.contains(t.getId())) {
                t.setReceive(2);
            }else {
                //检查是否可领取
                boolean receive = taskListService.isReceive(t, user);
                if (receive) {
                    t.setReceive(1);
                }
            }
        }

        return R.ok().put("daily",daily).put("novice",novice);
    }


    /**
     * 获得时段奖励
     * @param id
     * @param userId
     * @return
     */
    @Login
    @PostMapping("duration")
    public R duration(@RequestParam Long id,@RequestAttribute Long userId){
        //检查时间戳是否有效（15秒）
        long div = Math.abs(System.currentTimeMillis() - id);
        if(div > termOfValidity * 1000){
            appLogUtils.w(AppLogUtils.LOG_TYPE.GOLD, AppLogUtils.OPERATION.GOLD_DURATION,"时差过大：div=" + id,userId);
            return R.error(MsgStatus.RECEIVE_FAILURE);
        }

        Integer typeId = 32;

        //拿出时段奖励任务
        TaskListEntity taskEntity = taskListService.queryObject(typeId);

        TaskRecordEntity entity = new TaskRecordEntity();
        entity.setUserId(userId);
        entity.setType(typeId);
        entity.setId(new Date(id));

        //计算成长值
        Integer reward;
        if(!Objects.equals(taskEntity.getGoldMini(), taskEntity.getGoldMax())){
            reward = (new Random().nextInt(taskEntity.getGoldMax() - taskEntity.getGoldMini()) + taskEntity.getGoldMini());
        }else{
            reward = taskEntity.getGoldMini();
        }

        entity.setReward(reward);
        entity.setDescribe("时段奖励" + reward + "成长值");

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        int limit = calendar.get(Calendar.MINUTE) * 60 + calendar.get(Calendar.SECOND);

        int result;
        try {
            result = taskRecordService.saveBaseTask(entity,limit);
        } catch (DuplicateKeyException e) {
            appLogUtils.e(AppLogUtils.LOG_TYPE.GOLD, AppLogUtils.OPERATION.GOLD_DURATION,"重复领取",userId);
            return R.error(MsgStatus.RECEIVE_FAILURE);
        }

        if(result == 1){
            appLogUtils.i(AppLogUtils.LOG_TYPE.GOLD, AppLogUtils.OPERATION.GOLD_DURATION,"获得" + reward + "成长值",userId);
            return R.ok().put("reward",reward);
        }

        appLogUtils.e(AppLogUtils.LOG_TYPE.GOLD, AppLogUtils.OPERATION.GOLD_DURATION,"领取成长值失败" + taskRecordService.translateErr(result),userId, String.valueOf(result));

        return R.error(MsgStatus.RECEIVE_FAILURE);
    }


    /**
     * 完成阅读任务
     * @param id
     * @param user
     * @return
     */
    @Login
    @PostMapping("read")
    public R read(@RequestParam Long id,@LoginUser UserEntity user){
        long div = Math.abs(System.currentTimeMillis() - id);
        if(div > termOfValidity * 1000){
            appLogUtils.w(AppLogUtils.LOG_TYPE.GOLD, AppLogUtils.OPERATION.GOLD_READ,"时差过大：div=" + id,user.getUserId());
            return R.error(MsgStatus.RECEIVE_FAILURE);
        }

        Integer typeId = 33;

        //拿出阅读任务
        TaskListEntity taskEntity = taskListService.queryObject(typeId);

        TaskRecordEntity readTaskRecord = new TaskRecordEntity();
        readTaskRecord.setUserId(user.getUserId());
        readTaskRecord.setType(typeId);
        readTaskRecord.setId(new Date(id));

        //计算可得金币
        Integer reward;
        if(!Objects.equals(taskEntity.getGoldMini(), taskEntity.getGoldMax())){
            reward = (new Random().nextInt(taskEntity.getGoldMax() - taskEntity.getGoldMini()) + taskEntity.getGoldMini());
        }else{
            reward = taskEntity.getGoldMini();
        }

        readTaskRecord.setReward(reward);
        readTaskRecord.setDescribe("阅读获得成长值" + reward);

        int result;
        int limit = taskEntity.getDuration();
        try {
            result = taskRecordService.saveBaseTask(readTaskRecord,limit);
        } catch (DuplicateKeyException e) {
            appLogUtils.e(AppLogUtils.LOG_TYPE.GOLD, AppLogUtils.OPERATION.GOLD_READ,"重复领取",user.getUserId());
            return R.error(MsgStatus.RECEIVE_FAILURE);
        }

        if(result == 1){
            appLogUtils.i(AppLogUtils.LOG_TYPE.GOLD, AppLogUtils.OPERATION.GOLD_READ,"获得" + reward + "成长值",user.getUserId());
            //在金币表中添加今日阅读获得的金币
            goldService.updateTodayReadByUserId(user.getUserId(),reward);
            //给师傅做贡献
            inviteRecordService.additional(user,readTaskRecord);

            return R.ok().put("reward",reward);
        }
        
        appLogUtils.e(AppLogUtils.LOG_TYPE.GOLD, AppLogUtils.OPERATION.GOLD_READ,"领取成长值失败" + taskRecordService.translateErr(result),user.getUserId(), String.valueOf(result));

        return R.error(MsgStatus.RECEIVE_FAILURE);
    }

    /**
     * 完成看视频任务
     * @param id
     * @param userId
     * @return
     */
    @Login
    @PostMapping("watch")
    public R watch(@RequestParam Long id,@RequestAttribute Long userId){
        long div = Math.abs(System.currentTimeMillis() - id);
        if(div > termOfValidity * 1000){
            appLogUtils.w(AppLogUtils.LOG_TYPE.GOLD, AppLogUtils.OPERATION.GOLD_WATCH,"时差过大：div=" + id,userId);
            return R.error(MsgStatus.RECEIVE_FAILURE);
        }

        Integer typeId = 34;

        //拿出看视频任务
        TaskListEntity taskEntity = taskListService.queryObject(typeId);

        TaskRecordEntity entity = new TaskRecordEntity();
        entity.setUserId(userId);
        entity.setType(typeId);
        entity.setId(new Date(id));

        //计算成长值
        Integer reward;
        if(!Objects.equals(taskEntity.getGoldMini(), taskEntity.getGoldMax())){
            reward = (new Random().nextInt(taskEntity.getGoldMax() - taskEntity.getGoldMini()) + taskEntity.getGoldMini());
        }else{
            reward = taskEntity.getGoldMini();
        }

        entity.setReward(reward);
        entity.setDescribe("看视频获得成长值" + reward);

        int result;
        int limit = taskEntity.getDuration();
        try {
            result = taskRecordService.saveBaseTask(entity,limit);
        } catch (DuplicateKeyException e) {
            appLogUtils.e(AppLogUtils.LOG_TYPE.GOLD, AppLogUtils.OPERATION.GOLD_WATCH,"重复领取",userId);
            return R.error(MsgStatus.RECEIVE_FAILURE);
        }

        if(result == 1){
            appLogUtils.i(AppLogUtils.LOG_TYPE.GOLD, AppLogUtils.OPERATION.GOLD_WATCH,"获得" + reward + "成长值",userId);
            //在金币表中添加今日视频获得的金币
            goldService.updateTodayVedioByUserId(userId,reward);
            return R.ok().put("reward",reward);
        }

        appLogUtils.e(AppLogUtils.LOG_TYPE.GOLD, AppLogUtils.OPERATION.GOLD_WATCH,"领取成长值失败，" + taskRecordService.translateErr(result),userId, String.valueOf(result));

        return R.error(MsgStatus.RECEIVE_FAILURE);
    }

    /**
     * 完成新手任务和日常任务
     * @param id
     * @param typeId
     * @param user
     * @return
     */
    @Login
    @PostMapping("finish")
    public R finish(@RequestParam Long id,
                    @RequestParam Integer typeId,
                    @LoginUser UserEntity user){
        long div = Math.abs(System.currentTimeMillis() - id);
        if(div > termOfValidity * 1000){
            appLogUtils.w(AppLogUtils.LOG_TYPE.GOLD, AppLogUtils.OPERATION.TASK_FINISH,"时差过大：div=" + id,user.getUserId());
            return R.error(MsgStatus.RECEIVE_FAILURE);
        }

        //拿出任务信息
        TaskListEntity taskEntity = taskListService.queryObject(typeId);
        if(taskEntity == null){
        }else if("daily".equals(taskEntity.getType())){
            return daily(id,taskEntity,user);
        }else if("novice".equals(taskEntity.getType())){
            return novice(id,taskEntity,user);
        }

        appLogUtils.e(AppLogUtils.LOG_TYPE.GOLD, AppLogUtils.OPERATION.TASK_FINISH,"非法任务ID：" + typeId,user.getUserId());

        return R.error(MsgStatus.RECEIVE_FAILURE);
    }

    /**
     * 完成新手任务
     * @param id
     * @param taskEntity
     * @param user
     * @return
     */
    private R novice(Long id,TaskListEntity taskEntity,UserEntity user){

        if("innerJump".equals(taskEntity.getEvent()) || "outnerJump".equals(taskEntity.getEvent()) || "shareToGroup".equals(taskEntity.getEvent()) || "shareToCOF".equals(taskEntity.getEvent())){
            //这几种类型的任务后台不校验，信任客户端
        }else {
            boolean receive = taskListService.isReceive(taskEntity, user);
            if (!receive) {
                appLogUtils.e(AppLogUtils.LOG_TYPE.GOLD, AppLogUtils.OPERATION.TASK_NOVICE, "任务未完成", user.getUserId(), Integer.toString(taskEntity.getId()));
                return R.error(MsgStatus.RECEIVE_FAILURE);
            }
        }

        TaskRecordEntity entity = new TaskRecordEntity();
        entity.setUserId(user.getUserId());
        entity.setType(taskEntity.getId());
        entity.setId(new Date(id));

        //计算成长值
        Integer reward;
        if(!Objects.equals(taskEntity.getGoldMini(), taskEntity.getGoldMax())){
            reward = (new Random().nextInt(taskEntity.getGoldMax() - taskEntity.getGoldMini()) + taskEntity.getGoldMini());
        }else{
            reward = taskEntity.getGoldMini();
        }

        entity.setReward(reward);
        entity.setDescribe("完成新手成长获得成长值" + reward);

        int result;
        try {
            result = taskRecordService.saveNoviceTask(entity);
        } catch (DuplicateKeyException e) {
            appLogUtils.e(AppLogUtils.LOG_TYPE.GOLD, AppLogUtils.OPERATION.TASK_NOVICE,"重复领取", user.getUserId());
            return R.error(MsgStatus.RECEIVE_FAILURE);
        }

        if(result == 1){
            appLogUtils.i(AppLogUtils.LOG_TYPE.GOLD, AppLogUtils.OPERATION.TASK_NOVICE,"获得" + reward + "成长值",user.getUserId());
            return R.ok().put("reward",reward);
        }

        appLogUtils.e(AppLogUtils.LOG_TYPE.GOLD, AppLogUtils.OPERATION.TASK_NOVICE,"领取成长值失败，" + taskRecordService.translateErr(result),user.getUserId(), String.valueOf(result));

        return R.error(MsgStatus.RECEIVE_FAILURE);
    }


    /**
     * 完成日常任务
     * @param id
     * @param taskEntity
     * @param user
     * @return
     */
    private R daily( Long id,TaskListEntity taskEntity,UserEntity user){
        Long userId = user.getUserId();

        if("innerJump".equals(taskEntity.getEvent()) || "outnerJump".equals(taskEntity.getEvent()) || "shareToGroup".equals(taskEntity.getEvent()) || "shareToCOF".equals(taskEntity.getEvent())){
            //这几种类型的任务后台不校验，信任客户端
        }else {
            boolean receive = taskListService.isReceive(taskEntity, user);
            if (!receive) {
                appLogUtils.e(AppLogUtils.LOG_TYPE.GOLD, AppLogUtils.OPERATION.TASK_DAILY, "任务未完成", user.getUserId(), Integer.toString(taskEntity.getId()));
                return R.error(MsgStatus.RECEIVE_FAILURE);
            }
        }

        TaskRecordEntity entity = new TaskRecordEntity();
        entity.setUserId(userId);
        entity.setType(taskEntity.getId());
        entity.setId(new Date(id));

        //计算成长值
        Integer reward;
        if(!Objects.equals(taskEntity.getGoldMini(), taskEntity.getGoldMax())){
            reward = (new Random().nextInt(taskEntity.getGoldMax() - taskEntity.getGoldMini()) + taskEntity.getGoldMini());
        }else{
            reward = taskEntity.getGoldMini();
        }

        entity.setReward(reward);
        entity.setDescribe("完成日常成长获得成长值" + reward);

        int result;
        try {
            result = taskRecordService.saveDailyTask(entity);
        } catch (DuplicateKeyException e) {
            appLogUtils.e(AppLogUtils.LOG_TYPE.GOLD, AppLogUtils.OPERATION.TASK_DAILY,"重复领取",userId);
            return R.error(MsgStatus.RECEIVE_FAILURE);
        }

        if(result == 1){
            appLogUtils.i(AppLogUtils.LOG_TYPE.GOLD, AppLogUtils.OPERATION.TASK_DAILY,"获得" + reward + "成长值",userId);
            return R.ok().put("reward",reward);
        }

        appLogUtils.e(AppLogUtils.LOG_TYPE.GOLD, AppLogUtils.OPERATION.TASK_DAILY,"领取成长值失败，" + taskRecordService.translateErr(result),userId, String.valueOf(result));

        return R.error(MsgStatus.RECEIVE_FAILURE);
    }


    /**
     * 获取收益记录
     * @param userId
     * @return
     */
    @Login
    @PostMapping("record")
    public R record(@RequestAttribute Long userId,
                    @RequestParam Integer page,
                    @RequestParam Integer limit){
        List<TaskRecordEntity> list = taskRecordService.queryByUser(userId, 0, page, limit);
        return R.ok().put("list",list);
    }

    /**
     * 每日签到
     * @return
     */
    @Login
    @PostMapping("sign")
    public R sign(@RequestAttribute Long userId){
        //先检查今天是否已经签到了
        SignRecord signRecord = everydaySignService.querySignRecord(userId);
        Date lastDay = signRecord.getLastDay();
        int days = 1;//默认是拿第一天的奖励
        if(lastDay != null){
            int diff = DateUtils.differentDays(lastDay, new Date());
            if(diff == 0){
                return R.error(MsgStatus.RECEIVE_FAILURE.value(),"今日已签到");
            }else if(diff == 1){
                //昨天已经签到过，days值得信任
                days = signRecord.getDays() + 1;
                if(days == 8){
                    days = 1;
                }
            }
        }
        //更加days拿出奖励
        EverydaySignEntity signEntity = everydaySignService.queryObject(days);

        Integer reward = signEntity.getReward();

        //做一条记录
        TaskRecordEntity entity = new TaskRecordEntity();
        entity.setId(new Date());
        entity.setType(40);
        entity.setUserId(userId);
        entity.setReward(reward);
        entity.setDescribe("签到获得成长值" + reward);

        int result;
        try {
            result = taskRecordService.saveSignTask(entity);
        } catch (DuplicateKeyException e) {
            appLogUtils.e(AppLogUtils.LOG_TYPE.GOLD, AppLogUtils.OPERATION.TASK_SIGN,"重复领取",userId);
            return R.error(MsgStatus.RECEIVE_FAILURE);
        }

        if(result == 1){
            appLogUtils.i(AppLogUtils.LOG_TYPE.GOLD, AppLogUtils.OPERATION.TASK_SIGN,"获得" + reward + "成长值",userId);
            return R.ok().put("reward",reward);
        }

        appLogUtils.e(AppLogUtils.LOG_TYPE.GOLD, AppLogUtils.OPERATION.TASK_SIGN,"签到失败，" + taskRecordService.translateErr(result),userId, String.valueOf(result));

        return R.error(MsgStatus.RECEIVE_FAILURE);
    }

    /**
     * 获取签到记录和列表
     * @return
     */
    //@Login 允许不登陆时使用该接口
    @PostMapping("signRecord")
    public R signRecord(@RequestAttribute(required = false) Long userId){
        List<EverydaySignEntity> list = everydaySignService.queryList(Collections.emptyMap());

        //未登录的用户直接返回签到列表
        if(userId == null){
            return R.ok().put("list",list).put("today",false);
        }

        SignRecord signRecord = everydaySignService.querySignRecord(userId);
        Date lastDay = signRecord.getLastDay();
        Integer days = signRecord.getDays();
        Integer diff;
        if(lastDay == null){
            days = 0;
            diff = -1;
        }else {
            diff = DateUtils.differentDays(lastDay, new Date());
            //如果昨天未签到，则从零开始
            if(diff == 0){
                //如果是今天已经签到，days值得信任
            }else if(diff == 1){
                if(days == 7){
                    //昨天是第7次签到，今天应该是签第一次
                    days = 0;
                }
            }else{
                days = 0;
            }
        }

        //过去的日子都设为已领取
        for (int i = 0; i < days; i++) {
            EverydaySignEntity signEntity = list.get(i);
            signEntity.setReceive(SignRecordStatus.SIGN_ED);
        }
        //今天还没签到
        if(diff == 1){
            //设置今天为可签到
            list.get(days).setReceive(SignRecordStatus.SIGN_ABLE);
        }

        //今日是否已签到
        return R.ok().put("list",list).put("today",diff == 0);
    }
}
