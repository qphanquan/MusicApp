package com.example.musicapp.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.musicapp.R;
import com.example.musicapp.models.CategoryModel;
import com.example.musicapp.models.SongModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class SongsListAdapter extends RecyclerView.Adapter<SongsListAdapter.MyViewHolder> {
    private static final String TAG = "SongsListAdapter";
    private LayoutInflater mInflater;
    private List<String> songsId;

    public SongsListAdapter(Context context, List<String> songsId){
        this.mInflater = LayoutInflater.from(context);
        this.songsId = songsId;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.song_list_item_recycler_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        int pos = position;
        FirebaseFirestore.getInstance().collection("Song")
                .document(this.songsId.get(pos)).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        SongModel songModel = documentSnapshot.toObject(SongModel.class);
                        if(songModel != null){
                            holder.songName.setText(songModel.getSongName());
                            holder.singerName.setText(songModel.getSingerName());
                            Glide.with(holder.coverUrl).load(songModel.getCoverUrl())
                                    .apply(
                                            new RequestOptions().transform(new RoundedCorners(32)) // Bo goc
                                    ).into(holder.coverUrl);
                        }
                        Log.e(TAG, documentSnapshot.getData().toString());
                    }
                });
    }

    @Override
    public int getItemCount() {
        return this.songsId.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView coverUrl;
        private TextView songName;
        private TextView singerName;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            coverUrl = itemView.findViewById(R.id.songslist_coverUrl_image_view);
            songName = itemView.findViewById(R.id.songslist_songName_text_view);
            singerName = itemView.findViewById(R.id.songslist_singerName_text_view);
        }
    }
}