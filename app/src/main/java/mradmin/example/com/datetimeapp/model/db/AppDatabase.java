package mradmin.example.com.datetimeapp.model.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import mradmin.example.com.datetimeapp.model.NoteEntity;

/**
 * Created by yks-11 on 1/17/18.
 */

@Database(entities = {NoteEntity.class /*, AnotherEntityType.class, AThirdEntityType.class */}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract NoteEntityDao getNoteEntityDao();
}