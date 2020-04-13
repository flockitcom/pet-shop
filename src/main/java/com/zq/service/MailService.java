package com.zq.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;

/**
 * 发邮件工具类
 */
@Component
public class MailService {

    @Autowired
    private MailSender mailSender;

    public void sendMail(String tomail, int code) {
        // new 一个简单邮件消息对象
        SimpleMailMessage message = new SimpleMailMessage();
        // 和配置文件中的的username相同，相当于发送方
        message.setFrom("13294141428@163.com");
        // 收件人邮箱
        message.setTo(tomail);
        // 标题
        message.setSubject("宠物商店管理系统的设计实现");
        // 正文
        message.setText("【宠物商城】您的验证码为"+code+",该验证码5分钟内有效。请勿泄露于他人。");
        // 发送
        mailSender.send(message);
    }


}
