package com.example.remarques;

import android.Manifest;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.example.remarques.activities.CreateNoteActivity.fileEnd;
import static com.example.remarques.activities.CreateNoteActivity.folderName;


public class FileDisplayed extends AppCompatActivity implements onFileSelectListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.filedisplayed);

        runtimePermission();
    }

    private void runtimePermission() {
        Dexter.withContext(FileDisplayed.this).withPermission(Manifest.permission.READ_EXTERNAL_STORAGE).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                displayPdf();
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

    private void displayPdf() {
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(FileDisplayed.this,RecyclerView.VERTICAL,false));
        List<File> pdfList = new ArrayList<>(findPdf(Environment.getExternalStorageDirectory()));
        FileAdapter fileAdapter = new FileAdapter(this, pdfList, this);
        recyclerView.setAdapter(fileAdapter);
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

        File DestinationFile = new File(Environment.getExternalStorageDirectory().toString()+File.separator + "Remarques"+File.separator+ ".R "+folderName+ File.separator + file.getName());

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

