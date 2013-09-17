package com.hoy.asynctasks;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.FragmentManager;
import com.hoy.asynctasks.interfaces.GenericSuccessListHandleable;
import com.hoy.constants.MilongaHoyConstants;
import com.hoy.dto.EventDTO;
import com.hoy.dto.ParametersDTO;
import com.hoy.fragments.ProgressDialogFragment;
import com.hoy.helpers.FragmentHelper;
import com.hoy.helpers.SharedPreferencesHelper;
import com.hoy.model.FilterParams;
import com.hoy.model.PromoImg;
import com.hoy.services.EventsService;
import com.hoy.services.ImageService;
import com.hoy.utilities.DateUtils;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: ksairi
 * Date: 6/25/13
 * Time: 4:17 PM
 * To change this template use File | Settings | File Templates.
 */
public class GetInitialContentAsyncTask extends AsyncTask<String, Void, String> {

    private Context context;
    private PromoImg promoImg;
    private static String SUCCESS = "success";
    private GenericSuccessListHandleable<EventDTO> genericSuccessListHandleable;
    private FragmentManager fragmentManager;
    private ProgressDialogFragment progressDialogFragment;
    private List<EventDTO> eventDTOs;

    public GetInitialContentAsyncTask(Context context, PromoImg promoImg, FragmentManager fragmentManager, GenericSuccessListHandleable<EventDTO> genericSuccessListHandleable) {
        this.context = context;
        this.genericSuccessListHandleable = genericSuccessListHandleable;
        this.promoImg = promoImg;
        this.fragmentManager = fragmentManager;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialogFragment = FragmentHelper.showProgressDialog(fragmentManager);
    }

    @Override
    protected String doInBackground(String... strings) {

        String savedLastUpdateString = SharedPreferencesHelper.getValueInSharedPreferences(context, MilongaHoyConstants.LAST_FULL_UPDATE_DATE);
        try {
            Date savedLastUpdate = null;
            Date now = null;
            if (!savedLastUpdateString.equals(MilongaHoyConstants.EMPTY_STRING)) {
                savedLastUpdate = DateUtils.getDateFromString(savedLastUpdateString);
                now = DateUtils.getTodayCleanTime().getTime();
            }

            Boolean deltaUpdate = !savedLastUpdateString.equals(MilongaHoyConstants.EMPTY_STRING) && (now != null && savedLastUpdate != null && !now.after(savedLastUpdate) && !now.before(savedLastUpdate));
            String parametersDTO = deltaUpdate ? (ParametersDTO.getHourlyRefreshParameters(context)) : ParametersDTO.getDailyRefreshParameters();

            String jsonEvents = EventsService.getInstance().synchronizeEventsFromServer(context, MilongaHoyConstants.SYNC_EVENTS_URL, parametersDTO, deltaUpdate);

            ImageService.syncAndSavePromoImgs(promoImg, context);

            if (jsonEvents != null) {
                SharedPreferencesHelper.setValueSharedPreferences(context, MilongaHoyConstants.LAST_FULL_UPDATE_DATE, DateUtils.getTodayDateToShow());
                eventDTOs = EventsService.getInstance().getFilteredEventDTOs(context, new FilterParams(DateUtils.getTodayString()));
                return SUCCESS;
            }
            return null;
        } catch (ParseException e) {
            return null;
        }
    }

    @Override
    protected void onPostExecute(String responseString) {
        super.onPostExecute(responseString);
        fragmentManager.beginTransaction().remove(progressDialogFragment).commitAllowingStateLoss();
        if (responseString != null && responseString.equals(SUCCESS)) {

            genericSuccessListHandleable.handleSuccessCallBack(eventDTOs);

        } else {
            genericSuccessListHandleable.handleErrorCallBack(eventDTOs);
        }

    }


}

