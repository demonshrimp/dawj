package com.demonshrimp.dawj.service;

public interface CaptchaService {

	int generateCaptcha();

	void storeCaptcha(String key, int captcha);

	boolean checkCaptcha(String key, int captcha, boolean destroy);
}
