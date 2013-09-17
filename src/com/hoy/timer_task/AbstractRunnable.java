package com.hoy.timer_task;

import android.content.Context;
import android.os.Handler;

/**
 * Created with IntelliJ IDEA.
 * User: ksairi
 * Date: 1/3/13
 * Time: 3:09 AM
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractRunnable implements Runnable {

    protected Context context;
    protected Handler handler;

    public abstract void run();

    protected abstract String getParams();

    protected abstract String getUrl();
}
