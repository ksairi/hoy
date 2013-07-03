package com.hoy.activities;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import com.hoy.R;
import com.hoy.constants.MilongaHoyConstants;
import com.hoy.dto.EventDTO;
import com.hoy.fragments.EventDetailFragment;
import com.hoy.fragments.EventListFragment;
import com.hoy.fragments.PromoImgFragment;

/**
 * Created with IntelliJ IDEA.
 * User: ksairi
 * Date: 1/3/13
 * Time: 4:54 AM
 * To change this template use File | Settings | File Templates.
 */
public class HomeActivity extends GenericActivity implements EventListFragment.EventListFragmentListener, PromoImgFragment.PromoImgFragmentInterface {

	Boolean mDualPane;
	ViewGroup detailsWrapper;
	private static Integer INIT_POSITION = -1;
	Integer currentSelectedIndex = INIT_POSITION;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);

		detailsWrapper = (ViewGroup) findViewById(R.id.event_details_wrapper);
		mDualPane = detailsWrapper != null && detailsWrapper.getVisibility() == View.VISIBLE;
	}

	public void onItemSelected(EventDTO eventDTO, Integer newSelectedIndex) {


		if (mDualPane) {
			if (!this.currentSelectedIndex.equals(newSelectedIndex)) {

				this.currentSelectedIndex = newSelectedIndex;
				FragmentManager fragmentManager = getSupportFragmentManager();
				FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
				EventDetailFragment eventDetailFragment = new EventDetailFragment();
				Bundle bundle = new Bundle();
				bundle.putParcelable(MilongaHoyConstants.EVENT_DTO, eventDTO);
				eventDetailFragment.setArguments(bundle);
				detailsWrapper.removeAllViewsInLayout();
				fragmentTransaction.add(R.id.event_details_wrapper, eventDetailFragment);
				fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
				fragmentTransaction.addToBackStack(null);
				fragmentTransaction.commit();

			}

		} else {
			genericStartActivity(EventDetailsActivity.class, MilongaHoyConstants.EVENT_DTO, eventDTO, false);
		}

	}

	public void onMenuOptionSelected(MenuItem item) {
		if (item.getItemId() == R.id.refresh_btn) {

			EventListFragment eventListFragment = (EventListFragment) getSupportFragmentManager().findFragmentById(R.id.event_list_fragment);
			if (mDualPane) {

				EventDetailFragment eventDetailFragment = (EventDetailFragment) getSupportFragmentManager().findFragmentById(R.id.event_details_fragment);
				if (eventDetailFragment != null) {

					detailsWrapper.removeAllViewsInLayout();
				}
			}
			eventListFragment.updateManually();
		}
	}

	public void onDateOptionChanged() {
		if (mDualPane) {
			detailsWrapper.removeAllViewsInLayout();
		}
	}

	public void onClickTodaysMap() {
		genericStartActivity(GoogleMapActivity.class, false);
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		//((ImageView)findViewById(R.id.promo_img)).setImageBitmap(null);
/*
			((ViewManager)findViewById(R.id.promo_img).getParent()).removeView(findViewById(R.id.promo_img));
			ImageView imageView = new ImageView(getContext());
			imageView.setId(R.id.promo_img);
			LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
			imageView.setLayoutParams(layoutParams);
			((LinearLayout)findViewById(R.id.promo_img_wrapper)).addView(imageView);
*/
		//ImageService.retrievePromoImg(getContext());
	}

	public void autoSelectFirst(EventDTO eventDTO, Integer index) {
		if (mDualPane) {

			onItemSelected(eventDTO,index);
		}
	}

	public Boolean getNextPromoImg() {
		return true;
	}
}
