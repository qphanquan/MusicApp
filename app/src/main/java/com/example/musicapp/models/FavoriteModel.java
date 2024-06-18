package com.example.musicapp.models;

import java.io.Serializable;

public class FavoriteModel implements Serializable {
    private String id;
    private String songName;
    private String singerName;
    private String coverUrl;
    private String songUrl;

    public FavoriteModel() {}

    public FavoriteModel(String id, String songName, String singerName, String coverUrl, String songUrl) {
        this.id = id;
        this.songName = songName;
        this.singerName = singerName;
        this.coverUrl = coverUrl;
        this.songUrl = songUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public String getSingerName() {
        return singerName;
    }

    public void setSingerName(String singerName) {
        this.singerName = singerName;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public String getSongUrl() {
        return songUrl;
    }

    public void setSongUrl(String songUrl) {
        this.songUrl = songUrl;
    }
}
