package com.hoy.model;

/**
 * Created with IntelliJ IDEA.
 * User: ksairi
 * Date: 6/5/13
 * Time: 11:55 PM
 * To change this template use File | Settings | File Templates.
 */
public class PromoImg {

	private String base64p;
	private String base64l;
	private Integer width;
	private Integer height;
	private String urlDestination;

	public String getBase64p() {
		return base64p;
	}

	public void setBase64p(String base64p) {
		this.base64p = base64p;
	}

	public String getBase64l() {
		return base64l;
	}

	public void setBase64l(String base64l) {
		this.base64l = base64l;
	}

	public Integer getWidth() {
		return width;
	}

	public void setWidth(Integer width) {
		this.width = width;
	}

	public Integer getHeight() {
		return height;
	}

	public void setHeight(Integer height) {
		this.height = height;
	}

	public String getUrlDestination() {
		return urlDestination;
	}

	public void setUrlDestination(String urlDestination) {
		this.urlDestination = urlDestination;
	}
}
