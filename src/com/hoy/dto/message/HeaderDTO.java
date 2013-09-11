package com.hoy.dto.message;

import java.io.Serializable;

/**
 * Se utiliza para transportar todos los datos de configuracion o de seguridad en cada mensaje de servicios remotos.
 *
 * @author LDicesaro
 */
public class HeaderDTO implements Serializable {

	private static final long serialVersionUID = 4509429913201054959L;
	private String language;
	private String accessToken;
	private String deviceId;
	private Integer appVersion;

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public Integer getAppVersion() {
		return appVersion;
	}

	public void setAppVersion(Integer appVersion) {
		this.appVersion = appVersion;
	}
}
