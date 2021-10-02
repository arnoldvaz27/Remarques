package com.arnoldvaz27.remarques.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.view.menu.MenuPopupHelper;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.util.Linkify;
import android.util.Patterns;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.arnoldvaz27.remarques.DeviceFiles.DeviceFilesDisplay;
import com.arnoldvaz27.remarques.FileDisplayed;
import com.arnoldvaz27.remarques.FileAdapter;
import com.arnoldvaz27.remarques.R;
import com.arnoldvaz27.remarques.database.NotesDatabase;
import com.arnoldvaz27.remarques.entities.Note;
import com.arnoldvaz27.remarques.onFileSelectListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.BarcodeQRCode;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;
import com.xeoh.android.texthighlighter.TextHighlighter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import top.defaults.colorpicker.ColorPickerPopup;

import static com.arnoldvaz27.remarques.FileDisplayed.fileEnd;
import static com.arnoldvaz27.remarques.activities.MainActivity.createNoteFolder;

@SuppressLint({"SetTextI18n", "RestrictedApi"})
@SuppressWarnings({"deprecation"})
public class CreateNoteActivity extends AppCompatActivity implements onFileSelectListener {

    public static EditText inputNoteTitle, inputNoteSubTitle, inputNoteText, inputSearch;
    private TextView textDateTime, textDateTime2, labels, TextFileName;
    private ImageView imageSave, imageMoreOption;
    private View viewSubTitleIndicator;
    private HorizontalScrollView richTextLayout;
    private LinearLayout layoutWebURL, moreLayout, searchLayout, checkLinear;
    private ScrollView Parent;
    private TextView textWebURL;
    private BottomSheetDialog bottomSheetDialog, addingClip, addingFileType, addingRecorder;
    private String selectedNoteColor;
    private ImageView imageNote, imageDown, imageUp, imageSpeak, imageLock, imageUnLock, imageCopy, imagePaste, imageFiles, imageShare, imageColorLens, imageSearchClose,
            imageSearch, cancelSearch, imageSearchPic, imageScan, imageGoSearch, imageDefault, imageDefaultBackground, MoreImage, MoreUrl, imageSettings, imageClip;
    private ImageView NumberText, BulletText, circleText, squareText, starText, alphabetText, alphabetSmallText, rightText, wrongText, checkBoxText;
    private String selectedImagePath, selectedImagePath2, toShare, custom;
    private static final int REQUEST_CODE_STORAGE_PERMISSION = 1;
    private static final int REQUEST_CODE_SELECT_IMAGE = 2;
    private static final int CAMERA_REQUEST = 1888;
    private View sheetView;
    private String show = "No";
    private AlertDialog dialogAddURL, dialogLabelURL;
    private AlertDialog dialogDeleteNote, dialogSettingsNote, dialogResetNote, UrlOption, ImageOption;

    private final int REQ_CODE_TITLE = 100;
    private final int REQ_CODE_SUB_TITLE = 101;
    private final int REQ_CODE_NOTE = 102;
    public static File pdfFiles;

    private Typeface typeface1, typeface2, typeface3, typeface4, typeface5, typeface6;
    private Note alreadyAvailableNote, note;
    private String FileName, fontSettings, richText;
    final private int REQUEST_CODE_ASK_PERMISSIONS = 111;
    private File pdfFile;
    private TextToSpeech TTS;
    private int mDefaultColor, mDefaultTextColor, numberList = 1;
    private char c = 'A', d = 'a';
    private InputStream inputStream;
    private Bitmap bitmap;
    private Uri selectImageUri;
    public static String folderName, fileFormat;
    File sdCard;
    public static String viewHolder;
    private RecyclerView recyclerView;
    private int i;
    boolean isRecording;
    String fileName;
    MediaRecorder recorder;
    private ImageView fileMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_note);


        First();
        viewHolder = "CreateNote";
        displayPdf();

    }

    private void createFolder() throws Exception {
        File docsFolder = new File(Environment.getExternalStorageDirectory() + File.separator + "Remarques" + File.separator + ".Important File" + File.separator + ".R " + folderName);
        boolean success = true;
        if (!docsFolder.exists()) {
            success = docsFolder.mkdirs();
        }
        if (success) {
            pdfFile = new File(docsFolder.getAbsolutePath(), "ReadMe.pdf");
            OutputStream outputStream = new FileOutputStream(pdfFile);
            Document document = new Document();
            PdfWriter.getInstance(document, outputStream);
            document.open();

            Font f = new Font(Font.FontFamily.TIMES_ROMAN, 18f, Font.BOLD, BaseColor.RED);
            document.add(new Paragraph("Don't Delete this folder", f));

            document.close();
        } else {
            showToastRed("Error");
        }
    }

    private void First() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        ImageView imageBack = findViewById(R.id.imageBack);
        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        inputNoteTitle = findViewById(R.id.inputNoteTitle);
        inputNoteSubTitle = findViewById(R.id.inputNoteSubTitle);
        inputNoteText = findViewById(R.id.inputNote);
        labels = findViewById(R.id.labels);
        textDateTime = findViewById(R.id.textDateTime);
        textDateTime2 = findViewById(R.id.textDateTimeLive);
        imageMoreOption = findViewById(R.id.imageMoreMenu);
        viewSubTitleIndicator = findViewById(R.id.viewSubtitleIndicator);
        imageNote = findViewById(R.id.imageNote);
        textWebURL = findViewById(R.id.textWebURL);
        layoutWebURL = findViewById(R.id.layoutWebURL);
        imageDown = findViewById(R.id.imageDownLayout);
        imageUp = findViewById(R.id.imageUpLayout);
        moreLayout = findViewById(R.id.layoutMore);
        imageSpeak = findViewById(R.id.imageSpeak);
        imageLock = findViewById(R.id.imageLock);
        imageUnLock = findViewById(R.id.imageUnLock);
        imageCopy = findViewById(R.id.imageCopy);
        imagePaste = findViewById(R.id.imagePaste);
        imageFiles = findViewById(R.id.imageFiles);
        imageShare = findViewById(R.id.imageShare);
//        imageNewNote = findViewById(R.id.imageNewNote);
        imageColorLens = findViewById(R.id.imageColorLens);
        imageSearchClose = findViewById(R.id.imageSearchClose);
        imageSearch = findViewById(R.id.imageSearch);
        imageScan = findViewById(R.id.imageQRCodeScan);
        imageSettings = findViewById(R.id.imageSettings);
        searchLayout = findViewById(R.id.LayoutSearch);
        Parent = findViewById(R.id.Parent);
        cancelSearch = findViewById(R.id.SearchCancel);
        inputSearch = findViewById(R.id.inputSearch);
        imageSearchPic = findViewById(R.id.search);
        imageGoSearch = findViewById(R.id.searchGo);
        imageDefault = findViewById(R.id.imageDefault);
        imageDefaultBackground = findViewById(R.id.imageDefaultBackground);
        richTextLayout = findViewById(R.id.richText);
        MoreImage = findViewById(R.id.imageMoreImage);
        MoreUrl = findViewById(R.id.imageMoreWebURL);
//        checkLinear = findViewById(R.id.linearLayout);
        recyclerView = findViewById(R.id.recycler_view);
        NumberText = findViewById(R.id.numberedText);
        BulletText = findViewById(R.id.bulletText);
        circleText = findViewById(R.id.circleText);
        squareText = findViewById(R.id.squareText);
        starText = findViewById(R.id.starText);
        rightText = findViewById(R.id.rightText);
        wrongText = findViewById(R.id.wrongText);
        alphabetText = findViewById(R.id.alphaText);
        alphabetSmallText = findViewById(R.id.alphaSmallText);
        checkBoxText = findViewById(R.id.checkboxText);
//        moreIcons = findViewById(R.id.more_icons);
        imageClip = findViewById(R.id.imageClip);
        mDefaultColor = 0;
        mDefaultTextColor = 0;
        fileMenu = findViewById(R.id.fileMenuMore);
        TextFileName = findViewById(R.id.textFileName);

        sdCard = Environment.getExternalStorageDirectory();
        SharedPreferences getShared3 = getSharedPreferences("settings", MODE_PRIVATE);
        richText = getShared3.getString("rich text", null);

        if (richText.equals("1")) {
            richTextLayout.setVisibility(View.GONE);
        } else if (richText.equals("2")) {
            richTextLayout.setVisibility(View.VISIBLE);
        }

        SharedPreferences getShared4 = getSharedPreferences("settings", MODE_PRIVATE);
        String linkText = getShared4.getString("link text", null);

        if (linkText.equals("2")) {
            Linkify.addLinks(inputNoteText, Linkify.PHONE_NUMBERS | Linkify.WEB_URLS);
        }

        SharedPreferences getShared2 = getSharedPreferences("settings", MODE_PRIVATE);
        fontSettings = getShared2.getString("font number", null);

        typeface1 = Typeface.createFromAsset(getAssets(),
                "font/regular.ttf");
        typeface2 = Typeface.createFromAsset(getAssets(),
                "font/robotoregular.ttf");
        typeface3 = Typeface.createFromAsset(getAssets(),
                "font/lobsterregular.ttf");
        typeface4 = Typeface.createFromAsset(getAssets(),
                "font/dancingfont.ttf");
        typeface5 = Typeface.createFromAsset(getAssets(),
                "font/indieflowerregular.ttf");
        typeface6 = Typeface.createFromAsset(getAssets(),
                "font/pacificoregular.ttf");

        switch (fontSettings) {
            case "1":
                inputNoteTitle.setTypeface(typeface1);
                textDateTime.setTypeface(typeface1);
                textDateTime2.setTypeface(typeface1);
                textWebURL.setTypeface(typeface1);
                inputNoteSubTitle.setTypeface(typeface1);
                inputNoteText.setTypeface(typeface1);
                break;
            case "2":
                inputNoteTitle.setTypeface(typeface2);
                textDateTime.setTypeface(typeface2);
                textDateTime2.setTypeface(typeface2);
                textWebURL.setTypeface(typeface2);
                inputNoteSubTitle.setTypeface(typeface2);
                inputNoteText.setTypeface(typeface2);
                break;
            case "3":
                inputNoteTitle.setTypeface(typeface3);
                textDateTime.setTypeface(typeface3);
                textDateTime2.setTypeface(typeface3);
                textWebURL.setTypeface(typeface3);
                inputNoteSubTitle.setTypeface(typeface3);
                inputNoteText.setTypeface(typeface3);
                break;
            case "4":
                inputNoteTitle.setTypeface(typeface4);
                textDateTime.setTypeface(typeface4);
                textDateTime2.setTypeface(typeface4);
                textWebURL.setTypeface(typeface4);
                inputNoteSubTitle.setTypeface(typeface4);
                inputNoteText.setTypeface(typeface4);
                break;
            case "5":
                inputNoteTitle.setTypeface(typeface5);
                textDateTime.setTypeface(typeface5);
                textDateTime2.setTypeface(typeface5);
                textWebURL.setTypeface(typeface5);
                inputNoteSubTitle.setTypeface(typeface5);
                inputNoteText.setTypeface(typeface5);
                break;
            case "6":
                inputNoteTitle.setTypeface(typeface6);
                textDateTime.setTypeface(typeface6);
                textDateTime2.setTypeface(typeface6);
                textWebURL.setTypeface(typeface6);
                inputNoteSubTitle.setTypeface(typeface6);
                inputNoteText.setTypeface(typeface6);
                break;
        }

//        inputNoteText.setGravity(Gravity.RIGHT);
        TTS = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    TTS.setLanguage(Locale.UK);
                }
            }
        });

        textDateTime.setText("Created on: " +
                new SimpleDateFormat("EEEE , dd MM yyyy HH:mm a", Locale.getDefault()).format(new Date())
        );

        textDateTime2.setText("Editing Started on: " +
                new SimpleDateFormat("EEEE , dd MM yyyy HH:mm a", Locale.getDefault()).format(new Date())
        );

        imageSave = findViewById(R.id.imageSave);
        imageSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveNote();
            }
        });

        selectedNoteColor = "#333333";
        selectedImagePath = "";
        setSubTitleIndicatorColor();

        if (getIntent().getBooleanExtra("isViewOrUpdate", false)) {
            alreadyAvailableNote = (Note) getIntent().getSerializableExtra("note");
            setViewOrUpdateNote();
        } else {
            i = 1;
            createNoteFolder = createNoteFolder.replaceAll(":", "");
            createNoteFolder = createNoteFolder.replaceAll(" ", "");
            folderName = createNoteFolder;
            int hasWriteStoragePermission = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (hasWriteStoragePermission != PackageManager.PERMISSION_GRANTED) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (!shouldShowRequestPermissionRationale(Manifest.permission.WRITE_CONTACTS)) {
                        showMessage((dialog, which) -> requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_ASK_PERMISSIONS));
                        return;
                    }
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_ASK_PERMISSIONS);
                }
            } else {
                try {
                    createFolder();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            File docsFolder = new File(Environment.getExternalStorageDirectory() + File.separator + "Remarques" + File.separator + ".Important File" + File.separator + ".R " + folderName + File.separator + "ReadMe.pdf");
            docsFolder.delete();
        }

        MoreUrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
/*                textWebURL.setText(null);
                layoutWebURL.setVisibility(View.GONE);*/
                UrlOptions();
            }
        });

        MoreImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageOptions();
            }
        });

        imageDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageDown.setVisibility(View.GONE);
                imageUp.setVisibility(View.VISIBLE);
                moreLayout.setVisibility(View.VISIBLE);
            }
        });

        imageUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageUp.setVisibility(View.GONE);
                imageDown.setVisibility(View.VISIBLE);
                moreLayout.setVisibility(View.GONE);
            }
        });

        imageSpeak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
