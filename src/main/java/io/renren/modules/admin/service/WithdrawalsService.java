package io.renren.modules.admin.service;

import io.renren.modules.admin.entity.WithdrawalsEntity;
import io.renren.modules.api.entity.UserEntity;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 提现记录
 * 
 * @author itmx
 * @email itmx@foxmail.com
 * @date 2018-01-08 09:09:41
 */
public interface WithdrawalsService {
	
	WithdrawalsEntity queryObject(String id);
	
	List<WithdrawalsEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);

    Integer createNewEntity(Integer rmb, String channel, UserEntity user);

    List<WithdrawalsEntity> pageByUser(Long userId, Integer status, Integer page, Integer limit);

    void useOfAlipay(WithdrawalsEntity entity);

    Integer finish(String pay_no, String id, Date finishTime);

    void useOfWeixinPay(WithdrawalsEntity entity);

    void close(String oid, String closeMsg);

    Integer sumByUser(Long userId,String time);

    /**
     * 根据用户id和体现成功的状态去查询用户是否体现过记录
     * @param userId
     * @param status
     * @return
     */
    Integer queryCountByUserIdAndStatus(Long userId, int status);
}
