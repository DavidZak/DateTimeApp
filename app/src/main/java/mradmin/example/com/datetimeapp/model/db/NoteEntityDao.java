package mradmin.example.com.datetimeapp.model.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import mradmin.example.com.datetimeapp.model.NoteEntity;

@Dao
public interface NoteEntityDao {

    // Добавление Note в бд
    @Insert
    void insertAll(NoteEntity... notes);

    @Insert
    void insertNote(NoteEntity note);

    @Update
    void updateNote(NoteEntity note);

    // Удаление Note из бд
    @Delete
    void delete(NoteEntity note);

    // Получение всех Note из бд
    @Query("SELECT * FROM noteEntity")
    List<NoteEntity> getAllNotes();

    // Получение Note из бд по id
    @Query("SELECT * FROM noteEntity WHERE id = :id")
    List<NoteEntity> getNoteById(String id);

}
