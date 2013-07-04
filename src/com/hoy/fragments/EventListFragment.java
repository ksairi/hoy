package com.hoy.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.actionbarsherlock.app.SherlockListFragment;
import com.hoy.R;
import com.hoy.adapters.EventListAdapter;
import com.hoy.asynctasks.SyncLocalEventsAsyncTask;
import com.hoy.asynctasks.interfaces.GenericSuccessListHandleable;
import com.hoy.constants.MilongaHoyConstants;
import com.hoy.dto.EventDTO;
import com.hoy.helpers.FragmentHelper;
import com.hoy.helpers.SharedPreferencesHelper;
import com.hoy.model.FilterParams;
import com.hoy.schedulers.EventsScheduler;
import com.hoy.services.EventsService;
import com.hoy.utilities.DateUtils;

import java.util.List;
import java.util.concurrent.ScheduledFuture;

/**
 * Created with IntelliJ IDEA.
 * User: ksairi
 * Date: 5/21/13
 * Time: 9:10 PM
 * To change this template use File | Settings | File Templates.
 */
public class EventListFragment extends SherlockListFragment {

	protected List<EventDTO> eventDTOs;
	protected EventListFragmentListener listener;
	protected CheckBox todayEvents;
	private Activity activityAttached;
	private TextView syncLocalEvents;
	private Boolean newMilongasUpdate = false;
	private Boolean errorSyncEvents = false;
	private TextView todayDateTextView;
	private ScheduledFuture scheduleFutureHourly;
	private ScheduledFuture scheduleFutureDaily;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		setHasOptionsMenu(true);
		setRetainInstance(true);
		return inflater.inflate(R.layout.fragment_events_list_menu, container, false);
	}

	@Override
	public void onActivityCreated(Bundle bundle) {
		super.onActivityCreated(bundle);

		todayEvents = (CheckBox) activityAttached.findViewById(R.id.today_events);
		todayDateTextView = (TextView) activityAttached.findViewById(R.id.today_date);
		setTodayDateTextView(DateUtils.getTodayDateToShow());

		todayEvents.setOnCheckedChangeListener(onCheckedChangeListener);

		TextView todaysMap = (TextView) getActivity().findViewById(R.id.today_events_map);

		todaysMap.setOnClickListener(onClickTodaysMap);

		syncLocalEvents = (TextView) getActivity().findViewById(R.id.sync_local_events);
		syncLocalEvents.setOnClickListener(syncLocalEventsOnClick);

		Bundle extras = getActivity().getIntent().getExtras();
		if (extras != null) {
			eventDTOs = extras.getParcelableArrayList(MilongaHoyConstants.EVENT_DTOS);
			updateAdapter(eventDTOs);
		} else {
			syncEventList();
		}

		scheduleFutureHourly = EventsScheduler.startSyncEventsHourly(getActivity(), syncEventsHandler, 1);
		scheduleFutureDaily = EventsScheduler.startSyncEventsDaily(getActivity(), syncEventsHandler, 1);

		SharedPreferencesHelper.setValueSharedPreferences(getActivity(), MilongaHoyConstants.TODAY_STRING, DateUtils.getTodayDateToShow());
	}

	@Override
	public void onResume() {
		super.onResume();
		todayEvents.setText(R.string.today_list_events_label);
		String todayString = DateUtils.getTodayDateToShow();
		String savedTodayString = SharedPreferencesHelper.getValueInSharedPreferences(getActivity(), MilongaHoyConstants.TODAY_STRING);
		if (!todayString.equals(savedTodayString)) {
			newMilongasUpdate = false;
			reScheduleTasks();
			SharedPreferencesHelper.setValueSharedPreferences(getActivity(), MilongaHoyConstants.TODAY_STRING, todayString);
			if (SharedPreferencesHelper.getValueInSharedPreferences(getActivity(), MilongaHoyConstants.LAST_FULL_UPDATE_DATE).equals(todayString)) {
				syncEventList();
			} else {
				syncRemoteEvents();
			}

			setTodayDateTextView(todayString);

			return;
		}


		if (newMilongasUpdate) {

			syncEventList();
			newMilongasUpdate = false;

		} else {
			if (errorSyncEvents) {
				Toast.makeText(activityAttached, R.string.connection_errors, Toast.LENGTH_SHORT).show();
				errorSyncEvents = false;
			}
		}
	}

	private void reScheduleTasks() {

		if (scheduleFutureHourly != null) {
			scheduleFutureHourly.cancel(true);

		}
		if (scheduleFutureDaily != null) {

			scheduleFutureDaily.cancel(true);

		}
		scheduleFutureHourly = EventsScheduler.startSyncEventsHourly(getActivity(), syncEventsHandler, 1);
		scheduleFutureDaily = EventsScheduler.startSyncEventsDaily(getActivity(), syncEventsHandler, 1);
	}

	private void syncRemoteEvents() {

		EventsService.getInstance().synchronizeEventsFromServer(getActivity(), getFragmentManager(), getFilterParams(), new GenericSuccessListHandleable<EventDTO>() {
			public void handleSuccessCallBack(List<EventDTO> eventDTOs) {

				updateAdapter(eventDTOs);
			}

			public void handleErrorResult() {

				//Toast.makeText(activityAttached, R.string.no_events_to_show, Toast.LENGTH_SHORT).show();
			}

			public void handleErrorCallBack(List<EventDTO> eventDTOs) {

				updateAdapter(eventDTOs);
				//Toast.makeText(activityAttached, R.string.connection_errors, Toast.LENGTH_SHORT).show();
			}
		});

	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		listener.onItemSelected(eventDTOs.get(position), position);
	}

	private View.OnClickListener syncLocalEventsOnClick = new View.OnClickListener() {
		public void onClick(View view) {
			syncEventList();

		}
	};

	protected void syncEventList() {

		new SyncLocalEventsAsyncTask(activityAttached, getFilterParams(), getFragmentManager(), new GenericSuccessListHandleable<EventDTO>() {
			public void handleSuccessCallBack(List<EventDTO> localEventDTOs) {
				updateAdapter(localEventDTOs);
				syncLocalEvents.setVisibility(View.GONE);
			}

			public void handleErrorResult() {

				Toast.makeText(activityAttached, R.string.no_events_to_show, Toast.LENGTH_SHORT).show();
			}

			public void handleErrorCallBack(List<EventDTO> localEventDTOs) {

			}
		}).execute();

	}

	public interface EventListFragmentListener {

		public void onItemSelected(EventDTO eventDTO, Integer index);

		public void onFragmentMenuOptionSelected(MenuItem item);

		public void onDateOptionChanged();

		public void onClickTodaysMap();

		public void autoSelectFirst(EventDTO eventDTO, Integer index);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		if (activity instanceof EventListFragmentListener) {
			listener = (EventListFragmentListener) activity;
			activityAttached = activity;
		} else {
			throw new ClassCastException(activity.toString()
					+ " must implemenet EventListFragment.OnItemSelectedListener");
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		listener = null;
		activityAttached = null;
		if (scheduleFutureHourly != null) {
			scheduleFutureHourly.cancel(true);
		}
		if (scheduleFutureDaily != null) {
			scheduleFutureDaily.cancel(true);
		}
	}

	private CompoundButton.OnCheckedChangeListener onCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
		public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
			listener.onDateOptionChanged();
			syncEventList();

		}
	};

	private View.OnClickListener onClickTodaysMap = new View.OnClickListener() {
		public void onClick(View view) {
			if (!eventDTOs.isEmpty()) {

				listener.onClickTodaysMap();
			} else {
				Toast.makeText(activityAttached, R.string.no_events_to_show, Toast.LENGTH_SHORT).show();
			}
		}
	};

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.menu_refresh_refresh, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		listener.onFragmentMenuOptionSelected(item);
		return false;
	}

	public void updateManually() {

		if (EventsService.hasRecentlyManuallyUpdated(activityAttached)) {


			EventsService.getInstance().synchronizeEventsFromServer(activityAttached, getFragmentManager(), getFilterParams(), new GenericSuccessListHandleable<EventDTO>() {
				public void handleSuccessCallBack(List<EventDTO> eventDTOs) {

					SharedPreferencesHelper.setValueSharedPreferences(getActivity(), MilongaHoyConstants.LAST_MANUALLY_UPDATE_DATE, DateUtils.getTodayAndTimeString());
					updateAdapter(eventDTOs);
				}

				public void handleErrorResult() {

					//Toast.makeText(activityAttached, R.string.connection_errors, Toast.LENGTH_SHORT).show();

				}

				public void handleErrorCallBack(List<EventDTO> eventDTOs) {

					//Toast.makeText(activityAttached, R.string.connection_errors, Toast.LENGTH_SHORT).show();
				}
			});

		} else {
			Toast.makeText(activityAttached, R.string.too_many_update_manually, Toast.LENGTH_SHORT).show();
		}

	}

	private void updateAdapter(List<EventDTO> eventDTOs) {
		if (activityAttached != null && eventDTOs != null && !eventDTOs.isEmpty() && activityAttached != null) {
			this.eventDTOs = eventDTOs;
			ArrayAdapter<EventDTO> arrayAdapter = new EventListAdapter(activityAttached, eventDTOs, todayEvents.isChecked());
			getListView().setAdapter(arrayAdapter);
			listener.onDateOptionChanged();
			activityAttached.findViewById(R.id.list_view_labels).setVisibility(View.VISIBLE);
			listener.autoSelectFirst(eventDTOs.get(0), 0);
		} else {
			if (activityAttached != null) {
				activityAttached.findViewById(R.id.list_view_labels).setVisibility(View.GONE);
				Toast.makeText(activityAttached, R.string.no_events_to_show, Toast.LENGTH_SHORT).show();
			}
		}

	}

	private FilterParams getFilterParams() {

		if (todayEvents.isChecked()) {

			return new FilterParams(DateUtils.getTodayString());
		}
		return null;

	}

	private Handler syncEventsHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg != null) {

				if (msg.getData().containsKey(MilongaHoyConstants.NEW_MILONGAS_UPDATES)) {

					if (FragmentHelper.isFragmentInForeGround(getActivity())) {

						syncLocalEvents.setVisibility(View.VISIBLE);
					} else {
						newMilongasUpdate = true;
					}
				} else {
					if (msg.getData().containsKey(MilongaHoyConstants.ERROR_SYNC_EVENTS)) {

						if (FragmentHelper.isFragmentInForeGround(getActivity())) {
							Toast.makeText(activityAttached, R.string.connection_errors, Toast.LENGTH_SHORT).show();
						} else {
							errorSyncEvents = true;
						}
					}
				}
			}

		}
	};

	private void setTodayDateTextView(String todayString) {

		todayDateTextView.setText("(".concat(todayString).concat(")"));
	}

	//@Override
	//public void onViewStateRestored(Bundle savedInstanceState) {
		//super.onViewStateRestored(savedInstanceState);
		//todayEvents.setText(R.string.today_list_events_label);
	//}
}
