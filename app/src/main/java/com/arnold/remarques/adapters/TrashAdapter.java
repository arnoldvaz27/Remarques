package com.arnold.remarques.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.arnold.remarques.R;
import com.arnold.remarques.entities.Trash;
import com.arnold.remarques.listeners.TrashListeners;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class TrashAdapter extends RecyclerView.Adapter<TrashAdapter.TrashViewHolder> {

    private List<Trash> notes;
    private final TrashListeners notesListeners;
    protected Timer timer;
    private final List<Trash> notesSource;

    public TrashAdapter(List<Trash> notes, TrashListeners notesListeners) {
        this.notes = notes;
        this.notesListeners = notesListeners;
        notesSource = notes;

    }

    @NonNull
    @Override
    public TrashViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TrashViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.item_container_note, parent, false
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull TrashViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        holder.setTrash(notes.get(position));
        holder.layoutNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notesListeners.onTrashClicked(notes.get(position), position);


            }
        });
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    static class TrashViewHolder extends RecyclerView.ViewHolder {

        TextView textTitle, textSubTitle, textDateTime, textNote, url;
        LinearLayout layoutNote;
        RoundedImageView imageNote;
        private final String fieldsVisibleImage;
        private final String fieldsVisibleTitle;
        private final String fieldsVisibleSubTitle;
        private final String fieldsVisibleNote;
        private final String fieldsVisibleDateCreated;
        private final String fieldsVisibleDateEdited;
        private final String fieldsVisibleURL;
        private final String fieldsVisibleBackgroundColor;
        private final String fieldsVisibleLayout;

        public TrashViewHolder(@NonNull View itemView) {
            super(itemView);
            textTitle = itemView.findViewById(R.id.textTitle);
            textSubTitle = itemView.findViewById(R.id.textSubTitle);
            textDateTime = itemView.findViewById(R.id.textDateTime);
            layoutNote = itemView.findViewById(R.id.layoutNote);
            imageNote = itemView.findViewById(R.id.imageNote);
            textNote = itemView.findViewById(R.id.textNote);
            url = itemView.findViewById(R.id.textUrl);
            SharedPreferences fieldsVisibility3 = itemView.getContext().getSharedPreferences("field visibility", Context.MODE_PRIVATE);
            fieldsVisibleImage = fieldsVisibility3.getString("Image", null);
            fieldsVisibleTitle = fieldsVisibility3.getString("Title", null);
            fieldsVisibleSubTitle = fieldsVisibility3.getString("Sub Title", null);
            fieldsVisibleNote = fieldsVisibility3.getString("Note", null);
            fieldsVisibleDateCreated = fieldsVisibility3.getString("Date Created", null);
            fieldsVisibleDateEdited = fieldsVisibility3.getString("Date Edited", null);
            fieldsVisibleURL = fieldsVisibility3.getString("URL", null);
            fieldsVisibleBackgroundColor = fieldsVisibility3.getString("Background Color", null);
            fieldsVisibleLayout = fieldsVisibility3.getString("Layout Field", null);

        }

        void setTrash(Trash trash) {
            GradientDrawable gradientDrawable = (GradientDrawable) layoutNote.getBackground();
            if (fieldsVisibleImage.equals("0")) {
                imageNote.setVisibility(View.GONE);
            }
            else if (fieldsVisibleImage.equals("1") && trash.getImagePath() != null) {
                imageNote.setVisibility(View.VISIBLE);
                imageNote.setImageBitmap(BitmapFactory.decodeFile(trash.getImagePath()));
            }

            if (fieldsVisibleTitle.equals("0")) {
                textTitle.setVisibility(View.GONE);
            }
            else if (fieldsVisibleTitle.equals("1") && trash.getTitle() != null) {
                textTitle.setVisibility(View.VISIBLE);
                textTitle.setText(trash.getTitle());
            }

            if (fieldsVisibleSubTitle.equals("0")) {
                textSubTitle.setVisibility(View.GONE);
            }
            else if (fieldsVisibleSubTitle.equals("1") && trash.getSubtitle() != null) {
                textSubTitle.setVisibility(View.VISIBLE);
                textSubTitle.setText(trash.getSubtitle());
            }

            if (fieldsVisibleURL.equals("0")) {
                url.setVisibility(View.GONE);
            } else if (fieldsVisibleURL.equals("1") && trash.getWebLink() != null) {
                url.setVisibility(View.VISIBLE);
                url.setText(trash.getWebLink());

            }

            if (fieldsVisibleNote.equals("0")) {
                textNote.setVisibility(View.GONE);
            } else if (fieldsVisibleNote.equals("1") && trash.getNoteText() != null) {
                textNote.setVisibility(View.VISIBLE);
                textNote.setText(trash.getNoteText());
            }

            if (fieldsVisibleDateCreated.equals("0")) {
                textDateTime.setVisibility(View.GONE);
            } else if (fieldsVisibleDateCreated.equals("1") && trash.getDateTime() != null) {
                textDateTime.setVisibility(View.VISIBLE);
                textDateTime.setText(trash.getDateTime());
            }

            if (fieldsVisibleLayout.equals("1")) {
                layoutNote.setVisibility(View.VISIBLE);
            } else {
                layoutNote.setVisibility(View.GONE);
            }

            if (fieldsVisibleBackgroundColor.equals("0")) {
                gradientDrawable.setColor(Color.parseColor("#333333"));
            } else if (fieldsVisibleBackgroundColor.equals("1") && trash.getColor() != null) {
                gradientDrawable.setColor(Color.parseColor(trash.getColor()));
            }


        }
    }

    public void searchTrash(final String searchKeyword) {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void run() {
                if (searchKeyword.trim().isEmpty()) {
                    notes = notesSource;
                } else {
                    ArrayList<Trash> temp = new ArrayList<>();
                    for (Trash trash : notesSource) {
                        if (trash.getTitle().toLowerCase().contains(searchKeyword.toLowerCase()) ||
                                trash.getSubtitle().toLowerCase().contains(searchKeyword.toLowerCase()) ||
                                trash.getNoteText().toLowerCase().contains(searchKeyword.toLowerCase())) {
                            temp.add(trash);
                        }
                    }
                    notes = temp;
                }
                new Handler(Looper.getMainLooper()).post(() -> notifyDataSetChanged());
            }
        }, 500);
    }

    public void cancelTimer() {
        if (timer != null) {
            timer.cancel();
        }
    }
}

