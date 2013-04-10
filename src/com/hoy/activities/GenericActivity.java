package com.hoy.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ProgressBar;
import com.hoy.R;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: ksairi
 * Date: 1/2/13
 * Time: 5:58 AM
 * To change this template use File | Settings | File Templates.
 */
public abstract class GenericActivity extends Activity {

	@SuppressWarnings("unused")
		private static final String TAG = GenericActivity.class.getSimpleName();
		protected Map<String, Integer> serverErrors = new HashMap<String, Integer>();
		protected String languageCode = Locale.getDefault().getLanguage();

		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			requestWindowFeature(Window.FEATURE_NO_TITLE);
		}

		@Override
		protected void onPostCreate(Bundle savedInstanceState) {
			super.onPostCreate(savedInstanceState);
			// Para hacer desaparecer la progress bar iniciada en las Activities invocadoras.
			changeProgressBarVisibility(View.GONE);
		}

		protected void genericStartActivity(Class<?> clazz, Map<String, String> paramsMap, Boolean finish) {

			// Para hacer aparecer la progress bar ya que se esta haciendo navegacion entre View's.
			changeProgressBarVisibility(View.VISIBLE);
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
			// Para hacer aparecer la progress bar ya que se esta haciendo navegacion entre View's.
			changeProgressBarVisibility(View.VISIBLE);
			Intent intent = new Intent(this, clazz);
			if (extras != null) {
				intent.putExtra(extrasName, extras);
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
			// Para hacer aparecer la progress bar ya que se esta haciendo navegacion entre View's.
			changeProgressBarVisibility(View.VISIBLE);
			Intent intent = new Intent(this, clazz);
			startActivity(intent);
			finish();
		}

		protected void genericStartActivity(Class<?> clazz, Boolean finish) {
			// Para hacer aparecer la progress bar ya que se esta haciendo navegacion entre View's.
			changeProgressBarVisibility(View.VISIBLE);
			Intent intent = new Intent(this, clazz);
			customStartActivity(intent, finish);
		}

		protected void genericStartActivity(Class<?> clazz, List<Integer> flags) {
			// Para hacer aparecer la progress bar ya que se esta haciendo navegacion entre View's.
			changeProgressBarVisibility(View.VISIBLE);
			Intent intent = new Intent(this, clazz);

			for (Integer flag : flags) {

				intent.setFlags(flag);
			}

			startActivity(intent);
			finish();
		}

		protected abstract Context getContext();
		protected abstract Activity getActivity();


		protected void changeProgressBarVisibility(int visibility) {
			ProgressBar pb = (ProgressBar) findViewById(R.id.progress_bar);
			pb.setVisibility(visibility);

			ImageView progressBarBg = (ImageView) findViewById(R.id.progress_bar_bg);
			progressBarBg.setVisibility(visibility);
			progressBarBg.setOnClickListener(progressBarBgOnClick);
		}


		protected View.OnClickListener progressBarBgOnClick = new View.OnClickListener() {
			public void onClick(View view) {
				return;
			}
		};

		@Override
		protected void onStop() {
			super.onPause();
			changeProgressBarVisibility(View.GONE); //Para cuando se se inicia una actividad pero no se mata en la que nos encontramos para esconder el PB cuando nos vamos.
		}

		/**
		 * Fuente: http://stackoverflow.com/questions/5726657/how-to-detect-orientation-change-in-layout-in-android
		 */
		@Override
		public void onConfigurationChanged(Configuration newConfig) {
			super.onConfigurationChanged(newConfig);
			if (!newConfig.locale.getLanguage().equals(languageCode)) {
				languageCode = newConfig.locale.getLanguage();

			}
		}
	}

