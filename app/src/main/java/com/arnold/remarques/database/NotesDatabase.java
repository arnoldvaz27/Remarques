package com.arnold.remarques.database;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import com.arnold.remarques.dao.NoteDao;
import com.arnold.remarques.entities.Note;

@Database(entities = Note.class, version = 1,exportSchema = false)
public abstract class NotesDatabase extends RoomDatabase {
    private static NotesDatabase notesDatabase;

    public static synchronized NotesDatabase getNotesDatabase(Context context)
    {
        if (notesDatabase == null)
        {
            notesDatabase = Room.databaseBuilder(
                    context,NotesDatabase.class,"notes_db"
            ).build();
        }
        return notesDatabase;
    }

    public abstract NoteDao noteDao();
}
