package io.renren.modules.invite.dao;

import io.renren.modules.invite.entity.InviteRewardRecordEntity;
import io.renren.modules.sys.dao.BaseDao;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 发放记录
 * 
 * @author itmx
 * @email itmx@foxmail.com
 * @date 2018-03-20 10:14:28
 */
@Mapper
public interface InviteRewardRecordDao extends BaseDao<InviteRewardRecordEntity> {
    InviteRewardRecordEntity queryOne(@Param("master") Long master, @Param("apprentice") Long apprentice);

    List<InviteRewardRecordEntity> pageByMaster(@Param("master") Long master,@Param("a") Integer a,@Param("b") Integer b);
}
