package com.example.musicapp.models;

import java.util.List;

public class CategoryModel {
    private String nameCat;
    private String coverUrlCat;
    //private List<String> songModels;


    public CategoryModel(){}
    public CategoryModel(String nameCat, String coverUrlCat) {
        this.nameCat = nameCat;
        this.coverUrlCat = coverUrlCat;
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
}
