package com.example.gpstrigger;

import java.io.IOException;

import com.google.android.gms.maps.*;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.model.*;

import android.app.Activity;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.widget.Toast;

public class MapPane extends Activity{
	
	GPSTracker gps;
	Location loc;
	GoogleMap map;
	LatLng home;
	MarkerOptions mo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_activity);

        // Get a handle to the Map Fragment
        map = ((MapFragment) getFragmentManager()
                .findFragmentById(R.id.map)).getMap();
        map.setOnMapClickListener(new OnMapClickHandler());
        //LatLng sydney = new LatLng(-33.867, 151.206);
        
        //Home set to your location on default
        gps = new GPSTracker(MapPane.this);
        if(gps.isGPSEnabled && gps.canGetLocation){
        	loc = gps.getLocation();
        	home = new LatLng(loc.getLatitude(), loc.getLongitude());
        }
        map.setMyLocationEnabled(true);
        if (home != null) {
        	map.moveCamera(CameraUpdateFactory.newLatLngZoom(home, 13));
        } else {
        	//map.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 13));
        }
        //Should prompt user to enter address/town,city or cancel to manually set marker
        String result;
        result = getUserAddress();
        //take address as string? and convert to latlng
        LatLng spot = processAdd(result);
        //move camera to spot
        if(spot != null){
        	map.moveCamera(CameraUpdateFactory.newLatLngZoom(spot, 13));
        }
        //if given address, place marker and ask if correct?
        boolean userCheck = promptUserCheck();
        if(userCheck){
        	mo = new MarkerOptions()
        	 .title("Trigger Location")
        	 .position(spot);
        	map.addMarker(mo);
        }
        //if not, remove marker and allow user to manually place
        
        //if correct, request radius information
        int radius = grabRadiusInfo();
        //run ConfingureTriggerableActivity
        Intent intent = new Intent(this, ConfigureTriggerActivity.class);
        Bundle b = new Bundle();
        b.putInt("radius", radius);
        b.putParcelable("marker", mo);
        intent.putExtras(b);
        startActivity(intent);
    }
    
    private int grabRadiusInfo(){
    	int num = 1;
    	return num;
    }
    
    private boolean promptUserCheck(){
    	//Toast.makeText(this, "STOP!", Toast.LENGTH_LONG);
    	return true;
    }
    
    private LatLng processAdd(String s){
    	LatLng res=null;
    	Geocoder gc = new Geocoder(this);
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
    	String res ="711 Brook St, Bryn Mawr, PA";
    	
    	return res;
    }
    
    class OnMapClickHandler implements OnMapClickListener{
    	
		@Override
		public void onMapClick(LatLng point) {
			if(map != null){
				mo = new MarkerOptions()
				 .title("Trigger Location")
				 .position(point);
				map.addMarker(mo);
			}
		}
    }
}
