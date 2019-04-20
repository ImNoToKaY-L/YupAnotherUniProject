package com.example.drmarker.Step;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.example.drmarker.Event.StepEvent;
import com.example.drmarker.MainActivity;
import com.example.drmarker.MyApplication;
import com.example.drmarker.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import lombok.ast.app.Main;

//import cn.ikaze.healthgo.MainActivity;
//import cn.ikaze.healthgo.MyApplication;
//import cn.ikaze.healthgo.R;

/**
 * Created by gojuukaze on 16/8/17.
 * Email: i@ikaze.uu.me
 */
public class StepService extends Service {

    private StepThread thread;
    private PowerManager.WakeLock mWakeLock;
    private String uid;

    @Override
    public void onCreate() {
        EventBus.getDefault().register(this);
        super.onCreate();
        uid = MainActivity.uid;
        Log.d("service", "service create()");

        thread = new StepThread(this,uid);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent!=null){
            Log.d("service", "service start()");
            MyApplication app = (MyApplication) getApplication();
            app.setServiceRun(true);
            uid = intent.getStringExtra("uid");
            Log.d("IDS:service",uid);
            if (intent.getBooleanExtra("isActivity", false))
                thread.setActivity(true);
            String s = intent.getStringExtra("restart");
            if (s != null) {
                Log.d("restart", s);
            }
            if (thread.getState() == Thread.State.NEW)
                thread.start();
            SharedPreferences sharedPreferences = getSharedPreferences("conf", MODE_PRIVATE);
            boolean foreground_model = sharedPreferences.getBoolean("foreground_model", false);

            if (foreground_model) {

                myStartForeground();
                mWakeLock(this);
            } else {
                stopForeground(true);
                if (mWakeLock != null) {
                    if (mWakeLock.isHeld())
                        mWakeLock.release();
                    mWakeLock = null;
                }
            }


        }
        return START_STICKY;


    }

    @Subscribe(threadMode = ThreadMode.MAIN,priority = 1)
    public void onStepChanged(StepEvent event){
        Long steps = event.getStepNum();
        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("Steps:")
                        .setContentText(steps.toString());
        Intent notificationIntent = new Intent(this, MainActivity.class);
        notificationIntent.putExtra("uid",uid);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(notificationIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);

        startForeground(1, mBuilder.build());
        Log.d("notificationManager", "onStepChanged: "+event.getStepNum());
    }

    public void myStartForeground() {

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("Steps:")
                        .setContentText("running");

        Intent notificationIntent = new Intent(this, MainActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(notificationIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);

        startForeground(1, mBuilder.build());

    }


    @Override
    public void onDestroy() {
        Log.d("service", "service stop()");
        if (mWakeLock != null) {
            if (mWakeLock.isHeld())
                mWakeLock.release();
            mWakeLock = null;
        }

        stopForeground(true);
        thread.mystop();
        MyApplication app = (MyApplication) getApplication();

        app.setServiceRun(false);
        boolean temp = getSharedPreferences("conf", MODE_PRIVATE).getBoolean("switch_on", false);
        if (temp) {
            Log.d("restart", "auto restart");
            Intent intent = new Intent("cn.ikaze.pedometer.start");
            sendBroadcast(intent);
        }
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    synchronized private PowerManager.WakeLock mWakeLock(Context context) {
        if (mWakeLock != null) {
            if (mWakeLock.isHeld())
                mWakeLock.release();
            mWakeLock = null;
        }

        if (mWakeLock == null) {
            PowerManager mgr = (PowerManager) context
                    .getSystemService(Context.POWER_SERVICE);
            mWakeLock = mgr.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                    StepService.class.getName());
            mWakeLock.setReferenceCounted(true);
            mWakeLock.acquire();

//            Calendar c = Calendar.getInstance();
//            c.setTimeInMillis(System.currentTimeMillis());
//            int hour = c.get(Calendar.HOUR_OF_DAY);
//            if (hour >= 23 || hour <= 6) {
//                mWakeLock.acquire(5000);
//            } else {
//                mWakeLock.acquire(300000);
//            }
        }
        return (mWakeLock);
    }


}
