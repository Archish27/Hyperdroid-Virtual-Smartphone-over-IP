package com.hyperdroidclient;

import android.app.Activity;
import com.robotium.recorder.executor.Executor;

@SuppressWarnings("rawtypes")
public class WelcomeActivityExecutor extends Executor {

	@SuppressWarnings("unchecked")
	public WelcomeActivityExecutor() throws Exception {
		super((Class<? extends Activity>) Class.forName("com.hyperdroidclient.intro.WelcomeActivity"),  "com.hyperdroidclient.R.id.", new android.R.id(), false, false, "1519138555167");
	}

	public void setUp() throws Exception { 
		super.setUp();
	}
}
