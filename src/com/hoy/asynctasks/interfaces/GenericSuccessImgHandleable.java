package com.hoy.asynctasks.interfaces;

/**
 * Created with IntelliJ IDEA.
 * User: ksairi
 * Date: 6/4/13
 * Time: 6:42 PM
 * To change this template use File | Settings | File Templates.
 */
public interface GenericSuccessImgHandleable extends GenericErrorHandleable{

	public void handleSuccessCallBack(String base64String);
}
