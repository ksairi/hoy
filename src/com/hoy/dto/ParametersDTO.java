package com.hoy.dto;

import android.content.Context;
import com.hoy.constants.MilongaHoyConstants;
import com.hoy.helpers.SharedPreferencesHelper;
import com.hoy.utilities.DateUtils;

/**
 * Created with IntelliJ IDEA.
 * User: ksairi
 * Date: 1/2/13
 * Time: 5:33 AM
 * To change this template use File | Settings | File Templates.
 */
public class ParametersDTO {

	private static final String SYNC_EVENTS_SEL_PARAM_NAME = "sel";
	private static final String SYNC_EVENTS_FROM_PARAM_NAME = "from";
	private static final String SYNC_EVENTS_DATE_PARAM_NAME = "where";


	private static String sel = "*,now() as ".concat(MilongaHoyConstants.SERVER_LAST_UPDATE_TIME);
	private static String from = "MEventFull";


	public static String getDailyRefreshParameters() {


		return ("?").concat(SYNC_EVENTS_SEL_PARAM_NAME).concat("=").concat(sel).concat("&").concat(SYNC_EVENTS_FROM_PARAM_NAME).concat("=").concat(from).concat("&").concat(SYNC_EVENTS_DATE_PARAM_NAME).concat("=").concat(getWhereClause());
	}

	public static String getHourlyRefreshParameters(Context context) {

		String result = ("?").concat(SYNC_EVENTS_SEL_PARAM_NAME).concat("=").concat(sel).concat("&").concat(SYNC_EVENTS_FROM_PARAM_NAME).concat("=").concat(from).concat("&").concat(SYNC_EVENTS_DATE_PARAM_NAME).concat("=").concat(getWhereClause());
		if(context != null){
			String lastUpdate = SharedPreferencesHelper.getValueInSharedPreferences(context, MilongaHoyConstants.SERVER_LAST_UPDATE_TIME);
			if (!lastUpdate.equals(MilongaHoyConstants.EMPTY_STRING)) {
				result = result.concat(" AND lastUpdated >=").concat("'").concat(lastUpdate).concat("'");
			}
		}
		return result;

	}

	private static String getWhereClause(){

		String todayString = "'".concat(DateUtils.getTodayString()).concat("'");
		return "date between ".concat(todayString).concat(" AND ADDDATE(").concat(todayString).concat(",7) AND areaId=1");
	}

}
