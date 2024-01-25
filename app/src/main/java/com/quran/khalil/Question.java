package com.quran.khalil;

import java.util.List;

public class Question {
    private String verse;
    private String selectedSurah;
    private List<String> options;

    public Question() {
        // يجب وجود البناء الفارغ لاستخدام Firebase
    }

    public Question(String verse, String selectedSurah, List<String> options) {
        this.verse = verse;
        this.selectedSurah = selectedSurah;
        this.options = options;
    }

    public String getVerse() {
        return verse;
    }

    public String getSelectedSurah() {
        return selectedSurah;
    }

    public List<String> getOptions() {
        return options;
    }
}
