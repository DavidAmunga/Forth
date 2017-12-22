package com.labs.buttercell.forth.service;

import android.content.Intent;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.labs.buttercell.forth.CustomerCall;

/**
 * Created by amush on 22-Dec-17.
 */

public class MyFirebaseMessaging extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
//        Convert message to LatLng
        LatLng customer_location=new Gson().fromJson(remoteMessage.getNotification().getBody(),LatLng.class);

        Intent intent=new Intent(getBaseContext(), CustomerCall.class);
        intent.putExtra("lat",customer_location.latitude);
        intent.putExtra("lng",customer_location.longitude);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
