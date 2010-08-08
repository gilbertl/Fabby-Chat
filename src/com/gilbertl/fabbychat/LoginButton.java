package com.gilbertl.fabbychat;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.FacebookError;
import com.facebook.android.Facebook.DialogListener;

public class LoginButton extends Button implements OnClickListener {
	
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
		mFb.authorize(getContext(), 
			getContext().getString(R.string.fb_application_id),
			new String[] { "xmpp_login" }, 
			new LoginDialogListener());
	}
	
    public class LoginDialogListener implements DialogListener {
        public void onComplete(Bundle values) {
        	Log.d("", "complete");
        }
   
        public void onFacebookError(FacebookError e) {
        	Log.d("", "Facebook error");
        }
         
        public void onError(DialogError e) {
        	Log.d("", "plain error");
        	
        }

        public void onCancel() {
        	Log.d("", "cancel");
        }
    }

}
