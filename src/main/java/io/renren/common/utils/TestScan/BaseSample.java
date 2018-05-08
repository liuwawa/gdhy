package io.renren.common.utils.TestScan;

/**
 * Created by liuhai.lh on 17/01/12.
 */
public class BaseSample {

    protected static String accessKeyId = "LTAIwczpzUMC29Sp";
    protected static String accessKeySecret = "PHl0v8HT0mZ9uQ0pz6UU9AIQTm1gVX";

    protected static String regionId = "cn-shanghai";

//    static {
//        Properties properties = new Properties();
//
//        try {
//            properties.load(BaseSample.class.getResourceAsStream("config.properties"));
//            accessKeyId = properties.getProperty("accessKeyId");
//            accessKeySecret = properties.getProperty("accessKeySecret");
//            regionId = properties.getProperty("regionId");
//        } catch(IOException e) {
//            e.printStackTrace();
//        }
//
//    }


    protected static String getDomain(){
         if("cn-shanghai".equals(regionId)){
             return "green.cn-shanghai.aliyuncs.com";
         }

         if("cn-hangzhou".equals(regionId)){
             return "green.cn-hangzhou.aliyuncs.com";
         }
        
         if("local".equals(regionId)){
             return "api.green.alibaba.com";
         }

        return "green.cn-shanghai.aliyuncs.com";
    }

    protected static String getEndPointName(){
        return regionId;
    }

}
