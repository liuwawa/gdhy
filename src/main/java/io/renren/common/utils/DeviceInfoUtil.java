package io.renren.common.utils;

import org.apache.commons.codec.binary.Base64;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Administrator on 2018/4/19 0019.
 */
public class DeviceInfoUtil {
    public static Map<String, Object> analysisDeviceInfo(String deviceInfo1) {
        Map<String,Object> map=null;
        if(deviceInfo1!=null){
            //解码
            byte[] bytes = Base64.decodeBase64(deviceInfo1.getBytes());
            //将字节转成数组
            String result = new String(bytes);
            //emulator=false&Root=false&Phone number="" &IMSI=null&DeviceID(IMEI)=866334034498408
            String[] split = result.split("&");

            map= new LinkedHashMap<String,Object>();
            String[] ss = new String[2];
            for(String s:split){
                //emulator=false Root=false
                ss = s.split("=");
                //如果切开只有一个值的话，就让该值为key ,value 为空字符串
                if(ss.length==1){
                    map.put(ss[0]," ");
                }
                //切开为两个值的话，第一个值为key,第二值为value
                if(ss.length==2){
                    map.put(ss[0],ss[1]);
                    //如果value中有Null值的时候，替换为空字符串
                    if(ss[1].trim().equals("null")){
                        map.put(ss[0]," ");
                    }

                }
            }

        }
        return map;
    }
}
