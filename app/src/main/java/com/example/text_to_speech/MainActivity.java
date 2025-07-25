package com.example.text_to_speech;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private CardView cardTextToSpeech, cardSpeechToText, cardPdfReader, cardAudioExtractor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        // Make sure status bar is visible with proper styling
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), true);
        
        // Initialize UI components
        initializeViews();
        
        // Set click listeners
        setClickListeners();
    }
    
    private void initializeViews() {
        cardTextToSpeech = findViewById(R.id.cardTextToSpeech);
        cardSpeechToText = findViewById(R.id.cardSpeechToText);
        cardPdfReader = findViewById(R.id.cardPdfReader);
        cardAudioExtractor = findViewById(R.id.cardAudioExtractor);
    }
    
    private void setClickListeners() {
        cardTextToSpeech.setOnClickListener(this);
        cardSpeechToText.setOnClickListener(this);
        cardPdfReader.setOnClickListener(this);
        cardAudioExtractor.setOnClickListener(this);
    }
    


    @Override
    public void onClick(View v) {
        Intent intent = null;
        
        if (v.getId() == R.id.cardTextToSpeech) {
            intent = new Intent(MainActivity.this, TextToSpeechActivity.class);
        } else if (v.getId() == R.id.cardSpeechToText) {
            intent = new Intent(MainActivity.this, SpeechToTextActivity.class);
        } else if (v.getId() == R.id.cardPdfReader) {
            intent = new Intent(MainActivity.this, PdfReaderActivity.class);
        } else if (v.getId() == R.id.cardAudioExtractor) {
            intent = new Intent(MainActivity.this, AudioExtractorActivity.class);
        }
        
        if (intent != null) {
            startActivity(intent);
        }
    }
}