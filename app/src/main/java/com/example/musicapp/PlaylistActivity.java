package com.example.musicapp;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicapp.adapter.PlayListAdapter;
import com.example.musicapp.adapter.SongsListAdapter;
import com.example.musicapp.models.SongModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class PlaylistActivity extends AppCompatActivity {
    private static final String TAG = "PlaylistActivity";

    TextView textView;

    PlayListAdapter playListAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_playlist);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Init();
    }

    public void Init(){
        textView = this.findViewById(R.id.playlist_title);

        this.textView.setText(getIntent().getStringExtra("PLAYLISTNAME"));
        getSongs(getIntent().getStringExtra("PLAYLISTNAME"));
    }

    public void getSongs(String playListName){
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
                            SetUpSongPlayList(songModelList, playListName);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Error getting data!!!", Toast.LENGTH_LONG).show();
                    }
                });
    }

    public void SetUpSongPlayList(List<SongModel> songLists, String playlistName){
        List<String> songIds = new ArrayList<>();
        for (SongModel song : songLists){
            songIds.add(song.getId());
        }
        this.playListAdapter = new PlayListAdapter(this, songIds, playlistName);
        RecyclerView recyclerView = this.findViewById(R.id.playlist_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(this.playListAdapter);
    }
}