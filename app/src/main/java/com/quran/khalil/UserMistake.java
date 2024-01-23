package com.quran.khalil;

public class UserMistake {
    private String userName;
    private int Mistakes;
    private int Words;
//    private Map<String, Map<String, Map<String, Integer>>> bySurah; // تأكد من استخدام الهيكل الصحيح لـ bySurah

    public UserMistake() {
        // الكود الخاص بالبناء، يمكنك تركه فارغًا إذا لم تحتاج إلى بناء خاص
    }

    public UserMistake(String username, int mistakes, int words) {
        this.userName = username;
        this.Mistakes = mistakes;
        this.Words = words;
    }

    public String getUsername() {
        return userName;
    }

    public void setUsername(String username) {
        this.userName = username;
    }

    public int getMistakes() {
        return Mistakes;
    }

    public void setMistakes(int mistakes) {
        this.Mistakes = mistakes;
    }

    public int getWords() {
        return Words;
    }

    public void setWords(int words) {
        this.Words = words;
    }
//    public Map<String, Map<String, Map<String, Integer>>> getBySurah() {
//        return bySurah;
//    }
//
//    public void setBySurah(Map<String, Map<String, Map<String, Integer>>> bySurah) {
//        this.bySurah = bySurah;
//    }
}
