package com.example.remarques;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.FileUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.remarques.activities.CreateNoteActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import static com.example.remarques.FileDisplayed.fileEnd;
import static com.example.remarques.activities.CreateNoteActivity.folderName;
import static com.example.remarques.activities.CreateNoteActivity.viewHolder;


public class FileAdapter extends RecyclerView.Adapter<FileViewHolder> {

    private final Context context;
    private final List<File> fileList;
    private final onFileSelectListener onFileSelectListener;
    public String end = "";
    private View sheetView;

    @NonNull
    @Override
    public FileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FileViewHolder(LayoutInflater.from(context).inflate(R.layout.element_holder, parent, false));
    }

    public FileAdapter(Context context, List<File> fileList, onFileSelectListener onFileSelectListener) {
        this.context = context;
        this.fileList = fileList;
        this.onFileSelectListener = onFileSelectListener;
    }

    @Override
    public void onBindViewHolder(@NonNull FileViewHolder holder, int position) {
        holder.tvName.setText(fileList.get(position).getName());
        holder.tvName.setSelected(true);

        holder.moreMenuImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(viewHolder.equals("CreateNote")){
                    PopupMenu popupMenu = new PopupMenu(context, v);
                    popupMenu.getMenuInflater().inflate(R.menu.notedisplayedmenu, popupMenu.getMenu());
                    popupMenu.show();

                    popupMenu.setOnMenuItemClickListener(item -> {
                        if (item.getItemId() == R.id.download) {

                        }
                        if (item.getItemId() == R.id.open) {
                            Opening(fileList.get(position));
                        }
                        if (item.getItemId() == R.id.share) {
                            Uri uri = Uri.parse(fileList.get(position).getPath());
                            Intent share = new Intent(Intent.ACTION_SEND);
                            share.setType("application/pdf");
                            share.putExtra(Intent.EXTRA_STREAM, uri);
                            context.startActivity(Intent.createChooser(share, "Share Sound File"));
                        }
                        if (item.getItemId() == R.id.info) {
                        }
                        return true;
                    });
                }
                else if(viewHolder.equals("FileDisplayed")){
                    PopupMenu popupMenu = new PopupMenu(context, v);
                    popupMenu.getMenuInflater().inflate(R.menu.filedisplayedmenu, popupMenu.getMenu());
                    popupMenu.show();

                    popupMenu.setOnMenuItemClickListener(item -> {
                        if (item.getItemId() == R.id.open) {
                            Opening(fileList.get(position));
                        }
                        if (item.getItemId() == R.id.share) {
                            Uri uri = Uri.parse(fileList.get(position).getPath());
                            Intent share = new Intent(Intent.ACTION_SEND);
                            share.setType("application/pdf");
                            share.putExtra(Intent.EXTRA_STREAM, uri);
                            context.startActivity(Intent.createChooser(share, "Share Sound File"));
                        }
                        if (item.getItemId() == R.id.info) {

                        }
                        if (item.getItemId() == R.id.addToNote) {
                            adding(fileList.get(position));
                        }
                        return true;
                    });
                }
            }
        });

        if(holder.tvName.getText().toString().equals("ReadMe.pdf")){
            holder.container.setVisibility(View.GONE);
        }
        end = holder.tvName.getText().toString();
        if(end.endsWith(".mp4")){
            holder.extensionImage.setImageResource(R.drawable.mp4);
        }else if(end.endsWith(".pdf")){
            holder.extensionImage.setImageResource(R.drawable.element_pdf);
        }else if(end.endsWith(".docx")){
            holder.extensionImage.setImageResource(R.drawable.element_word);
        }else if(end.endsWith(".xlsx")){
            holder.extensionImage.setImageResource(R.drawable.element_xls);
        }else if(end.endsWith(".pptx")){
            holder.extensionImage.setImageResource(R.drawable.element_powerpoint);
        }else if(end.endsWith(".txt")){
            holder.extensionImage.setImageResource(R.drawable.element_txt);
        }else if(end.endsWith(".zip")){
            holder.extensionImage.setImageResource(R.drawable.element_zip);
        }else if(end.endsWith(".amr")){
            holder.extensionImage.setImageResource(R.drawable.amr);
        }
        holder.container.setOnClickListener(v -> {
            try {
                onFileSelectListener.onFileSelected(fileList.get(position));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void Opening(File file) {
        Uri uri = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider",file);
        Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
        pdfIntent.setDataAndType(uri, "application/pdf");
        pdfIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        context.startActivity(pdfIntent);
    }

    private void adding(File file) {
        File SourceFile = new File(file.getAbsolutePath());

        File DestinationFile = new File(Environment.getExternalStorageDirectory().toString()+File.separator + "Remarques"+File.separator+ ".R "+folderName+ File.separator + file.getName());

        if(SourceFile.renameTo(DestinationFile))
        {
            Toast.makeText(context, "File added", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(context, "Not moved", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public int getItemCount() {
        return fileList.size();
    }


}
