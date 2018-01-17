package mradmin.example.com.datetimeapp;

import android.app.Application;
import android.arch.persistence.room.Room;
import android.os.Bundle;

import mradmin.example.com.datetimeapp.model.db.AppDatabase;
import mradmin.example.com.datetimeapp.model.db.NoteEntityDao;

/**
 * Created by yks-11 on 1/17/18.
 */

public class App extends Application {

    private static AppDatabase appDatabase;
    private static NoteEntityDao noteEntityDao;

    @Override
    public void onCreate() {
        super.onCreate();

        appDatabase = Room.databaseBuilder(this,
                AppDatabase.class, "room-notes-database").build();

        noteEntityDao = appDatabase.getNoteEntityDao();
    }

    public static NoteEntityDao getNoteEntityDao(){
        return appDatabase.getNoteEntityDao();
    }
}
