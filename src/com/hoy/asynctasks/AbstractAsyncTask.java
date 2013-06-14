package com.hoy.asynctasks;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.widget.Toast;
import com.google.gson.reflect.TypeToken;
import com.hoy.R;
import com.hoy.dto.EventDTO;
import com.hoy.dto.message.MessageDTO;
import com.hoy.helpers.GsonHelper;
import com.hoy.services.EventsService;
import com.hoy.utilities.RestClient;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Encargada de generalizar todo el comportamiento que prepara los datos para enviar al servicio remoto y recibir su respuesta.
 * Tambien se encarga de manejar potenciales errores de servidor remoto.
 *
 * @param <T>
 * @author MKsairi, LDicesaro
 */
public abstract class AbstractAsyncTask<T> extends AsyncTask<String, Void, String> {

	private static final String TAG = AbstractAsyncTask.class.getSimpleName();

	protected Context uiContext;
	protected T paramEntity;
	protected List<EventDTO> eventDTOs;
	protected final String SUCCESS = "success";

	protected String doInBackground(String... urls) {
		String jsonResult = RestClient.executeHttpGetRequest(getUrl());
		jsonResult = GsonHelper.parseResponse(jsonResult);
		if(jsonResult != null){
			jsonResult = EventsService.getInstance().sortList(jsonResult);
			EventsService.saveMilongasData(uiContext, jsonResult);
			eventDTOs = GsonHelper.parseJsonToArrayListEntity(jsonResult,getType());
			return SUCCESS;
		}
		else{
			return null;
		}

	}

	// Ejemplo de como mandarlo por GET.
//	protected String doInBackground(String... urls) {
//		String params = "?username=" + userDTO.getUsername() + "&password=" + userDTO.getPassword();
//		return RestClient.executeHttpGetRequest(RestaUnoConstants.HOST + RestaUnoUrlProtocol.LOGIN_URL + params, null);
//	}

	protected void onPostExecute(String result) {

		if (result != null && result.equals(SUCCESS)) {
			doOnSuccess();
		} else {
			if (!isApplicationBroughtToBackground()) {
				Toast.makeText(uiContext, R.string.connection_errors, Toast.LENGTH_LONG).show();
			}
			doOnError();
		}
	}

	protected T getBodyDTO() {
		return paramEntity;
	}

	protected abstract String getUrl();

	protected abstract void doOnSuccess();

	protected abstract void doOnError();


	protected MessageDTO<T> getMessageDTO() {

		/*MessageDTO<T> messageDTO = new MessageDTO<T>();
		HeaderDTO headerDTO = new HeaderDTO();
		headerDTO.setDeviceId(InstallationUtils.id(uiContext));
		messageDTO.setHeaderDTO(headerDTO);*/
		return new MessageDTO<T>();
	}

	/**
	 * This will return whatever strings.xml uniquely identifies the device (IMEI on GSM, MEID for CDMA).
	 *
	 * @return
	 */
	protected String getDeviceID() {
		TelephonyManager telephonyManager = (TelephonyManager) uiContext.getSystemService(Context.TELEPHONY_SERVICE);
		return telephonyManager.getDeviceId();
	}

	protected String getMacAddress() {
		WifiManager wifiMan = (WifiManager) uiContext.getSystemService(Context.WIFI_SERVICE);
		WifiInfo wifiInf = wifiMan.getConnectionInfo();
		return wifiInf.getMacAddress();
	}

	protected String getAndroidId() {
		return Secure.getString(uiContext.getContentResolver(), Secure.ANDROID_ID);
	}

	/**
	 * Checks if the top running activity on the device belongs to application,
	 * by comparing package names.
	 * Fuente: http://stackoverflow.com/questions/4414171/how-to-detect-when-an-android-app-goes-to-the-background-and-come-back-to-the-fo
	 *
	 * @return
	 */
	private boolean isApplicationBroughtToBackground() {
		ActivityManager am = (ActivityManager) uiContext.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> tasks = am.getRunningTasks(1);
		if (!tasks.isEmpty()) {
			ComponentName topActivity = tasks.get(0).topActivity;
			if (!topActivity.getPackageName().equals(uiContext.getPackageName())) {
				return true;
			}
		}

		return false;
	}


			protected Type getType() {
				Type listType = new TypeToken<List<EventDTO>>() {
				}.getType();
				return listType;
			}

/*
	String jsonString = GsonHelper.parseEntityToJson(messageDTO);

	// Ejemplo de la utilizacion de JSON Objects
	JSONObject jsonEvent = null;
	try {
		// JSON object to hold the information, which is sent to the server
		jsonEvent = new JSONObject(jsonString);

		// Add a nested JSONObject (e.g. for header information)
		JSONObject jsonHeader = new JSONObject();
		JSONObject header = new JSONObject();
		header.put("deviceType", "Android"); // Device type
		header.put("deviceVersion", "2.0"); // Device OS version
		header.put("language", "es-es"); // Language of the Android client
		jsonHeader.put("header", header);
		jsonHeader.put("body", jsonEvent);

		// Output the JSON object we're sending to Logcat:
		Log.i(TAG, jsonEvent.toString(2));
	} catch (JSONException e) {
		e.printStackTrace();
	}

	// Send the HttpPostRequest and receive a JSONObject in return
	return RestClient.executeHttpPostRequest(RestaUnoConstants.HOST + RestaUnoUrlProtocol.SAVE_EVENT_URL, jsonString);
*/
}
