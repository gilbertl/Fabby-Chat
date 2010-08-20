package com.fabbychat;

import java.io.ByteArrayInputStream;

import org.jivesoftware.smack.XMPPConnection;

import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;

import com.fabbychat.models.FbContact;
import com.fabbychat.utils.DrawableManager;

public class ChatDialogs extends TabActivity {
	
	public static final int PICK_CONTACT_REQUEST = 0;
	
	private static final String TAG = "ChatDialogs";

	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.chat_dialogs);
	       
	    // add tab button
	    Button newDialog = (Button)	findViewById(R.id.add_tab_button);
	    newDialog.setOnClickListener(new NewDialogListener());
	    getTabHost().setOnTabChangedListener(new TabChangeListener());
	}
	
	private class NewDialogListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			startActivityForResult(new Intent(v.getContext(), Contacts.class), 
				PICK_CONTACT_REQUEST);
		}
	}
	
	private class TabChangeListener implements OnTabChangeListener {
		private int prevTabIndex = -1;
		
		@Override
		public void onTabChanged(String arg0) {
			LinearLayout ll;
			Resources res = getResources();
			
			int currTabIndex = getTabHost().getCurrentTab();
			if (prevTabIndex >= 0 && prevTabIndex != currTabIndex) {
				View prevTabView = 
					getTabWidget().getChildTabViewAt(prevTabIndex);
				ll = (LinearLayout) 
					prevTabView.findViewById(R.id.dialog_tab_border);
				ll.setBackgroundDrawable(
					res.getDrawable(R.drawable.dialog_tab_border));
			}
			prevTabIndex = currTabIndex;
			View currTabView = getTabHost().getCurrentTabView();
			ll = (LinearLayout) 
				currTabView.findViewById(R.id.dialog_tab_border);
			ll.setBackgroundDrawable(
				res.getDrawable(R.drawable.dialog_tab_border_selected));
		}
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == PICK_CONTACT_REQUEST && resultCode == RESULT_OK) {
			TabHost tabHost = getTabHost();
			FbContact contact = 
				data.getExtras().getParcelable(Contacts.FB_CONTACT_PARAM);
			String jid = contact.getJid();

			// maybe the tab already exists
			tabHost.setCurrentTabByTag(jid);
			// if not, create a new one
			if (!jid.equals(tabHost.getCurrentTabTag())) {
				Intent intent = new Intent(this, ChatDialog.class);
				intent.putExtra(ChatDialog.FB_CONTACT_PARAM, contact);
				TabSpec spec = tabHost.newTabSpec(jid);
				spec.setIndicator(tabView(contact));
				spec.setContent(intent);
				
				tabHost.addTab(spec);
				tabHost.setCurrentTabByTag(jid);
			}
		}
	}
	
	private View tabView(FbContact contact) {
		LayoutInflater vi = (LayoutInflater) 
			getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = vi.inflate(R.layout.dialog_tab, null);
		ImageView imageView = (ImageView) v.findViewById(R.id.profile_pic);
		if (imageView != null) {
			FbAvatarProducer dp = new FbAvatarProducer(
				FbChatConnection.getConnection(), contact.getJid());
			DrawableManager.getInstance().fetchDrawableOnThread(dp, imageView);
		}
		return v;
	}
}
