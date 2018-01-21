package mradmin.example.com.datetimeapp;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import mradmin.example.com.datetimeapp.activity.MainActivity;
import mradmin.example.com.datetimeapp.model.NoteEntity;

/**
 * Created by yks-11 on 1/19/18.
 */

public class NoteNotificationService extends IntentService {

    public static final String NOTETEXT = "com.mradmin.notenotificationservicetext";
    public static final String NOTEID = "com.mradmin.notenotificationserviceid";

    private String noteTitle;
    private String noteId;
    private Context mContext;

    public NoteNotificationService() {
        super("NoteNotificationService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        noteTitle = intent.getStringExtra(NOTETEXT);
        noteId = intent.getStringExtra(NOTEID);

        Log.d("Note App Log ---  ", "onHandleIntent called");
        NotificationManager manager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        Intent i = new Intent(this, MainActivity.class);
        i.putExtra(NoteNotificationService.NOTEID, noteId);
        //Intent deleteIntent = new Intent(this, DeleteNotificationService.class);
        //deleteIntent.putExtra(NOTEID, noteId);
        Notification notification = new Notification.Builder(this)
                .setContentTitle("Notesco")
                .setContentText(noteTitle)
                .setSmallIcon(R.drawable.ic_calendar)
                .setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_SOUND)
                //.setDeleteIntent(PendingIntent.getService(this, noteId.hashCode(), deleteIntent, PendingIntent.FLAG_UPDATE_CURRENT))
                .setContentIntent(PendingIntent.getActivity(this, noteId.hashCode(), i, PendingIntent.FLAG_UPDATE_CURRENT))
                .build();

        manager.notify(100, notification);
    }
}
