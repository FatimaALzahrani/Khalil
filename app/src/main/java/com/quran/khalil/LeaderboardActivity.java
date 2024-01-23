package com.quran.khalil;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
public class LeaderboardActivity extends AppCompatActivity {

    private DatabaseReference usersRef;
    private RecyclerView recyclerViewLeaderboard;
    private LeaderboardAdapter leaderboardAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);

        recyclerViewLeaderboard = findViewById(R.id.recyclerViewLeaderboard);
        recyclerViewLeaderboard.setLayoutManager(new LinearLayoutManager(this));
        leaderboardAdapter = new LeaderboardAdapter(new ArrayList<>());
        recyclerViewLeaderboard.setAdapter(leaderboardAdapter);

        usersRef = FirebaseDatabase.getInstance().getReference().child("Dashboard").child("Quran");

        // احصل على بيانات المستخدمين من Firebase وقم بتحديث لوحة المتصدرين
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<UserMistake> userMistakeList = new ArrayList<>();
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    UserMistake userMistake = userSnapshot.getValue(UserMistake.class);
                    if (userMistake != null) {
                        userMistakeList.add(userMistake);
                    }
                }

                // قم بترتيب قائمة المستخدمين حسب الكلمات والأخطاء
                Collections.sort(userMistakeList, new Comparator<UserMistake>() {
                    @Override
                    public int compare(UserMistake userMistake1, UserMistake userMistake2) {
                        // قم بترتيب حسب عدد الكلمات، ثم عدد الأخطاء
                        if (userMistake1.getWords() != userMistake2.getWords()) {
                            return userMistake2.getWords() - userMistake1.getWords();
                        } else {
                            return userMistake1.getMistakes() - userMistake2.getMistakes();
                        }
                    }
                });

                leaderboardAdapter.setData(userMistakeList); // قم بتحديث بيانات Adapter بدلاً من إعادة إنشاءه
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // التعامل مع الأخطاء في حالة الفشل
            }
        });
    }
}