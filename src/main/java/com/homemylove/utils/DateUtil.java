package com.homemylove.utils;


import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
    public static String formatDateTime(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }

    public static enum TimeUnits{
        SECOND,MINUTE,HOUR;
    }

    /**
     * 判断两个时间是不是小于给定值
     * @param d1
     * @param d2
     * @param diff
     * @param timeUnits
     * @return
     */
    public static boolean lessThan(Date d1,Date d2,Long diff,TimeUnits timeUnits){
        // 计算时间差（以毫秒为单位）
        long timeDifference = d1.getTime() - d2.getTime();
        // 将时间差转换为分钟
        if(timeUnits.equals(TimeUnits.SECOND)){
            return timeDifference /  1000 < diff;
        }else if(timeUnits.equals(TimeUnits.MINUTE)){
            return timeDifference / (60 * 1000) < diff;
        }else {
            return timeDifference / (60 * 60 * 1000) < diff;
        }
    }

}

