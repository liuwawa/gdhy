package io.renren.modules.stats.dao;

import io.renren.modules.stats.entity.WithdrawalsStatsEntity;
import io.renren.modules.sys.dao.BaseDao;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * 提现统计
 * 
 * @author itmx
 * @email itmx@foxmail.com
 * @date 2018-02-05 11:20:29
 */
@Mapper
public interface WithdrawalsStatsDao extends BaseDao<WithdrawalsStatsEntity> {

    Long sumGoldByTime(@Param("a") Date a, @Param("b")Date b);

    Long sumGoldFromStats(@Param("a")Date a, @Param("b")Date b, @Param("type")String type);

    List<WithdrawalsStatsEntity> RangeByHour(@Param("type") String type,@Param("a") String a,@Param("b") String b
            , @Param("hours") int hours,@Param("hours1") int hours1);

    List<WithdrawalsStatsEntity> RangeByCustom(@Param("type") String type,@Param("a") String a,@Param("b") String b,@Param("field") String field);

    Long sumOrderStatsByStatus(@Param("a")Date date, @Param("b")Date date1,@Param("status") int o);
}
