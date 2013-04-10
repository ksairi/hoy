package com.hoy.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.hoy.R;
import com.hoy.adapters.EventListAdapter;
import com.hoy.constants.MilongaHoyConstants;
import com.hoy.dto.EventDTO;
import com.hoy.helpers.GsonHelper;
import com.hoy.helpers.SharedPreferencesHelper;
import com.hoy.services.EventsService;

import java.util.ArrayList;
import java.util.List;

public class EventsListActivity extends GenericActivity
{
	protected List<EventDTO> eventDTOs;
	protected ListView lv;

	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.events_list_menu);
		setEventListTitle();
		lv = (ListView)findViewById(R.id.event_list);
		syncEventList();
    }

	@Override
	protected void onStart() {
		super.onStart();    //To change body of overridden methods use File | Settings | File Templates.
		if(lv != null){
			ArrayAdapter<EventDTO> adapter = new EventListAdapter(this, eventDTOs, isTodayEvents());
			lv.setAdapter(adapter);
		}

	}

	protected void syncEventList() {

		changeProgressBarVisibility(View.VISIBLE);
		eventDTOs = getEventDTOs();
		lv.setOnItemClickListener(onEventClickEvent);
		if (eventDTOs == null || eventDTOs.isEmpty()) {
			Toast.makeText(getActivity(), R.string.no_events_to_show, Toast.LENGTH_LONG).show();
		}
		changeProgressBarVisibility(View.GONE);
	}

	protected Boolean isTodayEvents() {

		return false;
	}


	@Override
	protected Context getContext() {
		return this;
	}

	@Override
	protected Activity getActivity() {
		return this;
	}

	protected List<EventDTO> getEventDTOs(){

		return EventsService.getInstance().getFilteredEventDTOs(getContext(),null);
	}

	@Override
	public void onBackPressed() {
		genericStartActivity(MainMenuActivity.class);
	}

	protected  void setEventListTitle(){

		((TextView)findViewById(R.id.event_list_title)).setText(R.string.list_events_label);
	}

	protected AdapterView.OnItemClickListener onEventClickEvent = new AdapterView.OnItemClickListener() {
		public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

			String eventJsonString = GsonHelper.parseEntityToJson(eventDTOs.get(i));
			genericStartActivity(EventDetails.class,MilongaHoyConstants.EVENT_DTO,eventJsonString,false);
		}
	};

}