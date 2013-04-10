package com.hoy.timer_task;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.google.gson.reflect.TypeToken;
import com.hoy.R;
import com.hoy.cache.interfaces.GenericListServiceable;
import com.hoy.constants.MilongaHoyConstants;
import com.hoy.dto.EventDTO;
import com.hoy.dto.ParametersDTO;
import com.hoy.helpers.GsonHelper;
import com.hoy.services.EventsService;

import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

public class SyncEventsDailyTimerTask extends SyncEventsTimerTask {

	private static final String TAG = SyncEventsHourlyTimerTask.class.getSimpleName();

	public SyncEventsDailyTimerTask(Context context) {
				this.context = context;
	}

	@Override
		public void run(){

		String params = getParams();
		if(params != null){

			List<EventDTO> eventDTOs = EventsService.getInstance().synchronizeEventsFromServer(getUrl(),params,new TypeToken<List<EventDTO>>() {}.getType()); //{
			if (eventDTOs != null && !eventDTOs.isEmpty()) {
				EventsService.getInstance().setDataInCache(context,eventDTOs);
			}
		}
		else{
			Log.i(TAG, "Error al obtener los parametros");
		}
	}


	protected String getParams(){

		return GsonHelper.parseEntityToJson(new ParametersDTO());
	}

	protected String getUrl(){

			return MilongaHoyConstants.SYNC_EVENTS_URL;
	}



}
