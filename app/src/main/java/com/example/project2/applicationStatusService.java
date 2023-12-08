package com.example.project2;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;

public class applicationStatusService extends Service {

    SharedPreferences sp;
    SharedPreferences.Editor spe;
    AtomicReference<String> saveResponse = new AtomicReference<>("");
    ArrayList<String> allApplications = new ArrayList<>();
    UseServer useServer = UseServer.getInstance(this);
    Boolean keepChecking;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Runnable waitToRunAgain = () -> getApplicationStatuses();
        Thread appStatusThread = new Thread(waitToRunAgain);
        appStatusThread.start();

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void getApplicationStatuses() {
        //Check the database for all applications and their statuses
        while (true) {
            useServer.getUserApplications(response -> {
                if (response == null) {
                    //Do nothing
                    Log.d("test", "Applications: none");
                } else {
                    Log.d("test", "Applications: " + response);
                }
            }, User.id);

            try {
                Thread.sleep(60000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
