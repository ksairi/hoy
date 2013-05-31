package com.hoy.model;

/**
 * Created with IntelliJ IDEA.
 * User: ksairi
 * Date: 1/4/13
 * Time: 4:40 AM
 * To change this template use File | Settings | File Templates.
 */
public class FilterParams {

	private String date;

	public FilterParams(String date) {
		this.date = date;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}
}
