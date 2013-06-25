package com.hoy.asynctasks;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import com.hoy.asynctasks.interfaces.GenericSuccessHandleable;
import com.hoy.asynctasks.interfaces.GenericSuccessListHandleable;
import com.hoy.dto.EventDTO;
import com.hoy.dto.ParametersDTO;
import com.hoy.services.EventsService;

/**
 * Created with IntelliJ IDEA.
 * User: ksairi
 * Date: 1/2/13
 * Time: 4:33 AM
 * To change this template use File | Settings | File Templates.
 */
public class SyncEventsAsyncTask extends AbstractGenericAsyncTask<ParametersDTO, EventDTO> {

	public SyncEventsAsyncTask(Context uiContext, ParametersDTO paramEntity, FragmentManager fragmentManager, GenericSuccessHandleable genericHandleable) {
		super(uiContext, paramEntity, fragmentManager, genericHandleable);
	}

	public SyncEventsAsyncTask(Context uiContext, ParametersDTO paramEntity, FragmentManager fragmentManager, GenericSuccessListHandleable<EventDTO> genericSuccessListHandleable) {
		super(uiContext, paramEntity, fragmentManager, genericSuccessListHandleable);
	}

	@Override
	protected String getUrl() {
		return EventsService.getParametersDTO();
	}

	/*//@Override
		protected Type getType() {
			Type listType = new TypeToken<List<EventDTO>>() {
			}.getType();
			return listType;
		}*/
}
