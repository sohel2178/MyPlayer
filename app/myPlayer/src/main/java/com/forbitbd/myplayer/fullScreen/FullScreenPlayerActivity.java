package com.forbitbd.myplayer.fullScreen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.forbitbd.myplayer.AppPreference;
import com.forbitbd.myplayer.MinuteListener;
import com.forbitbd.myplayer.MyThread;
import com.forbitbd.myplayer.R;
import com.forbitbd.myplayer.models.Movie;
import com.forbitbd.myplayer.utils.Constant;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.DefaultMediaSourceFactory;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.squareup.picasso.Picasso;

public class FullScreenPlayerActivity extends AppCompatActivity implements MinuteListener {

    PlayerView playerView;

    private SimpleExoPlayer player;



    private boolean playWhenReady = true;
    private int currentWindow = 0;
    private long playbackPosition = 0;

    private Movie movie;

    boolean fullscreen = false;

    private ImageView fullscreenButton;

    private Toolbar toolbar;

    private MyThread myThread;
    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_player);

        this.movie = (Movie) getIntent().getSerializableExtra(Constant.MOVIE);

        setupToolbar(R.id.toolbar);
        getSupportActionBar().setTitle(movie.getTitle());
        
        initView();

        playerView = findViewById(R.id.player);
        fullscreenButton = playerView.findViewById(R.id.exo_fullscreen_icon);
        fullscreenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(fullscreen) {
                    fullscreenButton.setImageDrawable(ContextCompat.getDrawable(FullScreenPlayerActivity.this, R.drawable.ic_fullscreen_expand));
                    getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
                    if(getSupportActionBar() != null){
                        getSupportActionBar().show();
                    }
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) playerView.getLayoutParams();
                    params.width = params.MATCH_PARENT;
                    params.height = (int) ( 250 * getApplicationContext().getResources().getDisplayMetrics().density);
                    playerView.setLayoutParams(params);
                    fullscreen = false;
                }else{
                    fullscreenButton.setImageDrawable(ContextCompat.getDrawable(FullScreenPlayerActivity.this, R.drawable.ic_fullscreen_skrink));
                    getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN
                            |View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                            |View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
                    if(getSupportActionBar() != null){
                        getSupportActionBar().hide();
                    }
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) playerView.getLayoutParams();
                    params.width = params.MATCH_PARENT;
                    params.height = params.MATCH_PARENT;
                    playerView.setLayoutParams(params);
                    fullscreen = true;
                }

            }
        });


        if(mInterstitialAd==null){
            mInterstitialAd = new InterstitialAd(this);
        }


        mInterstitialAd.setAdUnitId(getString(R.string.inter_ad_id));
//        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
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
                AppPreference.getInstance(getApplicationContext()).resetBackCounter();
            }

            @Override
            public void onAdLeftApplication() {
                super.onAdLeftApplication();
            }
        });



        myThread = new MyThread(this);
    }

    private void initView() {
        ImageView ivImage = findViewById(R.id.image);
        TextView tvTitle = findViewById(R.id.title);
        TextView tvCategory = findViewById(R.id.category);
        TextView tvYear = findViewById(R.id.year);
        TextView tvLanguage = findViewById(R.id.language);
        TextView tvIMDBRating = findViewById(R.id.imdb_rating);
        TextView tvPlayTime = findViewById(R.id.play_time);
        TextView tvViews = findViewById(R.id.views);
        TextView tvDescription = findViewById(R.id.description);

        if(movie.getImage_url()!=null && !movie.getImage_url().equals("")){
            Picasso.get().load(movie.getImage_url()).into(ivImage);
        }



        tvTitle.setText(movie.getTitle());
        tvCategory.setText(movie.getCategory().getName());
        tvYear.setText(movie.getRelease_date().split("-")[0]);
        tvLanguage.setText(movie.getLanguage());
        tvIMDBRating.setText(movie.getImdb_rating());
        tvPlayTime.setText(movie.getPlay_time());
        tvViews.setText(String.valueOf(movie.getViews()));
        tvDescription.setText(movie.getDescription());


    }

    public void setupToolbar(int id) {
        toolbar = (Toolbar) findViewById(id);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }


    private void initialize(){
        player = new SimpleExoPlayer.Builder(this).build();
        playerView.setPlayer(player);

        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(
                this,
                Util.getUserAgent(this, "exoplayer"));
/*
        ProgressiveMediaSource mediaSource = new ProgressiveMediaSource.Factory(dataSourceFactory)
                .createMediaSource(Uri.parse("file:///android_asset/jellyfish-3-mbps-hd-h264.mkv"));*/

        MediaSource videoSource = new DefaultMediaSourceFactory(dataSourceFactory)
                .createMediaSource(MediaItem.fromUri(Uri.parse(movie.getVideo_url())));

        MediaItem mediaItem = MediaItem.fromUri(movie.getVideo_url());
        player.setMediaSource(videoSource);
        player.setPlayWhenReady(playWhenReady);
        player.seekTo(currentWindow, playbackPosition);
        player.prepare();

    }

    @Override
    protected void onStart() {
        super.onStart();

        if(myThread.isAlive()){
            myThread.startThread();
        }

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
            super.onBackPressed();
        }
    }


    @Override
    public void increment() {
        AppPreference.getInstance(this).increaseCounter();

        if(AppPreference.getInstance(this).getCounter()>=30){

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    showInterAd();
                }
            });


        }

    }

    @Override
    public void onBackPressed() {
        AppPreference.getInstance(this).increaseBackCounter();

        if(AppPreference.getInstance(this).getBackCounter()>3){
            showInterAd();
        }else {
            super.onBackPressed();
        }

    }
}