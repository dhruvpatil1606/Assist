package com.example.text_to_speech;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.button.MaterialButton;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;

import java.io.InputStream;
import java.util.Locale;

public class PdfReaderActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView tvSelectedFileName;
    private TextView tvExtractedText;
    private MaterialButton btnSelectPdf;
    private MaterialButton btnReadText;

    private TextToSpeech textToSpeech;
    private String extractedText = "";

    private final ActivityResultLauncher<Intent> pdfPickerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    Uri selectedPdfUri = result.getData().getData();
                    if (selectedPdfUri != null) {
                        String fileName = getFileNameFromUri(selectedPdfUri);
                        tvSelectedFileName.setText(fileName);
                        extractTextFromPdf(selectedPdfUri);
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_reader);

        // Initialize UI components
        initializeViews();
        
        // Setup toolbar
        setupToolbar();
        
        // Initialize Text-to-Speech engine
        initializeTextToSpeech();
        
        // Setup PDF selection button
        setupPdfSelectionButton();
        
        // Setup read text button
        setupReadTextButton();
    }

    private void initializeViews() {
        toolbar = findViewById(R.id.toolbar);
        tvSelectedFileName = findViewById(R.id.tvSelectedFileName);
        tvExtractedText = findViewById(R.id.tvExtractedText);
        btnSelectPdf = findViewById(R.id.btnSelectPdf);
        btnReadText = findViewById(R.id.btnReadText);
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
                Toast.makeText(PdfReaderActivity.this, 
                        getString(R.string.tts_init_failed), 
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupPdfSelectionButton() {
        btnSelectPdf.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("application/pdf");
            pdfPickerLauncher.launch(intent);
        });
    }

    private void setupReadTextButton() {
        btnReadText.setOnClickListener(v -> {
            if (!extractedText.isEmpty()) {
                textToSpeech.speak(extractedText, TextToSpeech.QUEUE_FLUSH, null, null);
            } else {
                Toast.makeText(PdfReaderActivity.this, 
                        getString(R.string.no_text_to_read), 
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String getFileNameFromUri(Uri uri) {
        String result = uri.getPath();
        int cut = result.lastIndexOf('/');
        if (cut != -1) {
            result = result.substring(cut + 1);
        }
        return result;
    }

    private void extractTextFromPdf(Uri pdfUri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(pdfUri);
            PdfReader reader = new PdfReader(inputStream);
            StringBuilder textBuilder = new StringBuilder();
            
            int pages = reader.getNumberOfPages();
            for (int i = 1; i <= pages; i++) {
                textBuilder.append(PdfTextExtractor.getTextFromPage(reader, i)).append("\n");
                
                // Show progress for large PDFs
                if (i % 10 == 0 || i == pages) {
                    Toast.makeText(this, getString(R.string.extracting_progress, i, pages), 
                            Toast.LENGTH_SHORT).show();
                }
            }
            
            extractedText = textBuilder.toString();
            tvExtractedText.setText(extractedText);
            btnReadText.setEnabled(true);
            
            reader.close();
            if (inputStream != null) {
                inputStream.close();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Error extracting text: " + e.getMessage(), 
                    Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
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
