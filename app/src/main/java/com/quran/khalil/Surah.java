package com.quran.khalil;

import java.util.List;

public class Surah {
    private String verse;
    private String similarVerses;

    public Surah() {
        // يجب أن يكون هناك بناء فارغ لـ Firebase
    }

    public Surah(String verse, String similarVerses) {
        this.verse = verse;
        this.similarVerses = similarVerses;
    }

    public String getVerse() {
        return verse;
    }

    public void setVerse(String verse) {
        this.verse = verse;
    }

    public String getSimilarVerses() {
        return similarVerses;
    }

    public void setSimilarVerses(String similarVerses) {
        this.similarVerses = similarVerses;
    }
}