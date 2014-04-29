package com.example.gpstrigger.proxservice;

import java.util.ArrayList;
import java.util.List;

import android.app.Notification;

import com.example.gpstrigger.triggerables.Triggerable;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

public class ProximityIntentReceiver extends BroadcastReceiver {
    
    private static final int NOTIFICATION_ID = 1000;

    @Override
    public void onReceive(Context context, Intent intent) {
        
        String key = LocationManager.KEY_PROXIMITY_ENTERING;

        Boolean entering = intent.getBooleanExtra(key, false);
        
        if (entering) {
            Log.d(getClass().getSimpleName(), "entering");
        }
        else {
            Log.d(getClass().getSimpleName(), "exiting");
        }
      //execute all triggerables
        Bundle b = intent.getExtras();
        if(b.getInt("trigCount") > 0){
        	executeTrigEvents(b);
        }
        NotificationManager notificationManager = 
            (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        
        //PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, null, 0);        
        
        //Notification notification = createNotification();
        //notification.setLatestEventInfo(context, 
          //  "Proximity Alert!", "You are near your point of interest.", pendingIntent);
        
        //notificationManager.notify(NOTIFICATION_ID, notification);
        
    }
    
    private void executeTrigEvents(Bundle b){
    	List<Triggerable> build = new ArrayList<Triggerable>();
    	int cnt = b.getInt("trigCount");
    	for(int x=0;x <cnt; x++){
    		build.add((Triggerable) b.getSerializable("trigEv"+x));
    	}
    	executeTrigEvents(build);
    }
    
    private void executeTrigEvents(List<Triggerable> li){
    	//let's only handle one at a time
    	Triggerable t = li.get(0);
    	t.launch();
    }
    
    private Notification createNotification() {
        Notification notification = new Notification();
        
        //notification.icon = R.drawable.ic_menu_notifications;
        notification.when = System.currentTimeMillis();
        
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notification.flags |= Notification.FLAG_SHOW_LIGHTS;
        
        notification.defaults |= Notification.DEFAULT_VIBRATE;
        notification.defaults |= Notification.DEFAULT_LIGHTS;
        
        notification.ledARGB = Color.WHITE;
        notification.ledOnMS = 1500;
        notification.ledOffMS = 1500;
        
        return notification;
    }
    
}