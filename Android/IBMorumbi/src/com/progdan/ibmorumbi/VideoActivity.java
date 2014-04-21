package com.progdan.ibmorumbi;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

public class VideoActivity extends Activity {
	public static final String EXTRA_VIDEO_URL = "ibmorumbi.EXTRA_VIDEO_URL";
	public static final String ACAO_EXIBIR_VIDEO = "ibmorumbi.ACAO_EXIBIR_VIDEO";
	public static final String CATEGORIA_VIDEO = "ibmorumbi.CATEGORIA_VIDEO";
	
	private String link="http://www.ibmorumbi.org.br/audio/2014/morumbi_mais/morumbi_mais.mp4";
	
	private static ProgressDialog progressDialog;
	VideoView videoView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.videodisplay);
		videoView = (VideoView)findViewById(R.id.VideoView);

		Intent intent = getIntent();
		if(intent.hasExtra(EXTRA_VIDEO_URL)){
			link = intent.getStringExtra(EXTRA_VIDEO_URL);
		}
		
		
		progressDialog = ProgressDialog.show(VideoActivity.this, "", "Buffering video...", true);
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
			videoView.setOnPreparedListener(new OnPreparedListener(){
				@Override
				public void onPrepared(MediaPlayer mp){
					progressDialog.dismiss();
					videoView.start();
				}
			});
		} catch (Exception e) {
			// TODO: handle exception
			progressDialog.dismiss();
			Toast.makeText(this, "Video Play Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
			e.printStackTrace();
			finish();
		}
	}

}
