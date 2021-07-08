package com.example.remarques.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.speech.tts.TextToSpeech;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.remarques.R;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.BarcodeQRCode;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

@SuppressLint("SetTextI18n")
public class WordCounter extends AppCompatActivity {

    EditText enterText;
    ImageView imageSpeak,imageSpeakOff;
    TextView characterText;
    String gettingText;
    int numberText;
    String[] words;
    private TextToSpeech TTS;
    private File pdfFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.word_counter);

        enterText = findViewById(R.id.textEnter);
        characterText = findViewById(R.id.characterCounter);
        imageSpeak = findViewById(R.id.imageSpeak);
        imageSpeakOff = findViewById(R.id.imageSpeakOff);

        TTS = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    TTS.setLanguage(Locale.UK);
                }
            }
        });

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

        findViewById(R.id.imageBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        findViewById(R.id.imageDelete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterText.setText("");
                characterText.setText("Character Count :  0"+"\n\n\nWord Count :  0");
            }
        });

        findViewById(R.id.imageCopy).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String toCopy = enterText.getText().toString();
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Word Counter Text", toCopy);
                clipboard.setPrimaryClip(clip);
            }
        });

        findViewById(R.id.imagePaste).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData abc = clipboard.getPrimaryClip();
                ClipData.Item item = abc.getItemAt(0);
                enterText.setText(item.getText().toString());
            }
        });

        findViewById(R.id.imageSpeak).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageSpeak.setVisibility(View.GONE);
                imageSpeakOff.setVisibility(View.VISIBLE);
                String toSpeak = enterText.getText().toString();
                TTS.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
            }
        });

        findViewById(R.id.imageSpeakOff).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TTS.stop();
                imageSpeakOff.setVisibility(View.GONE);
                imageSpeak.setVisibility(View.VISIBLE);
            }
        });

        findViewById(R.id.imageShare).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, enterText.getText().toString());
                sendIntent.setType("text/plain");

                Intent shareIntent = Intent.createChooser(sendIntent, null);
                startActivity(shareIntent);

            }
        });

        findViewById(R.id.imageQRCode).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(enterText.getText().toString())){
                    Toast.makeText(WordCounter.this, "QR File can not be created Empty", Toast.LENGTH_SHORT).show();
                }
                else{
                    try {
                        createQRCode();
                    } catch (Exception e) {
                        Toast.makeText(WordCounter.this, "File couldn't be created", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();
        if (Intent.ACTION_SEND.equals(action) && type != null) {
            if(type.startsWith("text/")){
                handleSendText(intent);
            }
        }
    }

    private void createQRCode() throws Exception{
        File docsFolder = new File(Environment.getExternalStorageDirectory() + File.separator+"Remarques" +File.separator+ "/Counter Files");
        if (!docsFolder.exists()) {
            docsFolder.mkdir();
        }
        String text = enterText.getText().toString();
/*        Random r = new Random(System.currentTimeMillis());
        int randomCode = ((1 + r.nextInt(2)) * 1000 + r.nextInt(1000));
        String randCode = Integer.toString(randomCode);*/
        String randCode = new SimpleDateFormat("dd MM yyyy HH:mm:ss a", Locale.getDefault()).format(new Date());
        randCode = randCode.replaceAll(":","");
        randCode = randCode.replaceAll(" ","");
        pdfFile = new File(docsFolder.getAbsolutePath(), randCode + ".pdf");
        OutputStream outputStream = new FileOutputStream(pdfFile);
        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, outputStream);

        document.open();
        PdfContentByte cb = writer.getDirectContent();

        BarcodeQRCode barcodeQRCode = new BarcodeQRCode(text, 1000, 1000, null);
        Image codeQrImage = barcodeQRCode.getImage();
        codeQrImage.scaleAbsolute(350, 350);
        document.add(codeQrImage);
        document.newPage();

        document.close();
    }

    public void onPause() {
        if (TTS != null) {
            TTS.stop();
            TTS.shutdown();
        }
        super.onPause();
    }

    private void handleSendText(Intent intent) {
        String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
        if (sharedText != null) {
                enterText.setText(sharedText);
            }
        }
    }