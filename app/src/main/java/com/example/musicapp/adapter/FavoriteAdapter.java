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
import com.example.musicapp.CacheExoPlayer;
import com.example.musicapp.PlayerActivity;
import com.example.musicapp.R;
import com.example.musicapp.models.FavoriteModel;
import com.example.musicapp.models.SongModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.MyViewHolder> {
    private Context mContext;
    private List<FavoriteModel> mFavorites;
    private FirebaseFirestore db;
    private String userId;

    public FavoriteAdapter(Context context, List<FavoriteModel> favorites) {
        mContext = context;
        mFavorites = favorites;
        db = FirebaseFirestore.getInstance();
        this.userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.favorite_song_list_item_recycler_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        FavoriteModel favorite = mFavorites.get(position);

        holder.favoriteName.setText(favorite.getSongName());
        holder.favoriteDescription.setText(favorite.getSingerName());

        Glide.with(mContext)
                .load(favorite.getCoverUrl())
                .apply(new RequestOptions().transform(new RoundedCorners(32))) // Rounded corners
                .into(holder.favoriteImage);

        FirebaseFirestore.getInstance().collection("Users").document(this.userId).collection("Favorites")
                .document(favorite.getId())
                .get()
                .addOnSuccessListener(favoriteSnapshot -> {
                    if (favoriteSnapshot.exists()) {
                        getSongById(favorite.getId(), holder.itemView);
                    }
                });

        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteFavoriteFromFirestore(favorite);
            }
        });
    }

    public void getSongById(String id, View item){
        // Load song details from Firestore
        FirebaseFirestore.getInstance().collection("Song")
                .document(id)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        SongModel songModel = documentSnapshot.toObject(SongModel.class);
                        item.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                CacheExoPlayer.getInstance().startPlaying(mContext, songModel);
                                Intent intent = new Intent(mContext, PlayerActivity.class);
                                mContext.startActivity(intent);
                            }
                        });
                    }
                });

    }

    @Override
    public int getItemCount() {
        return mFavorites.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView favoriteImage;
        TextView favoriteName;
        TextView favoriteDescription;
        ImageButton deleteButton;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            favoriteImage = itemView.findViewById(R.id.songslist_coverUrl_image_view);
            favoriteName = itemView.findViewById(R.id.songslist_songName_text_view);
            favoriteDescription = itemView.findViewById(R.id.songslist_singerName_text_view);
            deleteButton = itemView.findViewById(R.id.remove_playlist);
        }
    }

    private void deleteFavoriteFromFirestore(FavoriteModel favorite) {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Users").document(userId).collection("Favorites")
                .document(favorite.getId())
                .delete()
                .addOnSuccessListener(aVoid -> {
                    mFavorites.remove(favorite);
                    notifyDataSetChanged();
                    Toast.makeText(mContext, "Deleted from favorites successfully", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(mContext, "Failed to delete from favorites: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}