package com.demonshrimp.dawj.model.constant;

public enum EditType {
	
	Text("输入框"),
	Select("选择列表"),
	Checkbox("选择框"),
	Date("日期");

	private String name;

	private EditType(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	public String getCode() {
		return this.toString();
	}

}
