package com.hoy.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.google.analytics.tracking.android.Tracker;
import com.hoy.R;
import com.hoy.adapters.EventListAdapter;
import com.hoy.asynctasks.SyncLocalEventsAsyncTask;
import com.hoy.asynctasks.interfaces.GenericSuccessListHandleable;
import com.hoy.constants.MilongaHoyConstants;
import com.hoy.dto.EventDTO;
import com.hoy.helpers.AnalyticsHelper;
import com.hoy.helpers.FragmentHelper;
import com.hoy.helpers.SharedPreferencesHelper;
import com.hoy.model.FilterParams;
import com.hoy.schedulers.EventsScheduler;
import com.hoy.services.EventsService;
import com.hoy.utilities.DateUtils;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ScheduledFuture;

/**
 * Created with IntelliJ IDEA.
 * User: ksairi
 * Date: 5/21/13
 * Time: 9:10 PM
 * To change this template use File | Settings | File Templates.
 */
public class EventListFragment extends ListFragment {

    protected List<EventDTO> eventDTOs, todayEventsDTO;
    protected EventListFragmentListener listener;
    protected CheckBox todayEvents;
    private Activity activityAttached;
    private TextView syncLocalEvents;
    private Boolean newMilongasUpdate = false;
    private TextView todayDateTextView;
    private ScheduledFuture scheduleFutureHourly;
    private ScheduledFuture scheduleFutureDaily;
    Tracker tracker;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setRetainInstance(true);
        return inflater.inflate(R.layout.fragment_events_list_menu, container, false);
    }

    @Override
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        tracker = AnalyticsHelper.getDefaultTracker(getActivity().getApplicationContext());

        if(tracker != null && !MilongaHoyConstants.DEBUG){
            tracker.sendEvent(MilongaHoyConstants.ANALYTICS_SCREEN_EVENTS_CATEGORY, MilongaHoyConstants.ANALYTICS_OPEN_SCREEN_ACTION, MilongaHoyConstants.ANALYTICS_OPEN_LIST_EVENT_LABEL, null);
        }
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

        setTodayDateTextView(DateUtils.getTodayDateToShow());
    }

    @Override
    public void onResume() {
        super.onResume();
        todayEvents.setText(R.string.today_list_events_label);
        if (newMilongasUpdate) {
            syncEventList();
            newMilongasUpdate = false;
        } else {
            String savedLastUpdateString = SharedPreferencesHelper.getValueInSharedPreferences(getActivity(), MilongaHoyConstants.LAST_FULL_UPDATE_DATE);
            try {
                Date savedLastUpdate = DateUtils.getDateFromString(savedLastUpdateString);
                Date now = DateUtils.getTodayCleanTime().getTime();
                if (now.after(savedLastUpdate) || now.before(savedLastUpdate)) {
                    setTodayDateTextView(DateUtils.getTodayDateToShow());
                    SharedPreferencesHelper.setValueSharedPreferences(getActivity(), MilongaHoyConstants.LAST_FULL_UPDATE_DATE,DateUtils.getTodayAndTimeString());
                    syncRemoteEvents();
                }
            } catch (ParseException e) {
                e.printStackTrace();
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
                reScheduleTasks();
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
                updateAdapter(new ArrayList<EventDTO>());
                Toast.makeText(activityAttached, R.string.no_events_to_show, Toast.LENGTH_SHORT).show();
            }

            public void handleErrorCallBack(List<EventDTO> localEventDTOs) {

            }
        }).execute();

    }

    public interface EventListFragmentListener {

        public void onItemSelected(EventDTO eventDTO, Integer index);

        public void onDateOptionChanged();

        public void onClickTodaysMap(List<EventDTO> eventDTOs);

        public void autoSelectFirst(EventDTO eventDTO, Integer index);

        public void showEmptyEventList();

        public void updateManuallyCallBack();
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
        public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
            listener.onDateOptionChanged();
            if (checked) {
                updateAdapter(todayEventsDTO);
            } else {
                syncEventList();
            }


        }
    };

    private View.OnClickListener onClickTodaysMap = new View.OnClickListener() {
        public void onClick(View view) {
            if (todayEventsDTO != null && !todayEventsDTO.isEmpty()) {
                if(tracker != null && !MilongaHoyConstants.DEBUG){
                    tracker.sendEvent(MilongaHoyConstants.ANALYTICS_SCREEN_EVENTS_CATEGORY, MilongaHoyConstants.ANALYTICS_OPEN_SCREEN_ACTION, MilongaHoyConstants.ANALYTICS_OPEN_TODAYS_MAP_LABEL, null);
                }
                listener.onClickTodaysMap(todayEventsDTO);
            } else {
                Toast.makeText(activityAttached, R.string.no_events_to_show, Toast.LENGTH_SHORT).show();
            }
        }
    };


    public void updateManually() {

            EventsService.getInstance().synchronizeEventsFromServer(activityAttached, null, getFilterParams(), new GenericSuccessListHandleable<EventDTO>() {
                public void handleSuccessCallBack(List<EventDTO> eventDTOs) {

                    SharedPreferencesHelper.setValueSharedPreferences(getActivity(), MilongaHoyConstants.LAST_FULL_UPDATE_DATE, DateUtils.getTodayAndTimeString());
                    updateAdapter(eventDTOs);
                    Toast.makeText(activityAttached, R.string.manually_update_success, Toast.LENGTH_SHORT).show();
                    listener.updateManuallyCallBack();

                }

                public void handleErrorResult() {
                    listener.updateManuallyCallBack();
                    //Toast.makeText(activityAttached, R.string.connection_errors, Toast.LENGTH_SHORT).show();

                }

                public void handleErrorCallBack(List<EventDTO> eventDTOs) {
                    listener.updateManuallyCallBack();
                    //Toast.makeText(activityAttached, R.string.connection_errors, Toast.LENGTH_SHORT).show();
                }
            });
    }

    private void updateAdapter(List<EventDTO> eventDTOs) {
        if (activityAttached != null && eventDTOs != null && !eventDTOs.isEmpty() && activityAttached != null) {
            this.eventDTOs = eventDTOs;
            if (todayEvents.isChecked()) {
                todayEventsDTO = eventDTOs;
            }
            ArrayAdapter<EventDTO> arrayAdapter = new EventListAdapter(activityAttached, eventDTOs, todayEvents.isChecked());
            getListView().setAdapter(arrayAdapter);
            listener.onDateOptionChanged();
            activityAttached.findViewById(R.id.list_view_labels).setVisibility(View.VISIBLE);
            listener.autoSelectFirst(eventDTOs.get(0), 0);
        } else {
            if (activityAttached != null) {
                listener.showEmptyEventList();
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
                        }
                    }
                }
            }

        }
    };

    private void setTodayDateTextView(String todayString) {

        todayDateTextView.setText("(".concat(todayString).concat(")"));
    }


}
