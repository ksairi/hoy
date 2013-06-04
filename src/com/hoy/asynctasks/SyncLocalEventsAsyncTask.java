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
import com.hoy.R;
import com.hoy.asynctasks.interfaces.GenericSuccessListHandleable;
import com.hoy.dto.EventDTO;
import com.hoy.dto.message.MessageDTO;
import com.hoy.helpers.GsonHelper;
import com.hoy.model.FilterParams;
import com.hoy.services.EventsService;
import com.hoy.utilities.RestClient;

import java.util.List;

/**
 * Encargada de generalizar todo el comportamiento que prepara los datos para enviar al servicio remoto y recibir su respuesta.
 * Tambien se encarga de manejar potenciales errores de servidor remoto.
 *
 * @author MKsairi, LDicesaro
 */
public class SyncLocalEventsAsyncTask extends AsyncTask<String, Void, String> {

	private static final String TAG = SyncLocalEventsAsyncTask.class.getSimpleName();

	protected Context uiContext;
	protected FilterParams filterParams;
	protected GenericSuccessListHandleable<EventDTO> genericSuccessListHandleable;
	protected List<EventDTO> eventDTOs;
	protected static final String RESULT_OK = "RESULT_OK";

	public SyncLocalEventsAsyncTask(Context uiContext, FilterParams filterParams, GenericSuccessListHandleable<EventDTO> genericSuccessListHandleable) {
		this.uiContext = uiContext;
		this.filterParams = filterParams;
		this.genericSuccessListHandleable = genericSuccessListHandleable;
	}

	protected String doInBackground(String... urls) {
		eventDTOs = EventsService.getInstance().getFilteredEventDTOs(uiContext, filterParams);
		if(eventDTOs != null){
			return RESULT_OK;
		}
		return null;
	}

	// Ejemplo de como mandarlo por GET.
//	protected String doInBackground(String... urls) {
//		String params = "?username=" + userDTO.getUsername() + "&password=" + userDTO.getPassword();
//		return RestClient.executeHttpGetRequest(RestaUnoConstants.HOST + RestaUnoUrlProtocol.LOGIN_URL + params, null);
//	}

	protected void onPostExecute(String resultString) {
		if (resultString != null && resultString.equals(RESULT_OK)) {
			genericSuccessListHandleable.handleSuccessCallBack(eventDTOs);
		} else {
			genericSuccessListHandleable.handleErrorResult();
			}
	}
}


