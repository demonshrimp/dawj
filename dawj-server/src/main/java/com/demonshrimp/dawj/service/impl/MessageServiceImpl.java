package com.demonshrimp.dawj.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

import com.demonshrimp.dawj.exception.ServiceException;
import com.demonshrimp.dawj.service.MessageService;

@Service
public class MessageServiceImpl implements MessageService {
	@Autowired
	private JavaMailSender javaMailSender;

	@Override
	public void sendMessage(String destination, String subject, String content, Type type) {
		switch (type) {
		case MOBILE:
			senSms(destination, content);
			break;
		case EMAIL:
			sendMail(destination, subject, content);
			break;
		default:
			throw new ServiceException("未知的消息发送类型");
		}
	}

	private void senSms(String mobile, String content) {
		throw new ServiceException("尚未实现短信发送方式");
	}

	private void sendMail(String to, String subject, String content) {
		// 建立邮件消息
		SimpleMailMessage mailMessage = new SimpleMailMessage();
		mailMessage.setTo(to);
		mailMessage.setFrom(((JavaMailSenderImpl)javaMailSender).getUsername());
		mailMessage.setSubject(subject);
		mailMessage.setText(content);
		javaMailSender.send(mailMessage);
	}

}
