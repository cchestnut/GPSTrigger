package com.example.gpstrigger.proxservice;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.os.IBinder;

import com.example.gpstrigger.triggerables.Triggerable;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class ProxAlertManager extends Service 
							implements LocationListener{

	Triggerable trig;
	MarkerOptions mo;
	LatLng pos;
	int radius;
	
	private boolean checkLocal(Location l1){
		if(mo != null){
			pos = mo.getPosition();
		}
		
		return false;
	}

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
	
	}

	 public void onStart(Intent intent, int startId) {
		  int start = Service.START_STICKY;
	 }
	
	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		//LocationClient cc;
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void onDestroy() {
		  // TODO Auto-generated method stub
		  //Log.i("info", "Service is destroyed");
		  //mLocationClient.removeLocationUpdates(this);
		  super.onDestroy();
	}
}
