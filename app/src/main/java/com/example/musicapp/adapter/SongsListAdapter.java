package com.example.musicapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
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
import com.example.musicapp.models.SongModel;
import com.example.musicapp.models.FavoriteModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class SongsListAdapter extends RecyclerView.Adapter<SongsListAdapter.MyViewHolder> {
    private Context context;
    private List<String> songsId;

    public SongsListAdapter(Context context, List<String> songsId){
        this.context = context;
        this.songsId = songsId;
    }

    public void SetSongsId(List<String> idSongs){
        this.songsId = idSongs;
        notifyDataSetChanged();
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

                        // Check if the song is already in favorites
                        FirebaseFirestore.getInstance().collection("Favorites")
                                .document(songModel.getId())
                                .get()
                                .addOnSuccessListener(favoriteSnapshot -> {
                                    if (favoriteSnapshot.exists()) {
                                        holder.addToFavoriteButton.setImageResource(R.drawable.ic_red_favorite_24);
                                    } else {
                                        holder.addToFavoriteButton.setImageResource(R.drawable.ic_shadow_favorite_24);
                                    }
                                });

                        // Handle add button click
                        holder.addToFavoriteButton.setOnClickListener(v -> addToFavorites(songModel, holder.addToFavoriteButton));
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(context, "Failed to load song: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    // Method to handle adding the song to favorites
    private void addToFavorites(SongModel songModel, ImageButton addToFavoriteButton) {
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
                    addToFavoriteButton.setImageResource(R.drawable.ic_red_favorite_24);
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
        ImageButton addToFavoriteButton;

        public MyViewHolder(View itemView) {
            super(itemView);
            coverUrl = itemView.findViewById(R.id.songslist_coverUrl_image_view);
            songName = itemView.findViewById(R.id.songslist_songName_text_view);
            singerName = itemView.findViewById(R.id.songslist_singerName_text_view);
            addToFavoriteButton = itemView.findViewById(R.id.add_playlist);
        }
    }
}