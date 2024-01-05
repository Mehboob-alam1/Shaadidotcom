package com.mehboob.myshadi.utils;

import android.app.Activity;
import android.app.Application;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.FirebaseApp;
import com.mehboob.myshadi.recievers.NetworkChangeReceiver;

public class App extends Application implements Application.ActivityLifecycleCallbacks {
    private NetworkChangeReceiver networkChangeReceiver;


    @Override
    public void onCreate() {
        super.onCreate();

        FirebaseApp.initializeApp(this);
        networkChangeReceiver = new NetworkChangeReceiver();


        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeReceiver, filter);
//        Intent serviceIntent = new Intent(this, NetworkMonitorService.class);
//        startService(serviceIntent);

        // Check if the user is logged in and all activities are completed
        checkIfActivitiesCompletedAndRedirect();
    }

    private void checkIfActivitiesCompletedAndRedirect() {


    }

    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {

    }

    @Override
    public void onActivityStarted(@NonNull Activity activity) {
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeReceiver,filter);
    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeReceiver, filter);
    }

    @Override
    public void onActivityPaused(@NonNull Activity activity) {
        unregisterReceiver(networkChangeReceiver);

    }

    @Override
    public void onActivityStopped(@NonNull Activity activity) {
        unregisterReceiver(networkChangeReceiver);

    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {
        unregisterReceiver(networkChangeReceiver);

    }
}
