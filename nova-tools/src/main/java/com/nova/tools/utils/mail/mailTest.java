package com.nova.tools.utils.mail;

import org.apache.commons.mail.HtmlEmail;

import java.util.Random;

/**
 * @Description:
 * @Author: wangzehui
 * @Date: 2021/9/14 9:40 上午
 */
public class mailTest {

    public static void main(String[] args) {

    }

    /**
     * 生成随机验证码
     *
     * @param number 几位数
     * @return
     */
    public String generateVerifyCode(int number) {
        Random random = new Random();
        StringBuilder builder = new StringBuilder();
        for (int i = 1; i <= number; i++) {
            builder.append(random.nextInt(10));
        }
        return builder.toString();
    }

    /**
     * 发送验证码
     * @param email
     * @return
     */
    public int sendAuthCodeEmail(String email) {
        try {
            HtmlEmail mail = new HtmlEmail();
            /*发送邮件的服务器 126邮箱为smtp.126.com,163邮箱为163.smtp.com，QQ为smtp.qq.com*/
            mail.setHostName("smtp.qq.com");
            /*不设置发送的消息有可能是乱码*/
            mail.setCharset("UTF-8");
            /*IMAP/SMTP服务的密码*/
            mail.setAuthentication("发送消息的邮箱如：2212312@qq.com", "密码");
            /*发送邮件的邮箱和发件人*/
            mail.setFrom("发件邮箱", "发件人");
            /*使用安全链接*/
            mail.setSSLOnConnect(true);
            /*接收的邮箱*/
            mail.addTo("1251850055@qq.com");
            /*验证码*/
            String code = this.generateVerifyCode(6);
            /*设置邮件的主题*/
            mail.setSubject("注册验证码");
            /*设置邮件的内容*/
            mail.setMsg("尊敬的用户:你好! 注册验证码为:" + code + "(有效期为一分钟)");
            mail.send();//发送
        } catch (Exception e) {
            return 0;
        }
        return 1;
    }
}
