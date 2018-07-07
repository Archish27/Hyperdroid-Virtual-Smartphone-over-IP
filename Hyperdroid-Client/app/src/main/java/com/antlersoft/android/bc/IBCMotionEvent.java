/**
 * Copyright (c) 2010 Michael A. MacDonald
 */
package com.antlersoft.android.bc;

import android.view.MotionEvent;

/**
 * Access to SDK-dependent features of MotionEvent
 * 
 * @see MotionEvent
 *
 * @author Michael A. MacDonald
 *
 */
public interface IBCMotionEvent {
	/**
	 * Obtain the number of pointers active in the event
	 * @see MotionEvent#getPointerCount()
	 * @param evt
	 * @return number of pointers
	 */
	int getPointerCount(MotionEvent evt);
}
