package com.fabbychat.utils;

import android.graphics.drawable.Drawable;

public interface DrawableProducer {
	abstract public Drawable getDrawable();
	abstract public String getKey();	// used by Drawable Manager to cache
}
