package io.renren.common.utils;

import java.util.regex.Pattern;

/**
 * Created by ITMX on 2018/2/7.
 */
public class VersionCompare {

    /**
     * 版本比较
     * v1 > v2 ,return 1
     * v1 = v2 ,return 0
     * v1 < v2 ,return -1
     * @param v1
     * @param v2
     * @return
     */
    public static int compare(String v1,String v2){
        Pattern pattern = Pattern.compile("\\d+(\\.\\d)*");
        if(!pattern.matcher(v1).matches() || !pattern.matcher(v2).matches()){
            throw new IllegalArgumentException("版本号格式错误");
        }
        String[] s1 =v1.split("\\.");
        String[] s2 =v2.split("\\.");

        int length =Math.min(s1.length,s2.length);

        for(int i=0;i<length;i++){
            int diff =Integer.valueOf(s1[i])-Integer.valueOf(s2[i]);
            if(diff != 0){
                return diff>0 ? 1 : -1;
            }
        }
        if(s1.length == s2.length) {
            return 0;
        }
        return s1.length>s2.length?1:-1;
    }
}
