package io.renren.modules.stats.task;

import io.renren.modules.stats.entity.WithdrawalsStatsEntity;
import io.renren.modules.stats.service.WithdrawalsStatsService;
import io.renren.modules.stats.utils.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Created by ITMX on 2018/2/12.
 */
@Component("withdrawalsStatsTask")
public class WithdrawalsStatsTask {

    @Autowired
    private WithdrawalsStatsService withdrawalsStatsService;

    private Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 开始时级统计
   */
    public void hour(){
        //TODO 暂时不统计时级
//        logger.info("开始时级统计...");
//        WithdrawalsStatsEntity entity = new WithdrawalsStatsEntity();
//        entity.setCreateTime(new Date());
//        entity.setThanHour(DateUtils.preHour(entity.getCreateTime()));
//        Date date = DateUtils.preHourTime(entity.getCreateTime());
//        entity.setThanDay(DateUtils.currentDayTime(date));
//        entity.setThanMonth(DateUtils.currentMonth(date));
//        entity.setThanYear(DateUtils.currentYear(date));
//        entity.setType("HOUR");
//
//        //统计上一个小时提现的金额
//        Long total = withdrawalsStatsService.sumGoldByTime(date, DateUtils.currentHourTime(entity.getCreateTime()));
//        entity.setTotal(total==null?0:total);
//        withdrawalsStatsService.save(entity);
//        logger.info("完成时级统计...");
    }

    /**
     * 每天统计
     */
    public void day(){
        logger.info("开始日级统计...");
        WithdrawalsStatsEntity entity = new WithdrawalsStatsEntity();
        entity.setCreateTime(new Date());
        Date date = DateUtils.preDayTime(entity.getCreateTime());
        entity.setThanDay(DateUtils.currentDayTime(date));
        entity.setThanMonth(DateUtils.currentMonth(date));
        entity.setThanYear(DateUtils.currentYear(date));
        entity.setType("DAY");

        //统计昨天提现的金额
        Long compairson =withdrawalsStatsService.sumGoldFromStats(date, DateUtils.currentDayTime(entity.getCreateTime()),"DAY");
//        if(total == null){
//            total = withdrawalsStatsService.sumGoldByTime(date, DateUtils.currentDayTime(entity.getCreateTime()));
//        }

        Long total=withdrawalsStatsService.sumOrderStatsByStatus(date,DateUtils.currentDayTime(entity.getCreateTime()),10);
        Long totalComplete=withdrawalsStatsService.sumOrderStatsByStatus(date,DateUtils.currentDayTime(entity.getCreateTime()),2);
        Long totalFail=withdrawalsStatsService.sumOrderStatsByStatus(date,DateUtils.currentDayTime(entity.getCreateTime()),-1);

        entity.setTotalComplete(totalComplete);
        entity.setTotalFail(totalFail);
        entity.setTotal(total==null?0L:total);
        entity.setCompairson(total-(compairson==null?0L:compairson));

        withdrawalsStatsService.save(entity);
        logger.info("完成日级统计...");
    }

    /**
     * 每月统计
     */
    public void month(){
        logger.info("开始月级统计...");
        WithdrawalsStatsEntity entity = new WithdrawalsStatsEntity();
        entity.setCreateTime(new Date());
        Date date = DateUtils.preMonthTime(entity.getCreateTime());
        entity.setThanDay(date);
        entity.setThanMonth(DateUtils.currentMonth(date));
        entity.setThanYear(DateUtils.currentYear(date));
        entity.setType("MONTH");

        //统计昨天提现的金额
//        Long total = withdrawalsStatsService.sumGoldFromStats(date, DateUtils.currentMonthTime(entity.getCreateTime()),"DAY");
//        if(total == null){
//            total = withdrawalsStatsService.sumGoldByTime(date, DateUtils.currentMonthTime(entity.getCreateTime()));
//        }
        Long compairson = withdrawalsStatsService.sumGoldFromStats(date, DateUtils.currentMonthTime(entity.getCreateTime()),"MONTH");

        Long total=withdrawalsStatsService.sumOrderStatsByStatus(date, DateUtils.currentMonthTime(entity.getCreateTime()),10);
        Long totalComplete=withdrawalsStatsService.sumOrderStatsByStatus(date, DateUtils.currentMonthTime(entity.getCreateTime()),2);
        Long totalFail=withdrawalsStatsService.sumOrderStatsByStatus(date, DateUtils.currentMonthTime(entity.getCreateTime()),-1);

        entity.setTotalComplete(totalComplete);
        entity.setTotalFail(totalFail);
        entity.setTotal(total==null?0L:total);
        entity.setCompairson(total-(compairson==null?0L:compairson));

        withdrawalsStatsService.save(entity);
        logger.info("完成月级统计...");
    }

    /**
     * 每年统计
     */
    public void year(){
        logger.info("开始年级统计...");
        WithdrawalsStatsEntity entity = new WithdrawalsStatsEntity();
        entity.setCreateTime(new Date());
        Date date = DateUtils.preYearTime(entity.getCreateTime());
        entity.setThanDay(date);
        entity.setThanMonth(1);
        entity.setThanYear(DateUtils.currentYear(date));
        entity.setType("YEAR");

        //统计昨天提现的金额
//        Long total = withdrawalsStatsService.sumGoldFromStats(date, DateUtils.currentYearTime(entity.getCreateTime()),"DAY");
//        if(total == null){
//            total = withdrawalsStatsService.sumGoldByTime(date, DateUtils.currentYearTime(entity.getCreateTime()));
//        }
//        entity.setTotal(total==null?0:total);

        Long compairson = withdrawalsStatsService.sumGoldFromStats(date, DateUtils.currentDayTime(entity.getCreateTime()),"YEAR");

        Long total=withdrawalsStatsService.sumOrderStatsByStatus(date,DateUtils.currentDayTime(entity.getCreateTime()),10);
        Long totalComplete=withdrawalsStatsService.sumOrderStatsByStatus(date,DateUtils.currentDayTime(entity.getCreateTime()),2);
        Long totalFail=withdrawalsStatsService.sumOrderStatsByStatus(date,DateUtils.currentDayTime(entity.getCreateTime()),-1);

        entity.setTotalComplete(totalComplete);
        entity.setTotalFail(totalFail);
        entity.setTotal(total==null?0L:total);
        entity.setCompairson(total-(compairson==null?0L:compairson));

        withdrawalsStatsService.save(entity);
        logger.info("完成年级统计...");
    }
}
