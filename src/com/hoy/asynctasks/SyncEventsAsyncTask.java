package com.hoy.asynctasks;

import android.content.Context;
import com.google.gson.reflect.TypeToken;
import com.hoy.asynctasks.interfaces.GenericListHandleable;
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
public class SyncEventsAsyncTask extends AbstractGenericListAsyncTask<ParametersDTO,EventDTO> {

	public SyncEventsAsyncTask(Context uiContext, ParametersDTO paramEntity, GenericListHandleable<EventDTO> genericListHandleable) {
		super(uiContext, paramEntity, genericListHandleable);
	}

	@Override
		protected Type getType() {
			Type listType = new TypeToken<List<EventDTO>>() {
			}.getType();
			return listType;
		}

	@Override
	protected String getUrl() {
		return MilongaHoyConstants.SYNC_EVENTS_URL;
	}
}
