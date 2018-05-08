package io.renren.modules.admin.dao;

import io.renren.modules.admin.entity.EverydaySignEntity;
import io.renren.modules.api.entity.SignRecord;
import io.renren.modules.sys.dao.BaseDao;
import org.apache.ibatis.annotations.Mapper;

/**
 * 签到奖励
 * 
 * @author itmx
 * @email itmx@foxmail.com
 * @date 2018-01-04 10:36:14
 */
@Mapper
public interface EverydaySignDao extends BaseDao<EverydaySignEntity> {

    void createNewEntity(Long userId);

    SignRecord querySignRecord(Long userId);

    /**
     * 签到
     * @param userId
     */
    void doSign(Long userId);

}
