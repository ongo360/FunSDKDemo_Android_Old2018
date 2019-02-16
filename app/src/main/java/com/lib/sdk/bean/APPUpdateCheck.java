package com.lib.sdk.bean;

import org.json.JSONException;
import org.json.JSONObject;

import com.basic.G;

/**
 * APP更新检测
 * 
 * @author hws
 * 
 */
public class APPUpdateCheck {
	public static final String CLASSNAME = "APPUpdateCheck";
	private String name;
	private String version;
	private int build = 0;
	private String changelog;
	private String versionShort;
	private String installUrl;
	private String install_url;
	private String update_url;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getChangelog() {
		return changelog;
	}

	public void setChangelog(String changelog) {
		this.changelog = changelog;
	}

	public String getVersionShort() {
		return versionShort;
	}

	public void setVersionShort(String versionShort) {
		this.versionShort = versionShort;
	}

	public String getInstallUrl() {
		return installUrl;
	}

	public void setInstallUrl(String installUrl) {
		this.installUrl = installUrl;
	}

	public String getInstall_url() {
		return install_url;
	}

	public void setInstall_url(String install_url) {
		this.install_url = install_url;
	}

	public String getUpdate_url() {
		return update_url;
	}

	public void setUpdate_url(String update_url) {
		this.update_url = update_url;
	}

	public boolean onParse(String json) {
		if (StringUtils.isStringNULL(json))
			return false;
		if (!json.contains("{") || !json.contains("}")) {
			return false;
		}
		try {
			int index = 0;
			if (!json.startsWith("{")) {
				index = json.indexOf("{");
			}
			if (index > 0 && index < json.length()) {
				json = json.substring(index, json.length());
			}
			if (!json.endsWith("}")) {
				index = json.lastIndexOf("}");
			}
			if (index > 0 && index < json.length()) {
				json = json.substring(0, index + 1);
			}
			JSONObject jsonObj = new JSONObject(json);
			return onParse(jsonObj);
		} catch (JSONException e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean onParse(JSONObject obj) throws JSONException {
		if (null == obj)
			return false;
		setName(obj.optString("name"));
		build = G.ParseInt(obj.getString("build"));
		version = obj.getString("version");
		setChangelog(obj.optString("changelog"));
		setVersionShort(obj.optString("versionShort"));
		setInstall_url(obj.optString("install_url"));
		setInstallUrl(obj.optString("installUrl"));
		setUpdate_url(obj.optString("update_url"));
		return true;
	}

	/**
	 * 判断是否需要更新
	 */
	public boolean isUpdate(int buildCode) {
		return build > buildCode;
	}

	@Override
	public String toString() {
		return "[name]:" + name + "[version]:" + version + "[changelog]:"
				+ changelog + "[install_rul]:" + install_url + "[installUrl]:"
				+ installUrl + "[update_url]:" + update_url;
	}
}
