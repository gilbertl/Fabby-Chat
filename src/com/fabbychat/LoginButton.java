package com.fabbychat;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.SASLAuthentication;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.fabbychat.sasl.SASLFacebookMechanism;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.FacebookError;
import com.facebook.android.Facebook.DialogListener;

public class LoginButton extends Button implements OnClickListener {
	
	private static final String 
		TAG = "LoginButton",
		FB_APP_ID = "118419214873353",
		FB_API_KEY = "33f89f44264edd92c78a91552e4f874b",
		FB_APP_SECRET = "92785de812ae2019007df12e2f638d3b";
	
	private Facebook mFb;
	
    public LoginButton(Context context) {
        super(context);
        init();
    }
    
    public LoginButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    
    public LoginButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }
	
	private void init() {
		mFb = new Facebook();
		setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		mFb.authorize(getContext(), FB_APP_ID,
			new String[] { "xmpp_login", "offline_access" }, 
			new LoginDialogListener());
	}
	
    public class LoginDialogListener implements DialogListener {
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
            	alert("Unable to connect to Facebook servers.", getContext());
                Log.e(TAG, "Exception occured while connecting to Facebook", e);
                return;
            }
            try {
            	String sessionKey = values.getString("session_key"),
            		   resource = getContext().getString(R.string.app_name);
                xmppConnection.login(FB_API_KEY + "|" + sessionKey,
                		FB_APP_SECRET, resource);
            } catch (XMPPException e) {
            	alert("An unexpected authentication error occured. Please report to developer.",
            		getContext());
                Log.e(TAG, "Exception occured while logging in", e);
                return;
            }
        }
   
        public void onFacebookError(FacebookError e) {
        	alert("An unexpected  error occured. Please report to developer.",
            		getContext());
            Log.e(TAG, "Exception occured while logging in", e);
        }
         
        public void onError(DialogError e) {
        	alert("An unexpected  error occured. Please report to developer.",
            		getContext());
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