/*                imageSpeak.setVisibility(View.GONE);
                imageSpeakOff.setVisibility(View.VISIBLE);*/
                if (inputNoteTitle.getText().toString().isEmpty() && inputNoteSubTitle.getText().toString().isEmpty() &&
                        inputNoteText.getText().toString().isEmpty()) {
                    String toSpeak = "There is no Data to read";
                    TTS.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
                } else if (inputNoteSubTitle.getText().toString().isEmpty()) {
                    String toSpeak = inputNoteTitle.getText().toString() + inputNoteText.getText().toString();
                    TTS.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
                } else {
                    String toSpeak = inputNoteTitle.getText().toString() + inputNoteSubTitle.getText().toString() + inputNoteText.getText().toString();
                    TTS.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
                }

            }
        });

/*        imageSpeakOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TTS.stop();
                imageSpeakOff.setVisibility(View.GONE);
                imageSpeak.setVisibility(View.VISIBLE);
            }
        });*/
        imageCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (inputNoteTitle.getText().toString().isEmpty() && inputNoteSubTitle.getText().toString().isEmpty() &&
                        inputNoteText.getText().toString().isEmpty()) {
                    String toSpeak = "There is no Data to copy";
                    TTS.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
                } else if (inputNoteSubTitle.getText().toString().isEmpty()) {
                    String toCopy = "Title \n\n" + inputNoteTitle.getText().toString() + "\n\nNote \n\n" + inputNoteText.getText().toString();
                    ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("Note Text", toCopy);
                    clipboard.setPrimaryClip(clip);
                } else {
                    String toCopy = "Title \n\n" + inputNoteTitle.getText().toString() + "\n\nSubTitle \n\n" + inputNoteSubTitle.getText().toString() + "\n\nNote \n\n" + inputNoteText.getText().toString();
                    ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("Note Text", toCopy);
                    clipboard.setPrimaryClip(clip);
                }
            }
        });

        imagePaste.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData abc = clipboard.getPrimaryClip();
                ClipData.Item item = abc.getItemAt(0);
                String string = inputNoteText.getText().toString();
                if (TextUtils.isEmpty(string)) {
                    String text = item.getText().toString();
                    inputNoteText.setText(text);
                } else {
                    inputNoteText.setText(string + "\n\n  ");
                    String text = item.getText().toString();
                    inputNoteText.setText(text);
                }
                inputNoteText.setSelection(inputNoteText.getText().length());
            }
        });
        imageFiles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), DeviceFilesDisplay.class));
            }
        });

        inputNoteText.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (TextUtils.isEmpty(inputNoteText.getText().toString())) {
                    showToastRed("There is no text to copy");
                } else {
                    String toCopy = inputNoteText.getText().toString();
                    ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("Note Text", toCopy);
                    clipboard.setPrimaryClip(clip);
                    showToast("Copied to clipboard");
                }
                return true;
            }
        });

        inputNoteTitle.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (TextUtils.isEmpty(inputNoteTitle.getText().toString())) {
                    showToastRed("There is no text to copy");
                } else {
                    String toCopy = inputNoteTitle.getText().toString();
                    ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("Title", toCopy);
                    clipboard.setPrimaryClip(clip);
                    showToast("Copied to clipboard");
                }
                return true;
            }
        });

        inputNoteSubTitle.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (TextUtils.isEmpty(inputNoteSubTitle.getText().toString())) {
                    showToastRed("There is no text to copy");
                } else {
                    String toCopy = inputNoteSubTitle.getText().toString();
                    ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("Sub Title", toCopy);
                    clipboard.setPrimaryClip(clip);
                    showToast("Copied to clipboard");
                }
                return true;
            }
        });

        imageScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ScannerView.class));
            }
        });
        inputSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() > 0) {
                    cancelSearch.setVisibility(View.VISIBLE);
                    imageSearchPic.setVisibility(View.GONE);
                    imageGoSearch.setVisibility(View.VISIBLE);
                } else {
                    cancelSearch.setVisibility(View.GONE);
                    imageSearchPic.setVisibility(View.VISIBLE);
                    imageGoSearch.setVisibility(View.GONE);
                }
            }
        });

        imageSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NoteSettings();
            }
        });

        cancelSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputSearch.setText("");
            }
        });

        imageShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (inputNoteTitle.getText().toString().isEmpty() && inputNoteSubTitle.getText().toString().isEmpty() &&
                        inputNoteText.getText().toString().isEmpty()) {
                    String toSpeak = "There is no Data to share";
                    TTS.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
                } else if (inputNoteSubTitle.getText().toString().isEmpty()) {
                    toShare = "Title \n\n" + inputNoteTitle.getText().toString() + "\n\nNote \n\n" + inputNoteText.getText().toString();
                } else {
                    toShare = "Title \n\n" + inputNoteTitle.getText().toString() + "\n\nSubTitle \n\n" + inputNoteSubTitle.getText().toString() + "\n\nNote \n\n" + inputNoteText.getText().toString();
                }
                ShareBottomLayout();
            }
        });
        imageMoreOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomUp();
            }
        });

        imageClip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddingClip();
            }
        });
/*        imageNewNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(
                        new Intent(CreateNoteActivity.this, CreateNoteActivity.class),
                        REQUEST_CODE_ADD_NOTE
                );
            }
        });*/

        imageGoSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new TextHighlighter()
                        .setBackgroundColor(Color.parseColor("#FFFF00"))
                        .setForegroundColor(Color.GREEN)
                        .addTarget(inputNoteText)
                        .highlight(inputSearch.getText().toString(), TextHighlighter.BASE_MATCHER);
            }
        });

        imageDefault.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showResetDialog();
            }
        });

        imageDefaultBackground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Parent.setBackgroundColor(ContextCompat.getColor(CreateNoteActivity.this, R.color.colorPrimary));
                mDefaultColor = Integer.parseInt(String.valueOf(getResources().getColor(R.color.colorPrimary)));
                mDefaultTextColor = Integer.parseInt(String.valueOf(getResources().getColor(R.color.colorWhite)));
                inputNoteTitle.setTextColor(Integer.parseInt(String.valueOf(getResources().getColor(R.color.colorWhite))));
                inputNoteSubTitle.setTextColor(Integer.parseInt(String.valueOf(getResources().getColor(R.color.colorWhite))));
                inputNoteText.setTextColor(Integer.parseInt(String.valueOf(getResources().getColor(R.color.colorWhite))));
                textDateTime.setTextColor(Integer.parseInt(String.valueOf(getResources().getColor(R.color.colorWhite))));
                textDateTime2.setTextColor(Integer.parseInt(String.valueOf(getResources().getColor(R.color.colorWhite))));
                labels.setTextColor(Integer.parseInt(String.valueOf(getResources().getColor(R.color.colorWhite))));
                TextFileName.setTextColor(Integer.parseInt(String.valueOf(getResources().getColor(R.color.colorWhite))));
            }
        });

        imageNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageBottomUp();
            }
        });

        imageColorLens.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                PopupMenu popup = new PopupMenu(getApplicationContext(), imageColorLens);
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.notes_color, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getItemId() == R.id.txtColor) {
                            new ColorPickerPopup.Builder(CreateNoteActivity.this).initialColor(
                                    getResources().getColor(R.color.colorPrimary)) // set initial color
                                    // of the color
                                    // picker dialog
                                    .enableBrightness(
                                            true) // enable color brightness
                                    // slider or not
                                    .enableAlpha(
                                            true) // enable color alpha
                                    // changer on slider or
                                    // not
                                    .okTitle(
                                            "Choose") // this is top right
                                    // Choose button
                                    .cancelTitle(
                                            "Cancel") // this is top left
                                    // Cancel button which
                                    // closes the
                                    .showIndicator(
                                            true) // this is the small box
                                    // which shows the chosen
                                    // color by user at the
                                    // bottom of the cancel
                                    // button
                                    .showValue(
                                            false) // this is the value which
                                    // shows the selected
                                    // color hex code
                                    // the above all values can be made
                                    // false to disable them on the
                                    // color picker dialog.
                                    .build()
                                    .show(
                                            v,
                                            new ColorPickerPopup.ColorPickerObserver() {
                                                @Override
                                                public void
                                                onColorPicked(int color) {
                                                    // set the color
                                                    // which is returned
                                                    // by the color
                                                    // picker
                                                    mDefaultTextColor = color;

                                                    // now as soon as
                                                    // the dialog closes
                                                    // set the preview
                                                    // box to returned
                                                    // color
                                                    inputNoteTitle.setTextColor(mDefaultTextColor);
                                                    inputNoteSubTitle.setTextColor(mDefaultTextColor);
                                                    inputNoteText.setTextColor(mDefaultTextColor);
                                                    textDateTime.setTextColor(mDefaultTextColor);
                                                    textDateTime2.setTextColor(mDefaultTextColor);
                                                    labels.setTextColor(mDefaultTextColor);
                                                    TextFileName.setTextColor(mDefaultTextColor);
                                                }
                                            });

                        }
                        if (item.getItemId() == R.id.bgColor) {
                            new ColorPickerPopup.Builder(CreateNoteActivity.this).initialColor(
                                    getResources().getColor(R.color.colorPrimary)) // set initial color
                                    // of the color
                                    // picker dialog
                                    .enableBrightness(
                                            true) // enable color brightness
                                    // slider or not
                                    .enableAlpha(
                                            true) // enable color alpha
                                    // changer on slider or
                                    // not
                                    .okTitle(
                                            "Choose") // this is top right
                                    // Choose button
                                    .cancelTitle(
                                            "Cancel") // this is top left
                                    // Cancel button which
                                    // closes the
                                    .showIndicator(
                                            true) // this is the small box
                                    // which shows the chosen
                                    // color by user at the
                                    // bottom of the cancel
                                    // button
                                    .showValue(
                                            false) // this is the value which
                                    // shows the selected
                                    // color hex code
                                    // the above all values can be made
                                    // false to disable them on the
                                    // color picker dialog.
                                    .build()
                                    .show(
                                            v,
                                            new ColorPickerPopup.ColorPickerObserver() {
                                                @Override
                                                public void
                                                onColorPicked(int color) {
                                                    // set the color
                                                    // which is returned
                                                    // by the color
                                                    // picker
                                                    mDefaultColor = color;

                                                    // now as soon as
                                                    // the dialog closes
                                                    // set the preview
                                                    // box to returned
                                                    // color
                                                    Parent.setBackgroundColor(mDefaultColor);
                                                }
                                            });
                        }

                        return true;
                    }
                });


                popup.show();

            }
        });

        imageSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageSearch.setVisibility(View.GONE);
                imageSearchClose.setVisibility(View.VISIBLE);
                searchLayout.setVisibility(View.VISIBLE);
            }
        });

        imageSearchClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageSearch.setVisibility(View.VISIBLE);
                imageSearchClose.setVisibility(View.GONE);
                searchLayout.setVisibility(View.GONE);
                new TextHighlighter()
                        .resetBackgroundColor()
                        .resetForegroundColor()
                        .addTarget(inputNoteText)
                        .highlight(inputSearch.getText().toString(), TextHighlighter.BASE_MATCHER);
            }
        });

        fileMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(getApplicationContext(), fileMenu);
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.createnotefileoption, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getItemId() == R.id.file) {
                            custom = "file";
                            displayCustomPdf();
                        }
                        if (item.getItemId() == R.id.video) {
                            custom = "video";
                            displayCustomPdf();
                        }
                        if (item.getItemId() == R.id.audio) {
                            custom = "audio";
                            displayCustomPdf();
                        }
                        if (item.getItemId() == R.id.music) {
                            custom = "music";
                            displayCustomPdf();
                        }
                        if (item.getItemId() == R.id.pictures) {
                            custom = "pictures";
                            displayCustomPdf();
                        }
                        return true;
                    }
                });

                popup.show();
            }
        });

        findViewById(R.id.refreshFile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayPdf();
            }
        });

        findViewById(R.id.NoteRight).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputNoteText.setGravity(Gravity.END);
            }
        });

        findViewById(R.id.NoteLeft).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputNoteText.setGravity(Gravity.START);
            }
        });

        findViewById(R.id.NoteMiddle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputNoteText.setGravity(Gravity.CENTER_HORIZONTAL);
            }
        });

        findViewById(R.id.imageQRCode).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(inputNoteTitle.getText().toString()) || TextUtils.isEmpty(inputNoteText.getText().toString())) {
                    showToastRed("Qr Can not be created empty");
                } else {
                    try {
                        createQRCode();
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                }
            }
        });

        BulletText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String string = inputNoteText.getText().toString();
                if (TextUtils.isEmpty(string)) {
                    inputNoteText.setText("⬤  ");
                } else {
                    inputNoteText.setText(string + "\n\n⬤  ");
                }
                inputNoteText.setSelection(inputNoteText.getText().length());
                inputNoteText.requestFocus();

            }
        });

        circleText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String string = inputNoteText.getText().toString();
                if (TextUtils.isEmpty(string)) {
                    inputNoteText.setText("〇  ");
                } else {
                    inputNoteText.setText(string + "\n\n〇  ");
                }
                inputNoteText.setSelection(inputNoteText.getText().length());
                inputNoteText.requestFocus();

            }
        });

        starText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String string = inputNoteText.getText().toString();
                if (TextUtils.isEmpty(string)) {
                    inputNoteText.setText("★  ");
                } else {
                    inputNoteText.setText(string + "\n\n★  ");
                }
                inputNoteText.setSelection(inputNoteText.getText().length());
                inputNoteText.requestFocus();

            }
        });

        rightText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String string = inputNoteText.getText().toString();
                if (TextUtils.isEmpty(string)) {
                    inputNoteText.setText("✔  ");
                } else {
                    inputNoteText.setText(string + "\n\n✔  ");
                }
                inputNoteText.setSelection(inputNoteText.getText().length());
                inputNoteText.requestFocus();

            }
        });

        wrongText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String string = inputNoteText.getText().toString();
                if (TextUtils.isEmpty(string)) {
                    inputNoteText.setText("X  ");
                } else {
                    inputNoteText.setText(string + "\n\nX  ");
                }
                inputNoteText.setSelection(inputNoteText.getText().length());
                inputNoteText.requestFocus();

            }
        });

        squareText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String string = inputNoteText.getText().toString();
                if (TextUtils.isEmpty(string)) {
                    inputNoteText.setText("▢  ");
                } else {
                    inputNoteText.setText(string + "\n\n▢  ");
                }
                inputNoteText.setSelection(inputNoteText.getText().length());
                inputNoteText.requestFocus();
            }
        });

        alphabetText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String string = inputNoteText.getText().toString();
                if (TextUtils.isEmpty(string)) {
                    inputNoteText.setText(c + ")  ");
                } else {
                    inputNoteText.setText(string + "\n\n" + c + ")  ");
                }
                inputNoteText.setSelection(inputNoteText.getText().length());
                c = (char) (c + 1);
                if (c > 'Z') {
                    c = 'A';
                }
                inputNoteText.requestFocus();
            }
        });

        alphabetSmallText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String string = inputNoteText.getText().toString();
                if (TextUtils.isEmpty(string)) {
                    inputNoteText.setText(d + ")  ");
                } else {
                    inputNoteText.setText(string + "\n\n" + d + ")  ");
                }
                inputNoteText.setSelection(inputNoteText.getText().length());
                d = (char) (d + 1);
                if (d > 'z') {
                    d = 'a';
                }
                inputNoteText.requestFocus();
            }
        });

        NumberText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String string = inputNoteText.getText().toString();
                if (TextUtils.isEmpty(string)) {
                    inputNoteText.setText(numberList + ")  ");
                } else {
                    inputNoteText.setText(string + "\n\n" + numberList + ")  ");
                }
                inputNoteText.setSelection(inputNoteText.getText().length());
                numberList = numberList + 1;
                inputNoteText.requestFocus();
            }
        });

        checkBoxText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String string = inputNoteText.getText().toString();
                if (TextUtils.isEmpty(string)) {
                    inputNoteText.setText("☑️  ");
                } else {
                    inputNoteText.setText(string + "\n\n☑️  ");
                }
                inputNoteText.setSelection(inputNoteText.getText().length());
                inputNoteText.requestFocus();
            }
        });

