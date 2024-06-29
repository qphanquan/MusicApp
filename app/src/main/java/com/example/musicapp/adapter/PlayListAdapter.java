package com.example.musicapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.musicapp.CacheExoPlayer;
import com.example.musicapp.PlayerActivity;
import com.example.musicapp.R;
import com.example.musicapp.models.PlayListModel;
import com.example.musicapp.models.SongModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class PlayListAdapter extends RecyclerView.Adapter<PlayListAdapter.MyViewHolder> {
    private Context context;
    private List<String> songsId;
    private String playlistName;

    public PlayListAdapter(Context context, List<String> songsId, String playlistName){
        this.context = context;
        this.songsId = songsId;
        this.playlistName = playlistName;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.playlist_item_recycler_row, parent, false);
        return new PlayListAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        String songId = songsId.get(position);

        // Load song details from Firestore
        FirebaseFirestore.getInstance().collection("Song")
                .document(songId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    SongModel songModel = documentSnapshot.toObject(SongModel.class);
                    if (songModel != null) {
                        holder.songName.setText(songModel.getSongName());
                        holder.singerName.setText(songModel.getSingerName());
                        Glide.with(context)
                                .load(songModel.getCoverUrl())
                                .apply(new RequestOptions().transform(new RoundedCorners(32)))
                                .into(holder.coverUrl);

                        holder.addPlayList.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                getPlayList(playlistName, songId);
                            }
                        });

                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(context, "Failed to load song: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    public void getPlayList(String playlistName, String songId) {
        FirebaseFirestore.getInstance().collection("PlayList")
                .document("Hello")
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    //PlayListModel playListModel = documentSnapshot.toObject(PlayListModel.class);
                    //createSongByList(playListModel, songId);
                    Log.e("GETPLAYLISTADAPTER", songId);
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(context, "Failed to load song: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    public void createSongByList(PlayListModel playListModel, String songId){
        List<String> songs = playListModel.getSongs();
        songs.add(songId);
        playListModel.setSongs(songs);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("PlayList")
                .document(playlistName)
                .set(playListModel)
                .addOnSuccessListener(aVoid -> {
                    Log.e("CREATEPLAYLISTADAPTER", songId);
                    Toast.makeText(context, "Added to playlist: " + songId, Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(context, "Failed to add to playlist: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }


    @Override
    public int getItemCount() {
        return songsId.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView coverUrl;
        TextView songName;
        TextView singerName;
        ImageButton addPlayList;

        public MyViewHolder(View itemView) {
            super(itemView);
            coverUrl = itemView.findViewById(R.id.playlist_coverUrl_image_view);
            songName = itemView.findViewById(R.id.playlist_songName_text_view);
            singerName = itemView.findViewById(R.id.playlist_singerName_text_view);
            addPlayList = itemView.findViewById(R.id.playlist_iconadd);
        }
    }
}
