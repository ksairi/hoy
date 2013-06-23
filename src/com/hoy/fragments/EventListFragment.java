package com.hoy.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
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

/**
 * Created with IntelliJ IDEA.
 * User: ksairi
 * Date: 5/21/13
 * Time: 9:10 PM
 * To change this template use File | Settings | File Templates.
 */
public class EventListFragment extends ListFragment {

	protected List<EventDTO> eventDTOs;
	protected EventListFragmentListener listener;
	protected CheckBox todayEvents;
	private Activity activityAttached;
	private TextView syncLocalEvents;
	private ProgressDialogFragment progressDialogFragment;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		setHasOptionsMenu(true);
		setRetainInstance(true);
		return inflater.inflate(R.layout.fragment_events_list_menu, container, false);
	}

	@Override
	public void onActivityCreated(Bundle bundle) {
		super.onActivityCreated(bundle);

		todayEvents = (CheckBox)activityAttached.findViewById(R.id.today_events);

		syncEventList();

		todayEvents.setOnCheckedChangeListener(onCheckedChangeListener);

		TextView todaysMap = (TextView)getActivity().findViewById(R.id.today_events_map);

		todaysMap.setOnClickListener(onClickTodaysMap);

		syncLocalEvents = (TextView)getActivity().findViewById(R.id.sync_local_events);
		syncLocalEvents.setOnClickListener(syncLocalEventsOnClick);

		Bundle extras = getActivity().getIntent().getExtras();
		if(extras != null){
			eventDTOs = extras.getParcelableArrayList(MilongaHoyConstants.EVENT_DTOS);
			updateAdapter(eventDTOs);
		}

		EventsScheduler.startSyncEventsHourly(getActivity(), syncEventsHandler, 1);
		EventsScheduler.startSyncEventsDaily(getActivity(), syncEventsHandler, 1);

	}

	@Override
	public void onResume() {
		super.onResume();
		if(!SharedPreferencesHelper.getValueInSharedPreferences(getActivity(),MilongaHoyConstants.NEW_MILONGAS_UPDATES).equals(MilongaHoyConstants.EMPTY_STRING)){

			syncLocalEvents.setVisibility(View.VISIBLE);
			SharedPreferencesHelper.removeValueSharedPreferences(getActivity(),MilongaHoyConstants.NEW_MILONGAS_UPDATES);
		}
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		listener.onItemSelected(eventDTOs.get(position),position);
	}

	private View.OnClickListener syncLocalEventsOnClick = new View.OnClickListener(){
		public void onClick(View view) {
			syncEventList();

		}
	};

	protected void syncEventList() {

		progressDialogFragment = FragmentHelper.showProgressDialog(getFragmentManager());
		new SyncLocalEventsAsyncTask(activityAttached,getFilterParams(),new GenericSuccessListHandleable<EventDTO>(){
			public void handleSuccessCallBack(List<EventDTO> localEventDTOs) {
				updateAdapter(localEventDTOs);
				syncLocalEvents.setVisibility(View.GONE);
				FragmentHelper.hideProgressDialog(progressDialogFragment);
			}

			public void handleErrorResult() {
				FragmentHelper.hideProgressDialog(progressDialogFragment);
				Toast.makeText(activityAttached, R.string.connection_errors, Toast.LENGTH_LONG).show();
			}
		}).execute();

	}

	public interface EventListFragmentListener {

		public void onItemSelected(EventDTO eventDTO, Integer index);
		public void onMenuOptionSelected(MenuItem item);
		public void onDateOptionChanged();
		public void onClickTodaysMap();
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
	}

	private CompoundButton.OnCheckedChangeListener onCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
		public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
			listener.onDateOptionChanged();
			syncEventList();

		}
	};

	private View.OnClickListener onClickTodaysMap = new View.OnClickListener() {
		public void onClick(View view) {
			if(!eventDTOs.isEmpty()){

				listener.onClickTodaysMap();
			}
			else {
				Toast.makeText(activityAttached, R.string.no_events_to_show, Toast.LENGTH_LONG).show();
			}
		}
	};

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.menu_refresh_refresh,menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		listener.onMenuOptionSelected(item);
		return false;
	}

	public void updateManually(){

		if (EventsService.hasRecentlyManuallyUpdated(activityAttached)) {

			progressDialogFragment = FragmentHelper.showProgressDialog(getFragmentManager());
			EventsService.getInstance().synchronizeEventsFromServer(activityAttached, new GenericSuccessListHandleable<EventDTO>() {
				public void handleSuccessCallBack(List<EventDTO> remoteEventDTOs) {
					updateAdapter(remoteEventDTOs);
					FragmentHelper.hideProgressDialog(progressDialogFragment);
					SharedPreferencesHelper.setValueSharedPreferences(getActivity(), MilongaHoyConstants.LAST_MANUALLY_UPDATE_DATE, DateUtils.getTodayAndTimeString());
					syncLocalEvents.setVisibility(View.GONE);
					Toast.makeText(activityAttached, R.string.events_updated_succ, Toast.LENGTH_SHORT).show();
				}

				public void handleErrorResult() {
					FragmentHelper.hideProgressDialog(progressDialogFragment);
					Toast.makeText(activityAttached, R.string.connection_errors, Toast.LENGTH_SHORT).show();

				}
			});
		}
		else{
			Toast.makeText(activityAttached, R.string.too_many_update_manually, Toast.LENGTH_SHORT).show();
		}

	}

	private void updateAdapter(List<EventDTO> eventDTOs){
		if(activityAttached != null){
			this.eventDTOs = eventDTOs;
			ArrayAdapter<EventDTO> arrayAdapter = new EventListAdapter(activityAttached, eventDTOs, todayEvents.isChecked());
			getListView().setAdapter(arrayAdapter);
			listener.onDateOptionChanged();
		}
		if(!eventDTOs.isEmpty() && activityAttached != null){
			activityAttached.findViewById(R.id.list_view_labels).setVisibility(View.VISIBLE);
		}
		else{
				if(activityAttached != null){
					activityAttached.findViewById(R.id.list_view_labels).setVisibility(View.GONE);
					Toast.makeText(activityAttached, R.string.no_events_to_show, Toast.LENGTH_LONG).show();
				}
			}

	}

	private FilterParams getFilterParams(){

		if(todayEvents.isChecked()){

			return new FilterParams(DateUtils.getTodayString());
		}
			return null;

	}

	private Handler syncEventsHandler = new Handler(){

			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				if((msg != null) && msg.getData().containsKey(MilongaHoyConstants.NEW_MILONGAS_UPDATES)){
					syncLocalEvents.setVisibility(View.VISIBLE);

				}
				else{
					Toast.makeText(getActivity(),R.string.cant_sync_new_events,Toast.LENGTH_SHORT).show();
				}
			}
		};
}
