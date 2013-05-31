package com.hoy.schedulers;

import android.content.Context;
import com.hoy.timer_task.SyncEventsHourlyTimerTask;
import com.hoy.timer_task.SyncEventsTimerTask;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;

/**
 * @author ksairi
 */
public class EventsScheduler {

	private static Timer scheduleTasksTimer;
	public static final int ONE_DAY_IN_MILLISECONDS = 1000 * 60 * 60 * 24;

	public static void startSyncEventsTasks(final Context context, Integer initDelay) {


		scheduleTasksTimer = new Timer();

		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.HOUR, initDelay);

		//Schedule the hourly timer task one hour after the daily timer task because the latter one includes the first one.
		calendar.add(Calendar.HOUR, 1 + initDelay);
		scheduleTask(new SyncEventsHourlyTimerTask(context), calendar.getTime(), ONE_DAY_IN_MILLISECONDS);

	}


	private static void scheduleTask(SyncEventsTimerTask syncEventsTimerTask, Date when, Integer period) {
		scheduleTasksTimer.schedule(syncEventsTimerTask, when, period);
	}

	public static void stopEventsSyncTasks() {
		if (scheduleTasksTimer != null) {
			scheduleTasksTimer.cancel();
		}
	}

	public static void rescheduleEventsSyncTask(SyncEventsTimerTask syncEventsTimerTask, Integer delay, Integer period) {
		if (scheduleTasksTimer != null) {
			scheduleTasksTimer.schedule(syncEventsTimerTask, delay, period);
		}
	}
}
