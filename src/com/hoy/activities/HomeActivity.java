package com.hoy.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.ViewGroup;
import com.hoy.R;
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


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
	}

	public void onItemSelected(EventDTO eventDTO) {
		ViewGroup detailsWrapper = (ViewGroup) findViewById(R.id.event_details_wrapper);
		Boolean mDualPane = detailsWrapper != null && detailsWrapper.getVisibility() == View.VISIBLE;

		if (mDualPane) {

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

	@Override
	protected Context getContext() {
		return this;
	}
}
