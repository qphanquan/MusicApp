package com.example.musicapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.musicapp.PlayerActivity;
import com.example.musicapp.R;
import com.example.musicapp.models.SongModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class SectionSongListAdapter extends RecyclerView.Adapter<SectionSongListAdapter.MyViewHolder> {
    private static final String TAG = "SectionSongListAdapter";
    private LayoutInflater mInflater;
    private List<String> songsId;

    public SectionSongListAdapter(Context context, List<String> songsId){
        this.mInflater = LayoutInflater.from(context);
        this.songsId = songsId;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.section_song_list_recycler_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        int pos = position;
        FirebaseFirestore.getInstance().collection("Song")
                .document(this.songsId.get(pos))
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        SongModel songModel = documentSnapshot.toObject(SongModel.class);
                        if(songModel != null){
                            holder.songName.setText(songModel.getSongName());
                            holder.singerName.setText(songModel.getSingerName());
                            Glide.with(holder.songcoverUrl).load(songModel.getCoverUrl())
                                    .apply(
                                            new RequestOptions().transform(new RoundedCorners(32)) // Bo goc
                                    ).into(holder.songcoverUrl);

                            holder.itemView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(v.getContext(), PlayerActivity.class);
                                    intent.putExtra("SONG", songModel);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    v.getContext().startActivity(intent);
                                }
                            });
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
        private ImageView songcoverUrl;
        private TextView songName;
        private TextView singerName;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            this.songcoverUrl = itemView.findViewById(R.id.section_songcoverUrl_image_view);
            this.songName = itemView.findViewById(R.id.section_songName_text_view);
            this.singerName = itemView.findViewById(R.id.section_singerName_text_view);
        }
    }
}
