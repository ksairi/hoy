package com.hoy.services;

import android.content.Context;
import android.util.Log;
import com.google.gson.reflect.TypeToken;
import com.hoy.asynctasks.SyncEventsAsyncTask;
import com.hoy.asynctasks.interfaces.GenericListHandleable;
import com.hoy.cache.interfaces.GenericListServiceable;
import com.hoy.constants.MilongaHoyConstants;
import com.hoy.dto.EventDTO;
import com.hoy.dto.ParametersDTO;
import com.hoy.helpers.GsonHelper;
import com.hoy.helpers.SharedPreferencesHelper;
import com.hoy.model.FilterParams;
import com.hoy.schedulers.EventsScheduler;
import com.hoy.utilities.MilongaCollectionUtils;
import com.hoy.utilities.RestClient;
import com.hoy.utilities.RestaunoPredicate;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author LDicesaro
 *
 */
public class EventsService{

	private static final String TAG = EventsService.class.getSimpleName();
	private static EventsService eventService;
	protected List<EventDTO> eventDTOs;

	private EventsService() {
	}

	public static EventsService getInstance() {
		if (eventService == null) {
			synchronized (EventsService.class) {
				if (eventService == null) {
					eventService = new EventsService();
				}
			}
		}
		return eventService;
	}

	/**
	 * Se destruye el Singleton.
	 */
	public void destroyInstance() {
		eventDTOs = null;
		eventService = null;
	}



	/**
	 * Se obtienen los eventos del servidor y se sincronizan con los eventos locales.
	 *
	 * @param context
	 * @param genericListServiceable
	 */
	public void synchronizeEventsFromServer(final Context context, final GenericListServiceable<EventDTO> genericListServiceable) {

		new SyncEventsAsyncTask(context, new ParametersDTO(), new GenericListHandleable<EventDTO>() {

			public void handleSuccessCallBack(List<EventDTO> eventDTOs) {
				if (eventDTOs != null && !eventDTOs.isEmpty()) {
					setDataInCache(context,eventDTOs);
					genericListServiceable.handleSuccessCallBack(eventDTOs);
				}
			}

			public void handleErrorResult() {
				Log.i(TAG, "Error al sincronizar los eventos");
				genericListServiceable.handleErrorResult();
			}
		}).execute();
	}

	public synchronized Boolean mergeEvents(Context context, List<EventDTO> synchronizedEventDTOs) {

		Boolean atLeastOneChanged = false;
			if (synchronizedEventDTOs != null && eventDTOs != null) {
				for (EventDTO synchronizedEventDTO : synchronizedEventDTOs) {
					Integer index = eventDTOs.indexOf(synchronizedEventDTO);
					if (!index.equals(-1) && !eventDTOs.get(index).getLastUpdated().equals(synchronizedEventDTO.getLastUpdated())) {
						eventDTOs.set(index, synchronizedEventDTO);
						atLeastOneChanged = true;
					}
				}

				if(atLeastOneChanged){
					setDataInCache(context, eventDTOs);
				}
			}

		return atLeastOneChanged;
		}

	public synchronized void setDataInCache(final Context uiContext, List<EventDTO> eventDTOs) {

		this.eventDTOs = eventDTOs;
		String jsonString = "";
		if (eventDTOs != null && !eventDTOs.isEmpty()) {
			jsonString = GsonHelper.parseEntityToJson(eventDTOs);
		}

		SharedPreferencesHelper.setValueSharedPreferences(uiContext, getSharedPreferencesKey(), jsonString);
	}

	public synchronized  List<EventDTO> getFilteredEventDTOs(final Context uiContext, final FilterParams filterParams) {

		populateEventsFromSharedPreferences(uiContext);
		if(filterParams != null){
			return (List<EventDTO>) MilongaCollectionUtils.select(eventDTOs, new RestaunoPredicate<EventDTO>() {
				public boolean evaluate(EventDTO eventDTO) {
					return filterParams!= null && filterParams.getDate() != null && filterParams.getDate().equals(eventDTO.getDate());
				}
			});
		}
		else{

		}   return eventDTOs;
	}


	public <T>List<T>synchronizeEventsFromServer(String url, String params, Type type) {

		String stringResult = RestClient.executeHttpPostRequest(url,params);
		return GsonHelper.parseJsonToArrayListEntity(stringResult,type);

	}



	protected synchronized void populateEventsFromSharedPreferences(final Context uiContext) {
			if (eventDTOs == null || eventDTOs.isEmpty()) {
				String jsonString = SharedPreferencesHelper.getValueInSharedPreferences(uiContext, getSharedPreferencesKey());
				if (!jsonString.equals(MilongaHoyConstants.EMPTY_STRING)) {
					Type listType = new TypeToken<List<EventDTO>>() {
					}.getType();
					eventDTOs = GsonHelper.parseJsonToArrayListEntity(jsonString, listType);
				}
			}
			if (eventDTOs == null || eventDTOs.isEmpty()) {
				eventDTOs = new ArrayList<EventDTO>();
			}
		}

	private String getSharedPreferencesKey() {
		return MilongaHoyConstants.SAVED_EVENTS_LIST;
	}
}
