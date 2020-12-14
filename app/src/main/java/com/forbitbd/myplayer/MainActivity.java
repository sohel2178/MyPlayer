package com.forbitbd.myplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.util.Util;

public class MainActivity extends AppCompatActivity {

    Button button ;

    private String url = "http://103.91.144.230/ftpdata/Movies/AMINATION_MOVIE/2000_2010/An%20Extremely%20Goofy%20Movie%20%282000%29/An.Extremely.Goofy.Movie.2000.1080p.WEBRip.x264-%5BYTS.AM%5D.mp4";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = findViewById(R.id.start_player);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),MyPlayerActivity.class);
                intent.putExtra("VIDEO_URL",url);
                startActivity(intent);
            }
        });




    }



}