package com.lib.sdk.bean;

import com.alibaba.fastjson.annotation.JSONField;

public class SocketAuthorityBean {
	@JSONField(name = "Level")
	private int level;
	@JSONField(name = "Ability")
	private int ability;

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getAbility() {
		return ability;
	}

	public void setAbility(int ability) {
		this.ability = ability;
	}

}
