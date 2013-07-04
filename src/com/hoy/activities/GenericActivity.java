package com.hoy.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.FragmentActivity;
import android.view.Window;
import com.actionbarsherlock.app.SherlockFragmentActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: ksairi
 * Date: 1/2/13
 * Time: 5:58 AM
 * To change this template use File | Settings | File Templates.
 */
public abstract class GenericActivity extends SherlockFragmentActivity {

	@SuppressWarnings("unused")
	private static final String TAG = GenericActivity.class.getSimpleName();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//requestWindowFeature(Window.FEATURE_NO_TITLE);
	}

	protected void genericStartActivity(Class<?> clazz, Map<String, String> paramsMap, Boolean finish) {


		Intent intent = new Intent(this, clazz);
		if (paramsMap != null) {
			Set<String> keySet = paramsMap.keySet();
			for (String key : keySet) {
				intent.putExtra(key, paramsMap.get(key));
			}
		}
		customStartActivity(intent, finish);
	}

	protected void genericStartActivity(Class<?> clazz, String extrasName, String extras, Boolean finish) {

		Intent intent = new Intent(this, clazz);
		if (extras != null) {
			intent.putExtra(extrasName, extras);
		}

		customStartActivity(intent, finish);
	}

	protected void genericStartActivity(Class<?> clazz, String extrasName, Parcelable extras, Boolean finish) {


		Intent intent = new Intent(this, clazz);
		if (extras != null) {
			intent.putExtra(extrasName, extras);
		}

		customStartActivity(intent, finish);
	}

	protected void genericStartActivity(Class<?> clazz, String extrasName, ArrayList<Parcelable> extras, Boolean finish) {


		Intent intent = new Intent(this, clazz);
		if (extras != null) {
			intent.putParcelableArrayListExtra(extrasName, extras);
		}

		customStartActivity(intent, finish);
	}

	protected void customStartActivity(Intent intent, Boolean finish) {

		startActivity(intent);
		if (finish) {
			finish();
		}
	}

	protected void genericStartActivity(Class<?> clazz) {


		Intent intent = new Intent(this, clazz);
		startActivity(intent);
		finish();
	}

	protected void genericStartActivity(Class<?> clazz, Boolean finish) {
		// Para hacer aparecer la progress bar ya que se esta haciendo navegacion entre View's.
		//changeProgressBarVisibility(View.VISIBLE);
		Intent intent = new Intent(this, clazz);
		customStartActivity(intent, finish);
	}

	protected void genericStartActivity(Class<?> clazz, List<Integer> flags) {
		// Para hacer aparecer la progress bar ya que se esta haciendo navegacion entre View's.
		//changeProgressBarVisibility(View.VISIBLE);
		Intent intent = new Intent(this, clazz);

		for (Integer flag : flags) {

			intent.setFlags(flag);
		}

		startActivity(intent);
		finish();
	}

	protected Context getContext() {

		return this;

	}
}