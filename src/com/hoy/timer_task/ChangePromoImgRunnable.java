package com.hoy.timer_task;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import com.hoy.constants.MilongaHoyConstants;
import com.hoy.helpers.SharedPreferencesHelper;
import com.hoy.services.ImageService;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: ksairi
 * Date: 6/5/13
 * Time: 11:03 PM
 * To change this template use File | Settings | File Templates.
 */
public class ChangePromoImgRunnable extends AbstractRunnable {

	protected Boolean getNextPromoImg;
	public ChangePromoImgRunnable(Context context, Handler handler, Boolean getNextPromoImg) {
		this.context = context;
		this.handler = handler;
		this.getNextPromoImg = getNextPromoImg;
	}

	@Override
	public void run() {

		//if(isActivityInForeGround()){

		Integer promoImgIndex;
		try{
			promoImgIndex =  Integer.parseInt(SharedPreferencesHelper.getValueInSharedPreferences(context,MilongaHoyConstants.CURRENT_IMG_PROMO_INDEX));
		}catch (NumberFormatException e){
			promoImgIndex = -1;

		}
		String[] result;
		if(getNextPromoImg){

			result =  ImageService.getNextPromoImgDataByIndex(context, promoImgIndex);
		}else{
			result =  ImageService.getPromoImgDataByIndex(context, promoImgIndex);
		}

		Message message = new Message();
		Bundle bundle = new Bundle();
		bundle.putStringArray(MilongaHoyConstants.PROMO_IMG_DATA,result);
		message.setData(bundle);
		handler.sendMessage(message);


	}
		//}



	private boolean isActivityInForeGround() {
			ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
			List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
			if (!tasks.isEmpty()) {
				ComponentName topActivity = tasks.get(0).topActivity;
				return topActivity.getClassName().equals(context.getClass().getName());
			}

			return false;
		}


	@Override
	protected String getParams() {
		return null;  //To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	protected String getUrl() {
		return null;  //To change body of implemented methods use File | Settings | File Templates.
	}


}
