package com.example.gpstrigger.activities;

import java.io.IOException;
import java.util.ArrayList;

import com.example.gpstrigger.Constants;
import com.example.gpstrigger.R;
import com.example.gpstrigger.R.id;
import com.example.gpstrigger.R.layout;
import com.example.gpstrigger.gmap.GPSTracker;
import com.example.gpstrigger.gmap.AddressDialog.EditNameDialogListener;
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
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
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
        gc = new Geocoder(this.getApplicationContext());
        if(gps.isGPSEnabled() && gps.canGetLocation()){
        	loc = gps.getLocation();
        	home = new LatLng(loc.getLatitude(), loc.getLongitude());
        	try{
        		ArrayList<Address> al = 
        			(ArrayList<Address>) gc.getFromLocation(loc.getLatitude(), loc.getLongitude(), 1);
        		//if(!al.isEmpty())
        		//mo = home;
        		uAddress = al.get(0).getAddressLine(0);	
        	} catch (IOException e){
        		e.printStackTrace();
        	} catch (IndexOutOfBoundsException e){
        		e.printStackTrace();
        	}
        	home = new LatLng(loc.getLatitude(), loc.getLongitude());
        }
        map.setMyLocationEnabled(true);
        plot(home);
    	getUserAddress();     
        
    }
    private void plot(Location l){
    	plot(new LatLng(l.getLatitude(), l.getLongitude()));    	
    }
    private void plot(String c){
        plot(processAdd(c));
    }
    private void plot(LatLng ll){
    	if(ll == null){
    		ll = home;
    	}
    	
    	if(map != null && ll != null){
    		map.moveCamera(CameraUpdateFactory.newLatLngZoom(ll, 17.0f));
    	}
    	map.clear();
		mo = new MarkerOptions()
		 .title("Trigger Location")
		 .position(ll);
		map.addMarker(mo);
    }
    protected void onStart(){
    	//Should prompt user to enter address/town,city or cancel to manually set marker
    	super.onStart();     
        
    }
    
    protected void onActivityResult(int reqCode, int resCode, Intent data){
    	if(resCode != this.RESULT_OK)
    		return;
    	//Toast t;
    	//t = Toast.makeText(this.getApplicationContext(), "help--------", Toast.LENGTH_LONG);
    	if(reqCode == Constants.addressRequestCode){
    		Bundle b = data.getExtras();
    		if(b != null){
    			uAddress = b.getString("address");
    			plot(uAddress);
    			promptUserCheck();
    		}
    	}
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
    	//alert.dismiss();
    }
    
    private LatLng processAdd(String s){
    	LatLng res=null;
    	String locString = s;
    	try{
    		ArrayList<Address> ls = (ArrayList<Address>) gc.getFromLocationName(locString, 1);
    		if(!ls.isEmpty()){
    			Address ad = ls.get(0);
    			res = new LatLng(ad.getLatitude(),ad.getLongitude());
    		}
    	}catch(IOException e){
    		e.printStackTrace();
    	} catch (NullPointerException e){
    		e.printStackTrace();
    	} catch (IndexOutOfBoundsException e){
    		e.printStackTrace();
    	}
    	return res;
    }
    
    private void getUserAddress(){
    	Intent intent = new Intent(this, AddressLauncher.class);
    	this.startActivityForResult(intent, Constants.addressRequestCode);
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
