package io.renren.modules.invite.dao;

import io.renren.modules.invite.entity.InviteCodeEntity;
import io.renren.modules.sys.dao.BaseDao;
import org.apache.ibatis.annotations.Mapper;

/**
 * 邀请码
 * 
 * @author itmx
 * @email itmx@foxmail.com
 * @date 2018-03-18 16:28:29
 */
@Mapper
public interface InviteCodeDao extends BaseDao<InviteCodeEntity> {

    void abandonBatch(Integer[] inviteCodes);

    void disableBatch(Integer[] inviteCodes);

    void enableBatch(Integer[] inviteCodes);

    InviteCodeEntity queryByUser(Long userId);
}
