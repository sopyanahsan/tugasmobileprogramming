package com.example.p6_siti_julianti;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import android.os.Handler;
import android.widget.Toast;

import com.gauravk.audiovisualizer.visualizer.WaveVisualizer;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;


public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 100;

    private TextView tvArab, tvLatin, tvArti, tvJudul;
    private ImageButton btnPlay;
    private RecyclerView recData;
    private MediaPlayer mediaPlayer;
    private Handler handler = new Handler();
    private List<AdzanModel> adzanList;
    private List<LyricsModel> lyricsList;
    private AdzanModel selectedAdzan;
    private Runnable lyricRunnable;
    private WaveVisualizer mVisualizer ;
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_main);

        tvArab = findViewById(R.id.tvArab);
        tvLatin = findViewById(R.id.tvLatin);
        tvArti = findViewById(R.id.tvArti);
        tvJudul = findViewById(R.id.tvJudul);
        btnPlay = findViewById(R.id.btnplay);
        recData = findViewById(R.id.recData);
        mVisualizer = findViewById(R.id.wave);

        // Minta permission RECORD_AUDIO saat runtime jika belum diberikan
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.RECORD_AUDIO},
                    REQUEST_RECORD_AUDIO_PERMISSION);
        }

        adzanList = loadAdzanList(this);

        if (adzanList.isEmpty()) {
            Toast.makeText(this, "Data adzan tidak ditemukan!", Toast.LENGTH_LONG).show();
            Log.e("MainActivity", "adzanList kosong!");
            return;
        }


        selectedAdzan = adzanList.get(0);
        tvJudul.setText(selectedAdzan.getTitle());

        AdzanAdapter adapter = new AdzanAdapter(adzanList, this, this::playAdzan);
        recData.setLayoutManager(new LinearLayoutManager(this));
        recData.setAdapter(new AdzanAdapter(adzanList, this, this::playAdzan));


        btnPlay.setOnClickListener(v -> {
            if (mediaPlayer != null) {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                    btnPlay.setImageResource(android.R.drawable.ic_media_play);
                    handler.removeCallbacks(lyricRunnable);
                } else {
                    mediaPlayer.start();
                    btnPlay.setImageResource(android.R.drawable.ic_media_pause);
                    startLyricSync();
                }
            }
        });

        // Inisialisasi AdMob
        MobileAds.initialize(this, initializationStatus -> {});

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_RECORD_AUDIO_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("MainActivity", "Record audio permission granted");
            } else {
                Log.e("MainActivity", "Record audio permission denied");
                Toast.makeText(this, "Permission RECORD_AUDIO diperlukan untuk visualizer suara", Toast.LENGTH_LONG).show();
            }
        }
    }

    private List<AdzanModel> loadAdzanList(Context context) {
        List<AdzanModel> list = new ArrayList<>();
        lyricsList = new ArrayList<>();

        try {
            InputStream is = context.getAssets().open("adzan_list.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String json = new String(buffer, "UTF-8");


            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);

                List<LyricsModel> lyrics = new ArrayList<>();
                JSONArray lyricsJson = obj.getJSONArray("lyrics");
                for (int j = 0; j < lyricsJson.length(); j++) {
                    JSONObject lyricObj = lyricsJson.getJSONObject(j);
                    lyrics.add(new LyricsModel(
                            lyricObj.getString("arab"),
                            lyricObj.getString("latin"),
                            lyricObj.getString("arti"),
                            lyricObj.getInt("timestamp")
                    ));
                }

                list.add(new AdzanModel(
                        obj.getString("country"),
                        obj.getString("title"),
                        obj.getString("flag"),
                        obj.getString("audio"),
                        lyrics
                ));
            }


        } catch (Exception e) {
            Log.e("MainActivity", "Gagal load JSON: " + e.getMessage());

        }
        return list;
    }




    private void playAdzan(AdzanModel adzan) {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }

        selectedAdzan = adzan; // penting: update data adzan aktif
        int audioResId = getResources().getIdentifier(adzan.getAudio(), "raw", getPackageName());
        mediaPlayer = MediaPlayer.create(this, audioResId);

        if (mediaPlayer == null) {
            Log.e("MainActivity", "Audio file tidak ditemukan: " + adzan.getAudio());
            Toast.makeText(this, "Gagal memuat audio: " + adzan.getAudio(), Toast.LENGTH_SHORT).show();
            return;
        }

        if (adzanList.isEmpty()) {
            Toast.makeText(this, "Data adzan kosong", Toast.LENGTH_LONG).show();
            return;
        }



        tvJudul.setText(adzan.getTitle());
        btnPlay.setImageResource(android.R.drawable.ic_media_pause);

        mediaPlayer.setOnPreparedListener(mp -> {
            mp.start();

            int audioSessionId = mp.getAudioSessionId();
            if (audioSessionId != -1) {
                try {
                    mVisualizer.setAudioSessionId(audioSessionId);
                } catch (RuntimeException e) {
                    Toast.makeText(this, "Gagal inisialisasi visualizer", Toast.LENGTH_SHORT).show();
                }
            }

            startLyricSync();
        });

        mediaPlayer.setOnCompletionListener(mp -> {
            btnPlay.setImageResource(android.R.drawable.ic_media_play);
        });
    }


    private void startLyricSync() {
        handler.removeCallbacksAndMessages(null);
        if (selectedAdzan == null || selectedAdzan.getLyrics() == null) return;

        for (LyricsModel lyric : selectedAdzan.getLyrics()) {
            handler.postDelayed(() -> {
                tvArab.setText(lyric.getArab());
                tvLatin.setText(lyric.getLatin());
                tvArti.setText(lyric.getArti());
            }, lyric.getTimestamp());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        if (mVisualizer != null) {
            mVisualizer.release();
        }
        handler.removeCallbacksAndMessages(null);
    }



}
