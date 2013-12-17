package com.hoy.helpers;

import android.content.Context;
import com.google.analytics.tracking.android.GoogleAnalytics;
import com.google.analytics.tracking.android.Tracker;
import com.hoy.constants.MilongaHoyConstants;

/**
 * Created with IntelliJ IDEA.
 * User: ksairi
 * Date: 12/16/13
 * Time: 3:18 PM
 * To change this template use File | Settings | File Templates.
 */
public class AnalyticsHelper {

    public static Tracker getDefaultTracker(Context context){
        GoogleAnalytics googleAnalytics = GoogleAnalytics.getInstance(context);
        //googleAnalytics.setDebug(true);
        return googleAnalytics.getTracker(MilongaHoyConstants.ANALYTICS_TRACK_ID);
    }
}
