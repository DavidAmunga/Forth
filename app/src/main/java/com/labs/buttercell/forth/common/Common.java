package com.labs.buttercell.forth.common;

import com.labs.buttercell.forth.retrofit.IGoogleApi;
import com.labs.buttercell.forth.retrofit.RetrofitClient;

/**
 * Created by amush on 12-Dec-17.
 */

public class Common {
    public static final String baseURL="https://maps.googleapis.com";
    public static IGoogleApi getGoogeApi()
    {
        return RetrofitClient.getClient(baseURL).create(IGoogleApi.class);
    }
}
