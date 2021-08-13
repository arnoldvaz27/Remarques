package com.arnold.remarques;

import android.Manifest;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.arnold.remarques.activities.CreateNoteActivity.fileFormat;
import static com.arnold.remarques.activities.CreateNoteActivity.folderName;
import static com.arnold.remarques.activities.CreateNoteActivity.viewHolder;


public class FileDisplayed extends AppCompatActivity implements onFileSelectListener {

    public static String fileEnd = "";
    private ImageView imageView;
    private BottomSheetDialog addingFileType;
    private View sheetView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(getResources().getColor(R.color.colorDelete));
        setContentView(R.layout.filedisplayed);

        runtimePermission();
        viewHolder = "FileDisplayed";

        if(fileFormat.equals("File")){
            imageView.setVisibility(View.VISIBLE);
        }
        else{
            imageView.setVisibility(View.INVISIBLE);
        }

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(fileFormat.equals("File")){
                    SelectingFile();
                }
            }
        });
    }

    private void SelectingFile() {
        addingFileType = new BottomSheetDialog(FileDisplayed.this, R.style.BottomSheetTheme);

        sheetView = LayoutInflater.from(FileDisplayed.this).inflate(R.layout.filetypelayout, (ViewGroup) findViewById(R.id.layoutFileType));

        sheetView.findViewById(R.id.layoutPdf).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fileEnd = "pdf";
                addingFileType.dismiss();
                displayFile();
            }
        });
        sheetView.findViewById(R.id.layoutWord).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fileEnd = "docx";
                addingFileType.dismiss();
                displayFile();
            }
        });
        sheetView.findViewById(R.id.layoutExcel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fileEnd = "xlsx";
                addingFileType.dismiss();
                displayFile();
            }
        });
        sheetView.findViewById(R.id.layoutPowerpoint).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fileEnd = "pptx";
                addingFileType.dismiss();
                displayFile();
            }
        });
        sheetView.findViewById(R.id.layoutText).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fileEnd = "txt";
                addingFileType.dismiss();
                displayFile();
            }
        });
        sheetView.findViewById(R.id.layoutZip).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fileEnd = "zip";
                addingFileType.dismiss();
                displayFile();
            }
        });

        addingFileType.setContentView(sheetView);
        addingFileType.show();
    }

    private void runtimePermission() {
        Dexter.withContext(FileDisplayed.this).withPermission(Manifest.permission.READ_EXTERNAL_STORAGE).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                displayFile();
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                Toast.makeText(FileDisplayed.this, "Permission is required to display all the pdf", Toast.LENGTH_SHORT).show();
                runtimePermission();
            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
//                permissionToken.continuePermissionRequest();
            }
        }).check();
    }

    public ArrayList<File> findPdf(File file) {
        ArrayList<File> arrayList = new ArrayList<>();
        File[] files = file.listFiles();

        assert files != null;
        for (File singleFile : files) {
            if (singleFile.isDirectory() && !singleFile.isHidden()) {
                arrayList.addAll(findPdf(singleFile));
            } else {
                if (singleFile.getName().endsWith("."+fileEnd)) {
                    arrayList.add(singleFile);
                }
            }
        }
        return arrayList;
    }

    private void displayFile() {
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        imageView = findViewById(R.id.menu_more);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(FileDisplayed.this,RecyclerView.VERTICAL,false));
        final String state = Environment.getExternalStorageState();
        if ( Environment.MEDIA_MOUNTED.equals(state) || Environment.MEDIA_MOUNTED_READ_ONLY.equals(state) ) {  // we can read the External Storage...
            List<File> pdfList = new ArrayList<>(findPdf(Environment.getExternalStorageDirectory()));
            FileAdapter fileAdapter = new FileAdapter(this, pdfList, this);
            recyclerView.setAdapter(fileAdapter);
        }

    }

    @Override
    public void onFileSelected(File file) throws Exception{
/*        Uri uri = FileProvider.getUriForFile(this, FileDisplayed.this.getApplicationContext().getPackageName() + ".provider",file);
        Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
        pdfIntent.setDataAndType(uri, "application/pdf");
        pdfIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(pdfIntent);*/
/*        File docsFolder = new File(Environment.getExternalStorageDirectory() + File.separator + "Remarques"+File.separator+ ".R "+folderName);
        File pdfFile;
        if (!docsFolder.exists()) {
            docsFolder.mkdir();
        }
        Random r = new Random(System.currentTimeMillis());
        int randomCode = ((1 + r.nextInt(2)) * 1000 + r.nextInt(1000));
        String randCode = Integer.toString(randomCode);
        pdfFile = new File(docsFolder.getAbsolutePath(), randCode + ".pdf");
        OutputStream outputStream = new FileOutputStream(pdfFile);*/
        File SourceFile = new File(file.getAbsolutePath());

        File DestinationFile = new File(Environment.getExternalStorageDirectory().toString() + File.separator + "Remarques" + File.separator+".Important File" +File.separator+".R " + folderName+ File.separator + file.getName());

        if(SourceFile.renameTo(DestinationFile))
        {
            Toast.makeText(this, "File added", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(this, "Not moved", Toast.LENGTH_SHORT).show();
        }
    }
}

