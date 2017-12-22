package com.labs.buttercell.forth.retrofit;

import com.labs.buttercell.forth.model.FCMResponse;
import com.labs.buttercell.forth.model.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by amush on 22-Dec-17.
 */

public interface FCMService {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAA-ZSnZ_g:APA91bHxkuLJOwT7psZKZzDGiRpzr3KLGU2tyBAg8QZke_DdLvq1AXuyM8rckgLN2pmUxlLm7S_Q3r_hS3XfhaL6FusgdGErqb7_ECtGpkudSzu7Ye2gAexredJA0-Xr6riVBKpWSTQ5"
            }
    )
    @POST("fcm/send")
    Call<FCMResponse> sendMessage(@Body Sender body);
}
