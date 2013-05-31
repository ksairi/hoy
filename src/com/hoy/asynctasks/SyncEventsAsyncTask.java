package com.hoy.asynctasks;

import android.content.Context;
import com.google.gson.reflect.TypeToken;
import com.hoy.asynctasks.interfaces.GenericSuccessHandleable;
import com.hoy.asynctasks.interfaces.GenericSuccessListHandleable;
import com.hoy.constants.MilongaHoyConstants;
import com.hoy.dto.EventDTO;
import com.hoy.dto.ParametersDTO;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: ksairi
 * Date: 1/2/13
 * Time: 4:33 AM
 * To change this template use File | Settings | File Templates.
 */
public class SyncEventsAsyncTask extends AbstractGenericAsyncTask<ParametersDTO, EventDTO> {

	public SyncEventsAsyncTask(Context uiContext, ParametersDTO paramEntity, GenericSuccessHandleable genericHandleable) {
		super(uiContext, paramEntity, genericHandleable);
	}
	public SyncEventsAsyncTask(Context uiContext, ParametersDTO paramEntity, GenericSuccessListHandleable genericSuccessListHandleable) {
			super(uiContext, paramEntity, genericSuccessListHandleable);
		}
	@Override
	protected String getUrl() {
		return MilongaHoyConstants.SYNC_EVENTS_URL;
	}

	@Override
		protected Type getType() {
			Type listType = new TypeToken<List<EventDTO>>() {
			}.getType();
			return listType;
		}
}
