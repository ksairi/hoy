package com.hoy.activities;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;
import com.hoy.R;
import com.hoy.constants.MilongaHoyConstants;
import com.hoy.dto.EventDTO;
import com.hoy.helpers.GsonHelper;

/**
 * Created with IntelliJ IDEA.
 * User: ksairi
 * Date: 3/22/13
 * Time: 8:27 PM
 * To change this template use File | Settings | File Templates.
 */
public class EventDetails extends GenericActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);    //To change body of overridden methods use File | Settings | File Templates.
		setContentView(R.layout.event_details);
		String extras = getIntent().getExtras().getString(MilongaHoyConstants.EVENT_DTO);
		EventDTO eventDTO = GsonHelper.parseJsonToEntity(extras, EventDTO.class);
		setEventData(eventDTO);
		getIntent().getExtras().remove(MilongaHoyConstants.EVENT_DTO);
	}

	private void setEventData(EventDTO eventDTO){
		TextView eventName = (TextView)findViewById(R.id.event_name_title);
		TextView eventCancelled = (TextView)findViewById(R.id.event_cancelled);
		TextView eventDate = (TextView)findViewById(R.id.event_date);
		TextView startTime = (TextView)findViewById(R.id.start_time);
		TextView endTime = (TextView)findViewById(R.id.end_time);
		TextView eventGenre = (TextView)findViewById(R.id.event_genre);
		TextView eventArea = (TextView)findViewById(R.id.event_area);
		TextView eventAddress = (TextView)findViewById(R.id.event_address);
		TextView nameOfPlace = (TextView)findViewById(R.id.name_of_place);
		TextView howToGetThere = (TextView)findViewById(R.id.howToGetThere);
		TextView phoneNumber = (TextView)findViewById(R.id.phone_number);
		TextView emailAddress = (TextView)findViewById(R.id.email_address);

		eventName.setText(eventDTO.getName());
		if(eventDTO.getEventCancelledFlag()){
			eventCancelled.setText(R.string.event_cancelled);
			eventCancelled.setTextColor(Color.RED);
		}
		eventDate.setText(eventDTO.getDate());
		startTime.setText(eventDTO.getStartTime());
		endTime.setText(eventDTO.getEndTime());
		eventGenre.setText(eventDTO.getGenre());
		eventArea.setText(eventDTO.getFamiliarNameOfArea());
		eventAddress.setText(eventDTO.getStreetLine1());
		nameOfPlace.setText(eventDTO.getNameOfPlace());
		howToGetThere.setText(eventDTO.getHowToGetThere());
		phoneNumber.setText(eventDTO.getPhones());
		emailAddress.setText(eventDTO.getEmailContact());


	}

	@Override
	protected Context getContext() {
		return this;  //To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	protected Activity getActivity() {
		return this;  //To change body of implemented methods use File | Settings | File Templates.
	}


}
