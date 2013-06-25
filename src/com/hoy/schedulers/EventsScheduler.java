package com.hoy.schedulers;

import android.content.Context;
import android.os.Handler;
import com.hoy.model.PromoImg;
import com.hoy.timer_task.AbstractRunnable;
import com.hoy.timer_task.ChangePromoImgRunnable;
import com.hoy.timer_task.RetrievePromoImgRunnable;
import com.hoy.timer_task.SyncEventsDailyRunnable;
import com.hoy.timer_task.SyncEventsHourlyRunnable;

import java.util.Calendar;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * @author ksairi
 */
public class EventsScheduler {

	private static final int ONE_HOUR_IN_MILLISECONDS = 1000 * 60 * 60;
	private static final int ONE_HOUR_AND_HALF_IN_MILLISECONDS = 1000 * 60 * 90;
	private static final int ONE_DAY_IN_MILLISECONDS = ONE_HOUR_AND_HALF_IN_MILLISECONDS * 24;
	private static final int TWO_MINUTES_IN_MILLISECONDS = 1000 * 60 * 2;
	private static ScheduledExecutorService scheduledExecutorService;

	public static ScheduledFuture startSyncEventsHourly(final Context context, Handler handler, Integer initDelay) {

		Calendar now = Calendar.getInstance();

		//Schedule the hourly timer task with one hour delay because it has just updated from the server
		Calendar oneHourFuture = Calendar.getInstance();
		oneHourFuture.add(Calendar.HOUR, initDelay);
		Long oneHourDelay = oneHourFuture.getTimeInMillis() - now.getTimeInMillis();


		return scheduleTask(new SyncEventsHourlyRunnable(context, handler), oneHourDelay, ONE_HOUR_IN_MILLISECONDS);


	}

	public static ScheduledFuture startSyncEventsDaily(final Context context, Handler handler, Integer initDelay) {

		Calendar now = Calendar.getInstance();
		Calendar oneDayFuture = Calendar.getInstance();
		oneDayFuture.add(Calendar.DAY_OF_MONTH, initDelay);

		Long oneDayDelay = oneDayFuture.getTimeInMillis() - now.getTimeInMillis();

		//return scheduleTask(new SyncEventsDailyRunnable(context,handler), oneDayDelay, ONE_DAY_IN_MILLISECONDS);
		return scheduleTask(new SyncEventsDailyRunnable(context, handler), 0, 1000 * 60 * 2);
	}

	public static ScheduledFuture startRetrievePromoImgTask(Context context, PromoImg promoImg) {

		Calendar now = Calendar.getInstance();
		Calendar future = Calendar.getInstance();

		// We delay 1 day we have just updated the image
		future.add(Calendar.DAY_OF_MONTH, 1);
		long delay = future.getTimeInMillis() - now.getTimeInMillis();

		return scheduleTask(new RetrievePromoImgRunnable(context, promoImg), delay, ONE_DAY_IN_MILLISECONDS);
	}

	public static ScheduledFuture startChangePromoImgTask(Context context, Handler handler, Boolean getNextPromoImg) {

		return scheduleTask(new ChangePromoImgRunnable(context, handler, getNextPromoImg), 0, TWO_MINUTES_IN_MILLISECONDS);
	}


	private static ScheduledFuture scheduleTask(AbstractRunnable syncEventsRunnable, long initialDelay, Integer period) {

		scheduledExecutorService = getScheduledExecutorService();

		return scheduledExecutorService.scheduleWithFixedDelay(syncEventsRunnable, initialDelay, period, TimeUnit.MILLISECONDS);
	}

	public static void cancelScheduledExecutorService() {
		if (scheduledExecutorService != null) {
			scheduledExecutorService.shutdown();
		}
	}

	private static ScheduledExecutorService getScheduledExecutorService() {
		if (scheduledExecutorService == null) {
			scheduledExecutorService = Executors.newScheduledThreadPool(3);
		}
		return scheduledExecutorService;
	}
}
