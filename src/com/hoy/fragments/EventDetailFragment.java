package com.hoy.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.hoy.R;
import com.hoy.constants.MilongaHoyConstants;
import com.hoy.dto.EventDTO;

/**
 * Created with IntelliJ IDEA.
 * User: ksairi
 * Date: 5/22/13
 * Time: 11:25 AM
 * To change this template use File | Settings | File Templates.
 */
public class EventDetailFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_event_details, container, false);
	}


	public void setEventProperties(EventDTO eventDTO) {

		if(eventDTO.getName() != null){
			TextView eventName = (TextView) getActivity().findViewById(R.id.event_name_title);
			eventName.setText(eventDTO.getName());
		}
		else{
			getActivity().findViewById(R.id.event_name_title).setVisibility(View.GONE);
		}

		if (eventDTO.getEventCancelledFlag().equals(MilongaHoyConstants.TRUE)) {
			TextView eventCancelled = (TextView) getActivity().findViewById(R.id.event_cancelled);
			eventCancelled.setText(R.string.event_cancelled);
			eventCancelled.setTextColor(Color.RED);
		}

		if(eventDTO.getDate() != null){
			TextView eventDate = (TextView) getActivity().findViewById(R.id.event_date);
			eventDate.setText(eventDTO.getDate());
		}
		else{
			getActivity().findViewById(R.id.event_date_label).setVisibility(View.GONE);
		}

		if(eventDTO.getStartTime() != null){
			TextView startTime = (TextView) getActivity().findViewById(R.id.start_time);
			startTime.setText(eventDTO.getStartTime());
		}
		else{
			getActivity().findViewById(R.id.start_time_label).setVisibility(View.GONE);
		}

		if(eventDTO.getEndTime() != null){
			TextView endTime = (TextView) getActivity().findViewById(R.id.end_time);
			endTime.setText(eventDTO.getEndTime());
		}else{
			getActivity().findViewById(R.id.end_time_label).setVisibility(View.GONE);
		}

		if(eventDTO.getGenre() != null){
			TextView eventGenre = (TextView) getActivity().findViewById(R.id.event_genre);
			eventGenre.setText(eventDTO.getGenre());
		}else{
			getActivity().findViewById(R.id.event_genre_label).setVisibility(View.GONE);
		}

		if(eventDTO.getFamiliarNameOfArea() != null){

			TextView eventArea = (TextView) getActivity().findViewById(R.id.event_area);
			eventArea.setText(eventDTO.getFamiliarNameOfArea());
		}
		else{
			getActivity().findViewById(R.id.event_address_label).setVisibility(View.GONE);
		}

		if(eventDTO.getStreetLine1() != null){

			TextView eventAddress = (TextView) getActivity().findViewById(R.id.event_address);
			eventAddress.setText(eventDTO.getStreetLine1());
		}

		if(eventDTO.getNameOfPlace() != null){
			TextView nameOfPlace = (TextView) getActivity().findViewById(R.id.name_of_place);
			nameOfPlace.setText(eventDTO.getNameOfPlace());
		}else{
			getActivity().findViewById(R.id.name_of_place_label).setVisibility(View.GONE);
		}

		if(eventDTO.getHowToGetThere() != null){
			TextView howToGetThere = (TextView) getActivity().findViewById(R.id.how_to_get_there);
			howToGetThere.setText(eventDTO.getHowToGetThere());
		}else{
			getActivity().findViewById(R.id.how_to_get_there_label).setVisibility(View.GONE);
		}

		if(eventDTO.getPhones() != null){
			TextView phoneNumber = (TextView) getActivity().findViewById(R.id.phone_number);
			phoneNumber.setText(eventDTO.getPhones());
		}else{
			getActivity().findViewById(R.id.phone_number_label).setVisibility(View.GONE);
		}

		if(eventDTO.getEmailContact() != null){
			TextView emailAddress = (TextView) getActivity().findViewById(R.id.email_address);
			emailAddress.setText(eventDTO.getEmailContact());
		}else{
			getActivity().findViewById(R.id.email_address_label).setVisibility(View.GONE);
		}

		if(eventDTO.getWebsite() != null){
			TextView website = (TextView) getActivity().findViewById(R.id.website);
			website.setText(eventDTO.getWebsite());
		}else{
			getActivity().findViewById(R.id.website_label).setVisibility(View.GONE);
		}

		if(eventDTO.getOffersClassFlag().equals(MilongaHoyConstants.TRUE)){

			TextView firstClassStartTime = (TextView) getActivity().findViewById(R.id.first_class_start_time);
			TextView lastClassEndTime = (TextView) getActivity().findViewById(R.id.last_class_end_time);
			TextView classDetailsAndPricing = (TextView) getActivity().findViewById(R.id.class_details_and_pricing);
			firstClassStartTime.setText(eventDTO.getFirstClassStarts());
			lastClassEndTime.setText(eventDTO.getLastClassEnds());
			classDetailsAndPricing.setText(eventDTO.getClassContentAndPricingDetails());
		}
		else{
			getActivity().findViewById(R.id.class_details).setVisibility(View.GONE);
			getActivity().findViewById(R.id.class_icon).setVisibility(View.GONE);
		}

		if(eventDTO.getSpecialEventFlag().equals(MilongaHoyConstants.FALSE)){
			getActivity().findViewById(R.id.special_event_icon).setVisibility(View.GONE);
		}

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {

		super.onActivityCreated(savedInstanceState);
		Bundle arguments = getArguments();
		if (arguments != null) {
			EventDTO eventDTO = arguments.getParcelable(MilongaHoyConstants.EVENT_DTO);
			setEventProperties(eventDTO);
		}
	}
}
