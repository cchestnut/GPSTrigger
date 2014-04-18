package com.example.gpstrigger.gmap;

import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;

public class GPSTracker extends Service implements LocationListener {

	private Context mContext;
	
	boolean isGPSEnabled = false;
	public boolean isGPSEnabled() {
		return isGPSEnabled;
	}



	public void setGPSEnabled(boolean isGPSEnabled) {
		this.isGPSEnabled = isGPSEnabled;
	}



	public boolean isCanGetLocation() {
		return canGetLocation;
	}



	public void setCanGetLocation(boolean canGetLocation) {
		this.canGetLocation = canGetLocation;
	}

	boolean isNetworkEnabled = false;
	boolean canGetLocation = false;
	
	Location loc;
	double lat;
	double longitude;
	
	private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;//10 meters
	private static final long MIN_TIME_BW_UPDATES = 1000 * 60;//1 min
	
	protected LocationManager locMan;
	
	public GPSTracker(Context context){
		this.mContext = context;
		getLocation();
	}
	
	
	
	public Location getLocation(){
		try {
			locMan = (LocationManager) mContext
					.getSystemService(LOCATION_SERVICE);
			//getting gps status
			isGPSEnabled = locMan.isProviderEnabled(LocationManager.GPS_PROVIDER);
			
			isNetworkEnabled = locMan
						.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
			if (!isGPSEnabled && !isNetworkEnabled) {
                // no network provider is enabled
            } else {
                this.canGetLocation = true;
                // First get location from Network Provider
                if (isNetworkEnabled) {
                    locMan.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                    //Log.d("Network", "Network");
                    if (locMan != null) {
                        loc = locMan
                                .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (loc != null) {
                            lat = loc.getLatitude();
                            longitude = loc.getLongitude();
                        }
                    }
                }
                // if GPS Enabled get lat/long using GPS Services
                if (isGPSEnabled) {
                    if (loc == null) {
                        locMan.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                        //Log.d("GPS Enabled", "GPS Enabled");
                        if (locMan != null) {
                            loc = locMan
                                    .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (loc != null) {
                                lat = loc.getLatitude();
                                longitude = loc.getLongitude();
                            }
                        }
                    }
                }
            }
 
        } catch (Exception e) {
            e.printStackTrace();
        }
 
        return loc;
    }
	
	public double getLatitude(){
        if(loc != null){
            lat = loc.getLatitude();
        }
         
        // return latitude
        return lat;
	}
     
    /**
     * Function to get longitude
     * */
    public double getLongitude(){
        if(loc != null){
            longitude = loc.getLongitude();
        }
         
        // return longitude
        return longitude;
    }
    
    public boolean canGetLocation() {
        return this.canGetLocation;
    }
     
    public void stopUsingGPS(){
    	if(locMan != null){
    		locMan.removeUpdates(GPSTracker.this);
    	}
    }
    /**
     * Function to show settings alert dialog
     * */
    public void showSettingsAlert(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
      
        // Setting Dialog Title
        alertDialog.setTitle("GPS is settings");
  
        // Setting Dialog Message
        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");
  
        // Setting Icon to Dialog
        //alertDialog.setIcon(R.drawable.delete);
  
        // On pressing Settings button
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                mContext.startActivity(intent);
            }
        });
  
        // on pressing cancel button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            dialog.cancel();
            }
        });
  
        // Showing Alert Message
        alertDialog.show();
    }

    
	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

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

}
