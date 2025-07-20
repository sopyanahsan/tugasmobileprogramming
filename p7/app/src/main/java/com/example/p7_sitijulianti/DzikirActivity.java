package com.example.p7_sitijulianti;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

public class DzikirActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private DzikirPagerAdapter pagerAdapter;
    private List<DzikirModel> dzikirList = new ArrayList<>();
    private String kategori;
    private TextView pagerIndicatorText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dzikir);

        viewPager = findViewById(R.id.viewPagerDzikir);
        pagerIndicatorText = findViewById(R.id.pagerIndicatorText);
        kategori = getIntent().getStringExtra("kategori");

        loadDataFromJson();
    }

    private void loadDataFromJson() {
        try {
            InputStream inputStream = getResources().openRawResource(R.raw.dzikir);
            Reader reader = new InputStreamReader(inputStream);
            Gson gson = new Gson();

            DzikirModel[] data = gson.fromJson(reader, DzikirModel[].class);
            for (DzikirModel model : data) {
                if (model.getKategori().equalsIgnoreCase(kategori)) {
                    dzikirList.add(model);
                }
            }

            reader.close();

            if (!dzikirList.isEmpty()) {
                pagerAdapter = new DzikirPagerAdapter(getSupportFragmentManager(), dzikirList);
                viewPager.setAdapter(pagerAdapter);


                pagerIndicatorText.setVisibility(View.VISIBLE);
                pagerIndicatorText.setText("1 / " + dzikirList.size());

                viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

                    @Override
                    public void onPageSelected(int position) {
                        pagerIndicatorText.setText((position + 1) + " / " + dzikirList.size());
                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {}
                });
            } else {
                pagerIndicatorText.setVisibility(View.GONE); // kalau kosong
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}