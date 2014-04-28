package com.progdan.ibmorumbi;

import com.google.analytics.tracking.android.EasyTracker;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

public class FragmentAbout extends Fragment {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {
		View view = inflater.inflate(R.layout.fragment_about, container, false);
		
		try {
			String app_ver = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0).versionName;
			TextView textViewRelease = (TextView) view.findViewById(R.id.textViewRelease);
			textViewRelease.setText(app_ver);
		} catch (NameNotFoundException e) {
			Log.v("ERROR", e.getMessage());
		}
		
		final ImageButton facebook = (ImageButton) view.findViewById(R.id.imageButtonFacebook);
		facebook.setOnClickListener( new OnClickListener() {		
			@Override
			public void onClick(View v) {
				openFacebook(v);
			}
		});
		
		ImageButton twitter = (ImageButton) view.findViewById(R.id.imageButtonTwitter);
		twitter.setOnClickListener( new OnClickListener() {		
			@Override
			public void onClick(View v) {
				openTwitter(v);
			}
		});
		
		return view;
	}
	
	@Override
	public void onStart(){
		super.onStart();
		EasyTracker.getInstance(getActivity()).activityStart(getActivity());
	}
	
	@Override
	public void onStop(){
		super.onStop();
		EasyTracker.getInstance(getActivity()).activityStop(getActivity());
	}
	
	public void openFacebook(View v) {
		Intent intent;
		try {
			Context context = v.getContext();
			context.getPackageManager().getPackageInfo("com.facebook.katana", 0);
			intent = new Intent(Intent.ACTION_VIEW,Uri.parse("fb://profile/100002117943796"));
		} catch (Exception e) {
			intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/igreja.batistadomorumbi"));
		}
		startActivity(intent);
	}
	
	public void openTwitter(View v) {
		Intent intent;
		try {
			Context context = v.getContext();
			context.getPackageManager().getPackageInfo("com.twitter.android", 0);
			intent = new Intent(Intent.ACTION_VIEW,Uri.parse("twitter://user?screen_name=ibmorumbi"));
		} catch (Exception e) {
			intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/ibmorumbi"));
		}
		startActivity(intent);
	}
}
