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
import com.progdan.ibmorumbi.imageloader.ImageLoader;
import com.progdan.ibmorumbi.json.JSONParser;

import android.app.Activity;
import android.app.ListFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class FragmentMessage extends ListFragment {
	private String url = "http://mini.progdan.com/ibmorumbi/messages.php";
	private String settingsurl = "http://mini.progdan.com/ibmorumbi/appsettings.php";

	private List<Map<String, Object>> messages;
	private Map<String, Object> appsettigs;
	
	private static ProgressTask progressTask = null;
	private ProgressDialog dialog = null;

	private boolean videomode;
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		
		if(dialog != null && dialog.isShowing()){
			dialog.dismiss();
			dialog = null;
		}
	}

	@Override
	public void onResume() {
		super.onResume();

		// Get tracker.
		Tracker t = ((IBMorumbiApp) getActivity().getApplication())
				.getTracker();

		// Set screen name.
		// Where path is a String representing the screen name.
		t.setScreenName("Message Screen");

		// Send a screen view.
		t.send(new HitBuilders.AppViewBuilder().build());
	}

	@Override
	public void onStart() {
		super.onStart();
		// Get an Analytics tracker to report app starts & uncaught exceptions
		// etc.
		GoogleAnalytics.getInstance(getActivity()).reportActivityStart(
				getActivity());
	}

	@Override
	public void onStop() {
		super.onStop();
		// Get an Analytics tracker to report app starts & uncaught exceptions
		// etc.
		GoogleAnalytics.getInstance(getActivity()).reportActivityStop(
				getActivity());
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.message_list_layout, null);
		setHasOptionsMenu(true);
		setRetainInstance(true);

		// Get a Tracker (should auto-report)
		((IBMorumbiApp) getActivity().getApplication()).getTracker();

		// Identificação da versão do SO, Tipo de Dispositivo e versão do
		// Aplicativo
		String androidOS = Build.VERSION.RELEASE;
		String device = Build.MANUFACTURER + " (" + Build.BRAND + ") - "
				+ Build.MODEL;
		String app_ver = "unknow";
		try {
			app_ver = getActivity().getPackageManager().getPackageInfo(
					getActivity().getPackageName(), 0).versionName;
		} catch (NameNotFoundException e) {
			Log.v("ERROR", e.getMessage());
		}

		this.url = new String(
				"http://mini.progdan.com/ibmorumbi/messages.php?platform=Android&device="
						+ device + "&os=" + androidOS + "&client=" + app_ver)
				.replaceAll(" ", "%20");
		this.settingsurl = new String(
				"http://mini.progdan.com/ibmorumbi/appsettings.php?platform=Android&device="
						+ device + "&os=" + androidOS + "&client=" + app_ver)
				.replaceAll(" ", "%20");

		return v;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.message_type_menu, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);

		// Marca o item selecionado
		item.setChecked(true);

		switch (item.getItemId()) {
		case R.id.video_message:
			inicializarLista();
			videomode = true;
			new ProgressTask(FragmentMessage.this).execute();
			break;
		case R.id.audio_message:
			inicializarLista();
			videomode = false;
			new ProgressTask(FragmentMessage.this).execute();
			break;
		}

		return true;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		videomode = true;
		
		inicializarLista();
		
		progressTask = new ProgressTask(this);
		progressTask.execute();
	}
	
	private synchronized void inicializarLista(){
		if(dialog == null) {
			dialog = new ProgressDialog(getActivity());
			dialog.setMessage(getString(R.string.content_loading));
			if(!dialog.isShowing())
				dialog.show();
		}
	}
	
	private synchronized void atualizarLista(SimpleAdapter adapter){
		setListAdapter(adapter);
		if(dialog != null && dialog.isShowing()){
			dialog.dismiss();
			dialog = null;
		}
	}

	@Override
	public void onListItemClick(ListView list, View view, int position, long id) {
		// do something with the data
		Map<String, Object> map = messages.get(position);

		if (videomode) {
			Intent intent = new Intent(VideoActivity.ACAO_EXIBIR_VIDEO);
			intent.addCategory(VideoActivity.CATEGORIA_VIDEO);

			intent.putExtra(VideoActivity.EXTRA_VIDEO_URL,
					(String) map.get("video"));

			if (position > 0) {
				intent.putExtra(VideoActivity.EXTRA_VIDEO_THEME,
						(String) map.get("text"));
				intent.putExtra(VideoActivity.EXTRA_VIDEO_REFERENCES,
						(String) map.get("passagens"));
				intent.putExtra(VideoActivity.EXTRA_VIDEO_PREGADOR_IMG,
						(String) map.get("image"));
				intent.putExtra(VideoActivity.EXTRA_VIDEO_PREGADOR,
						(String) map.get("detail"));
				intent.putExtra(VideoActivity.EXTRA_VIDEO_CULTOS,
						(String) map.get("cultos"));
				intent.putExtra(VideoActivity.EXTRA_VIDEO_DATA,
						(String) map.get("data"));
			}

			startActivity(intent);
		} else {
			Intent intent = new Intent(AudioActivity.ACAO_EXIBIR_AUDIO);

			intent.addCategory(AudioActivity.CATEGORIA_AUDIO);

			intent.putExtra(AudioActivity.EXTRA_AUDIO_URL,
					(String) map.get("audio"));
			intent.putExtra(AudioActivity.EXTRA_AUDIO_THEME,
					(String) map.get("text"));
			intent.putExtra(AudioActivity.EXTRA_AUDIO_REFERENCES,
					(String) map.get("passagens"));
			intent.putExtra(AudioActivity.EXTRA_AUDIO_PREGADOR_IMG,
					(String) map.get("image"));
			intent.putExtra(AudioActivity.EXTRA_AUDIO_PREGADOR,
					(String) map.get("detail"));
			intent.putExtra(AudioActivity.EXTRA_AUDIO_CULTOS,
					(String) map.get("cultos"));
			intent.putExtra(AudioActivity.EXTRA_AUDIO_DATA,
					(String) map.get("data"));

			startActivity(intent);
		}
	}

	private class ProgressTask extends AsyncTask<String, Void, Boolean> {
		private Context context;

		public ProgressTask(ListFragment fragment) {
			context = fragment.getActivity();
		}

		@Override
		protected void onPostExecute(final Boolean success) {
			String[] from = { "image", "text", "detail" };
			int[] to = { R.id.imageView, R.id.textLabel, R.id.detailTextLabel };

			SimpleAdapter adapter = new SimpleAdapter(context, messages,
					R.layout.list_row, from, to);
			adapter.setViewBinder(new MyViewBinder());
			
			atualizarLista(adapter);
		}

		@Override
		protected Boolean doInBackground(String... args) {
			messages = new ArrayList<Map<String, Object>>();

			JSONParser jParser = new JSONParser();

			// get JSON config data from URL
			JSONArray json = jParser.getJSONFromUrl(settingsurl);

			try {
				JSONObject c = json.getJSONObject(0);

				String logo_mensagens = c.getString("logo_mensagens");
				String logo_morumbiplus = c.getString("logo_morumbiplus");
				String video_morumbiplus = c.getString("video_morumbiplus");
				String video_morumbiplushd = c.getString("video_morumbiplushd");

				appsettigs = new HashMap<String, Object>();

				// Add child node to HashMap key & value
				appsettigs.put("logo_mensagens", logo_mensagens);
				appsettigs.put("logo_morumbiplus", logo_morumbiplus);
				appsettigs.put("video_morumbiplus", video_morumbiplus);
				appsettigs.put("video_morumbiplushd", video_morumbiplushd);
			} catch (JSONException e) {
				e.printStackTrace();
			}

			// Configura a imagem da série de mensagens
			Activity activity = getActivity();
			if(activity != null){
				activity.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						// Loader image - will be shown before loading image
//						int loader = R.drawable.image_serie;
						int loader = 0;
	
						// Imageview to show
						ImageView imageView = (ImageView) getView().findViewById(
								R.id.message_series);
	
						// Image url
						String image_url = (String) appsettigs
								.get("logo_mensagens");
	
						// ImageLoader class instance
						ImageLoader imgLoader = new ImageLoader(getView()
								.getContext().getApplicationContext());
						imgLoader.DisplayImage(image_url, loader, imageView);
					}
				});
			}

			// Se for Vídeo, apresenta o primeiro item o Morumbi+
			if (videomode) {
				String text = "Morumbi+";
				String detail = "";
				Object image = R.drawable.image_morumbiplus;
				String video = (String) appsettigs.get("video_morumbiplus");

				HashMap<String, Object> map = new HashMap<String, Object>();

				// Add child node to HashMap key & value
				map.put("text", text);
				map.put("detail", detail);
				map.put("image", image);
				map.put("video", video);

				messages.add(map);
			}

			jParser = new JSONParser();

			// get JSON data from URL
			json = jParser.getJSONFromUrl(url);

			for (int i = 0; i < json.length(); i++) {
				try {
					JSONObject c = json.getJSONObject(i);

					String data = c.getString("data");
					String detail = c.getString("pregador");
					Object image = c.getString("pregador_img");
					String cultos = c.getString("cultos");
					String text = c.getString("tema");
					String passagens = c.getString("passagens");
					String audio = c.getString("audio");
					String video = c.getString("video");

					HashMap<String, Object> map = new HashMap<String, Object>();

					// Add child node to HashMap key & value
					map.put("data", data);
					map.put("detail", detail);
					map.put("image", image);
					map.put("cultos", cultos);
					map.put("text", text);
					map.put("passagens", passagens);
					map.put("audio", audio);
					map.put("video", video);
					messages.add(map);

				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			return null;
		}
	}
}
