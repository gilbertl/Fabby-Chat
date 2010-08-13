package com.fabbychat.models;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Comparator;

import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.packet.Presence;

public class FbContact {
	
	public static Comparator<FbContact> 
	NAME_COMPARATOR = new Comparator<FbContact>() {
		public int compare(FbContact obj1, FbContact obj2) {
			return obj1.getName().compareTo(obj2.getName());
		}
	};
	
	private static String TAG = "FbContact",
						  GRAPH_API = "http://graph.facebook.com/";
	
	private String jid,
				   name,
				   uid;
	private Presence presence;
	
	public FbContact(RosterEntry re, Presence p) {
		jid = re.getUser();
		name = re.getName();
		uid = extractUID(jid);
		presence = p;
	}
	
	public String getPicURL() {
		return String.format("%s/%s/picture?type=square", GRAPH_API, uid);
	}
	
	public String getJid() {
		return jid;
	}
	public void setJid(String jid) {
		this.jid = jid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUsername() {
		return uid;
	}
	public void setUsername(String username) {
		this.uid = username;
	}
	
	public Presence getPresence() {
		return presence;
	}
	public void setPresence(Presence presence) {
		this.presence = presence;
	}
	
	private String extractUID(String jid) {
		// jids are in the form of -123412341234@chat.facebook.com
		return jid.substring(1, jid.indexOf('@'));
	}
	

}
