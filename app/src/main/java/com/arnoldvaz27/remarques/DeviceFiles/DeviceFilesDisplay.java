package com.arnoldvaz27.remarques.DeviceFiles;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.arnoldvaz27.remarques.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DeviceFilesDisplay extends AppCompatActivity implements onDeviceFileSelectListener{


    private ImageView imageView,empty;
    private TextView emptyText;
    private BottomSheetDialog addingFileType;
    private View sheetView;
    String fileEnding = "";
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(getResources().getColor(R.color.colorDelete));
        setContentView(R.layout.device_files_display);
        recyclerView = findViewById(R.id.recycler_view);
        imageView = findViewById(R.id.menu_more);
        empty = findViewById(R.id.empty);
        emptyText = findViewById(R.id.text);

        Gallery();

        findViewById(R.id.menu_more).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(getApplicationContext(), v);
                popupMenu.getMenuInflater().inflate(R.menu.devicefiles, popupMenu.getMenu());
                popupMenu.show();

                popupMenu.setOnMenuItemClickListener(item -> {
                    if (item.getItemId() == R.id.gallery) {
                          Gallery();
                    }
                    if (item.getItemId() == R.id.favourite) {
                        FavouritePicture();
                    }
                    if (item.getItemId() == R.id.pdf) {
                        PDFFiles();
                    }
                    if (item.getItemId() == R.id.qrCodes) {
                        QRCodes();
                    }
                    if (item.getItemId() == R.id.text) {
                        TextFiles();
                    }
                    if (item.getItemId() == R.id.sharedFiles) {
                        SharedFiles();
                    }
                    return true;
                });
            }
        });
    }

    private void SharedFiles() {
        fileEnding = "png";
        recyclerView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(DeviceFilesDisplay.this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        List<File> pdfList = new ArrayList<>(findPdf(new File(Environment.getExternalStorageDirectory(),"Remarques"+File.separator+"Shared Images")));
        DeviceFileAdapter fileAdapter = new DeviceFileAdapter(this, pdfList, this);
        recyclerView.setAdapter(fileAdapter);
    }

    private void QRCodes() {
        fileEnding = "pdf";
        recyclerView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(DeviceFilesDisplay.this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        List<File> pdfList = new ArrayList<>(findPdf(new File(Environment.getExternalStorageDirectory(),"Remarques"+File.separator+"QR Codes")));
        DeviceFileAdapter fileAdapter = new DeviceFileAdapter(this, pdfList, this);
        recyclerView.setAdapter(fileAdapter);
    }

    private void TextFiles() {
        fileEnding = "txt";
        recyclerView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(DeviceFilesDisplay.this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        List<File> pdfList = new ArrayList<>(findPdf(new File(Environment.getExternalStorageDirectory(),"Remarques"+File.separator+"Text Files")));
        DeviceFileAdapter fileAdapter = new DeviceFileAdapter(this, pdfList, this);
        recyclerView.setAdapter(fileAdapter);
    }

    private void FavouritePicture() {
        fileEnding = "png";
        recyclerView.setHasFixedSize(true);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        List<File> pdfList = new ArrayList<>(findPdf(new File(Environment.getExternalStorageDirectory(),"Remarques"+File.separator+"Favorite Pictures")));
        DeviceFileAdapter fileAdapter = new DeviceFileAdapter(this, pdfList, this);
        recyclerView.setAdapter(fileAdapter);
    }

    private void PDFFiles() {
        fileEnding = "pdf";
        recyclerView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(DeviceFilesDisplay.this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        List<File> pdfList = new ArrayList<>(findPdf(new File(Environment.getExternalStorageDirectory(),"Remarques"+File.separator+"PDF Files")));
        DeviceFileAdapter fileAdapter = new DeviceFileAdapter(this, pdfList, this);
        recyclerView.setAdapter(fileAdapter);
    }

    private void Gallery() {
        fileEnding = "png";
        recyclerView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(DeviceFilesDisplay.this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        List<File> pdfList = new ArrayList<>(findPdf(new File(Environment.getExternalStorageDirectory(),"Remarques"+File.separator+"Gallery")));
        DeviceFileAdapter fileAdapter = new DeviceFileAdapter(this, pdfList, this);
        recyclerView.setAdapter(fileAdapter);
    }

    public ArrayList<File> findPdf(File file) {
        ArrayList<File> arrayList = new ArrayList<>();
        if(file.exists()){
            File[] files = file.listFiles();
            if(files == null || files.length==0){
                emptyText.setVisibility(View.VISIBLE);
                empty.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            }else{
                emptyText.setVisibility(View.GONE);
                empty.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                for (File singleFile : files) {
                    if (singleFile.isDirectory() && !singleFile.isHidden()) {
                        arrayList.addAll(findPdf(singleFile));
                    } else {
                        if (singleFile.getName().endsWith("."+fileEnding)) {
                            arrayList.add(singleFile);
                        }
                    }
                }
            }
        }else{
            emptyText.setVisibility(View.VISIBLE);
            empty.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
        return arrayList;
    }

    @Override
    public void onFileSelected(File file) throws Exception {

    }
}