package com.quran.khalil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;

public class Forget2 extends AppCompatActivity {
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget2);

    }

    public void Rest(View view) {
        Intent intent=getIntent();
        mAuth = FirebaseAuth.getInstance();
        String strEmail =intent.getStringExtra("email");
         mAuth.sendPasswordResetEmail(strEmail)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(Forget2.this, "تم إرسال رابط إعادة تعيين كلمة المرور إلى بريدك الإلكتروني المسجل", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Forget2.this, "Error :- " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }

    public void login(View view) {
        Intent intent=new Intent(this,Login.class);
        startActivity(intent);
    }
}