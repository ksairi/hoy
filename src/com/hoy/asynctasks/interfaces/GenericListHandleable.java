package com.hoy.asynctasks.interfaces;

import java.util.List;

public interface GenericListHandleable<T> extends GenericHandleable {

	public void handleSuccessCallBack(List<T> entity);
}
