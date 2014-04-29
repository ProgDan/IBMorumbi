package com.progdan.ibmorumbi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.progdan.ibmorumbi.json.JSONParser;
import com.progdan.ibmorumbi.pdftools.PDFTools;

import android.app.ListFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class FragmentNewsletter extends ListFragment {
	private List<Map<String, Object>> boletins;
	private String url = "http://mini.progdan.com/ibmorumbi/boletins.php";

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
        t.setScreenName("Newsletter Screen");

        // Send a screen view.
        t.send(new HitBuilders.AppViewBuilder().build());
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		//Get a Tracker (should auto-report)
		((IBMorumbiApp) getActivity().getApplication()).getTracker();

		// Identificação da versão do SO, Tipo de Dispositivo e versão do Aplicativo
		String androidOS = Build.VERSION.RELEASE;
		String device = Build.MANUFACTURER + " (" + Build.BRAND + ") - " + Build.MODEL;
		String app_ver = "unknow";
		try {
			app_ver = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0).versionName;
		} catch (NameNotFoundException e) {
			Log.v("ERROR", e.getMessage());
		}
		
			this.url = new String("http://mini.progdan.com/ibmorumbi/boletins.php?platform=Android&device=" + device + "&os=" + androidOS + "&client="+ app_ver).replaceAll(" ","%20");

		
		new ProgressTask(FragmentNewsletter.this).execute();
	}

	@Override
	public void onListItemClick(ListView list, View view, int position, long id) {
		// do something with the data
		Map<String, Object> map = boletins.get(position);
		String descricao = (String) map.get("detail");
		String mensagem = "Boletim selecionado: " + descricao;
		Toast.makeText(getActivity(), mensagem, Toast.LENGTH_SHORT).show();
		PDFTools.showPDFUrl(getActivity(), (String) map.get("file"));
	}

	private class ProgressTask extends AsyncTask<String, Void, Boolean> {
		private ProgressDialog dialog;
		private Context context;

		public ProgressTask(ListFragment fragment) {
			context = fragment.getActivity();
			dialog = new ProgressDialog(context);
		}

		protected void onPreExecute() {
			this.dialog.setMessage(getString(R.string.content_loading));
			this.dialog.show();
		}

		@Override
		protected void onPostExecute(final Boolean success) {
			if (dialog.isShowing()) {
				dialog.dismiss();
			}

			String[] from = { "image", "text", "detail" };
			int[] to = { R.id.imageView, R.id.textLabel, R.id.detailTextLabel };

			SimpleAdapter adapter = new SimpleAdapter(context, boletins,
					R.layout.list_row, from, to);
			adapter.setViewBinder(new MyViewBinder());

			setListAdapter(adapter);
		}

		@Override
		protected Boolean doInBackground(String... args) {
			boletins = new ArrayList<Map<String, Object>>();
			JSONParser jParser = new JSONParser();

			// get JSON data from URL
			JSONArray json = jParser.getJSONFromUrl(url);

			for (int i = 0; i < json.length(); i++) {
				try {
					JSONObject c = json.getJSONObject(i);

					Object image = R.drawable.icon_pdf;
					String text = "Boletim Informativo";
					String detail = c.getString("data");
					String file = c.getString("file");

					HashMap<String, Object> map = new HashMap<String, Object>();

					// Add child node to HashMap key & value
					map.put("image", image);
					map.put("text", text);
					map.put("detail", detail);
					map.put("file", file);
					boletins.add(map);

				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			return null;
		}
	}
}
