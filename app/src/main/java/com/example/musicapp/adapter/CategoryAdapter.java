package com.example.musicapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.RoundedCorner;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.musicapp.R;
import com.example.musicapp.SongsListActivity;
import com.example.musicapp.models.CategoryModel;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.MyViewHolder>{
    private LayoutInflater mInflater;
    private List<CategoryModel> categories;
    private Context context;

    public CategoryAdapter(Context context, List<CategoryModel> categoryList){
        this.mInflater = LayoutInflater.from(context);
        this.categories = categoryList;
        this.context = context;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.category_item_recycler_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        int pos = position;
        CategoryModel category = this.categories.get(pos);
        holder.txtV.setText(category.getNameCat());
        Glide.with(holder.imgV).load(category.getCoverUrlCat())
                .apply(
                        new RequestOptions().transform(new RoundedCorners(32)) // Bo goc
                ).into(holder.imgV);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), SongsListActivity.class);
                intent.putExtra("CATEGORY", category);
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.categories.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        private ImageView imgV;
        private TextView txtV;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imgV = itemView.findViewById(R.id.coverUrlCat_image_view);
            txtV = itemView.findViewById(R.id.nameCat_text_view);
        }
    }
}
