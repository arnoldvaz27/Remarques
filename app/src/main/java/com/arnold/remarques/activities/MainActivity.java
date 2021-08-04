package com.arnold.remarques.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.arnold.remarques.DeviceFiles.DeviceFilesDisplay;
import com.arnold.remarques.R;
import com.arnold.remarques.adapters.NotesAdapter;
import com.arnold.remarques.database.NotesDatabase;
import com.arnold.remarques.entities.Note;
import com.arnold.remarques.listeners.NotesListeners;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements NotesListeners {

    public static final int REQUEST_CODE_ADD_NOTE = 1;
    public static final int REQUEST_CODE_UPDATE_NOTE = 2;
    public static final int REQUEST_CODE_SHOW_NOTES = 3;
    public static final int REQUEST_CODE_SELECT_IMAGE = 4;
    public static final int REQUEST_CODE_STORAGE_PERMISSION = 5;

    private RecyclerView notesRecyclerView;

    private List<Note> noteList;
    private NotesAdapter notesAdapter;

    private Typeface typeface1, typeface2, typeface3, typeface4, typeface5, typeface6;
    private AlertDialog dialogAddURL,dialogAddText;
    private int noteClickedPosition = 0;
    private final int REQ_CODE = 100;
    private EditText inputSearch;
    private TextView myNote;
    private LinearLayoutManager linearLayoutManager, mainOption;
    private StaggeredGridLayoutManager staggeredGridLayoutManager;
    private String appOpen, fontSettings, richText, notesReturn, layoutType, fullLayoutOption;
    private String fieldsVisibleImage, fieldsVisibleTitle, fieldsVisibleSubTitle, fieldsVisibleNote, fieldsVisibleDateCreated, fieldsVisibleDateEdited,
            fieldsVisibleURL, fieldsVisibleBackgroundColor,fieldsVisibleLayout;
    String keeper = "";

    private AlertDialog speechDialog, fullDetailOption;

    private BottomSheetDialog bottomSheetDialog;

    // search declaration

    ScrollView scrollView;
    TextView moreText,lessText;
    LinearLayout firstLayout,secondLayout;
    ImageView downImage,upImage;
    public static String createNoteFolder;

    //finding data

    LinearLayout findingImage,findingTitle,findingSubTitle,findingNote,findingDateCreated,findingURL,findingBG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(getResources().getColor(R.color.colorDelete));
        setContentView(R.layout.main);

/*        SharedPreferences getShared6 = getSharedPreferences("settings", MODE_PRIVATE);
        notesReturn = getShared6.getString("Note Return", null);

        final SharedPreferences shard6 = getSharedPreferences("settings",MODE_PRIVATE);
        final SharedPreferences.Editor editor6 = shard6.edit();

        if (notesReturn == null) {
            editor6.putString("Note Return", "1");
        }
        editor6.apply();*/

        scrollView = findViewById(R.id.horizontal);
        moreText = findViewById(R.id.LayoutName3);
        lessText = findViewById(R.id.LayoutName4);
        firstLayout = findViewById(R.id.firstSmartLayout);
        secondLayout = findViewById(R.id.secondSmartLayout);
        downImage = findViewById(R.id.noteDown);
        upImage = findViewById(R.id.noteUp);

        findingImage = findViewById(R.id.findImage);
        findingTitle = findViewById(R.id.findTitle);
        findingSubTitle = findViewById(R.id.findSubTitle);
        findingNote = findViewById(R.id.findNote);
        findingDateCreated = findViewById(R.id.findDateTime);
        findingURL = findViewById(R.id.findURL);
        findingBG = findViewById(R.id.findBG);

        First();


        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();
        if (Intent.ACTION_SEND.equals(action) && type != null) {
            if (type.startsWith("image/")) {
                handleSendImage(intent); // Handle single image being sent
            } else if (type.startsWith("text/")) {
                handleSendText(intent);
            }
        }
    }

    private void handleSendText(Intent intent) {
        String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
        if (sharedText != null) {
            if (sharedText.startsWith("https://") || sharedText.startsWith("http://")) {
                if (dialogAddURL == null) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    View view = LayoutInflater.from(this).inflate(
                            R.layout.layout_add_url, (ViewGroup) findViewById(R.id.layoutAddUrlContainer)
                    );
                    builder.setView(view);

                    dialogAddURL = builder.create();
                    if (dialogAddURL.getWindow() != null) {
                        dialogAddURL.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                    }

                    final EditText inputURL = view.findViewById(R.id.inputURL);

                    inputURL.setText(sharedText);
                    inputURL.setSelection(inputURL.getText().length());
                    inputURL.requestFocus();
                    view.findViewById(R.id.textAdd).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (inputURL.getText().toString().trim().isEmpty()) {
                                Toast.makeText(MainActivity.this, "Enter URL", Toast.LENGTH_SHORT).show();
                            } else if (!Patterns.WEB_URL.matcher(inputURL.getText().toString()).matches()) {
                                Toast.makeText(MainActivity.this, "Enter Valid URL", Toast.LENGTH_SHORT).show();
                            } else {
                                Intent intent = new Intent(getApplicationContext(), CreateNoteActivity.class);
                                intent.putExtra("isFromQuickActions", true);
                                intent.putExtra("quickActionType", "URL");
                                intent.putExtra("URL", inputURL.getText().toString());
                                createNoteFolder = new SimpleDateFormat("dd MM yyyy HH:mm:ss a", Locale.getDefault()).format(new Date());
                                startActivityForResult(intent, REQUEST_CODE_ADD_NOTE);
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
            } else {
                if (dialogAddText == null) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    View view = LayoutInflater.from(this).inflate(
                            R.layout.layoutaddtext, (ViewGroup) findViewById(R.id.layoutAddTextContainer)
                    );
                    builder.setView(view);

                    dialogAddText = builder.create();
                    if (dialogAddText.getWindow() != null) {
                        dialogAddText.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                    }

                    final EditText inputText = view.findViewById(R.id.inputText);

                    inputText.setText(sharedText);
                    inputText.setSelection(inputText.getText().length());
                    inputText.requestFocus();
                    view.findViewById(R.id.textAdd).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (inputText.getText().toString().trim().isEmpty()) {
                                Toast.makeText(MainActivity.this, "Enter Text", Toast.LENGTH_SHORT).show();
                            }else {
                                Intent intent = new Intent(getApplicationContext(), CreateNoteActivity.class);
                                intent.putExtra("isFromQuickActions", true);
                                intent.putExtra("quickActionType", "Text");
                                intent.putExtra("Text", inputText.getText().toString());
                                createNoteFolder = new SimpleDateFormat("dd MM yyyy HH:mm:ss a", Locale.getDefault()).format(new Date());
                                startActivityForResult(intent, REQUEST_CODE_ADD_NOTE);
                                dialogAddText.dismiss();
                            }
                        }
                    });

                    view.findViewById(R.id.textCancel).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialogAddText.dismiss();
                        }
                    });
                }

                dialogAddText.show();
            }
        }
    }

    void handleSendImage(Intent intent) {
        Uri selectImageUri = intent.getParcelableExtra(Intent.EXTRA_STREAM);
        if (selectImageUri != null) {
            try {
                String selectedImagePath = getPathFromUri(selectImageUri);
                Intent intent2 = new Intent(getApplicationContext(), CreateNoteActivity.class);
                intent2.putExtra("isFromQuickActions", true);
                intent2.putExtra("quickActionType", "image");
                intent2.putExtra("imagePath", selectedImagePath);
                createNoteFolder = new SimpleDateFormat("dd MM yyyy HH:mm:ss a", Locale.getDefault()).format(new Date());
                startActivityForResult(intent2, REQUEST_CODE_ADD_NOTE);

            } catch (Exception e) {
                Toast.makeText(this, "No Image Found", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void First() {

        SharedPreferences settingLayoutType = getSharedPreferences("settings", MODE_PRIVATE);
        layoutType = settingLayoutType.getString("layout type", null);

        final SharedPreferences settingLayoutType1 = getSharedPreferences("settings", MODE_PRIVATE);
        final SharedPreferences.Editor settingLayoutType2 = settingLayoutType1.edit();
        if (layoutType == null) {
            settingLayoutType2.putString("layout type", "1");
        }
        settingLayoutType2.apply();

        SharedPreferences LayoutFull = getSharedPreferences("layoutFull", MODE_PRIVATE);
        fullLayoutOption = LayoutFull.getString("layout number", null);

        final SharedPreferences LayoutFull1 = getSharedPreferences("layoutFull", MODE_PRIVATE);
        final SharedPreferences.Editor LayoutFull2 = LayoutFull1.edit();
        if (fullLayoutOption == null) {
            LayoutFull2.putString("layout number", "1");
        }
        LayoutFull2.apply();

        SharedPreferences fieldsVisibility = getSharedPreferences("field visibility", MODE_PRIVATE);
        fieldsVisibleImage = fieldsVisibility.getString("Image", null);
        fieldsVisibleTitle = fieldsVisibility.getString("Title", null);
        fieldsVisibleSubTitle = fieldsVisibility.getString("Sub Title", null);
        fieldsVisibleNote = fieldsVisibility.getString("Note", null);
        fieldsVisibleDateCreated = fieldsVisibility.getString("Date Created", null);
        fieldsVisibleDateEdited = fieldsVisibility.getString("Date Edited", null);
        fieldsVisibleURL = fieldsVisibility.getString("URL", null);
        fieldsVisibleBackgroundColor = fieldsVisibility.getString("Background Color", null);
        fieldsVisibleLayout = fieldsVisibility.getString("Layout Field", null);

        final SharedPreferences fieldsVisibility1 = getSharedPreferences("field visibility", MODE_PRIVATE);
        final SharedPreferences.Editor fieldsVisibility2 = fieldsVisibility1.edit();
        if (fieldsVisibleImage == null && fieldsVisibleTitle == null && fieldsVisibleSubTitle == null && fieldsVisibleDateCreated == null
                && fieldsVisibleDateEdited == null && fieldsVisibleURL == null && fieldsVisibleBackgroundColor == null && fieldsVisibleNote == null && fieldsVisibleLayout == null) {
            fieldsVisibility2.putString("Image", "1");
            fieldsVisibility2.putString("Title", "1");
            fieldsVisibility2.putString("Sub Title", "1");
            fieldsVisibility2.putString("Note", "0");
            fieldsVisibility2.putString("Date Created", "1");
            fieldsVisibility2.putString("Date Edited", "0");
            fieldsVisibility2.putString("URL", "0");
            fieldsVisibility2.putString("Background Color", "1");
            fieldsVisibility2.putString("Layout Field", "1");
        }
        fieldsVisibility2.apply();

        SharedPreferences layout = getSharedPreferences("layout", MODE_PRIVATE);
        appOpen = layout.getString("layout number", null);

        final SharedPreferences layout1 = getSharedPreferences("layout", MODE_PRIVATE);
        final SharedPreferences.Editor layout2 = layout1.edit();
        if (appOpen == null) {
            layout2.putString("layout number", "1");
        }
        layout2.apply();

        SharedPreferences settingsFontNumber = getSharedPreferences("settings", MODE_PRIVATE);
        fontSettings = settingsFontNumber.getString("font number", null);

        final SharedPreferences settingsFontNumber1 = getSharedPreferences("settings", MODE_PRIVATE);
        final SharedPreferences.Editor settingsFontNumber2 = settingsFontNumber1.edit();
        if (fontSettings == null) {
            settingsFontNumber2.putString("font number", "1");
        }
        settingsFontNumber2.apply();

        SharedPreferences settingsRichText = getSharedPreferences("settings", MODE_PRIVATE);
        richText = settingsRichText.getString("rich text", null);

        final SharedPreferences settingsRichText1 = getSharedPreferences("settings", MODE_PRIVATE);
        final SharedPreferences.Editor settingsRichText2 = settingsRichText1.edit();
        if (richText == null) {
            settingsRichText2.putString("rich text", "1");
        }
        settingsRichText2.apply();

        inputSearch = findViewById(R.id.inputSearch);
        myNote = findViewById(R.id.MyNotes);
        SharedPreferences SettingsFontNumber = getSharedPreferences("settings", MODE_PRIVATE);
        fontSettings = SettingsFontNumber.getString("font number", null);

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
                inputSearch.setTypeface(typeface1);
                myNote.setTypeface(typeface1);
                break;
            case "2":
                inputSearch.setTypeface(typeface2);
                myNote.setTypeface(typeface2);
                break;
            case "3":
                inputSearch.setTypeface(typeface3);
                myNote.setTypeface(typeface3);
                break;
            case "4":
                inputSearch.setTypeface(typeface4);
                myNote.setTypeface(typeface4);
                break;
            case "5":
                inputSearch.setTypeface(typeface5);
                myNote.setTypeface(typeface5);
                break;
            case "6":
                inputSearch.setTypeface(typeface6);
                myNote.setTypeface(typeface6);
                break;
        }

        ImageView imageAddNoteMain = findViewById(R.id.imageAddNoteMain);
        imageAddNoteMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNoteFolder = new SimpleDateFormat("dd MM yyyy HH:mm:ss a", Locale.getDefault()).format(new Date());
                startActivityForResult(
                        new Intent(MainActivity.this, CreateNoteActivity.class),
                        REQUEST_CODE_ADD_NOTE
                );
            }
        });
/*        notesRecyclerView = findViewById(R.id.notesRecyclerView);
        notesRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));*/
        notesRecyclerView = findViewById(R.id.notesRecyclerView);

        SharedPreferences settingsLayoutType = getSharedPreferences("settings", MODE_PRIVATE);
        layoutType = settingsLayoutType.getString("layout type", null);

        switch (layoutType) {
            case "1": {
                findViewById(R.id.fullOption).setVisibility(View.GONE);
                findViewById(R.id.mainOptions).setVisibility(View.VISIBLE);

                SharedPreferences getShared4 = getSharedPreferences("layout", MODE_PRIVATE);
                appOpen = getShared4.getString("layout number", null);

                switch (appOpen) {
                    case "1":
                        linearLayoutManager = new LinearLayoutManager(MainActivity.this);
                        linearLayoutManager.setReverseLayout(true);
                        linearLayoutManager.setStackFromEnd(true);
                        notesRecyclerView.setLayoutManager(linearLayoutManager);
                        findViewById(R.id.listView).setVisibility(View.GONE);
                        findViewById(R.id.listViewUp).setVisibility(View.GONE);
                        findViewById(R.id.DashViewLeft).setVisibility(View.GONE);
                        findViewById(R.id.DashViewRight).setVisibility(View.GONE);
                        findViewById(R.id.DashView).setVisibility(View.VISIBLE);
                        findViewById(R.id.listViewDown).setVisibility(View.VISIBLE);
                        break;
                    case "2":
                        linearLayoutManager = new LinearLayoutManager(MainActivity.this);
                        linearLayoutManager.setReverseLayout(false);
                        linearLayoutManager.setStackFromEnd(false);
                        notesRecyclerView.setLayoutManager(linearLayoutManager);
                        findViewById(R.id.listViewDown).setVisibility(View.GONE);
                        findViewById(R.id.listViewUp).setVisibility(View.VISIBLE);
                        findViewById(R.id.DashViewLeft).setVisibility(View.GONE);
                        findViewById(R.id.DashViewRight).setVisibility(View.GONE);
                        findViewById(R.id.DashView).setVisibility(View.VISIBLE);
                        findViewById(R.id.listView).setVisibility(View.GONE);
                        break;
                    case "3":
                        linearLayoutManager = new LinearLayoutManager(MainActivity.this);
                        linearLayoutManager.setReverseLayout(true);
                        linearLayoutManager.setStackFromEnd(true);
                        notesRecyclerView.setLayoutManager(linearLayoutManager);
                        findViewById(R.id.listViewDown).setVisibility(View.VISIBLE);
                        findViewById(R.id.listViewUp).setVisibility(View.GONE);
                        findViewById(R.id.DashViewLeft).setVisibility(View.GONE);
                        findViewById(R.id.DashViewRight).setVisibility(View.GONE);
                        findViewById(R.id.DashView).setVisibility(View.VISIBLE);
                        findViewById(R.id.listView).setVisibility(View.GONE);
                        break;
                    case "4":
                        notesRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
                        findViewById(R.id.DashView).setVisibility(View.GONE);
                        findViewById(R.id.listView).setVisibility(View.VISIBLE);
                        findViewById(R.id.listViewUp).setVisibility(View.GONE);
                        findViewById(R.id.listViewDown).setVisibility(View.GONE);
                        findViewById(R.id.DashViewLeft).setVisibility(View.VISIBLE);
                        findViewById(R.id.DashViewRight).setVisibility(View.GONE);
                        findViewById(R.id.listView).setVisibility(View.VISIBLE);
                        findViewById(R.id.DashView).setVisibility(View.GONE);
                        break;
                    case "5":
                        notesRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
                        staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
                        staggeredGridLayoutManager.setReverseLayout(true);
                        notesRecyclerView.setLayoutManager(staggeredGridLayoutManager);
                        findViewById(R.id.DashViewLeft).setVisibility(View.GONE);
                        findViewById(R.id.DashViewRight).setVisibility(View.VISIBLE);
                        findViewById(R.id.listView).setVisibility(View.VISIBLE);
                        findViewById(R.id.listViewUp).setVisibility(View.GONE);
                        findViewById(R.id.listViewDown).setVisibility(View.GONE);
                        findViewById(R.id.DashView).setVisibility(View.GONE);
                        break;
                    case "6":
                        notesRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
                        staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
                        staggeredGridLayoutManager.setReverseLayout(false);
                        notesRecyclerView.setLayoutManager(staggeredGridLayoutManager);
                        findViewById(R.id.DashViewLeft).setVisibility(View.VISIBLE);
                        findViewById(R.id.DashViewRight).setVisibility(View.GONE);
                        findViewById(R.id.listView).setVisibility(View.VISIBLE);
                        findViewById(R.id.listViewUp).setVisibility(View.GONE);
                        findViewById(R.id.listViewDown).setVisibility(View.GONE);
                        findViewById(R.id.DashView).setVisibility(View.GONE);
                        break;
                }
                break;
            }
            case "2": {
                findViewById(R.id.mainOptions).setVisibility(View.GONE);
                findViewById(R.id.fullOption).setVisibility(View.VISIBLE);

                SharedPreferences getShared9 = getSharedPreferences("layoutFull", MODE_PRIVATE);
                fullLayoutOption = getShared9.getString("layout number", null);

                switch (fullLayoutOption) {
                    case "1":
                        linearLayoutManager = new LinearLayoutManager(MainActivity.this);
                        linearLayoutManager.setReverseLayout(true);
                        linearLayoutManager.setStackFromEnd(true);
                        notesRecyclerView.setLayoutManager(linearLayoutManager);
                        break;
                    case "2":
                        linearLayoutManager = new LinearLayoutManager(MainActivity.this);
                        linearLayoutManager.setReverseLayout(false);
                        linearLayoutManager.setStackFromEnd(false);
                        notesRecyclerView.setLayoutManager(linearLayoutManager);
                        break;
                    case "3":
                        notesRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
                        staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
                        staggeredGridLayoutManager.setReverseLayout(true);
                        notesRecyclerView.setLayoutManager(staggeredGridLayoutManager);
                        break;
                    case "4":
                        notesRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
                        staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
                        staggeredGridLayoutManager.setReverseLayout(false);
                        notesRecyclerView.setLayoutManager(staggeredGridLayoutManager);
                        break;
                }
                break;
            }
        }

        findViewById(R.id.fullOption).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fullOption();
            }
        });
        final SharedPreferences LayoutEditor = getSharedPreferences("layout", MODE_PRIVATE);
        final SharedPreferences.Editor editor = LayoutEditor.edit();

        findViewById(R.id.listView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLayoutManager = new LinearLayoutManager(MainActivity.this);
                linearLayoutManager.setReverseLayout(true);
                linearLayoutManager.setStackFromEnd(true);
                notesRecyclerView.setLayoutManager(linearLayoutManager);
                findViewById(R.id.listView).setVisibility(View.GONE);
                findViewById(R.id.listViewUp).setVisibility(View.GONE);
                findViewById(R.id.DashViewLeft).setVisibility(View.GONE);
                findViewById(R.id.DashViewRight).setVisibility(View.GONE);
                findViewById(R.id.DashView).setVisibility(View.VISIBLE);
                findViewById(R.id.listViewDown).setVisibility(View.VISIBLE);
                editor.putString("layout number", "1");
                editor.apply();
            }
        });
        findViewById(R.id.listViewDown).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLayoutManager = new LinearLayoutManager(MainActivity.this);
                linearLayoutManager.setReverseLayout(false);
                linearLayoutManager.setStackFromEnd(false);
                notesRecyclerView.setLayoutManager(linearLayoutManager);
                findViewById(R.id.listViewDown).setVisibility(View.GONE);
                findViewById(R.id.listViewUp).setVisibility(View.VISIBLE);
                findViewById(R.id.DashViewLeft).setVisibility(View.GONE);
                findViewById(R.id.DashViewRight).setVisibility(View.GONE);
                findViewById(R.id.DashView).setVisibility(View.VISIBLE);
                findViewById(R.id.listView).setVisibility(View.GONE);
                editor.putString("layout number", "2");
                editor.apply();
            }
        });
        findViewById(R.id.listViewUp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLayoutManager = new LinearLayoutManager(MainActivity.this);
                linearLayoutManager.setReverseLayout(true);
                linearLayoutManager.setStackFromEnd(true);
                notesRecyclerView.setLayoutManager(linearLayoutManager);
                findViewById(R.id.listViewDown).setVisibility(View.VISIBLE);
                findViewById(R.id.listViewUp).setVisibility(View.GONE);
                findViewById(R.id.DashViewLeft).setVisibility(View.GONE);
                findViewById(R.id.DashViewRight).setVisibility(View.GONE);
                findViewById(R.id.DashView).setVisibility(View.VISIBLE);
                findViewById(R.id.listView).setVisibility(View.GONE);
                editor.putString("layout number", "3");
                editor.apply();
            }
        });

        findViewById(R.id.DashView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notesRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
                findViewById(R.id.DashView).setVisibility(View.GONE);
                findViewById(R.id.listView).setVisibility(View.VISIBLE);
                findViewById(R.id.listViewUp).setVisibility(View.GONE);
                findViewById(R.id.listViewDown).setVisibility(View.GONE);
                findViewById(R.id.DashViewLeft).setVisibility(View.VISIBLE);
                findViewById(R.id.DashViewRight).setVisibility(View.GONE);
                findViewById(R.id.listView).setVisibility(View.VISIBLE);
                findViewById(R.id.DashView).setVisibility(View.GONE);
                editor.putString("layout number", "4");
                editor.apply();
            }
        });

        findViewById(R.id.DashViewLeft).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notesRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
                staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
                staggeredGridLayoutManager.setReverseLayout(true);
                notesRecyclerView.setLayoutManager(staggeredGridLayoutManager);
                findViewById(R.id.DashViewLeft).setVisibility(View.GONE);
                findViewById(R.id.DashViewRight).setVisibility(View.VISIBLE);
                findViewById(R.id.listView).setVisibility(View.VISIBLE);
                findViewById(R.id.listViewUp).setVisibility(View.GONE);
                findViewById(R.id.listViewDown).setVisibility(View.GONE);
                findViewById(R.id.DashView).setVisibility(View.GONE);
                editor.putString("layout number", "5");
                editor.apply();
            }
        });

        findViewById(R.id.DashViewRight).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notesRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
                staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
                staggeredGridLayoutManager.setReverseLayout(false);
                notesRecyclerView.setLayoutManager(staggeredGridLayoutManager);
                findViewById(R.id.DashViewLeft).setVisibility(View.VISIBLE);
                findViewById(R.id.DashViewRight).setVisibility(View.GONE);
                findViewById(R.id.listView).setVisibility(View.VISIBLE);
                findViewById(R.id.listViewUp).setVisibility(View.GONE);
                findViewById(R.id.listViewDown).setVisibility(View.GONE);
                findViewById(R.id.DashView).setVisibility(View.GONE);
                editor.putString("layout number", "6");
                editor.apply();
            }
        });

        findViewById(R.id.imageDefault).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Content Refreshed", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(MainActivity.this, MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                finish();
                overridePendingTransition(0, 0);
                startActivity(i);
                overridePendingTransition(0, 0);
            }
        });

        noteList = new ArrayList<>();
        notesAdapter = new NotesAdapter(noteList, this);
        notesRecyclerView.setAdapter(notesAdapter);

        getNotes(REQUEST_CODE_SHOW_NOTES, false);

        findViewById(R.id.noteSearch).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,
                        RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Need to speak");
                try {
                    startActivityForResult(intent, REQ_CODE);
                } catch (ActivityNotFoundException a) {
                    Toast.makeText(getApplicationContext(),
                            "Sorry your device not supported",
                            Toast.LENGTH_SHORT).show();
                }*/
                SpeechRecording();
            }
        });

        inputSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                notesAdapter.cancelTimer();
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (noteList.size() != 0) {
                    notesAdapter.searchNotes(s.toString());
                }
            }
        });

        findViewById(R.id.imageAddNote).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
