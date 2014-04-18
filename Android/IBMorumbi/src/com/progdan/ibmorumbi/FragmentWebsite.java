package com.progdan.ibmorumbi;

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
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {
		View view = inflater.inflate(R.layout.website, container, false);
		
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