/*if(searchKeyword.equals("Images")  && note.getImagePath()!=null) {
        temp.add(note);
        }
        else if(searchKeyword.equals("Title")  && note.getTitle()!=null) {
        temp.add(note);
        }
        else if(searchKeyword.equals("Sub-Title")  && note.getSubtitle()!=null) {
        temp.add(note);
        }
        else if(searchKeyword.equals("Note")  && note.getNoteText()!=null) {
        temp.add(note);
        }
        else if(searchKeyword.equals("Date Created")  && note.getDateTime()!=null) {
        temp.add(note);
        }
        else if(searchKeyword.equals("URL")  && note.getWebLink()!=null) {
        temp.add(note);
        }
        else if(searchKeyword.equals("Background Color")  && note.getBackgroundColor()!=null) {
        temp.add(note);
        }
        else if(!searchKeyword.equals("Images") && !searchKeyword.equals("Title") && !searchKeyword.equals("Sub-Title") &&
        !searchKeyword.equals("Note") && !searchKeyword.equals("Date Created") && !searchKeyword.equals("URL")
        && !searchKeyword.equals("Background Color"))
        {
        if (note.getTitle().toLowerCase().contains(searchKeyword.toLowerCase()) ||
        note.getSubtitle().toLowerCase().contains(searchKeyword.toLowerCase()) ||
        note.getNoteText().toLowerCase().contains(searchKeyword.toLowerCase())) {
        temp.add(note);
        }
        }*/

    /*void setNote(Note note) {
        if (note.getTitle().trim().isEmpty()) {
            textTitle.setVisibility(View.GONE);
        } else {
            textTitle.setText(note.getTitle());
        }

        if (note.getWebLink().trim().isEmpty()) {
            url.setVisibility(View.GONE);
        } else {
            url.setText(note.getWebLink());
        }

        if (note.getSubtitle().trim().isEmpty()) {
            textSubTitle.setVisibility(View.GONE);
        } else {
            textSubTitle.setText(note.getSubtitle());
        }

        if (note.getNoteText().trim().isEmpty()) {
            textNote.setVisibility(View.GONE);
        } else {
            textNote.setText(note.getNoteText());
        }

        textDateTime.setText(note.getDateTime());

        GradientDrawable gradientDrawable = (GradientDrawable) layoutNote.getBackground();
        if (note.getColor() != null) {
            gradientDrawable.setColor(Color.parseColor(note.getColor()));
        } else {
            gradientDrawable.setColor(Color.parseColor("#333333"));
        }

        if (note.getImagePath() != null) {
            imageNote.setImageBitmap(BitmapFactory.decodeFile(note.getImagePath()));
            imageNote.setVisibility(View.VISIBLE);
        } else {
            imageNote.setVisibility(View.GONE);
        }
    }*/

 /*   void setNote(Note note) {

        if(fieldsVisibleImage.equals("1") && note.getImagePath() != null)
        {
            imageNote.setImageBitmap(BitmapFactory.decodeFile(note.getImagePath()));
            imageNote.setVisibility(View.VISIBLE);
        }
        else{
            imageNote.setVisibility(View.GONE);
        }
        if(fieldsVisibleTitle.equals("1") && note.getTitle().trim().isEmpty())
        {
            textTitle.setText(note.getTitle());
            textTitle.setVisibility(View.VISIBLE);
        }
        else{
            textTitle.setVisibility(View.GONE);
        }
        if(fieldsVisibleSubTitle.equals("1") && note.getSubtitle().trim().isEmpty())
        {
            textSubTitle.setText(note.getSubtitle());
            textSubTitle.setVisibility(View.VISIBLE);
        }
        else{
            textSubTitle.setVisibility(View.GONE);
        }
        if(fieldsVisibleURL.equals("1") && note.getWebLink().trim().isEmpty())
        {
            url.setText(note.getWebLink());
            url.setVisibility(View.VISIBLE);
        }
        else{
            url.setVisibility(View.GONE);
        }
        if(fieldsVisibleNote.equals("1") && note.getNoteText().trim().isEmpty())
        {
            textNote.setText(note.getNoteText());
            textNote.setVisibility(View.VISIBLE);
        }
        else{
            textNote.setVisibility(View.GONE);
        }
        if(fieldsVisibleDateCreated.equals("1") && note.getDateTime().trim().isEmpty())
        {
            textDateTime.setText(note.getDateTime());
            textDateTime.setVisibility(View.VISIBLE);
        }
        else{
            textDateTime.setVisibility(View.GONE);
        }
        if(fieldsVisibleLayout.equals("1"))
        {
            layoutNote.setVisibility(View.VISIBLE);
        }
        else{
            layoutNote.setVisibility(View.GONE);
        }
        GradientDrawable gradientDrawable = (GradientDrawable) layoutNote.getBackground();
        if (fieldsVisibleBackgroundColor.equals("1") && note.getColor() != null) {
            gradientDrawable.setColor(Color.parseColor(note.getColor()));
        } else {
            gradientDrawable.setColor(Color.parseColor("#333333"));
        }

    }*/

