package com.hoy.model;



/**
 * 
 * @author LDicesaro
 *
 */
public class MainMenuItem<T> {

	private int id;
	private int imageDrawableId;
	private String name;
	private Class<T> targetActivityClass;

	public MainMenuItem(int id, int imageDrawableId, String name, Class<T> targetActivityClass) {
		this.id = id;
		this.imageDrawableId = imageDrawableId;
		this.name = name;
		this.targetActivityClass = targetActivityClass;
	}

	public MainMenuItem(int id, int imageDrawableId, String name, String description) {
		this.id = id;
		this.imageDrawableId = imageDrawableId;
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getImageDrawableId() {
		return imageDrawableId;
	}

	public void setImageDrawableId(int imageDrawableId) {
		this.imageDrawableId = imageDrawableId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	public String toString() {
		return name;
	}

	public Class<T> getTargetActivity() {
		return targetActivityClass;
	}

	public void setTargetActivity(Class<T> targetActivityClass) {
		this.targetActivityClass = targetActivityClass;
	}
}