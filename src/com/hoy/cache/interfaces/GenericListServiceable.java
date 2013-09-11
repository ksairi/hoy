package com.hoy.cache.interfaces;

import java.util.List;

public interface GenericListServiceable<T> extends AbstractServiceable {

	public void handleSuccessCallBack(List<T> genericList);
}
