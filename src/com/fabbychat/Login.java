package com.fabbychat;

import org.jivesoftware.smack.XMPPException;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.FacebookError;
import com.facebook.android.Facebook.DialogListener;

import com.fabbychat.Application;

public class Login extends Activity {
	
	private static final String 
		TAG = "Login",
		FB_APP_ID = "118419214873353";
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    
    @Override
    public void onStart() {
    	super.onStart();
        Facebook fb = new Facebook(FB_APP_ID);
        // open Facebook login dialog
		fb.authorize(this,
			new String[] { "xmpp_login", "offline_access" }, 
			new LoginDialogListener());
    }
    
    public class LoginDialogListener implements DialogListener {
    	
    	// make XMPP connection
        public void onComplete(Bundle values) {
        	FbChatService.LocalBinder service = 
        		((Application) getApplication()).getService();
	    	try {
				service.login(values.getString("session_key"));
			} catch (XMPPException e) {
				alert("Failed to connect with Facebook Chat.", Login.this);
	            Log.e(TAG, "Exception occured while logging in", e);
			}
			startActivity(new Intent(Login.this, ChatDialogs.class));
        }
   
        public void onFacebookError(FacebookError e) {
        	alert("An unexpected  error occured. Please report to developer.",
            	Login.this);
            Log.e(TAG, "Exception occured while logging in", e);
        }
         
        public void onError(DialogError e) {
        	alert("An unexpected  error occured. Please report to developer.",
            	Login.this);
            Log.e(TAG, "Exception occured while logging in", e);
        }

        public void onCancel() {
        }
    }
    
    private void alert(String errorStr, Context ctx) {
    	AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
    	builder.setMessage(errorStr)
		       .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		                dialog.dismiss();
		           }
		       });
    	AlertDialog alert = builder.create();
    	alert.show();
    }
}
