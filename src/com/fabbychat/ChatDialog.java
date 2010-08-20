package com.fabbychat;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;

import android.app.ListActivity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;

import com.fabbychat.adapters.ChatContentAdapter;
import com.fabbychat.models.FbContact;

public class ChatDialog extends ListActivity {
	
	public static final String FB_CONTACT_PARAM = 
		"com.fabbychat.ChatDialog.fb_contact";
	private static final String TAG = "ChatDialog";
	
	private XMPPConnection conn;
	private ChatManager chatMngr;
	private Chat chat;
	ArrayAdapter<String> msgAdapter;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		conn = FbChatConnection.getConnection();
		chatMngr = conn.getChatManager();

		msgAdapter = new ChatContentAdapter(this);

		setListAdapter(msgAdapter);
		setContentView(R.layout.chat_dialog);

		Button send = (Button) findViewById(R.id.send_button);
		send.setOnClickListener(new SendOnClickListener());
		
		FbContact contact = getIntent().getExtras()
			.getParcelable(FB_CONTACT_PARAM);
		chat = chatMngr.createChat(
			contact.getJid(), new IncomingMessageListener());
	}
	
	private final Handler mHandler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			String body = (String) msg.obj;
			msgAdapter.add(body);
		}
	};
	
	private class IncomingMessageListener implements MessageListener {
		public void processMessage(Chat chat, Message message) {
			String body = message.getBody();
			if (body != null) {
				mHandler.obtainMessage(0, body).sendToTarget();
			}
		}
	}

	private class SendOnClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			EditText textInput = (EditText) findViewById(R.id.message_input);
			String text = textInput.getText().toString();
			try {
				chat.sendMessage(text);
				msgAdapter.add(text);
				textInput.setText("");
			} catch (XMPPException e) {
				msgAdapter.add("Error devliering msg.");
				Log.d(TAG, "Error devlivering message.", e);
			}
		}
	}

}
