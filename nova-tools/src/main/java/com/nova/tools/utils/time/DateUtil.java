package com.nova.tools.utils.time;

import com.alibaba.fastjson.JSONObject;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class DateUtil {

    /**
     * 获取传入日期一天的开始时间、结束时间
     *
     * @param curr 当前时间
     * @param day  获取当天 day 传0
     *             <p>
     *             返回计算那天的00 和 23:59:59秒
     */
    public static List<Date> getDayStartAndEnd(Date curr, int day) {
        List<Date> dates = new ArrayList<>();
        //第一个
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(curr);
        calendar.add(Calendar.DAY_OF_MONTH, day);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        dates.add(calendar.getTime());
        //第二个
        calendar = Calendar.getInstance();
        calendar.setTime(curr);
        calendar.add(Calendar.DAY_OF_MONTH, day);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        dates.add(calendar.getTime());
        return dates;
    }


    /**
     * 定义常量
     **/
    public static final String DATE_FULL_STR = "yyyy-MM-dd HH:mm:ss";

    /**
     * 定义常量
     **/
    public static final String DATE_STR = "yyyy-MM-dd";

    /**
     * 使用预设格式提取字符串日期
     *
     * @param strDate 日期字符串
     * @return
     */

    public static Date parse(String strDate) {
        return parse(strDate, DATE_FULL_STR);
    }


    /**
     * 使用用户格式提取字符串日期
     *
     * @param strDate 日期字符串
     * @param pattern 日期格式
     * @return
     */


    public static Date parse(String strDate, String pattern) {
        SimpleDateFormat df = new SimpleDateFormat(pattern);
        try {
            return df.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }


    public static String parse(Date date, String pattern) {
        SimpleDateFormat df = new SimpleDateFormat(pattern);
        try {
            return df.format(date);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public static String parse(Date date) {
        SimpleDateFormat df = new SimpleDateFormat(DATE_FULL_STR);
        try {
            return df.format(date);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 两个时间比较
     *
     * @return
     */
    public static int compareDateWithNow(Date date1) {
        Date date2 = new Date();
        int rnum = date1.compareTo(date2);
        return rnum;
    }

    /**
     * 给时间加上几个小时
     *
     * @param hour 需要加的时间
     * @return
     */
    public static Date addDateMinut(Date date, int hour) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.HOUR, hour);
        date = cal.getTime();
        return date;
    }

    /**
     * 获取系统当前时间
     *
     * @return
     */
    public static String getNowTime() {
        SimpleDateFormat df = new SimpleDateFormat(DATE_FULL_STR);
        return df.format(new Date());
    }

    /**
     * 获取系统当前时间
     *
     * @return
     */
    public static String getNowTime(String type) {
        SimpleDateFormat df = new SimpleDateFormat(type);
        return df.format(new Date());
    }


    /**
     * 判断某一时间是否在一个区间内
     *
     * @param sourceTime 时间区间,半闭合,如[10:00-20:00)
     * @param curTime    需要判断的时间 如10:00
     * @return
     * @throws IllegalArgumentException
     */
    public static boolean isInTime(String sourceTime, String curTime) {
        if (sourceTime == null || !sourceTime.contains("-") || !sourceTime.contains(":")) {
            throw new IllegalArgumentException("Illegal Argument arg:" + sourceTime);
        }
        if (curTime == null || !curTime.contains(":")) {
            throw new IllegalArgumentException("Illegal Argument arg:" + curTime);
        }
        String[] args = sourceTime.split("-");
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        if (args.length > 0) {
            try {
                long now = sdf.parse(curTime).getTime();
                long start = sdf.parse(args[0]).getTime();
                long end = sdf.parse(args[1]).getTime();
                if (args[1].equals("00:00")) {
                    args[1] = "24:00";
                }
                if (end < start) {
                    if (now >= end && now < start) {
                        return false;
                    } else {
                        return true;
                    }
                } else {
                    if (now >= start && now < end) {
                        return true;
                    } else {
                        return false;
                    }
                }
            } catch (ParseException e) {
                e.printStackTrace();
                throw new IllegalArgumentException("Illegal Argument arg:" + sourceTime);
            }
        } else {
            return false;
        }
    }

    /**
     * 判断某一时间是否在一个区间内
     *
     * @param sourceTime 时间区间,半闭合,如[10:00-20:00)
     * @param curTime    需要判断的时间 如10:00
     * @return
     * @throws IllegalArgumentException
     */
    public static boolean isInTimeContain(String sourceTime, String curTime) {
        if (sourceTime == null || !sourceTime.contains("-") || !sourceTime.contains(":")) {
            throw new IllegalArgumentException("Illegal Argument arg:" + sourceTime);
        }
        if (curTime == null || !curTime.contains(":")) {
            throw new IllegalArgumentException("Illegal Argument arg:" + curTime);
        }
        String[] args = sourceTime.split("-");
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        if (args.length > 0) {
            try {
                long now = sdf.parse(curTime).getTime();
                long start = sdf.parse(args[0]).getTime();
                long end = sdf.parse(args[1]).getTime();
                if ("00:00".equals(args[1])) {
                    args[1] = "24:00";
                }
                if (end < start) {
                    return now < end || now > start;
                } else {
                    return now >= start && now <= end;
                }
            } catch (ParseException e) {
                e.printStackTrace();
                throw new IllegalArgumentException("Illegal Argument arg:" + sourceTime);
            }
        } else {
            return false;
        }
    }

    /**
     * @param startDate ("2013-11-29");
     * @param endDate   ( "2013-12-5");
     * @return
     * @throws Exception
     */
    public static String[] getBetweenDateStr(String startDate, String endDate)
            throws Exception {

        GregorianCalendar[] resultGC = getBetweenDate(startDate, endDate);
        String[] gcStr = new String[resultGC.length];
        int i = 0;
        for (GregorianCalendar gc : resultGC) {
            Date date = gc.getTime();
            gcStr[i] = formatDate2Str(date);
            i++;
        }
        return gcStr;
    }

    public static String formatDate2Str(Date date) throws Exception {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(date);
    }

    private static GregorianCalendar[] getBetweenDate(String startDate, String endDate) throws Exception {
        Vector<GregorianCalendar> dateVector = new Vector<GregorianCalendar>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        GregorianCalendar startGC = new GregorianCalendar();
        GregorianCalendar endGC = new GregorianCalendar();

        startGC.setTime(sdf.parse(startDate));
        endGC.setTime(sdf.parse(endDate));
        do {
            GregorianCalendar tempGC = (GregorianCalendar) startGC.clone();
            dateVector.add(tempGC);
            startGC.add(Calendar.DAY_OF_MONTH, 1);
        } while (!startGC.after(endGC));

        return dateVector.toArray(new GregorianCalendar[dateVector.size()]);
    }

    /**
     * 获取日期期间内的所有日期
     *
     * @param Begin
     * @param End
     * @return
     */
    public static List<Date> findDates(String Begin, String End) {
        List lDate = new ArrayList();
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date dBegin = sdf.parse(Begin);
            Date dEnd = sdf.parse(End);
            lDate.add(dBegin);
            Calendar calBegin = Calendar.getInstance();
            // 使用给定的 Date 设置此 Calendar 的时间
            calBegin.setTime(dBegin);
            Calendar calEnd = Calendar.getInstance();
            // 使用给定的 Date 设置此 Calendar 的时间
            calEnd.setTime(dEnd);
            // 测试此日期是否在指定日期之后
            while (dEnd.after(calBegin.getTime())) {
                // 根据日历的规则，为给定的日历字段添加或减去指定的时间量
                calBegin.add(Calendar.DAY_OF_MONTH, 1);
                lDate.add(calBegin.getTime());
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return lDate;
    }


    /**
     * @param max 总和
     * @param min
     * @param cnt
     * @return
     */
    public static List<BigDecimal> createSumGold(double max, double min, int cnt) {
        List<BigDecimal> goldList = new ArrayList<>();
        int scl = 2; // 小数最大位数
        int pow = (int) Math.pow(10, scl); // 用于提取指定小数位
        double sum = 0; // 用于验证总和
        double one;

        for (int i = 0; i < cnt; i++) {
            if (i < cnt) {
                // min~max 指定小数位的随机数
                one = Math.floor((Math.random() * (max - min) + min) * pow) / pow;
                System.out.println(one);
            } else {
                one = max;
            }
            max -= one;
            sum += one;
        }
        return goldList;
    }



    /**
     * 获取当前日期是星期几<br>
     *
     * @param dt
     * @return 当前日期是星期几
     */
    public static String getWeekOfDate(Date dt) {
        String[] weekDays = {"周日", "周一", "周二", "周三", "周四", "周五", "周六"};
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0) {
            w = 0;
        }
        return weekDays[w];
    }

    /**
     * 获取两个日期相差的天数
     *
     * @param date      日期字符串
     * @param otherDate 另一个日期字符串
     * @return 相差天数
     */
    public static int getIntervalDays(String date, String otherDate) {
        return getIntervalDays(StringToDate(date), StringToDate(otherDate));
    }

    /**
     * @param date
     * @param otherDate 另一个日期
     * @return 相差天数
     */
    public static int getIntervalDays(Date date, Date otherDate) {
        date = DateUtil.StringToDate(DateUtil.getDate(date));
        long time = Math.abs(date.getTime() - otherDate.getTime());
        return (int) time / (24 * 60 * 60 * 1000);
    }

    /**
     * 获取日期。默认yyyy-MM-dd格式。失败返回null。
     *
     * @param date
     * @return
     */
    public static String getDate(Date date) {
        return DateToString(date, "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 将日期转化为日期字符串。失败返回null。
     *
     * @param date
     * @param dateStyle 日期风格
     * @return 日期字符串
     */
    public static String DateToString(Date date, DateStyle dateStyle) {
        String dateString = null;
        if (dateStyle != null) {
            dateString = DateToString(date, dateStyle.getValue());
        }
        return dateString;
    }

    /**
     * 将日期转化为日期字符串。失败返回null。
     *
     * @param date
     * @param parttern 日期格式
     * @return 日期字符串
     */
    public static String DateToString(Date date, String parttern) {
        String dateString = null;
        if (date != null) {
            try {
                dateString = getDateFormat(parttern).format(date);
            } catch (Exception e) {
            }
        }
        return dateString;
    }

    /**
     * 将日期字符串转化为日期。失败返回null。
     *
     * @param date 日期字符串
     * @return
     */
    public static Date StringToDate(String date) {
        DateStyle dateStyle = null;
        return StringToDate(date, dateStyle);
    }

    /**
     * 将日期字符串转化为日期。失败返回null。
     *
     * @param date      日期字符串
     * @param dateStyle 日期风格
     * @return
     */
    public static Date StringToDate(String date, DateStyle dateStyle) {
        Date myDate = null;
        if (dateStyle == null) {
            List<Long> timestamps = new ArrayList<Long>();
            for (DateStyle style : DateStyle.values()) {
                Date dateTmp = StringToDate(date, style.getValue());
                if (dateTmp != null) {
                    timestamps.add(dateTmp.getTime());
                }
            }
            myDate = getAccurateDate(timestamps);
        } else {
            myDate = StringToDate(date, dateStyle.getValue());
        }
        return myDate;
    }


    /**
     * 获取精确的日期
     *
     * @param timestamps 时间long集合
     * @return
     */
    private static Date getAccurateDate(List<Long> timestamps) {
        Date date = null;
        long timestamp = 0;
        Map<Long, long[]> map = new HashMap<>(16);
        List<Long> absoluteValues = new ArrayList<Long>();

        if (timestamps != null && timestamps.size() > 0) {
            if (timestamps.size() > 1) {
                for (int i = 0; i < timestamps.size(); i++) {
                    for (int j = i + 1; j < timestamps.size(); j++) {
                        long absoluteValue = Math.abs(timestamps.get(i) - timestamps.get(j));
                        absoluteValues.add(absoluteValue);
                        long[] timestampTmp = {timestamps.get(i), timestamps.get(j)};
                        map.put(absoluteValue, timestampTmp);
                    }
                }

                // 有可能有相等的情况。如2012-11和2012-11-01。时间戳是相等的
                long minAbsoluteValue = -1;
                if (!absoluteValues.isEmpty()) {
                    // 如果timestamps的size为2，这是差值只有一个，因此要给默认值
                    minAbsoluteValue = absoluteValues.get(0);
                }
                for (int i = 0; i < absoluteValues.size(); i++) {
                    for (int j = i + 1; j < absoluteValues.size(); j++) {
                        if (absoluteValues.get(i) > absoluteValues.get(j)) {
                            minAbsoluteValue = absoluteValues.get(j);
                        } else {
                            minAbsoluteValue = absoluteValues.get(i);
                        }
                    }
                }

                if (minAbsoluteValue != -1) {
                    long[] timestampsLastTmp = map.get(minAbsoluteValue);
                    if (absoluteValues.size() > 1) {
                        timestamp = Math.max(timestampsLastTmp[0], timestampsLastTmp[1]);
                    } else if (absoluteValues.size() == 1) {
                        // 当timestamps的size为2，需要与当前时间作为参照
                        long dateOne = timestampsLastTmp[0];
                        long dateTwo = timestampsLastTmp[1];
                        if ((Math.abs(dateOne - dateTwo)) < 100000000000L) {
                            timestamp = Math.max(timestampsLastTmp[0], timestampsLastTmp[1]);
                        } else {
                            long now = System.currentTimeMillis();
                            if (Math.abs(dateOne - now) <= Math.abs(dateTwo - now)) {
                                timestamp = dateOne;
                            } else {
                                timestamp = dateTwo;
                            }
                        }
                    }
                }
            } else {
                timestamp = timestamps.get(0);
            }
        }

        if (timestamp != 0) {
            date = new Date(timestamp);
        }
        return date;
    }


    /**
     * 将日期字符串转化为日期。失败返回null。
     *
     * @param date     日期字符串
     * @param parttern 日期格式
     * @return
     */
    public static Date StringToDate(String date, String parttern) {
        Date myDate = null;
        if (date != null) {
            try {
                myDate = getDateFormat(parttern).parse(date);
            } catch (Exception e) {
            }
        }
        return myDate;
    }

    /**
     * 获取SimpleDateFormat
     *
     * @param parttern 日期格式
     * @return SimpleDateFormat对象
     * @throws RuntimeException 异常：非法日期格式
     */
    public static SimpleDateFormat getDateFormat(String parttern) throws RuntimeException {
        return new SimpleDateFormat(parttern);
    }


    public static Long getDaysBetween(Date startDate, Date endDate) {
        Calendar fromCalendar = Calendar.getInstance();
        fromCalendar.setTime(startDate);
        fromCalendar.set(Calendar.HOUR_OF_DAY, 0);
        fromCalendar.set(Calendar.MINUTE, 0);
        fromCalendar.set(Calendar.SECOND, 0);
        fromCalendar.set(Calendar.MILLISECOND, 0);
        Calendar toCalendar = Calendar.getInstance();
        toCalendar.setTime(endDate);
        toCalendar.set(Calendar.HOUR_OF_DAY, 0);
        toCalendar.set(Calendar.MINUTE, 0);
        toCalendar.set(Calendar.SECOND, 0);
        toCalendar.set(Calendar.MILLISECOND, 0);
        return (toCalendar.getTime().getTime() - fromCalendar.getTime().getTime()) / (1000 * 60 * 60 * 24);
    }

    /**
     * 当前日期加上天数后的日期
     *
     * @param days 为增加的天数
     * @throws ParseException
     */
    public static Date plusDays(int days) {
        Date d = new Date();
        Calendar ca = Calendar.getInstance();
        ca.add(Calendar.DATE, days);// num为增加的天数，可以改变的
        d = ca.getTime();
        return d;
    }


    /**
     * 当前日期加上天数后的日期
     *
     * @param days 为增加的天数
     */
    public static String plusDay(int days) {
        Calendar ca = Calendar.getInstance();
        ca.add(Calendar.DATE, days);// num为增加的天数，可以改变的
        Date d = ca.getTime();
        return DateUtil.parse(d, DATE_STR);
    }

    /**
     * 判断时间是否属于范围内
     *
     * @param now
     * @param start
     * @param end
     * @return
     */
    public static boolean between(Date now, Date start, Date end) {
        if (null == now || null == start || null == end) {
            return false;
        }
        return now.compareTo(start) >= 0 && now.compareTo(end) < 0;
    }

    /**
     * 获取两个日期相差多少天
     * @param start
     * @param end
     * @param format
     * @return
     */
    public static int getDays(String start, String end,String format) {
        SimpleDateFormat df = new SimpleDateFormat(format);
        Calendar cl1 = Calendar.getInstance();
        Calendar cl2 = Calendar.getInstance();
        try {
            cl1.setTime(df.parse(start));
            cl2.setTime(df.parse(end));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int count = 0;
        while (cl1.compareTo(cl2) <= 0) {
            if (cl1.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY && cl1.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY)
                count++;
            cl1.add(Calendar.DAY_OF_MONTH, 1);
        }
        return count;
    }

    public static void main(String[] args) {
        boolean result = between(parse("2021-07-03 23:59:59", DATE_FULL_STR), parse("2021-06-11 00:00:00", DATE_FULL_STR), parse("2021-07-03 23:59:59", DATE_FULL_STR));
        System.out.println(result);


        BigDecimal min = new BigDecimal("188.1");
        BigDecimal max = new BigDecimal("184.7");
        int n = 5;
        HashSet<BigDecimal> set = new HashSet<>();
        A(min,max,n,set);

        TreeSet<BigDecimal> sort = new TreeSet<>(set);
        System.out.println(JSONObject.toJSONString(sort));
    }

    public static void A(BigDecimal min, BigDecimal max, int n, HashSet<BigDecimal> set) {
        if (max.compareTo(min) < 0) {
            return;
        }
        for (int i = 0; i < n; i++) {
            // 将不同的数存入HashSet中
            String num = BigDecimal.valueOf(Math.random()).multiply(max.subtract(min)).add(min).setScale(2, BigDecimal.ROUND_UP).stripTrailingZeros().toPlainString();
            set.add(new BigDecimal(num));
        }
        int setSize = set.size();
        // 如果存入的数小于指定生成的个数，则调用递归再生成剩余个数的随机数，如此循环，直到达到指定大小
        if (setSize < n) {
            A(min, max, n - setSize, set);
        }
    }

}
