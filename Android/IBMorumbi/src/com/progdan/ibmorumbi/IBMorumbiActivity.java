package com.progdan.ibmorumbi;


import com.google.android.gms.analytics.GoogleAnalytics;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;

public class IBMorumbiActivity extends Activity {
	ActionBar.Tab message, newsletter, website, map, about;
	Fragment fragmentMessage = new FragmentMessage();
	Fragment fragmentNewsletter = new FragmentNewsletter();
	Fragment fragmentWebsite = new FragmentWebsite();
	Fragment fragmentMap = new FragmentMap();
	Fragment fragmentAbout = new FragmentAbout();

	@Override
	public void onStart() {
		super.onStart();
		//Get an Analytics tracker to report app starts & uncaught exceptions etc.
		GoogleAnalytics.getInstance(this).reportActivityStart(this);
	}
	@Override
	public void onStop() {
		super.onStop();
		//Get an Analytics tracker to report app starts & uncaught exceptions etc.
		GoogleAnalytics.getInstance(this).reportActivityStop(this);
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ibmorumbi);
		//Get a Tracker (should auto-report)
		((IBMorumbiApp) getApplication()).getTracker();

		ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		message = actionBar.newTab().setText(R.string.message);
		newsletter = actionBar.newTab().setText(R.string.newsletter);
		website = actionBar.newTab().setText(R.string.website);
		map = actionBar.newTab().setText(R.string.map);
		about = actionBar.newTab().setText(R.string.about);

		message.setTabListener(new MyTabListener(fragmentMessage));
		newsletter.setTabListener(new MyTabListener(fragmentNewsletter));
		website.setTabListener(new MyTabListener(fragmentWebsite));
		map.setTabListener(new MyTabListener(fragmentMap));
		about.setTabListener(new MyTabListener(fragmentAbout));

		actionBar.addTab(message);
		actionBar.addTab(newsletter);
		actionBar.addTab(website);
		actionBar.addTab(map);
		actionBar.addTab(about);
	}
	
}