/*                startActivityForResult(
                        new Intent(getApplicationContext(), CreateNoteActivity.class),
                        REQUEST_CODE_ADD_NOTE
                );*/
                BottomLayout();
            }
        });
        findViewById(R.id.imageAddImage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager
                        .PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]
                                    {Manifest.permission.READ_EXTERNAL_STORAGE},
                            REQUEST_CODE_STORAGE_PERMISSION
                    );
                } else {
                    selectImage();
                }
            }
        });
        findViewById(R.id.imageAddWeb).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddURLDialog();
            }
        });
        findViewById(R.id.wordCounter).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,WordCounter.class);
                startActivity(intent);
            }
        });

        downImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upImage.setVisibility(View.VISIBLE);
                downImage.setVisibility(View.GONE);
                scrollView.setVisibility(View.VISIBLE);
            }
        });

        upImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upImage.setVisibility(View.GONE);
                downImage.setVisibility(View.VISIBLE);
                scrollView.setVisibility(View.GONE);
                inputSearch.setText("");
            }
        });

        moreText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moreText.setVisibility(View.GONE);
                secondLayout.setVisibility(View.VISIBLE);
                lessText.setVisibility(View.VISIBLE);
            }
        });
        lessText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                secondLayout.setVisibility(View.GONE);
                lessText.setVisibility(View.GONE);
                moreText.setVisibility(View.VISIBLE);
            }
        });
        findingImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputSearch.setText("");
                inputSearch.setText("Images");
                inputSearch.setSelection(inputSearch.getText().length());
                inputSearch.requestFocus();
            }
        });
        findingTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputSearch.setText("");
                inputSearch.setText("Title");
                inputSearch.setSelection(inputSearch.getText().length());
                inputSearch.requestFocus();
            }
        });
        findingSubTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputSearch.setText("");
                inputSearch.setText("Sub-Title");
                inputSearch.setSelection(inputSearch.getText().length());
                inputSearch.requestFocus();
            }
        });
        findingNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputSearch.setText("");
                inputSearch.setText("Note");
                inputSearch.setSelection(inputSearch.getText().length());
                inputSearch.requestFocus();
            }
        });
        findingDateCreated.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputSearch.setText("");
                inputSearch.setText("Date Created");
                inputSearch.setSelection(inputSearch.getText().length());
                inputSearch.requestFocus();
            }
        });
        findingURL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputSearch.setText("");
                inputSearch.setText("URL");
                inputSearch.setSelection(inputSearch.getText().length());
                inputSearch.requestFocus();
            }
        });
        findingBG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputSearch.setText("");
                inputSearch.setText("Background Color");
                inputSearch.setSelection(inputSearch.getText().length());
                inputSearch.requestFocus();
            }
        });

    }

    private void fullOption() {
        if (fullDetailOption == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            View view = LayoutInflater.from(this).inflate(
                    R.layout.detail_options, (ViewGroup) findViewById(R.id.layoutMoreDetailOptions)
            );

            builder.setView(view);
            builder.setCancelable(false);
            fullDetailOption = builder.create();
            if (fullDetailOption.getWindow() != null) {
                fullDetailOption.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            }

            final LinearLayout linearLayout, linearLayout1, linearLayout2,linearLayout3;
            final ImageView imageView, imageView1; //list and grid main
            final ImageView imageView2, imageView3, imageView4, imageView5; //list and grid options
            final TextView textView, textView1, textView2, textView3;

            final ImageView imageView6, imageView7, imageView8, imageView9, imageView10, imageView11, imageView12, imageView13,imageView14;
            final TextView textView4, textView5, textView6, textView7, textView8, textView9, textView10, textView11,textView12,textView13,textView14;
            final SwitchCompat switchCompat, switchCompat1;
            final RelativeLayout relativeLayout, relativeLayout1;
            final CheckBox checkBox, checkBox1, checkBox2, checkBox3, checkBox4, checkBox5, checkBox6, checkBox7,checkBox8;
            final String fieldsVisibleImage2, fieldsVisibleTitle2, fieldsVisibleSubTitle2, fieldsVisibleNote2, fieldsVisibleDateEdited2, fieldsVisibleDateCreated2, fieldsVisibleURL2, fieldsVisibleBackgroundColor2, fieldsVisibleLayout2;

            imageView6 = view.findViewById(R.id.imImage);
            imageView7 = view.findViewById(R.id.imTitle);
            imageView8 = view.findViewById(R.id.imSubTitle);
            imageView9 = view.findViewById(R.id.imDateCreated);
            imageView10 = view.findViewById(R.id.imDateEdited);
            imageView11 = view.findViewById(R.id.imURL);
            imageView12 = view.findViewById(R.id.imBackgroundColor);
            imageView13 = view.findViewById(R.id.imNote);
            imageView14 = view.findViewById(R.id.imLayout);

            textView4 = view.findViewById(R.id.textImage);
            textView5 = view.findViewById(R.id.textTitle);
            textView6 = view.findViewById(R.id.textSubTitle);
            textView7 = view.findViewById(R.id.textDateCreated);
            textView8 = view.findViewById(R.id.textDateEdited);
            textView9 = view.findViewById(R.id.textURL);
            textView10 = view.findViewById(R.id.textBackgroundColor);
            textView11 = view.findViewById(R.id.textNote);
            textView12 = view.findViewById(R.id.textLayout);

            switchCompat = view.findViewById(R.id.switchCompat);
            switchCompat1 = view.findViewById(R.id.switchCompat2);

            relativeLayout = view.findViewById(R.id.mainLayout);
            relativeLayout1 = view.findViewById(R.id.EditingVisibilityLayout);

            checkBox = view.findViewById(R.id.imageChecked);
            checkBox1 = view.findViewById(R.id.titleChecked);
            checkBox2 = view.findViewById(R.id.subTitleChecked);
            checkBox3 = view.findViewById(R.id.dateCreatedChecked);
            checkBox4 = view.findViewById(R.id.dateEditedChecked);
            checkBox5 = view.findViewById(R.id.urlChecked);
            checkBox6 = view.findViewById(R.id.backgroundColorChecked);
            checkBox7 = view.findViewById(R.id.noteChecked);
            checkBox8 = view.findViewById(R.id.layoutChecked);

            SharedPreferences fieldsVisibility = getSharedPreferences("field visibility", MODE_PRIVATE);
            fieldsVisibleImage2 = fieldsVisibility.getString("Image", null);
            fieldsVisibleTitle2 = fieldsVisibility.getString("Title", null);
            fieldsVisibleSubTitle2 = fieldsVisibility.getString("Sub Title", null);
            fieldsVisibleNote2 = fieldsVisibility.getString("Note", null);
            fieldsVisibleDateCreated2 = fieldsVisibility.getString("Date Created", null);
            fieldsVisibleDateEdited2 = fieldsVisibility.getString("Date Edited", null);
            fieldsVisibleURL2 = fieldsVisibility.getString("URL", null);
            fieldsVisibleBackgroundColor2 = fieldsVisibility.getString("Background Color", null);
            fieldsVisibleLayout2 = fieldsVisibility.getString("Layout Field", null);

            if (fieldsVisibleImage2.equals("1")) {
                imageView6.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
                textView4.setTextColor(getResources().getColor(R.color.colorAccent));
                checkBox.setChecked(true);
            }
            if (fieldsVisibleTitle2.equals("1")) {
                imageView7.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
                textView5.setTextColor(getResources().getColor(R.color.colorAccent));
                checkBox1.setChecked(true);
            }
            if (fieldsVisibleSubTitle2.equals("1")) {
                imageView8.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
                textView6.setTextColor(getResources().getColor(R.color.colorAccent));
                checkBox2.setChecked(true);
            }
            if (fieldsVisibleDateCreated2.equals("1")) {
                imageView9.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
                textView7.setTextColor(getResources().getColor(R.color.colorAccent));
                checkBox3.setChecked(true);
            }
            if (fieldsVisibleDateEdited2.equals("1")) {
                imageView10.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
                textView8.setTextColor(getResources().getColor(R.color.colorAccent));
                checkBox4.setChecked(true);
            }
            if (fieldsVisibleURL2.equals("1")) {
                imageView11.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
                textView9.setTextColor(getResources().getColor(R.color.colorAccent));
                checkBox5.setChecked(true);
            }
            if (fieldsVisibleBackgroundColor2.equals("1")) {
                imageView12.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
                textView10.setTextColor(getResources().getColor(R.color.colorAccent));
                checkBox6.setChecked(true);
            }
            if (fieldsVisibleNote2.equals("1")) {
                imageView13.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
                textView11.setTextColor(getResources().getColor(R.color.colorAccent));
                checkBox7.setChecked(true);
            }
            if (fieldsVisibleLayout2.equals("1")) {
                imageView14.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
                textView12.setTextColor(getResources().getColor(R.color.colorAccent));
                checkBox8.setChecked(true);
            }

            linearLayout = view.findViewById(R.id.listViewOptions);
            linearLayout1 = view.findViewById(R.id.gridViewOptions);
            linearLayout2 = view.findViewById(R.id.differentLayoutText4);
            linearLayout3 = view.findViewById(R.id.differentLayoutText5);

            imageView = view.findViewById(R.id.listView);
            imageView1 = view.findViewById(R.id.gridView);
            textView = view.findViewById(R.id.listViewText);
            textView1 = view.findViewById(R.id.gridViewText);

            textView2 = view.findViewById(R.id.TextUp);
            textView3 = view.findViewById(R.id.TextDown);
            textView13 = view.findViewById(R.id.TextLeftUp);
            textView14 = view.findViewById(R.id.TextLeftDown);

            imageView2 = view.findViewById(R.id.listViewDown);
            imageView3 = view.findViewById(R.id.listViewUp);
            imageView4 = view.findViewById(R.id.gridViewDown);
            imageView5 = view.findViewById(R.id.gridViewUp);

            SharedPreferences getShared7 = getSharedPreferences("layoutFull", MODE_PRIVATE);
            fullLayoutOption = getShared7.getString("layout number", null);

            switch (fullLayoutOption) {
                case "1":
                    imageView.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
                    textView.setTextColor(getResources().getColor(R.color.colorAccent));
                    linearLayout.setVisibility(View.VISIBLE);
                    linearLayout2.setVisibility(View.VISIBLE);
                    linearLayout1.setVisibility(View.GONE);
                    linearLayout3.setVisibility(View.GONE);
                    textView3.setTextColor(getResources().getColor(R.color.colorAccent));
                    imageView2.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
                    break;
                case "2":
                    imageView.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
                    textView.setTextColor(getResources().getColor(R.color.colorAccent));
                    linearLayout.setVisibility(View.VISIBLE);
                    linearLayout2.setVisibility(View.VISIBLE);
                    linearLayout1.setVisibility(View.GONE);
                    linearLayout3.setVisibility(View.GONE);
                    textView2.setTextColor(getResources().getColor(R.color.colorAccent));
                    imageView3.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
                    break;
                case "3":
                    imageView1.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
                    textView1.setTextColor(getResources().getColor(R.color.colorAccent));
                    linearLayout1.setVisibility(View.VISIBLE);
                    linearLayout3.setVisibility(View.VISIBLE);
                    linearLayout.setVisibility(View.GONE);
                    linearLayout2.setVisibility(View.GONE);
                    textView14.setTextColor(getResources().getColor(R.color.colorAccent));
                    imageView4.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
                    break;
                case "4":
                    imageView1.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
                    textView1.setTextColor(getResources().getColor(R.color.colorAccent));
                    linearLayout1.setVisibility(View.VISIBLE);
                    linearLayout3.setVisibility(View.VISIBLE);
                    linearLayout.setVisibility(View.GONE);
                    linearLayout2.setVisibility(View.GONE);
                    textView13.setTextColor(getResources().getColor(R.color.colorAccent));
                    imageView5.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
                    break;
            }


            final SharedPreferences shard7 = getSharedPreferences("layoutFull", MODE_PRIVATE);
            final SharedPreferences.Editor editor7 = shard7.edit();

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    imageView.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
                    textView.setTextColor(getResources().getColor(R.color.colorAccent));
                    imageView2.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
                    textView3.setTextColor(getResources().getColor(R.color.colorAccent));
                    imageView1.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorWhite)));
                    textView1.setTextColor(getResources().getColor(R.color.colorWhite));
                    imageView3.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorWhite)));
                    textView2.setTextColor(getResources().getColor(R.color.colorWhite));
                    imageView4.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorWhite)));
                    imageView5.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorWhite)));
                    linearLayout.setVisibility(View.VISIBLE);
                    linearLayout1.setVisibility(View.GONE);
                    linearLayout3.setVisibility(View.GONE);
                    linearLayout2.setVisibility(View.VISIBLE);
                    editor7.putString("layout number", "1");
                    editor7.apply();
                }
            });

            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    imageView.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
                    textView.setTextColor(getResources().getColor(R.color.colorAccent));
                    imageView2.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
                    textView3.setTextColor(getResources().getColor(R.color.colorAccent));
                    imageView1.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorWhite)));
                    textView1.setTextColor(getResources().getColor(R.color.colorWhite));
                    imageView3.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorWhite)));
                    textView2.setTextColor(getResources().getColor(R.color.colorWhite));
                    imageView4.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorWhite)));
                    imageView5.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorWhite)));
                    linearLayout.setVisibility(View.VISIBLE);
                    linearLayout1.setVisibility(View.GONE);
                    linearLayout3.setVisibility(View.GONE);
                    linearLayout2.setVisibility(View.VISIBLE);
                    editor7.putString("layout number", "1");
                    editor7.apply();
                }
            });

            imageView2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    imageView.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
                    textView.setTextColor(getResources().getColor(R.color.colorAccent));
                    imageView2.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
                    textView3.setTextColor(getResources().getColor(R.color.colorAccent));
                    imageView3.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorWhite)));
                    textView2.setTextColor(getResources().getColor(R.color.colorWhite));
                    imageView4.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorWhite)));
                    imageView5.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorWhite)));
                    editor7.putString("layout number", "1");
                    editor7.apply();
                }
            });

            imageView3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    imageView.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
                    textView.setTextColor(getResources().getColor(R.color.colorAccent));
                    imageView3.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
                    textView2.setTextColor(getResources().getColor(R.color.colorAccent));
                    imageView2.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorWhite)));
                    textView3.setTextColor(getResources().getColor(R.color.colorWhite));
                    imageView4.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorWhite)));
                    imageView5.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorWhite)));
                    editor7.putString("layout number", "2");
                    editor7.apply();
                }
            });

            imageView1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    imageView.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorWhite)));
                    textView.setTextColor(getResources().getColor(R.color.colorWhite));
                    imageView1.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
                    textView1.setTextColor(getResources().getColor(R.color.colorAccent));
                    imageView4.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
                    textView14.setTextColor(getResources().getColor(R.color.colorAccent));
                    imageView2.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorWhite)));
                    imageView3.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorWhite)));
                    imageView5.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorWhite)));
                    textView13.setTextColor(getResources().getColor(R.color.colorWhite));
                    linearLayout1.setVisibility(View.VISIBLE);
                    linearLayout3.setVisibility(View.VISIBLE);
                    linearLayout.setVisibility(View.GONE);
                    linearLayout2.setVisibility(View.GONE);
                    editor7.putString("layout number", "3");
                    editor7.apply();
                }
            });

            textView1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    imageView.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorWhite)));
                    textView.setTextColor(getResources().getColor(R.color.colorWhite));
                    imageView1.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
                    textView1.setTextColor(getResources().getColor(R.color.colorAccent));
                    imageView4.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
                    textView14.setTextColor(getResources().getColor(R.color.colorAccent));
                    imageView2.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorWhite)));
                    imageView3.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorWhite)));
                    imageView5.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorWhite)));
                    textView13.setTextColor(getResources().getColor(R.color.colorWhite));
                    linearLayout1.setVisibility(View.VISIBLE);
                    linearLayout3.setVisibility(View.VISIBLE);
                    linearLayout.setVisibility(View.GONE);
                    linearLayout2.setVisibility(View.GONE);
                    editor7.putString("layout number", "3");
                    editor7.apply();
                }
            });

            imageView4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    imageView4.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
                    textView3.setTextColor(getResources().getColor(R.color.colorAccent));
                    imageView.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorWhite)));
                    textView.setTextColor(getResources().getColor(R.color.colorWhite));
                    imageView2.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorWhite)));
                    imageView3.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorWhite)));
                    imageView5.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorWhite)));
                    textView13.setTextColor(getResources().getColor(R.color.colorWhite));
                    textView14.setTextColor(getResources().getColor(R.color.colorAccent));
                    editor7.putString("layout number", "3");
                    editor7.apply();
                }
            });

            imageView5.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    imageView5.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
                    textView2.setTextColor(getResources().getColor(R.color.colorAccent));
                    imageView.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorWhite)));
                    textView.setTextColor(getResources().getColor(R.color.colorWhite));
                    imageView2.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorWhite)));
                    imageView3.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorWhite)));
                    imageView4.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorWhite)));
                    textView14.setTextColor(getResources().getColor(R.color.colorWhite));
                    textView13.setTextColor(getResources().getColor(R.color.colorAccent));
                    editor7.putString("layout number", "4");
                    editor7.apply();
                }
            });

