package com.example.musicapp.models;

import java.util.List;

public class PlayListModel {
    private String namePlayList;
    private List<String> Songs;

    public PlayListModel(String namePlayList, List<String> songs) {
        this.namePlayList = namePlayList;
        Songs = songs;
    }

    public String getNamePlayList() {
        return namePlayList;
    }

    public void setNamePlayList(String namePlayList) {
        this.namePlayList = namePlayList;
    }

    public List<String> getSongs() {
        return Songs;
    }

    public void setSongs(List<String> songs) {
        Songs = songs;
    }
}
