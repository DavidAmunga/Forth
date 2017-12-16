package com.labs.buttercell.forth.retrofit;

import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by amush on 12-Dec-17.
 */

public class RetrofitClient {
    private static Retrofit retrofit=null;

    public static Retrofit getClient(String baseUrl)
    {
        if(retrofit==null)
        {
            retrofit=new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