/*            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    imageView.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
                    textView.setTextColor(getResources().getColor(R.color.colorAccent));
                    linearLayout.setVisibility(View.VISIBLE);
                    linearLayout2.setVisibility(View.VISIBLE);
                }
            });*/

            switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (switchCompat.isChecked()) {
                        relativeLayout.setVisibility(View.GONE);
                        relativeLayout1.setVisibility(View.VISIBLE);
                        switchCompat.setChecked(false);
                        switchCompat1.setChecked(true);
                    }
                }
            });
            switchCompat1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (switchCompat1.isChecked()) {
                        relativeLayout.setVisibility(View.GONE);
                        relativeLayout1.setVisibility(View.VISIBLE);
                    } else {
                        relativeLayout.setVisibility(View.VISIBLE);
                        relativeLayout1.setVisibility(View.GONE);
                    }
                    switchCompat.setChecked(false);
                    switchCompat1.setChecked(true);

                }
            });
            final SharedPreferences visibilityField = getSharedPreferences("field visibility", MODE_PRIVATE);
            final SharedPreferences.Editor visibilityField1 = visibilityField.edit();

            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (checkBox.isChecked()) {
                        visibilityField1.putString("Image", "1");
                        imageView6.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
                        textView4.setTextColor(getResources().getColor(R.color.colorAccent));

                    } else {
                        visibilityField1.putString("Image", "0");
                        imageView6.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorWhite)));
                        textView4.setTextColor(getResources().getColor(R.color.colorWhite));
                    }
                    visibilityField1.apply();
                }
            });
            checkBox1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (checkBox1.isChecked()) {
                        visibilityField1.putString("Title", "1");
                        imageView7.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
                        textView5.setTextColor(getResources().getColor(R.color.colorAccent));
                    } else {
                        visibilityField1.putString("Title", "0");
                        imageView7.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorWhite)));
                        textView5.setTextColor(getResources().getColor(R.color.colorWhite));
                    }
                    visibilityField1.apply();
                }
            });
            checkBox2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (checkBox2.isChecked()) {
                        visibilityField1.putString("Sub Title", "1");
                        imageView8.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
                        textView6.setTextColor(getResources().getColor(R.color.colorAccent));
                    } else {
                        visibilityField1.putString("Sub Title", "0");
                        imageView8.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorWhite)));
                        textView6.setTextColor(getResources().getColor(R.color.colorWhite));
                    }
                    visibilityField1.apply();
                }
            });
            checkBox3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (checkBox3.isChecked()) {
                        visibilityField1.putString("Date Created", "1");
                        imageView9.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
                        textView7.setTextColor(getResources().getColor(R.color.colorAccent));
                    } else {
                        visibilityField1.putString("Date Created", "0");
                        imageView9.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorWhite)));
                        textView7.setTextColor(getResources().getColor(R.color.colorWhite));
                    }
                    visibilityField1.apply();
                }
            });
            checkBox4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (checkBox4.isChecked()) {
                        visibilityField1.putString("Date Edited", "1");
                        imageView10.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
                        textView8.setTextColor(getResources().getColor(R.color.colorAccent));
                    } else {
                        visibilityField1.putString("Date Edited", "0");
                        imageView10.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorWhite)));
                        textView8.setTextColor(getResources().getColor(R.color.colorWhite));
                    }
                    visibilityField1.apply();
                }
            });
            checkBox5.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (checkBox5.isChecked()) {
                        visibilityField1.putString("URL", "1");
                        imageView11.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
                        textView9.setTextColor(getResources().getColor(R.color.colorAccent));
                    } else {
                        visibilityField1.putString("URL", "0");
                        imageView11.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorWhite)));
                        textView9.setTextColor(getResources().getColor(R.color.colorWhite));
                    }
                    visibilityField1.apply();
                }
            });
            checkBox6.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (checkBox6.isChecked()) {
                        visibilityField1.putString("Background Color", "1");
                        imageView12.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
                        textView10.setTextColor(getResources().getColor(R.color.colorAccent));
                    } else {
                        visibilityField1.putString("Background Color", "0");
                        imageView12.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorWhite)));
                        textView10.setTextColor(getResources().getColor(R.color.colorWhite));
                    }
                    visibilityField1.apply();
                }
            });
            checkBox7.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (checkBox7.isChecked()) {
                        imageView13.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
                        textView11.setTextColor(getResources().getColor(R.color.colorAccent));
                        visibilityField1.putString("Note", "1");
                    } else {
                        imageView13.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorWhite)));
                        textView11.setTextColor(getResources().getColor(R.color.colorWhite));
                        visibilityField1.putString("Note", "0");
                    }
                    visibilityField1.apply();
                }
            });
            checkBox8.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (checkBox8.isChecked()) {
                        imageView14.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
                        textView12.setTextColor(getResources().getColor(R.color.colorAccent));
                        visibilityField1.putString("Layout Field", "1");
                    } else {
                        imageView14.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorWhite)));
                        textView12.setTextColor(getResources().getColor(R.color.colorWhite));
                        visibilityField1.putString("Layout Field", "0");
                    }
                    visibilityField1.apply();
                }
            });

            view.findViewById(R.id.ImageSettingsCancel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    relativeLayout.setVisibility(View.VISIBLE);
                    relativeLayout1.setVisibility(View.GONE);
                    fullDetailOption.cancel();
                    First();
                }
            });

            view.findViewById(R.id.saveOption).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    relativeLayout.setVisibility(View.VISIBLE);
                    relativeLayout1.setVisibility(View.GONE);
                    fullDetailOption.cancel();
                    First();
                }
            });
        }
        fullDetailOption.show();
    }

    private void SpeechRecording() {
        if (speechDialog == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            View view = LayoutInflater.from(this).inflate(
                    R.layout.speech_dialog, (ViewGroup) findViewById(R.id.layoutSpeechOptions)
            );

            builder.setView(view);
            speechDialog = builder.create();
            if (speechDialog.getWindow() != null) {
                speechDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            }

            final ImageView imageView, imageView1, imageView2, imageView3, imageView4;
            final TextView textView, textView1;
            imageView = view.findViewById(R.id.imageView1);
            imageView1 = view.findViewById(R.id.imageView2);
            imageView2 = view.findViewById(R.id.ImageSettingsCancel);
            imageView3 = view.findViewById(R.id.listening);
            imageView4 = view.findViewById(R.id.tickSearch);
            textView = view.findViewById(R.id.listeningText);
            textView1 = view.findViewById(R.id.LanguageRecorededText);

            final SpeechRecognizer speechRecognizer;
            final Intent speechRecognizerIntent;

            speechRecognizer = SpeechRecognizer.createSpeechRecognizer(MainActivity.this);
            speechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

            speechRecognizer.setRecognitionListener(new RecognitionListener() {
                @Override
                public void onReadyForSpeech(Bundle params) {

                }

                @Override
                public void onBeginningOfSpeech() {

                }

                @Override
                public void onRmsChanged(float rmsdB) {

                }

                @Override
                public void onBufferReceived(byte[] buffer) {

                }

                @Override
                public void onEndOfSpeech() {

                }

                @Override
                public void onError(int error) {

                }

                @Override
                public void onResults(Bundle results) {
                    ArrayList<String> matchesFound = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);

                    if (matchesFound != null) {
                        keeper = matchesFound.get(0);

                        textView1.setText(keeper);
//                        Toast.makeText(FileDisplayed.this, "Results = " + keeper, Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onPartialResults(Bundle partialResults) {

                }

                @Override
                public void onEvent(int eventType, Bundle params) {

                }
            });

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    imageView.setVisibility(View.GONE);
                    imageView3.setVisibility(View.GONE);
                    imageView1.setVisibility(View.VISIBLE);
                    textView.setVisibility(View.VISIBLE);
                    imageView4.setVisibility(View.INVISIBLE);
                    speechRecognizer.startListening(speechRecognizerIntent);
                    keeper = "";
                    textView1.setText(keeper);
                }
            });

            imageView1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    imageView.setVisibility(View.VISIBLE);
                    imageView3.setVisibility(View.VISIBLE);
                    imageView1.setVisibility(View.GONE);
                    textView.setVisibility(View.INVISIBLE);
                    speechRecognizer.stopListening();
                    if (TextUtils.isEmpty(textView1.getText().toString())) {
                        imageView4.setVisibility(View.INVISIBLE);
                    } else {
                        imageView4.setVisibility(View.VISIBLE);
                    }
                }
            });

            imageView4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    inputSearch.setText(keeper);
                    imageView.setVisibility(View.VISIBLE);
                    imageView3.setVisibility(View.VISIBLE);
                    imageView1.setVisibility(View.GONE);
                    textView.setVisibility(View.INVISIBLE);
                    imageView4.setVisibility(View.INVISIBLE);
                    textView1.setText("");
                    speechDialog.cancel();
                }
            });

            imageView2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    speechDialog.dismiss();
                }
            });

        }
        speechDialog.show();
    }

    private void BottomLayout() {
        View sheetView;
        bottomSheetDialog = new BottomSheetDialog(MainActivity.this, R.style.BottomSheetTheme);

        sheetView = LayoutInflater.from(MainActivity.this).inflate(R.layout.layout_more_options, (ViewGroup) findViewById(R.id.layoutMoreOptions));

        sheetView.findViewById(R.id.imageSettings).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AppSettings.class);
                startActivity(intent);
                bottomSheetDialog.dismiss();
            }
        });
        sheetView.findViewById(R.id.imagePinnedNotes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
            }
        });
        sheetView.findViewById(R.id.imageFolder).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),DeviceFilesDisplay.class));
                bottomSheetDialog.dismiss();
            }
        });
        sheetView.findViewById(R.id.imageStarNotes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        sheetView.findViewById(R.id.imageArchiveNotes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        sheetView.findViewById(R.id.imageTrashNotes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        bottomSheetDialog.setContentView(sheetView);
        bottomSheetDialog.show();
    }

    @SuppressLint("QueryPermissionsNeeded")
    private void selectImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, REQUEST_CODE_SELECT_IMAGE);
        }
    }

    private void getNotes(final int requestCode, final boolean isNoteDeleted) {
        @SuppressLint("StaticFieldLeak")
        class GetNotesTask extends AsyncTask<Void, Void, List<Note>> {

            @Override
            protected List<Note> doInBackground(Void... voids) {
                return NotesDatabase.getNotesDatabase(getApplicationContext())
                        .noteDao().getAllNotes();
            }

            @Override
            protected void onPostExecute(List<Note> notes) {
                super.onPostExecute(notes);
/*                if(noteList.size() == 0)
                {
                    noteList.addAll(notes);
                    notesAdapter.notifyDataSetChanged();
                }
                else
                {
                    noteList.add(0,notes.get(0));
                    notesAdapter.notifyItemInserted(0);
                }*/
//                notesRecyclerView.smoothScrollToPosition(0);
                if (requestCode == REQUEST_CODE_SHOW_NOTES) {
                    noteList.addAll(notes);
                    notesAdapter.notifyDataSetChanged();
                } else if (requestCode == REQUEST_CODE_ADD_NOTE) {
                    noteList.add(0, notes.get(0));
                    notesAdapter.notifyItemInserted(0);
                    notesRecyclerView.smoothScrollToPosition(0);
                } else if (requestCode == REQUEST_CODE_UPDATE_NOTE) {
                    noteList.remove(noteClickedPosition);
                    if (isNoteDeleted) {
                        notesAdapter.notifyItemRemoved(noteClickedPosition);
                    } else {
                        noteList.add(noteClickedPosition, notes.get(noteClickedPosition));
                        notesAdapter.notifyItemChanged(noteClickedPosition);
                    }
                }
            }
        }

        new GetNotesTask().execute();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_ADD_NOTE && resultCode == RESULT_OK) {
            getNotes(REQUEST_CODE_ADD_NOTE, false);
        } else if (requestCode == REQ_CODE && resultCode == RESULT_OK && null != data) {
            ArrayList<String> result = data
                    .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            inputSearch.setText(result.get(0));
//            inputSearch.append(result.toString().replaceAll("\\[", "").replaceAll("]", ""));
        } else if (requestCode == REQUEST_CODE_UPDATE_NOTE && resultCode == RESULT_OK) {
            if (data != null) {
                getNotes(REQUEST_CODE_UPDATE_NOTE, data.getBooleanExtra("isNoteDeleted", false));
            }
        } else if (requestCode == REQUEST_CODE_SELECT_IMAGE && resultCode == RESULT_OK) {
            if (data != null) {
                Uri selectImageUri = data.getData();
                if (selectImageUri != null) {
                    try {
                        String selectedImagePath = getPathFromUri(selectImageUri);
                        Intent intent = new Intent(getApplicationContext(), CreateNoteActivity.class);
                        intent.putExtra("isFromQuickActions", true);
                        intent.putExtra("quickActionType", "image");
                        intent.putExtra("imagePath", selectedImagePath);
                        createNoteFolder = new SimpleDateFormat("dd MM yyyy HH:mm:ss a", Locale.getDefault()).format(new Date());
                        startActivityForResult(intent, REQUEST_CODE_ADD_NOTE);
                    } catch (Exception exception) {
                        Toast.makeText(this, "Error occurred, Please try again", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_STORAGE_PERMISSION && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                selectImage();
            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void showAddURLDialog() {
        if (dialogAddURL == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
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
                        Toast.makeText(MainActivity.this, "Enter URL", Toast.LENGTH_SHORT).show();
                    } else if (!Patterns.WEB_URL.matcher(inputURL.getText().toString()).matches()) {
                        Toast.makeText(MainActivity.this, "Enter Valid URL", Toast.LENGTH_SHORT).show();
                    } else {
                        Intent intent = new Intent(getApplicationContext(), CreateNoteActivity.class);
                        intent.putExtra("isFromQuickActions", true);
                        intent.putExtra("quickActionType", "URL");
                        intent.putExtra("URL", inputURL.getText().toString());
                        createNoteFolder = new SimpleDateFormat("dd MM yyyy HH:mm:ss a", Locale.getDefault()).format(new Date());
                        startActivityForResult(intent, REQUEST_CODE_ADD_NOTE);
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

    @Override
    public void onNoteClicked(Note note, int position) {
        noteClickedPosition = position;
        Intent intent = new Intent(getApplicationContext(), CreateNoteActivity.class);
        intent.putExtra("isViewOrUpdate", true);
        intent.putExtra("note", note);
        startActivityForResult(intent, REQUEST_CODE_UPDATE_NOTE);
    }
    
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }

}