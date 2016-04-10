package com.demonshrimp.dawj.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

import com.demonshrimp.dawj.exception.ServiceException;
import com.demonshrimp.dawj.message.sms.weimi.WeiMiSmsProvider;
import com.demonshrimp.dawj.service.MessageService;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

@Service
public class MessageServiceImpl implements MessageService {
	@Autowired
	private JavaMailSender javaMailSender;

	@Autowired
	private WeiMiSmsProvider weiMiSmsProvider;

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
		System.out.println(mobile + ":" + content);
		String json = weiMiSmsProvider.sms_api1(mobile, content, "json");
		JsonObject obj = new Gson().fromJson(json, JsonObject.class);
		System.out.println(json);
		if (obj.get("code").getAsInt() != 0) {
			throw new ServiceException(obj.get("msg").getAsString());
		}
	}

	private void sendMail(String to, String subject, String content) {
		// 建立邮件消息
		SimpleMailMessage mailMessage = new SimpleMailMessage();
		mailMessage.setTo(to);
		mailMessage.setFrom(((JavaMailSenderImpl) javaMailSender).getUsername());
		mailMessage.setSubject(subject);
		mailMessage.setText(content);
		javaMailSender.send(mailMessage);
	}

	
}
