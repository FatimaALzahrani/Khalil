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

public class SimlartyPage extends AppCompatActivity {
    RecyclerView recyclerView;
    private String from="1",to="30";
    int i = 0;
    TextView count;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simlarty_page);
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
        from = intent.getStringExtra("From");
        to = intent.getStringExtra("To");
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
                            if (Integer.parseInt(surahNum) >= Integer.parseInt(from) && Integer.parseInt(surahNum) <= Integer.parseInt(to)) {
                                for (DataSnapshot ayahSnapshot : surahSnapshot.getChildren()) {
                                    String SurahNum = ayahSnapshot.child("SurahNum").getValue(String.class);
                                    String ayahNum = ayahSnapshot.child("AyahNum").getValue(String.class);
                                    String ayahText = ayahSnapshot.child("Ayah").getValue(String.class);
                                    similarityList.add(new SimilarityItem(SurahNum, ayahNum, ayahText));
                                    count.setText("تم العثور على " + i + " آية متشابهة!");
                                    i++;
                                    adapter.notifyDataSetChanged();
                                    progressBar.setVisibility(View.INVISIBLE);
//                                    for (DataSnapshot aimilartySnapshot : ayahSnapshot.getChildren()) {
//                                        i++;
//                                        String SurahNum2 = aimilartySnapshot.child("SurahNum").getValue(String.class);
//                                        String ayahNum2 = ayahSnapshot.child("AyahNum").getValue(String.class);
//                                        String ayahText2 = aimilartySnapshot.child("Ayah").getValue(String.class);
//                                        String similarly = aimilartySnapshot.child("Similarly").getValue(String.class);
//                                        // إضافة البيانات إلى قائمة الـ RecyclerView
//                                        similarityList.add(new SimilarityItem(SurahNum2, ayahNum2, ayahText2, similarly));
//                                        adapter.notifyDataSetChanged();
//                                        progressBar.setVisibility(View.INVISIBLE);
//                                        count.setText("تم العثور على " + i + " آية متشابهة!");
//
//                                    }
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



        adapter.setOnItemClickListener(new SimilarityAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                // Handle item click here
                SimilarityItem selectedItem = similarityList.get(position);
                String[] surahNames = new String[]{
                        "الفاتحة", "البقرة", "آل عمران", "النساء", "المائدة", "الأنعام", "الأعراف", "الأنفال", "التوبة", "يونس",
                        "هود", "يوسف", "الرعد", "ابراهيم", "الحجر", "النحل", "الإسراء", "الكهف", "مريم", "طه",
                        "الأنبياء", "الحج", "المؤمنون", "النور", "الفرقان", "الشعراء", "النمل", "القصص", "العنكبوت", "الروم",
                        "لقمان", "السجدة", "الأحزاب", "سبإ", "فاطر", "يس", "الصافات", "ص", "الزمر", "غافر",
                        "فصلت", "الشورى", "الزخرف", "الدخان", "الجاثية", "الأحقاف", "محمد", "الفتح", "الحجرات", "ق",
                        "الذاريات", "الطور", "النجم", "القمر", "الرحمن", "الواقعة", "الحديد", "المجادلة", "الحشر", "الممتحنة",
                        "الصف", "الجمعة", "المنافقون", "التغابن", "الطلاق", "التحريم", "الملك", "القلم", "الحاقة", "المعارج",
                        "نوح", "الجن", "المزمل", "المدثر", "القيامة", "الإنسان", "المرسلات", "النبإ", "النازعات", "عبس",
                        "التكوير", "الإنفطار", "المطففين", "الإنشقاق", "البروج", "الطارق", "الأعلى", "الغاشية", "الفجر", "البلد",
                        "الشمس", "الليل", "الضحى", "الشرح", "التين", "العلق", "القدر", "البينة", "الزلزلة", "العاديات",
                        "القارعة", "التكاثر", "العصر", "الهمزة", "الفيل", "قريش", "الماعون", "الكوثر", "الكافرون", "النصر",
                        "المسد", "الإخلاص", "الفلق", "الناس"
                };
                int index=1;
                // Create an Intent to start the "sim" activity
                Intent intent = new Intent(SimlartyPage.this, SimilartyAyah.class);
                for (int i = 0; i < surahNames.length; i++) {
                    if (surahNames[i].equals(selectedItem.getSurahNum())) {
                        index = i;
                        break;
                    }
                }
                // Put extra data in the Intent
                intent.putExtra("SurahNum", String.valueOf(index));
                intent.putExtra("AyahNum", selectedItem.getAyahNum());
                intent.putExtra("AyahText", selectedItem.getAyahText());

                // Start the new activity
                startActivity(intent);
            }
        });


    }
}