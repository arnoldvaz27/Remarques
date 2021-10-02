package com.arnoldvaz27.remarques.DeviceFiles;

import static com.arnoldvaz27.remarques.activities.CreateNoteActivity.folderName;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.arnoldvaz27.remarques.FileViewHolder;
import com.arnoldvaz27.remarques.R;

import java.io.File;
import java.util.List;


public class DeviceFileAdapter extends RecyclerView.Adapter<FileViewHolder> {

    private final Context context;
    private final List<File> fileList;
    private final onDeviceFileSelectListener onFileSelectListener;
    public String end = "";
    private View sheetView;

    @NonNull
    @Override
    public FileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FileViewHolder(LayoutInflater.from(context).inflate(R.layout.element_holder, parent, false));
    }

    public DeviceFileAdapter(Context context, List<File> fileList, onDeviceFileSelectListener onFileSelectListener) {
        this.context = context;
        this.fileList = fileList;
        this.onFileSelectListener = onFileSelectListener;
    }

    @Override
    public void onBindViewHolder(@NonNull FileViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.tvName.setText(fileList.get(position).getName());
        holder.tvName.setSelected(true);

        holder.moreMenuImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    PopupMenu popupMenu = new PopupMenu(context, v);
                    popupMenu.getMenuInflater().inflate(R.menu.displayfile, popupMenu.getMenu());
                    popupMenu.show();

                    popupMenu.setOnMenuItemClickListener(item -> {
                        if (item.getItemId() == R.id.open) {
                            Opening(fileList.get(position));
                        }
                        if (item.getItemId() == R.id.share) {
                            Sharing(fileList.get(position));
                        }
                        if (item.getItemId() == R.id.info) {
                        }
                        return true;
                    });
            }
        });
        end = fileList.get(position).getName();
        if(end.endsWith(".mp4")){
            holder.extensionImage.setImageResource(R.drawable.mp4);
        }else if(end.endsWith(".mp3")){
            holder.extensionImage.setImageResource(R.drawable.mp3);
        }else if(end.endsWith(".png")){
            holder.extensionImage.setImageResource(R.drawable.png);
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

    private void Sharing(File file) {
        Uri uri = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider",file);
        Intent pdfIntent = new Intent(Intent.ACTION_SEND);
        if(file.getName().endsWith(".pdf") ){
            pdfIntent.setDataAndType(uri, "application/pdf");
        }
        else if(file.getName().endsWith(".mp4") || file.getName().endsWith(".amr") || file.getName().endsWith(".mp3")){
            pdfIntent.setDataAndType(uri, "audio/x-wav");
        }
        else if(file.getName().endsWith(".png")){
            pdfIntent.setDataAndType(uri, "image/*");
        }
        pdfIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        pdfIntent.putExtra(Intent.EXTRA_STREAM, uri);
        try {
            context.startActivity(Intent.createChooser(pdfIntent, "Share Sound File"));
        } catch (ActivityNotFoundException e) {
            Toast.makeText(context, "No Applications found to open this format file. You can download relevant application to view this file format", Toast.LENGTH_LONG).show();
        }
    }

    private void Opening(File file) {
        Uri uri = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider",file);
        Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
        if(file.getName().endsWith(".pdf") ){
            pdfIntent.setDataAndType(uri, "application/pdf");
        }
        else if(file.getName().endsWith(".mp4") || file.getName().endsWith(".amr") || file.getName().endsWith(".mp3")){
            pdfIntent.setDataAndType(uri, "audio/x-wav");
        }
        else if(file.getName().endsWith(".png")){
            pdfIntent.setDataAndType(uri, "image/*");
        }
        pdfIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        try {
            context.startActivity(pdfIntent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(context, "No Applications found to open this format file. You can download relevant application to view this file format", Toast.LENGTH_LONG).show();
        }
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
