package com.nova.tools.utils.mail;

import com.sun.xml.internal.messaging.saaj.packaging.mime.MessagingException;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;
import java.util.List;
import java.util.Properties;

public class SendEmail {

    private static MimeMessage message;
    private static Session session;
    private static Transport transport;
    private static Properties properties = new Properties();
    private static String mailHost;
    private static String senderUsername;
    private static String senderPassword;
    private static String nickname;

    /*
     * 初始化方法
     */
    private SendEmail(boolean debug) {
        session = Session.getInstance(properties);
        session.setDebug(debug);
        message = new MimeMessage(session);
    }

    public static SendEmail builder(String mailHost, String senderUsername, String senderPassword, String nickname, Boolean debug) {
        SendEmail sendEmail = new SendEmail(debug);
        SendEmail.mailHost = mailHost;
        SendEmail.senderUsername = senderUsername;
        SendEmail.senderPassword = senderPassword;
        SendEmail.nickname = nickname;
        return sendEmail;
    }

    public static SendEmail builder(String mailHost, String senderUsername, String senderPassword, String nickname) {
        SendEmail sendEmail = new SendEmail(false);
        SendEmail.mailHost = mailHost;
        SendEmail.senderUsername = senderUsername;
        SendEmail.senderPassword = senderPassword;
        SendEmail.nickname = nickname;
        return sendEmail;
    }

    /**
     * 发送邮件
     *
     * @param subject 邮件主题
     * @param content 邮件内容
     * @param tolist  收件人地址
     * @param cclist  抄送人地址
     */
    public void doSendHtmlEmail(String subject, String content, List<String> tolist, List<String> cclist) {
        try {
            Address from = new InternetAddress(MimeUtility.encodeWord(nickname) + " <" + senderUsername + ">");
            Address[] to = new InternetAddress[tolist.size()];
            for (int i = 0; i < tolist.size(); i++) {
                to[i] = new InternetAddress(tolist.get(i));
            }
            message.setRecipients(Message.RecipientType.TO, to);
            if (cclist != null) {
                Address[] cc = new InternetAddress[cclist.size()];
                for (int i = 0; i < cclist.size(); i++) {
                    cc[i] = new InternetAddress(cclist.get(i));
                }
                message.setRecipients(Message.RecipientType.CC, cc);
            }
            message.setFrom(from);
            message.setSubject(subject);
            message.setContent(content, "text/html;charset=UTF-8");
            message.saveChanges();
            transport = session.getTransport("smtp");
            transport.connect(mailHost, senderUsername, senderPassword);
            transport.sendMessage(message, message.getAllRecipients());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (transport != null) {
                try {
                    transport.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 发送邮件
     *
     * @param subject 邮件主题
     * @param content 邮件内容
     * @param tolist  收件人地址
     * @param cclist  抄送人地址
     */
    public void doSendTextEmail(String subject, String content, List<String> tolist, List<String> cclist) {
        try {
            Address from = new InternetAddress(MimeUtility.encodeWord(nickname) + " <" + senderUsername + ">");
            Address[] to = new InternetAddress[tolist.size()];
            for (int i = 0; i < tolist.size(); i++) {
                to[i] = new InternetAddress(tolist.get(i));
            }
            message.setRecipients(Message.RecipientType.TO, to);
            if (cclist != null) {
                Address[] cc = new InternetAddress[cclist.size()];
                for (int i = 0; i < cclist.size(); i++) {
                    cc[i] = new InternetAddress(cclist.get(i));
                }
                message.setRecipients(Message.RecipientType.CC, cc);
            }
            message.setFrom(from);
            message.setSubject(subject);
            message.setContent(content, "text/plain;charset=UTF-8");
            message.saveChanges();
            transport = session.getTransport("smtp");
            transport.connect(mailHost, senderUsername, senderPassword);
            transport.sendMessage(message, message.getAllRecipients());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (transport != null) {
                try {
                    transport.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }


}