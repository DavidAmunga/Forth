package com.labs.buttercell.forth.service;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.labs.buttercell.forth.CustomerCall;
import com.labs.buttercell.forth.R;

/**
 * Created by amush on 22-Dec-17.
 */

public class MyFirebaseMessaging extends FirebaseMessagingService {
    private static final String TAG = "MyFirebaseMessaging";

    @Override
    public void onMessageReceived(final RemoteMessage remoteMessage) {


        if (remoteMessage.getNotification().getBody().equals("Driver has cancelled your request")) {
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(MyFirebaseMessaging.this, "" + remoteMessage.getNotification().getBody(), Toast.LENGTH_SHORT).show();
                }
            });

        } else {
            Log.d(TAG, "LatLng " + remoteMessage.getNotification().getBody());
//        Convert message to LatLng
            LatLng customer_location = new Gson().fromJson(remoteMessage.getNotification().getBody(), LatLng.class);

            Intent intent = new Intent(getBaseContext(), CustomerCall.class);
            intent.putExtra("lat", customer_location.latitude);
            intent.putExtra("lng", customer_location.longitude);
            intent.putExtra("customer", remoteMessage.getNotification().getTitle());
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);


            Log.d(TAG, "Customer" + remoteMessage.getNotification().getTitle());

            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(MyFirebaseMessaging.this, "" + remoteMessage.getNotification().getBody(), Toast.LENGTH_SHORT).show();
                }
            });
        }

    }


}
