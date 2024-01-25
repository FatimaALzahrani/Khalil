package com.quran.khalil;

import android.content.Intent;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.media.browse.MediaBrowser;
import android.net.Uri;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.api.gax.rpc.NotFoundException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;
import java.util.Scanner;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Recitation extends AppCompatActivity {
    int err=0;
    int prevEr=0;
    String all="";
    private TextToSpeech textToSpeech;
    private TextView txvResult;
    private TextView Result;
    private TextView Word;
    private TextView Mis;
    private int currentAyah;  // Keep track of the current verse number
    private String jsonData;
    private String surahKey;
    private DatabaseReference userRef;
    private int currentAttempts;
    private int currentMistakes;
    private int currentWords;
    private int Words=0;
    private int ayahNumber;
    private TextView SurahName;
    private String[] surahNames;
    private double per;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recitation);
        jsonData = readRawResource(R.raw.quran_data);
        txvResult = findViewById(R.id.txvResult);
        Result = findViewById(R.id.Result);
        Word = findViewById(R.id.words);
        Mis = findViewById(R.id.mistake);
        SurahName = findViewById(R.id.Surah);
        String currentUsername = "Fatimah_Alzahrani";
        Intent intent = getIntent();
        int surahNumber = intent.getIntExtra("SURAH_NUMBER", 1);
        String ayahNumberStr = intent.getStringExtra("Ayah_NUMBER");
        int ayahNumber = ayahNumberStr != null ? Integer.parseInt(ayahNumberStr) : 1;

        surahNames = new String[]{
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
        SurahName.setText(surahNames[surahNumber-1]+"سورة ");
        currentAyah=ayahNumber;
        surahKey = String.valueOf(surahNumber);
        userRef = FirebaseDatabase.getInstance().getReference().child("Dashboard").child("Quran").child(currentUsername);
        userRef.child("bySurah").child(surahKey).child("attempts").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                 currentAttempts = 0;

                if (dataSnapshot.exists()) {
                    currentAttempts = dataSnapshot.getValue(Integer.class);
                }
                userRef.child("bySurah").child(surahKey).child("attempts").setValue(currentAttempts + 1);
                userRef.child("bySurah").child(surahKey).child("attemptsDetails").child("attempt_" + (currentAttempts + 1)).child("Mistake").setValue(0);
                userRef.child("bySurah").child(surahKey).child("attemptsDetails").child("attempt_" + (currentAttempts + 1)).child("Words").setValue(0);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        // قم بفحص قيمة Mistakes الحالية وتحديثها
        userRef.child("Mistakes").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    currentMistakes = dataSnapshot.getValue(Integer.class);
                    userRef.child("Mistakes").setValue(currentMistakes);
                } else {
                    userRef.child("Mistakes").setValue(0);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        
        //نجيب عدد الكلمات ونحدثه
        userRef.child("Words").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    currentWords = dataSnapshot.getValue(Integer.class);
                    userRef.child("Words").setValue(currentWords);
                } else {
                    userRef.child("Words").setValue(0);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });


        textToSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int langResult = textToSpeech.setLanguage(new Locale("ar"));
                    if (langResult == TextToSpeech.LANG_MISSING_DATA || langResult == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Toast.makeText(Recitation.this, "اللغة غير مدعومة", Toast.LENGTH_SHORT).show();
                    } else {
                        convertTextToSpeech(getAyahText(currentAyah));

                        textToSpeech.setOnUtteranceProgressListener(new UtteranceProgressListener() {
                            @Override
                            public void onStart(String utteranceId) {
                                // Handle start of speech
                            }

                            @Override
                            public void onDone(String utteranceId) {
                                if (utteranceId.equals("uniqueId")) {
                                    // Correction sound is done, start recording
                                    startRecording();
                                }
                            }

                            @Override
                            public void onError(String utteranceId) {
                                // Handle errors
                            }
                        });
                    }
                } else {
                    Toast.makeText(Recitation.this, "TextToSpeech initialization failed", Toast.LENGTH_SHORT).show();
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

    private void convertTextToSpeech(String text) {
        if (textToSpeech != null) {
            textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, "uniqueId");
        }
    }

    private void startRecording() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, 10);
        } else {
            Toast.makeText(getApplicationContext(), "Your Device Don't Support Speech Input", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 10:
                if (resultCode == RESULT_OK && data != null) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    String userSpeech = result.get(0);

                    String ayahText = getAyahText(currentAyah);


                    if (userSpeech.equals(ayahText)) {
                        Toast.makeText(this, "تمت المطابقة!", Toast.LENGTH_SHORT).show();
                        all+=getAyahTextOthman(currentAyah);
                        all+="﴿"+currentAyah+"﴾";
                        txvResult.setText(all);
                        err=0;
                    } else {
                        currentAyah--;
                        err++;
                    }
                    currentAyah++;
                    if(err==3) {
                        if(!getAudioFileName(currentAyah).equals(""))
                            playAudio(getAudioFileName(currentAyah));
                        else
                            convertTextToSpeech(getAyahTextOthman(currentAyah));
                        err=0;
                        prevEr++;
                        currentMistakes++;
                        userRef.child("bySurah").child(surahKey).child("attemptsDetails").child("attempt_" + (currentAttempts + 1)).child("Mistake").setValue(prevEr);
                        userRef.child("Mistakes").setValue(currentMistakes);
                        Mis.setText("✖️"+String.valueOf(prevEr));
                        per=(1 - (double) prevEr / Words) * 100;
                        Result.setText(String.format("%.2f%%", per));
                    }else if(currentAyah <= getMaxAyah() && err==0){
                        convertTextToSpeech("");
                        String [] numberOfWords =getAyahText(currentAyah).split(" ");
                        currentWords+=numberOfWords.length;
                        Words++;
                        err=0;
                        userRef.child("Words").setValue(currentWords);
                        userRef.child("bySurah").child(surahKey).child("attemptsDetails").child("attempt_" + (currentAttempts + 1)).child("Words").setValue(Words);
                        Word.setText("✔️"+Words);
                        per=(1 - (double) prevEr / Words) * 100;
                        if(prevEr==0)
                            per=100;
                        Result.setText(String.format("%.2f%%", per));
                        userRef.child("Surah").child(surahKey).child(String.valueOf(currentAyah));
                    }else if (err>0){
                        convertTextToSpeech("إنتبه");
                    } else {
                        Toast.makeText(this, "انتهت السورة "+err, Toast.LENGTH_SHORT).show();
                        if(ayahNumber==1){
                            userRef.child("bySurah").child(surahKey).child("Done").setValue("Yes");
                            new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                                    .setTitleText("مُبارك !🎉")
                                    .setContentText("لقد انتهيت من سورة "+surahNames[Integer.parseInt(surahKey)-1]).show();
                        }
                    }
                }
                break;
        }
    }

    private int getMaxAyah() {
        int maxAyah = 0;

        try {
            JSONObject quranData = new JSONObject(jsonData);
            if (quranData.has(surahKey)) {
                JSONObject selectedSurah = quranData.getJSONObject(surahKey);

                // Get the keys (ayah numbers) and find the maximum
                Iterator<String> ayahKeys = selectedSurah.keys();
                while (ayahKeys.hasNext()) {
                    String ayahKey = ayahKeys.next();
                    int ayahNumber = Integer.parseInt(ayahKey);
                    if (ayahNumber > maxAyah) {
                        maxAyah = ayahNumber;
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return maxAyah;
    }


    private String getAudioFileName(int ayahNumber) {
        try {
            JSONObject quranData = new JSONObject(jsonData);
            if (quranData.has(surahKey)) {
                JSONObject selectedAyah = quranData.getJSONObject(surahKey);
                if (selectedAyah.has(String.valueOf(ayahNumber))) {
                    JSONObject ayahData = selectedAyah.getJSONObject(String.valueOf(ayahNumber));
                    return ayahData.getString("audioFileName");
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }
    private String getAyahText(int ayahNumber) {
        try {
            JSONObject quranData = new JSONObject(jsonData);
            if (quranData.has(surahKey)) {
                JSONObject selectedAyah = quranData.getJSONObject(surahKey);
                if (selectedAyah.has(String.valueOf(ayahNumber))) {
                    JSONObject ayahData = selectedAyah.getJSONObject(String.valueOf(ayahNumber));
                    return ayahData.getString("text");
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }
    private String getAyahTextOthman(int ayahNumber) {
        try {
            JSONObject quranData = new JSONObject(jsonData);
            if (quranData.has(surahKey)) {
                JSONObject selectedAyah = quranData.getJSONObject(surahKey);
                if (selectedAyah.has(String.valueOf(ayahNumber))) {
                    JSONObject ayahData = selectedAyah.getJSONObject(String.valueOf(ayahNumber));
                    return ayahData.getString("displayText");
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }

    private void playAudio(String audioFileName) {
        MediaPlayer mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(getApplicationContext(), Uri.parse(audioFileName));
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.start();
                }
            });
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.release();
                    // بعد انتهاء الصوت، قم ببدء التسجيل
                    startRecording();
                }
            });
            mediaPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
    }
}
