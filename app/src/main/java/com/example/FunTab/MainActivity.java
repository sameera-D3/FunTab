package com.example.FunTab;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private TextToSpeech textToSpeech;
    private int selectedTable = 2; // Default table
    private TextView txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize UI elements
        Spinner spinnerTables = findViewById(R.id.spinner_tables);
        Button btnSpeakTable = findViewById(R.id.btn_speak_table);
        Button btnStartQuiz = findViewById(R.id.btn_start_quiz);
        txt = findViewById(R.id.txt1);

        // Initialize Text-to-Speech
        textToSpeech = new TextToSpeech(this, status -> {
            if (status == TextToSpeech.SUCCESS) {
                textToSpeech.setLanguage(Locale.US);
                textToSpeech.setOnUtteranceProgressListener(new UtteranceProgressListener() {
                    @Override
                    public void onStart(String utteranceId) {
                        int currentStep = Integer.parseInt(utteranceId);
                        runOnUiThread(() -> txt.setText(getWrittenText(selectedTable, currentStep)));
                    }

                    @Override
                    public void onDone(String utteranceId) {
                        int nextStep = Integer.parseInt(utteranceId) + 1;
                        if (nextStep <= 10) {
                            speakLine(getSpeakableText(selectedTable, nextStep), nextStep);
                        }
                    }

                    @Override
                    public void onError(String utteranceId) {
                        runOnUiThread(() -> Toast.makeText(MainActivity.this,
                                "Error speaking multiplication step", Toast.LENGTH_SHORT).show());
                    }
                });
            } else {
                Toast.makeText(MainActivity.this, "TTS Initialization Failed!", Toast.LENGTH_SHORT).show();
            }
        });

        // Handle spinner item selection
        spinnerTables.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedTable = Integer.parseInt(parent.getItemAtPosition(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

        // Speak table button functionality
        btnSpeakTable.setOnClickListener(v -> {
            if (textToSpeech != null) {
                speakTable(selectedTable);
            }
        });

        // Start quiz button functionality
        btnStartQuiz.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, activity_quiz.class); // Update to match your quiz activity class
            startActivity(intent);
        });

        // Add animation for cloud
        ImageView cloud = findViewById(R.id.cloud1);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.floating);
        cloud.startAnimation(animation);
    }

    // Function to generate and speak the selected table
    private void speakTable(int table) {
        txt.setText("");
        speakLine(getSpeakableText(table, 1), 1);
    }

    // Function to get the written multiplication text for the TextView (normal format)
    private String getWrittenText(int table, int step) {
        return table + " x " + step + " = " + (table * step);
    }

    // Function to get the custom speakable text for TTS
    private String getSpeakableText(int table, int step) {
        String suffix = "";
        switch (step) {
            case 1: suffix = "onza"; break;
            case 2: suffix = "tooza"; break;
            case 3: suffix = "triza"; break;
            case 4: suffix = "fourza"; break;
            case 5: suffix = "fivza"; break;
            case 6: suffix = "sixza"; break;
            case 7: suffix = "sevenza"; break;
            case 8: suffix = "eightza"; break;
            case 9: suffix = "nainza"; break;
            case 10: suffix = "tenza"; break;
        }
        return table + " " + suffix + " " + (table * step);
    }

    // Function to speak a multiplication line
    private void speakLine(String line, int index) {
        if (textToSpeech != null) {
            textToSpeech.speak(line, TextToSpeech.QUEUE_FLUSH, null, String.valueOf(index));
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
