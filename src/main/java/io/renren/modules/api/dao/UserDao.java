package io.renren.modules.api.dao;

import io.renren.modules.api.entity.UserEntity;
import io.renren.modules.sys.dao.BaseDao;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户
 * 
 * @author itmx
 * @email itmx@foxmail.com
 * @date 2017-03-23 15:22:06
 */
@Mapper
public interface UserDao extends BaseDao<UserEntity> {

    UserEntity queryByPhone(String phone);
    UserEntity queryByWeChat(String openid);
    UserEntity queryByAlipay(String userId);
}
