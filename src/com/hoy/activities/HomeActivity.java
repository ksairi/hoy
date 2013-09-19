package com.hoy.activities;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.view.*;
import android.widget.LinearLayout;
import com.hoy.R;
import com.hoy.constants.MilongaHoyConstants;
import com.hoy.dto.EventDTO;
import com.hoy.fragments.EventDetailFragment;
import com.hoy.fragments.EventListFragment;
import com.hoy.fragments.PromoImgFragment;

import java.util.ArrayList;
import java.util.List;

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
    PromoImgFragment listPromoImgFragment;
    PromoImgFragment detailPromoImgFragment;
    DrawerLayout mDrawer;
    EventDetailFragment eventDetailFragment;
    Menu menu;
    ActionBarDrawerToggle mDrawerToggle;
    EventListFragment eventListFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        detailsWrapper = (ViewGroup) findViewById(R.id.event_details_wrapper);
        mDualPane = detailsWrapper != null && detailsWrapper.getVisibility() == View.VISIBLE;
        if (!mDualPane) {
            prepareSlidingMenu();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!mDualPane) {
            eventListFragment = (EventListFragment) getSupportFragmentManager().findFragmentById(R.id.event_list_fragment);
        }

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        if (mDrawerToggle != null) {
            mDrawerToggle.syncState();
        }
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
            //genericStartActivity(EventDetailsActivity.class, MilongaHoyConstants.EVENT_DTO, eventDTO, false);
            eventDetailFragment.setEventProperties(eventDTO);
            LinearLayout mDrawerLayout = (LinearLayout) findViewById(R.id.fragment_list_wrapper);
            mDrawer.closeDrawer(mDrawerLayout);
        }

    }

    public void onFragmentMenuOptionSelected(MenuItem item) {
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

    public void onClickTodaysMap(List<EventDTO> eventDTOs) {
        genericStartActivity(GoogleMapActivity.class, MilongaHoyConstants.TODAY_DTOS, (ArrayList) eventDTOs, false);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (mDrawerToggle != null) {
            mDrawerToggle.onConfigurationChanged(newConfig);
        }
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
        onItemSelected(eventDTO, index);
        openDrawer();
    }

    @Override
    public void showEmptyEventList() {
        mDrawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_OPEN);
        mDrawer.findViewById(R.id.event_detail_content).setVisibility(View.INVISIBLE);
    }

    public void openDrawer() {
        if (mDrawer != null) {
            mDrawer.openDrawer(Gravity.LEFT);
            mDrawer.findViewById(R.id.event_detail_content).setVisibility(View.VISIBLE);
        }
    }

    public Boolean getNextPromoImg() {
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_refresh_refresh, menu);
        this.menu = menu;
        super.onCreateOptionsMenu(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        onFragmentMenuOptionSelected(item);
        return true;
    }

    private void startDetailPromoImg() {
        if (detailPromoImgFragment == null) {
            detailPromoImgFragment = new PromoImgFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.promo_img_fragment_detail_wrapper, detailPromoImgFragment).commit();
            getSupportFragmentManager().executePendingTransactions();
        } else {
            detailPromoImgFragment.startPromoImgTimerTask();
        }
    }

    @Override
    public void onBackPressed() {
        if (!mDrawer.isDrawerOpen(Gravity.LEFT)) {
            mDrawer.openDrawer(Gravity.LEFT);
        } else {
            super.onBackPressed();
        }
    }

    private void prepareSlidingMenu() {

        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawer.setFocusableInTouchMode(false);
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawer,         /* DrawerLayout object */
                R.drawable.dummy,  /* nav drawer icon to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description */
                R.string.drawer_close  /* "close drawer" description */
        ) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                //getActionBar().setTitle(mTitle);
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                //getActionBar().setTitle(mDrawerTitle);
            }
        };

        // Set the drawer toggle as the DrawerListener
        mDrawer.setDrawerListener(mDrawerToggle);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        mDrawer.setDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(View drawerView) {
                if (detailPromoImgFragment != null) {
                    detailPromoImgFragment.cancelPromoImgTimerTask();
                }
                if (listPromoImgFragment != null) {
                    listPromoImgFragment.startPromoImgTimerTask();
                }
                menu.getItem(0).setVisible(true);

            }

            @Override
            public void onDrawerClosed(View drawerView) {
                listPromoImgFragment.cancelPromoImgTimerTask();
                startDetailPromoImg();
                menu.getItem(0).setVisible(false);
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });

        listPromoImgFragment = (PromoImgFragment) getSupportFragmentManager().findFragmentById(R.id.promo_img_list);
        eventDetailFragment = (EventDetailFragment) getSupportFragmentManager().findFragmentById(R.id.event_details_fragment);
    }
}
