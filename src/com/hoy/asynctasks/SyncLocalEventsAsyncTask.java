package com.hoy.asynctasks;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.FragmentManager;
import com.hoy.asynctasks.interfaces.GenericSuccessListHandleable;
import com.hoy.dto.EventDTO;
import com.hoy.fragments.ProgressDialogFragment;
import com.hoy.helpers.FragmentHelper;
import com.hoy.model.FilterParams;
import com.hoy.services.EventsService;

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

    private FragmentManager fragmentManager;
    private ProgressDialogFragment progressDialogFragment;

    public SyncLocalEventsAsyncTask(Context uiContext, FilterParams filterParams, FragmentManager fragmentManager, GenericSuccessListHandleable<EventDTO> genericSuccessListHandleable) {
        this.uiContext = uiContext;
        this.filterParams = filterParams;
        this.genericSuccessListHandleable = genericSuccessListHandleable;
        this.fragmentManager = fragmentManager;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialogFragment = FragmentHelper.showProgressDialog(fragmentManager);
    }

    protected String doInBackground(String... urls) {
        eventDTOs = EventsService.getInstance().getFilteredEventDTOs(uiContext, filterParams);
        if (eventDTOs != null) {
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
        FragmentHelper.hideProgressDialog(progressDialogFragment);
        if (resultString != null && resultString.equals(RESULT_OK)) {
            genericSuccessListHandleable.handleSuccessCallBack(eventDTOs);
        } else {
            genericSuccessListHandleable.handleErrorResult();
        }
    }
}


