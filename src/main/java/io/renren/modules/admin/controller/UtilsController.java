package io.renren.modules.admin.controller;

import io.renren.common.utils.DeviceInfoUtil;
import io.renren.common.utils.IPUtils;
import io.renren.common.utils.R;
import io.renren.modules.api.dao.DeviceInfoDao;
import io.renren.modules.api.entity.DeviceInfo;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * 一些工具
 * Created by ITMX on 2018/3/12.
 */
@RestController
@RequestMapping("/admin/utils")
public class UtilsController {

    @Autowired
    private DeviceInfoDao deviceInfoDao;

    /**
     * 根据IP获取归属地址
     * @param ip
     * @return
     */
    @RequestMapping("/ipToAddress")
    public R ipToAddress(String ip){
//        JSONObject jsonObject = IPUtils.ipToAddress(ip);
        JSONObject jsonObject = IPUtils.ipToAddressByTaoBao(ip);
        if(jsonObject==null){
            return R.error();
        }
        //type为2用淘宝
        return R.ok().put("data",jsonObject).put("type",2);
    }


    /**
     * 获取用户的设备信息
     * @param userId
     * @return
     */
    @RequestMapping("/getDeviceInfo/{userId}")
    public R getDeviceInfo(@PathVariable Long userId){
        DeviceInfo deviceInfo = deviceInfoDao.queryObject(userId);
        String deviceInfo1=null;
        if(deviceInfo!=null){
            if(deviceInfo.getDeviceInfo()!=null){
                //获得设备信息字段的值
                deviceInfo1 = deviceInfo.getDeviceInfo();
            }
        }

        Map<String,Object> map =null;
        if(deviceInfo1!=null){
           map = DeviceInfoUtil.analysisDeviceInfo(deviceInfo1);
           if(map==null){
                return R.error();
           }
        }
        //将map集合存入
        return R.ok().put("devInfo",deviceInfo).put("mapInfo",map);
    }

    @RequestMapping("/getDeviceInfoByUserId")
    public R getDeviceInfoByUserId(Long userId){
        List<DeviceInfo> deviceInfos = deviceInfoDao.queryListByUserId(userId);

        ArrayList<Map<String,Object>> datas=new ArrayList<>();
        for (DeviceInfo info:deviceInfos){
            String deviceInfo1=null;
            if(deviceInfos!=null){
                if(info.getDeviceInfo()!=null){
                    //获得设备信息字段的值
                    deviceInfo1 = info.getDeviceInfo();
                }
            }

            Map<String,Object> map =null;
            if(deviceInfo1!=null){
                map = DeviceInfoUtil.analysisDeviceInfo(deviceInfo1);
                if(map==null){
                    return R.error();
                }
            }

            List<Long> longs = deviceInfoDao.queryListByImei(info.getImei(),userId);
            map.put("Multiuser",longs);
            map.put("createTime",info.getCreateTime());
            datas.add(map);
        }
        //将map集合存入
        return R.ok().put("devInfo",deviceInfos).put("mapInfo",datas);
    }

}
