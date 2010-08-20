package com.fabbychat.models;

import java.util.Comparator;

import org.jivesoftware.smack.RosterEntry;

import android.os.Parcel;
import android.os.Parcelable;

public class FbContact implements Parcelable {
	
	public static Comparator<FbContact> 
	NAME_COMPARATOR = new Comparator<FbContact>() {
		public int compare(FbContact obj1, FbContact obj2) {
			return obj1.getName().compareTo(obj2.getName());
		}
	};
	
	private static String TAG = "FbContact",
						  GRAPH_API = "http://graph.facebook.com/";
	
	// fields
	private String jid,
				   name,
				   uid;
	
	// constructors and initializers
	public FbContact(RosterEntry re) {
		String jid = re.getUser();
		init(re.getName(), jid);
	}
	 
    public FbContact(Parcel in) {
    	readFromParcel(in);
    }
    
	private void init(String name, String jid) {
		this.jid = jid;
		this.name = name;
		this.uid = extractUID(jid);
	}
	
	public String getJid() {
		return jid;
	}
	
	public String getName() {
		return name;
	}

	public String getUsername() {
		return uid;
	}
	
	// helpers
	private String extractUID(String jid) {
		// jids are in the form of -123412341234@chat.facebook.com
		return jid.substring(1, jid.indexOf('@'));
	}

	// parcelable implementation
    @Override
    public void writeToParcel(Parcel dest, int flags) {
    	dest.writeString(name);
    	dest.writeString(jid);
    }
    
    public void readFromParcel(Parcel in) {
    	init(in.readString(), in.readString());
    }
 
    public static final Parcelable.Creator<FbContact> CREATOR =
    	new Parcelable.Creator<FbContact>() {
	        public FbContact createFromParcel(Parcel in) {
	            return new FbContact(in);
	        }
	 
	        public FbContact[] newArray(int size) {
	            return new FbContact[size];
	        }
    };
 
    @Override
    public int describeContents() {
        return 0;
    }

}
