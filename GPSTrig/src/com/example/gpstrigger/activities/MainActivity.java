package com.example.gpstrigger.activities;

import com.example.gpstrigger.Constants;
import com.example.gpstrigger.R;
import com.example.gpstrigger.R.id;
import com.example.gpstrigger.R.layout;
import com.example.gpstrigger.R.menu;
import com.example.gpstrigger.gmap.GPSTracker;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.location.Location;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener{

	//Button btnShowLocation;
    LatLng trigLoc;
    // GPSTracker class
    GPSTracker gps;
    Button mapLaunch;
     
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mapLaunch = (Button) this.findViewById(R.id.mapButton);
        mapLaunch.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Toast t = Toast.makeText(this.getApplicationContext(), "res", Toast.LENGTH_SHORT);
        t.show();
    	if(resultCode != this.RESULT_OK){
        	return;
        }
    	if (requestCode == Constants.mapRequestCode) {
           Bundle b = data.getExtras();
           if(b != null){
        	   MarkerOptions mo;
        	   String locName;
        	   mo = b.getParcelable("marker");
        	   locName = b.getString("locName");
        	   trigLoc = mo.getPosition();
        	   TextView tv = (TextView)findViewById(R.id.mapStatus);
        	   tv.setText(locName);
           }
        } else if ( requestCode == Constants.trigRequestCode){
        	
        }
    }

	@Override
	public void onClick(View v) {
		//Toast t = Toast.makeText(this.getApplicationContext(), "Lets go - " +v.getId() + " " + mapLaunch.getId(), Toast.LENGTH_LONG);
		//t.show();
		// TODO Auto-generated method stub
		if(v.getId() == mapLaunch.getId()){
			Intent intent = new Intent(this, MapPane.class);
			this.startActivityForResult(intent, Constants.mapRequestCode);
			//t.show();
		}
		
	}
    
}
