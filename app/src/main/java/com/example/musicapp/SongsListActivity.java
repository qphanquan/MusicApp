package com.example.musicapp;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.musicapp.adapter.SongsListAdapter;
import com.example.musicapp.models.CategoryModel;

public class SongsListActivity extends AppCompatActivity {

    private SongsListAdapter songsListAdapter;
    private CategoryModel categoryModel;
    private ImageView coverUrlCat;
    private TextView nameCat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_songs_list);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        this.Init();
    }

    public void Init(){
        this.categoryModel = (CategoryModel) getIntent().getSerializableExtra("CATEGORY");
        this.coverUrlCat = this.findViewById(R.id.songslist_coverCat_image_view);
        this.nameCat = this.findViewById(R.id.songslist_nameCat_text_view);

        Glide.with(this.coverUrlCat).load(this.categoryModel.getCoverUrlCat())
                .apply(
                        new RequestOptions().transform(new RoundedCorners(32)) // Bo goc
                ).into(this.coverUrlCat);
        this.nameCat.setText(this.categoryModel.getNameCat());
        SetupSongsList();
    }

    public void SetupSongsList(){
        this.songsListAdapter = new SongsListAdapter(this, this.categoryModel.getSongs());
        RecyclerView recyclerView = this.findViewById(R.id.songslist_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(this.songsListAdapter);
    }
}