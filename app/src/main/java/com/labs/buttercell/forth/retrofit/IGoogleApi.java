package com.labs.buttercell.forth.retrofit;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

/**
 * Created by amush on 12-Dec-17.
 */

public interface IGoogleApi {
    @GET
    Call<String> getPath(@Url String url);
}
