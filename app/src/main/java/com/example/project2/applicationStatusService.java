package com.example.project2;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;

public class applicationStatusService extends Service {

    ArrayList<AppStatusObj> allApplications = new ArrayList<>();
    UseServer useServer = UseServer.getInstance(this);

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
                    //Parse the response
                    allApplications.clear();
                    String[] allStatuses = response.split(Formatting.DELIMITER_2);
                    //loop through all jobs for processing
                    for (String status : allStatuses) {
                        //Parse the individual items
                        String[] statusInfo = status.split(Formatting.DELIMITER_1);
                        allApplications.add(new AppStatusObj(statusInfo[0],statusInfo[1],statusInfo[2]));
                    }
                    updateTheUI();
                }
            }, User.id);

            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void updateTheUI() {
        Intent broadcastIntent = new Intent("ACTION_DATA_UPDATED");
        broadcastIntent.putParcelableArrayListExtra("appStatuses", allApplications);
        LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent);
        Log.d("test", "broadcasted " + allApplications);
    }
}
