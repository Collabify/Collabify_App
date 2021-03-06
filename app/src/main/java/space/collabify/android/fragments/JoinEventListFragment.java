package space.collabify.android.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.location.LocationRequest;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import space.collabify.android.managers.AppManager;
import space.collabify.android.requests.EventsRequest;
import space.collabify.android.R;
import space.collabify.android.activities.JoinEventActivity;
import space.collabify.android.models.Event;

/**
 * Created by andrew on 3/20/15.
 */
public class JoinEventListFragment extends SwipeRefreshListFragment {
    private static final String TAG = JoinEventListFragment.class.getSimpleName();

    private static final String ENABLE_GPS = "Please enable GPS to see events";
    private static final String NO_EVENTS = "No events found.\nStart your own by hitting the + below!";
    public boolean isGpsEnabled = false;

    private Location mLastKnownLocation;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Event tmpEvent;
        if(!isGpsEnabled){
            tmpEvent = new Event(ENABLE_GPS, "9999", "", false);
        }else{
            tmpEvent = new Event(NO_EVENTS, "9999", "", false);
        }

        List<Event> temp = new ArrayList<>();
        temp.add(tmpEvent);
        EventListAdapter adapter = new EventListAdapter(getActivity().getApplicationContext(), temp);
        setListAdapter(adapter);

        setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initiateRefresh();
            }
        });
        initiateRefresh();
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        //super.onListItemClick(l, v, position, id);
        final Event item = (Event)l.getItemAtPosition(position);

        //shouldn't be able to click on the dummy message
        if(item.getName().equalsIgnoreCase(ENABLE_GPS) || item.getName().equalsIgnoreCase(NO_EVENTS)){
            return;
        }

        //either prompt to join event, or to enter password
        if(item == null){
            //not sure if possible, but can't do anything
            return;
        }

        if (!AppManager.getInstance().isEventUpdating()) {
            if (item.isProtectedEvent()) {
                setupPasswordDialog(item);
            } else {
                setupJoinDialog(item);
            }
        }

    }

    /**
     * Prompts user if they want to actually join the event they clicked on
     * see helpful link: http://stackoverflow.com/questions/10903754/input-text-dialog-android
     *
     * @param event
     */
    private void setupJoinDialog(final Event event){
        //just prompt to join
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.join_event_dialog_title));
        builder.setMessage(event.getName());
        builder.setPositiveButton(getString(R.string.join_event_dialog_positive_text),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //go on to event
                        ((JoinEventActivity) getActivity()).toCollabifier(event, "");
                    }
                });
        builder.setNegativeButton(getString(R.string.join_event_dialog_negative_text),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        builder.show();
    }

    /**
     * Prompts user for event password and verifies it before going on
     * @param event
     */
    private void setupPasswordDialog(final Event event){
        //TODO: may have to change how password is handled/displayed
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Join Password Protected Event?");
        builder.setMessage(event.getName() + " - password:");

        //set up password input
        final EditText input = new EditText(getActivity());
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        builder.setView(input);
        builder.setPositiveButton(getString(R.string.join_event_dialog_positive_text),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //maybe go on to event
                        ((JoinEventActivity) getActivity()).toCollabifier(event, input.getText().toString());
                    }
                });
        builder.setNegativeButton(getString(R.string.join_event_dialog_negative_text),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        builder.show();
    }
    /**
     * Starts a LoadEventsTask in background with user location
     */
    private void initiateRefresh() {
        Log.i(TAG, "initiate event list refresh");
        final Activity activity = getActivity();

        if(AppManager.getInstance().getLastKnownLocation() != null){
            mLastKnownLocation = AppManager.getInstance().getLastKnownLocation();
        }
        // load the events
        if(mLastKnownLocation != null){
            AppManager.getInstance().loadEvents(Double.toString(mLastKnownLocation.getLatitude()),
                    Double.toString(mLastKnownLocation.getLongitude()), new Callback<List<Event>>() {
                @Override
                public void success(final List<Event> events, Response response) {
                    if(activity != null){
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                onRefreshComplete(events);
                            }
                        });
                    }

                }

                @Override
                public void failure(RetrofitError retrofitError) {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            onRefreshComplete(null);
                        }
                    });
                }
            });
        }else{
            setRefreshing(false);
        }
    }

    /**
     * Callback from the LoadEventsTask for when the data has been fetched from server
     * @param events list of events close to user
     */
    private void onRefreshComplete(List<Event> events){
        // Remove all items from the ListAdapter, and then replace them with the new items
        EventListAdapter adapter = (EventListAdapter) getListAdapter();
        adapter.clear();

        if(events == null || events.size() == 0){
            Event tmp = new Event(NO_EVENTS, "9999", "", false);
            adapter.add(tmp);
        }else{
            for (Event event: events) {
                adapter.add(event);
            }
        }

        // Stop the refreshing indicator
        setRefreshing(false);
    }


    /**
     * Displays the refresh circle and gets the events around the user
     */
    public void initializeList() {
        if(!isRefreshing()){
            setRefreshing(true);
            initiateRefresh();
        }
    }


    /**
     * Handles the display of events in a row
     */
    private class EventListAdapter extends ArrayAdapter<Event> {
        private EventListAdapter(Context context, List<Event> objects) {
            super(context, R.layout.join_event_list_row, objects);
        }

        private EventListAdapter(Context context, Event[] events) {
            super(context, R.layout.join_event_list_row,events);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            View customView  = inflater.inflate(R.layout.join_event_list_row, parent, false);

            Event eventItem = getItem(position);
            TextView rowText = (TextView) customView.findViewById(R.id.join_event_row_text);
            ImageView rowImage = (ImageView) customView.findViewById(R.id.join_event_row_image);

            rowText.setText(eventItem.getName());
            int visibility = eventItem.isProtectedEvent()? View.VISIBLE: View.INVISIBLE;
            rowImage.setVisibility(visibility);

            return customView;
        }
    }
}
