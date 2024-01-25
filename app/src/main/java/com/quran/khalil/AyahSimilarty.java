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
import java.util.Map;
import java.util.Set;

public class AyahSimilarty extends AppCompatActivity implements AyahAdapter.OnItemClickListener {

    private RecyclerView recyclerView;
    private AyahAdapter ayahAdapter;
    private List<Surah> AyahList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ayah_similarty);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        AyahList = new ArrayList<>();
        ayahAdapter = new AyahAdapter(AyahList,this);

        recyclerView.setAdapter(ayahAdapter);

        // استرجاع البيانات من Firebase
        String selectedSurah = getIntent().getStringExtra("selectedverse");
        int surahNumber = getIntent().getIntExtra("SurahNumber", 2);
        loadVerseData(selectedSurah, surahNumber);
    }

    private void loadVerseData(String selectedverse, int surahNumber) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference()
                .child("Similarty").child("1").child(String.valueOf(surahNumber)).child(selectedverse);

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ayahSnapshot : dataSnapshot.getChildren()) {
                    String selectedSurah = ayahSnapshot.getKey();
                    String verse = ayahSnapshot.getValue().toString();
                    Object ayahValue = ayahSnapshot.getValue();
                    if (ayahValue instanceof Map) {
                        for (Object verseObject : ((Map<String, Object>) ayahValue).values()) {
                            verse = verseObject.toString();
                        }
                    }
                    Surah ayah = new Surah(verse, selectedSurah);
                    AyahList.add(ayah);


                }

                ayahAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(AyahSimilarty.this, "Failed to load data", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void onItemClick(int position) {

    }
}
