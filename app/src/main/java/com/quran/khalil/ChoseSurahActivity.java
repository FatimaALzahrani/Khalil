package com.quran.khalil;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.api.gax.rpc.NotFoundException;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ChoseSurahActivity extends AppCompatActivity {

    private String jsonData;
    private int ayahCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chose_surah);

        ListView surahListView = findViewById(R.id.surahListView);

        // قائمة بأسماء السور
        String[] surahNames = {
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


        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, surahNames);
        surahListView.setAdapter(adapter);
        surahListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int surahNumber = position + 1;
                List<String> versesArray = new ArrayList<>();

                jsonData = readRawResource(R.raw.quran_data);
                JSONObject quranData = null;
                try {
                    quranData = new JSONObject(jsonData);
                    if (quranData.has(String.valueOf(surahNumber))) {
                        JSONObject surah = quranData.getJSONObject(String.valueOf(surahNumber));
                        ayahCount = surah.length();

                        for (int i = 1; i <= ayahCount; i++) {
                            versesArray.add(String.valueOf(i));
                        }
                    }

                    AlertDialog.Builder builder = new AlertDialog.Builder(ChoseSurahActivity.this);
                    builder.setTitle("اختر الآية");
                    builder.setItems(versesArray.toArray(new String[0]), (dialog, which) -> {
                        // عند اختيار الآية، قم بتمريرها إلى الصفحة التالية
                        String selectedVerse = versesArray.get(which);
                        Log.d("ChoseSurahActivity", "Selected Surah: " + surahNumber + ", Selected Verse: " + selectedVerse);

                        Intent intent = new Intent(ChoseSurahActivity.this, Recitation.class);
                        intent.putExtra("SURAH_NUMBER", surahNumber);
                        intent.putExtra("Ayah_NUMBER", selectedVerse);
                        startActivity(intent);

                        startActivity(intent);
                    });

                    builder.show();
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
    private String readRawResource(int resourceId) {
        try {
            Resources res = getResources();
            InputStream inputStream = res.openRawResource(resourceId);
            Scanner scanner = new Scanner(inputStream, "UTF-8").useDelimiter("\\A");
            return scanner.hasNext() ? scanner.next() : "";
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }
}
