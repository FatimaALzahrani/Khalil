package com.quran.khalil;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SimilartyAyah extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private TextView count;
    int i = 0;
    private String SurahNum,AyahNum,AyahText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_similarty_ayah);

        recyclerView=findViewById(R.id.recyclerView);
        progressBar=findViewById(R.id.progressBar);
        count = findViewById(R.id.count);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        List<SimilarityItem> similarityList = new ArrayList<>();
        List<SimilarityItem> similarityList2 = new ArrayList<>();
        SimilarityAdapter adapter = new SimilarityAdapter(similarityList);
        recyclerView.setAdapter(adapter);
        Intent intent=getIntent();
        SurahNum = intent.getStringExtra("SurahNum");
        AyahNum = intent.getStringExtra("AyahNum");
        AyahText = intent.getStringExtra("AyahText");

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference similarityRef = database.getReference("results");

        similarityRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // تحقق من وجود بيانات
                if (dataSnapshot.exists()) {
                    for (DataSnapshot partSnapshot : dataSnapshot.getChildren()) {
                        for (DataSnapshot surahSnapshot : partSnapshot.getChildren()) {
                            String surahNum = surahSnapshot.getKey();
                            if (Integer.parseInt(surahNum) ==Integer.parseInt(SurahNum) ) {
                                for (DataSnapshot ayahSnapshot : surahSnapshot.getChildren()) {
//                                    String SurahNum = ayahSnapshot.child("SurahNum").getValue(String.class);
//                                    String ayahNum = ayahSnapshot.child("AyahNum").getValue(String.class);
//                                    String ayahText = ayahSnapshot.child("Ayah").getValue(String.class);
                                    for (DataSnapshot aimilartySnapshot : ayahSnapshot.getChildren()) {
                                        String SurahNum2 = aimilartySnapshot.child("SurahNum").getValue(String.class);
                                        String ayahNum2 = ayahSnapshot.child("AyahNum").getValue(String.class);
                                        String ayahText2 = aimilartySnapshot.child("Ayah").getValue(String.class);
                                        String similarly = aimilartySnapshot.child("Similarly").getValue(String.class);
                                        i++;
                                        // إضافة البيانات إلى قائمة الـ RecyclerView
                                        similarityList.add(new SimilarityItem(SurahNum2, ayahNum2, ayahText2, similarly));
                                        adapter.notifyDataSetChanged();
                                        progressBar.setVisibility(View.INVISIBLE);
                                        count.setText("تم العثور على " + i + " آية !");

                                    }
                                }
                            }
                        }
                    }
                } else {
                    Log.d("SimilarityData", "لا توجد بيانات في جدول Similarity");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // يمكنك إضافة معالج لحالة الخطأ إذا لزم الأمر
            }
        });


    }
}