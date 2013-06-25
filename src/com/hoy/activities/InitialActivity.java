package com.hoy.activities;

import android.content.Context;
import android.os.Bundle;
import com.hoy.R;
import com.hoy.asynctasks.GetInitialContentAsyncTask;
import com.hoy.asynctasks.interfaces.GenericSuccessHandleable;
import com.hoy.model.PromoImg;
import com.hoy.schedulers.EventsScheduler;
import com.hoy.services.ImageService;

/**
 * Created with IntelliJ IDEA.
 * User: ksairi
 * Date: 5/29/13
 * Time: 5:51 PM
 * To change this template use File | Settings | File Templates.
 */
public class InitialActivity extends GenericActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.init_layout);
		prepareContent();
	}

	private void prepareContent() {
		final PromoImg promoImg = ImageService.getPromoImg(getContext());
		new GetInitialContentAsyncTask(getContext(), promoImg, getSupportFragmentManager(), new GenericSuccessHandleable() {
			public void handleSuccessCallBack() {
				EventsScheduler.startRetrievePromoImgTask(getContext(), promoImg);
				genericStartActivity(HomeActivity.class, true);
			}

			public void handleErrorResult() {
				EventsScheduler.startRetrievePromoImgTask(getContext(), promoImg);
				genericStartActivity(HomeActivity.class, true);
			}
		}).execute();

	}

	@Override
	protected Context getContext() {
		return this;  //To change body of implemented methods use File | Settings | File Templates.
	}
}
