package com.fabbychat;

import java.util.ArrayList;
import java.util.Collection;

import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.RosterListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.packet.Presence;

import android.app.ListActivity;
import android.os.Bundle;

import com.fabbychat.adapters.FbContactAdapter;
import com.fabbychat.models.FbContact;
import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;

public class Contacts extends ListActivity {
	public static final String TAG = "Contacts";
	
	private XMPPConnection conn;
	private Roster roster;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	  
		// initialize vars
		conn = FbChatConnection.getConnection();
		roster = conn.getRoster();
	  
		Collection<RosterEntry> rosterEntries = roster.getEntries();
		rosterEntries = 
			Collections2.filter(rosterEntries, new Predicate<RosterEntry>() {
				public boolean apply(RosterEntry re) {
					return roster.getPresence(re.getUser()).isAvailable();
				}
			});
		// transform roster entries
		ArrayList<FbContact> contacts = new ArrayList<FbContact>(
			Collections2.transform(rosterEntries, 
				new Function<RosterEntry, FbContact>() {
					public FbContact apply(RosterEntry re) {
						return 
						new FbContact(re, roster.getPresence(re.getUser()));
					}
				}));

		FbContactAdapter adapter = new FbContactAdapter(this, 
			R.layout.fb_contact_row, contacts, FbContact.NAME_COMPARATOR);
		setListAdapter(adapter);
		
		roster.addRosterListener(new ContactsListener());
	}
	
	private class ContactsListener implements RosterListener {

		@Override
		public void entriesAdded(Collection<String> arg0) {
			// TODO Auto-generated method stub
			// unlikely to happen; implement later
			
		}

		@Override
		public void entriesDeleted(Collection<String> arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void entriesUpdated(Collection<String> arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void presenceChanged(Presence pres) {
			String user = pres.getFrom();
			// get presence with highest priority and availability
		    Presence bestPres = roster.getPresence(user);
		    // TODO: when we make a more complicated roster object, we'll
		    // keep a sorted set of users and then we can update it uniquely
		    
		}
		
	}

}
