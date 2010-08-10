package com.fabbychat;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.SASLAuthentication;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.fabbychat.sasl.SASLFacebookMechanism;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.FacebookError;
import com.facebook.android.Facebook.DialogListener;

public class Login extends Activity {
	
	private static final String 
		TAG = "LoginButton",
		FB_APP_ID = "118419214873353",
		FB_API_KEY = "33f89f44264edd92c78a91552e4f874b",
		FB_APP_SECRET = "92785de812ae2019007df12e2f638d3b";
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Facebook fb = new Facebook();
        // open Facebook login dialog
		fb.authorize(this, FB_APP_ID,
			new String[] { "xmpp_login", "offline_access" }, 
			new LoginDialogListener(this));
    }
    
    public class LoginDialogListener implements DialogListener {
    	
    	private Context mContext;
    	
    	public LoginDialogListener(Context c) {
    		mContext = c;
    	}
    	
    	// make XMPP connection
        public void onComplete(Bundle values) {
        	// register Facebook SASL mechanism
        	SASLAuthentication.registerSASLMechanism(
        		SASLFacebookMechanism.NAME,
                SASLFacebookMechanism.class);
        	SASLAuthentication.supportSASLMechanism(
        		SASLFacebookMechanism.NAME, 0);

        	// create a connection
        	ConnectionConfiguration config = 
        		new ConnectionConfiguration("chat.facebook.com", 5222);
            XMPPConnection xmppConnection = new XMPPConnection(config);
            // by default, reconnection is allowed; is this okay?

            try {
            	xmppConnection.connect();
            } catch (XMPPException e) {
            	alert("Unable to connect to Facebook servers.", mContext);
                Log.e(TAG, "Exception occured while connecting to Facebook", e);
                return;
            }
            try {
            	String sessionKey = values.getString("session_key"),
            		   resource = mContext.getString(R.string.app_name);
                xmppConnection.login(FB_API_KEY + "|" + sessionKey,
                		FB_APP_SECRET, resource);
            } catch (XMPPException e) {
            	alert("An unexpected authentication error occured. Please report to developer.",
            		mContext);
                Log.e(TAG, "Exception occured while logging in", e);
                return;
            }
        }
   
        public void onFacebookError(FacebookError e) {
        	alert("An unexpected  error occured. Please report to developer.",
            		mContext);
            Log.e(TAG, "Exception occured while logging in", e);
        }
         
        public void onError(DialogError e) {
        	alert("An unexpected  error occured. Please report to developer.",
            		mContext);
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