package com.hoy.helpers;

import android.content.Context;
import com.hoy.constants.MilongaHoyConstants;
import com.hoy.schedulers.EventsScheduler;
import com.hoy.utilities.DateUtils;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: ksairi
 * Date: 1/5/13
 * Time: 2:04 AM
 * To change this template use File | Settings | File Templates.
 */
public class EventHelper {

	public static Boolean hasLocalData(Context context) {
			return !SharedPreferencesHelper.getValueInSharedPreferences(context, MilongaHoyConstants.SAVED_EVENTS_LIST).equals(MilongaHoyConstants.EMPTY_STRING);
	}

	public static boolean hasRecentlyManuallyUpdated(Context context){
		Boolean result = false;
		try{
			String strLastManuallyUpdatedDate = SharedPreferencesHelper.getValueInSharedPreferences(context, MilongaHoyConstants.LAST_MANUALLY_UPDATE_DATE);
			if(!strLastManuallyUpdatedDate.equals(MilongaHoyConstants.EMPTY_STRING)){
				Date lastManuallyUpdatedDate = DateUtils.getDateAndTimeFromString(strLastManuallyUpdatedDate);
				Calendar lastManuallyUpdatedCalendar =  Calendar.getInstance();
				lastManuallyUpdatedCalendar.setTime(lastManuallyUpdatedDate);
				lastManuallyUpdatedCalendar.add(Calendar.HOUR, MilongaHoyConstants.MANUALLY_UPDATE_PERIOD);
				Calendar nowCalendar = Calendar.getInstance();
				result = lastManuallyUpdatedCalendar.before(nowCalendar);
			}
		}catch (ParseException e){
				return result;
		}

		return result;
	}

}