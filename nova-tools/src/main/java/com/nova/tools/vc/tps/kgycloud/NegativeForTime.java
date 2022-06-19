package com.nova.tools.vc.tps.kgycloud;



import com.nova.tools.utils.time.DateUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author wangzehui
 * @date 2018/10/9 9:41
 */

public class NegativeForTime {
    /**
     * 开始时间
     */
    public static final String FeatureTime = "21:20";
    /**
     * 结束时间
     */
    public static final String TotalTime = "23:00";

    public static void main(String[] args) {
        String startTime = "";
        String endTime = "";

        String date = "2018-10-31";

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");

        SimpleDateFormat simpleDateFormatNew = new SimpleDateFormat("yyyy-MM-dd");

        try {
            Date parseDate = simpleDateFormatNew.parse(date);
            Date parseFeatureTime = simpleDateFormat.parse(FeatureTime);
            Date parseTotalTime = simpleDateFormat.parse(TotalTime);
            //Date.after  当前大于后返回true
            boolean after = parseFeatureTime.after(parseTotalTime);
            startTime = String.format("%s %s:00", date, FeatureTime);
            if (after == true) {
                date = simpleDateFormatNew.format(DateUtils.addOneDay(parseDate));

                endTime = String.format("%s %s:00", date, TotalTime);

            } else {
                endTime = String.format("%s %s:00", date, TotalTime);
            }

            int i = DateUtils.minuteBetween(startTime, endTime);
            System.out.println(i);

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
