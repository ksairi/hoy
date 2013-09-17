package com.hoy.timer_task;

import android.content.Context;
import android.os.Handler;
import com.hoy.dto.ParametersDTO;

public class SyncEventsHourlyRunnable extends GenericSyncEvents {

    public SyncEventsHourlyRunnable(Context context, Handler handler) {
        super(context, handler);
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

    protected String getParams() {

        return ParametersDTO.getHourlyRefreshParameters(context);
    }

    @Override
    protected Boolean isDeltaUpdate() {
        return true;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    protected void setLastFullUpdateDate() {

    }

}
