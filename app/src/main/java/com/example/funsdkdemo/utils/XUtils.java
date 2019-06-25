package com.example.funsdkdemo.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;

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

    /**
     * 0  TYPE_NO_PASSWORD,1,TYPE_WPA,2,TYPE_WEB
     *
     * @param capabilities
     * @return
     */
    public static final int getCapabilities(String capabilities) {
        if (!TextUtils.isEmpty(capabilities)) {
            if (capabilities.contains("WPA") || capabilities.contains("wpa")) {
                return 1;
            } else if (capabilities.contains("WEP") || capabilities.contains("wep")) {
                return 2;
            } else {
                return 0;
            }
        }
        return 0;
    }

    /**
     * @param capabilities
     * @return
     */
    public static int getEncrypPasswordType(String capabilities) {
        if (capabilities.contains("WPA2") && capabilities.contains("CCMP")) {
            // sEncrypType = "AES";
            // sAuth = "WPA2";
            return 1;
        } else if (capabilities.contains("WPA2") && capabilities.contains("TKIP")) {
            // sEncrypType = "TKIP";
            // sAuth = "WPA2";
            return 2;
        } else if (capabilities.contains("WPA") && capabilities.contains("TKIP")) {
            // EncrypType = "TKIP";
            // sAuth = "WPA";
            return 2;
        } else if (capabilities.contains("WPA") && capabilities.contains("CCMP")) {
            // sEncrypType = "AES";
            // sAuth = "WPA";
            return 1;
        } else if (capabilities.contains("WEP")) {
            return 3;
        } else {
            // sEncrypType = "NONE";
            // sAuth = "OPEN";
            return 0;
        }
    }

    /**
     * Ascii码值转成字符串
     *
     * @param value
     * @return
     */
    public static String asciiToString(String value) {
        StringBuffer sbu = new StringBuffer();
        int pos = 0;
        int length = value.length();
        String str = null;
        do {
            str = value.substring(pos, pos + 2);
            sbu.append((char) Integer.parseInt(str, 16));
            pos += 2;
        } while (pos <= (length - 2));
        return sbu.toString();
    }

    /**
     * 获取版本号
     *
     * @return 当前应用的包名
     */
    public static String getPackageName(Context context) {
        if (context == null)
            return null;
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            String version = info.packageName;
            return version;
        } catch (Exception e) {
            e.printStackTrace();
            return "Unkown";
        }
    }
}
