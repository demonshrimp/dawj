package com.demonshrimp.dawj.service;

public interface MessageService {
	/**
	 * 发送消息
	 * @param destination
	 * @param subject
	 * @param content
	 * @param type
	 */
	void sendMessage(String destination, String subject, String content, Type type);

	public enum Type {
		MOBILE, EMAIL
	}
}
