package com.hoy.timer_task;

import android.content.Context;

import java.util.TimerTask;

/**
 * Created with IntelliJ IDEA.
 * User: ksairi
 * Date: 1/3/13
 * Time: 3:09 AM
 * To change this template use File | Settings | File Templates.
 */
public abstract class SyncEventsTimerTask extends TimerTask {

	protected Context context;

	@Override
	public abstract void run();

	protected abstract String getParams();

	protected abstract String getUrl();
}
