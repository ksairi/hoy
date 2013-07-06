package com.hoy.asynctasks.interfaces;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: ksairi
 * Date: 5/29/13
 * Time: 6:56 PM
 * To change this template use File | Settings | File Templates.
 */
public interface GenericSuccessListHandleable<T> extends GenericErrorHandleable {

	public void handleSuccessCallBack(List<T> list);

	public void handleErrorCallBack(List<T> list);
}
