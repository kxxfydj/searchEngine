package com.kxxfydj.utils;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Properties;

/**
 * Created by Administrator on 2017/7/7.
 */
public class SendEmail {
    private static final String FORM = "18826137380@163.com";

    private static final String USERNAME = "18826137380@163.com";

    private static final String PASSWORD = "kxx19960315fydj";

    /**
     * 邮件发送功能
     * @param from  发件人邮箱地址,使用163邮箱
     * @param username  发件人用户名
     * @param password  发件人的授权码,smtp授权码
     * @param toEmail   收件人邮箱地址
     * @param title 邮箱标题
     * @param content   邮箱内容
     * @throws Exception
     */
    public static void sendEmail(String from,String username,String password,String toEmail,String title,String content) throws MessagingException{
        Properties pros = new Properties();
        pros.setProperty("mail.smtp.host","smtp.163.com");
        pros.setProperty("mail.smtp.auth","true");

        Session session = Session.getInstance(pros);

        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(from));
        message.setRecipient(Message.RecipientType.TO,new InternetAddress(toEmail));
        message.setSubject(title);
        message.setContent(content,"text/html;charset=utf-8");
        message.setSentDate(new Date());
        message.saveChanges();

        Transport transport = session.getTransport("smtp");
        transport.connect(username,password);
        transport.sendMessage(message,message.getAllRecipients());
        transport.close();

    }

    public static void sendEmail(String toEmail,String content) throws MessagingException{
        sendEmail(FORM,USERNAME,PASSWORD,toEmail,"",content);
    }

}
