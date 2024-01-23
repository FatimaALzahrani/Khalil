package com.quran.khalil;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.media.browse.MediaBrowser;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.api.gax.rpc.NotFoundException;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;

public class Try extends AppCompatActivity {
    int err=0;
    int prevEr=0;
    String all="";
    private TextToSpeech textToSpeech;
    private TextView txvResult;
    public static final int REQUEST_AUDIO_PERMISSION_CODE = 1;

    private int currentAyah = 1;  // Keep track of the current verse number
    private String jsonData;
    private String surahKey;
    // creating a variable for media recorder object class.
    private MediaRecorder mRecorder;

    // creating a variable for mediaplayer class
    private MediaPlayer mPlayer;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_try);
        jsonData = readRawResource(R.raw.quran_data);
        txvResult = findViewById(R.id.txvResult);
        Intent intent = getIntent();
        int surahNumber = intent.getIntExtra("SURAH_NUMBER", 1);
        surahKey = String.valueOf(surahNumber);
        textToSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int langResult = textToSpeech.setLanguage(new Locale("ar"));
                    if (langResult == TextToSpeech.LANG_MISSING_DATA || langResult == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Toast.makeText(Try.this, "اللغة غير مدعومة", Toast.LENGTH_SHORT).show();
                    } else {
                        int defaultAyah = 1;
                        convertTextToSpeech(getAyahText(defaultAyah));

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
                    Toast.makeText(Try.this, "TextToSpeech initialization failed", Toast.LENGTH_SHORT).show();
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

//    private void startRecording() {
//        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
//        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
//        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
//
//        if (intent.resolveActivity(getPackageManager()) != null) {
//            startActivityForResult(intent, 10);
//        } else {
//            Toast.makeText(getApplicationContext(), "Your Device Don't Support Speech Input", Toast.LENGTH_SHORT).show();
//        }
//    }
private void startRecording() {
    if (CheckPermissions()) {
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        try {
            // below method will prepare
            // our audio recorder class
            mRecorder.prepare();
        } catch (IOException e) {
            Log.e("TAG", "prepare() failed");
        }
        mRecorder.start();

        // Start speech recognition
        startSpeechRecognition();
    } else {
        RequestPermissions();
    }
}

    private void startSpeechRecognition() {
        if (SpeechRecognizer.isRecognitionAvailable(this)) {
            SpeechRecognizer speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
            speechRecognizer.setRecognitionListener(new RecognitionListener() {
                @Override
                public void onReadyForSpeech(Bundle params) {
                    // Called when the speech recognition service is ready to receive speech input
                }

                @Override
                public void onBeginningOfSpeech() {
                    // Called when the user starts to speak
                }

                @Override
                public void onRmsChanged(float rmsdB) {
                    // Called when the RMS (Root Mean Square) value of the audio being processed changes
                }

                @Override
                public void onBufferReceived(byte[] bytes) {

                }

                @Override
                public void onEndOfSpeech() {
                    // Called when the user has finished speaking
                }

                @Override
                public void onError(int error) {
                    // Called when an error occurs during speech recognition
                    Log.e("TAG", "Speech recognition error: " + error);
                }

                @Override
                public void onResults(Bundle results) {
                    // Called when the recognition is successfulT
                    ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                    if (matches != null && matches.size() > 0) {
                        String recognizedText = matches.get(0);
                        // Do something with the recognized text
                        Toast.makeText(Try.this, "Recognized: " + recognizedText, Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onPartialResults(Bundle partialResults) {
                    // Called when partial recognition results are available
                }

                @Override
                public void onSegmentResults(@NonNull Bundle segmentResults) {
                    RecognitionListener.super.onSegmentResults(segmentResults);
                }

                @Override
                public void onEndOfSegmentedSession() {
                    RecognitionListener.super.onEndOfSegmentedSession();
                }

                @Override
                public void onLanguageDetection(@NonNull Bundle results) {
                    RecognitionListener.super.onLanguageDetection(results);
                }

                @Override
                public void onEvent(int eventType, Bundle params) {
                    // Called when an event related to speech recognition occurs
                }
            });

            Intent recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            recognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getPackageName());

            speechRecognizer.startListening(recognizerIntent);
        } else {
            // Speech recognition not available on this device
            Log.e("TAG", "Speech recognition not available on this device");
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_AUDIO_PERMISSION_CODE:
                if (grantResults.length > 0) {
                    boolean permissionToRecord = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean permissionToStore = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if (permissionToRecord && permissionToStore) {
                        Toast.makeText(getApplicationContext(), "Permission Granted", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Permission Denied", Toast.LENGTH_LONG).show();
                    }
                }
                break;
        }
    }

    public boolean CheckPermissions() {
        // this method is used to check permission
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(), RECORD_AUDIO);
        return result1 == PackageManager.PERMISSION_GRANTED;
    }

    private void RequestPermissions() {
        // this method is used to request the
        // permission for audio recording and storage.
        ActivityCompat.requestPermissions(Try.this, new String[]{RECORD_AUDIO, WRITE_EXTERNAL_STORAGE}, REQUEST_AUDIO_PERMISSION_CODE);
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

                    } else {
                        Toast.makeText(this, "خطأ في المطابقة!", Toast.LENGTH_SHORT).show();
                        playAudio(getAudioFileName(currentAyah));
                        currentAyah--;
                        err++;
                    }

                    currentAyah++;
                    if(err!=prevEr) {
                        prevEr=err;
                    }else if (currentAyah <= getMaxAyah() ){
                        convertTextToSpeech("");
                    } else {
                        Toast.makeText(this, "انتهت السورة "+err, Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }

    private int getMaxAyah() {
        return 7;  // Assuming there are 3 verses in your example
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
