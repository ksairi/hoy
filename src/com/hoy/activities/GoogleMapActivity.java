package com.hoy.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.hoy.R;
import com.hoy.constants.MilongaHoyConstants;
import com.hoy.dto.EventDTO;
import com.hoy.helpers.AddressHelper;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: ksairi
 * Date: 6/3/13
 * Time: 10:56 AM
 * To change this template use File | Settings | File Templates.
 */
public class GoogleMapActivity extends GenericActivity {

    GoogleMap map;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);


        if (map == null) {

            int connectionResult = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
            if (ConnectionResult.SUCCESS != connectionResult) {
                GooglePlayServicesUtil.getErrorDialog(connectionResult, this, 0, new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        finish();
                    }
                }).show();
            } else {

                if (isGoogleMapsInstalled()) {
                    setContentView(R.layout.google_map);
                    map = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();

                    Bundle extras = getIntent().getExtras();

                    if (extras != null) {
                        if (!extras.containsKey(MilongaHoyConstants.TODAY_DTOS)) {
                            //coming from DetailFragment, show only the event selected
                            Double eventLatitude = extras.getDouble(MilongaHoyConstants.EVENT_LATITUDE);
                            Double eventLongitude = extras.getDouble(MilongaHoyConstants.EVENT_LONGITUDE);
                            String eventName = extras.getString(MilongaHoyConstants.EVENT_NAME);
                            String eventAddress = extras.getString(MilongaHoyConstants.EVENT_ADDRESS);
                            extras.remove(MilongaHoyConstants.EVENT_LATITUDE);
                            extras.remove(MilongaHoyConstants.EVENT_LONGITUDE);
                            extras.remove(MilongaHoyConstants.EVENT_NAME);
                            extras.remove(MilongaHoyConstants.EVENT_ADDRESS);
                            addElementsInMap(eventName, eventAddress, eventLatitude, eventLongitude);
                        } else {
                            //coming from EventListFragment, showing today's events
                            List<EventDTO> eventDTOs = extras.getParcelableArrayList(MilongaHoyConstants.TODAY_DTOS);
                            addElementsInMap(eventDTOs);
                        }
                    }


                } else {
                    redirectToMarket();
                }
            }
        }
    }

    private void addElementsInMap(String eventName, String address, Double latitude, Double longitude) {

        LatLng milongaLocation = new LatLng(latitude, longitude);

        addMarker(milongaLocation, eventName, address);

        addUserLocation();

        // Move the camera instantly to milongaLocation with a zoom of 15.
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(milongaLocation, 15));

        // Zoom in, animating the camera.
        //map.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);

    }

    private void addElementsInMap(List<EventDTO> eventDTOs) {

        LatLng milongaLocation = null;
        for (EventDTO eventDTO : eventDTOs) {

            try {
                Double latitude = Double.parseDouble(eventDTO.getLatitude());
                Double longitude = Double.parseDouble(eventDTO.getLongitude());
                String name = eventDTO.getName() == null ? MilongaHoyConstants.EMPTY_STRING : eventDTO.getName();

                if (latitude != null && longitude != null) {

                    milongaLocation = new LatLng(latitude, longitude);

                    addMarker(milongaLocation, name, AddressHelper.getEventAddress(eventDTO));
                }
            } catch (NumberFormatException e) {

            }
        }

        addUserLocation();

        if (milongaLocation != null) {
            // Move the camera instantly to milongaLocation with a zoom of 13.
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(milongaLocation, 13));
        }

        // Zoom in, animating the camera.
        //map.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);

    }

    private void addMarker(LatLng milongaLocation, String eventName, String address) {

        map.addMarker(new MarkerOptions()
                .position(milongaLocation)
                .title(eventName)
                .snippet(address)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_bubble)));
    }

    private void addUserLocation() {

        //add user's location

        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        if (location != null) {
            Double userLatitude = location.getLatitude();
            Double userLongitude = location.getLongitude();

            LatLng userLocation = new LatLng(userLatitude, userLongitude);
            map.addMarker(new MarkerOptions()
                    .position(userLocation)
                    .title(getResources().getString(R.string.you_are_here)));

        }

    }


    private void redirectToMarket() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.google_maps_not_found);
        builder.setCancelable(false);
        builder.setPositiveButton(R.string.install, getGoogleMapsListener());
        AlertDialog dialog = builder.create();
        dialog.show();


    }

    public DialogInterface.OnClickListener getGoogleMapsListener() {
        return new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.google.android.apps.maps"));
                startActivity(intent);

                //Finish the activity so they can't circumvent the check
                finish();
            }
        };
    }

    public boolean isGoogleMapsInstalled() {
        try {
            getPackageManager().getApplicationInfo("com.google.android.apps.maps", 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return true;
    }
}
