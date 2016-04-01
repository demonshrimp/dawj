package com.demonshrimp.dawj.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.demonshrimp.dawj.service.CaptchaService;

@Service
public class CaptchaServiceImpl implements CaptchaService {
	private static final long EFFECTIVE_TIME = 1000 * 60 * 5;// 5分钟(单位ms)
	private static final Map<String, Captcha> CAPTCHA_POOL = new HashMap<>();

	@Override
	public int generateCaptcha() {
		return (int) (Math.random() * 899999 + 100000);
	}

	@Override
	public void storeCaptcha(String key, int captcha) {
		CAPTCHA_POOL.put(key, new Captcha(captcha, System.currentTimeMillis()));
	}

	@Override
	public boolean checkCaptcha(String key, int captcha, boolean destroy) {
		Captcha c = CAPTCHA_POOL.get(key);
		if (null != c && c.captcha == captcha && (System.currentTimeMillis() - c.time) < EFFECTIVE_TIME) {
			if (destroy) {
				CAPTCHA_POOL.remove(key);
			}
			return true;
		}
		return false;
	}

	private class Captcha {
		private int captcha;
		private long time;

		public Captcha(int captcha, long time) {
			super();
			this.captcha = captcha;
			this.time = time;
		}
	}

}