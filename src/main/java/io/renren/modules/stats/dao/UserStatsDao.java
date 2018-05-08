package io.renren.modules.stats.dao;

import io.renren.modules.stats.entity.UserStatsEntity;
import io.renren.modules.sys.dao.BaseDao;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * 用户增长情况
 * 
 * @author itmx
 * @email itmx@foxmail.com
 * @date 2018-02-05 11:20:29
 */
@Mapper
public interface UserStatsDao extends BaseDao<UserStatsEntity> {

    UserStatsEntity preHour();

    int countAll();

    Integer countUserByTime(@Param("a") Date a, @Param("b") Date b);

    List<UserStatsEntity> queryByRange(@Param("type")String type,@Param("a") Date a,@Param("b") Date b);

    List<UserStatsEntity> RangeByHour(@Param("type") String type,@Param("a") String a,@Param("b") String b
            , @Param("hours") int hours,@Param("hours1") int hours1);

    List<UserStatsEntity> RangeByCustom(@Param("type") String type,@Param("a") String a,@Param("b") String b,@Param("field") String field);
    List<UserStatsEntity> RangeByCustom(@Param("type") String type,@Param("a") Date a,@Param("b") Date b,@Param("field") String field);

}
