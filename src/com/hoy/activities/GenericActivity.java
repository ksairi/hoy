package com.hoy.activities;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import com.hoy.R;
import com.hoy.constants.MilongaHoyConstants;
import com.hoy.dto.EventDTO;
import com.hoy.helpers.ImageHelper;
import com.hoy.timer_task.AbstractRunnable;


import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ScheduledFuture;

/**
 * Created with IntelliJ IDEA.
 * User: ksairi
 * Date: 1/2/13
 * Time: 5:58 AM
 * To change this template use File | Settings | File Templates.
 */
public abstract class GenericActivity extends FragmentActivity {

	@SuppressWarnings("unused")
	private static final String TAG = GenericActivity.class.getSimpleName();
	protected String languageCode = Locale.getDefault().getLanguage();
	protected ScheduledFuture changePromoImgtask;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);

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

	protected void genericStartActivity(Class<?> clazz, String extrasName, ArrayList<EventDTO> extras, Boolean finish) {


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

	protected Context getContext(){

		return this;

	}


	@Override
	protected void onStop() {
		super.onPause();
		//changeProgressBarVisibility(View.GONE); //Para cuando se se inicia una actividad pero no se mata en la que nos encontramos para esconder el PB cuando nos vamos.
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

	protected Handler changePromoImgHandler = new Handler(){

			ImageView promoImageView;
			Bundle bundle;
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				bundle = msg.getData();
				promoImageView = (ImageView)findViewById(R.id.promo_img);
				String[] promoImgData = bundle.getStringArray(MilongaHoyConstants.PROMO_IMG_DATA);
				Animation myFadeInAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.fade_in);
				promoImageView.startAnimation(myFadeInAnimation); //Set animation to your ImageView
				promoImageView.setImageBitmap(ImageHelper.getBitMap(promoImgData[MilongaHoyConstants.PROMO_IMG_BASE_64_INDEX_POSITION]));
				final String promoImgUrlDestination = promoImgData[(MilongaHoyConstants.PROMO_IMG_URL_DESTINATION_INDEX_POSITION)];

				promoImageView.setOnClickListener(new View.OnClickListener() {
					public void onClick(View view) {
						Intent intent = new Intent(Intent.ACTION_VIEW);
						if(promoImgUrlDestination != null && !promoImgUrlDestination.equals(MilongaHoyConstants.EMPTY_STRING)){
							intent.setData(Uri.parse(promoImgUrlDestination));
							startActivity(intent);
						}
					}
				});

			}


};


}