/*        checkBoxText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox ch = new CheckBox(getApplicationContext());
                ch.setText("☑️");
                checkLinear.addView(ch);
            }
        });*/

/*        moreIcons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IconBottomLayout();
            }
        });*/

        imageLock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageLock.setVisibility(View.GONE);
                imageUnLock.setVisibility(View.VISIBLE);
            }
        });
        imageUnLock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageLock.setVisibility(View.VISIBLE);
                imageUnLock.setVisibility(View.GONE);
            }
        });

        sheetView = LayoutInflater.from(CreateNoteActivity.this).inflate(R.layout.layout_miscellaneous, (ViewGroup) findViewById(R.id.layoutMiscellaneous));

        if (alreadyAvailableNote != null && alreadyAvailableNote.getBackgroundColor() != null && !alreadyAvailableNote.getBackgroundColor().trim().isEmpty()) {
            Parent.setBackgroundColor(Integer.parseInt(alreadyAvailableNote.getBackgroundColor()));
            mDefaultColor = Integer.parseInt(alreadyAvailableNote.getBackgroundColor());
        }
        if (alreadyAvailableNote != null && alreadyAvailableNote.getTextColor() != null && !alreadyAvailableNote.getTextColor().trim().isEmpty()) {
            mDefaultTextColor = Integer.parseInt(alreadyAvailableNote.getTextColor());
            inputNoteTitle.setTextColor(Integer.parseInt(alreadyAvailableNote.getTextColor()));
            inputNoteSubTitle.setTextColor(Integer.parseInt(alreadyAvailableNote.getTextColor()));
            inputNoteText.setTextColor(Integer.parseInt(alreadyAvailableNote.getTextColor()));
            textDateTime.setTextColor(Integer.parseInt(alreadyAvailableNote.getTextColor()));
            textDateTime2.setTextColor(Integer.parseInt(alreadyAvailableNote.getTextColor()));
            labels.setTextColor(Integer.parseInt(alreadyAvailableNote.getTextColor()));
            TextFileName.setTextColor(Integer.parseInt(alreadyAvailableNote.getTextColor()));
        }
        if (alreadyAvailableNote != null && alreadyAvailableNote.getColor() != null && !alreadyAvailableNote.getColor().trim().isEmpty()) {
            ChangingDBBackground();
        }

        if (getIntent().getBooleanExtra("isFromQuickActions", false)) {
            String type = getIntent().getStringExtra("quickActionType");
            if (type != null) {
                switch (type) {
                    case "image":
                        selectedImagePath = getIntent().getStringExtra("imagePath");
                        imageNote.setImageBitmap(BitmapFactory.decodeFile(selectedImagePath));
                        imageNote.setVisibility(View.VISIBLE);
                        findViewById(R.id.imageMoreImage).setVisibility(View.VISIBLE);
                        break;
                    case "URL":
                        textWebURL.setText(getIntent().getStringExtra("URL"));
                        layoutWebURL.setVisibility(View.VISIBLE);
                        break;
                    case "Text":
                        inputNoteText.setText(getIntent().getStringExtra("Text"));
                        break;
                }
            }
        }

        final ImageView imageSpeech = findViewById(R.id.imageSpeech);
        imageSpeech.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(getApplicationContext(), imageSpeech);
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.voice_note, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getItemId() == R.id.title) {
                            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,
                                    RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
                            intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Need to speak");
                            try {
                                startActivityForResult(intent, REQ_CODE_TITLE);
                            } catch (ActivityNotFoundException a) {
                                showToastRed("Sorry your device not supported");
                            }
                        }
                        if (item.getItemId() == R.id.sub) {
                            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,
                                    RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
                            intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Need to speak");
                            try {
                                startActivityForResult(intent, REQ_CODE_SUB_TITLE);
                            } catch (ActivityNotFoundException a) {
                                showToastRed("Sorry your device not supported");
                            }
                        }
                        if (item.getItemId() == R.id.note) {
                            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,
                                    RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
                            intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Need to speak");
                            try {
                                startActivityForResult(intent, REQ_CODE_NOTE);
                            } catch (ActivityNotFoundException a) {
                                showToastRed("Sorry your device not supported");
                            }
                        }

                        return true;
                    }
                });

                popup.show();
            }
        });
