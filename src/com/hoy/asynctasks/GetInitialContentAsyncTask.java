package com.hoy.asynctasks;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.FragmentManager;
import com.hoy.asynctasks.interfaces.GenericSuccessListHandleable;
import com.hoy.constants.MilongaHoyConstants;
import com.hoy.dto.EventDTO;
import com.hoy.fragments.ProgressDialogFragment;
import com.hoy.helpers.FragmentHelper;
import com.hoy.helpers.SharedPreferencesHelper;
import com.hoy.model.FilterParams;
import com.hoy.model.PromoImg;
import com.hoy.services.EventsService;
import com.hoy.services.ImageService;
import com.hoy.utilities.DateUtils;

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

	//public GetInitialContentAsyncTask(Context context, PromoImg promoImg, FragmentManager fragmentManager, GenericSuccessHandleable genericSuccessHandleable) {
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

		String jsonEvents = EventsService.syncAndSaveEvents(EventsService.getParametersDTO(), context);
		eventDTOs = EventsService.getInstance().getFilteredEventDTOs(context, new FilterParams(DateUtils.getTodayString()));

		String promoImgResult = ImageService.syncAndSavePromoImgs(promoImg, context);

		if (jsonEvents != null && promoImgResult != null) {
			SharedPreferencesHelper.setValueSharedPreferences(context, MilongaHoyConstants.LAST_FULL_UPDATE_DATE, DateUtils.getTodayDateToShow());
			return SUCCESS;
		}
		return null;
	}

	@Override
	protected void onPostExecute(String responseString) {
		super.onPostExecute(responseString);
		FragmentHelper.hideProgressDialog(progressDialogFragment);
		if (responseString != null && responseString.equals(SUCCESS)) {

			genericSuccessListHandleable.handleSuccessCallBack(eventDTOs);

		} else {
			genericSuccessListHandleable.handleErrorCallBack(eventDTOs);
		}

	}


}

