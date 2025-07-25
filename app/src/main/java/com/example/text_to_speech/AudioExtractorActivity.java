package com.example.text_to_speech;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.button.MaterialButton;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class AudioExtractorActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView tvSelectedAudioName;
    private TextView tvExtractedText;
    private TextView tvCurrentTime;
    private TextView tvTotalTime;
    private SeekBar seekBarAudio;
    private ImageButton btnPlayPause;
    private ImageButton btnStop;
    private MaterialButton btnSelectAudio;
    private MaterialButton btnExtractText;

    private MediaPlayer mediaPlayer;
    private Handler handler;
    private Runnable runnable;
    private Uri selectedAudioUri;
    private boolean isPlaying = false;

    private final ActivityResultLauncher<Intent> audioPickerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    Uri uri = result.getData().getData();
                    if (uri != null) {
                        selectedAudioUri = uri;
                        String fileName = getFileNameFromUri(uri);
                        tvSelectedAudioName.setText(fileName);
                        setupMediaPlayer(uri);
                        btnExtractText.setEnabled(true);
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_extractor);

        // Initialize UI components
        initializeViews();
        
        // Setup toolbar
        setupToolbar();
        
        // Initialize handler for seekbar updates
        handler = new Handler();
        
        // Setup audio selection button
        setupAudioSelectionButton();
        
        // Setup media control buttons
        setupMediaControlButtons();
        
        // Setup extract text button
        setupExtractTextButton();
    }

    private void initializeViews() {
        toolbar = findViewById(R.id.toolbar);
        tvSelectedAudioName = findViewById(R.id.tvSelectedAudioName);
        tvExtractedText = findViewById(R.id.tvExtractedText);
        tvCurrentTime = findViewById(R.id.tvCurrentTime);
        tvTotalTime = findViewById(R.id.tvTotalTime);
        seekBarAudio = findViewById(R.id.seekBarAudio);
        btnPlayPause = findViewById(R.id.btnPlayPause);
        btnStop = findViewById(R.id.btnStop);
        btnSelectAudio = findViewById(R.id.btnSelectAudio);
        btnExtractText = findViewById(R.id.btnExtractText);
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    private void setupAudioSelectionButton() {
        btnSelectAudio.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("audio/*");
            audioPickerLauncher.launch(intent);
        });
    }

    private void setupMediaPlayer(Uri uri) {
        try {
            if (mediaPlayer != null) {
                mediaPlayer.release();
            }
            
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(getApplicationContext(), uri);
            mediaPlayer.prepare();
            
            // Set total duration
            int totalDuration = mediaPlayer.getDuration();
            tvTotalTime.setText(formatDuration(totalDuration));
            seekBarAudio.setMax(totalDuration);
            
            // Set seekbar change listener
            seekBarAudio.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    if (fromUser && mediaPlayer != null) {
                        mediaPlayer.seekTo(progress);
                        tvCurrentTime.setText(formatDuration(progress));
                    }
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
            
            // Set completion listener
            mediaPlayer.setOnCompletionListener(mp -> {
                btnPlayPause.setImageResource(R.drawable.ic_play);
                isPlaying = false;
                seekBarAudio.setProgress(0);
                tvCurrentTime.setText("00:00");
                handler.removeCallbacks(runnable);
            });
            
        } catch (IOException e) {
            Toast.makeText(this, getString(R.string.error_playing_audio, e.getMessage()), 
                    Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void setupMediaControlButtons() {
        btnPlayPause.setOnClickListener(v -> {
            if (mediaPlayer != null) {
                if (isPlaying) {
                    mediaPlayer.pause();
                    btnPlayPause.setImageResource(R.drawable.ic_play);
                    handler.removeCallbacks(runnable);
                } else {
                    mediaPlayer.start();
                    btnPlayPause.setImageResource(android.R.drawable.ic_media_pause);
                    updateSeekBar();
                }
                isPlaying = !isPlaying;
            }
        });
        
        btnStop.setOnClickListener(v -> {
            if (mediaPlayer != null) {
                mediaPlayer.stop();
                try {
                    mediaPlayer.prepare();
                    seekBarAudio.setProgress(0);
                    tvCurrentTime.setText("00:00");
                    btnPlayPause.setImageResource(R.drawable.ic_play);
                    isPlaying = false;
                    handler.removeCallbacks(runnable);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void setupExtractTextButton() {
        btnExtractText.setOnClickListener(v -> {
            if (selectedAudioUri != null) {
                // In a real app, we would use a speech recognition API or service
                // For this demo, we'll simulate text extraction with a speech recognizer
                simulateAudioTextExtraction();
            } else {
                Toast.makeText(this, getString(R.string.select_audio_first), 
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void simulateAudioTextExtraction() {
        // In a real app, this would connect to a speech recognition service
        // For demo purposes, we'll show a message and some sample extracted text
        Toast.makeText(this, getString(R.string.processing_audio), Toast.LENGTH_SHORT).show();
        
        // Simulate processing delay
        new Handler().postDelayed(() -> {
            // Sample extracted text
            String sampleText = "This is a simulation of text extracted from an audio file. " +
                    "In a real application, this would use a speech recognition API like " +
                    "Google Cloud Speech-to-Text or similar services to convert the audio " +
                    "to text. The actual implementation would require API keys and network " +
                    "connectivity to send the audio to the recognition service.";
            
            tvExtractedText.setText(sampleText);
            Toast.makeText(AudioExtractorActivity.this, 
                    getString(R.string.text_extraction_completed), Toast.LENGTH_SHORT).show();
        }, 2000);
    }

    private void updateSeekBar() {
        if (mediaPlayer != null) {
            int currentPosition = mediaPlayer.getCurrentPosition();
            seekBarAudio.setProgress(currentPosition);
            tvCurrentTime.setText(formatDuration(currentPosition));
            
            runnable = () -> updateSeekBar();
            handler.postDelayed(runnable, 1000);
        }
    }

    private String formatDuration(int duration) {
        return String.format(Locale.getDefault(), "%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(duration),
                TimeUnit.MILLISECONDS.toSeconds(duration) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration)));
    }

    private String getFileNameFromUri(Uri uri) {
        String result = uri.getPath();
        int cut = result.lastIndexOf('/');
        if (cut != -1) {
            result = result.substring(cut + 1);
        }
        return result;
    }

    @Override
    protected void onDestroy() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        if (handler != null) {
            handler.removeCallbacks(runnable);
        }
        super.onDestroy();
    }
}
