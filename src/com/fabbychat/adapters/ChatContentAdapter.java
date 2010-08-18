package com.fabbychat.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.fabbychat.R;

public class ChatContentAdapter extends ArrayAdapter<String> {
	
	private static String TAG = "ChatContentAdapter";
	
	public ChatContentAdapter(Context context) {
		super(context, android.R.layout.simple_list_item_1,
			new ArrayList<String>());
	}
	
	@Override
	public View getView(int pos, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater vi = (LayoutInflater)
            	getContext().
            	getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.chat_content_row, null);
        }
        String msg = super.getItem(pos);
        TextView msgView = (TextView) v.findViewById(R.id.message);
        msgView.setText(msg);   

        return v;
	}
}
