package com.sopyan.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class KonversiSuhuActivity extends AppCompatActivity {

    private EditText etInputSuhu;
    private Spinner spinnerDari, spinnerKe;
    private Button btnKonversi, btnKembali;
    private TextView tvHasil;
    private Spinner spinnerKonversi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_konversi_suhu);

        // Inisialisasi view
        etInputSuhu = findViewById(R.id.etInputSuhu);
//        spinnerDari = findViewById(R.id.spinnerDari);
//        spinnerKe = findViewById(R.id.spinnerKe);
        btnKonversi = findViewById(R.id.btnKonversi);
        btnKembali = findViewById(R.id.btnKembali);
        tvHasil = findViewById(R.id.tvHasil);
        spinnerKonversi = findViewById(R.id.spinnerKonversi);

        // Setup spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.suhu_units, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinnerDari.setAdapter(adapter);
//        spinnerKe.setAdapter(adapter);
        spinnerKonversi.setAdapter(adapter);

        // Button konversi click listener
        btnKonversi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                konversiSuhu();
            }
        });

        // Button kembali click listener
        btnKembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void konversiSuhu() {
        String inputStr = etInputSuhu.getText().toString();

        if (inputStr.isEmpty()) {
            Toast.makeText(this, "Masukkan nilai suhu terlebih dahulu", Toast.LENGTH_SHORT).show();
            return;
        }

        double input = Double.parseDouble(inputStr);
        String selectedConversion = spinnerKonversi.getSelectedItem().toString();
        double hasil = 0;

        switch (selectedConversion) {
            case "Celcius ke Fahrenheit":
                hasil = (input * 9/5) + 32;
                break;
            case "Celcius ke Kelvin":
                hasil = input + 273.15;
                break;
            case "Celcius ke Reamur":
                hasil = input * 4/5;
                break;
            case "Fahrenheit ke Celcius":
                hasil = (input - 32) * 5/9;
                break;
            case "Kelvin ke Celcius":
                hasil = input - 273.15;
                break;
            case "Reamur ke Celcius":
                hasil = input * 5/4;
                break;
            case "Fahrenheit ke Kelvin":
                hasil = (input - 32) * 5/9 + 273.15;
                break;
            case "Kelvin ke Fahrenheit":
                hasil = (input - 273.15) * 9/5 + 32;
                break;
            case "Reamur ke Fahrenheit":
                hasil = (input * 9/4) + 32;
                break;
            case "Fahrenheit ke Reamur":
                hasil = (input - 32) * 4/9;
                break;
            case "Kelvin ke Reamur":
                hasil = (input - 273.15) * 4/5;
                break;
            case "Reamur ke Kelvin":
                hasil = (input * 5/4) + 273.15;
                break;
        }

        String hasilStr = String.format("%.2f menjadi %.2f", input, hasil);
        tvHasil.setText(hasilStr);
    }
}