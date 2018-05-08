package io.renren.modules.stats.task;

import io.renren.modules.invite.service.InviteRecordService;
import io.renren.modules.stats.entity.UserStatsEntity;
import io.renren.modules.stats.service.UserStatsService;
import io.renren.modules.stats.utils.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by ITMX on 2018/2/5.
 */
@Component("userStatsTask")
public class UserStatsTask {

    @Autowired
    private UserStatsService userStatsService;
    @Autowired
    private InviteRecordService InviteRecordService;

    private Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 每小时统计
     */
    public void hour(){
        logger.info("开始时级统计...");
        UserStatsEntity entity = new UserStatsEntity();
        entity.setCreateTime(new Date());
        entity.setThanHour(DateUtils.preHour(entity.getCreateTime()));
        Date date = DateUtils.preHourTime(entity.getCreateTime());
        entity.setThanDay(DateUtils.currentDayTime(date));
        entity.setThanMonth(DateUtils.currentMonth(date));
        entity.setThanYear(DateUtils.currentYear(date));
        entity.setType("HOUR");

        Calendar calendar = Calendar.getInstance();
        Date paramDate = DateUtils.preHours(new Date(), -2);
        calendar.setTime(paramDate);
//        List<UserStatsEntity> userStatsEntity = userStatsService.RangeByHour("HOUR",new SimpleDateFormat("yyyy-MM-dd").format(paramDate),new SimpleDateFormat("yyyy-MM-dd").format(paramDate),calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.HOUR_OF_DAY));
        Integer yesterdayTotal = userStatsService.countUserByTime(DateUtils.preHours(new Date(),-2), DateUtils.preHourTime(new Date()));

        //统计上一个小时新增用户数
        Integer increase = userStatsService.countUserByTime(DateUtils.preHourTime(new Date()), DateUtils.currentHourTime(new Date()));
        entity.setIncrease(increase);
        //获取上一次小时统计
        UserStatsEntity preHour = userStatsService.preHour();
        if(preHour == null){
            int total = userStatsService.countAll();
            entity.setTotal(total);
        }else{
            entity.setTotal(preHour.getTotal() + increase);
        }

        entity.setCompairson(entity.getTotal() - (yesterdayTotal==null?0:yesterdayTotal));

        int invitationsNum=InviteRecordService.SummaryInvitationsNum(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(DateUtils.preHourTime(new Date())),new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format( DateUtils.currentHourTime(new Date())));

        entity.setInvitationsNum(invitationsNum);
        userStatsService.save(entity);
        logger.info("完成时级统计...");
    }

    /**
     * 每天统计
     */
    public void day(){
        logger.info("开始日级统计...");
        UserStatsEntity entity = new UserStatsEntity();
        entity.setCreateTime(new Date());
        Date date = DateUtils.preDayTime(entity.getCreateTime());
        entity.setThanDay(date);
        entity.setThanMonth(DateUtils.currentMonth(date));
        entity.setThanYear(DateUtils.currentYear(date));
        entity.setType("DAY");

        Integer yesterdayTotal = userStatsService.countUserByTime(DateUtils.preDayTime(date), date);


        //统计昨天新增用户数
        Integer increase = userStatsService.countUserByTime(date, DateUtils.currentDayTime(entity.getCreateTime()));
        entity.setIncrease(increase);
        //统计所有用户
        entity.setTotal(userStatsService.countAll());

        entity.setCompairson(entity.getTotal()-(yesterdayTotal==null?0:yesterdayTotal));

        int invitationsNum=InviteRecordService.SummaryInvitationsNum(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date), new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(DateUtils.currentDayTime(entity.getCreateTime())));

        entity.setInvitationsNum(invitationsNum);
        userStatsService.save(entity);
        logger.info("完成日级统计...");
    }

    /**
     * 每月统计
     */
    public void month(){
        logger.info("开始月级统计...");
        UserStatsEntity entity = new UserStatsEntity();
        entity.setCreateTime(new Date());
        Date date = DateUtils.preMonthTime(entity.getCreateTime());
        entity.setThanDay(date);
        entity.setThanMonth(DateUtils.currentMonth(date));
        entity.setThanYear(DateUtils.currentYear(date));
        entity.setType("MONTH");

        List<UserStatsEntity> userStatsEntity = userStatsService.RangeByCustom("MONTH",DateUtils.preTwoMonths(new Date()), DateUtils.preMonthTime(new Date()), "than_day");

        //统计昨天新增用户数
        Integer increase = userStatsService.countUserByTime(date, DateUtils.currentMonthTime(entity.getCreateTime()));
        entity.setIncrease(increase);
        //统计所有用户
        entity.setTotal(userStatsService.countAll());

        if(userStatsEntity.size()==0){
            entity.setCompairson(entity.getTotal());
        }else{
            entity.setCompairson(entity.getTotal()-userStatsEntity.get(0).getTotal());
        }
        int invitationsNum=InviteRecordService.SummaryInvitationsNum(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date), new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(DateUtils.currentMonthTime(entity.getCreateTime())));

        entity.setInvitationsNum(invitationsNum);
        userStatsService.save(entity);
        logger.info("完成月级统计...");
    }

    /**
     * 每年统计
     */
    public void year(){
        logger.info("开始年级统计...");
        UserStatsEntity entity = new UserStatsEntity();
        entity.setCreateTime(new Date());

        Date date = DateUtils.preYearTime(entity.getCreateTime());
        entity.setThanYear(DateUtils.currentYear(date));
        entity.setType("YEAR");
        entity.setThanMonth(1);
        entity.setThanDay(DateUtils.preMonthTime(entity.getCreateTime()));

        List<UserStatsEntity> userStatsEntities = userStatsService.RangeByCustom("YEAR", String.valueOf(DateUtils.currentYear(date) - 1), String.valueOf(DateUtils.currentYear(date) - 1), "than_year");


        //统计昨天新增用户数
        Integer increase = userStatsService.countUserByTime(date, DateUtils.currentYearTime(entity.getCreateTime()));
        entity.setIncrease(increase);
        //统计所有用户
        entity.setTotal(userStatsService.countAll());

        if(userStatsEntities.size()==0){
            entity.setCompairson(entity.getTotal());
        }else{
            entity.setCompairson(entity.getTotal()-userStatsEntities.get(0).getTotal());
        }

        int invitationsNum=InviteRecordService.SummaryInvitationsNum(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date), new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(DateUtils.currentYearTime(entity.getCreateTime())));

        entity.setInvitationsNum(invitationsNum);
        userStatsService.save(entity);
        logger.info("开始年级统计...");
    }
}
