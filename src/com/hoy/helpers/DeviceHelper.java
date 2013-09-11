package com.hoy.helpers;

import android.content.Context;
import android.view.Display;
import android.view.WindowManager;

/**
 * Created with IntelliJ IDEA.
 * User: ksairi
 * Date: 6/10/13
 * Time: 4:19 PM
 * To change this template use File | Settings | File Templates.
 */
public class DeviceHelper {

	public static Display getDisplay(Context context) {

		return ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
	}
}
