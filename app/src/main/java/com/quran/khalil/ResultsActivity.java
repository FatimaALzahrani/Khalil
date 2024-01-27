package com.quran.khalil;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class ResultsActivity extends AppCompatActivity {

    private ListView resultsListView;
    private ArrayList<String> resultsList;
    private ArrayAdapter<String> resultsAdapter;
    private ProgressBar progressBar;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        databaseReference = FirebaseDatabase.getInstance().getReference("results").child("22");
        resultsListView = findViewById(R.id.resultsListView);
        resultsList = new ArrayList<>();
        resultsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, resultsList);
        resultsListView.setAdapter(resultsAdapter);

        // قراءة ملف JSON وعرض النتائج
        displayResults();
        progressBar.setVisibility(View.GONE);

    }

    private void displayResults() {
        try {
            // قراءة ملف JSON من المجلد raw
            InputStream is = getResources().openRawResource(R.raw.quran_data);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();

            String json = new String(buffer, "UTF-8");
            JSONObject quranData = new JSONObject(json);

            for (Iterator<String> surahKeys = quranData.keys(); surahKeys.hasNext(); ) {
                String surahKey = surahKeys.next();
                JSONObject surah = quranData.getJSONObject(surahKey);
                if (Integer.parseInt(surahKey)>=34 && Integer.parseInt(surahKey)<=35) {
                    databaseReference = databaseReference.child(surahKey);
                    for (Iterator<String> verseKeys = surah.keys(); verseKeys.hasNext(); ) {
                        String verseKey = verseKeys.next();
                        JSONObject verse = surah.getJSONObject(verseKey);
                        String text = verse.getString("text");
                        String displayText = verse.getString("displayText");
                        if (Integer.parseInt(verseKey)>0) {
                            // مقارنة الآية بالآيات الأخرى
                            compareVerses(quranData, surahKey, verseKey, text,displayText);
                        }
                    }
                }
            }
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    private void compareVerses(JSONObject quranData, String surahKey, String verseKey, String text,String displayText) throws JSONException {
        String key=databaseReference.push().getKey();
        for (Iterator<String> otherSurahKeys = quranData.keys(); otherSurahKeys.hasNext(); ) {
            String otherSurahKey = otherSurahKeys.next();
            if (!otherSurahKey.equals(surahKey)) {
                JSONObject otherSurah = quranData.getJSONObject(otherSurahKey);
                for (Iterator<String> otherVerseKeys = otherSurah.keys(); otherVerseKeys.hasNext(); ) {
                    String otherVerseKey = otherVerseKeys.next();
                    JSONObject otherVerse = otherSurah.getJSONObject(otherVerseKey);
                    String otherText = otherVerse.getString("text");
                    String otherdisplayText = otherVerse.getString("displayText");

                    double similarity = calculateSimilarity(text, otherText);
                    if((similarity * 100) >=50) {
                        String key2=databaseReference.child(key).push().getKey();
                        // إضافة النتيجة إلى قاعدة البيانات هنا
                        databaseReference.child(verseKey).child(key2).setValue("آية " + surahKey + ":" + verseKey +
                                " - آية " + otherSurahKey + ":" + otherVerseKey +
                                " - نسبة التشابه: " + (int) (similarity * 100) + "%");
                        databaseReference.child(verseKey).child("SurahNum").setValue(surahKey);
                        databaseReference.child(verseKey).child("Ayah").setValue(displayText);
                        databaseReference.child(verseKey).child("AyahNum").setValue(verseKey);
                        databaseReference.child(verseKey).child(key2).child("SurahNum").setValue(otherSurahKey);
                        databaseReference.child(verseKey).child(key2).child("AyahNum").setValue(otherVerseKey);
                        databaseReference.child(verseKey).child(key2).child("Ayah").setValue(otherdisplayText);
                        databaseReference.child(verseKey).child(key2).child("Similarly").setValue((int) (similarity * 100) + "%");
                        resultsList.add("آية " +displayText+" السورة والايه"+ surahKey + ":" + verseKey +
                                " - آية " + otherSurahKey + ":" + otherVerseKey +
                                " - نسبة التشابه: " + (int) (similarity * 100) + "%");
                        resultsAdapter.notifyDataSetChanged();
                    }
                }
            }
        }
    }

    private double calculateSimilarity(String text1, String text2) {
        int commonWords = 0;

        String[] words1 = text1.split("\\s+");
        String[] words2 = text2.split("\\s+");

        for (String word1 : words1) {
            for (String word2 : words2) {
                if (word1.equals(word2)) {
                    commonWords++;
                    break;
                }
            }
        }

        double totalWords = Math.max(words1.length, words2.length);
        return commonWords / totalWords;
    }
}
