package com.nova.tools.demo.exercise;

import java.util.Scanner;

/**
 * @author vc
 * 本地城镇户口和外地城镇户口：养老保险企业缴费比例20%，个人缴费比例8%；医疗保险企业缴费比例10%+大额互助1%，个人缴费比例2%+3元；失业保险企业缴费比例1%，个人缴费比例0.2%；工伤保险核定比例0.2%-2%，个人不缴费；生育保险企业缴费比例0.8%，个人不缴费。
 * 外地农村劳动力：养老保险企业缴费比例20%，个人缴费比例8%；医疗保险企业缴费比例10%+大额互助1%，个人缴费比例2%+3元；失业保险企业缴费比例1%，个人不缴费；工伤保险核定比例0.2%-2%，个人不缴费；生育保险企业缴费比例0.8%，个人不缴费。
 * （2）2014年度北京公积金缴存比例
 * 2014年度（2014年7月1日至2015年6月30日）住房公积金缴存比例为12%。
 * 五险一金缴纳比例是按年度计算的，通常一个年度横跨两个年份，因此，以上2014年北京五险一金缴纳比例相关介绍，仅仅适合2014年7月1日以后的缴费计算，至于2014年7月1日之前的缴纳比例
 */
class RealWages {

    static final double BASE = 5000;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.err.println("请输入工资总数");
        double all = sc.nextDouble();
        System.err.println("请输入社保公积金缴纳基数");
        double sb = sc.nextDouble();
        System.err.println("请输入户口性质：1表示城镇，2表示农村");
        //1为城镇，2为农村
        String hukou = sc.next();
        double shebao = 0;
        double yanglao = sb * 0.08;
        double yiliao = sb * 0.02 + 3;
        double shiye = 0;
        if ("1".equals(hukou)) {
            shiye = sb * 0.002;
            shebao = yanglao + yiliao + shiye;
        } else {
            shebao = yanglao + yiliao;
        }
        double gongjijin = sb * 0.12;
        double jisuan = all - shebao - gongjijin;
        //本程序不计算工资35000以上的变态人士
        double jb = jisuan - BASE;

        /**
         * 个税计算7级标准
         全月应纳税所额                     税率            速算扣除数（元）
         全月应纳税额不超过1500元             3%             0
         全月应纳税额超过1500元至4500元      10%            105
         全月应纳税额超过4500元至9000元      20%            555
         全月应纳税额超过9000元至35000元     25%            1005
         全月应纳税额超过35000元至55000元    30%             2755
         全月应纳税额超过55000元至80000元    35%             5505
         全月应纳税额超过80000元              45%             13505
         */
        double geshui = 0;
        if (jb > 9000) {
            geshui = jb * 0.25 - 1005;
        } else if (jb > 4500 && jb < 9000) {
            geshui = jb * 0.2 - 555;
        } else if (jb > 1500 && jb < 4500) {
            geshui = jb * 0.1 - 105;
        } else if (jb < 1500) {
            geshui = jb * 0.03;
        }
        System.err.println("您缴纳的公积金=" + gongjijin);
        System.err.println("您缴纳的社保=" + shebao + "，包含：养老=" + yanglao + "，医疗=" + yiliao + "，失业=" + shiye);
        System.err.println("您缴纳的个税=" + geshui);
        double shifagongzi = all - shebao - geshui - gongjijin;
        System.err.println("实发工资=" + shifagongzi);
    }

}
