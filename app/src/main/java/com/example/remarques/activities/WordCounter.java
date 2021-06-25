package com.example.remarques.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.remarques.R;

public class WordCounter extends AppCompatActivity {

    EditText enterText;
    ImageView imageView;
    TextView characterText;
    String gettingText;
    int numberText;
    String[] words;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.word_counter);

        enterText = findViewById(R.id.textEnter);
        characterText = findViewById(R.id.characterCounter);
        imageView = findViewById(R.id.imageBack);

        enterText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @SuppressLint("SetTextI18n")
            @Override
            public void afterTextChanged(Editable s) {
                numberText = s.length();

                words = s.toString().split("\\s");

                characterText.setText("Character Count :  "+numberText+"\n\n\nWord Count :  "+words.length);
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });




    }
}