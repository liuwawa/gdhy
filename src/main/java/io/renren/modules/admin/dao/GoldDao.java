package io.renren.modules.admin.dao;

import io.renren.modules.admin.entity.GoldEntity;
import io.renren.modules.sys.dao.BaseDao;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * 用户金币
 * 
 * @author itmx
 * @email itmx@foxmail.com
 * @date 2017-12-24 17:59:05
 */
@Mapper
public interface GoldDao extends BaseDao<GoldEntity> {

    int resetToday();

    void modifySurplusGold(Map<String, Object> map);

    /**
     *
     * 根据用户id去更新今日阅读金币
     * @param userId
     * @param reward
     */
    void updateTodayReadByUserId(@Param("userId") Long userId, @Param("reward") Integer reward);

    /**
     *
     * 根据用户id去更新今日视频金币
     * @param userId
     * @param reward
     */
    void updateTodayVedioByUserId(@Param("userId") Long userId, @Param("reward") Integer reward);
}
