package com.example.musicapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.musicapp.adapter.CategoryAdapter;
import com.example.musicapp.adapter.SectionSongListAdapter;
import com.example.musicapp.adapter.SongsListAdapter;
import com.example.musicapp.models.CategoryModel;
import com.example.musicapp.models.FavoriteModel;
import com.example.musicapp.models.SongModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;


import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private CategoryAdapter categoryAdapter;
    private SectionSongListAdapter sectionSongListAdapter;
    private SongsListAdapter songsListAdapter;
    ImageView show;
    ImageView playlistAdd;
    EditText playlistName;
    SearchView searchViewSongs;
    List<String> idSongNames;
    List<SongModel> songModels;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.navigation_home) {
                    // Handle the "Home" navigation item
                    Intent homeIntent = new Intent(MainActivity.this, MainActivity.class);
                    startActivity(homeIntent);
                    finish();
                    return true;
                } else if (id == R.id.navigation_playlist) {
                    // Handle the "Playlist" navigation item
                    //Intent playlistIntent = new Intent(MainActivity.this, FavoriteActivity.class);
                    Intent playlistIntent = new Intent(MainActivity.this, PlaylistActivity.class);
                    startActivity(playlistIntent);
                    return true;
                } else if (id == R.id.navigation_output) {
                    FirebaseAuth.getInstance().signOut();
                    Intent playlistIntent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(playlistIntent);
                    return true;
                }
                return false;
            }
        });
        Init();
    }
//    private void replaceFragment(Fragment fragment){
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        fragmentTransaction.commit();
//    }

    @Override
    protected void onResume() {
        super.onResume();
        ShowPlayerView();
    }

    public void Init(){
        this.idSongNames = new ArrayList<>();
        this.getCategories();
        this.getSongs();

        this.SetUpSection("Trending", R.id.section_1_recycler_view, R.id.section_1_title, this.findViewById(R.id.section_1_main_layout));
        this.SetUpSection("Lofi Chill", R.id.section_2_recycler_view, R.id.section_2_title, this.findViewById(R.id.section_2_main_layout));

        this.searchViewSongs = this.findViewById(R.id.searchSongs);
        this.searchViewSongs.clearFocus();
        this.searchViewSongs.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                SearchSongs(newText);
                return true;
            }
        });

        this.songsListAdapter = new SongsListAdapter(this, this.idSongNames);
        RecyclerView recyclerView = this.findViewById(R.id.searchSongs_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(this.songsListAdapter);

        this.playlistAdd = this.findViewById(R.id.main_playlistAdd);
        this.playlistName = this.findViewById(R.id.main_playlistName);

        this.playlistAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), PlaylistActivity.class);
                intent.putExtra("PLAYLISTNAME", playlistName.getText().toString());
                v.getContext().startActivity(intent);
            }
        });
    }

    public void SearchSongs(String txtSearch){
        this.idSongNames = new ArrayList<>();
        if(!txtSearch.isEmpty()){
            for(SongModel song : this.songModels){
                if(song.getSongName().toLowerCase().contains(txtSearch.toLowerCase())){
                    this.idSongNames.add(song.getId());
                }
            }
        }
        this.songsListAdapter.SetSongsId(this.idSongNames);
    }

    public void getSongs(){
        FirebaseFirestore.getInstance().collection("Song")
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if(queryDocumentSnapshots.isEmpty()){
                            Log.d(TAG, "onSuccess: LIST EMPTY");
                            return;
                        }
                        else
                        {
                            List<SongModel> songModelList = queryDocumentSnapshots.toObjects(SongModel.class);
                            songModels = songModelList;
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Error getting data!!!", Toast.LENGTH_LONG).show();
                    }
                });
    }

    public void getCategories(){
        FirebaseFirestore.getInstance().collection("Category")
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if(queryDocumentSnapshots.isEmpty()){
                            Log.d(TAG, "onSuccess: LIST EMPTY");
                            return;
                        }
                        else
                        {
                            List<CategoryModel> categoryModelList = queryDocumentSnapshots.toObjects(CategoryModel.class);
                            SetUpCategory(categoryModelList);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Error getting data!!!", Toast.LENGTH_LONG).show();
                    }
                });
    }

    public void SetUpCategory(List<CategoryModel> categories){
        this.categoryAdapter = new CategoryAdapter(this, categories);
        RecyclerView recyclerView =this.findViewById(R.id.categories_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(categoryAdapter);
    }

    public void SetUpSection(String documentId, int idLayout, int idTitle, RelativeLayout relativeLayout){
        RecyclerView recyclerView = this.findViewById(idLayout);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        FirebaseFirestore.getInstance().collection("Sections")
                .document(documentId).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        CategoryModel section = documentSnapshot.toObject(CategoryModel.class);
                        TextView title = findViewById(idTitle);
                        title.setText(section.getNameCat());
                        sectionSongListAdapter  = new SectionSongListAdapter(getApplicationContext(), section.getSongs());
                        recyclerView.setAdapter(sectionSongListAdapter);
                        relativeLayout.setVisibility(View.VISIBLE);
                        //Log.e(TAG, documentSnapshot.getData().toString());
                    }
                });
    }

    public void ShowPlayerView(){
        FavoriteModel favorite = new FavoriteModel();
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        RelativeLayout relativeLayout = this.findViewById(R.id.cachePlayer_view);
        ImageView coverUrl = this.findViewById(R.id.main_songcoverUrl_image_view);
        TextView songName = this.findViewById(R.id.main_songName_text_view);
        TextView singerName = this.findViewById(R.id.main_singerName_text_view);
        ImageButton addPlaylist = this.findViewById(R.id.main_add_playlist);
        SongModel currentSong = CacheExoPlayer.getInstance().getCurrentSong();
        if(currentSong != null) {
            relativeLayout.setVisibility(View.VISIBLE);
            songName.setText(currentSong.getSongName());
            singerName.setText(currentSong.getSingerName());
            Glide.with(coverUrl).load(currentSong.getCoverUrl()).circleCrop().into(coverUrl);

            FirebaseFirestore.getInstance().collection("Users").document(userId).collection("Favorites")
                    .document(currentSong.getId())
                    .get()
                    .addOnSuccessListener(favoriteSnapshot -> {
                        if (favoriteSnapshot.exists()) {
                            addPlaylist.setImageResource(R.drawable.ic_red_favorite_24);
                        } else {
                            addPlaylist.setImageResource(R.drawable.ic_shadow_favorite_24);
                        }
                    });

            relativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), PlayerActivity.class);
                    v.getContext().startActivity(intent);
                }
            });
        }
        else
            relativeLayout.setVisibility(View.GONE);
    }
}