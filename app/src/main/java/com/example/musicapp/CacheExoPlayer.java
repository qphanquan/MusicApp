package com.example.musicapp;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import androidx.media3.common.MediaItem;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.ui.PlayerView;

import com.example.musicapp.models.SongModel;

public class CacheExoPlayer {
    private static CacheExoPlayer Instance;
    private ExoPlayer exoPlayer;
    private SongModel currentSong = null;

    public static CacheExoPlayer getInstance(){
        if(Instance == null)
            Instance = new CacheExoPlayer();
        return Instance;
    }
    public void startPlaying(Context context, SongModel song){
        if(exoPlayer == null)
            this.exoPlayer = new ExoPlayer.Builder(context).build();

        if(currentSong != song){
            this.currentSong = song;
//            PlayerView playerView = ((Activity)context).findViewById(R.id.player_view);
//            playerView.setPlayer(this.exoPlayer);
            MediaItem mediaItem = MediaItem.fromUri(this.currentSong.getSongUrl());
            exoPlayer.setMediaItem(mediaItem);
            exoPlayer.prepare();
            exoPlayer.play();
        }
    }

    public SongModel getCurrentSong() {
        return currentSong;
    }

    public ExoPlayer getExoPlayer(){
        return  this.exoPlayer;
    }
}
