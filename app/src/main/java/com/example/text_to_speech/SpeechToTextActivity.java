package com.example.text_to_speech;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class SpeechToTextActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private FloatingActionButton fabRecord;
    private TextView tvRecordingStatus;
    private TextView tvRecognizedText;
    private Spinner spinnerLanguage;
    private MaterialButton btnClear;

    private final HashMap<String, String> languageMap = new HashMap<>();
    private String currentLanguageCode = "en-US";

    private final ActivityResultLauncher<Intent> speechRecognitionLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    ArrayList<String> matches = result.getData().getStringArrayListExtra(
                            RecognizerIntent.EXTRA_RESULTS);
                    if (matches != null && !matches.isEmpty()) {
                        String recognizedText = matches.get(0);
                        String currentText = tvRecognizedText.getText().toString();
                        
                        if (!currentText.isEmpty()) {
                            currentText += "\n\n";
                        }
                        
                        tvRecognizedText.setText(currentText + recognizedText);
                        tvRecordingStatus.setText(getString(R.string.tap_to_start_recording));
                    }
                } else {
                    tvRecordingStatus.setText(getString(R.string.tap_to_start_recording));
                    Toast.makeText(this, getString(R.string.speech_recognition_failed), Toast.LENGTH_SHORT).show();
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speech_to_text);

        // Initialize UI components
        initializeViews();
        
        // Setup toolbar
        setupToolbar();
        
        // Setup language spinner
        setupLanguageSpinner();
        
        // Setup record button
        setupRecordButton();
        
        // Setup clear button
        setupClearButton();
    }

    private void initializeViews() {
        toolbar = findViewById(R.id.toolbar);
        fabRecord = findViewById(R.id.fabRecord);
        tvRecordingStatus = findViewById(R.id.tvRecordingStatus);
        tvRecognizedText = findViewById(R.id.tvRecognizedText);
        spinnerLanguage = findViewById(R.id.spinnerLanguage);
        btnClear = findViewById(R.id.btnClear);
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    private void setupLanguageSpinner() {
        // Populate language map
        languageMap.put("English (US)", "en-US");
        languageMap.put("English (UK)", "en-GB");
        languageMap.put("French", "fr-FR");
        languageMap.put("German", "de-DE");
        languageMap.put("Italian", "it-IT");
        languageMap.put("Japanese", "ja-JP");
        languageMap.put("Korean", "ko-KR");
        languageMap.put("Chinese", "zh-CN");

        // Create language list for spinner
        List<String> languageList = new ArrayList<>(languageMap.keySet());
        
        // Create adapter for spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, languageList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        
        // Set adapter to spinner
        spinnerLanguage.setAdapter(adapter);
        
        // Set listener for spinner
        spinnerLanguage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedLanguage = parent.getItemAtPosition(position).toString();
                String languageCode = languageMap.get(selectedLanguage);
                if (languageCode != null) {
                    currentLanguageCode = languageCode;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });
    }

    private void setupRecordButton() {
        fabRecord.setOnClickListener(v -> {
            tvRecordingStatus.setText(getString(R.string.listening));
            startSpeechRecognition();
        });
    }

    private void setupClearButton() {
        btnClear.setOnClickListener(v -> tvRecognizedText.setText(""));
    }

    private void startSpeechRecognition() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, currentLanguageCode);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, getString(R.string.speak_now_prompt));
        
        try {
            speechRecognitionLauncher.launch(intent);
        } catch (Exception e) {
            Toast.makeText(this, getString(R.string.speech_not_supported), 
                    Toast.LENGTH_SHORT).show();
            tvRecordingStatus.setText(getString(R.string.tap_to_start_recording));
        }
    }
}
