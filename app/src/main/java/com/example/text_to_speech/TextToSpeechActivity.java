package com.example.text_to_speech;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class TextToSpeechActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private EditText etInputText;
    private Spinner spinnerLanguage;
    private SeekBar seekBarSpeed;
    private MaterialButton btnSpeak;
    private TextView tvOutputText;

    private TextToSpeech textToSpeech;
    private final HashMap<String, Locale> languageMap = new HashMap<>();
    private float speechRate = 1.0f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_to_speech);

        // Initialize UI components
        initializeViews();
        
        // Setup toolbar
        setupToolbar();
        
        // Initialize Text-to-Speech engine
        initializeTextToSpeech();
        
        // Setup language spinner
        setupLanguageSpinner();
        
        // Setup speech rate seekbar
        setupSpeechRateSeekBar();
        
        // Setup speak button
        setupSpeakButton();
    }

    private void initializeViews() {
        toolbar = findViewById(R.id.toolbar);
        etInputText = findViewById(R.id.etInputText);
        spinnerLanguage = findViewById(R.id.spinnerLanguage);
        seekBarSpeed = findViewById(R.id.seekBarSpeed);
        btnSpeak = findViewById(R.id.btnSpeak);
        tvOutputText = findViewById(R.id.tvOutputText);
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    private void initializeTextToSpeech() {
        textToSpeech = new TextToSpeech(getApplicationContext(), status -> {
            if (status != TextToSpeech.ERROR) {
                textToSpeech.setLanguage(Locale.US);
            } else {
                Toast.makeText(TextToSpeechActivity.this, 
                        getString(R.string.tts_init_failed), 
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupLanguageSpinner() {
        // Populate language map
        languageMap.put("English (US)", Locale.US);
        languageMap.put("English (UK)", Locale.UK);
        languageMap.put("French", Locale.FRANCE);
        languageMap.put("German", Locale.GERMANY);
        languageMap.put("Italian", Locale.ITALY);
        languageMap.put("Japanese", Locale.JAPAN);
        languageMap.put("Korean", Locale.KOREA);
        languageMap.put("Chinese", Locale.CHINA);

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
                Locale locale = languageMap.get(selectedLanguage);
                if (locale != null) {
                    int result = textToSpeech.setLanguage(locale);
                    if (result == TextToSpeech.LANG_MISSING_DATA || 
                            result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Toast.makeText(TextToSpeechActivity.this, 
                                getString(R.string.language_not_supported), 
                                Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });
    }

    private void setupSpeechRateSeekBar() {
        seekBarSpeed.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                speechRate = 0.5f + ((float) progress / 100) * 1.5f; // Range from 0.5 to 2.0
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Do nothing
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // Do nothing
            }
        });
    }

    private void setupSpeakButton() {
        btnSpeak.setOnClickListener(v -> {
            String text = etInputText.getText().toString().trim();
            if (text.isEmpty()) {
                Toast.makeText(TextToSpeechActivity.this, 
                        getString(R.string.enter_text_to_speak), 
                        Toast.LENGTH_SHORT).show();
                return;
            }
            
            textToSpeech.setSpeechRate(speechRate);
            textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
            tvOutputText.setText(text);
        });
    }

    @Override
    protected void onDestroy() {
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        super.onDestroy();
    }
}
