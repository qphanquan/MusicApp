package com.example.musicapp.models;

import java.io.Serializable;
import java.util.List;

public class CategoryModel implements Serializable {
    private String nameCat;
    private String coverUrlCat;
    private List<String> songs;

    public CategoryModel(){}

    public CategoryModel(String nameCat, String coverUrlCat, List<String> songs) {
        this.nameCat = nameCat;
        this.coverUrlCat = coverUrlCat;
        this.songs = songs;
    }

    public String getNameCat() {
        return nameCat;
    }

    public void setNameCat(String nameCat) {
        this.nameCat = nameCat;
    }

    public String getCoverUrlCat() {
        return coverUrlCat;
    }

    public void setCoverUrlCat(String coverUrlCat) {
        this.coverUrlCat = coverUrlCat;
    }

    public List<String> getSongs() {
        return songs;
    }

    public void setSongs(List<String> songs) {
        this.songs = songs;
    }
}
