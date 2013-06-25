package com.hoy.helpers;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.support.v4.app.FragmentManager;
import com.hoy.fragments.ProgressDialogFragment;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: ksairi
 * Date: 5/30/13
 * Time: 11:19 PM
 * To change this template use File | Settings | File Templates.
 */
public class FragmentHelper {

	public static ProgressDialogFragment showProgressDialog(FragmentManager fragmentManager) {

		ProgressDialogFragment progressDialogFragment = new ProgressDialogFragment();

		progressDialogFragment.show(fragmentManager, "progress");

		return progressDialogFragment;

	}


	public static void hideProgressDialog(ProgressDialogFragment progressDialogFragment) {

		if (progressDialogFragment != null) {
			progressDialogFragment.dismiss();

		}
	}

	public static Boolean isFragmentInForeGround(Context context) {

		if (context != null) {
			ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
			List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
			if (!tasks.isEmpty()) {
				ComponentName topActivity = tasks.get(0).topActivity;
				return topActivity.getClassName().equals(context.getClass().getName());
			}
		}

		return false;

	}

}
