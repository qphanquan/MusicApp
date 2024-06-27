package com.example.musicapp;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicapp.adapter.FavoriteAdapter;
import com.example.musicapp.models.FavoriteModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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
