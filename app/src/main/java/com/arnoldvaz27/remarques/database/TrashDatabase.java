package com.arnoldvaz27.remarques.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.arnoldvaz27.remarques.dao.TrashDao;
import com.arnoldvaz27.remarques.entities.Trash;

@Database(entities = Trash.class, version = 1,exportSchema = false)
public abstract class TrashDatabase extends RoomDatabase {
    private static TrashDatabase trashDatabase;

    public static synchronized TrashDatabase getTrashDatabase(Context context)
    {
        if (trashDatabase == null)
        {
            trashDatabase = Room.databaseBuilder(
                    context, TrashDatabase.class,"trash_db"
            ).build();
        }
        return trashDatabase;
    }

    public abstract TrashDao trashDao();
}
