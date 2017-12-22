package com.labs.buttercell.forth.common;

import android.location.Location;

import com.labs.buttercell.forth.retrofit.FCMClient;
import com.labs.buttercell.forth.retrofit.FCMService;
import com.labs.buttercell.forth.retrofit.IGoogleApi;
import com.labs.buttercell.forth.retrofit.RetrofitClient;

/**
 * Created by amush on 12-Dec-17.
 */

public class Common {
    public static final String baseURL="https://maps.googleapis.com";
    public static final String fcmURL="https://fcm.googleapis.com";
    public static IGoogleApi getGoogeApi()
    {
        return RetrofitClient.getClient(baseURL).create(IGoogleApi.class);
    }
    public static FCMService getFCMService()
    {
        return FCMClient.getClient(fcmURL).create(FCMService.class);
    }

    public static Location mLastLocation=null;

}
