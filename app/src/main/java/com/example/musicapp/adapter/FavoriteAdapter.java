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
import com.example.musicapp.models.FavoriteModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.MyViewHolder> {
    private Context mContext;
    private List<FavoriteModel> mFavorites;

    public FavoriteAdapter(Context context, List<FavoriteModel> favorites) {
        mContext = context;
        mFavorites = favorites;
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

        // Load image using Glide library
        Glide.with(mContext)
                .load(favorite.getCoverUrl())
                .apply(new RequestOptions().transform(new RoundedCorners(32))) // Rounded corners
                .into(holder.favoriteImage);

        // Handle item click to open PlayerActivity
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, PlayerActivity.class);
                intent.putExtra("FAVORITE", favorite);
                mContext.startActivity(intent);
            }
        });

        // Handle delete button click to remove favorite from Firestore
        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteFavoriteFromFirestore(favorite);
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
        Button deleteButton;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            favoriteImage = itemView.findViewById(R.id.songslist_coverUrl_image_view);
            favoriteName = itemView.findViewById(R.id.songslist_songName_text_view);
            favoriteDescription = itemView.findViewById(R.id.songslist_singerName_text_view);
            deleteButton = itemView.findViewById(R.id.remove_playlist);
        }
    }

    private void deleteFavoriteFromFirestore(FavoriteModel favorite) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Favorites")
                .document(favorite.getId())
                .delete()
                .addOnSuccessListener(aVoid -> {
                    // Handle success: Remove from local list and update UI
                    mFavorites.remove(favorite);
                    notifyDataSetChanged(); // Refresh RecyclerView
                    Toast.makeText(mContext, "Deleted from favorites successfully", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    // Handle failure: Show error message or log the error
                    Toast.makeText(mContext, "Failed to delete from favorites: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}