package io.renren.modules.admin.dao;

import io.renren.modules.admin.entity.WithdrawalsEntity;
import io.renren.modules.sys.dao.BaseDao;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

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
@Mapper
public interface WithdrawalsDao extends BaseDao<WithdrawalsEntity> {

    void make(Map<String, Object> map);

    List<WithdrawalsEntity> pageByUser(@Param("userId") Long userId,@Param("status") Integer status, @Param("a") Integer a, @Param("b") Integer b);

    Integer finish(@Param("id") String id,@Param("payNo") String payNo,@Param("finishTime") Date finishTime);

    void close(Map<String, Object> map);

    Integer sumByUser(@Param("userId") Long userId,@Param("time") String time);
    /**
     * 根据用户id和体现成功的状态去查询用户是否体现过记录
     * @param userId
     * @param status
     * @return
     */
    Integer queryCountByUserIdAndStatus(@Param("userId") Long userId,@Param("status") int status);
}
