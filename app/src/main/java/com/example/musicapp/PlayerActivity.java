package com.example.musicapp;

import android.os.Bundle;
import android.transition.Explode;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.media3.common.MediaItem;
import androidx.media3.common.Player;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.ui.PlayerView;

import com.bumptech.glide.Glide;
import com.example.musicapp.models.SongModel;

public class PlayerActivity extends AppCompatActivity {

    private ExoPlayer exoPlayer;
    private SongModel songModel;

    private ImageView coverImg;
    private ImageView gifImg;
    private TextView songName;
    private TextView singerName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_player);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        this.Init();
    }

    public void Init(){
        this.coverImg = this.findViewById(R.id.player_songcoverUrl_image_view);
        this.gifImg = this.findViewById(R.id.player_songgif_image_view);
        this.songName = this.findViewById(R.id.player_songName_text_view);
        this.singerName = this.findViewById(R.id.player_singerName_text_view);

        this.songModel = (SongModel) this.getIntent().getSerializableExtra("SONG");
        Glide.with(this.coverImg).load(this.songModel.getCoverUrl()).circleCrop().into(this.coverImg);
        Glide.with(this.gifImg).load(R.drawable.media_playing).circleCrop().into(this.gifImg);
        this.songName.setText(songModel.getSongName());
        this.singerName.setText(songModel.getSingerName());

        //SetUpExoPlayer();
        CacheExoPlayer.getInstance().startPlaying(this, this.songModel);

        this.exoPlayer = CacheExoPlayer.getInstance().getExoPlayer();
        this.exoPlayer.addListener(new Player.Listener() {
            @Override
            public void onIsPlayingChanged(boolean isPlaying) {
                if (isPlaying) {
                    showGif(true);
                } else {
                    showGif(false);
                }
            }
        });
    }

    public void showGif(boolean isPlaying){
        if(isPlaying)
            this.gifImg.setVisibility(View.VISIBLE);
        else
            this.gifImg.setVisibility(View.INVISIBLE);
    }
}