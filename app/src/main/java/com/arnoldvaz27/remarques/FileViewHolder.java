package com.arnoldvaz27.remarques;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class FileViewHolder extends RecyclerView.ViewHolder {

    public TextView tvName;
    public RelativeLayout container;
    public ImageView extensionImage,moreMenuImage;

    public FileViewHolder(@NonNull View itemView) {
        super(itemView);

        tvName = itemView.findViewById(R.id.textFileName);
        container = itemView.findViewById(R.id.container);
        extensionImage = itemView.findViewById(R.id.img);
        moreMenuImage = itemView.findViewById(R.id.viewMore);
    }
}
