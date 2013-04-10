package com.hoy.asynctasks;

import android.content.Context;
import com.hoy.asynctasks.interfaces.GenericListHandleable;
import com.hoy.helpers.GsonHelper;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Encargada de generalizar todo el comportamiento que prepara los datos para enviar al servicio remoto y recibir su respuesta.
 * Los resultados, deben ser Listas y no Entidades.
 *
 * @param <T>
 * @author MKsairi, LDicesaro
 */
public abstract class AbstractGenericListAsyncTask<T, S> extends AbstractAsyncTask<T> {

	@SuppressWarnings("unused")
	private static final String TAG = AbstractGenericListAsyncTask.class.getSimpleName();

	protected GenericListHandleable<S> genericListHandleable;

	public AbstractGenericListAsyncTask(Context uiContext, T paramEntity, GenericListHandleable<S> genericListHandleable) {
		this.uiContext = uiContext;
		this.paramEntity = paramEntity;
		this.genericListHandleable = genericListHandleable;
	}

	@Override
	protected void doOnSuccess(final String jsonString) {
		List<S> entityList = GsonHelper.parseJsonToArrayListEntity(jsonString, getType());
		genericListHandleable.handleSuccessCallBack(entityList);
	}

	@Override
	protected void doOnError() {
		genericListHandleable.handleErrorResult();
	}

	protected abstract Type getType();
}
