package com.quran.khalil;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.os.Bundle;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

public class TestActivity extends AppCompatActivity {

    private DatabaseReference databaseReference;
    private TextView questionTextView;
    private Button option1Button, option2Button, option3Button;
    private List<Question> questionList;
    private int currentQuestionIndex;
    private int score;
    String[] surahNames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        // إعداد Firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();

        questionTextView = findViewById(R.id.questionTextView);
        option1Button = findViewById(R.id.option1Button);
        option2Button = findViewById(R.id.option2Button);
        option3Button = findViewById(R.id.option3Button);

        // استرجاع نوع الاختبار المحدد
        String selectedTest = getIntent().getStringExtra("selectedTest");

        // استرجاع البيانات من Firebase وعرض السؤال الأول
        if ("اختبار في الجزء".equals(selectedTest)
                || "اختبار في السورة".equals(selectedTest)
                || "اختبار في القرآن كامل".equals(selectedTest)) {
            DatabaseReference testRef = databaseReference.child("Question");
            testRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    populateQuestionList(snapshot);
                    displayQuestion();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // التعامل مع الأخطاء إذا لزم الأمر
                }
            });
        }

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

    }

    private void populateQuestionList(DataSnapshot dataSnapshot) {
        questionList = new ArrayList<>();
        for (DataSnapshot partSnapshot : dataSnapshot.getChildren()) {
            for (DataSnapshot surahSnapshot : partSnapshot.getChildren()) {
                for (DataSnapshot ayahSnapshot : surahSnapshot.getChildren()) {
                    String selectedSurah = ayahSnapshot.getKey();
                    String verse = ayahSnapshot.getValue().toString();
                    Object ayahValue = ayahSnapshot.getValue();

                    if (ayahValue instanceof Map) {
                        for (Object verseObject : ((Map<String, Object>) ayahValue).values()) {
                             verse = verseObject.toString();
                        }
                    }
                    List<String> options = new ArrayList<>();
                    for (DataSnapshot optionSnapshot : surahSnapshot.getChildren()) {
                        options.add(surahNames[Integer.parseInt(optionSnapshot.getKey())-1]);
                    }
                    Question question = new Question(verse, surahNames[Integer.parseInt(selectedSurah)-1], options);
                    questionList.add(question);


                    // خلط قائمة الأسئلة
                    Collections.shuffle(questionList);

                    // خلط خيارات كل سؤال
                    for (Question question2 : questionList) {
                        Collections.shuffle(question2.getOptions());
                    }

                }
            }
        }
    }

    private void displayQuestion() {
        if (currentQuestionIndex < questionList.size()) {
            // استمرار عرض السؤال والخيارات
            Question currentQuestion = questionList.get(currentQuestionIndex);
            questionTextView.setText("الآية: " + currentQuestion.getVerse() + " - اختر السورة الصحيحة:");

            // تحقق من وجود الإجابة في الخيارات
            List<String> options = currentQuestion.getOptions();
            String correctSurah = currentQuestion.getSelectedSurah();
            if(options.size()>2) {
                String[] option = {options.get(0), options.get(1), options.get(2)};
                if (!option[0].equals(correctSurah) && !option[1].equals(correctSurah) && !option[2].equals(correctSurah)) {
                    option[2] = correctSurah;
                    Collections.shuffle(Arrays.asList(option));
                }
                option1Button.setText(option[0]);
                option2Button.setText(option[1]);
                option3Button.setText(option[2]);
            }else {
                Random random = new Random();
                options.add(surahNames[random.nextInt(113) + 1]);
                // عرض الخيارات
                option1Button.setText(options.get(0));
                option2Button.setText(options.get(1));
                option3Button.setText(options.get(2));
            }



            // تحديد الاستماع للاختيارات
            option1Button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    checkAnswer(option1Button.getText().toString(), correctSurah);
                }
            });

            option2Button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    checkAnswer(option2Button.getText().toString(), correctSurah);
                }
            });

            option3Button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    checkAnswer(option3Button.getText().toString(), correctSurah);
                }
            });
        } else {
            // إظهار النتيجة أو أي شيء آخر
            questionTextView.setText("انتهى الاختبار. نقاطك: " + score);
            option1Button.setVisibility(View.INVISIBLE);
            option2Button.setVisibility(View.INVISIBLE);
            option3Button.setVisibility(View.INVISIBLE);
        }
    }

    private void checkAnswer(String selectedOption, String correctSurah) {
        if (selectedOption.equals(correctSurah)) {
            // الإجابة صحيحة
            score++;
        }
//        score+=currentQuestionIndex +" "+selectedOption+" "+correctSurah+"\n";
        // انتقال إلى السؤال التالي
        currentQuestionIndex++;

        // عرض السؤال التالي إذا كان متاحًا، وإلا انهاء الاختبار
        if (currentQuestionIndex < 3) {
            displayQuestion();
        } else {
            // عرض النتيجة
            questionTextView.setText("انتهى الاختبار. نقاطك: " + score);
            option1Button.setVisibility(View.INVISIBLE);
            option2Button.setVisibility(View.INVISIBLE);
            option3Button.setVisibility(View.INVISIBLE);
        }
    }
}
