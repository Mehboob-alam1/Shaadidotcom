package com.mehboob.myshadi.recievers;



import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.mehboob.myshadi.views.activities.NoNetworkActivity;

public class NetworkChangeReceiver extends BroadcastReceiver {



    @Override
    public void onReceive(Context context, Intent intent) {
        if (isNetworkConnected(context)) {
            // Internet connection is available
            // You can show a dialog here or trigger any action
            // ...

//            showSnackBar((Activity) context,"You are online");
            Toast.makeText(context, "You are offline", Toast.LENGTH_SHORT).show();
        } else {
            // Internet connection is lost

            Toast.makeText(context, "offline", Toast.LENGTH_SHORT).show();
            Intent noNetworkIntent = new Intent(context, NoNetworkActivity.class);
            noNetworkIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(noNetworkIntent);
        }
    }

    private boolean isNetworkConnected(Context context) {
        final ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connMgr != null) {
            NetworkInfo activeNetworkInfo = connMgr.getActiveNetworkInfo();

            if (activeNetworkInfo != null) { // connected to the internet
                // connected to the mobile provider's data plan
                if (activeNetworkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                    // connected to wifi
                    return true;
                } else return activeNetworkInfo.getType() == ConnectivityManager.TYPE_MOBILE;
            }
        }
        return false;
    }
}
