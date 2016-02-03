package com.demonshrimp.dawj.model.constant;

public enum ValidateMethod {
	Required("不能为空"),
	IsNumber("只能输入数字"),
	IsInteger("只能输入整数"),
	GT("必须大于{lowValue}"),
	GET("必须大于等于{lowValue}"),
	LT("必须小于{highValue}"),
	LET("必须小于等于{highValue}");
	
	private String name;

	private ValidateMethod(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	public String getCode() {
		return this.toString();
	}
}
