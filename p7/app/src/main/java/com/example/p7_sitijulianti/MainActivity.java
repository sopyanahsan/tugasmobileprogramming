package com.example.p7_sitijulianti;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    CardView pagiCard, soreCard, CalCard, ShareCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pagiCard = findViewById(R.id.pagi);
        soreCard = findViewById(R.id.sore);
        ShareCard = findViewById(R.id.shareapp);

        pagiCard.setOnClickListener(v -> openDzikir("pagi"));
        soreCard.setOnClickListener(v -> openDzikir("petang"));
        ShareCard.setOnClickListener(v -> shareApplication());
    }

    private void openDzikir(String kategori) {
        Intent intent = new Intent(MainActivity.this, DzikirActivity.class);
        intent.putExtra("kategori", kategori);
        startActivity(intent);
    }
    private void shareApplication() {


        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.setType("text/plain");

        Intent shareIntent = Intent.createChooser(sendIntent, "Bagikan aplikasi lewat:");
        startActivity(shareIntent);
    }

}