package com.hoy.activities;

import android.content.Context;
import android.os.Bundle;
import com.hoy.R;
import com.hoy.constants.MilongaHoyConstants;
import com.hoy.dto.EventDTO;
import com.hoy.fragments.EventDetailFragment;
import com.hoy.schedulers.EventsScheduler;

/**
 * Created with IntelliJ IDEA.
 * User: ksairi
 * Date: 3/22/13
 * Time: 8:27 PM
 * To change this template use File | Settings | File Templates.
 */
public class EventDetailsActivity extends GenericActivity {


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);    //To change body of overridden methods use File | Settings | File Templates.
		setContentView(R.layout.activity_event_detail);
		setPromoImage();
		EventDTO eventDTO = getIntent().getExtras().getParcelable(MilongaHoyConstants.EVENT_DTO);
		getIntent().getExtras().remove(MilongaHoyConstants.EVENT_DTO);
		EventDetailFragment eventDetailFragment = (EventDetailFragment) getSupportFragmentManager().findFragmentById(R.id.event_details_fragment);
		eventDetailFragment.setEventProperties(eventDTO);
	}

	@Override
	protected Context getContext() {
		return this;  //To change body of implemented methods use File | Settings | File Templates.
	}

	private void setPromoImage(){

		changePromoImgtask = EventsScheduler.startChangePromoImgTask(this,changePromoImgHandler);
	}

	@Override
	public void onBackPressed() {
		if(changePromoImgHandler != null){
			changePromoImgtask.cancel(true);
		}
		finish();
	}
}