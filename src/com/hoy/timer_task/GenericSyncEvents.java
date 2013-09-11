package com.hoy.timer_task;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import com.hoy.constants.MilongaHoyConstants;
import com.hoy.services.EventsService;

/**
 * Created with IntelliJ IDEA.
 * User: ksairi
 * Date: 6/25/13
 * Time: 4:07 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class GenericSyncEvents extends AbstractRunnable {
	private static final String TAG = SyncEventsDailyRunnable.class.getSimpleName();

	public GenericSyncEvents(Context context, Handler handler) {
		this.context = context;
		this.handler = handler;
	}

	@Override
	public void run() {
		String params = getParams();
		if (params != null) {
			Message message = new Message();
			Bundle bundle = new Bundle();
			String result = EventsService.getInstance().synchronizeEventsFromServer(context, getUrl(), params, isDeltaUpdate());
			if (result == null) {
				bundle.putString(MilongaHoyConstants.ERROR_SYNC_EVENTS, MilongaHoyConstants.ERROR_SYNC_EVENTS);
			} else {
				if (result.equals(MilongaHoyConstants.SAVE_MILONGAS_SUCCESS)) {

					bundle.putString(MilongaHoyConstants.NEW_MILONGAS_UPDATES, MilongaHoyConstants.NEW_MILONGAS_UPDATES);
					setLastFullUpdateDate();
				}
			}
			message.setData(bundle);
			handler.sendMessage(message);
		} else {
			Log.i(TAG, "Error al obtener los parametros");
		}
	}

	@Override
	protected abstract String getParams();

	protected abstract void setLastFullUpdateDate();

	@Override
	protected String getUrl() {
		return MilongaHoyConstants.SYNC_EVENTS_URL;
	}

	protected abstract Boolean isDeltaUpdate();
}
