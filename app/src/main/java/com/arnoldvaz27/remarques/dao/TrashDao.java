package com.arnoldvaz27.remarques.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.arnoldvaz27.remarques.entities.Note;
import com.arnoldvaz27.remarques.entities.Trash;

import java.util.List;

@Dao
public interface TrashDao {

    @Query("SELECT * FROM trash ORDER BY id DESC")
    List<Note> getAllTrash();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertTrash(Trash note);

    @Delete
    void deleteTrash(Trash note);

    @Query("DELETE FROM trash")
    void deleteAll();
}
