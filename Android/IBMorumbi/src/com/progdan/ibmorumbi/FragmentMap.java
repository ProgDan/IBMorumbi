package com.progdan.ibmorumbi;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class FragmentMap extends Fragment {
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {
		View view = inflater.inflate(R.layout.tab, container, false);
		TextView textView = (TextView) view.findViewById(R.id.tabtextview);
		textView.setText(R.string.map);
		return view;
	}
}
