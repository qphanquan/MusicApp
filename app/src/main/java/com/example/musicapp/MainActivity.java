package com.example.musicapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
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

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.musicapp.adapter.CategoryAdapter;
import com.example.musicapp.adapter.SectionSongListAdapter;
import com.example.musicapp.models.CategoryModel;
import com.example.musicapp.models.SongModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private CategoryAdapter categoryAdapter;
    private SectionSongListAdapter sectionSongListAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        this.Init();
    }

    public void Init(){
        this.getCategories();

        this.SetUpSection("Trending", R.id.section_1_recycler_view, R.id.section_1_title, this.findViewById(R.id.section_1_main_layout));
        this.SetUpSection("Lofi Chill", R.id.section_2_recycler_view, R.id.section_2_title, this.findViewById(R.id.section_2_main_layout));
    }

    public void getCategories(){
        FirebaseFirestore.getInstance().collection("Category")
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if(queryDocumentSnapshots.isEmpty()){
                            Log.d(TAG, "onSuccess: LIST EMPTY");
                            return;
                        }
                        else
                        {
                            List<CategoryModel> categoryModelList = queryDocumentSnapshots.toObjects(CategoryModel.class);
                            SetUpCategory(categoryModelList);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Error getting data!!!", Toast.LENGTH_LONG).show();
                    }
                });
    }

    public void SetUpCategory(List<CategoryModel> categories){
        this.categoryAdapter = new CategoryAdapter(this, categories);
        RecyclerView recyclerView =this.findViewById(R.id.categories_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(categoryAdapter);
    }

    public void SetUpSection(String documentId, int idLayout, int idTitle, RelativeLayout relativeLayout){
        RecyclerView recyclerView = this.findViewById(idLayout);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        FirebaseFirestore.getInstance().collection("Sections")
                .document(documentId).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        CategoryModel section = documentSnapshot.toObject(CategoryModel.class);
                        TextView title = findViewById(idTitle);
                        title.setText(section.getNameCat());
                        sectionSongListAdapter  = new SectionSongListAdapter(getApplicationContext(), section.getSongs());
                        recyclerView.setAdapter(sectionSongListAdapter);
                        relativeLayout.setVisibility(View.VISIBLE);
                        //Log.e(TAG, documentSnapshot.getData().toString());
                    }
                });
    }
}