package com.hoy.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.text.BoringLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Toast;
import com.hoy.R;
import com.hoy.adapters.EventListAdapter;
import com.hoy.asynctasks.SyncLocalEventsAsyncTask;
import com.hoy.asynctasks.interfaces.GenericSuccessHandleable;
import com.hoy.asynctasks.interfaces.GenericSuccessListHandleable;
import com.hoy.dto.EventDTO;
import com.hoy.helpers.FragmentHelper;
import com.hoy.model.FilterParams;
import com.hoy.services.EventsService;
import com.hoy.utilities.DateUtils;

import java.util.ArrayList;
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
	protected OnItemSelectedListener listener;
	protected RadioButton todayEvents;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);    //To change body of overridden methods use File | Settings | File Templates.
		setRetainInstance(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		setHasOptionsMenu(true);
		return inflater.inflate(R.layout.fragment_events_list_menu, container, false);
	}

	@Override
	public void onActivityCreated(Bundle bundle) {
		super.onActivityCreated(bundle);
		todayEvents = (RadioButton)getActivity().findViewById(R.id.today_events);

		todayEvents.setOnCheckedChangeListener(onCheckedChangeListener);

		//we initialize with today's events
		todayEvents.setChecked(true);
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {

		listener.onItemSelected(eventDTOs.get(position));
	}

	protected void syncEventList() {

		FragmentHelper.changeProgressDialogState(getFragmentManager(),true);
		new SyncLocalEventsAsyncTask(getActivity(),getFilterParams(),new GenericSuccessListHandleable<EventDTO>(){
			public void handleSuccessCallBack(List<EventDTO> localEventDTOs) {
				FragmentHelper.changeProgressDialogState(getFragmentManager(),false);
				updateAdapter(localEventDTOs);
			}

			public void handleErrorResult() {
				FragmentHelper.changeProgressDialogState(getFragmentManager(),false);
				updateAdapter(new ArrayList<EventDTO>());
				Toast.makeText(getActivity(), R.string.no_events_to_show, Toast.LENGTH_LONG).show();
			}
		}).execute();
	}

	public interface OnItemSelectedListener {

		public void onItemSelected(EventDTO eventDTO);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		if (activity instanceof OnItemSelectedListener) {
			listener = (OnItemSelectedListener) activity;
		} else {
			throw new ClassCastException(activity.toString()
					+ " must implemenet EventListFragment.OnItemSelectedListener");
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		listener = null;
	}

	private CompoundButton.OnCheckedChangeListener onCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
		public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

			syncEventList();

		}
	};

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.menu_refresh_refresh,menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(item.getItemId() == R.id.refresh_btn){
			updateManually();
		}
		return false;

	}

		private void updateManually(){

			//if (EventsService.hasRecentlyManuallyUpdated(getActivity().getBaseContext())) {

				FragmentHelper.changeProgressDialogState(getFragmentManager(), true);
				EventsService.getInstance().synchronizeEventsFromServer(getActivity().getBaseContext(), new GenericSuccessListHandleable<EventDTO>() {
					public void handleSuccessCallBack(List<EventDTO> remoteEventDTOs) {
						updateAdapter(remoteEventDTOs);
						FragmentHelper.changeProgressDialogState(getFragmentManager(), false);
						Toast.makeText(getActivity().getBaseContext(), R.string.events_updated_succ, Toast.LENGTH_SHORT);
					}

					public void handleErrorResult() {
						FragmentHelper.changeProgressDialogState(getFragmentManager(), false);
						Toast.makeText(getActivity().getBaseContext(), R.string.connection_errors, Toast.LENGTH_SHORT);

					}
				});
			/*}
			else{
				Toast.makeText(getActivity().getBaseContext(), R.string.too_many_update_manually, Toast.LENGTH_SHORT);
			} */

		}

	private void updateAdapter(List<EventDTO> eventDTOs){
		this.eventDTOs = eventDTOs;
		ArrayAdapter<EventDTO> arrayAdapter = new EventListAdapter(getActivity(), eventDTOs, todayEvents.isChecked());
		getListView().setAdapter(arrayAdapter);

	}

	private FilterParams getFilterParams(){

		if(todayEvents.isChecked()){

			return new FilterParams(DateUtils.getTodayString());
		}
			return null;

	}




}
