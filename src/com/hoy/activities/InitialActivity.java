package com.hoy.activities;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import com.hoy.R;
import com.hoy.asynctasks.interfaces.GenericErrorHandleable;
import com.hoy.asynctasks.interfaces.GenericSuccessHandleable;
import com.hoy.constants.MilongaHoyConstants;
import com.hoy.helpers.FragmentHelper;
import com.hoy.helpers.SharedPreferencesHelper;
import com.hoy.schedulers.EventsScheduler;
import com.hoy.services.EventsService;
import com.hoy.utilities.DateUtils;

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
			prepareContent();
		}

	private void prepareContent() {


		FragmentHelper.changeProgressDialogState(getSupportFragmentManager(),true);
		EventsService.getInstance().synchronizeEventsFromServer(getContext(), new GenericSuccessHandleable() {
			public void handleSuccessCallBack() {
				FragmentHelper.changeProgressDialogState(getSupportFragmentManager(),false);
				EventsScheduler.startSyncEventsTasks(getContext(), 1);
				SharedPreferencesHelper.setValueSharedPreferences(getContext(), MilongaHoyConstants.LAST_MANUALLY_UPDATE_DATE, DateUtils.getTodayAndTimeString());
				genericStartActivity(HomeActivity.class, true);
			}

			public void handleErrorResult() {
				FragmentHelper.changeProgressDialogState(getSupportFragmentManager(),false);
				EventsScheduler.startSyncEventsTasks(getContext(), 1);
				genericStartActivity(HomeActivity.class,true);
				Toast.makeText(getContext(), R.string.connection_errors, Toast.LENGTH_SHORT);
			}
		});
	}

	@Override
	protected Context getContext() {
		return this;  //To change body of implemented methods use File | Settings | File Templates.
	}

}
