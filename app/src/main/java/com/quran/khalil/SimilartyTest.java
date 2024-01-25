package com.quran.khalil;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SimilartyTest extends AppCompatActivity {

    private ListView testListView;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_similarty_test);

        testListView = findViewById(R.id.testListView);

        // إعداد Firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();

        // قائمة الخيارات
        String[] testOptions = {"اختبار في الجزء"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, testOptions);
        testListView.setAdapter(adapter);

        // التعامل مع اختيار المستخدم
        testListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // اختار الاختبار المناسب
                String selectedTest = testOptions[position];
                Intent intent = new Intent(SimilartyTest.this, TestActivity.class);
                intent.putExtra("selectedTest", selectedTest);
                startActivity(intent);
            }
        });
    }
}
