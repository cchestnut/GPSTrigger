/**
 * 
 */
package com.example.gpstrigger.activities;

import com.google.android.gms.maps.model.MarkerOptions;

import android.app.Activity;
import android.os.Bundle;

/**
 * @author Chestnut
 *
 */
public class ConfigureTriggerActivity extends Activity {

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// TODO Auto-generated method stub
		//which Triggerable?
		
		
		//set trigerrable to new thread that will call obj's
		//trigger method when within Rad range of Marker
		int radius;
		MarkerOptions mo;
		Bundle b = this.getIntent().getExtras();
		if( b != null){
			//radius = b.getInt("radius");
			mo = b.getParcelable("marker");
		}
		//run thread
		
		

	}

}
