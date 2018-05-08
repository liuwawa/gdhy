package io.renren.modules.invite.dao;

import io.renren.modules.invite.entity.InviteRecordEntity;
import io.renren.modules.sys.dao.BaseDao;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 收徒记录
 * 
 * @author itmx
 * @email itmx@foxmail.com
 * @date 2018-03-19 21:46:21
 */
@Mapper
public interface InviteRecordDao extends BaseDao<InviteRecordEntity> {

    InviteRecordEntity queryOne(@Param("master") Long master,@Param("apprentice") Long apprentice);

    List<InviteRecordEntity> pageByMaster(@Param("master") Long master, @Param("a") Integer a, @Param("b") Integer b ,@Param("time") String time);

    Integer totalProfit(@Param("master") Long master);

    InviteRecordEntity queryByApprentice(@Param("apprentice") Long apprentice);

    int SummaryInvitationsNum(@Param("startTime")String startTime,@Param("endTime") String endTime);
}
