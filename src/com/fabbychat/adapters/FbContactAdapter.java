package com.fabbychat.adapters;

import java.util.ArrayList;
import java.util.Comparator;

import org.jivesoftware.smack.XMPPConnection;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.fabbychat.FbAvatarProducer;
import com.fabbychat.FbChatConnection;
import com.fabbychat.R;
import com.fabbychat.models.FbContact;
import com.fabbychat.utils.DrawableManager;

public class FbContactAdapter extends ArrayAdapter<FbContact> {
	
	private static String TAG = "FbContactAdapter";
	
	private Comparator<FbContact> mComp;
	private XMPPConnection mConn;
	
    public FbContactAdapter(Context context, ArrayList<FbContact> contacts,
    		Comparator<FbContact> comp, XMPPConnection conn) {
            super(context, android.R.layout.simple_list_item_1, contacts);
            mComp = comp;
            super.sort(mComp);
            mConn = conn;
    }
    
    @Override
    public void add(FbContact contact) {
    	super.add(contact);
    	super.sort(mComp);
    }
    
    @Override
    public void remove(FbContact contact) {
    	super.remove(contact);
    	super.sort(mComp);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater vi = (LayoutInflater)
            	getContext().
            	getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.fb_contact_row, null);
        }
        FbContact contact = super.getItem(position);
        if (contact != null) {
            TextView nameView = (TextView) v.findViewById(R.id.name);
            if (nameView != null) {
                  nameView.setText(contact.getName());   
            }
            ImageView imageView = (ImageView) 
            	v.findViewById(R.id.profile_pic);
            if (imageView != null) {
            	FbAvatarProducer dp = new FbAvatarProducer(
        			FbChatConnection.getConnection(), contact.getJid(),
        			getContext().getResources());
        		DrawableManager.getInstance().
        			fetchDrawableOnThread(dp, imageView);
            }
        }
        return v;
    }

}
