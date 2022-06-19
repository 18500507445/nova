package com.nova.tools.vc.tps.hfh.recharge;

/**
 * @author wangzehui
 * @date 2018/8/1 13:51
 */

public class BeanTest {
    public static void main(String[] args) {
//        //情况1
//        Bean bean = null;

        //情况2
        Bean bean = new Bean();
        bean.setStatus("1");
        bean.setCeshi("success");

//        //情况3
//        Bean bean = new Bean();


        if (bean == null) {
            System.out.println("返回状态-1");
            System.out.println("返回code超时");
            System.out.println("返回msg售票系统方响应超时");
        } else {
            if (("1").equals(bean.getStatus())) {
                System.out.println("返回code成功");
                System.out.println("返回msg成功");
                String ceShi = bean.getCeshi();
                switch (ceShi) {
                    case "success":
                        System.out.println("返回状态1");
                        System.out.println("交易状态true");
                        break;
                    case "wait":
                        System.out.println("返回状态0");
                        System.out.println("交易状态false");
                        break;
                    case "fail":
                        System.out.println("返回状态-1");
                        System.out.println("交易状态false");
                        break;
                    default:
                        break;
                }
            } else {
                System.out.println("返回状态-1");
                System.out.println("返回code未知");
                System.out.println("返回msg未知");
            }
        }
    }
}
