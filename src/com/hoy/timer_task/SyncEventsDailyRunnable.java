package com.hoy.timer_task;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import com.hoy.constants.MilongaHoyConstants;
import com.hoy.dto.ParametersDTO;
import com.hoy.services.EventsService;

public class SyncEventsDailyRunnable extends AbstractRunnable {

	private static final String TAG = SyncEventsDailyRunnable.class.getSimpleName();

	public SyncEventsDailyRunnable(Context context, Handler handler) {
		this.context = context;
		this.handler = handler;
	}

	/*protected void createNotification() {

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
	}*/

	@Override
	public void run() {
		String params = getParams();
		if (params != null) {

			if(EventsService.getInstance().synchronizeEventsFromServer(context, getUrl(), params,false).equals(MilongaHoyConstants.SAVE_MILONGAS_SUCCESS)){

					Bundle bundle = new Bundle();
					bundle.putString(MilongaHoyConstants.NEW_MILONGAS_UPDATES,MilongaHoyConstants.NEW_MILONGAS_UPDATES);
					Message message = new Message();
					message.setData(bundle);
					handler.sendMessage(message);
			}


		} else {
			Log.i(TAG, "Error al obtener los parametros");
		}
	}

	protected String getParams() {

		return ParametersDTO.getDailyRefreshParameters();
	}

	protected String getUrl() {

		return MilongaHoyConstants.SYNC_EVENTS_URL;
	}

}
