package com.progdan.ibmorumbi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.analytics.tracking.android.EasyTracker;
import com.progdan.ibmorumbi.json.JSONParser;
import com.progdan.ibmorumbi.pdftools.PDFTools;

import android.app.ListFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class FragmentNewsletter extends ListFragment {
	private List<Map<String, Object>> boletins;
	private static String url = "http://mini.progdan.com/ibmorumbi/boletins.php";

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		new ProgressTask(FragmentNewsletter.this).execute();
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
