package com.demonshrimp.dawj.model.constant;

public enum CreateType {
	
	CreateNew("新建"),
	Clone("克隆"),
	Inherit("继承");

	private String name;

	private CreateType(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	public String getCode() {
		return this.toString();
	}

}