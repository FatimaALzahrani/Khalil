package com.quran.khalil;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.*;
import java.util.HashSet;
import java.util.Set;

import java.io.BufferedReader;
import java.io.FileReader;

public class QuranComparison {
    private static Object jsonData;

    public static void collectSimilaritiesInSurah2() {
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader("res/raw/quran_data.json"));
            StringBuilder jsonString = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                jsonString.append(line);
            }
            bufferedReader.close();

            JSONObject quranData = new JSONObject(jsonString.toString());
            JSONArray verses = quranData.getJSONObject("2").getJSONArray("verses"); // 2 هو رقم سورة البقرة

            for (int i = 0; i < verses.length(); i++) {
                JSONObject currentVerse = verses.getJSONObject(i);
                String currentText = currentVerse.getString("text");

                for (int j = 0; j < verses.length(); j++) {
                    if (i != j) {
                        JSONObject otherVerse = verses.getJSONObject(j);
                        String otherText = otherVerse.getString("text");

                        double similarity = calculateSimilarity(currentText, otherText);

                        if (similarity > 0.5) {
                            addToDatabase(currentVerse, otherVerse);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void performComparison() {
        try {
//            jsonData = readRawResource(R.raw.quran_data);
//            JSONObject quranData = new JSONObject(jsonData);
            // قراءة ملف JSON
            BufferedReader bufferedReader = new BufferedReader(new FileReader("res/raw/quran_data.json"));
            StringBuilder jsonString = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                jsonString.append(line);
            }
            bufferedReader.close();

            // تحليل الملف JSON
            JSONObject quranData = new JSONObject(jsonString.toString());

            // الحصول على جميع الآيات
            JSONArray verses = quranData.getJSONArray("verses");

            // مقارنة كل آية بكل الآيات الأخرى
            for (int i = 0; i < verses.length(); i++) {
                JSONObject currentVerse = verses.getJSONObject(i);
                String currentText = currentVerse.getString("text");

                for (int j = 0; j < verses.length(); j++) {
                    if (i != j) {
                        JSONObject otherVerse = verses.getJSONObject(j);
                        String otherText = otherVerse.getString("text");

                        // قياس التشابه بين الآيات (يمكنك استخدام خوارزمية معينة)
                        double similarity = calculateSimilarity(currentText, otherText);

                        // إذا كان التشابه أكبر من 0.5، قم بإضافة الآية إلى قاعدة البيانات
                        if (similarity > 0.5) {
                            addToDatabase(currentVerse,otherVerse);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static double calculateSimilarity(String text1, String text2) {
        Set<Character> set1 = new HashSet<>();
        Set<Character> set2 = new HashSet<>();

        // إضافة الأحرف إلى مجموعة الجملة الأولى
        for (char c : text1.toCharArray()) {
            set1.add(c);
        }

        // إضافة الأحرف إلى مجموعة الجملة الثانية
        for (char c : text2.toCharArray()) {
            set2.add(c);
        }

        // حساب الاجتماع (Union)
        Set<Character> union = new HashSet<>(set1);
        union.addAll(set2);

        // حساب التقاطع (Intersection)
        Set<Character> intersection = new HashSet<>(set1);
        intersection.retainAll(set2);

        // حساب معامل التشابه النصي
        return (double) intersection.size() / union.size();
    }

    public static void addToDatabase(JSONObject verse1, JSONObject verse2) {
        try {
            // استخراج رقم السورة ورقم الآية لكل من الآيتين
            int surahNumber1 = verse1.getInt("surah");
            int ayahNumber1 = verse1.getInt("numberInSurah");

            int surahNumber2 = verse2.getInt("surah");
            int ayahNumber2 = verse2.getInt("numberInSurah");

            // إنشاء مرجع لجدول "Similarty" في Realtime Database
            DatabaseReference similarityRef = FirebaseDatabase.getInstance().getReference().child("Similarty");

            // إضافة رقم السورة ورقم الآية للآية الأولى
            similarityRef.child(String.valueOf(surahNumber1))
                    .child(String.valueOf(ayahNumber1))
                    .child("similarVerses")
                    .child(String.valueOf(ayahNumber2))
                    .setValue(surahNumber2);

            // إضافة رقم السورة ورقم الآية للآية الثانية
            similarityRef.child(String.valueOf(surahNumber2))
                    .child(String.valueOf(ayahNumber2))
                    .child("similarVerses")
                    .child(String.valueOf(ayahNumber1))
                    .setValue(surahNumber1);

            System.out.println("Added similar verses to the Similarty table: "
                    + surahNumber1 + ":" + ayahNumber1 + " - " + surahNumber2 + ":" + ayahNumber2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
