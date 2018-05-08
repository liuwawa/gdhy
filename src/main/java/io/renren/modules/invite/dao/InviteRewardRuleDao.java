package io.renren.modules.invite.dao;

import io.renren.modules.invite.entity.InviteRewardRuleEntity;
import io.renren.modules.sys.dao.BaseDao;
import org.apache.ibatis.annotations.Mapper;

/**
 * 奖励规则
 * 
 * @author itmx
 * @email itmx@foxmail.com
 * @date 2018-03-19 14:27:31
 */
@Mapper
public interface InviteRewardRuleDao extends BaseDao<InviteRewardRuleEntity> {

    InviteRewardRuleEntity getCurrentRule();
}
