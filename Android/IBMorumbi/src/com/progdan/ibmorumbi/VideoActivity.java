package com.progdan.ibmorumbi;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.progdan.ibmorumbi.imageloader.ImageLoader;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

public class VideoActivity extends Activity {
	public static final String EXTRA_VIDEO_URL = "ibmorumbi.EXTRA_VIDEO_URL";
	public static final String EXTRA_VIDEO_THEME = "ibmorumbi.EXTRA_VIDEO_THEME";
	public static final String EXTRA_VIDEO_REFERENCES = "ibmorumbi.EXTRA_VIDEO_REFERENCES";
	public static final String EXTRA_VIDEO_PREGADOR_IMG = "ibmorumbi.EXTRA_VIDEO_PREGADOR_IMG";
	public static final String EXTRA_VIDEO_PREGADOR = "ibmorumbi.EXTRA_VIDEO_PREGADOR";
	public static final String EXTRA_VIDEO_CULTOS = "ibmorumbi.EXTRA_VIDEO_CULTOS";
	public static final String EXTRA_VIDEO_DATA = "ibmorumbi.EXTRA_VIDEO_DATA";

	public static final String ACAO_EXIBIR_VIDEO = "ibmorumbi.ACAO_EXIBIR_VIDEO";
	public static final String CATEGORIA_VIDEO = "ibmorumbi.CATEGORIA_VIDEO";

	private String link;

	private static ProgressDialog progressDialog;
	VideoView videoView;

	@Override
	public void onStart() {
		super.onStart();
		//Get an Analytics tracker to report app starts & uncaught exceptions etc.
		GoogleAnalytics.getInstance(this).reportActivityStart(this);
	}
	@Override
	public void onStop() {
		super.onStop();
		//Get an Analytics tracker to report app starts & uncaught exceptions etc.
		GoogleAnalytics.getInstance(this).reportActivityStop(this);
	}
	@Override
	public void onResume() {
		super.onResume();

        // Get tracker.
        Tracker t = ((IBMorumbiApp) getApplication()).getTracker();

        // Set screen name.
        // Where path is a String representing the screen name.
        t.setScreenName("MessageVideoPlayer Screen");

        // Send a screen view.
        t.send(new HitBuilders.AppViewBuilder().build());
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_video);
		//Get a Tracker (should auto-report)
		((IBMorumbiApp) getApplication()).getTracker();
		
		videoView = (VideoView) findViewById(R.id.VideoView);

		ImageView logo = (ImageView) findViewById(R.id.message_logo);
		TextView theme = (TextView) findViewById(R.id.message_theme);
		TextView references = (TextView) findViewById(R.id.message_references);
		TextView pregador = (TextView) findViewById(R.id.message_pregador);
		TextView cultos = (TextView) findViewById(R.id.message_cultos);
		TextView data = (TextView) findViewById(R.id.message_data);
		ImageView pregador_img = (ImageView) findViewById(R.id.message_pregador_img);

		Intent intent = getIntent();
		if (intent.hasExtra(EXTRA_VIDEO_URL)) {
			link = intent.getStringExtra(EXTRA_VIDEO_URL);
		}

		if (intent.hasExtra(EXTRA_VIDEO_THEME)) {
			logo.setVisibility(View.GONE);
			theme.setVisibility(View.VISIBLE);
			theme.setText(intent.getStringExtra(EXTRA_VIDEO_THEME));
			references.setVisibility(View.VISIBLE);
			references.setText(intent.getStringExtra(EXTRA_VIDEO_REFERENCES));
			pregador.setVisibility(View.VISIBLE);
			pregador.setText(intent.getStringExtra(EXTRA_VIDEO_PREGADOR));
			cultos.setVisibility(View.VISIBLE);
			cultos.setText(intent.getStringExtra(EXTRA_VIDEO_CULTOS));
			data.setVisibility(View.VISIBLE);
			data.setText(intent.getStringExtra(EXTRA_VIDEO_DATA));

			pregador_img.setVisibility(View.VISIBLE);
			int loader = R.drawable.ibmorumbi;
			String image_url = intent.getStringExtra(EXTRA_VIDEO_PREGADOR_IMG);
			// ImageLoader class instance
			ImageLoader imgLoader = new ImageLoader(getApplicationContext());
			imgLoader.DisplayImage(image_url, loader, pregador_img);
		} else {
			logo.setVisibility(View.VISIBLE);
			theme.setVisibility(View.GONE);
			references.setVisibility(View.GONE);
			pregador.setVisibility(View.GONE);
			cultos.setVisibility(View.GONE);
			data.setVisibility(View.GONE);
			pregador_img.setVisibility(View.GONE);
		}

		progressDialog = ProgressDialog.show(VideoActivity.this, "",
				getString(R.string.buffering_video), true);
		progressDialog.setCancelable(true);

		PlayVideo();
	}

	private void PlayVideo() {
		try {
			getWindow().setFormat(PixelFormat.TRANSLUCENT);
			MediaController mediaController = new MediaController(this);
			mediaController.setAnchorView(videoView);

			Uri video = Uri.parse(link);
			videoView.setMediaController(mediaController);
			videoView.setVideoURI(video);
			videoView.requestFocus();
			videoView.setOnPreparedListener(new OnPreparedListener() {
				@Override
				public void onPrepared(MediaPlayer mp) {
					progressDialog.dismiss();
					videoView.start();
				}
			});
		} catch (Exception e) {
			// TODO: handle exception
			progressDialog.dismiss();
			Toast.makeText(
					this,
					getString(R.string.video_play_error) + ": "
							+ e.getMessage(), Toast.LENGTH_SHORT).show();
	         Log.e("VideoActivity ERROR", e.getLocalizedMessage()); // enable to log errors
			finish();
		}
	}
}
