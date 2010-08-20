package com.fabbychat;

import java.io.ByteArrayInputStream;

import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.packet.VCard;

import com.fabbychat.utils.DrawableProducer;

import android.graphics.drawable.Drawable;
import android.util.Log;

public class FbAvatarProducer implements DrawableProducer {
	
	private final static String TAG = FbAvatarProducer.class.getName();

	private XMPPConnection mConn;
	private String mJid;
	
	public FbAvatarProducer(XMPPConnection conn, String jid) {
		mConn = conn;
		mJid = jid;
	}
	
	@Override
	public Drawable getDrawable() {
		VCard vCard = new VCard();
		try {
			vCard.load(mConn, mJid);
			byte[] avatarBytes = vCard.getAvatar();
			ByteArrayInputStream bais = new ByteArrayInputStream(avatarBytes);
			return Drawable.createFromStream(bais, "src");
		} catch (XMPPException e) {
			Log.e(TAG, "Failed to load avatar for " + mJid, e);
			return null;
		}
	}

	@Override
	public String getKey() {
		return this.getClass().getName() + ":" + mJid;
	}

}
