package com.example.p6_siti_julianti;
public class LyricsModel {
    private String arab, latin, arti;
    private int timestamp;

    public LyricsModel(String arab, String latin, String arti, int timestamp) {
        this.arab = arab;
        this.latin = latin;
        this.arti = arti;
        this.timestamp = timestamp;
    }

    public String getArab() { return arab; }
    public String getLatin() { return latin; }
    public String getArti() { return arti; }
    public int getTimestamp() { return timestamp; }
}


