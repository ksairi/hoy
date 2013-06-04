package com.hoy.timer_task;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.google.gson.reflect.TypeToken;
import com.hoy.R;
import com.hoy.activities.HomeActivity;
import com.hoy.constants.MilongaHoyConstants;
import com.hoy.dto.EventDTO;
import com.hoy.dto.ParametersDTO;
import com.hoy.helpers.GsonHelper;
import com.hoy.services.EventsService;

import java.util.List;

public class SyncEventsHourlyTimerTask extends SyncEventsTimerTask {

	private static final String TAG = SyncEventsHourlyTimerTask.class.getSimpleName();

	public SyncEventsHourlyTimerTask(Context context) {
		this.context = context;
	}

	protected void createNotification() {

		NotificationManager notificationManager = (NotificationManager)
				context.getSystemService(Context.NOTIFICATION_SERVICE);

		// Create Notifcation

		Notification notification = new Notification(R.drawable.launch_icon, context.getResources().getText(R.string.news_on_events)
				, System.currentTimeMillis());

		// Cancel the notification after its selected
		notification.flags |= Notification.FLAG_AUTO_CANCEL;

		//DEFAULT SOUND
		notification.defaults |= Notification.DEFAULT_SOUND;

		// Specify the called Activity
		Intent intent = new Intent(context, HomeActivity.class);

		PendingIntent activity = PendingIntent.getActivity(context, 0, intent, 0);

		notification.setLatestEventInfo(context, context.getResources().getText(R.string.news_on_events_title),
				context.getResources().getText(R.string.news_on_events), activity);

		notificationManager.notify((int) (Math.random() * 1000) + 1, notification);
	}

	@Override
	public void run() {
		String params = getParams();
		if (params != null) {

			EventsService.getInstance().synchronizeEventsFromServer(context, getUrl(), params); //{

		} else {
			Log.i(TAG, "Error al obtener los parametros");
		}
	}

	protected String getParams() {

		return GsonHelper.parseEntityToJson(new ParametersDTO());
	}

	protected String getUrl() {

		return MilongaHoyConstants.SYNC_EVENTS_URL;
	}

}
