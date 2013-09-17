package com.hoy.asynctasks;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import com.hoy.asynctasks.interfaces.GenericSuccessHandleable;
import com.hoy.asynctasks.interfaces.GenericSuccessListHandleable;
import com.hoy.dto.EventDTO;
import com.hoy.model.FilterParams;

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


    public AbstractGenericAsyncTask(Context uiContext, T paramEntity, FragmentManager fragmentManager, GenericSuccessHandleable genericSuccessHandleable) {
        this.uiContext = uiContext;
        this.paramEntity = paramEntity;
        this.genericSuccessHandleable = genericSuccessHandleable;
        this.fragmentManager = fragmentManager;
    }

    public AbstractGenericAsyncTask(Context uiContext, T paramEntity, FragmentManager fragmentManager, FilterParams filterParams, GenericSuccessListHandleable<EventDTO> genericSuccessListHandleable) {
        this.uiContext = uiContext;
        this.paramEntity = paramEntity;
        this.genericSuccessListHandleable = genericSuccessListHandleable;
        this.fragmentManager = fragmentManager;
        this.filterParams = filterParams;

    }

    @Override
    protected void doOnSuccess() {

        if (genericSuccessHandleable != null) {
            genericSuccessHandleable.handleSuccessCallBack();
        } else {
            genericSuccessListHandleable.handleSuccessCallBack(eventDTOs);
        }
    }

    @Override
    protected void doOnError() {
        if (genericSuccessHandleable != null) {
            genericSuccessHandleable.handleErrorResult();
        } else {
            genericSuccessListHandleable.handleErrorCallBack(eventDTOs);
        }
    }


    //protected abstract Type getType();
}
