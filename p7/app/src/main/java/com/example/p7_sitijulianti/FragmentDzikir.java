package com.example.p7_sitijulianti;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class FragmentDzikir extends Fragment {

    private static final String ARG_ARAB = "arab";
    private static final String ARG_LATIN = "latin";
    private static final String ARG_ARTI = "arti";
    private static final String ARG_JUMLAH = "jumlah";
    private static final String ARG_JUDUL = "judul";
    private static final String ARG_KATEGORI = "kategori";
    private static final String ARG_AUDIO = "audio";

    private String arab, latin, arti, judul, kategori, audio;
    private int jumlah;

    private int counter = 0;
    private boolean isPlaying = false;
    private boolean isZoomed = false;
    private boolean isLatinVisible = true;
    private MediaPlayer mediaPlayer;

    // komponen UI
    private TextView txtArab, txtLatin, txtArti, txtCounter;
    private TextView txtJudul, txtJumlahBacaan;
    private ImageButton btnTambah, btnReset, btnPlay, btnZoom, btnToggleLatin, btnCopy;
    private Toolbar toolbar;

    public FragmentDzikir() {
        // wajib constructor kosong
    }

    public static FragmentDzikir newInstance(DzikirModel model) {
        FragmentDzikir fragment = new FragmentDzikir();
        Bundle args = new Bundle();
        args.putString(ARG_ARAB, model.getArab());
        args.putString(ARG_LATIN, model.getLatin());
        args.putString(ARG_ARTI, model.getArti());
        args.putInt(ARG_JUMLAH, model.getJumlah());
        args.putString(ARG_KATEGORI, model.getKategori());
        args.putString(ARG_AUDIO, model.getAudio());
        args.putString(ARG_JUDUL, model.getJudul());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dzikir, container, false);

        // Inisialisasi komponen tampilan
        txtArab = view.findViewById(R.id.txtArab);
        txtLatin = view.findViewById(R.id.txtLatin);
        txtArti = view.findViewById(R.id.txtArti);
        txtCounter = view.findViewById(R.id.txtCounter);
        txtJudul = view.findViewById(R.id.txtJudul);
        txtJumlahBacaan = view.findViewById(R.id.txtJumlahBacaan);
        btnTambah = view.findViewById(R.id.btnTambah);
        btnReset = view.findViewById(R.id.btnre);
        btnPlay = view.findViewById(R.id.btnPlay);
        btnZoom = view.findViewById(R.id.btnZoom);
        btnToggleLatin = view.findViewById(R.id.btnToggleLatin);
        btnCopy = view.findViewById(R.id.btnCopy);
        toolbar = view.findViewById(R.id.back_toolbar);

        // Ambil data dari Bundle
        // Ambil data dari Bundle
        if (getArguments() != null) {
            arab = getArguments().getString(ARG_ARAB);
            latin = getArguments().getString(ARG_LATIN);
            arti = getArguments().getString(ARG_ARTI);
            jumlah = getArguments().getInt(ARG_JUMLAH);
            judul = getArguments().getString(ARG_JUDUL);
            kategori = getArguments().getString(ARG_KATEGORI);
            audio = getArguments().getString(ARG_AUDIO);

            // ✅ BERSIHKAN TAG <br> DAN SPASI GANDA DARI LATIN & ARTI
            if (latin != null) {
                latin = latin.replace("<br>", "\n").replaceAll("\\s{2,}", " ").trim();
            }

            if (arti != null) {
                arti = arti.replace("<br>", "\n").replaceAll("\\s{2,}", " ").trim();
            }

            // ✅ SET TEXT KE UI
            txtArab.setText(arab);
            txtLatin.setText(latin);
            txtArti.setText(arti);
            txtJudul.setText(judul);
            txtJumlahBacaan.setText("Dibaca " + jumlah + "x");

            if (kategori != null) {
                if (kategori.equals("pagi")) {
                    toolbar.setTitle("Dzikir Pagi");
                } else if (kategori.equals("petang")) {
                    toolbar.setTitle("Dzikir Petang");
                } else {
                    toolbar.setTitle("Dzikir");
                }
            }
            toolbar.setNavigationOnClickListener(v -> requireActivity().onBackPressed());
        }


        txtCounter.setText(String.valueOf(counter));

        btnTambah.setOnClickListener(v -> {
            counter++;
            txtCounter.setText(String.valueOf(counter));
        });

        btnReset.setOnClickListener(v -> {
            counter = 0;
            txtCounter.setText(String.valueOf(counter));
        });

        btnPlay.setOnClickListener(v -> {
            try {
                if (mediaPlayer == null) {
                    int resId = getResources().getIdentifier(audio, "raw", requireContext().getPackageName());
                    if (resId == 0) {
                        Toast.makeText(getContext(), "Audio tidak ditemukan", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    mediaPlayer = MediaPlayer.create(getContext(), resId);
                    mediaPlayer.setOnCompletionListener(mp -> {
                        isPlaying = false;
                        btnPlay.setImageResource(R.drawable.ic_media_play);
                    });
                }

                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                    isPlaying = false;
                    btnPlay.setImageResource(R.drawable.ic_media_play);
                } else {
                    mediaPlayer.start();
                    isPlaying = true;
                    btnPlay.setImageResource(R.drawable.ic_baseline_pause_circle_outline_24);
                }
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getContext(), "Gagal memutar audio", Toast.LENGTH_SHORT).show();
            }
        });

        btnZoom.setOnClickListener(v -> {
            if (isZoomed) {
                // Ukuran teks normal
                txtArab.setTextSize(28f);
                txtLatin.setTextSize(18f);
                txtArti.setTextSize(16f);

                // Ganti ikon ke zoom in
                btnZoom.setImageResource(R.drawable.ic_baseline_zoom_in);
            } else {
                // Perbesar teks
                txtArab.setTextSize(36f);
                txtLatin.setTextSize(22f);
                txtArti.setTextSize(18f);

                // Ganti ikon ke zoom out
                btnZoom.setImageResource(R.drawable.ic_baseline_zoom);
            }

            // Toggle status
            isZoomed = !isZoomed;
        });


        btnToggleLatin.setOnClickListener(v -> {
            txtLatin.setVisibility(isLatinVisible ? View.GONE : View.VISIBLE);
            isLatinVisible = !isLatinVisible;
        });

        btnCopy.setOnClickListener(v -> {
            String teks = arab + "\n\n" + latin + "\n\n" + arti;
            ClipboardManager clipboard = (ClipboardManager) requireContext().getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("Dzikir", teks);
            clipboard.setPrimaryClip(clip);
            Toast.makeText(getContext(), "Teks dzikir disalin", Toast.LENGTH_SHORT).show();
        });

        if (jumlah == 1) {
            txtCounter.setVisibility(View.GONE);
            btnTambah.setVisibility(View.GONE);
            btnReset.setVisibility(View.GONE);
        }

        return view;
    }

    @Override
    public void onDestroy() {
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
        super.onDestroy();
    }

}