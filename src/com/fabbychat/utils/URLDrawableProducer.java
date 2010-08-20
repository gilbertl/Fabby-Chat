package com.fabbychat.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.graphics.drawable.Drawable;
import android.util.Log;

public class URLDrawableProducer implements DrawableProducer {
	
	private final static String TAG = URLDrawableProducer.class.getName();
	
	private String mUrl;
	
	public URLDrawableProducer(String url) {
		mUrl = url;
	}

	@Override
	public Drawable getDrawable() {
    	try {
    		InputStream is = fetchURL(mUrl);
    		Drawable drawable = Drawable.createFromStream(is, "src");
    		Log.d(this.getClass().getSimpleName(), "got a thumbnail drawable: " 
    				+ drawable.getBounds() + ", "
    				+ drawable.getIntrinsicHeight() + "," 
    				+ drawable.getIntrinsicWidth() + ", "
    				+ drawable.getMinimumHeight() + "," 
    				+ drawable.getMinimumWidth());
    		return drawable;
    	} catch (MalformedURLException e) {
    		Log.e(TAG, "fetchDrawable failed", e);
    		return null;
    	} catch (IOException e) {
    		Log.e(TAG, "fetchDrawable failed", e);
    		return null;
    	}
	}
	
    private InputStream 
    fetchURL(String urlString) throws MalformedURLException, IOException {
    	DefaultHttpClient httpClient = new DefaultHttpClient();
    	HttpGet request = new HttpGet(urlString);
    	HttpResponse response = httpClient.execute(request);
    	return response.getEntity().getContent();
    }

	@Override
	public String getKey() {
		return this.getClass().getName() + ":" + mUrl;
	}

}
