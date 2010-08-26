package com.fabbychat;

import java.io.ByteArrayInputStream;

import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.packet.VCard;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.fabbychat.utils.DrawableProducer;

public class FbAvatarProducer implements DrawableProducer {
	
	private final static String TAG = FbAvatarProducer.class.getName();

	private XMPPConnection mConn;
	private String mJid;
	private Drawable defaultDrawable;
	
	public FbAvatarProducer(XMPPConnection conn, String jid, Resources res) {
		mConn = conn;
		mJid = jid;
		defaultDrawable = res.getDrawable(R.drawable.default_fb_avatar);
	}
	
	@Override
	public Drawable getDrawable() {
		Drawable d = defaultDrawable;
		VCard vCard = new VCard();
		try {
			vCard.load(mConn, mJid);
			byte[] avatarBytes = vCard.getAvatar();
			if (avatarBytes != null) {
				ByteArrayInputStream bais = 
					new ByteArrayInputStream(avatarBytes);
				d = Drawable.createFromStream(bais, "src");
			}
		} catch (XMPPException e) {
			Log.e(TAG, "Failed to load avatar for " + mJid, e);
		}
		return d;
	}

	@Override
	public String getKey() {
		return this.getClass().getName() + ":" + mJid;
	}

}
