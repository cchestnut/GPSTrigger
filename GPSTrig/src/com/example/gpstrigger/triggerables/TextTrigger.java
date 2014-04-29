/**
 * 
 */
package com.example.gpstrigger.triggerables;

import java.io.Serializable;

import com.example.gpstrigger.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.telephony.SmsManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
 
public class TextTrigger extends Activity implements OnClickListener{
 
	Button buttonSend;
	EditText textPhoneNo;
	EditText textSMS;
 
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.textlayout);
 
		buttonSend = (Button) findViewById(R.id.buttonSend);
		textPhoneNo = (EditText) findViewById(R.id.editTextPhoneNo);
		textSMS = (EditText) findViewById(R.id.editTextSMS);
 
		buttonSend.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		String phoneNo = textPhoneNo.getText().toString();
		String sms = textSMS.getText().toString();
		TTrigger t = new TTrigger(sms, phoneNo);
		Intent intent = new Intent();
        Bundle b = new Bundle();
        // b.putInt("radius", radius);
        b.putSerializable("trig", t);
        intent.putExtras(b);
        this.setResult(RESULT_OK, intent);
        finish();
	}

	
}

class TTrigger implements Triggerable, Serializable{
	String mess, phone;
	
	TTrigger(String m, String p){
		mess = m;
		phone = p;
	}
	
	@Override
	public void launch() {
		// TODO Auto-generated method stub 
		try {
		SmsManager smsManager = SmsManager.getDefault();
		smsManager.sendTextMessage(phone, null, mess, null, null);
		/*Toast.makeText(getApplicationContext(), "SMS Sent!",
					Toast.LENGTH_LONG).show();*/
	  } catch (Exception e) {
		/*Toast.makeText(getApplicationContext(),
			"SMS faild, please try again later!",
			Toast.LENGTH_LONG).show();*/
		e.printStackTrace();
	  }
		
	}
	public String toString(){
		return "Text Event - " + mess;
	}

}
