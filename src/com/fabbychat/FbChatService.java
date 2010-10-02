package com.fabbychat;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.SASLAuthentication;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.provider.ProviderManager;
import org.jivesoftware.smackx.packet.VCard;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.fabbychat.sasl.SASLFacebookMechanism;

public class FbChatService extends Service {
	
	private static final String TAG = FbChatService.class.getName();
	
	private static final String 
		FB_APP_ID = "118419214873353",
		FB_API_KEY = "33f89f44264edd92c78a91552e4f874b",
		FB_APP_SECRET = "92785de812ae2019007df12e2f638d3b";
	
    private NotificationManager mNM;
    private XMPPConnection mXMPPConn;

    /**
     * Class for clients to access.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with
     * IPC.
     */
    public class LocalBinder extends Binder {
    	public void login(String sessionKey) throws XMPPException {
            mXMPPConn.connect();
        	String resource = getString(R.string.app_name);
            mXMPPConn.login(FB_API_KEY + "|" + sessionKey, FB_APP_SECRET, 
            	resource);
            
            showNotification();
    	}
    	
    	public VCard getVCard(String jid) throws XMPPException {
    		VCard vCard = new VCard();
    		vCard.load(mXMPPConn, jid);
    		return vCard;
    	}
    }

    @Override
    public void onCreate() {
    	initXMPPConn();
        mNM = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);

        // TODO: port evetyhing that uses FbChatConnection over to FBChatService
    }
    
    private void initXMPPConn() {
		ProviderManager.getInstance().addIQProvider("vCard", "vcard-temp",
				new org.jivesoftware.smackx.provider.VCardProvider());
    	// register Facebook SASL mechanism
    	SASLAuthentication.registerSASLMechanism(
    		SASLFacebookMechanism.NAME,
            SASLFacebookMechanism.class);
    	SASLAuthentication.supportSASLMechanism(
    		SASLFacebookMechanism.NAME, 0);

    	// create a connection
    	ConnectionConfiguration config = 
    		new ConnectionConfiguration("chat.facebook.com", 5222);
        mXMPPConn = new XMPPConnection(config);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("LocalService", "Received start id " + startId + ": " + intent);
        // We want this service to continue running until it is explicitly
        // stopped, so return sticky.
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        // Cancel the persistent notification.
       // mNM.cancel(R.string.service_started);

        Log.d(TAG, "on destroy");
        // Tell the user we stopped.
        Toast.makeText(this, "local service stopped", Toast.LENGTH_SHORT)
        	.show();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    // This is the object that receives interactions from clients.  See
    // RemoteService for a more complete example.
    private final IBinder mBinder = new LocalBinder();

    /**
     * Show a notification while this service is running.
     */
    private void showNotification() {
        // In this sample, we'll use the same text for the ticker and the expanded notification
        CharSequence text = getString(R.string.service_started);

        // Set the icon, scrolling text and timestamp
        Notification notification = new Notification(
        	R.drawable.default_fb_avatar, text, System.currentTimeMillis());

        // The PendingIntent to launch our activity if the user selects this notification
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, ChatDialogs.class), 0);

        // Set the info for the views that show in the notification panel.
        notification.setLatestEventInfo(this, getText(R.string.service_started),
                       text, contentIntent);

        // Send the notification.
        // We use a layout id because it is a unique number.  We use it later to cancel.
        mNM.notify(R.string.service_started, notification);
    }
}
