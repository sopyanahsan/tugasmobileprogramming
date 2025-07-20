package com.example.p7_sitijulianti;

public class DzikirModel {
    private String arab;
    private String latin;
    private String arti;
    private int jumlah;
    private String audioUrl;
    private String kategori;
    private String audio;
    private String judul;

    public DzikirModel(String arab, String latin, String arti, int jumlah, String audioUrl, String kategori) {
        this.arab = arab;
        this.latin = latin;
        this.arti = arti;
        this.judul = judul;
        this.jumlah = jumlah;
        this.audioUrl = audioUrl;
        this.audio = audio;
        this.kategori = kategori;
    }

    public String getArab() {
        return arab;
    }

    public String getLatin() {
        return latin;
    }

    public String getArti() {
        return arti;
    }

    public int getJumlah() {
        return jumlah;
    }

    public String getAudioUrl() {
        return audioUrl;
    }

    public String getKategori() {
        return kategori;
    }
    public String getAudio() {
        return audio;
    }
    public String getJudul() {
        return judul;
    }
    public void setJudul(String judul) {
        this.judul = judul;
    }
}
