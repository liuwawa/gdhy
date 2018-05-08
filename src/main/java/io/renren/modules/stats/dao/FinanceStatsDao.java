package io.renren.modules.stats.dao;

import io.renren.modules.stats.entity.FinanceStatsEntity;
import io.renren.modules.sys.dao.BaseDao;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * 财务支出情况
 * 
 * @author itmx
 * @email itmx@foxmail.com
 * @date 2018-02-05 11:20:29
 */
@Mapper
public interface FinanceStatsDao extends BaseDao<FinanceStatsEntity> {

    Integer sumRmbByTime(@Param("a") Date a, @Param("b")Date b);

    Integer sumRmbFromStats(@Param("a") Date a, @Param("b") Date b,@Param("type") String type);

    List<FinanceStatsEntity> RangeByCustom(@Param("type") String type,@Param("a") String a,@Param("b") String b,@Param("field") String field);
    List<FinanceStatsEntity> RangeByCustom(@Param("type") String type,@Param("a") Date a,@Param("b") Date b,@Param("field") String field);

    List<FinanceStatsEntity> RangeByHour(@Param("type") String type,@Param("a") String a,@Param("b") String b
            , @Param("hours") int hours,@Param("hours1") int hours1);
}
