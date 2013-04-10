package com.hoy.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.hoy.R;
import com.hoy.constants.MilongaHoyConstants;
import com.hoy.dto.EventDTO;

import java.util.List;

/**
 * @author Ksairi
 * @date 4/27/12 1:37
 */
public class EventListAdapter extends ArrayAdapter<EventDTO> {

	private final Context context;
	private final List<EventDTO> eventsDTO;
	private Boolean todayEvents;

	static class ViewHolder {
		public TextView eventName;
		public TextView appointmentDate;
		public TextView beginTime;
	}

	public EventListAdapter(Context context, List<EventDTO> eventsDTO, Boolean todayEvents) {
			super(context, R.layout.event_list_item, eventsDTO);
			this.context = context;
			this.eventsDTO = eventsDTO;
			this.todayEvents = todayEvents;
		}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View rowView = convertView;
		if (rowView == null) {

			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			rowView = inflater.inflate(R.layout.event_list_item, null);
			ViewHolder viewHolder = new ViewHolder();

			viewHolder.eventName = (TextView) rowView.findViewById(R.id.event_name);
			viewHolder.appointmentDate = (TextView) rowView.findViewById(R.id.event_appointment_date);
			viewHolder.beginTime = (TextView) rowView.findViewById(R.id.event_begin_time);

			rowView.setTag(viewHolder);

		}

		ViewHolder holder = (ViewHolder) rowView.getTag();
		String eventName = "";
		String appointmentDate = "";
		String beginTime = "";

		if (eventsDTO != null && !eventsDTO.isEmpty()) {
			eventName = eventsDTO.get(position).getName();
			beginTime = eventsDTO.get(position).getStartTime();
			if(todayEvents != null && !todayEvents){
				appointmentDate = eventsDTO.get(position).getDate();
			}
		}

		holder.eventName.setText(eventName);
		holder.appointmentDate.setText(appointmentDate);
		holder.beginTime.setText(beginTime);

		return rowView;
	}
}
