package com.progdan.ibmorumbi;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;



@SuppressLint("SetJavaScriptEnabled")
public class FragmentWebsite extends Fragment {
	@Override
	public void onStart() {
		super.onStart();
		//Get an Analytics tracker to report app starts & uncaught exceptions etc.
		GoogleAnalytics.getInstance(getActivity()).reportActivityStart(getActivity());
	}
	@Override
	public void onStop() {
		super.onStop();
		//Get an Analytics tracker to report app starts & uncaught exceptions etc.
		GoogleAnalytics.getInstance(getActivity()).reportActivityStop(getActivity());
	}
	@Override
	public void onResume() {
		super.onResume();

        // Get tracker.
        Tracker t = ((IBMorumbiApp) getActivity().getApplication()).getTracker();

        // Set screen name.
        // Where path is a String representing the screen name.
        t.setScreenName("WebView Screen");

        // Send a screen view.
        t.send(new HitBuilders.AppViewBuilder().build());
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {
		View view = inflater.inflate(R.layout.fragment_website, container, false);
		//Get a Tracker (should auto-report)
		((IBMorumbiApp) getActivity().getApplication()).getTracker();
		
		String url = "http://www.ibmorumbi.com.br/";
		WebView myWebView = (WebView) view.findViewById(R.id.webview);
		WebSettings settings = myWebView.getSettings();
		
		settings.setUseWideViewPort(true);
		settings.setBuiltInZoomControls(true);
		settings.setSupportZoom(true);

		settings.setJavaScriptEnabled(true);
		settings.setLoadWithOverviewMode(true);

		myWebView.loadUrl(url);
		
		return view;
	}
	
}
