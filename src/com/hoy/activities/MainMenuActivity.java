package com.hoy.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import com.hoy.R;
import com.hoy.adapters.MainMenuItemListAdapter;
import com.hoy.cache.interfaces.GenericListServiceable;
import com.hoy.constants.MilongaHoyConstants;
import com.hoy.dto.EventDTO;
import com.hoy.helpers.EventHelper;
import com.hoy.helpers.SharedPreferencesHelper;
import com.hoy.model.MainMenuItem;
import com.hoy.schedulers.EventsScheduler;
import com.hoy.services.EventsService;
import com.hoy.timer_task.SyncEventsDailyTimerTask;
import com.hoy.utilities.DateUtils;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: ksairi
 * Date: 1/3/13
 * Time: 4:54 AM
 * To change this template use File | Settings | File Templates.
 */
public class MainMenuActivity extends GenericActivity {

	private MainMenuItemListAdapter mainMenuItemListAdapter;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_menu);

		List<MainMenuItem<?>> mainMenuItems = new ArrayList<MainMenuItem<?>>();
		mainMenuItems.add(new MainMenuItem<TodayEventsListActivity>(R.string.today_list_events_label, R.drawable.today_events_icon, getString(R.string.today_list_events_label), TodayEventsListActivity.class));
		mainMenuItems.add(new MainMenuItem<EventsListActivity>(R.string.list_events_label, R.drawable.all_events_icon, getString(R.string.list_events_label),EventsListActivity.class));

		ListView mainMenuItemListView = (ListView) findViewById(R.id.main_menu_item_list);
		mainMenuItemListAdapter = new MainMenuItemListAdapter(getContext(), mainMenuItems);
		mainMenuItemListView.setAdapter(mainMenuItemListAdapter);
		mainMenuItemListView.setOnItemClickListener(onMainMenuItemClickListener);
		Button update_manually = (Button)findViewById(R.id.update_manually);
		update_manually.setOnClickListener(updateManuallyOnClick);
		prepareContent();
	}

	private AdapterView.OnItemClickListener onMainMenuItemClickListener = new AdapterView.OnItemClickListener() {

			public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

				MainMenuItem<?> item = mainMenuItemListAdapter.getItem(position);

					Class<?> targetActivity = item.getTargetActivity();
					genericStartActivity(targetActivity,true);

			}
		};

	private View.OnClickListener updateManuallyOnClick =new View.OnClickListener(){


		public void onClick(View view) {

			if(EventHelper.hasRecentlyManuallyUpdated(getContext())){

				changeProgressBarVisibility(View.VISIBLE);
				EventsService.getInstance().synchronizeEventsFromServer(getContext(),new GenericListServiceable<EventDTO>() {
					public void handleSuccessCallBack(List<EventDTO> eventDTOs) {
						changeProgressBarVisibility(View.GONE);
						SharedPreferencesHelper.setValueSharedPreferences(getContext(),MilongaHoyConstants.LAST_MANUALLY_UPDATE_DATE,DateUtils.getToday().toString());
						EventsScheduler.rescheduleEventsSyncTask(new SyncEventsDailyTimerTask(getContext()), MilongaHoyConstants.SCHEDULE_DELAY, EventsScheduler.ONE_HOUR_IN_MILLISECONDS);
						//TODO VER SI SOBRESCRIBE LA TASK QUE YA EXISTE O CREA OTRA
						Toast.makeText(getContext(),R.string.events_updated_succ,Toast.LENGTH_SHORT);
					}

					public void handleErrorResult() {
						changeProgressBarVisibility(View.GONE);
						Toast.makeText(getContext(),R.string.connection_errors,Toast.LENGTH_SHORT);

					}
				});
			}

		}
	};

	private void prepareContent(){

		if(EventHelper.hasLocalData(getContext())){

			EventsScheduler.startSyncEventsTasks(getContext(),0);
		}
		else{

			changeProgressBarVisibility(View.VISIBLE);
			EventsService.getInstance().synchronizeEventsFromServer(getContext(),new GenericListServiceable<EventDTO>() {
				public void handleSuccessCallBack(List<EventDTO> genericList) {
					changeProgressBarVisibility(View.GONE);
					Toast.makeText(getContext(),R.string.events_updated_succ,Toast.LENGTH_SHORT);
					EventsScheduler.startSyncEventsTasks(getContext(),1);
				}

				public void handleErrorResult() {
					changeProgressBarVisibility(View.GONE);
					Toast.makeText(getContext(),R.string.connection_errors,Toast.LENGTH_SHORT);
				}
			});
		}

	}

	@Override
		protected Activity getActivity() {
			return this;
		}

	@Override
	protected Context getContext() {
		return this;
	}
}
