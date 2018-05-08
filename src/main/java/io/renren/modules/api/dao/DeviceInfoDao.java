package io.renren.modules.api.dao;

import io.renren.modules.api.entity.DeviceInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;

/**
 * Created by ITMX on 2018/3/13.
 */
@Mapper
public interface DeviceInfoDao {
    int save(DeviceInfo deviceInfo);

    DeviceInfo queryObject(Long userId);

    /**
     *根据用户id和md5查询是否有该用户的设备信息
     * @param deviceInfo
     */
    Integer queryCountByUserIdAndMd5(DeviceInfo deviceInfo);

    List<DeviceInfo> queryListByUserId(Long userId);

    List<Long> queryListByImei(@Param("imei") String imei,@Param("userId") Long userId);
}
