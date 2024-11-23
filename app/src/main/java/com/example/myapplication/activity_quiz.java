package com.example.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class activity_quiz extends AppCompatActivity {

    private TextView questionText;
    private Button option1, option2, option3, option4;
    private int correctAnswer;
    private int score = 0;
    private int questionCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        questionText = findViewById(R.id.questionText);
        option1 = findViewById(R.id.option1);
        option2 = findViewById(R.id.option2);
        option3 = findViewById(R.id.option3);
        option4 = findViewById(R.id.option4);

        generateQuestion();

        View.OnClickListener answerListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button clickedButton = (Button) v;
                int answer = Integer.parseInt(clickedButton.getText().toString());
                if (answer == correctAnswer) {
                    score++;
                    Toast.makeText(activity_quiz.this, "Correct!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(activity_quiz.this, "Wrong!", Toast.LENGTH_SHORT).show();
                }

                questionCount++;
                if (questionCount < 10) {
                    generateQuestion();
                } else {
                    Toast.makeText(activity_quiz.this, "Quiz Over! Your Score: " + score, Toast.LENGTH_LONG).show();
                    finish(); // End quiz and return to main screen
                }
            }
        };

        option1.setOnClickListener(answerListener);
        option2.setOnClickListener(answerListener);
        option3.setOnClickListener(answerListener);
        option4.setOnClickListener(answerListener);
    }

    private void generateQuestion() {
        Random random = new Random();
        int table = random.nextInt(10) + 1; // Random table between 1-10
        int num = random.nextInt(10) + 1; // Random number between 1-10
        correctAnswer = table * num;

        questionText.setText("What is " + table + " x " + num + "?");

        int correctOption = random.nextInt(4); // Randomize correct option
        int[] options = new int[4];
        options[correctOption] = correctAnswer;

        for (int i = 0; i < 4; i++) {
            if (i != correctOption) {
                int wrongAnswer;
                do {
                    wrongAnswer = random.nextInt(100) + 1; // Random wrong answer
                } while (wrongAnswer == correctAnswer);
                options[i] = wrongAnswer;
            }
        }

        option1.setText(String.valueOf(options[0]));
        option2.setText(String.valueOf(options[1]));
        option3.setText(String.valueOf(options[2]));
        option4.setText(String.valueOf(options[3]));
    }
}
