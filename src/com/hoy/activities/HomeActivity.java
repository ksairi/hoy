package com.hoy.activities;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.hoy.R;
import com.hoy.asynctasks.RetrivePromoImgAysncTask;
import com.hoy.asynctasks.interfaces.GenericSuccessImgHandleable;
import com.hoy.constants.MilongaHoyConstants;
import com.hoy.dto.EventDTO;
import com.hoy.fragments.EventDetailFragment;
import com.hoy.fragments.EventListFragment;

/**
 * Created with IntelliJ IDEA.
 * User: ksairi
 * Date: 1/3/13
 * Time: 4:54 AM
 * To change this template use File | Settings | File Templates.
 */
public class HomeActivity extends GenericActivity implements EventListFragment.OnItemSelectedListener {

	Boolean mDualPane;
	ViewGroup detailsWrapper;
	Integer position;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		retrievePromoImg();
		detailsWrapper = (ViewGroup) findViewById(R.id.event_details_wrapper);
		mDualPane = detailsWrapper != null && detailsWrapper.getVisibility() == View.VISIBLE;
	}

	public void onItemSelected(EventDTO eventDTO, Integer position) {

		if (mDualPane) {
			this.position = position;
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

		} else {
			genericStartActivity(EventDetailsActivity.class, MilongaHoyConstants.EVENT_DTO, eventDTO, false);

		}
	}

	public void onMenuOptionSelected(MenuItem item) {
		if(item.getItemId() == R.id.refresh_btn){

			if(mDualPane){

				EventDetailFragment eventDetailFragment = (EventDetailFragment)getSupportFragmentManager().findFragmentById(R.id.event_details_fragment);
				if(eventDetailFragment != null){
					EventListFragment eventListFragment = (EventListFragment)getSupportFragmentManager().findFragmentById(R.id.event_list_fragment);
					detailsWrapper.removeAllViewsInLayout();
					eventListFragment.updateManually();
				}
			}
		}
	}

	private void retrievePromoImg(){

		new RetrivePromoImgAysncTask(MilongaHoyConstants.PROMO_IMG_URL,new GenericSuccessImgHandleable() {
			public void handleSuccessCallBack(Bitmap bitmap) {
				((ImageView)findViewById(R.id.promo_img)).setImageBitmap(bitmap);
			}

			public void handleErrorResult() {
				//To change body of implemented methods use File | Settings | File Templates.
			}
		}).execute();
	}

	public void onDateOptionChanged() {
		if(mDualPane){
			detailsWrapper.removeAllViewsInLayout();
		}
	}

	@Override
	protected Context getContext() {
		return this;
	}
}
