package mradmin.example.com.datetimeapp.util;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import static android.content.Context.ALARM_SERVICE;

/**
 * Created by yks-11 on 1/19/18.
 */

public class NoteAlarmManager {

    //----------------ALARM

    public Context context;

    public NoteAlarmManager(Context context) {
        this.context = context;
    }

    public AlarmManager getAlarmManager(){
        return (AlarmManager)context.getSystemService(ALARM_SERVICE);
    }

    public boolean doesPendingIntentExist(Intent i, int requestCode){
        PendingIntent pi = PendingIntent.getService(context, requestCode, i, PendingIntent.FLAG_NO_CREATE);
        return pi != null;
    }

    public void createAlarm(Intent i, int requestCode, long timeInMillis){
        AlarmManager am = getAlarmManager();
        PendingIntent pi = PendingIntent.getService(context,requestCode, i, PendingIntent.FLAG_UPDATE_CURRENT);
        am.set(AlarmManager.RTC_WAKEUP, timeInMillis, pi);
        Log.d("AlArM LoG -_-_-_-_ ", "createAlarm "+requestCode+" time: "+timeInMillis+" PI "+pi.toString());
    }
    public void deleteAlarm(Intent i, int requestCode){
        if(doesPendingIntentExist(i, requestCode)){
            PendingIntent pi = PendingIntent.getService(context, requestCode,i, PendingIntent.FLAG_NO_CREATE);
            pi.cancel();
            getAlarmManager().cancel(pi);
            Log.d("AlArM LoG -_-_-_-_ ", "PI Cancelled " + doesPendingIntentExist(i, requestCode));
        }
    }

    //----------------
}
