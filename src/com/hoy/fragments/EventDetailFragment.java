package com.hoy.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.google.analytics.tracking.android.Tracker;
import com.hoy.R;
import com.hoy.activities.GoogleMapActivity;
import com.hoy.asynctasks.GetMilongaImgAsyncTask;
import com.hoy.asynctasks.interfaces.GenericSuccessImgHandleable;
import com.hoy.constants.MilongaHoyConstants;
import com.hoy.dto.EventDTO;
import com.hoy.helpers.AddressHelper;
import com.hoy.helpers.AnalyticsHelper;
import com.hoy.helpers.ImageHelper;

import java.util.BitSet;
import java.util.Locale;

/**
 * Created with IntelliJ IDEA.
 * User: ksairi
 * Date: 5/22/13
 * Time: 11:25 AM
 * To change this template use File | Settings | File Templates.
 */
public class EventDetailFragment extends Fragment {

    private EventDTO eventDTO;
    Tracker tracker;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_event_details, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle arguments = getArguments();
        ImageButton googleMap = (ImageButton) getActivity().findViewById(R.id.google_map);
        googleMap.setOnClickListener(googleMapOnClickListener);
        tracker = AnalyticsHelper.getDefaultTracker(getActivity().getApplicationContext());
        if (arguments != null) {
            EventDTO eventDTO = arguments.getParcelable(MilongaHoyConstants.EVENT_DTO);
            setEventProperties(eventDTO);
        }
    }

    public void setEventProperties(EventDTO eventDTO) {

        if(tracker != null && !MilongaHoyConstants.DEBUG){
            tracker.sendEvent(MilongaHoyConstants.ANALYTICS_SCREEN_EVENTS_CATEGORY, MilongaHoyConstants.ANALYTICS_OPEN_SCREEN_ACTION, MilongaHoyConstants.ANALYTICS_OPEN_DETAIL_EVENT_LABEL, null);
        }

        this.eventDTO = eventDTO;
        String currentLocale = Locale.getDefault().getDisplayName();
        getActivity().findViewById(R.id.milonga_image).setVisibility(View.GONE);
        new GetMilongaImgAsyncTask(getActivity(),eventDTO.getMilongaID(),new GenericSuccessImgHandleable() {
            @Override
            public void handleSuccessCallBack(Bitmap bitmap) {
                getActivity().findViewById(R.id.milonga_image).setVisibility(View.VISIBLE);
                ((ImageView)getActivity().findViewById(R.id.milonga_image)).setImageBitmap(bitmap);
            }

            @Override
            public void handleErrorResult() {

            }
        }).execute();

        if (eventDTO.getName() != null) {
            TextView eventName = (TextView) getActivity().findViewById(R.id.event_name_title);
            eventName.setText(eventDTO.getName());
        } else {
            ((View) getActivity().findViewById(R.id.event_name_title).getParent()).setVisibility(View.GONE);
        }

        if (eventDTO.getEventCancelledFlag() != null && eventDTO.getEventCancelledFlag().equals(MilongaHoyConstants.TRUE)) {
            getActivity().findViewById(R.id.event_cancelled_detail).setVisibility(View.VISIBLE);
        } else {
            getActivity().findViewById(R.id.event_cancelled_detail).setVisibility(View.GONE);
        }

        if ((eventDTO.getDetails() != null && !eventDTO.getDetails().equals(MilongaHoyConstants.EMPTY_STRING)) || (eventDTO.getDetails_en() != null && !eventDTO.getDetails_en().equals(MilongaHoyConstants.EMPTY_STRING))) {

            ((View) getActivity().findViewById(R.id.event_details_label).getParent()).setVisibility(View.VISIBLE);
            TextView eventDetails = (TextView) getActivity().findViewById(R.id.event_details_value);
            if(currentLocale.equals(MilongaHoyConstants.LOCALE_SPANISH)){
                eventDetails.setText(eventDTO.getDetails());
            }
            else{
                    if(eventDTO.getDetails_en() != null){
                        eventDetails.setText(eventDTO.getDetails_en());
                    }
                    else{
                        eventDetails.setText(eventDTO.getDetails());
                    }
                }
        } else {
            ((View) getActivity().findViewById(R.id.event_details_label).getParent()).setVisibility(View.GONE);
        }

        if (eventDTO.getDateToShow() != null && !eventDTO.getDateToShow().equals(MilongaHoyConstants.EMPTY_STRING)) {
            TextView eventDate = (TextView) getActivity().findViewById(R.id.event_date);
            eventDate.setText(eventDTO.getDateToShow());
        }

        if (eventDTO.getStartTime() != null && !eventDTO.getStartTime().equals(MilongaHoyConstants.EMPTY_STRING)) {

            TextView startTime = (TextView) getActivity().findViewById(R.id.start_time);
            startTime.setText(eventDTO.getStartTime());
        }

        if (eventDTO.getEndTime() != null && !eventDTO.getEndDateTime().equals(MilongaHoyConstants.EMPTY_STRING)) {
            ((View) getActivity().findViewById(R.id.end_time_label).getParent()).setVisibility(View.VISIBLE);
            TextView endTime = (TextView) getActivity().findViewById(R.id.end_time);
            endTime.setText(eventDTO.getEndTime());
        } else {
            ((View) getActivity().findViewById(R.id.end_time_label).getParent()).setVisibility(View.GONE);
        }

        if (eventDTO.getGenre() != null && !eventDTO.getGenre().equals(MilongaHoyConstants.EMPTY_STRING)) {

            TextView eventGenre = (TextView) getActivity().findViewById(R.id.event_genre);
            eventGenre.setText(eventDTO.getGenre());
        }

        if (eventDTO.getStreetLine1() != null && !eventDTO.getStreetLine1().equals(MilongaHoyConstants.EMPTY_STRING)) {
            String eventAddressString = eventDTO.getStreetLine1();

            TextView eventAddress = (TextView) getActivity().findViewById(R.id.event_address);

            if (eventDTO.getFamiliarNameOfArea() != null && !eventDTO.getFamiliarNameOfArea().equals(MilongaHoyConstants.EMPTY_STRING)) {
                eventAddressString = eventAddressString.concat(" ").concat(eventDTO.getFamiliarNameOfArea());
            }
            eventAddress.setText(eventAddressString);
        }

        if (eventDTO.getNameOfPlace() != null && !eventDTO.getNameOfPlace().equals(MilongaHoyConstants.EMPTY_STRING)) {
            TextView nameOfPlace = (TextView) getActivity().findViewById(R.id.name_of_place);
            nameOfPlace.setText(eventDTO.getNameOfPlace());
        }

        if (eventDTO.getPrice() != null && !eventDTO.getPrice().equals(MilongaHoyConstants.EMPTY_STRING)) {
            ((View) getActivity().findViewById(R.id.event_price_label).getParent()).setVisibility(View.VISIBLE);
            TextView eventPrice = (TextView) getActivity().findViewById(R.id.event_price);
            eventPrice.setText(eventDTO.getPrice());

        } else {
            ((View) getActivity().findViewById(R.id.event_price_label).getParent()).setVisibility(View.GONE);
        }

        if (eventDTO.getHowToGetThere() != null && !eventDTO.getHowToGetThere().equals(MilongaHoyConstants.EMPTY_STRING)) {
            ((View) getActivity().findViewById(R.id.how_to_get_there).getParent()).setVisibility(View.VISIBLE);
            TextView howToGetThere = (TextView) getActivity().findViewById(R.id.how_to_get_there);
            howToGetThere.setText(eventDTO.getHowToGetThere());
        } else {
            ((View) getActivity().findViewById(R.id.how_to_get_there).getParent()).setVisibility(View.GONE);
        }

        if (eventDTO.getOrganizersNames() != null && !eventDTO.getOrganizersNames().equals(MilongaHoyConstants.EMPTY_STRING)) {
            ((View) getActivity().findViewById(R.id.event_organizers_label).getParent()).setVisibility(View.VISIBLE);
            TextView organizersName = (TextView) getActivity().findViewById(R.id.event_organizers);
            organizersName.setText(eventDTO.getOrganizersNames());

        } else {
            ((View) getActivity().findViewById(R.id.event_organizers_label).getParent()).setVisibility(View.GONE);
        }

        if (eventDTO.getPhones() != null && !eventDTO.getPhones().equals(MilongaHoyConstants.EMPTY_STRING)) {
            ((View) getActivity().findViewById(R.id.phone_number_label).getParent()).setVisibility(View.VISIBLE);
            TextView phoneNumber = (TextView) getActivity().findViewById(R.id.phone_number);
            phoneNumber.setText(eventDTO.getPhones());
        } else {
            ((View) getActivity().findViewById(R.id.phone_number_label).getParent()).setVisibility(View.GONE);
        }

        if (eventDTO.getEmailContact() != null && !eventDTO.getEmailContact().equals(MilongaHoyConstants.EMPTY_STRING)) {
            ((View) getActivity().findViewById(R.id.email_address_label).getParent()).setVisibility(View.VISIBLE);
            TextView emailAddress = (TextView) getActivity().findViewById(R.id.email_address);
            emailAddress.setText(eventDTO.getEmailContact());
        } else {
            ((View) getActivity().findViewById(R.id.email_address_label).getParent()).setVisibility(View.GONE);
        }

        if (eventDTO.getWebsite() != null && !eventDTO.getWebsite().equals(MilongaHoyConstants.EMPTY_STRING)) {
            ((View) getActivity().findViewById(R.id.website_label).getParent()).setVisibility(View.VISIBLE);
            TextView website = (TextView) getActivity().findViewById(R.id.website);
            website.setText(eventDTO.getWebsite());
        } else {
            ((View) getActivity().findViewById(R.id.website_label).getParent()).setVisibility(View.GONE);
        }

        View classIcon = getActivity().findViewById(R.id.class_icon);
        if (eventDTO.getOffersClassFlag() != null && eventDTO.getOffersClassFlag().equals(MilongaHoyConstants.TRUE)) {

            getActivity().findViewById(R.id.class_details).setVisibility(View.VISIBLE);
            classIcon.setVisibility(View.VISIBLE);

            TextView firstClassStartTime = (TextView) getActivity().findViewById(R.id.first_class_start_time);
            TextView lastClassEndTime = (TextView) getActivity().findViewById(R.id.last_class_end_time);
            TextView classDetailsAndPricing = (TextView) getActivity().findViewById(R.id.class_details_and_pricing);
            if(eventDTO.getFirstClassStarts() != null){
                firstClassStartTime.setText(eventDTO.getFirstClassStarts());
            }
            if(eventDTO.getLastClassEnds() != null){
                lastClassEndTime.setText(eventDTO.getLastClassEnds());
            }
            if(eventDTO.getClassContentAndPricingDetails() != null || eventDTO.getClassContentAndPricingDetails_en() != null){
                if(currentLocale.equals(MilongaHoyConstants.LOCALE_SPANISH)){
                    classDetailsAndPricing.setText(eventDTO.getClassContentAndPricingDetails());
                }
                else{
                        if(eventDTO.getClassContentAndPricingDetails_en() != null){
                            classDetailsAndPricing.setText(eventDTO.getClassContentAndPricingDetails_en());
                        }
                        else{
                            classDetailsAndPricing.setText(eventDTO.getClassContentAndPricingDetails());
                        }
                    }
            }

        } else {
            getActivity().findViewById(R.id.class_details).setVisibility(View.GONE);
            classIcon.setVisibility(View.GONE);
        }

        View specialIcon = getActivity().findViewById(R.id.special_event_icon);
        if (eventDTO.getSpecialEventFlag() != null && eventDTO.getSpecialEventFlag().equals(MilongaHoyConstants.FALSE)) {
            specialIcon.setVisibility(View.GONE);
        } else {
            specialIcon.setVisibility(View.VISIBLE);
        }

        TextView reservationAdvised = (TextView) getActivity().findViewById(R.id.reservation_advised);
        if (eventDTO.getReservationAdvicedFlag() != null && eventDTO.getReservationAdvicedFlag().equals(MilongaHoyConstants.TRUE)) {

            ((View) reservationAdvised.getParent()).setVisibility(View.VISIBLE);
        } else {
            ((View) reservationAdvised.getParent()).setVisibility(View.GONE);
        }

        if (specialIcon != null && specialIcon.getVisibility() == View.GONE && classIcon != null && classIcon.getVisibility() == View.GONE) {

            ((View) specialIcon.getParent()).setVisibility(View.GONE);
        } else {
            ((View) specialIcon.getParent()).setVisibility(View.VISIBLE);
        }

        ((ScrollView)getActivity().findViewById(R.id.event_detail_wrapper)).fullScroll(View.FOCUS_UP);

    }


    private View.OnClickListener googleMapOnClickListener = new View.OnClickListener() {
        public void onClick(View view) {

        if(tracker != null && !MilongaHoyConstants.DEBUG){
            tracker.sendEvent(MilongaHoyConstants.ANALYTICS_SCREEN_EVENTS_CATEGORY, MilongaHoyConstants.ANALYTICS_OPEN_SCREEN_ACTION, MilongaHoyConstants.ANALYTICS_OPEN_MILONGA_MAP_LABEL, null);
        }

        Intent intent = new Intent(getActivity(), GoogleMapActivity.class);
        try {
            Double latitude = Double.parseDouble(eventDTO.getLatitude());
            Double longitude = Double.parseDouble(eventDTO.getLongitude());
            String name = eventDTO.getName();
            String eventAddress = AddressHelper.getEventAddress(eventDTO);
            if (name == null) {
                name = MilongaHoyConstants.EMPTY_STRING;
            }
            intent.putExtra(MilongaHoyConstants.EVENT_NAME, name);
            intent.putExtra(MilongaHoyConstants.EVENT_LATITUDE, latitude);
            intent.putExtra(MilongaHoyConstants.EVENT_LONGITUDE, longitude);
            intent.putExtra(MilongaHoyConstants.EVENT_ADDRESS, eventAddress);
            getActivity().startActivity(intent);


        } catch (NumberFormatException e) {
            Toast.makeText(getActivity(), R.string.invalid_map_data, Toast.LENGTH_SHORT);
        }

        }
    };
}
