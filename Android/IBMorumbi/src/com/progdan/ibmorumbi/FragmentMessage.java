package com.progdan.ibmorumbi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.progdan.ibmorumbi.imageloader.ImageLoader;
import com.progdan.ibmorumbi.json.JSONParser;

import android.app.ListFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
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
	private static String url = "http://mini.progdan.com/ibmorumbi/messages.php";
	private static String settingsurl = "http://mini.progdan.com/ibmorumbi/appsettings.php";

	private List<Map<String, Object>> messages;
	private Map<String, Object> appsettigs;

	private boolean videomode;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.message_list_layout, null);
		setHasOptionsMenu(true);
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
			videomode = true;
			new ProgressTask(FragmentMessage.this).execute();
			break;
		case R.id.audio_message:
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
		new ProgressTask(FragmentMessage.this).execute();
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

			SimpleAdapter adapter = new SimpleAdapter(context, messages,
					R.layout.list_row, from, to);
			adapter.setViewBinder(new MyViewBinder());

			setListAdapter(adapter);
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
			getActivity().runOnUiThread(new Runnable() {
				@Override
				public void run() {
					// Loader image - will be shown before loading image
					int loader = R.drawable.image_serie;

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
