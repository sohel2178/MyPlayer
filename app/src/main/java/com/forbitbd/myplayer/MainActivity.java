package com.forbitbd.myplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.forbitbd.myplayer.fullScreen.FullScreenPlayerActivity;
import com.forbitbd.myplayer.models.Category;
import com.forbitbd.myplayer.models.Movie;
import com.forbitbd.myplayer.rtmp.RTMPPlayerActivity;
import com.forbitbd.myplayer.utils.Constant;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.util.Util;

public class MainActivity extends AppCompatActivity {

    Button button ;

    private String url = "http://www.freemediabd.com/ftpdata1/Animation/1996-Aladdin.And.The.King.Of.Thieves.mp4";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Category category = new Category();
        category.setName("Animation");

        Movie movie = new Movie();
        movie.setTitle("Dino King: Journey to Fire Mountain");
        movie.setDescription("Speckles, a ferocious tarbosaurus and his young son junior, mourning the loss of their family in an epic battle, roam the lands in search of food, adventure and peace. Under the watchful eye of his dad, Junior is growing up healthy and strong, but with an overconfidence thanks to his young age. After one encounter results in Junior being kidnapped, Speckles embarks on an adventure to the ends of earth to find his son. Encountering friend and foe, ally and enemy, Speckles will stop at nothing and will take on all corners to save his offspring.");
        movie.setImage_url("http://103.91.144.230/Admin/main/images/tt7659018/poster/w6N6xJvvjY6pLxOnHz5yu9Z8quG.jpg");
        movie.setVideo_url("http://103.144.201.119:4444/movies/bollywood/2019/Dabangg.3.2019__1080p.BRrip__E-BOX.mp4");
        movie.setRelease_date("2018-12-25");
        movie.setPlay_time("01 H 30 M");
        movie.setLanguage("English");
        movie.setCategory(category);
        movie.setImdb_rating("7.7 / 10");
        movie.setViews(100);

        button = findViewById(R.id.start_player);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), FullScreenPlayerActivity.class);
                intent.putExtra(Constant.MOVIE,movie);
                startActivity(intent);
            }
        });




    }



}