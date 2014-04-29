/**
 * 
 */
package com.example.gpstrigger.activities;

import com.example.gpstrigger.gmap.AddressDialog;
import com.example.gpstrigger.gmap.AddressDialog.EditNameDialogListener;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
// ...

public class AddressLauncher extends FragmentActivity implements EditNameDialogListener {

	AddressDialog ad;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showEditDialog();
    }

    private void showEditDialog() {
        FragmentManager fm = getSupportFragmentManager();
        ad = new AddressDialog();
        ad.show(fm, "addressdialog");
    }

    @Override
    public void onFinishEditDialog(String inputText) {    	
    	Intent intent = new Intent();
        Bundle b = new Bundle();
        // b.putInt("radius", radius);
        b.putString("address", inputText);
        intent.putExtras(b);
        this.setResult(RESULT_OK, intent);
        ad.dismiss();
        finish();
    }
}
