package io.renren.modules.stats.dao;

import io.renren.modules.stats.entity.GoldStatsEntity;
import io.renren.modules.sys.dao.BaseDao;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * 金币领取情况
 * 
 * @author itmx
 * @email itmx@foxmail.com
 * @date 2018-02-05 11:20:29
 */
@Mapper
public interface GoldStatsDao extends BaseDao<GoldStatsEntity> {

    Integer sumGoldByTime(@Param("a") Date a, @Param("b") Date b);

    GoldStatsEntity preHour();

    Integer sumGoldFromStats(@Param("a") Date a, @Param("b") Date b,@Param("type") String type);

    List<GoldStatsEntity> queryByRange(@Param("type") String type,@Param("a") Date a, @Param("b") Date b);

    List<GoldStatsEntity> RangeByCustom(@Param("type") String type,@Param("a") String a,@Param("b") String b,@Param("field") String field);

    List<GoldStatsEntity> RangeByHour(@Param("type") String type,@Param("a") String a,@Param("b") String b
            , @Param("hours") int hours,@Param("hours1") int hours1);
}
