package mradmin.example.com.datetimeapp.model.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import mradmin.example.com.datetimeapp.model.NoteEntity;

@Database(entities = {NoteEntity.class /*, AnotherEntityType.class, AThirdEntityType.class */}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract NoteEntityDao getNoteEntityDao();
}