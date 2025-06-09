package com.example.p6_siti_julianti;

import java.util.List;

public class AdzanModel {
    private String country;
    private String title;
    private String flag;
    private String audio;
    private List<LyricsModel> lyrics;

    public AdzanModel(String country, String title, String flag, String audio, List<LyricsModel> lyrics) {
        this.country = country;
        this.title = title;
        this.flag = flag;
        this.audio = audio;
        this.lyrics = lyrics;
    }

    public String getCountry() {
        return country;
    }

    public String getTitle() {
        return title;
    }

    public String getFlag() {
        return flag;
    }

    public String getAudio() {
        return audio;
    }

    public List<LyricsModel> getLyrics() {
        return lyrics;
    }

}



