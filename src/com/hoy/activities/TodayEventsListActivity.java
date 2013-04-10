package com.hoy.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.hoy.R;
import com.hoy.adapters.EventListAdapter;
import com.hoy.dto.EventDTO;
import com.hoy.model.FilterParams;
import com.hoy.services.EventsService;
import com.hoy.utilities.DateUtils;

import java.util.Calendar;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: ksairi
 * Date: 1/4/13
 * Time: 2:29 AM
 * To change this template use File | Settings | File Templates.
 */
public class TodayEventsListActivity extends EventsListActivity {

	protected List<EventDTO> getEventDTOs(){

		FilterParams filterParams = new FilterParams();
		filterParams.setDate(DateUtils.getDateStringFromDate(Calendar.getInstance().getTime()));
		return EventsService.getInstance().getFilteredEventDTOs(getContext(),filterParams);

	}

	protected  void setEventListTitle(){

			((TextView)findViewById(R.id.event_list_title)).setText(R.string.today_list_events_label);
		}

	@Override
	protected Boolean isTodayEvents() {

			return true;
		}

}
