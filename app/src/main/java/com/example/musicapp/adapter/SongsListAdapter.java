package com.example.musicapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.musicapp.PlayerActivity;
import com.example.musicapp.R;
<<<<<<< HEAD
import com.example.musicapp.models.SongModel;
import com.example.musicapp.models.FavoriteModel;
=======
import com.example.musicapp.models.CategoryModel;
import com.example.musicapp.models.SongModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
>>>>>>> parent of 33ec5e1 (commit)
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class SongsListAdapter extends RecyclerView.Adapter<SongsListAdapter.MyViewHolder> {
    private Context context;
    private List<String> songsId;

    public SongsListAdapter(Context context, List<String> songsId){
        this.context = context;
        this.songsId = songsId;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.song_list_item_recycler_row, parent, false);
        return new MyViewHolder(view);
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

                        // Handle add button click
                        holder.addToFavoriteButton.setOnClickListener(v -> addToFavorites(songModel));
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(context, "Failed to load song: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    // Method to handle adding the song to favorites
    private void addToFavorites(SongModel songModel) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FavoriteModel favoriteModel = new FavoriteModel(
                songModel.getId(),
                songModel.getSongName(),
                songModel.getSingerName(),
                songModel.getCoverUrl(),
                songModel.getSongUrl()
        );

        db.collection("Favorites")
                .document(favoriteModel.getId())
                .set(favoriteModel)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(context, "Added to favorites: " + songModel.getSongName(), Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(context, "Failed to add to favorites: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    @Override
    public int getItemCount() {
        return songsId.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView coverUrl;
        TextView songName;
        TextView singerName;
        Button addToFavoriteButton;

        public MyViewHolder(View itemView) {
            super(itemView);
            coverUrl = itemView.findViewById(R.id.songslist_coverUrl_image_view);
            songName = itemView.findViewById(R.id.songslist_songName_text_view);
            singerName = itemView.findViewById(R.id.songslist_singerName_text_view);
            addToFavoriteButton = itemView.findViewById(R.id.add_playlist);
        }
    }
}