//        Linkify.addLinks(inputNoteText, Linkify.PHONE_NUMBERS | Linkify.WEB_URLS);

        /*Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();
        if (Intent.ACTION_SEND.equals(action) && type != null) {
            if (type.startsWith("image/")) {
                handleSendImage(intent); // Handle single image being sent
            } else if (type.startsWith("text/")) {
                handleSendText(intent);
            }
        }*/
    }

    private void AddingClip() {

        viewHolder = "CreateNote";
        addingClip = new BottomSheetDialog(CreateNoteActivity.this, R.style.BottomSheetTheme);

        sheetView = LayoutInflater.from(CreateNoteActivity.this).inflate(R.layout.addingbottomlayout, (ViewGroup) findViewById(R.id.layoutAdding));

        sheetView.findViewById(R.id.layoutAddImage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addingClip.dismiss();
                if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager
                        .PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(CreateNoteActivity.this, new String[]
                                    {Manifest.permission.READ_EXTERNAL_STORAGE},
                            REQUEST_CODE_STORAGE_PERMISSION
                    );
                } else {
                    selectImage();
                }
            }
        });

        sheetView.findViewById(R.id.layoutAddImage2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addingClip.dismiss();
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
        });

        sheetView.findViewById(R.id.layoutAddUrl).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addingClip.dismiss();
                showAddURLDialog();
            }
        });
        sheetView.findViewById(R.id.layoutAddLabel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addingClip.dismiss();
                showAddLabelDialog();
            }
        });

        sheetView.findViewById(R.id.layoutAddTimeStamp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addingClip.dismiss();
                @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd G 'at' HH:mm:ss z");
                String currentDateandTime = sdf.format(new Date());
                String string = inputNoteText.getText().toString();
                if (TextUtils.isEmpty(string)) {
                    inputNoteText.setText(currentDateandTime);
                } else {
                    inputNoteText.setText(string + "\n\n" + currentDateandTime);
                }
                inputNoteText.setSelection(inputNoteText.getText().length());
                inputNoteText.requestFocus();
            }
        });
        
        sheetView.findViewById(R.id.layoutSelectFile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

/*                PopupMenu popup = new PopupMenu(getApplicationContext(), sheetView.findViewById(R.id.layoutSelectFile));
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.storage_choosing, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getItemId() == R.id.internal) {
                            addingClip.dismiss();
                            fileFormat = "File";
                            fileEnd = "pdf";
                            Intent intent = new Intent(CreateNoteActivity.this, FileDisplayed.class);
                            startActivity(intent);
*//*                            fileEnd = "pdf";
                            Intent intent = new Intent(CreateNoteActivity.this,FileDisplayed.class);
                            startActivity(intent);*//*
                        }
                        if (item.getItemId() == R.id.external) {
                            Toast.makeText(CreateNoteActivity.this, "Feature in progress", Toast.LENGTH_SHORT).show();
                        }
                        return true;
                    }
                });

                popup.show();*/
                fileFormat = "File";
                fileEnd = "pdf";
                startActivity(new Intent(getApplicationContext(), FileDisplayed.class));
            }
        });

        sheetView.findViewById(R.id.layoutSelectAudio).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*PopupMenu popup = new PopupMenu(getApplicationContext(), sheetView.findViewById(R.id.layoutSelectAudio));
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.storage_choosing, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getItemId() == R.id.internal) {
                            fileFormat = "Audio";
                            fileEnd = "amr";
                            Intent intent = new Intent(CreateNoteActivity.this, FileDisplayed.class);
                            startActivity(intent);
                        }
                        if (item.getItemId() == R.id.bgColor) {
                            Toast.makeText(CreateNoteActivity.this, "Feature in progress", Toast.LENGTH_SHORT).show();
                        }

                        return true;
                    }
                });
                popup.show();*/
                fileFormat = "Audio";
                fileEnd = "amr";
                startActivity(new Intent(getApplicationContext(), FileDisplayed.class));
            }
        });

        sheetView.findViewById(R.id.layoutSelectVideo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*PopupMenu popup = new PopupMenu(getApplicationContext(), sheetView.findViewById(R.id.layoutSelectVideo));
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.storage_choosing, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getItemId() == R.id.internal) {
                            fileFormat = "Video";
                            fileEnd = "mp4";
                            Intent intent = new Intent(CreateNoteActivity.this, FileDisplayed.class);
                            startActivity(intent);
                        }
                        if (item.getItemId() == R.id.bgColor) {
                            Toast.makeText(CreateNoteActivity.this, "Feature in progress", Toast.LENGTH_SHORT).show();
                        }

                        return true;
                    }
                });


                popup.show();*/
                fileFormat = "Video";
                fileEnd = "mp4";
                startActivity(new Intent(getApplicationContext(), FileDisplayed.class));
            }
        });

        sheetView.findViewById(R.id.layoutSelectMusic).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*PopupMenu popup = new PopupMenu(getApplicationContext(), sheetView.findViewById(R.id.layoutSelectMusic));
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.storage_choosing, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getItemId() == R.id.internal) {
                            fileFormat = "Music";
                            fileEnd = "mp3";
                            Intent intent = new Intent(CreateNoteActivity.this, FileDisplayed.class);
                            startActivity(intent);
                        }
                        if (item.getItemId() == R.id.bgColor) {
                            Toast.makeText(CreateNoteActivity.this, "Feature in progress", Toast.LENGTH_SHORT).show();
                        }

                        return true;
                    }
                });


                popup.show();*/
                fileFormat = "Music";
                fileEnd = "mp3";
                startActivity(new Intent(getApplicationContext(), FileDisplayed.class));
            }
        });

        sheetView.findViewById(R.id.layoutRecord).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addingClip.dismiss();
                RecordingBottom();
            }
        });
        addingClip.setContentView(sheetView);
        addingClip.show();
    }

    private void RecordingBottom() {
        addingRecorder = new BottomSheetDialog(CreateNoteActivity.this, R.style.BottomSheetTheme);

        sheetView = LayoutInflater.from(CreateNoteActivity.this).inflate(R.layout.recordingbottom, (ViewGroup) findViewById(R.id.layoutRecording));

        ImageButton btnRec;
        TextView txtRecStatus;
        Chronometer timeRec;
        ImageView cancel;

        File path = new File(Environment.getExternalStorageDirectory() + File.separator + "Remarques" + File.separator + ".Important File" + File.separator + ".R " + folderName);

        btnRec = sheetView.findViewById(R.id.btnRec);
        txtRecStatus = sheetView.findViewById(R.id.txtRecStatus);
        timeRec = sheetView.findViewById(R.id.timeRec);
        cancel = sheetView.findViewById(R.id.cancelRecorder);


        isRecording = false;

        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
        String date = format.format(new Date());

        fileName = path + "/recording_" + date + ".amr";

        btnRec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isRecording) {
                    try {
                        startRecording();
                        timeRec.setBase(SystemClock.elapsedRealtime());
                        timeRec.start();
                        txtRecStatus.setText("Recording...");
                        btnRec.setImageResource(R.drawable.ic_stop);
                        isRecording = true;
                    } catch (Exception e) {
                        e.printStackTrace();
                        showToastRed("Couldn't Record");
                    }

                } else {
                    stopRecording();
                    timeRec.setBase(SystemClock.elapsedRealtime());
                    timeRec.stop();
                    txtRecStatus.setText("");
                    btnRec.setImageResource(R.drawable.ic_record);
                    isRecording = false;
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addingRecorder.dismiss();
                displayPdf();
                AddingClip();
            }
        });
        addingRecorder.setContentView(sheetView);
        addingRecorder.show();


    }

    public void startRecording() {
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB);
        recorder.setOutputFile(fileName);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            recorder.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }

        recorder.start();
    }

    public void stopRecording() {
        displayPdf();
        recorder.stop();
        recorder.release();
        recorder = null;
    }


    private void imageBottomUp() {
        bottomSheetDialog = new BottomSheetDialog(CreateNoteActivity.this, R.style.BottomSheetTheme);

        sheetView = LayoutInflater.from(CreateNoteActivity.this).inflate(R.layout.imagefull, (ViewGroup) findViewById(R.id.layoutImage));

        final ImageView imageView = sheetView.findViewById(R.id.fullImage);
        final ImageView imageView2 = sheetView.findViewById(R.id.fullImage2);
        final ImageView imageView3 = sheetView.findViewById(R.id.left);
        final ImageView imageView4 = sheetView.findViewById(R.id.right);

        if (alreadyAvailableNote.getImagePath() != null && !alreadyAvailableNote.getImagePath().trim().isEmpty() && selectImageUri != null) {
            imageView.setVisibility(View.GONE);
            imageView2.setVisibility(View.GONE);
            imageView2.setImageBitmap(BitmapFactory.decodeFile(alreadyAvailableNote.getImagePath()));
            try {
                inputStream = getContentResolver().openInputStream(selectImageUri);
                bitmap = BitmapFactory.decodeStream(inputStream);
                imageView.setImageBitmap(bitmap);
                selectedImagePath2 = getPathFromUri(selectImageUri);
            } catch (Exception exception) {
                showToastRed("Error occurred, Please try again");
            }
            selectedImagePath2 = alreadyAvailableNote.getImagePath();
            imageView2.setVisibility(View.VISIBLE);
            imageView4.setVisibility(View.VISIBLE);
            imageView4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    imageView4.setVisibility(View.INVISIBLE);
                    imageView2.setVisibility(View.GONE);
                    imageView.setVisibility(View.VISIBLE);
                    imageView3.setVisibility(View.VISIBLE);
                }
            });
            imageView3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    imageView.setVisibility(View.GONE);
                    imageView3.setVisibility(View.INVISIBLE);
                    imageView4.setVisibility(View.VISIBLE);
                    imageView2.setVisibility(View.VISIBLE);
                }
            });
        } else {
            if (alreadyAvailableNote.getImagePath() != null && !alreadyAvailableNote.getImagePath().trim().isEmpty()) {
                imageView2.setImageBitmap(BitmapFactory.decodeFile(alreadyAvailableNote.getImagePath()));
                selectedImagePath2 = alreadyAvailableNote.getImagePath();
            }
            if (selectImageUri != null) {
                try {
                    inputStream = getContentResolver().openInputStream(selectImageUri);
                    bitmap = BitmapFactory.decodeStream(inputStream);
                    imageView.setImageBitmap(bitmap);
                    selectedImagePath2 = getPathFromUri(selectImageUri);
                } catch (Exception exception) {
                    showToastRed("Error occurred, Please try again");
                }
            }
        }
        bottomSheetDialog.setContentView(sheetView);
        bottomSheetDialog.show();
    }

    private void ImageOptions() {
        if (ImageOption == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(CreateNoteActivity.this);
            View view = LayoutInflater.from(this).inflate(
                    R.layout.imagemenu, (ViewGroup) findViewById(R.id.layoutImageMenuContainer)
            );
            builder.setView(view);
            ImageOption = builder.create();
            if (ImageOption.getWindow() != null) {
                ImageOption.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            }

            LinearLayout linearLayout, linearLayout1, linearLayout2, linearLayout3, linearLayout4;
            linearLayout = view.findViewById(R.id.Expand);
            linearLayout1 = view.findViewById(R.id.Download);
            linearLayout2 = view.findViewById(R.id.Delete);
            linearLayout3 = view.findViewById(R.id.imageHistory);
            linearLayout4 = view.findViewById(R.id.Share);
            ImageView imageView = view.findViewById(R.id.ImageSettingsCancel);
            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   /* Drawable drawable=imageNote.getDrawable();
                    Bitmap bitmap= ((BitmapDrawable)drawable).getBitmap();
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                    byte[] b = baos.toByteArray();
                    Intent intent=new Intent(CreateNoteActivity.this,FullImageView.class);
                    intent.putExtra("picture", b);
                    startActivity(intent);
                    ImageOption.cancel();*/
                    imageBottomUp();
                    ImageOption.cancel();
                }
            });

            linearLayout1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String randCode = new SimpleDateFormat("dd MM yyyy HH:mm:ss a", Locale.getDefault()).format(new Date());
                    randCode = randCode.replaceAll(":", "");
                    randCode = randCode.replaceAll(" ", "");
                    BitmapDrawable bitmapDrawable = (BitmapDrawable) imageNote.getDrawable();
                    Bitmap bitmap = bitmapDrawable.getBitmap();

                    FileOutputStream outputStream = null;
                    File file = Environment.getExternalStorageDirectory();
                    File dir = new File(file.getAbsolutePath() + File.separator + "Remarques" + File.separator + "Gallery");
                    boolean success = true;
                    if (!dir.exists()) {
                        success = dir.mkdirs();
                    }
                    if (success) {
                        String fileName = String.format(randCode + ".png", System.currentTimeMillis());
                        File outFile = new File(dir, fileName);
                        try {
                            outputStream = new FileOutputStream(outFile);

                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                        try {
                            assert outputStream != null;
                            outputStream.flush();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        try {
                            outputStream.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        showToast("Image saved in gallery");
                    } else {
                        showToastRed("Error");
                    }
                    ImageOption.cancel();
                }
            });

            linearLayout3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    saveInFavorite();
                    ImageOption.cancel();
                }
            });

            linearLayout2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    imageNote.setImageDrawable(null);
                    imageNote.setVisibility(View.GONE);
                    MoreImage.setVisibility(View.GONE);
                    ImageOption.cancel();
                }
            });

            linearLayout4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String fileName;
                    String randCode = new SimpleDateFormat("dd MM yyyy HH:mm:ss a", Locale.getDefault()).format(new Date());
                    randCode = randCode.replaceAll(":", "");
                    randCode = randCode.replaceAll(" ", "");
                    BitmapDrawable bitmapDrawable = (BitmapDrawable) imageNote.getDrawable();
                    Bitmap bitmap = bitmapDrawable.getBitmap();

                    FileOutputStream outputStream = null;
                    File file = Environment.getExternalStorageDirectory();
                    File dir = new File(file.getAbsolutePath() + File.separator + "Remarques" + File.separator + "Shared Images");
                    boolean success = true;
                    if (!dir.exists()) {
                        success = dir.mkdirs();
                    }
                    if (success) {
                        fileName = String.format(randCode + ".png", System.currentTimeMillis());
                        File outFile = new File(dir, fileName);
                        try {
                            outputStream = new FileOutputStream(outFile);

                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                        try {
                            assert outputStream != null;
                            outputStream.flush();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        try {
                            outputStream.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        File dir2 = new File(file.getAbsolutePath() + File.separator + "Remarques" + File.separator + "Shared Images" + File.separator + fileName);
                        Sharing(dir2);
                    } else {
                        showToastRed("Error");
                    }
                }
            });

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ImageOption.dismiss();
                }
            });
        }
        ImageOption.show();
    }

    private void saveInFavorite() {
        /*Random r = new Random(System.currentTimeMillis());
        int randomCode = ((1 + r.nextInt(2)) * 10000000 + r.nextInt(10000000));
        String randCode = Integer.toString(randomCode);*/
        String randCode = new SimpleDateFormat("dd MM yyyy HH:mm:ss a", Locale.getDefault()).format(new Date());
        randCode = randCode.replaceAll(":", "");
        randCode = randCode.replaceAll(" ", "");
        BitmapDrawable bitmapDrawable = (BitmapDrawable) imageNote.getDrawable();
        Bitmap bitmap = bitmapDrawable.getBitmap();

        FileOutputStream outputStream = null;
        File file = Environment.getExternalStorageDirectory();
        File dir = new File(file.getAbsolutePath() + File.separator + "Remarques" + File.separator + "Favorite Pictures");
        boolean success = true;
        if (!dir.exists()) {
            success = dir.mkdirs();
        }
        if (success) {
            String fileName = String.format(randCode + ".png", System.currentTimeMillis());
            File outFile = new File(dir, fileName);
            try {
                outputStream = new FileOutputStream(outFile);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            try {
                assert outputStream != null;
                outputStream.flush();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                outputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            showToastRed("Image saved in favorite");
        } else {
            showToast("Error");
        }
    }

    private void UrlOptions() {
        if (UrlOption == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(CreateNoteActivity.this);
            View view = LayoutInflater.from(this).inflate(
                    R.layout.urlmenu, (ViewGroup) findViewById(R.id.layoutUrlMenuContainer)
            );
            builder.setView(view);
            UrlOption = builder.create();
            if (UrlOption.getWindow() != null) {
                UrlOption.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            }
            LinearLayout linearLayout, linearLayout1, linearLayout2, linearLayout3, linearLayout4, linearLayout5, linearLayout6;
            linearLayout = view.findViewById(R.id.SearchGoogle);
            linearLayout1 = view.findViewById(R.id.SearchDuckDuck);
//            linearLayout2 = view.findViewById(R.id.Web);
            linearLayout3 = view.findViewById(R.id.SavePDF);
            linearLayout4 = view.findViewById(R.id.QRCode);
            linearLayout5 = view.findViewById(R.id.Share);
            linearLayout6 = view.findViewById(R.id.Delete);
            ImageView imageView;
            imageView = view.findViewById(R.id.ImageSettingsCancel);

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UrlOption.dismiss();
                }
            });

            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        String toCopy = textWebURL.getText().toString();
                        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                        ClipData clip = ClipData.newPlainText("Google Chrome Url", toCopy);
                        clipboard.setPrimaryClip(clip);
                        showToast("Paste the URL in the search box");
                        PackageManager pm = getPackageManager();
                        Intent intent2 = pm.getLaunchIntentForPackage("com.android.chrome");
                        startActivity(intent2);
                    } catch (Exception e) {
                        showToastRed("Google Chrome does not exists");
                    }
                }
            });

            linearLayout1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        String toCopy = textWebURL.getText().toString();
                        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                        ClipData clip = ClipData.newPlainText("Duck Duck Go Url", toCopy);
                        clipboard.setPrimaryClip(clip);
                        showToast("Paste the URL in the search box");
                        PackageManager pm = getPackageManager();
                        Intent intent2 = pm.getLaunchIntentForPackage("com.duckduckgo.mobile.android");
                        startActivity(intent2);
                    } catch (Exception e) {
                        showToastRed("Duck Duck Go does not exists");
                    }
                }
            });

            linearLayout3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        int hasWriteStoragePermission = ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
                        if (hasWriteStoragePermission != PackageManager.PERMISSION_GRANTED) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                if (!shouldShowRequestPermissionRationale(Manifest.permission.WRITE_CONTACTS)) {
                                    showMessage(new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_ASK_PERMISSIONS);
                                        }
                                    });
                                    return;
                                }
                                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_ASK_PERMISSIONS);
                            }
                        } else {
                            showToast("PDF copy has been saved to this device successfully");
                            File docsFolder = new File(Environment.getExternalStorageDirectory() + File.separator + "Remarques" + File.separator + "PDF Files");
                            boolean success = true;
                            if (!docsFolder.exists()) {
                                success = docsFolder.mkdirs();
                            }
                            if (success) {
                                String randCode = new SimpleDateFormat("dd MM yyyy HH:mm:ss a", Locale.getDefault()).format(new Date());
                                randCode = randCode.replaceAll(":", "");
                                randCode = randCode.replaceAll(" ", "");
                                pdfFile = new File(docsFolder.getAbsolutePath(), randCode + ".pdf");
                                OutputStream outputStream = new FileOutputStream(pdfFile);
                                Document document = new Document();
                                PdfWriter.getInstance(document, outputStream);
                                document.open();

                                Font f = new Font(Font.FontFamily.TIMES_ROMAN, 18f, Font.BOLD, BaseColor.BLUE);
                                document.add(new Paragraph(textWebURL.getText().toString(), f));
                                document.close();
                            } else {
                                showToastRed("Error");
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            linearLayout4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    try {
                        File docsFolder = new File(Environment.getExternalStorageDirectory() + File.separator + "Remarques" + File.separator + "QR Codes");
                        boolean success = true;
                        if (!docsFolder.exists()) {
                            success = docsFolder.mkdirs();
                        }
                        if (success) {
                            String randCode = new SimpleDateFormat("dd MM yyyy HH:mm:ss a", Locale.getDefault()).format(new Date());
                            randCode = randCode.replaceAll(":", "");
                            randCode = randCode.replaceAll(" ", "");
                            pdfFile = new File(docsFolder.getAbsolutePath(), randCode + ".pdf");
                            OutputStream outputStream = new FileOutputStream(pdfFile);
                            Document document = new Document();
                            PdfWriter writer = PdfWriter.getInstance(document, outputStream);

                            document.open();
                            PdfContentByte cb = writer.getDirectContent();

                            BarcodeQRCode barcodeQRCode = new BarcodeQRCode(textWebURL.getText().toString(), 1000, 1000, null);
                            Image codeQrImage = barcodeQRCode.getImage();
                            codeQrImage.scaleAbsolute(350, 350);
                            document.add(codeQrImage);
                            document.newPage();

                            document.close();
                        } else {
                            showToastRed("Error");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            linearLayout5.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(android.content.Intent.ACTION_SEND);
                    String shareBody = textWebURL.getText().toString();
                    intent.setType("text/plain");
                    intent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                    startActivity(Intent.createChooser(intent, "Share"));
                }
            });

            linearLayout6.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    textWebURL.setText(null);
                    layoutWebURL.setVisibility(View.GONE);
                    UrlOption.cancel();
                }
            });
        }
        UrlOption.show();
    }

    private void NoteSettings() {
        if (dialogSettingsNote == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(CreateNoteActivity.this);
            View view = LayoutInflater.from(this).inflate(
                    R.layout.note_settings_layout, (ViewGroup) findViewById(R.id.layoutSettingsNoteContainer)
            );
            builder.setView(view);
            dialogSettingsNote = builder.create();
            if (dialogSettingsNote.getWindow() != null) {
                dialogSettingsNote.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            }
            final CheckBox textBox, textBox1;
            String richText, linkText;
            textBox = view.findViewById(R.id.richTextBox);
            textBox1 = view.findViewById(R.id.linkTextBox);

            SharedPreferences getShared3 = getSharedPreferences("settings", MODE_PRIVATE);
            richText = getShared3.getString("rich text", null);

            if (richText.equals("1")) {
                textBox.setChecked(false);
            } else if (richText.equals("2")) {
                textBox.setChecked(true);
            }

            SharedPreferences getShared4 = getSharedPreferences("settings", MODE_PRIVATE);
            linkText = getShared4.getString("link text", null);

            if (linkText.equals("1")) {
                textBox1.setChecked(false);
            } else if (linkText.equals("2")) {
                textBox1.setChecked(true);
            }
            textBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final SharedPreferences shard2 = getSharedPreferences("settings", MODE_PRIVATE);
                    final SharedPreferences.Editor editor2 = shard2.edit();
                    if (textBox.isChecked()) {
                        editor2.putString("rich text", "2");
                    } else {
                        editor2.putString("rich text", "1");
                    }
                    editor2.apply();
                    dialogSettingsNote.cancel();
                    First();
                }
            });

            textBox1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final SharedPreferences shard2 = getSharedPreferences("settings", MODE_PRIVATE);
                    final SharedPreferences.Editor editor2 = shard2.edit();
                    if (textBox1.isChecked()) {
                        editor2.putString("link text", "2");
                    } else {
                        editor2.putString("link text", "1");
                    }
                    editor2.apply();
                    dialogSettingsNote.cancel();
                    First();
                }
            });

            view.findViewById(R.id.ImageSettingsCancel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogSettingsNote.dismiss();
                }
            });
        }
        dialogSettingsNote.show();
    }

    /*private void IconBottomLayout() {
        bottomSheetDialog = new BottomSheetDialog(CreateNoteActivity.this, R.style.BottomSheetTheme);

        sheetView = LayoutInflater.from(CreateNoteActivity.this).inflate(R.layout.icon_layout, (ViewGroup) findViewById(R.id.layoutIcons));

        final SwitchCompat switchCompat, switchCompat1;

        switchCompat = sheetView.findViewById(R.id.switchCompat);
        switchCompat1 = sheetView.findViewById(R.id.switchCompat1);
        final TextView textView = sheetView.findViewById(R.id.textShown);
        final TextView textView1 = sheetView.findViewById(R.id.textShown1);
        final LinearLayout linearLayout, linearLayout1, linearLayout2, linearLayout3;
        linearLayout = sheetView.findViewById(R.id.layoutOutline1);
        linearLayout2 = sheetView.findViewById(R.id.layoutOutline2);

        linearLayout1 = sheetView.findViewById(R.id.layoutFilled1);
        linearLayout3 = sheetView.findViewById(R.id.layoutFilled2);
        final LinearLayout linearLayoutSymbol;
        linearLayoutSymbol = sheetView.findViewById(R.id.layoutSymbol1);

        final ImageView imageView, imageView1, imageView2, imageView3, imageView4, imageView5;
        imageView = sheetView.findViewById(R.id.symbol1);
        imageView1 = sheetView.findViewById(R.id.symbol2);
        imageView2 = sheetView.findViewById(R.id.symbol3);
        imageView3 = sheetView.findViewById(R.id.symbol4);
        imageView4 = sheetView.findViewById(R.id.symbol5);
        imageView5 = sheetView.findViewById(R.id.symbol6);
        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton,
                                         boolean b) {
                if (switchCompat.isChecked()) {
                    linearLayout.setVisibility(View.GONE);
                    linearLayout2.setVisibility(View.GONE);
                    textView.setText("Filled");
                    linearLayout1.setVisibility(View.VISIBLE);
                    linearLayout3.setVisibility(View.VISIBLE);
                } else {
                    linearLayout1.setVisibility(View.GONE);
                    linearLayout3.setVisibility(View.GONE);
                    textView.setText("Outlined");
                    linearLayout.setVisibility(View.VISIBLE);
                    linearLayout2.setVisibility(View.VISIBLE);
                }
            }
        });
        bottomSheetDialog.dismiss();

        switchCompat1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton,
                                         boolean b) {
                if (switchCompat1.isChecked()) {
                    linearLayoutSymbol.setVisibility(View.VISIBLE);
                    textView1.setText("Hide Symbols");
                } else {
                    linearLayoutSymbol.setVisibility(View.GONE);
                    textView1.setText("Show Symbols");
                }
            }
        });
        bottomSheetDialog.dismiss();

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String string = inputNoteText.getText().toString();
                if (TextUtils.isEmpty(string)) {
                    inputNoteText.setText("₹  ");
                } else {
                    inputNoteText.setText(string + "\n\n₹  ");
                }
                inputNoteText.setSelection(inputNoteText.getText().length());
                bottomSheetDialog.dismiss();
            }
        });

        imageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String string = inputNoteText.getText().toString();
                if (TextUtils.isEmpty(string)) {
                    inputNoteText.setText("Rs  ");
                } else {
                    inputNoteText.setText(string + "\n\nRs  ");
                }
                inputNoteText.setSelection(inputNoteText.getText().length());
                bottomSheetDialog.dismiss();
            }
        });

        imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String string = inputNoteText.getText().toString();
                if (TextUtils.isEmpty(string)) {
                    inputNoteText.setText("◆  ");
                } else {
                    inputNoteText.setText(string + "\n\n◆  ");
                }
                inputNoteText.setSelection(inputNoteText.getText().length());
                bottomSheetDialog.dismiss();
            }
        });

        imageView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String string = inputNoteText.getText().toString();
                if (TextUtils.isEmpty(string)) {
                    inputNoteText.setText("❖  ");
                } else {
                    inputNoteText.setText(string + "\n\n❖  ");
                }
                inputNoteText.setSelection(inputNoteText.getText().length());
                bottomSheetDialog.dismiss();
            }
        });
        imageView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String string = inputNoteText.getText().toString();
                if (TextUtils.isEmpty(string)) {
                    inputNoteText.setText("♠️  ");
                } else {
                    inputNoteText.setText(string + "\n\n♠️  ");
                }
                inputNoteText.setSelection(inputNoteText.getText().length());
                bottomSheetDialog.dismiss();
            }
        });
        imageView5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String string = inputNoteText.getText().toString();
                if (TextUtils.isEmpty(string)) {
                    inputNoteText.setText("➤  ");
                } else {
                    inputNoteText.setText(string + "\n\n➤  ");
                }
                inputNoteText.setSelection(inputNoteText.getText().length());
                bottomSheetDialog.dismiss();
            }
        });

        imageView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String string = inputNoteText.getText().toString();
                if (TextUtils.isEmpty(string)) {
                    inputNoteText.setText("♠  ");
                } else {
                    inputNoteText.setText(string + "\n\n♠  ");
                }
                inputNoteText.setSelection(inputNoteText.getText().length());
                bottomSheetDialog.dismiss();
            }
        });
        bottomSheetDialog.setContentView(sheetView);
        bottomSheetDialog.show();
    }*/
    private void Sharing(File file) {
        Uri uri = FileProvider.getUriForFile(getApplicationContext(), getApplicationContext().getPackageName() + ".provider", file);
        Intent pdfIntent = new Intent(Intent.ACTION_SEND);
        pdfIntent.setDataAndType(uri, "image/*");

        pdfIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        pdfIntent.putExtra(Intent.EXTRA_STREAM, uri);
        try {
            startActivity(Intent.createChooser(pdfIntent, "Share Sound File"));
        } catch (ActivityNotFoundException e) {
            Toast.makeText(getApplicationContext(), "No Applications found to open this format file. You can download relevant application to view this file format", Toast.LENGTH_LONG).show();
        }
    }

    private void ChangingDBBackground() {
        switch (alreadyAvailableNote.getColor()) {
            case "#FDBE3B":
                selectedNoteColor = "#FDBE3B";
                setSubTitleIndicatorColor();

                break;
            case "#FF4842":
                selectedNoteColor = "#FF4842";
                setSubTitleIndicatorColor();
                break;
            case "#3a52fc":
                selectedNoteColor = "#3a52fc";
                setSubTitleIndicatorColor();
                break;
            case "#000000":
                selectedNoteColor = "#000000";
                setSubTitleIndicatorColor();
                break;
            case "#EE82EE":
                selectedNoteColor = "#EE82EE";
                setSubTitleIndicatorColor();
                break;
            case "#800080":
                selectedNoteColor = "#800080";
                setSubTitleIndicatorColor();
                break;
            case "#000080":
                selectedNoteColor = "#000080";
                setSubTitleIndicatorColor();
                break;
            case "#0d98ba":
                selectedNoteColor = "#0d98ba";
                setSubTitleIndicatorColor();
                break;
            case "#7cfc00":
                selectedNoteColor = "#7cfc00";
                setSubTitleIndicatorColor();
                break;
            case "#006400":
                selectedNoteColor = "#006400";
                setSubTitleIndicatorColor();
                break;
            case "#fb7268":
                selectedNoteColor = "#fb7268";
                setSubTitleIndicatorColor();
                break;

        }

    }

    private void ShareBottomLayout() {
        bottomSheetDialog = new BottomSheetDialog(CreateNoteActivity.this, R.style.BottomSheetTheme);

        sheetView = LayoutInflater.from(CreateNoteActivity.this).inflate(R.layout.layout_share, (ViewGroup) findViewById(R.id.layoutMiscellaneous));

        sheetView.findViewById(R.id.WhatsApp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PackageManager pm = CreateNoteActivity.this.getPackageManager();
                try {
                    Intent waIntent = new Intent(Intent.ACTION_SEND);
                    waIntent.setType("text/plain");
                    String text = toShare;
                    PackageInfo info = pm.getPackageInfo("com.whatsapp", PackageManager.GET_META_DATA);
                    waIntent.setPackage("com.whatsapp");
                    waIntent.putExtra(Intent.EXTRA_TEXT, text);
                    startActivity(Intent.createChooser(waIntent, "Share with"));
                } catch (PackageManager.NameNotFoundException e) {
                    showToastRed("WhatsApp not Installed");
                }
                bottomSheetDialog.dismiss();
            }
        });
        bottomSheetDialog.setContentView(sheetView);
        bottomSheetDialog.show();
    }

    private void bottomUp() {
        bottomSheetDialog = new BottomSheetDialog(CreateNoteActivity.this, R.style.BottomSheetTheme);

        sheetView = LayoutInflater.from(CreateNoteActivity.this).inflate(R.layout.layout_miscellaneous, (ViewGroup) findViewById(R.id.layoutMiscellaneous));

        final ImageView imageColor1, imageColor2, imageColor3, imageColor4, imageColor5, imageColor6, imageColor7, imageColor8, imageColor9, imageColor10, imageColor11, imageColor12;

        final LinearLayout linearLayout, linearLayout1, linearLayout2, linearLayout3, linearLayout4, linearLayout5;
        imageColor1 = sheetView.findViewById(R.id.imageColor1);
        imageColor2 = sheetView.findViewById(R.id.imageColor2);
        imageColor3 = sheetView.findViewById(R.id.imageColor3);
        imageColor4 = sheetView.findViewById(R.id.imageColor4);
        imageColor5 = sheetView.findViewById(R.id.imageColor5);
        imageColor6 = sheetView.findViewById(R.id.imageColor6);
        imageColor7 = sheetView.findViewById(R.id.imageColor7);
        imageColor8 = sheetView.findViewById(R.id.imageColor8);
        imageColor9 = sheetView.findViewById(R.id.imageColor9);
        imageColor10 = sheetView.findViewById(R.id.imageColor10);
        imageColor11 = sheetView.findViewById(R.id.imageColor11);
        imageColor12 = sheetView.findViewById(R.id.imageColor12);

        linearLayout = sheetView.findViewById(R.id.layoutPin);
//        linearLayout1 = sheetView.findViewById(R.id.layoutUnPin);
        linearLayout2 = sheetView.findViewById(R.id.layoutStar);
//        linearLayout3 = sheetView.findViewById(R.id.layoutUnStar);
        linearLayout4 = sheetView.findViewById(R.id.layoutArchive);
//        linearLayout5 = sheetView.findViewById(R.id.layoutUnArchive);

        sheetView.findViewById(R.id.viewColor1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedNoteColor = "#333333";
                imageColor1.setImageResource(R.drawable.done);
                imageColor2.setImageResource(0);
                imageColor3.setImageResource(0);
                imageColor4.setImageResource(0);
                imageColor5.setImageResource(0);
                imageColor6.setImageResource(0);
                imageColor7.setImageResource(0);
                imageColor8.setImageResource(0);
                imageColor9.setImageResource(0);
                imageColor10.setImageResource(0);
                imageColor11.setImageResource(0);
                imageColor12.setImageResource(0);
                setSubTitleIndicatorColor();
                bottomSheetDialog.dismiss();
            }
        });
        sheetView.findViewById(R.id.viewColor2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedNoteColor = "#FDBE3B";
                imageColor1.setImageResource(0);
                imageColor2.setImageResource(R.drawable.done);
                imageColor3.setImageResource(0);
                imageColor4.setImageResource(0);
                imageColor5.setImageResource(0);
                imageColor6.setImageResource(0);
                imageColor7.setImageResource(0);
                imageColor8.setImageResource(0);
                imageColor9.setImageResource(0);
                imageColor10.setImageResource(0);
                imageColor11.setImageResource(0);
                imageColor12.setImageResource(0);
                setSubTitleIndicatorColor();
                bottomSheetDialog.dismiss();
            }
        });
        sheetView.findViewById(R.id.viewColor3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedNoteColor = "#FF4842";
                imageColor1.setImageResource(0);
                imageColor2.setImageResource(0);
                imageColor3.setImageResource(R.drawable.done);
                imageColor4.setImageResource(0);
                imageColor5.setImageResource(0);
                imageColor6.setImageResource(0);
                imageColor7.setImageResource(0);
                imageColor8.setImageResource(0);
                imageColor9.setImageResource(0);
                imageColor10.setImageResource(0);
                imageColor11.setImageResource(0);
                imageColor12.setImageResource(0);
                setSubTitleIndicatorColor();
                bottomSheetDialog.dismiss();
            }
        });
        sheetView.findViewById(R.id.viewColor4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedNoteColor = "#3a52fc";
                imageColor1.setImageResource(0);
                imageColor2.setImageResource(0);
                imageColor3.setImageResource(0);
                imageColor4.setImageResource(R.drawable.done);
                imageColor5.setImageResource(0);
                imageColor6.setImageResource(0);
                imageColor7.setImageResource(0);
                imageColor8.setImageResource(0);
                imageColor9.setImageResource(0);
                imageColor10.setImageResource(0);
                imageColor11.setImageResource(0);
                imageColor12.setImageResource(0);
                setSubTitleIndicatorColor();
                bottomSheetDialog.dismiss();
            }
        });
        sheetView.findViewById(R.id.viewColor5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedNoteColor = "#000000";
                imageColor1.setImageResource(0);
                imageColor2.setImageResource(0);
                imageColor3.setImageResource(0);
                imageColor4.setImageResource(0);
                imageColor5.setImageResource(R.drawable.done);
                imageColor6.setImageResource(0);
                imageColor7.setImageResource(0);
                imageColor8.setImageResource(0);
                imageColor9.setImageResource(0);
                imageColor10.setImageResource(0);
                imageColor11.setImageResource(0);
                imageColor12.setImageResource(0);
                setSubTitleIndicatorColor();
                bottomSheetDialog.dismiss();
            }
        });
        sheetView.findViewById(R.id.viewColor6).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedNoteColor = "#EE82EE";
                imageColor1.setImageResource(0);
                imageColor2.setImageResource(0);
                imageColor3.setImageResource(0);
                imageColor4.setImageResource(0);
                imageColor5.setImageResource(0);
                imageColor6.setImageResource(R.drawable.done);
                imageColor7.setImageResource(0);
                imageColor8.setImageResource(0);
                imageColor9.setImageResource(0);
                imageColor10.setImageResource(0);
                imageColor11.setImageResource(0);
                imageColor12.setImageResource(0);
                setSubTitleIndicatorColor();
                bottomSheetDialog.dismiss();
            }
        });
        sheetView.findViewById(R.id.viewColor7).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedNoteColor = "#800080";
                imageColor1.setImageResource(0);
                imageColor2.setImageResource(0);
                imageColor3.setImageResource(0);
                imageColor4.setImageResource(0);
                imageColor5.setImageResource(0);
                imageColor6.setImageResource(0);
                imageColor7.setImageResource(R.drawable.done);
                imageColor8.setImageResource(0);
                imageColor9.setImageResource(0);
                imageColor10.setImageResource(0);
                imageColor11.setImageResource(0);
                imageColor12.setImageResource(0);
                setSubTitleIndicatorColor();
                bottomSheetDialog.dismiss();
            }
        });
        sheetView.findViewById(R.id.viewColor8).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedNoteColor = "#000080";
                imageColor1.setImageResource(0);
                imageColor2.setImageResource(0);
                imageColor3.setImageResource(0);
                imageColor4.setImageResource(0);
                imageColor5.setImageResource(0);
                imageColor6.setImageResource(0);
                imageColor7.setImageResource(0);
                imageColor8.setImageResource(R.drawable.done);
                imageColor9.setImageResource(0);
                imageColor10.setImageResource(0);
                imageColor11.setImageResource(0);
                imageColor12.setImageResource(0);
                setSubTitleIndicatorColor();
                bottomSheetDialog.dismiss();
            }
        });
        sheetView.findViewById(R.id.viewColor9).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedNoteColor = "#0d98ba";
                imageColor1.setImageResource(0);
                imageColor2.setImageResource(0);
                imageColor3.setImageResource(0);
                imageColor4.setImageResource(0);
                imageColor5.setImageResource(0);
                imageColor6.setImageResource(0);
                imageColor7.setImageResource(0);
                imageColor8.setImageResource(0);
                imageColor9.setImageResource(R.drawable.done);
                imageColor10.setImageResource(0);
                imageColor11.setImageResource(0);
                imageColor12.setImageResource(0);
                setSubTitleIndicatorColor();
                bottomSheetDialog.dismiss();
            }
        });
        sheetView.findViewById(R.id.viewColor10).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedNoteColor = "#7cfc00";
                imageColor1.setImageResource(0);
                imageColor2.setImageResource(0);
                imageColor3.setImageResource(0);
                imageColor4.setImageResource(0);
                imageColor5.setImageResource(0);
                imageColor6.setImageResource(0);
                imageColor7.setImageResource(0);
                imageColor8.setImageResource(0);
                imageColor9.setImageResource(0);
                imageColor10.setImageResource(R.drawable.done);
                imageColor11.setImageResource(0);
                imageColor12.setImageResource(0);
                setSubTitleIndicatorColor();
                bottomSheetDialog.dismiss();
            }
        });
        sheetView.findViewById(R.id.viewColor11).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedNoteColor = "#006400";
                imageColor1.setImageResource(0);
                imageColor2.setImageResource(0);
                imageColor3.setImageResource(0);
                imageColor4.setImageResource(0);
                imageColor5.setImageResource(0);
                imageColor6.setImageResource(0);
                imageColor7.setImageResource(0);
                imageColor8.setImageResource(0);
                imageColor9.setImageResource(0);
                imageColor10.setImageResource(0);
                imageColor11.setImageResource(R.drawable.done);
                imageColor12.setImageResource(0);
                setSubTitleIndicatorColor();
                bottomSheetDialog.dismiss();
            }
        });
        sheetView.findViewById(R.id.viewColor12).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedNoteColor = "#fb7268";
                imageColor1.setImageResource(0);
                imageColor2.setImageResource(0);
                imageColor3.setImageResource(0);
                imageColor4.setImageResource(0);
                imageColor5.setImageResource(0);
                imageColor6.setImageResource(0);
                imageColor7.setImageResource(0);
                imageColor8.setImageResource(0);
                imageColor9.setImageResource(0);
                imageColor10.setImageResource(0);
                imageColor11.setImageResource(0);
                imageColor12.setImageResource(R.drawable.done);
                setSubTitleIndicatorColor();
                bottomSheetDialog.dismiss();
            }
        });

        if (alreadyAvailableNote != null && alreadyAvailableNote.getColor() != null && !alreadyAvailableNote.getColor().trim().isEmpty()) {
            switch (alreadyAvailableNote.getColor()) {
                case "#FDBE3B":
                    sheetView.findViewById(R.id.viewColor2).performClick();
                    break;
                case "#FF4842":
                    sheetView.findViewById(R.id.viewColor3).performClick();
                    break;
                case "#3a52fc":
                    sheetView.findViewById(R.id.viewColor4).performClick();
                    break;
                case "#000000":
                    sheetView.findViewById(R.id.viewColor5).performClick();
                    break;
                case "#EE82EE":
                    sheetView.findViewById(R.id.viewColor6).performClick();
                    break;
                case "#800080":
                    sheetView.findViewById(R.id.viewColor7).performClick();
                    break;
                case "#000080":
                    sheetView.findViewById(R.id.viewColor8).performClick();
                    break;
                case "#0d98ba":
                    sheetView.findViewById(R.id.viewColor9).performClick();
                    break;
                case "#7cfc00":
                    sheetView.findViewById(R.id.viewColor10).performClick();
                    break;
                case "#006400":
                    sheetView.findViewById(R.id.viewColor11).performClick();
                    break;
                case "#fb7268":
                    sheetView.findViewById(R.id.viewColor12).performClick();
                    break;

            }
        }
