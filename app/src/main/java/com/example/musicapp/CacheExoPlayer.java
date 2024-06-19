package com.example.musicapp;

import android.app.Activity;
import android.content.Context;

import androidx.media3.common.MediaItem;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.ui.PlayerView;

import com.example.musicapp.models.SongModel;

public class CacheExoPlayer {
    private static CacheExoPlayer Instance;
    private ExoPlayer exoPlayer;
    private SongModel songModel;

    public static CacheExoPlayer getInstance(){
        if(Instance == null)
            Instance = new CacheExoPlayer();
        return Instance;
    }
    public void startPlaying(Context context, SongModel song){
        this.songModel = song;
        PlayerView playerView = ((Activity)context).findViewById(R.id.player_view);
        this.exoPlayer = new ExoPlayer.Builder(context).build();
        playerView.setPlayer(this.exoPlayer);
        MediaItem mediaItem = MediaItem.fromUri(this.songModel.getSongUrl());
        exoPlayer.setMediaItem(mediaItem);
        exoPlayer.prepare();
        exoPlayer.play();
    }

    public SongModel getSongModel(){
        return this.songModel;
    }

    public ExoPlayer getExoPlayer(){
        return  this.exoPlayer;
    }
}
