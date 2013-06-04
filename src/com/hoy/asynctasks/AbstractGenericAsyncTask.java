package com.hoy.asynctasks;

import android.content.Context;
import com.hoy.asynctasks.interfaces.GenericSuccessHandleable;
import com.hoy.asynctasks.interfaces.GenericSuccessListHandleable;
import com.hoy.dto.EventDTO;
import com.hoy.helpers.GsonHelper;
import com.hoy.services.EventsService;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Encargada de generalizar todo el comportamiento que prepara los datos para enviar al servicio remoto y recibir su respuesta.
 * Los resultados, deben ser Listas y no Entidades.
 *
 * @param <T>
 * @author MKsairi, LDicesaro
 */
public abstract class AbstractGenericAsyncTask<T, S> extends AbstractAsyncTask<T> {

	@SuppressWarnings("unused")
	private static final String TAG = AbstractGenericAsyncTask.class.getSimpleName();

	protected GenericSuccessHandleable genericSuccessHandleable;
	protected GenericSuccessListHandleable<S> genericSuccessListHandleable;

	public AbstractGenericAsyncTask(Context uiContext, T paramEntity, GenericSuccessHandleable genericSuccessHandleable) {
		this.uiContext = uiContext;
		this.paramEntity = paramEntity;
		this.genericSuccessHandleable = genericSuccessHandleable;
	}

	public AbstractGenericAsyncTask(Context uiContext, T paramEntity, GenericSuccessListHandleable<S> genericSuccessListHandleable) {
			this.uiContext = uiContext;
			this.paramEntity = paramEntity;
			this.genericSuccessListHandleable = genericSuccessListHandleable;
		}

	@Override
	protected void doOnSuccess(final String jsonString) {
		EventsService.saveDataInDB(uiContext, jsonString);
		if(genericSuccessHandleable != null){
			genericSuccessHandleable.handleSuccessCallBack();
		}else{
			List<S> eventDTOs = GsonHelper.parseJsonToArrayListEntity(jsonString, getType());
			genericSuccessListHandleable.handleSuccessCallBack(eventDTOs);
		}
	}

	@Override
	protected void doOnError() {
		genericSuccessHandleable.handleErrorResult();
	}


	protected abstract Type getType();
}
