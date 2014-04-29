package com.example.gpstrigger.activities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.example.gpstrigger.Constants;
import com.example.gpstrigger.triggerables.*;
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
import android.widget.EditText;

public class MainActivity extends Activity implements OnClickListener{

	//Button btnShowLocation;
    LatLng trigLoc;
    // GPSTracker class
    GPSTracker gps;
    int radius;
    List<Triggerable> trigList;
     
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button mapLaunch = (Button) this.findViewById(R.id.mapButton);
        mapLaunch.setOnClickListener(this);
        Button newTrig = (Button) this.findViewById(R.id.trigButton1);
        newTrig.setOnClickListener(this);
        Button ac = (Button) this.findViewById(R.id.actButton);
        ac.setOnClickListener(this);
        Button up = (Button) this.findViewById(R.id.moreRadButton);
        up.setOnClickListener(this);
        Button down = (Button) this.findViewById(R.id.lessRadButton);
        down.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //Toast t = Toast.makeText(this.getApplicationContext(), "res", Toast.LENGTH_SHORT);
        //t.show();
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
        	Bundle b = data.getExtras();
        	if(b != null){
        		Triggerable t = (Triggerable) b.getSerializable("trig");
        		if(trigList == null){
        			trigList = new ArrayList<Triggerable>();       			
        		}
        		trigList.add(t);
        		//if(t instanceof TTrigger)
        		Button bt = (Button)findViewById(R.id.trigButton1);
        		bt.setText(t.toString());
        	}
        	
        }
    }

	@Override
	public void onClick(View v) {
		//Toast t = Toast.makeText(this.getApplicationContext(), "Lets go - " +v.getId() + " " + mapLaunch.getId(), Toast.LENGTH_LONG);
		//t.show();
		// TODO Auto-generated method stub
		EditText radText = ((EditText) findViewById(R.id.radiusBox));
		if(v.getId() == R.id.mapButton){
			Intent intent = new Intent(this, MapPane.class);
			this.startActivityForResult(intent, Constants.mapRequestCode);
			//t.show();
		} else if (v.getId() == R.id.actButton){
			if(radText.getText() == null)
				radText.setText("1");
			String radS = radText.getText().toString();
			radius = Integer.parseInt(radS);
			if(trigLoc != null && !trigList.isEmpty()){
				Bundle b = new Bundle();				
				b.putParcelable("trigLoc", trigLoc);
				b.putInt("trigCount", trigList.size());
				b.putInt("radius", radius);
				int c = 0;
				for(Triggerable t: trigList)
				{
					b.putSerializable("trigEv"+c, (Serializable) t); 
				}
				//b.putSerializable("trigList", trigList);
				Intent intent = new Intent(this, ProxAlertActivity.class);
				intent.putExtras(b);
				//this.startActivityForResult(intent, Constants.);
				startActivity(intent);
				finish();
			}
		} else if (v.getId() == R.id.trigButton1){
			//Dialog intent
			Intent intent = new Intent(this, TextTrigger.class);
			this.startActivityForResult(intent, Constants.trigRequestCode);
		}else if (v.getId() == R.id.moreRadButton){
			String r = radText.getText().toString();
			if(r.length() > 0){
				radius = Integer.parseInt(r) + 1;
			} else{
				radius = 1;
			}
			radText.setText(radius + "");
			
		}else if (v.getId() == R.id.lessRadButton){
			String r = radText.getText().toString();
			if(r.length() > 0){
				radius = Integer.parseInt(r) - 1;
			} else{
				radius = 1;
			}
			radText.setText(radius + "");
		}
		
	}
    
}
