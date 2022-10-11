package com.nova.tools.vc.activity;

import com.nova.tools.utils.time.DateUtil;

/**
 * @Description:
 * @Author: wangzehui
 * @Date: 2018/11/19 17:35
 */
public class BetweenTime {

    public static void main(String[] args) throws Exception {

        String formatBegin = "2018-12-04";
        String formatEnd = "2018-12-10";
        int intervalDays = DateUtil.getIntervalDays(formatEnd, formatBegin);

        System.out.println(intervalDays);

        String[] betweenDateStr = DateUtil.getBetweenDateStr(formatBegin, formatEnd);
        System.out.println(betweenDateStr.length - 1);

    }


}
