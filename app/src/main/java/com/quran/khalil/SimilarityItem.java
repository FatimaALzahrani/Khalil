package com.quran.khalil;

// SimilarityItem.java
public class SimilarityItem {
    private String surahNum;
    private String ayahNum;
    private String ayahText;
    private String similarly;

    public SimilarityItem(String surahNum, String ayahNum, String ayahText, String similarly) {
        this.surahNum = surahNum;
        this.ayahNum = ayahNum;
        this.ayahText = ayahText;
        this.similarly = similarly;
    }
    public SimilarityItem(String surahNum, String ayahNum, String ayahText) {
        this.surahNum = surahNum;
        this.ayahNum = ayahNum;
        this.ayahText = ayahText;
        this.similarly = similarly;
    }

    public String getSurahNum() {
        return surahNum;
    }

    public String getAyahNum() {
        return ayahNum;
    }

    public String getAyahText() {
        return ayahText;
    }

    public String getSimilarly() {
        return similarly;
    }
}
