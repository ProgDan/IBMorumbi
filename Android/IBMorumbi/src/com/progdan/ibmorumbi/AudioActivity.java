package com.progdan.ibmorumbi;

import java.io.IOException;

import com.progdan.ibmorumbi.imageloader.ImageLoader;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class AudioActivity extends Activity {
	public static final String EXTRA_AUDIO_URL = "ibmorumbi.EXTRA_AUDIO_URL";
	public static final String EXTRA_AUDIO_THEME = "ibmorumbi.EXTRA_AUDIO_THEME";
	public static final String EXTRA_AUDIO_REFERENCES = "ibmorumbi.EXTRA_AUDIO_REFERENCES";
	public static final String EXTRA_AUDIO_PREGADOR_IMG = "ibmorumbi.EXTRA_AUDIO_PREGADOR_IMG";
	public static final String EXTRA_AUDIO_PREGADOR = "ibmorumbi.EXTRA_AUDIO_PREGADOR";
	public static final String EXTRA_AUDIO_CULTOS = "ibmorumbi.EXTRA_AUDIO_CULTOS";
	public static final String EXTRA_AUDIO_DATA = "ibmorumbi.EXTRA_AUDIO_DATA";

	public static final String ACAO_EXIBIR_AUDIO = "ibmorumbi.ACAO_EXIBIR_AUDIO";
	public static final String CATEGORIA_AUDIO = "ibmorumbi.CATEGORIA_AUDIO";

	private String link;

	private Button btnPlay;
	private Button btnPause;
	private Button btnStop;
	private Button btnBack;

	private MediaPlayer mediaPlayer;

	// remain false till media is not completed, inside OnCompletionListener
	// make it true.
	private boolean intialStage = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_audio);

		TextView theme = (TextView) findViewById(R.id.message_theme);
		TextView references = (TextView) findViewById(R.id.message_references);
		TextView pregador = (TextView) findViewById(R.id.message_pregador);
		TextView cultos = (TextView) findViewById(R.id.message_cultos);
		TextView data = (TextView) findViewById(R.id.message_data);
		ImageView pregador_img = (ImageView) findViewById(R.id.message_pregador_img);

		Intent intent = getIntent();
		if (intent.hasExtra(EXTRA_AUDIO_URL)) {
			link = intent.getStringExtra(EXTRA_AUDIO_URL);

			theme.setText(intent.getStringExtra(EXTRA_AUDIO_THEME));
			references.setText(intent.getStringExtra(EXTRA_AUDIO_REFERENCES));
			pregador.setText(intent.getStringExtra(EXTRA_AUDIO_PREGADOR));
			cultos.setText(intent.getStringExtra(EXTRA_AUDIO_CULTOS));
			data.setText(intent.getStringExtra(EXTRA_AUDIO_DATA));

			int loader = R.drawable.ibmorumbi;
			String image_url = intent.getStringExtra(EXTRA_AUDIO_PREGADOR_IMG);
			// ImageLoader class instance
			ImageLoader imgLoader = new ImageLoader(getApplicationContext());
			imgLoader.DisplayImage(image_url, loader, pregador_img);

		}

		btnPlay = (Button) findViewById(R.id.buttonPlay);
		btnPause = (Button) findViewById(R.id.buttonPause);
		btnStop = (Button) findViewById(R.id.buttonStop);
		btnBack = (Button) findViewById(R.id.buttonBack);

		mediaPlayer = new MediaPlayer();
		mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

		btnPlay.setOnClickListener(clickPlay);
		btnPause.setOnClickListener(clickPause);
		btnStop.setOnClickListener(clickStop);
		btnBack.setOnClickListener(clickBack);

		btnPlay.setEnabled(true);
		btnPlay.getBackground().setColorFilter(null);
		btnPause.setEnabled(false);
		btnPause.getBackground().setColorFilter(Color.GRAY,
				PorterDuff.Mode.MULTIPLY);
		btnStop.setEnabled(false);
		btnStop.getBackground().setColorFilter(Color.GRAY,
				PorterDuff.Mode.MULTIPLY);
		btnBack.setEnabled(false);
		btnBack.getBackground().setColorFilter(Color.GRAY,
				PorterDuff.Mode.MULTIPLY);
	}

	private OnClickListener clickPlay = new OnClickListener() {
		@Override
		public void onClick(View v) {
			btnPlay.setEnabled(false);
			btnPlay.getBackground().setColorFilter(Color.GRAY,
					PorterDuff.Mode.MULTIPLY);
			btnPause.setEnabled(true);
			btnPause.getBackground().setColorFilter(null);
			btnStop.setEnabled(true);
			btnStop.getBackground().setColorFilter(null);
			btnBack.setEnabled(true);
			btnBack.getBackground().setColorFilter(null);

			if (intialStage)
				new Player().execute(link);
			else {
				if (!mediaPlayer.isPlaying())
					mediaPlayer.start();
			}
		}
	};

	private OnClickListener clickPause = new OnClickListener() {
		@Override
		public void onClick(View v) {
			btnPlay.setEnabled(true);
			btnPlay.getBackground().setColorFilter(null);
			btnPause.setEnabled(false);
			btnPause.getBackground().setColorFilter(Color.GRAY,
					PorterDuff.Mode.MULTIPLY);
			btnStop.setEnabled(true);
			btnStop.getBackground().setColorFilter(null);
			btnBack.setEnabled(true);
			btnBack.getBackground().setColorFilter(null);

			if (mediaPlayer.isPlaying())
				mediaPlayer.pause();
		}
	};

	private OnClickListener clickStop = new OnClickListener() {
		@Override
		public void onClick(View v) {
			intialStage = true;
			btnPlay.setEnabled(true);
			btnPlay.getBackground().setColorFilter(null);
			btnPause.setEnabled(false);
			btnPause.getBackground().setColorFilter(Color.GRAY,
					PorterDuff.Mode.MULTIPLY);
			btnStop.setEnabled(false);
			btnStop.getBackground().setColorFilter(Color.GRAY,
					PorterDuff.Mode.MULTIPLY);
			btnBack.setEnabled(false);
			btnBack.getBackground().setColorFilter(Color.GRAY,
					PorterDuff.Mode.MULTIPLY);

			mediaPlayer.stop();
			mediaPlayer.reset();
		}
	};

	private OnClickListener clickBack = new OnClickListener() {
		@Override
		public void onClick(View v) {
			btnPlay.setEnabled(false);
			btnPlay.getBackground().setColorFilter(Color.GRAY,
					PorterDuff.Mode.MULTIPLY);
			btnPause.setEnabled(true);
			btnPause.getBackground().setColorFilter(null);
			btnStop.setEnabled(true);
			btnStop.getBackground().setColorFilter(null);
			btnBack.setEnabled(true);
			btnBack.getBackground().setColorFilter(null);

			mediaPlayer.stop();
			mediaPlayer.reset();

			intialStage = true;
			new Player().execute(link);
		}
	};

	/**
	 * preparing mediaplayer will take sometime to buffer the content so prepare
	 * it inside the background thread and starting it on UI thread.
	 */
	class Player extends AsyncTask<String, Void, Boolean> {
		private ProgressDialog progress;

		@Override
		protected Boolean doInBackground(String... params) {
			Boolean prepared;
			try {
				mediaPlayer.setDataSource(params[0]);

				mediaPlayer.setOnCompletionListener(new OnCompletionListener() {
					@Override
					public void onCompletion(MediaPlayer mp) {
						intialStage = true;
						btnPlay.setEnabled(true);
						btnPlay.getBackground().setColorFilter(null);
						btnPause.setEnabled(false);
						btnPause.getBackground().setColorFilter(Color.GRAY,
								PorterDuff.Mode.MULTIPLY);
						btnStop.setEnabled(false);
						btnStop.getBackground().setColorFilter(Color.GRAY,
								PorterDuff.Mode.MULTIPLY);
						btnBack.setEnabled(false);
						btnBack.getBackground().setColorFilter(Color.GRAY,
								PorterDuff.Mode.MULTIPLY);

						mediaPlayer.stop();
						mediaPlayer.reset();
					}
				});
				mediaPlayer.prepare();
				prepared = true;
			} catch (SecurityException e) {
				prepared = false;
				e.printStackTrace();
			} catch (IllegalStateException e) {
				prepared = false;
				e.printStackTrace();
			} catch (IOException e) {
				prepared = false;
				e.printStackTrace();
			}
			return prepared;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			if (progress.isShowing()) {
				progress.cancel();
			}
			Log.d("Prepared", "//" + result);
			mediaPlayer.start();

			intialStage = false;
		}

		public Player() {
			progress = new ProgressDialog(AudioActivity.this);
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			this.progress.setMessage(getString(R.string.buffering_audio));
			this.progress.show();
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (mediaPlayer != null) {
			mediaPlayer.reset();
			mediaPlayer.release();
			mediaPlayer = null;
		}
	}
}
