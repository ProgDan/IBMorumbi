package com.progdan.ibmorumbi;

import java.io.IOException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class AudioActivity extends Activity {
	public static final String EXTRA_AUDIO_URL = "ibmorumbi.EXTRA_AUDIO_URL";
	public static final String ACAO_EXIBIR_AUDIO = "ibmorumbi.ACAO_EXIBIR_AUDIO";
	public static final String CATEGORIA_AUDIO = "ibmorumbi.CATEGORIA_AUDIO";
	
	private String link;

	
	private Button btn;
	private boolean playPause;	// help to toggle between play and pause.
	private MediaPlayer mediaPlayer;
	
	// remain false till media is not completed, inside OnCompletionListener make it true.
	private boolean intialStage = true;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.audiodisplay);
		
		Intent intent = getIntent();
		if(intent.hasExtra(EXTRA_AUDIO_URL)){
			link = intent.getStringExtra(EXTRA_AUDIO_URL);
		}		
		
		btn = (Button) findViewById(R.id.button1);
		mediaPlayer = new MediaPlayer();
		mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
		btn.setOnClickListener(pausePlay);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	
	private OnClickListener pausePlay = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if (!playPause) {
				btn.setBackgroundResource(R.drawable.button_pause);
				if (intialStage)
					new Player()
					.execute(link);
				else {
					if (!mediaPlayer.isPlaying())
						mediaPlayer.start();
				}
				playPause = true;
			}
			else {
				btn.setBackgroundResource(R.drawable.button_play);
				if (mediaPlayer.isPlaying())
					mediaPlayer.pause();
				playPause = false;
			}
		}
	};
	
	/**
	 * preparing mediaplayer will take sometime to buffer the content so prepare it inside the background thread and starting it on UI thread.
	 * @author progdan
	 *
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
						playPause=false;
						btn.setBackgroundResource(R.drawable.button_play);
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
			this.progress.setMessage("Buffering...");
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
