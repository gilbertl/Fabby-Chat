package com.fabbychat;

import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.FacebookError;
import com.facebook.android.Facebook.DialogListener;

public class Login extends Activity {
	
	private static final String 
		TAG = "Login",
		FB_APP_ID = "118419214873353",
		FB_API_KEY = "33f89f44264edd92c78a91552e4f874b",
		FB_APP_SECRET = "92785de812ae2019007df12e2f638d3b";
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    
    @Override
    public void onStart() {
    	super.onStart();
        Facebook fb = new Facebook();
        // open Facebook login dialog
		fb.authorize(this, FB_APP_ID,
			new String[] { "xmpp_login", "offline_access" }, 
			new LoginDialogListener());
    }
    
    public class LoginDialogListener implements DialogListener {
    	
    	private Context mContext;
    	
    	public LoginDialogListener() {
    		mContext = Login.this;
    	}
    	
    	private ServiceConnection mConnection = new ServiceConnection() {
    	    public void onServiceConnected(ComponentName className, IBinder service) {
    	        // This is called when the connection with the service has been
    	        // established, giving us the service object we can use to
    	        // interact with the service.  Because we have bound to a explicit
    	        // service that we know is running in our own process, we can
    	        // cast its IBinder to a concrete class and directly access it.
    	    	/*
    	        mBoundService = ((LocalService.LocalBinder)service).getService();

    	        // Tell the user about this for our demo.
    	        Toast.makeText(Binding.this, R.string.local_service_connected,
    	                Toast.LENGTH_SHORT).show();
    	                */
    	    	Log.d(TAG, "connected to local service");
    	    }

    	    public void onServiceDisconnected(ComponentName className) {
    	        // This is called when the connection with the service has been
    	        // unexpectedly disconnected -- that is, its process crashed.
    	        // Because it is running in our same process, we should never
    	        // see this happen.
    	    	/*
    	        mBoundService = null;
    	        Toast.makeText(Binding.this, R.string.local_service_disconnected,
    	                Toast.LENGTH_SHORT).show();
    	                */
    	    	Log.d(TAG, "disconnected from local service");
    	    }
    	};
    	
    	// make XMPP connection
        public void onComplete(Bundle values) {
        	Log.d(TAG, "onComplete");
        	
        	bindService(new Intent(mContext, FbChatService.class), 
        		mConnection, Context.BIND_AUTO_CREATE);
        	
        	XMPPConnection xmppConnection = FbChatConnection.getConnection();
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
            startActivity(new Intent(mContext, ChatDialogs.class));;
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