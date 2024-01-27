package com.quran.khalil;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class DisplayServices extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_services);
    }

    public void Start1(View view) {
        Intent intent=new Intent(this,ChoseSurahActivity.class);
        startActivity(intent);
    }
    public void Start2(View view) {
        Intent intent=new Intent(this,Test.class);
        startActivity(intent);
    }
}