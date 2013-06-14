package com.hoy.activities;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import com.hoy.R;
import com.hoy.asynctasks.SyncPromoImgsAysncTask;
import com.hoy.asynctasks.interfaces.GenericSuccessHandleable;
import com.hoy.asynctasks.interfaces.GenericSuccessListHandleable;
import com.hoy.constants.MilongaHoyConstants;
import com.hoy.dto.EventDTO;
import com.hoy.helpers.FragmentHelper;
import com.hoy.helpers.SharedPreferencesHelper;
import com.hoy.model.PromoImg;
import com.hoy.schedulers.EventsScheduler;
import com.hoy.services.EventsService;
import com.hoy.services.ImageService;
import com.hoy.utilities.DateUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: ksairi
 * Date: 5/29/13
 * Time: 5:51 PM
 * To change this template use File | Settings | File Templates.
 */
public class InitialActivity extends GenericActivity{

	@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.init_layout);
			prepareContent();
		}

	private void prepareContent() {


		FragmentHelper.changeProgressDialogState(getSupportFragmentManager(),true);
		EventsService.getInstance().synchronizeEventsFromServer(getContext(), new GenericSuccessHandleable() {
			public void handleSuccessCallBack() {

				SharedPreferencesHelper.setValueSharedPreferences(getContext(), MilongaHoyConstants.LAST_MANUALLY_UPDATE_DATE, DateUtils.getTodayAndTimeString());
				retrieveImgsPromos();

			}

			public void handleErrorResult() {
				FragmentHelper.changeProgressDialogState(getSupportFragmentManager(),false);
				retrieveImgsPromos();
				Toast.makeText(getContext(), R.string.connection_errors, Toast.LENGTH_SHORT).show();
			}
		});
	}

	@Override
	protected Context getContext() {
		return this;  //To change body of implemented methods use File | Settings | File Templates.
	}

	private void retrieveImgsPromos(){

		final PromoImg promoImg = ImageService.getPromoImg(getContext());
		new SyncPromoImgsAysncTask(getContext(), ImageService.getPromoImg(getContext()),new GenericSuccessHandleable(){
			public void handleSuccessCallBack() {
				FragmentHelper.changeProgressDialogState(getSupportFragmentManager(),false);
				EventsScheduler.startRetrievePromoImgTask(getContext(),promoImg);
				genericStartActivity(HomeActivity.class,true);

			}

			public void handleErrorResult() {
				FragmentHelper.changeProgressDialogState(getSupportFragmentManager(),false);
				EventsScheduler.startRetrievePromoImgTask(getContext(),promoImg);
				genericStartActivity(HomeActivity.class,true);
			}
		}).execute();
	}
}
