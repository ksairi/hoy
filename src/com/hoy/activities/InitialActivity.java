package com.hoy.activities;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;
import com.hoy.R;
import com.hoy.asynctasks.GetInitialContentAsyncTask;
import com.hoy.asynctasks.interfaces.GenericSuccessListHandleable;
import com.hoy.constants.MilongaHoyConstants;
import com.hoy.dto.EventDTO;
import com.hoy.model.PromoImg;
import com.hoy.schedulers.EventsScheduler;
import com.hoy.services.ImageService;

import java.util.ArrayList;
import java.util.List;

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
		new GetInitialContentAsyncTask(getContext(), promoImg, getSupportFragmentManager(), new GenericSuccessListHandleable<EventDTO>() {
			public void handleSuccessCallBack(List<EventDTO> eventDTOs) {
				EventsScheduler.startRetrievePromoImgTask(getContext(), promoImg);
				genericStartActivity(HomeActivity.class, MilongaHoyConstants.EVENT_DTOS, (ArrayList) eventDTOs, true);
			}

			public void handleErrorResult() {

			}

			public void handleErrorCallBack(List<EventDTO> eventDTOs) {

				Toast.makeText(getContext(), R.string.connection_errors, Toast.LENGTH_SHORT).show();
				EventsScheduler.startRetrievePromoImgTask(getContext(), promoImg);
				genericStartActivity(HomeActivity.class, MilongaHoyConstants.EVENT_DTOS, (ArrayList) eventDTOs, true);
			}
		}).execute();

	}

	@Override
	protected Context getContext() {
		return this;  //To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		ImageView imageView = (ImageView) findViewById(R.id.init_image);
		imageView.setBackgroundResource(R.drawable.init_image);
	}
}
