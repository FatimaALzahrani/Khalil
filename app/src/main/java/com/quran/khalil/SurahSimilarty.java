package com.quran.khalil;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SurahSimilarty extends AppCompatActivity implements VerseAdapter.OnItemClickListener {

    private RecyclerView recyclerView;
    private VerseAdapter verseAdapter;
    private List<String> verseList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_surah_similarty);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        verseList = new ArrayList<>();
        verseAdapter = new VerseAdapter(verseList,this);

        recyclerView.setAdapter(verseAdapter);

        // استرجاع البيانات من Firebase
        String selectedSurah = getIntent().getStringExtra("SurahName");
        int surahNumber = getIntent().getIntExtra("SurahNumber", 2);
        loadVerseData(selectedSurah, surahNumber);
    }

    private void loadVerseData(String selectedSurah, int surahNumber) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference()
                .child("Similarty").child("1").child(String.valueOf(surahNumber));

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot verseSnapshot : dataSnapshot.getChildren()) {
                    String verse = verseSnapshot.getKey();
                    if (verse != null) {
                        verseList.add(verse);
                    }
                }

                verseAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(SurahSimilarty.this, "Failed to load data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onItemClick(int position) {
        String selectedverse = verseList.get(position);
        Intent intent=new Intent(this,AyahSimilarty.class);
        intent.putExtra("selectedverse", selectedverse);
//        intent.getExtra("AyahName", verseList);
        intent.putExtra("position", position);
        startActivity(intent);
    }
}
