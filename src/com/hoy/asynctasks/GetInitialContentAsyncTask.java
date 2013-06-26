package com.hoy.asynctasks;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.FragmentManager;
import com.hoy.asynctasks.interfaces.GenericSuccessHandleable;
import com.hoy.fragments.ProgressDialogFragment;
import com.hoy.helpers.FragmentHelper;
import com.hoy.model.PromoImg;
import com.hoy.services.EventsService;
import com.hoy.services.ImageService;

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
	private GenericSuccessHandleable genericSuccessHandleable;
	private FragmentManager fragmentManager;
	private ProgressDialogFragment progressDialogFragment;

	public GetInitialContentAsyncTask(Context context, PromoImg promoImg, FragmentManager fragmentManager, GenericSuccessHandleable genericSuccessHandleable) {
		this.context = context;
		this.genericSuccessHandleable = genericSuccessHandleable;
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
		String promoImgResult = ImageService.syncAndSavePromoImgs(promoImg, context);
		if (jsonEvents != null && promoImgResult != null) {
			return SUCCESS;
		}
		return null;
	}

	@Override
	protected void onPostExecute(String responseString) {
		super.onPostExecute(responseString);
		FragmentHelper.hideProgressDialog(progressDialogFragment);
		if (responseString != null && responseString.equals(SUCCESS)) {

			genericSuccessHandleable.handleSuccessCallBack();

		} else {
			genericSuccessHandleable.handleErrorResult();
		}

	}


}

