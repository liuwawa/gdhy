package io.renren.modules.stats.task;

import io.renren.modules.stats.entity.FinanceStatsEntity;
import io.renren.modules.stats.service.FinanceStatsService;
import io.renren.modules.stats.utils.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * Created by ITMX on 2018/2/26.
 */
@Component("financeStatsTask")
public class FinanceStatsTask {

    @Autowired
    private FinanceStatsService financeStatsService;

    private Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 开始时级统计
     */
    public void hour(){
        logger.info("开始时级统计...");
        FinanceStatsEntity entity = new FinanceStatsEntity();
        entity.setCreateTime(new Date());
        entity.setThanHour(DateUtils.preHour(entity.getCreateTime()));
        Date date = DateUtils.preHourTime(entity.getCreateTime());
        entity.setThanDay(DateUtils.currentDayTime(date));
        entity.setThanMonth(DateUtils.currentMonth(date));
        entity.setThanYear(DateUtils.currentYear(date));
        entity.setType("HOUR");

        //统计上一个小时提现的金额
        Integer total = financeStatsService.sumRmbByTime(DateUtils.preHourTime(new Date()), DateUtils.currentHourTime(new Date()));
        entity.setTotal(total==null?0:total);
        financeStatsService.save(entity);
        logger.info("完成时级统计...");
    }

    /**
     * 每天统计
     */
    public void day(){
        logger.info("开始日级统计...");
        FinanceStatsEntity entity = new FinanceStatsEntity();
        entity.setCreateTime(new Date());
        Date date = DateUtils.preDayTime(entity.getCreateTime());
        entity.setThanDay(DateUtils.currentDayTime(date));
        entity.setThanMonth(DateUtils.currentMonth(date));
        entity.setThanYear(DateUtils.currentYear(date));
        entity.setType("DAY");

        Integer total = financeStatsService.sumRmbFromStats(date, DateUtils.currentDayTime(entity.getCreateTime()),"DAY");

        Integer increase = financeStatsService.sumRmbByTime(date, DateUtils.currentDayTime(entity.getCreateTime()));

        List<FinanceStatsEntity> financeStatsEntities = financeStatsService.RangeByCustom("DAY", DateUtils.preYesterday(new Date()), DateUtils.preYesterday(new Date()), "than_day");

        increase=increase==null?0:increase;
        entity.setTotal(total==null?0+increase:total+increase);
        entity.setIncrease(increase);

        if(financeStatsEntities.size()==0){
            entity.setCompairson(entity.getTotal());
        }else{
            entity.setCompairson(entity.getTotal()-financeStatsEntities.get(0).getTotal());
        }

        financeStatsService.save(entity);
        logger.info("完成日级统计...");
    }

    /**
     * 每月统计
     */
    public void month(){
        logger.info("开始月级统计...");
        FinanceStatsEntity entity = new FinanceStatsEntity();
        entity.setCreateTime(new Date());
        Date date = DateUtils.preMonthTime(entity.getCreateTime());
        entity.setThanDay(date);
        entity.setThanMonth(DateUtils.currentMonth(date));
        entity.setThanYear(DateUtils.currentYear(date));
        entity.setType("MONTH");

        Integer total = financeStatsService.sumRmbFromStats(date,  DateUtils.currentMonthTime(entity.getCreateTime()),"MONTH");

        Integer increase = financeStatsService.sumRmbByTime(date, DateUtils.currentMonthTime(entity.getCreateTime()));

        List<FinanceStatsEntity> financeStatsEntities = financeStatsService.RangeByCustom("MONTH",DateUtils.preTwoMonths(new Date()), DateUtils.preMonthTime(new Date()), "than_day");

        increase=increase==null?0:increase;
        entity.setTotal(total==null?0+increase:total+increase);
        entity.setIncrease(increase);

        if(financeStatsEntities.size()==0){
            entity.setCompairson(entity.getTotal());
        }else{
            entity.setCompairson(entity.getTotal()-financeStatsEntities.get(0).getTotal());
        }

        financeStatsService.save(entity);
        logger.info("完成月级统计...");
    }

    /**
     * 每年统计
     */
    public void year(){
        logger.info("开始年级统计...");
        FinanceStatsEntity entity = new FinanceStatsEntity();
        entity.setCreateTime(new Date());
        Date date = DateUtils.preYearTime(entity.getCreateTime());
        entity.setThanDay(date);
        entity.setThanYear(DateUtils.currentYear(date));
        entity.setType("YEAR");

        //统计昨天提现的金额
        Integer total = financeStatsService.sumRmbFromStats(date, DateUtils.currentYearTime(entity.getCreateTime()),"DAY");
        if(total == null){
            total = financeStatsService.sumRmbByTime(date, DateUtils.currentYearTime(entity.getCreateTime()));
        }
        entity.setTotal(total==null?0:total);

        financeStatsService.save(entity);
        logger.info("完成年级统计...");
    }
}
