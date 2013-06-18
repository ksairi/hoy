package com.hoy.asynctasks;

import android.content.Context;
import android.os.AsyncTask;
import com.hoy.asynctasks.interfaces.GenericSuccessHandleable;
import com.hoy.helpers.ImageHelper;
import com.hoy.model.PromoImg;
import com.hoy.services.ImageService;
import com.hoy.utilities.RestClient;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: ksairi
 * Date: 6/4/13
 * Time: 6:40 PM
 * To change this template use File | Settings | File Templates.
 */
public class SyncPromoImgsAysncTask extends AsyncTask<String, Void, String> {

	private Context context;
	private PromoImg promoImg;
	private static String SUCCESS ="success";
	private GenericSuccessHandleable genericSuccessHandleable;

	public SyncPromoImgsAysncTask(Context context, PromoImg promoImg, GenericSuccessHandleable genericSuccessHandleable) {
		this.context = context;
		this.genericSuccessHandleable = genericSuccessHandleable;
		this.promoImg = promoImg;
	}

	@Override
	protected String doInBackground(String... strings) {
		String url = ImageHelper.buildUrl(promoImg);
		String jsonString =  RestClient.executeHttpGetRequest(url);
		List<PromoImg> promoImgs= ImageHelper.parseResponse(jsonString, promoImg);
		if(promoImgs != null && ImageService.savePromoImgs(context, promoImgs)!= null){
			return SUCCESS;
		}

		return null;
	}

	@Override
	protected void onPostExecute(String responseString) {
		super.onPostExecute(responseString);
		if(responseString!= null && responseString.equals(SUCCESS)){

			genericSuccessHandleable.handleSuccessCallBack();

		}
		else{
			genericSuccessHandleable.handleErrorResult();
		}

	}


}