/*        sheetView.findViewById(R.id.layoutAddImage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                bottomSheetDialog.dismiss();
                if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager
                        .PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(CreateNoteActivity.this, new String[]
                                    {Manifest.permission.READ_EXTERNAL_STORAGE},
                            REQUEST_CODE_STORAGE_PERMISSION
                    );
                } else {
                    selectImage();
                }
            }
        });

        sheetView.findViewById(R.id.layoutAddImage2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
        });

        sheetView.findViewById(R.id.layoutAddUrl).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
                showAddURLDialog();
            }
        });*/
        if (!inputNoteText.getText().toString().isEmpty() && !inputNoteTitle.getText().toString().isEmpty()) {
            sheetView.findViewById(R.id.SavingLayout).setVisibility(View.VISIBLE);
            sheetView.findViewById(R.id.layoutSavePdf).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        createPDF();
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                }
            });
            sheetView.findViewById(R.id.layoutSaveTxt).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        createPDF2();
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                }
            });
        }
/*        if(!inputNoteText.getText().toString().isEmpty() && !inputNoteTitle.getText().toString().isEmpty())
        {
            sheetView.findViewById(R.id.layoutSaveWord).setVisibility(View.VISIBLE);
            sheetView.findViewById(R.id.layoutSaveWord).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        createPDF1();
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                }
            });
        }*/
        if (!inputNoteText.getText().toString().isEmpty() && !inputNoteTitle.getText().toString().isEmpty()) {
            sheetView.findViewById(R.id.layoutPin).setVisibility(View.VISIBLE);
            sheetView.findViewById(R.id.layoutPin).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(CreateNoteActivity.this, "Feature in Progress", Toast.LENGTH_SHORT).show();
                }
            });
        }
        if (!inputNoteText.getText().toString().isEmpty() && !inputNoteTitle.getText().toString().isEmpty()) {
            sheetView.findViewById(R.id.layoutStar).setVisibility(View.VISIBLE);
            sheetView.findViewById(R.id.layoutStar).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(CreateNoteActivity.this, "Feature in Progress", Toast.LENGTH_SHORT).show();
                }
            });
        }
        if (!inputNoteText.getText().toString().isEmpty() && !inputNoteTitle.getText().toString().isEmpty()) {
            sheetView.findViewById(R.id.layoutArchive).setVisibility(View.VISIBLE);
            sheetView.findViewById(R.id.layoutArchive).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(CreateNoteActivity.this, "Feature in Progress", Toast.LENGTH_SHORT).show();
                }
            });
        }

        if (alreadyAvailableNote != null) {
            sheetView.findViewById(R.id.layoutDeleteNote).setVisibility(View.VISIBLE);
            sheetView.findViewById(R.id.layoutDeleteNote).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDeleteDialog();
                }
            });
        }

        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        bottomSheetDialog.setContentView(sheetView);
        bottomSheetDialog.show();
    }

    private void createQRCode() throws Exception {
        File docsFolder = new File(Environment.getExternalStorageDirectory() + File.separator + "Remarques" + File.separator + "QR Codes");
        boolean success = true;
        if (!docsFolder.exists()) {
            success = docsFolder.mkdirs();
        }
        if (success) {
            String randCode = new SimpleDateFormat("dd MM yyyy HH:mm:ss a", Locale.getDefault()).format(new Date());
            randCode = randCode.replaceAll(":", "");
            randCode = randCode.replaceAll(" ", "");
            pdfFile = new File(docsFolder.getAbsolutePath(), randCode + ".pdf");
            OutputStream outputStream = new FileOutputStream(pdfFile);
            Document document = new Document();
            PdfWriter writer = PdfWriter.getInstance(document, outputStream);

            document.open();
            PdfContentByte cb = writer.getDirectContent();

            BarcodeQRCode barcodeQRCode = new BarcodeQRCode("Title: \n" + inputNoteTitle + "\nNote :\n\n" + inputNoteText, 1000, 1000, null);
            Image codeQrImage = barcodeQRCode.getImage();
            codeQrImage.scaleAbsolute(350, 350);
            document.add(codeQrImage);
            document.newPage();
            document.close();
            showToast("Qr Code created successfully");
        } else {
            showToastRed("Error");
        }
    }

    private void createPDF2() throws Exception {
        int hasWriteStoragePermission = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (hasWriteStoragePermission != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!shouldShowRequestPermissionRationale(Manifest.permission.WRITE_CONTACTS)) {
                    showMessage(new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_ASK_PERMISSIONS);
                        }
                    });
                    return;
                }
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_ASK_PERMISSIONS);
            }
        } else {
            showToast("TEXT copy has been saved to this device successfully");
            File docsFolder = new File(Environment.getExternalStorageDirectory() + File.separator + "Remarques" + File.separator + "Text Files");
            boolean success = true;
            if (!docsFolder.exists()) {
                success = docsFolder.mkdirs();
            }
            if (success) {
                String randCode = new SimpleDateFormat("dd MM yyyy HH:mm:ss a", Locale.getDefault()).format(new Date());
                randCode = randCode.replaceAll(":", "");
                randCode = randCode.replaceAll(" ", "");
                pdfFile = new File(docsFolder.getAbsolutePath(), randCode + ".txt");
                OutputStream outputStream = new FileOutputStream(pdfFile);
                Document document = new Document();
                PdfWriter.getInstance(document, outputStream);
                document.open();

                Font f = new Font(Font.FontFamily.TIMES_ROMAN, 18f, Font.BOLD, BaseColor.RED);
                document.add(new Paragraph("Title", f));

                Font f1 = new Font(Font.FontFamily.TIMES_ROMAN, 15f, Font.NORMAL, BaseColor.BLACK);
                document.add(new Paragraph("\n\n" + inputNoteTitle.getText().toString(), f1));

                Font f2 = new Font(Font.FontFamily.TIMES_ROMAN, 18f, Font.BOLD, BaseColor.RED);
                document.add(new Paragraph("\n\n" + "Note", f2));

                Font f3 = new Font(Font.FontFamily.TIMES_ROMAN, 15f, Font.BOLD, BaseColor.BLACK);
                document.add(new Paragraph("\n\n" + inputNoteText.getText().toString(), f3));

                document.close();
            } else {
                showToastRed("Error");
            }
        }
    }

    private void showResetDialog() {
        if (dialogResetNote == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(CreateNoteActivity.this);
            View view = LayoutInflater.from(this).inflate(
                    R.layout.layout_reset, (ViewGroup) findViewById(R.id.layoutDeleteNoteContainer)
            );
            builder.setView(view);
            dialogResetNote = builder.create();
            if (dialogResetNote.getWindow() != null) {
                dialogResetNote.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            }
            view.findViewById(R.id.textDeleteNote).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (getIntent().getBooleanExtra("isViewOrUpdate", false)) {
                        setViewOrUpdateNote();
                    } else {
                        inputNoteTitle.setText("");
                        inputNoteSubTitle.setText("");
                        inputNoteText.setText("");
                        layoutWebURL.setVisibility(View.GONE);
                        textWebURL.setText("");
                        imageNote.setVisibility(View.GONE);
                        MoreImage.setVisibility(View.GONE);
                        imageNote.setImageBitmap(null);
                    }
                    dialogResetNote.cancel();
                }
            });

            view.findViewById(R.id.textCancel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogResetNote.dismiss();
                }
            });
        }
        dialogResetNote.show();
    }

    private void showDeleteDialog() {
        if (dialogDeleteNote == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(CreateNoteActivity.this);
            View view = LayoutInflater.from(this).inflate(
                    R.layout.layout_delete_note, (ViewGroup) findViewById(R.id.layoutDeleteNoteContainer)
            );
            builder.setView(view);
            dialogDeleteNote = builder.create();
            if (dialogDeleteNote.getWindow() != null) {
                dialogDeleteNote.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            }
            view.findViewById(R.id.textDeleteNote).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteRecursive(new File(Environment.getExternalStorageDirectory() + File.separator + "Remarques" + File.separator + ".Important File" + File.separator + ".R " + folderName));
                    @SuppressLint("StaticFieldLeak")
                    class DeleteNoteTask extends AsyncTask<Void, Void, Void> {

                        @Override
                        protected Void doInBackground(Void... voids) {
                            NotesDatabase.getNotesDatabase(getApplicationContext()).noteDao()
                                    .deleteNote(alreadyAvailableNote);
                            return null;

                        }

                        @Override
                        protected void onPostExecute(Void aVoid) {
                            super.onPostExecute(aVoid);
                            Intent intent = new Intent();
                            intent.putExtra("isNoteDeleted", true);
                            setResult(RESULT_OK, intent);
                            finish();
                        }
                    }
                    new DeleteNoteTask().execute();
                }
            });

            view.findViewById(R.id.textCancel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogDeleteNote.dismiss();
                }
            });
        }
        dialogDeleteNote.show();
    }

    private void saveNote() {
        /*if(inputNoteTitle.getText().toString().trim().isEmpty())
        {
            Toast.makeText(this, "Note title can't be empty!", Toast.LENGTH_SHORT).show();
        }
        else if(inputNoteSubTitle.getText().toString().trim().isEmpty() && inputNoteText.getText().toString().trim().isEmpty())
        {
            Toast.makeText(this, "Note can't be empty", Toast.LENGTH_SHORT).show();
        }
        else
        {

        }*/
        i = 0;
        final Note note = new Note();
        note.setTitle(inputNoteTitle.getText().toString());
        note.setSubtitle(inputNoteSubTitle.getText().toString());
        note.setNoteText(inputNoteText.getText().toString());
        note.setDateTime(textDateTime.getText().toString());
        note.setFolder(folderName);
        note.setBackgroundColor(String.valueOf(mDefaultColor));
        note.setTextColor(String.valueOf(mDefaultTextColor));
        note.setColor(selectedNoteColor);
        note.setNoteLabel(labels.getText().toString());

        if (imageNote.getVisibility() == View.VISIBLE) {
            note.setImagePath(selectedImagePath);
        }
        if (layoutWebURL.getVisibility() == View.VISIBLE) {
            note.setWebLink(textWebURL.getText().toString());
        }
        if (imageUnLock.getVisibility() == View.VISIBLE) {
            note.setPasswordState("Yes");
        } else {
            note.setPasswordState("No");
        }
        if (alreadyAvailableNote != null) {
            note.setId(alreadyAvailableNote.getId());
        }

        @SuppressLint("StaticFieldLeak")
        class SaveNoteTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {
                NotesDatabase.getNotesDatabase(getApplicationContext()).noteDao().insertNote(note);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                Intent intent = new Intent(CreateNoteActivity.this, MainActivity.class);
                setResult(RESULT_OK, intent);
                startActivity(intent);

            }
        }

        new SaveNoteTask().execute();
    }

    private void setSubTitleIndicatorColor() {
        GradientDrawable gradientDrawable = (GradientDrawable) viewSubTitleIndicator.getBackground();
        gradientDrawable.setColor(Color.parseColor(selectedNoteColor));

    }

    @SuppressLint("QueryPermissionsNeeded")
    private void selectImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, REQUEST_CODE_SELECT_IMAGE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_STORAGE_PERMISSION && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                selectImage();
            } else {
                showToastRed("Permission Denied");
            }
        }
        if (requestCode == REQUEST_CODE_ASK_PERMISSIONS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                try {
                    createPDF();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                showToastRed("Write_External Permission Denied");
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SELECT_IMAGE && resultCode == RESULT_OK) {
            if (data != null) {
                selectImageUri = data.getData();
                if (selectImageUri != null) {
                    try {
                        inputStream = getContentResolver().openInputStream(selectImageUri);
                        bitmap = BitmapFactory.decodeStream(inputStream);
                        imageNote.setImageBitmap(bitmap);
                        imageNote.setVisibility(View.VISIBLE);
                        findViewById(R.id.imageMoreImage).setVisibility(View.VISIBLE);
                        selectedImagePath = getPathFromUri(selectImageUri);
                    } catch (Exception exception) {
                        showToastRed("Error occurred, Please try again");
                    }
                }
            }
        } else if (requestCode == REQ_CODE_TITLE && resultCode == RESULT_OK && null != data) {
            ArrayList<String> result = data
                    .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            String i = result.get(0);
            if (i.equals("Next") || i.equals("next")) {
                inputNoteTitle.append("\n");
            } else {
                inputNoteTitle.append(result.get(0));
            }
//            inputSearch.append(result.toString().replaceAll("\\[", "").replaceAll("]", ""));
        } else if (requestCode == REQ_CODE_SUB_TITLE && resultCode == RESULT_OK && null != data) {
            ArrayList<String> result = data
                    .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            String i = result.get(0);
            if (i.equals("Next") || i.equals("next")) {
                inputNoteSubTitle.append("\n");
            } else {
                inputNoteSubTitle.append(result.get(0));
            }

//            inputSearch.append(result.toString().replaceAll("\\[", "").replaceAll("]", ""));
        } else if (requestCode == REQ_CODE_NOTE && resultCode == RESULT_OK && null != data) {
            ArrayList<String> result = data
                    .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            String i = result.get(0);
            if (i.equals("Next") || i.equals("next")) {
                inputNoteText.append("\n");
            } else {
                inputNoteText.append(result.get(0));
            }
//            inputSearch.append(result.toString().replaceAll("\\[", "").replaceAll("]", ""));
        } else if (requestCode == CAMERA_REQUEST && null != data) {
            selectImageUri = data.getData();
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            imageNote.setVisibility(View.VISIBLE);
            findViewById(R.id.imageMoreImage).setVisibility(View.VISIBLE);
            imageNote.setImageBitmap(photo);
            selectedImagePath = getPathFromUri(selectImageUri);
        } else if (requestCode == 12 && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();
            String uriString = uri.toString();
            File myFile = new File(uriString);
            String path = myFile.getAbsolutePath();
            String displayName = null;

            TextView textView;
            textView = findViewById(R.id.text);
            if (uriString.startsWith("content://")) {
                Cursor cursor = null;
                try {
                    cursor = getApplicationContext().getContentResolver().query(uri, null, null, null, null);
                    if (cursor != null && cursor.moveToFirst()) {
                        displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                        textView.setText(displayName);
                    }
                } finally {
                    cursor.close();
                }
            } else if (uriString.startsWith("file://")) {
                displayName = myFile.getName();
                textView.setText(displayName);
            }


        }
    }

    private String getPathFromUri(Uri contentUri) {
        String filePath;
        Cursor cursor = getContentResolver().query(contentUri, null, null, null, null);
        if (cursor == null) {
            filePath = contentUri.getPath();
        } else {
            cursor.moveToFirst();
            int index = cursor.getColumnIndex("_data");
            filePath = cursor.getString(index);
            cursor.close();
        }

        return filePath;
    }

    private void showAddURLDialog() {
        if (dialogAddURL == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(CreateNoteActivity.this);
            View view = LayoutInflater.from(this).inflate(
                    R.layout.layout_add_url, (ViewGroup) findViewById(R.id.layoutAddUrlContainer)
            );
            builder.setView(view);

            dialogAddURL = builder.create();
            if (dialogAddURL.getWindow() != null) {
                dialogAddURL.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            }

            final EditText inputURL = view.findViewById(R.id.inputURL);
            inputURL.requestFocus();

            view.findViewById(R.id.textAdd).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (inputURL.getText().toString().trim().isEmpty()) {
                        showToastRed("Enter URL");
                    } else if (!Patterns.WEB_URL.matcher(inputURL.getText().toString()).matches()) {
                        showToastRed("Enter Valid URL");
                    } else {
                        layoutWebURL.setVisibility(View.VISIBLE);
                        MoreUrl.setVisibility(View.VISIBLE);
                        textWebURL.setText(inputURL.getText().toString());
                        dialogAddURL.dismiss();
                    }
                }
            });

            view.findViewById(R.id.textCancel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogAddURL.dismiss();
                }
            });
        }

        dialogAddURL.show();
    }

    private void showAddLabelDialog() {
        if (dialogLabelURL == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(CreateNoteActivity.this);
            View view = LayoutInflater.from(this).inflate(
                    R.layout.layout_add_label, (ViewGroup) findViewById(R.id.layoutAddUrlContainer)
            );
            builder.setView(view);

            dialogLabelURL = builder.create();
            if (dialogLabelURL.getWindow() != null) {
                dialogLabelURL.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            }

            final EditText inputLabel = view.findViewById(R.id.inputLabel);
            inputLabel.requestFocus();

            view.findViewById(R.id.textAdd).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (inputLabel.getText().toString().trim().isEmpty()) {
                        showToastRed("Enter Label");
                    } else {
                        if (labels.getText().toString().equals("No Label")) {
                            labels.setText(inputLabel.getText().toString());
                        } else {
                            String string = labels.getText().toString();
                            labels.setText(string + " , " + inputLabel.getText().toString());
                        }
                        dialogLabelURL.dismiss();
                        inputLabel.setText("");

                    }
                }
            });

            view.findViewById(R.id.textCancel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogLabelURL.dismiss();
                }
            });
        }
        dialogLabelURL.show();
    }

    private void setViewOrUpdateNote() {
        if (alreadyAvailableNote.getPasswordState().equals("Yes")) {
            imageLock.setVisibility(View.GONE);
            imageUnLock.setVisibility(View.VISIBLE);
            if (dialogResetNote == null) {
                AlertDialog.Builder builder = new AlertDialog.Builder(CreateNoteActivity.this);
                View view = LayoutInflater.from(this).inflate(
                        R.layout.enter_note_password, (ViewGroup) findViewById(R.id.layoutPasswordNoteContainer)
                );
                builder.setView(view);
                dialogResetNote = builder.create();
                if (dialogResetNote.getWindow() != null) {
                    dialogResetNote.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                }

                EditText editText = view.findViewById(R.id.textPassword);
                editText.setSelection(editText.getText().length());
                editText.requestFocus();
                editText.getShowSoftInputOnFocus();
                view.findViewById(R.id.textSubmitNote).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (editText.getText().toString().equals("123456")) {
                            inputNoteTitle.setText(alreadyAvailableNote.getTitle());
                            inputNoteSubTitle.setText(alreadyAvailableNote.getSubtitle());
                            inputNoteText.setText(alreadyAvailableNote.getNoteText());
                            textDateTime.setText(alreadyAvailableNote.getDateTime());
                            labels.setText(alreadyAvailableNote.getNoteLabel());
                            folderName = alreadyAvailableNote.getFolder();
//        Parent.setBackgroundColor(Integer.parseInt(alreadyAvailableNote.getBackgroundColor()));
                            File path = new File(Environment.getExternalStorageDirectory() + File.separator + "Remarques" + File.separator + ".Important File" + File.separator + ".R " + folderName);

                            if (!path.exists()) {
                                try {
                                    createFolder();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                            File docsFolder = new File(Environment.getExternalStorageDirectory() + File.separator + "Remarques" + File.separator + ".Important File" + File.separator + ".R " + folderName + File.separator + "ReadMe.pdf");
                            docsFolder.delete();

                            if (alreadyAvailableNote.getImagePath() != null && !alreadyAvailableNote.getImagePath().trim().isEmpty()) {
                                imageNote.setImageBitmap(BitmapFactory.decodeFile(alreadyAvailableNote.getImagePath()));
                                imageNote.setVisibility(View.VISIBLE);
                                findViewById(R.id.imageMoreImage).setVisibility(View.VISIBLE);
                                selectedImagePath = alreadyAvailableNote.getImagePath();
                            } else {
                                imageNote.setVisibility(View.GONE);
                                MoreImage.setVisibility(View.GONE);
                                imageNote.setImageBitmap(null);
                            }
                            if (alreadyAvailableNote.getWebLink() != null && !alreadyAvailableNote.getWebLink().trim().isEmpty()) {
                                textWebURL.setText(alreadyAvailableNote.getWebLink());
                                layoutWebURL.setVisibility(View.VISIBLE);
                            } else {
                                layoutWebURL.setVisibility(View.GONE);
                                MoreUrl.setVisibility(View.GONE);
                                textWebURL.setText("");
                            }
                            dialogResetNote.dismiss();
                        } else {
                            showToastRed("Please enter correct password");
                        }
                    }
                });

                view.findViewById(R.id.textReset).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });
            }
            dialogResetNote.getWindow().setSoftInputMode(
                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
            dialogResetNote.show();
        } else {
            imageLock.setVisibility(View.VISIBLE);
            imageUnLock.setVisibility(View.GONE);
            inputNoteTitle.setText(alreadyAvailableNote.getTitle());
            inputNoteSubTitle.setText(alreadyAvailableNote.getSubtitle());
            inputNoteText.setText(alreadyAvailableNote.getNoteText());
            textDateTime.setText(alreadyAvailableNote.getDateTime());
            labels.setText(alreadyAvailableNote.getNoteLabel());
            folderName = alreadyAvailableNote.getFolder();
            File path = new File(Environment.getExternalStorageDirectory() + File.separator + "Remarques" + File.separator + ".Important File" + File.separator + ".R " + folderName);

            if (!path.exists()) {
                try {
                    createFolder();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            File docsFolder = new File(Environment.getExternalStorageDirectory() + File.separator + "Remarques" + File.separator + ".Important File" + File.separator + ".R " + folderName + File.separator + "ReadMe.pdf");
            docsFolder.delete();
//        Parent.setBackgroundColor(Integer.parseInt(alreadyAvailableNote.getBackgroundColor()));

            if (alreadyAvailableNote.getImagePath() != null && !alreadyAvailableNote.getImagePath().trim().isEmpty()) {
                imageNote.setImageBitmap(BitmapFactory.decodeFile(alreadyAvailableNote.getImagePath()));
                imageNote.setVisibility(View.VISIBLE);
                findViewById(R.id.imageMoreImage).setVisibility(View.VISIBLE);
                selectedImagePath = alreadyAvailableNote.getImagePath();
            } else {
                imageNote.setVisibility(View.GONE);
                MoreImage.setVisibility(View.GONE);
                imageNote.setImageBitmap(null);
            }
            if (alreadyAvailableNote.getWebLink() != null && !alreadyAvailableNote.getWebLink().trim().isEmpty()) {
                textWebURL.setText(alreadyAvailableNote.getWebLink());
                layoutWebURL.setVisibility(View.VISIBLE);
            } else {
                layoutWebURL.setVisibility(View.GONE);
                MoreUrl.setVisibility(View.GONE);
                textWebURL.setText("");
            }
        }

//        if (alreadyAvailableNote.getFolder() != null && !alreadyAvailableNote.getFolder().trim().isEmpty()) {
//            recyclerView.setVisibility(View.VISIBLE);
//        } else {
//            recyclerView.setVisibility(View.GONE);
//        }


    }

    private void createPDF() throws Exception {
        int hasWriteStoragePermission = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (hasWriteStoragePermission != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!shouldShowRequestPermissionRationale(Manifest.permission.WRITE_CONTACTS)) {
                    showMessage(new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_ASK_PERMISSIONS);
                        }
                    });
                    return;
                }
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_ASK_PERMISSIONS);
            }
        } else {
            showToast("PDF copy has been saved to this device successfully");
            File docsFolder = new File(Environment.getExternalStorageDirectory() + File.separator + "Remarques" + File.separator + "PDF Files");
            boolean success = true;
            if (!docsFolder.exists()) {
                success = docsFolder.mkdirs();
            }
            if (success) {
                String randCode = new SimpleDateFormat("dd MM yyyy HH:mm:ss a", Locale.getDefault()).format(new Date());
                randCode = randCode.replaceAll(":", "");
                randCode = randCode.replaceAll(" ", "");
                pdfFile = new File(docsFolder.getAbsolutePath(), randCode + ".pdf");
                OutputStream outputStream = new FileOutputStream(pdfFile);
                Document document = new Document();
                PdfWriter.getInstance(document, outputStream);
                document.open();

                Font f = new Font(Font.FontFamily.TIMES_ROMAN, 18f, Font.BOLD, BaseColor.RED);
                document.add(new Paragraph("Title", f));

                Font f1 = new Font(Font.FontFamily.TIMES_ROMAN, 15f, Font.NORMAL, BaseColor.BLACK);
                document.add(new Paragraph("\n\n" + inputNoteTitle.getText().toString(), f1));

                Font f2 = new Font(Font.FontFamily.TIMES_ROMAN, 18f, Font.BOLD, BaseColor.RED);
                document.add(new Paragraph("\n\n" + "Note", f2));

                Font f3 = new Font(Font.FontFamily.TIMES_ROMAN, 15f, Font.BOLD, BaseColor.BLACK);
                document.add(new Paragraph("\n\n" + inputNoteText.getText().toString(), f3));


                document.close();
            } else {
                showToastRed("Error");
            }
        }
    }

    private void showMessage(DialogInterface.OnClickListener onClickListener) {
        new AlertDialog.Builder(this)
                .setMessage("You need to allow to Storage")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("OK", onClickListener)
                .create()
                .show();
    }

    @SuppressLint("RestrictedApi")
    public void buttonBold(View view) {
        PopupMenu popup = new PopupMenu(getApplicationContext(), view);
        popup.getMenuInflater().inflate(R.menu.text_alignment, popup.getMenu());

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem menu_item) {
                switch (menu_item.getItemId()) {
                    case R.id.Center:
                        Toast.makeText(CreateNoteActivity.this, "Center", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.Right:
                        break;
                }
                return true;
            }
        });

        MenuPopupHelper menuHelper = new MenuPopupHelper(getApplicationContext(), (MenuBuilder) popup.getMenu(), view);
        menuHelper.setForceShowIcon(true);
        menuHelper.setGravity(Gravity.END);
        menuHelper.show();
    }

    public void onPause() {
        if (TTS != null) {
            TTS.stop();
            TTS.shutdown();
        }
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        if (i == 1) {
            deleteRecursive(new File(Environment.getExternalStorageDirectory() + File.separator + "Remarques" + File.separator + ".Important File" + File.separator + ".R " + folderName));
            Intent i = new Intent(CreateNoteActivity.this, MainActivity.class);
            startActivity(i);
        } else {
            Intent i = new Intent(CreateNoteActivity.this, MainActivity.class);
            startActivity(i);
        }
    }

    private void displayPdf() {
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(CreateNoteActivity.this, RecyclerView.VERTICAL, false));
        List<File> pdfList = new ArrayList<>(findPdf(new File(Environment.getExternalStorageDirectory() + File.separator + "Remarques" + File.separator + ".Important File" + File.separator + ".R " + folderName)));
        FileAdapter fileAdapter = new FileAdapter(this, pdfList, this);
        recyclerView.setAdapter(fileAdapter);
    }

    public ArrayList<File> findPdf(File file) {
        ArrayList<File> arrayList = new ArrayList<>();

        if (file.exists()) {
            File[] files = file.listFiles();
            if (files != null) {
                for (File singleFile : files) {
                    if (singleFile.isDirectory() || singleFile.isHidden()) {
                        arrayList.addAll(findPdf(singleFile));
                    } else {
                        if (singleFile.getName().contains(".")) {
                            arrayList.add(singleFile);
                        }
                    }
                }
            }
        }
        return arrayList;
    }

    private void displayCustomPdf() {
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(CreateNoteActivity.this, RecyclerView.VERTICAL, false));
        List<File> pdfList = new ArrayList<>(findCustomPdf(new File(Environment.getExternalStorageDirectory() + File.separator + "Remarques" + File.separator + ".Important File" + File.separator + ".R " + folderName)));
        FileAdapter fileAdapter = new FileAdapter(this, pdfList, this);
        recyclerView.setAdapter(fileAdapter);
    }

    public ArrayList<File> findCustomPdf(File file) {
        ArrayList<File> arrayList = new ArrayList<>();

        if (file.exists()) {
            File[] files = file.listFiles();
            if (files != null) {
                for (File singleFile : files) {
                    if (singleFile.isDirectory() || singleFile.isHidden()) {
                        arrayList.addAll(findCustomPdf(singleFile));
                    } else {
                        switch (custom) {
                            case "file":
                                if (singleFile.getName().endsWith(".pdf") || singleFile.getName().endsWith(".docx") || singleFile.getName().endsWith(".pptx") || singleFile.getName().endsWith(".xlsx") || singleFile.getName().endsWith(".zip") || singleFile.getName().endsWith(".txt")) {
                                    arrayList.add(singleFile);
                                } else {
                                    showToastRed("No Files");
                                }
                                break;
                            case "video":
                                if (singleFile.getName().endsWith(".mp4")) {
                                    arrayList.add(singleFile);
                                } else {
                                    showToastRed("No Videos");
                                }
                                break;
                            case "audio":
                                if (singleFile.getName().endsWith(".amr")) {
                                    arrayList.add(singleFile);
                                } else {
                                    showToastRed("No Audios");
                                }
                                break;
                            case "music":
                                if (singleFile.getName().endsWith(".mp3")) {
                                    arrayList.add(singleFile);
                                } else {
                                    showToastRed("No Musics");
                                }
                                break;
                            case "pictures":
                                if (singleFile.getName().endsWith(".jpg") || singleFile.getName().endsWith(".jpeg") || singleFile.getName().endsWith(".png")) {
                                    arrayList.add(singleFile);
                                } else {
                                    showToastRed("No Pictures");
                                }
                                break;
                        }

                    }
                }
            } else {
                showToastRed("Nothing to display");
            }
        } else {
            showToastRed("Files not available");
        }
        return arrayList;
    }

    @Override
    public void onFileSelected(File file) {
        Uri uri = FileProvider.getUriForFile(this, CreateNoteActivity.this.getApplicationContext().getPackageName() + ".provider", file);
        Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
        if (file.getName().endsWith(".pdf")) {
            pdfIntent.setDataAndType(uri, "application/pdf");
        } else if (file.getName().endsWith(".mp4") || file.getName().endsWith(".amr") || file.getName().endsWith(".mp3")) {
            pdfIntent.setDataAndType(uri, "audio/x-wav");
        }
        pdfIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        try {
            startActivity(pdfIntent);
        } catch (ActivityNotFoundException e) {
            showToastRed("No Applications found to open this format file. You can download relevant application to view this file format");
        }
    }

    public void deleteRecursive(File file) {
        if (file.isDirectory())
            for (File child : Objects.requireNonNull(file.listFiles())) {
                child.delete();
                deleteRecursive(child);
            }
        file.delete();
    }

    @Override
    protected void onResume() {
        super.onResume();
        viewHolder = "CreateNote";
        displayPdf();
    }

    void showToast(String message) {
        Toast toast = new Toast(CreateNoteActivity.this);

        @SuppressLint("InflateParams") View view = LayoutInflater.from(CreateNoteActivity.this)
                .inflate(R.layout.toast_green, null);

        TextView tvMessage = view.findViewById(R.id.tvMessage);
        tvMessage.setText(message);

        toast.setView(view);
        toast.show();
    }

    void showToastRed(String message) {
        Toast toast = new Toast(CreateNoteActivity.this);

        @SuppressLint("InflateParams") View view = LayoutInflater.from(CreateNoteActivity.this)
                .inflate(R.layout.toast_layout, null);

        TextView tvMessage = view.findViewById(R.id.Message);
        tvMessage.setText(message);

        toast.setView(view);
        toast.show();
    }


}