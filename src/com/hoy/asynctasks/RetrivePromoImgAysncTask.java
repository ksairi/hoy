package com.hoy.asynctasks;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import com.hoy.asynctasks.interfaces.GenericSuccessImgHandleable;
import com.hoy.utilities.ImgRestClient;

/**
 * Created with IntelliJ IDEA.
 * User: ksairi
 * Date: 6/4/13
 * Time: 6:40 PM
 * To change this template use File | Settings | File Templates.
 */
public class RetrivePromoImgAysncTask extends AsyncTask<String, Void, String> {

	private Bitmap remoteImgBitMap;
	private static String SUCCESS ="success";
	private GenericSuccessImgHandleable genericSuccessImgHandleable;
	private String imgUrl;

	public RetrivePromoImgAysncTask(String imgUrl, GenericSuccessImgHandleable genericSuccessImgHandleable) {
		this.genericSuccessImgHandleable = genericSuccessImgHandleable;
		this.imgUrl = imgUrl;
	}

	@Override
	protected String doInBackground(String... strings) {
		remoteImgBitMap = ImgRestClient.getImage(imgUrl);
		if(remoteImgBitMap != null){
			return SUCCESS;
		}
		return null;
	}

	@Override
	protected void onPostExecute(String responseString) {
		super.onPostExecute(responseString);    //To change body of overridden methods use File | Settings | File Templates.
		if(responseString!= null && responseString.equals(SUCCESS)){

		genericSuccessImgHandleable.handleSuccessCallBack(remoteImgBitMap);

		}
		genericSuccessImgHandleable.handleErrorResult();

	}
}
