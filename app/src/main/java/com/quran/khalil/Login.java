package com.quran.khalil;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Login extends AppCompatActivity {

    private static final int RC_SIGN_IN = 1;
    private static final String TAG = "GOOGLEAUTH";
    Dialog dialog;
    private EditText password;
    private EditText email;
    private FirebaseAuth mAuth;
    GoogleSignInOptions gso;
    ProgressBar progressBar;
    private GoogleSignInClient mGoogleSignInClient;
    private GoogleSignInClient gsc;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        gso=new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc= GoogleSignIn.getClient(this, gso);
        // Configure sign-in to request the user's ID, email address, and basic profile
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        // Build a GoogleSignInClient with the options specified by gso
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        // Set the dimensions of the sign-in button.
        SignInButton signInButton = findViewById(R.id.google);
        signInButton.setSize(SignInButton.SIZE_STANDARD);


        findViewById(R.id.google).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });

        password = (EditText) findViewById(R.id.pass);
        email = (EditText) findViewById(R.id.email);
        mAuth=FirebaseAuth.getInstance();
        progressBar=findViewById(R.id.progressBar);
    }

    // في دالة signIn
    public void sing_in(View v){
        String em = email.getText().toString().trim();
        String pass = password.getText().toString().trim();
        if (em.isEmpty()) {
            email.setError("البريد الإلكتروني مطلوب");
            email.requestFocus();
            return;
        }
        if (em.length() < 7 || !em.contains("@") || !em.contains(".")) {
            email.setError("رجاءً أدخل بريد إلكتروني صحيح!");
            email.requestFocus();
            return;
        }
        if (pass.isEmpty()) {
            password.setError("كلمة المرور مطلوبة");
            password.requestFocus();
            return;
        }
        if (pass.length() < 6) {
            password.setError("يجب ان يكون طول كلمة المرور أكثر من 6 خانات!");
            password.requestFocus();
            return;
        }

        // التحقق من وجود اتصال بالإنترنت
        if (!isOnline()) {
            new SweetAlertDialog(Login.this, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("خطأ في الانترنت")
                    .setContentText("لايوجد لديك اتصال بالانترنت أو الاتصال ضعيف, يُرجى اعادة الاتصال والمحاولة مرة اخرى")
                    .setConfirmText("تم")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            sDialog.dismissWithAnimation();
                        }
                    })
                    .show();
            progressBar.setVisibility(View.INVISIBLE);
        } else {
            // قم بإجراء تسجيل الدخول إذا كان هناك اتصال بالإنترنت
            progressBar.setVisibility(View.VISIBLE);
            mAuth.signInWithEmailAndPassword(em, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//                        if (em.equals("442006322@stu.bu.edu.sa")) {
//                            finish();
//                            Intent intent = new Intent(Login.this, menu2.class);
//                            startActivity(intent);
//                        }
                        DatabaseReference adminReference = FirebaseDatabase.getInstance().getReference("Admin");
                        adminReference.orderByChild("email").equalTo(em).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    // The email exists in the "admin" table
                                    finish();
                                    Intent intent = new Intent(Login.this, ChoseSurahActivity.class);
                                    startActivity(intent);
                                } else {
                                    if (user.isEmailVerified()) {
                                        finish();
                                        Intent intent = new Intent(Login.this, ChoseSurahActivity.class);
                                        intent.putExtra("email", em);
                                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                                        DatabaseReference usersRef = database.getReference("Users");
                                        Map<String, Object> updates = new HashMap<>();
                                        updates.put("password", pass);
                                        usersRef.child(user.getUid()).updateChildren(updates);
                                        startActivity(intent);
                                    } else {
                                        user.sendEmailVerification();
                                        Toast.makeText(Login.this, "تحقق من بريدك الإلكتروني لتأكيد حسابك!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                // Handle any errors that occurred.
                            }
                        });

                    } else {
                        Toast.makeText(Login.this, "فشل تسجيل الدخول! يرجى التحقق من البيانات المدخلة", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                }
            });
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI accordingly
                Log.w(TAG, "Google sign in failed", e);
                // ...
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
        Intent intent = new Intent(Login.this, ChoseSurahActivity.class);
        startActivity(intent);
    }

    public void forgetPass(View view) {
        Intent intent=new Intent(this,Forget.class);
        startActivity(intent);
    }

    public void Signup(View view) {
        Intent intent=new Intent(this,Signup.class);
        startActivity(intent);
    }
    //check the internet of user
    public boolean isOnline() {
        ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connManager.getActiveNetworkInfo();
        if(networkInfo != null && networkInfo.isConnectedOrConnecting()){
            return true;
        }
        else{
            return false;
        }
    }

}