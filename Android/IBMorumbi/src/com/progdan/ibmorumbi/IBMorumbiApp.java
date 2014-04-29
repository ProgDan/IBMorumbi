package com.progdan.ibmorumbi;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Logger.LogLevel;
import com.google.android.gms.analytics.Tracker;

import android.app.Application;

public class IBMorumbiApp extends Application {
	private Tracker tracker;

	public IBMorumbiApp() {
		super();
	}

	synchronized Tracker getSynchronizedTracker() {
		if (tracker == null) {
			GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
//			analytics.getLogger().setLogLevel(LogLevel.VERBOSE);
			tracker = analytics.newTracker(R.xml.global_tracker);
		}
		return tracker;
	}

	public Tracker getTracker() {
		return getSynchronizedTracker();
	}

}
