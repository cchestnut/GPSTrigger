package com.example.gpstrigger.activities;

import java.io.IOException;

import com.example.gpstrigger.R;
import com.example.gpstrigger.R.id;
import com.example.gpstrigger.R.layout;
import com.example.gpstrigger.gmap.GPSTracker;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.model.*;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
//import android.widget.Toast;
import android.widget.Toast;

public class MapPane extends Activity implements OnClickListener, OnMapClickListener{
	
	GPSTracker gps;
	Location loc;
	GoogleMap map;
	LatLng home;
	MarkerOptions mo;
	AlertDialog alert;
	Geocoder gc;
	String uAddress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Toast t = Toast.makeText(this.getApplicationContext(), "Lets go", Toast.LENGTH_LONG);
        //t.show();
        setContentView(R.layout.map_activity);

        // Get a handle to the Map Fragment
        map = ((MapFragment) getFragmentManager()
                .findFragmentById(R.id.map)).getMap();
        map.setOnMapClickListener(this);
        //LatLng sydney = new LatLng(-33.867, 151.206);
        
        //Home set to your location on default
        gps = new GPSTracker(MapPane.this);
        gc = new Geocoder(this);
        if(gps.isGPSEnabled() && gps.canGetLocation()){
        	loc = gps.getLocation();
        	home = new LatLng(loc.getLatitude(), loc.getLongitude());
        }
        map.setMyLocationEnabled(true);
        if (home != null) {
        	map.moveCamera(CameraUpdateFactory.newLatLngZoom(home, 13));
        } else {
        	//map.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 13));
        }
        
        //if not, remove marker and allow user to manually place
        
        //if correct, request radius information
        //int radius = grabRadiusInfo();
        //run ConfingureTriggerableActivity
        
        
        //startActivity(intent);
        
    }
    protected void onStart(){
    	//Should prompt user to enter address/town,city or cancel to manually set marker
    	super.onStart();
        uAddress = getUserAddress();
        //take address as string? and convert to latlng
        LatLng spot = processAdd(uAddress);
        //move camera to spot
        if(spot != null){
        	map.moveCamera(CameraUpdateFactory.newLatLngZoom(spot, 13));
        }
        //if given address, place marker and ask if correct?

		mo = new MarkerOptions()
       	 .title("Trigger Location")
       	 .position(spot);
       	map.addMarker(mo);
        promptUserCheck();
        
    }
    
    
    private int grabRadiusInfo(){
    	int num = 1;
    	return num;
    }
    
    private boolean promptUserCheck(){
    	//Toast.makeText(this, "STOP!", Toast.LENGTH_LONG);AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
		//context);
		 Builder alertDialogBuilder = new Builder(this); 
		// set title
		alertDialogBuilder.setTitle("Confirm");
	
		// set dialog message
		alertDialogBuilder
			.setMessage("Trigger Location Set?")
			.setCancelable(false)
			.setPositiveButton("Done",this)
			.setNegativeButton("Nope", this);

		// create alert dialog
		alert = alertDialogBuilder.create();

		// show it
    	alert.show();		
		return true;
    }
    public void onClick(DialogInterface dialog,int id) {
		// if this button is clicked, just close
		// the dialog box and do nothing
		dialog.dismiss();
    	if(id == DialogInterface.BUTTON_POSITIVE){
	       	Intent intent = new Intent();
	        Bundle b = new Bundle();
	        // b.putInt("radius", radius);
	        b.putParcelable("marker", mo);
	        b.putString("locName", uAddress);
	        intent.putExtras(b);
	        this.setResult(RESULT_OK, intent);
	        finish();
       }
       
	}
    
    protected void onPause(){
    	super.onPause();
    	alert.dismiss();
    }
    
    private LatLng processAdd(String s){
    	LatLng res=null;
    	String locString = s;
    	try{
    		Address ad = (gc.getFromLocationName(locString, 1)).get(0);
    		res = new LatLng(ad.getLatitude(),ad.getLongitude());
    	}catch(IOException e){
    		e.printStackTrace();
    	}
    	return res;
    }
    
    private String getUserAddress(){
    	String res ="683 Colts Neck Rd, Freehold, NJ";
    	
    	return res;
    }
    
    public void onMapClick(LatLng point) {
			if(map != null){
				map.clear();
				mo = new MarkerOptions()
				 .title("Trigger Location")
				 .position(point);
				map.addMarker(mo);
				try{
	    		uAddress = gc.getFromLocation(point.latitude, point.longitude, 1).get(0).getAddressLine(0);
				}catch(IOException e){
					e.printStackTrace();
				}
				promptUserCheck();
			}
		}
    
}
