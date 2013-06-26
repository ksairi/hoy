package com.hoy.services;

import android.content.Context;
import android.database.SQLException;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import com.google.gson.reflect.TypeToken;
import com.hoy.asynctasks.SyncEventsAsyncTask;
import com.hoy.asynctasks.interfaces.GenericSuccessHandleable;
import com.hoy.asynctasks.interfaces.GenericSuccessListHandleable;
import com.hoy.constants.MilongaHoyConstants;
import com.hoy.datasources.MilongaDataSource;
import com.hoy.dto.EventDTO;
import com.hoy.dto.ParametersDTO;
import com.hoy.helpers.GsonHelper;
import com.hoy.helpers.SharedPreferencesHelper;
import com.hoy.model.FilterParams;
import com.hoy.utilities.DateUtils;
import com.hoy.utilities.MilongaCollectionUtils;
import com.hoy.utilities.RestClient;
import com.hoy.utilities.RestaunoPredicate;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * @author LDicesaro
 */
public class EventsService {

	private static final String TAG = EventsService.class.getSimpleName();
	private static EventsService eventService;
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

		eventService = null;
	}

	public void synchronizeEventsFromServer(final Context context, FragmentManager fragmentManager, final GenericSuccessHandleable genericSuccessHandleable) {

		new SyncEventsAsyncTask(context, new ParametersDTO(), fragmentManager, new GenericSuccessHandleable() {

			public void handleSuccessCallBack() {

				genericSuccessHandleable.handleSuccessCallBack();

			}

			public void handleErrorResult() {
				Log.i(TAG, "Error al sincronizar los eventos");
				genericSuccessHandleable.handleErrorResult();
			}
		}).execute();
	}


	public void synchronizeEventsFromServer(final Context context, FragmentManager fragmentManager, final GenericSuccessListHandleable<EventDTO> genericSuccessHandleable) {

		new SyncEventsAsyncTask(context, new ParametersDTO(), fragmentManager, new GenericSuccessListHandleable<EventDTO>() {

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

	public static synchronized void saveMilongasData(final Context uiContext, String jsonString) {

		getMilongaDataSource(uiContext).open();
		getMilongaDataSource(uiContext).createData(jsonString);
		getMilongaDataSource(uiContext).close();

	}

	public synchronized List<EventDTO> getFilteredEventDTOs(final Context uiContext, final FilterParams filterParams) {

		List<EventDTO> eventDTOs = populateEventsFromDatabase(uiContext);
		if (filterParams != null) {
			return (List<EventDTO>) MilongaCollectionUtils.select(eventDTOs, new RestaunoPredicate<EventDTO>() {
				public boolean evaluate(EventDTO eventDTO) {
					return filterParams.getDate() != null && filterParams.getDate().equals(eventDTO.getDate());
				}
			});
		} else {
			return (List<EventDTO>) MilongaCollectionUtils.select(eventDTOs, new RestaunoPredicate<EventDTO>() {
				public boolean evaluate(EventDTO eventDTO) {

					return !DateUtils.getTodayString().equals(eventDTO.getDate());
				}
			});

		}
	}


	public String synchronizeEventsFromServer(Context uiContext, String url, String params, Boolean deltaUpdate) {

		String eventsToSave;
		String remoteEvents = RestClient.executeHttpGetRequest(url.concat(params));
		if (remoteEvents == null) {
			return null;
		}
		remoteEvents = GsonHelper.parseResponse(remoteEvents);

		if (remoteEvents != null && !remoteEvents.equals(MilongaHoyConstants.EMPTY_STRING)) {
			try {
				if (deltaUpdate) {

					List<EventDTO> localEventDTOs = populateEventsFromDatabase(uiContext);
					eventsToSave = updateLocalEvents(localEventDTOs, remoteEvents);
				} else {
					eventsToSave = remoteEvents;
				}

				if (eventsToSave != null && !eventsToSave.equals(MilongaHoyConstants.EMPTY_STRING)) {
					saveServerLastTimeUpdate(uiContext, remoteEvents);
					saveMilongasData(uiContext, eventsToSave);
				}

			} catch (SQLException e) {
				return null;
			}
			return MilongaHoyConstants.SAVE_MILONGAS_SUCCESS;
		}
		return MilongaHoyConstants.EMPTY_STRING;
	}

	private static void saveServerLastTimeUpdate(Context context, String jsonString) {
		//we save the time of the server of each query
		try {
			JSONArray jsonArray = new JSONArray(jsonString);

			JSONObject jsonObject = jsonArray.getJSONObject(0);
			String serverLastUpdateTime = (String) jsonObject.get(MilongaHoyConstants.SERVER_LAST_UPDATE_TIME);
			SharedPreferencesHelper.setValueSharedPreferences(context, MilongaHoyConstants.SERVER_LAST_UPDATE_TIME, serverLastUpdateTime);
		} catch (JSONException e) {

		}
	}

	private String updateLocalEvents(List<EventDTO> localEventsDTO, String remoteEvents) {


		List<EventDTO> remoteEventsDTOs = GsonHelper.parseJsonToArrayListEntity(remoteEvents, new TypeToken<List<EventDTO>>() {
		}.getType());
		String resultLocalEvents = MilongaHoyConstants.EMPTY_STRING;

		if (remoteEventsDTOs != null && localEventsDTO != null) {
			for (EventDTO remoteEventDTO : remoteEventsDTOs) {
				int index = localEventsDTO.indexOf(remoteEventDTO);
				if (index != -1) {
					localEventsDTO.set(index, remoteEventDTO);
				} else {
					localEventsDTO.add(remoteEventDTO);
				}
			}
			sortList(remoteEvents);
			resultLocalEvents = GsonHelper.parseEntityToJson(localEventsDTO);
		}

		return resultLocalEvents;
	}


	protected synchronized List<EventDTO> populateEventsFromDatabase(final Context uiContext) {

		List<EventDTO> eventDTOs = null;
		String jsonString = getAllMilongas(uiContext);
		if (!jsonString.equals(MilongaHoyConstants.EMPTY_STRING)) {
			Type listType = new TypeToken<List<EventDTO>>() {
			}.getType();

			eventDTOs = GsonHelper.parseJsonToArrayListEntity(jsonString, listType);
		}

		if (eventDTOs == null) {
			eventDTOs = new ArrayList<EventDTO>();
		}
		return eventDTOs;
	}


	public static boolean hasRecentlyManuallyUpdated(Context context) {
		Boolean result = false;
		try {
			String strLastManuallyUpdatedDate = SharedPreferencesHelper.getValueInSharedPreferences(context, MilongaHoyConstants.LAST_MANUALLY_UPDATE_DATE);
			if (!strLastManuallyUpdatedDate.equals(MilongaHoyConstants.EMPTY_STRING)) {
				Date lastManuallyUpdatedDate = DateUtils.getDateAndTimeFromString(strLastManuallyUpdatedDate);
				Calendar lastManuallyUpdatedCalendar = Calendar.getInstance();
				lastManuallyUpdatedCalendar.setTime(lastManuallyUpdatedDate);
				lastManuallyUpdatedCalendar.add(Calendar.MINUTE, MilongaHoyConstants.MANUALLY_UPDATE_PERIOD);
				Calendar nowCalendar = Calendar.getInstance();
				result = lastManuallyUpdatedCalendar.before(nowCalendar);
			}
		} catch (ParseException e) {
			return result;
		}

		return result;
	}

	private static MilongaDataSource getMilongaDataSource(Context uiContext) {
		if (milongaDataSource == null) {
			milongaDataSource = new MilongaDataSource(uiContext);
		}
		return milongaDataSource;
	}

	public void sortList(List<EventDTO> eventDTOs) {
		Collections.sort(eventDTOs);
	}

	public String sortList(String jsonString) {
		List<EventDTO> eventDTOs = GsonHelper.parseJsonToArrayListEntity(jsonString, new TypeToken<List<EventDTO>>() {
		}.getType());
		Collections.sort(eventDTOs);
		return GsonHelper.parseEntityToJson(eventDTOs);
	}

	private String getAllMilongas(Context uiContext) {
		getMilongaDataSource(uiContext).open();
		String jsonString = getMilongaDataSource(uiContext).getAllMilongas();
		getMilongaDataSource(uiContext).close();

		return jsonString;
	}

	public synchronized static String syncAndSaveEvents(String url, Context uiContext) {

		String jsonResult = RestClient.executeHttpGetRequest(url);
		jsonResult = GsonHelper.parseResponse(jsonResult);
		if (jsonResult != null) {
			jsonResult = EventsService.getInstance().sortList(jsonResult);
			saveServerLastTimeUpdate(uiContext, jsonResult);
			EventsService.saveMilongasData(uiContext, jsonResult);
		}

		return jsonResult;
	}

	public synchronized static String getParametersDTO() {
		return MilongaHoyConstants.SYNC_EVENTS_URL.concat(ParametersDTO.getDailyRefreshParameters());
	}
}
