package com.example.musicapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicapp.adapter.FavoriteAdapter;
import com.example.musicapp.models.FavoriteModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class FavoriteActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FavoriteAdapter favoriteAdapter;
    private List<FavoriteModel> favoriteList;
    private TextView emptyMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_list);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.navigation_playlist);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.navigation_home) {
                    // Handle the "Home" navigation item
                    Intent homeIntent = new Intent(FavoriteActivity.this, MainActivity.class);
                    startActivity(homeIntent);
                    finish();
                    return true;
                } else if (id == R.id.navigation_playlist) {
                    // Handle the "Playlist" navigation item
                    Intent playlistIntent = new Intent(FavoriteActivity.this, FavoriteActivity.class);
                    startActivity(playlistIntent);
                    return true;
                }
                return false;
            }
        });

        recyclerView = findViewById(R.id.recycler_view);
        emptyMessage = findViewById(R.id.empty_message);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        favoriteList = new ArrayList<>();
        favoriteAdapter = new FavoriteAdapter(this, favoriteList);
        recyclerView.setAdapter(favoriteAdapter);

        loadFavorites();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadFavorites();
    }

    private void loadFavorites() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Favorites")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            favoriteList.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                FavoriteModel favorite = document.toObject(FavoriteModel.class);
                                favorite.setId(document.getId());
                                favoriteList.add(favorite);
                            }
                            favoriteAdapter.notifyDataSetChanged();
                            updateEmptyView();
                        } else {

                            // Log.e(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    private void updateEmptyView() {
        if (favoriteList.isEmpty()) {
            emptyMessage.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            emptyMessage.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }
}
