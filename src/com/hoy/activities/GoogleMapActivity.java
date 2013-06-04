package com.hoy.activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.widget.Toast;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.hoy.R;
import com.hoy.constants.MilongaHoyConstants;

/**
 * Created with IntelliJ IDEA.
 * User: ksairi
 * Date: 6/3/13
 * Time: 10:56 AM
 * To change this template use File | Settings | File Templates.
 */
public class GoogleMapActivity extends GenericActivity{

	GoogleMap map;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if(map == null){

			int connectionResult = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);

			if(ConnectionResult.SUCCESS == connectionResult){

				if(isGoogleMapsInstalled()){
					setContentView(R.layout.google_map);
					map = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();

					Double eventLatitude = Double.valueOf((String)getIntent().getExtras().get(MilongaHoyConstants.EVENT_LATITUDE));
					Double eventLongitude = Double.valueOf((String)getIntent().getExtras().get(MilongaHoyConstants.EVENT_LONGITUDE));
					String eventName = getIntent().getExtras().getString(MilongaHoyConstants.EVENT_NAME);
					String eventAddress = getIntent().getExtras().getString(MilongaHoyConstants.EVENT_ADDRESS);
					getIntent().getExtras().remove(MilongaHoyConstants.EVENT_LATITUDE);
					getIntent().getExtras().remove(MilongaHoyConstants.EVENT_LONGITUDE);
					getIntent().getExtras().remove(MilongaHoyConstants.EVENT_NAME);
					getIntent().getExtras().remove(MilongaHoyConstants.EVENT_ADDRESS);

					LatLng  milongaLocation = new LatLng(eventLatitude,eventLongitude);

					map.addMarker(new MarkerOptions()
							.position(milongaLocation)
							.title(eventName)
							.snippet(eventAddress)
							.icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_bubble)));

					//add user's location

					LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
					Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);

					if(location != null){
						Double userLatitude = location.getLatitude();
						Double userLongitude = location.getLongitude();

						LatLng userLocation = new LatLng(userLatitude,userLongitude);
						map.addMarker(new MarkerOptions()
									   .position(userLocation)
										.title(getResources().getString(R.string.you_are_here)));

					}

					 // Move the camera instantly to milongaLocation with a zoom of 15.
					map.moveCamera(CameraUpdateFactory.newLatLngZoom(milongaLocation, 15));

					// Zoom in, animating the camera.
					//map.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);

				}
				else{
					redirectToMarket();
				}
			}
			else{
				redirectToMarket();
			}
		}


	}


	@Override
	protected Context getContext() {
		return this;
	}

	@Override
	    public boolean onCreateOptionsMenu(Menu menu) {
	        // Inflate the menu; this adds items to the action bar if it is present.
	        getMenuInflater().inflate(R.menu.google_map, menu);
	        return true;
		}

	private void redirectToMarket(){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		            builder.setMessage(R.string.most_recent_google_play_services_needed);
		            builder.setCancelable(false);
		            builder.setPositiveButton(R.string.install, getGoogleMapsListener());
		            AlertDialog dialog = builder.create();
		            dialog.show();


	}

	public DialogInterface.OnClickListener getGoogleMapsListener()
	{
	    return new DialogInterface.OnClickListener()
	    {
	        public void onClick(DialogInterface dialog, int which)
	        {
	            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.google.android.apps.maps"));
	            startActivity(intent);

	            //Finish the activity so they can't circumvent the check
	            finish();
	        }
	    };
	}

	public boolean isGoogleMapsInstalled()
	{
	    try
	    {
	     	getPackageManager().getApplicationInfo("com.google.android.apps.maps", 0 );
	        return true;
	    }
	    catch(PackageManager.NameNotFoundException e)
	    {
	        return false;
	    }
			}
}
