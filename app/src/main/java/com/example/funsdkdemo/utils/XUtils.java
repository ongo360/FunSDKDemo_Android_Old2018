package com.example.funsdkdemo.utils;

import com.lib.sdk.bean.DayLightTimeBean;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * @author Administrator
 * @name FunSDKDemo_Android_Old2018
 * @class name：com.example.funsdkdemo.utils
 * @class describe
 * @time 2019-03-04 14:12
 * @change
 * @chang time
 * @class describe
 */
public class XUtils {
    public static DayLightTimeBean getDayLightTimeInfo(TimeZone tz) {
        DayLightTimeBean dltInfo = new DayLightTimeBean();
        dltInfo.useDLT = tz.useDaylightTime();
        if (dltInfo.useDLT) {
            try {
                Date date = new Date();
                SimpleDateFormat sdfYear = new SimpleDateFormat("yyyy");
                sdfYear.setTimeZone(tz);

                SimpleDateFormat sdfDay = new SimpleDateFormat("yyyy-MM-dd");
                sdfDay.setTimeZone(tz);

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                sdf.setTimeZone(tz);

                // 年份就取当年
                dltInfo.year = Integer.parseInt(sdfYear.format(date));

                // 先找到起始月份
                for ( int month = 1; month <= 12; month ++ ) {
                    String tmpStr = String.format("%04d-%02d-%02d", dltInfo.year, month, 1);

                    if ( dltInfo.beginMonth == 0 && tz.inDaylightTime(sdfDay.parse(tmpStr)) ) {
                        dltInfo.beginMonth = month;
                    }

                    if ( dltInfo.beginMonth > 0 && dltInfo.endMonth == 0 && !tz.inDaylightTime(sdfDay.parse(tmpStr)) ) {
                        // 这个只是可能的结束月份,实际的结束月份很可能是上个月
                        dltInfo.endMonth = month;
                    }

                    if (dltInfo.beginMonth > 0 && dltInfo.endMonth > 0 && tz.inDaylightTime(sdfDay.parse(tmpStr))) {
                        dltInfo.beginMonth = month;
                        break;
                    }
                }

                // 起始天肯定在起始月份当中的某一天,简单查找,就认为每个月都有31天吧
                if (dltInfo.beginMonth > 1) {
                    dltInfo.beginDay = 1;
                    for (int day = 1; day <= 31; day++) {
                        String tmpStr = String.format("%04d-%02d-%02d", dltInfo.year, dltInfo.beginMonth - 1, day);
                        if (tz.inDaylightTime(sdfDay.parse(tmpStr))) {
                            dltInfo.beginDay = day;
                            dltInfo.beginMonth = dltInfo.beginMonth - 1;
                            break;
                        }
                    }
                }

                // 结束的天,应该是上述结束月份中的1号,或者是之前一个月的某一天
                if ( dltInfo.endMonth > 1 ) {
                    dltInfo.endDay = 1;
                    // 上个月中查找是否存在结束日期,如果不存在就是之前找到的月份的1号,简单查找,就认为每个月都有31天吧
                    for ( int day = 1; day <= 31; day ++ ) {
                        int year = dltInfo.beginMonth > dltInfo.endMonth ? dltInfo.year + 1 : dltInfo.year;
                        String tmpStr = String.format("%04d-%02d-%02d", year, dltInfo.endMonth - 1, day);
                        if ( !tz.inDaylightTime(sdfDay.parse(tmpStr)) ) {
                            dltInfo.endMonth = dltInfo.endMonth - 1;
                            dltInfo.endDay = day;
                            break;
                        }
                    }
                }

                // 时/分暂不处理,暂定00:00

            } catch (Exception e) {

            }
        }
        return dltInfo;
    }
}
