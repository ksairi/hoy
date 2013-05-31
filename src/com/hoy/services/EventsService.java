package com.hoy.services;

import android.content.Context;
import android.util.Log;
import com.google.gson.reflect.TypeToken;
import com.hoy.asynctasks.SyncEventsAsyncTask;
import com.hoy.asynctasks.interfaces.GenericSuccessHandleable;
import com.hoy.asynctasks.interfaces.GenericSuccessListHandleable;
import com.hoy.constants.MilongaHoyConstants;
import com.hoy.dto.EventDTO;
import com.hoy.dto.ParametersDTO;
import com.hoy.helpers.GsonHelper;
import com.hoy.helpers.MilongaDataSource;
import com.hoy.helpers.SharedPreferencesHelper;
import com.hoy.model.FilterParams;
import com.hoy.utilities.DateUtils;
import com.hoy.utilities.MilongaCollectionUtils;
import com.hoy.utilities.RestClient;
import com.hoy.utilities.RestaunoPredicate;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author LDicesaro
 */
public class EventsService {

	private static final String TAG = EventsService.class.getSimpleName();
	private static EventsService eventService;
	protected List<EventDTO> eventDTOs;
	protected static MilongaDataSource milongaDataSource;

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
	 * @param //context
	 * @param //genericListServiceable
	 */
	public void synchronizeEventsFromServer(final Context context, final GenericSuccessHandleable genericSuccessHandleable) {

		new SyncEventsAsyncTask(context, new ParametersDTO(), new GenericSuccessHandleable() {

			public void handleSuccessCallBack() {

				genericSuccessHandleable.handleSuccessCallBack();

			}

			public void handleErrorResult() {
				Log.i(TAG, "Error al sincronizar los eventos");
				genericSuccessHandleable.handleErrorResult();
			}
		}).execute();
	}

	public void synchronizeEventsFromServer(final Context context, final GenericSuccessListHandleable genericSuccessHandleable) {

			new SyncEventsAsyncTask(context, new ParametersDTO(), new GenericSuccessListHandleable<EventDTO>() {

				public void handleSuccessCallBack(List<EventDTO> remoteEventDTOs) {

					genericSuccessHandleable.handleSuccessCallBack(remoteEventDTOs);

				}

				public void handleErrorResult() {
					Log.i(TAG, "Error al sincronizar los eventos");
					genericSuccessHandleable.handleErrorResult();
				}
			}).execute();
		}

/*	public synchronized Boolean mergeEvents(Context context, String jsonString) {

		Boolean atLeastOneChanged = false;
		if (synchronizedEventDTOs != null && eventDTOs != null) {
			for (EventDTO synchronizedEventDTO : synchronizedEventDTOs) {
				Integer index = eventDTOs.indexOf(synchronizedEventDTO);
				if (!index.equals(-1) && !eventDTOs.get(index).getLastUpdated().equals(synchronizedEventDTO.getLastUpdated())) {
					eventDTOs.set(index, synchronizedEventDTO);
					atLeastOneChanged = true;
				}
			}

			if (atLeastOneChanged) {
				setDataInCache(context, eventDTOs);
			}
		}

		return atLeastOneChanged;
	}*/

	public static synchronized void saveDataInDB(final Context uiContext, String jsonString) {

		getMilongaDataSource(uiContext).open();
			MilongaDataSource milongaDataSource = getMilongaDataSource(uiContext);
			milongaDataSource.createData(jsonString);
		getMilongaDataSource(uiContext).close();
	}

	public synchronized List<EventDTO> getFilteredEventDTOs(final Context uiContext, final FilterParams filterParams) {

		populateEventsFromDatabase(uiContext);
		if (filterParams != null) {
			return (List<EventDTO>) MilongaCollectionUtils.select(eventDTOs, new RestaunoPredicate<EventDTO>() {
				public boolean evaluate(EventDTO eventDTO) {
					return filterParams != null && filterParams.getDate() != null && filterParams.getDate().equals(eventDTO.getDate());
				}
			});
		}
		return eventDTOs;
	}


	public <T> List<T> synchronizeEventsFromServer(Context uiContext, String url, String params, Type type) {

		String stringResult = RestClient.executeHttpPostRequest(url, params);
		if(!stringResult.equals(MilongaHoyConstants.EMPTY_STRING)){
			getMilongaDataSource(uiContext).open();
				getMilongaDataSource(uiContext).createData(stringResult);
			getMilongaDataSource(uiContext).close();

		}
		stringResult = GsonHelper.parseResponse(stringResult);
		return GsonHelper.parseJsonToArrayListEntity(stringResult, type);

	}


	protected synchronized void populateEventsFromDatabase(final Context uiContext) {

			//String jsonString = SharedPreferencesHelper.getValueInSharedPreferences(uiContext, getSharedPreferencesKey());
			getMilongaDataSource(uiContext).open();
				String jsonString =getMilongaDataSource(uiContext).getAllMilongas();
			getMilongaDataSource(uiContext).close();
			if (!jsonString.equals(MilongaHoyConstants.EMPTY_STRING)) {
				Type listType = new TypeToken<List<EventDTO>>() {
				}.getType();
				eventDTOs = GsonHelper.parseJsonToArrayListEntity(jsonString, listType);
			}

		if (eventDTOs == null) {
			eventDTOs = new ArrayList<EventDTO>();
		}
	}

	public static Boolean hasLocalData(Context context) {

		getMilongaDataSource(context).open();
		String jsonString = getMilongaDataSource(context).getAllMilongas();
		getMilongaDataSource(context).close();

		return !jsonString.equals(MilongaHoyConstants.EMPTY_STRING);

	}

		public static boolean hasRecentlyManuallyUpdated(Context context) {
			Boolean result = false;
			try {
				String strLastManuallyUpdatedDate = SharedPreferencesHelper.getValueInSharedPreferences(context, MilongaHoyConstants.LAST_MANUALLY_UPDATE_DATE);
				if (!strLastManuallyUpdatedDate.equals(MilongaHoyConstants.EMPTY_STRING)) {
					Date lastManuallyUpdatedDate = DateUtils.getDateAndTimeFromString(strLastManuallyUpdatedDate);
					Calendar lastManuallyUpdatedCalendar = Calendar.getInstance();
					lastManuallyUpdatedCalendar.setTime(lastManuallyUpdatedDate);
					lastManuallyUpdatedCalendar.add(Calendar.HOUR, MilongaHoyConstants.MANUALLY_UPDATE_PERIOD);
					Calendar nowCalendar = Calendar.getInstance();
					result = lastManuallyUpdatedCalendar.before(nowCalendar);
				}
			} catch (ParseException e) {
				return result;
			}

			return result;
		}

	private static MilongaDataSource getMilongaDataSource(Context uiContext){
		if(milongaDataSource == null){
			milongaDataSource = new MilongaDataSource(uiContext);
		}
		return milongaDataSource;


	}
}
