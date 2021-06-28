package com.example.remarques;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.List;


public class FileAdapter extends RecyclerView.Adapter<FileViewHolder> {

    private final Context context;
    private final List<File> fileList;
    private final onFileSelectListener onFileSelectListener;
    public String end = "";
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

        if(holder.tvName.getText().toString().equals("ReadMe.pdf")){
            holder.container.setVisibility(View.GONE);
        }
        end = holder.tvName.getText().toString();
        if(end.endsWith(".mp4")){
            holder.imageView.setImageResource(R.drawable.mp4);
        }else if(end.endsWith(".pdf")){
            holder.imageView.setImageResource(R.drawable.element_pdf);
        }else if(end.endsWith(".docx")){
            holder.imageView.setImageResource(R.drawable.element_word);
        }else if(end.endsWith(".xlsx")){
            holder.imageView.setImageResource(R.drawable.element_xls);
        }else if(end.endsWith(".pptx")){
            holder.imageView.setImageResource(R.drawable.element_powerpoint);
        }else if(end.endsWith(".txt")){
            holder.imageView.setImageResource(R.drawable.element_txt);
        }else if(end.endsWith(".zip")){
            holder.imageView.setImageResource(R.drawable.element_zip);
        }
        holder.container.setOnClickListener(v -> {
            try {
                onFileSelectListener.onFileSelected(fileList.get(position));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public int getItemCount() {
        return fileList.size();
    }


}
