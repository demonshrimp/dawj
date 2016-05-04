package com.tenpay.util;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class JsonUtil {
	private static Gson gson = null;

	public static String getJsonValue(String rescontent, String key) {
		JsonObject jsonObject;
		String v = null;
		try {
			jsonObject = getGson().fromJson(rescontent, JsonObject.class);
			v = jsonObject.get(key).getAsString();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return v;
	}

	public static Gson getGson() {
		if (null == gson) {
			gson = new Gson();
		}
		return gson;
	}
}
