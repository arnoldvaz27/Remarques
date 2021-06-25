package com.example.remarques.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;

import com.example.remarques.R;

public class AppSettings extends AppCompatActivity {

    private CheckBox font1,font2,font3,font4,font5,font6,richTextBox,detailLayout,normalLayout;
    private String fontSettings,richText,layoutType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_settings);
        getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        font1 = findViewById(R.id.font1);
        font2 = findViewById(R.id.font2);
        font3 = findViewById(R.id.font3);
        font4 = findViewById(R.id.font4);
        font5 = findViewById(R.id.font5);
        font6 = findViewById(R.id.font6);
        detailLayout = findViewById(R.id.detailLayout);
        normalLayout = findViewById(R.id.normalLayout);
        richTextBox = findViewById(R.id.richText);

        SharedPreferences getShared3 = getSharedPreferences("settings", MODE_PRIVATE);
        richText = getShared3.getString("rich text", null);

        SharedPreferences getShared2 = getSharedPreferences("settings", MODE_PRIVATE);
        fontSettings = getShared2.getString("font number", null);

        SharedPreferences getShared6 = getSharedPreferences("settings", MODE_PRIVATE);
        layoutType = getShared6.getString("layout type", null);

        checkRichText();

        checkFont();

        checkLayoutType();

        choosingFont();

        choosingRichText();

        choosingLayoutType();
    }

    private void checkRichText() {
        switch (richText) {
            case "1":
                richTextBox.setChecked(false);
                break;
            case "2":
                richTextBox.setChecked(true);
                break;
        }
    }

    private void choosingLayoutType() {
        final SharedPreferences shard6 = getSharedPreferences("settings",MODE_PRIVATE);
        final SharedPreferences.Editor editor2 = shard6.edit();
        normalLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(normalLayout.isChecked())
                {
                    normalLayout.setChecked(true);
                    detailLayout.setChecked(false);
                    editor2.putString("layout type", "1");
                    editor2.apply();
                }
            }
        });
        detailLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(detailLayout.isChecked())
                {
                    detailLayout.setChecked(true);
                    normalLayout.setChecked(false);
                    editor2.putString("layout type", "2");
                    editor2.apply();
                }
            }
        });
    }

    private void checkLayoutType() {
        switch (layoutType) {
            case "1":
                normalLayout.setChecked(true);
                detailLayout.setChecked(false);
                break;
            case "2":
                detailLayout.setChecked(true);
                normalLayout.setChecked(false);
                break;
        }
    }

    private void choosingRichText() {
        final SharedPreferences shard2 = getSharedPreferences("settings",MODE_PRIVATE);
        final SharedPreferences.Editor editor2 = shard2.edit();
        richTextBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(richTextBox.isChecked())
                {
                    editor2.putString("rich text", "2");
                }
                else
                {
                    editor2.putString("rich text", "1");
                }
                editor2.apply();
            }
        });
    }

    private void choosingFont() {
        final SharedPreferences shard2 = getSharedPreferences("settings",MODE_PRIVATE);
        final SharedPreferences.Editor editor2 = shard2.edit();
        font1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                font1.setChecked(true);
                font2.setChecked(false);
                font3.setChecked(false);
                font4.setChecked(false);
                font5.setChecked(false);
                font6.setChecked(false);
                editor2.putString("font number", "1");
                editor2.apply();
            }
        });
        font2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                font2.setChecked(true);
                font1.setChecked(false);
                font3.setChecked(false);
                font4.setChecked(false);
                font5.setChecked(false);
                font6.setChecked(false);
                editor2.putString("font number", "2");
                editor2.apply();
            }
        });
        font3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                font3.setChecked(true);
                font1.setChecked(false);
                font2.setChecked(false);
                font4.setChecked(false);
                font5.setChecked(false);
                font6.setChecked(false);
                editor2.putString("font number", "3");
                editor2.apply();
            }
        });
        font4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                font4.setChecked(true);
                font1.setChecked(false);
                font2.setChecked(false);
                font3.setChecked(false);
                font5.setChecked(false);
                font6.setChecked(false);
                editor2.putString("font number", "4");
                editor2.apply();
            }
        });
        font5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                font5.setChecked(true);
                font1.setChecked(false);
                font2.setChecked(false);
                font3.setChecked(false);
                font4.setChecked(false);
                font6.setChecked(false);
                editor2.putString("font number", "5");
                editor2.apply();

            }
        });
        font6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                font6.setChecked(true);
                font1.setChecked(false);
                font2.setChecked(false);
                font3.setChecked(false);
                font4.setChecked(false);
                font5.setChecked(false);
                editor2.putString("font number", "6");
                editor2.apply();
            }
        });
    }

    private void checkFont() {
        switch (fontSettings) {
            case "1":
                font1.setChecked(true);
                font2.setChecked(false);
                font3.setChecked(false);
                font4.setChecked(false);
                font5.setChecked(false);
                font6.setChecked(false);
                break;
            case "2":
                font2.setChecked(true);
                font1.setChecked(false);
                font3.setChecked(false);
                font4.setChecked(false);
                font5.setChecked(false);
                font6.setChecked(false);
                break;
            case "3":
                font2.setChecked(false);
                font1.setChecked(false);
                font3.setChecked(true);
                font4.setChecked(false);
                font5.setChecked(false);
                font6.setChecked(false);
                break;
            case "4":
                font4.setChecked(true);
                font1.setChecked(false);
                font2.setChecked(false);
                font3.setChecked(false);
                font5.setChecked(false);
                font6.setChecked(false);
                break;
            case "5":
                font5.setChecked(true);
                font1.setChecked(false);
                font2.setChecked(false);
                font3.setChecked(false);
                font4.setChecked(false);
                font6.setChecked(false);
                break;
            case "6":
                font6.setChecked(true);
                font1.setChecked(false);
                font2.setChecked(false);
                font3.setChecked(false);
                font4.setChecked(false);
                font5.setChecked(false);
                break;
        }
    }
}