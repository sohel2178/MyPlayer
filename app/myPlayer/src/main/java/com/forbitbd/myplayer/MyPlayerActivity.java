package com.forbitbd.myplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;

import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.util.Log;
import com.google.android.exoplayer2.util.Util;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

public class MyPlayerActivity extends AppCompatActivity implements MinuteListener{

    PlayerView playerView;

    private SimpleExoPlayer player;

    private String url = "http://103.91.144.230/ftpdata/Movies/AMINATION_MOVIE/2000_2010/An%20Extremely%20Goofy%20Movie%20%282000%29/An.Extremely.Goofy.Movie.2000.1080p.WEBRip.x264-%5BYTS.AM%5D.mp4";

    private boolean playWhenReady = true;
    private int currentWindow = 0;
    private long playbackPosition = 0;

    private MyThread myThread;

    private InterstitialAd mInterstitialAd;

    private int counter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_my_player);
        playerView = findViewById(R.id.player_view);

        Log.d("MYThread",Thread.currentThread().getName());

        this.counter = AppPreference.getInstance(this).getCounter();

        if(mInterstitialAd==null){
            mInterstitialAd = new InterstitialAd(this);
        }
        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());




        mInterstitialAd.setAdListener(new AdListener(){
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
            }

            @Override
            public void onAdClosed() {
                super.onAdClosed();
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
                AppPreference.getInstance(getApplicationContext()).resetCounter();
            }

            @Override
            public void onAdLeftApplication() {
                super.onAdLeftApplication();
            }
        });



        myThread = new MyThread(this);


    }

    private void initialize(){
        player = new SimpleExoPlayer.Builder(this).build();
        playerView.setPlayer(player);
        MediaItem mediaItem = MediaItem.fromUri(url);
        player.setMediaItem(mediaItem);
        player.setPlayWhenReady(playWhenReady);
        player.seekTo(currentWindow, playbackPosition);
        player.prepare();

    }

    @Override
    protected void onStart() {
        super.onStart();
        myThread.startThread();
        if(Util.SDK_INT>=24){
            initialize();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        hideSystemUi();
        if ((Util.SDK_INT < 24 || player == null)) {
            initialize();
        }

    }

    @SuppressLint("InlinedApi")
    private void hideSystemUi() {
        playerView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

    }


    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT < 24) {
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        myThread.stopThread();
        if (Util.SDK_INT >= 24) {
            releasePlayer();
        }
    }

    private void releasePlayer() {
        if (player != null) {
            playWhenReady = player.getPlayWhenReady();
            playbackPosition = player.getCurrentPosition();
            currentWindow = player.getCurrentWindowIndex();
            player.release();
            player = null;
        }

    }

    public void showInterAd(){
        if(mInterstitialAd.isLoaded()){
            mInterstitialAd.show();
        }else{
            AppPreference.getInstance(this).increaseCounter();
        }
    }


    @Override
    public void increment() {

        AppPreference.getInstance(this).increaseCounter();

        if(AppPreference.getInstance(this).getCounter()>=15){

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    showInterAd();
                }
            });


        }


    }
}