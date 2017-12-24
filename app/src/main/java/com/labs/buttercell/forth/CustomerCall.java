package com.labs.buttercell.forth;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.JointType;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.SquareCap;
import com.labs.buttercell.forth.common.Common;
import com.labs.buttercell.forth.driver.DriverMap;
import com.labs.buttercell.forth.model.FCMResponse;
import com.labs.buttercell.forth.model.Notification;
import com.labs.buttercell.forth.model.Sender;
import com.labs.buttercell.forth.model.Token;
import com.labs.buttercell.forth.retrofit.FCMService;
import com.labs.buttercell.forth.retrofit.IGoogleApi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class CustomerCall extends AppCompatActivity {
    private static final String TAG = "CustomerCall";

    TextView txtTime, txtAddress, txtDistance;

    Button btnAccept, btnDecline;

    MediaPlayer mediaPlayer;

    IGoogleApi mService;

    FCMService mFCMService;


    String customerId;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Overlock-Regular.ttf")
                .setFontAttrId(R.attr.fontPath).build());

        setContentView(R.layout.activity_customer_call);


        mService = Common.getGoogeApi();

        mFCMService = Common.getFCMService();

        initViews();


        if (getIntent() != null) {
            double lat = getIntent().getDoubleExtra("lat", -1.0);
            double lng = getIntent().getDoubleExtra("lng", -1.0);
            customerId = getIntent().getStringExtra("customer");

            getDirection(lat, lng);


            mediaPlayer = MediaPlayer.create(this, R.raw.ringtone);
            mediaPlayer.setLooping(true);
            mediaPlayer.start();
        }

        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btnDecline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(customerId)) {
                    Log.d(TAG, "onClick: Empty");
                    cancelBooking(customerId);
                }
            }
        });

    }

    private void cancelBooking(String customerId) {
        Token token = new Token(customerId);

        Notification notification = new Notification("Notice!", "Driver has cancelled your request");
        Sender sender = new Sender(token.getToken(), notification);

        mFCMService.sendMessage(sender)
                .enqueue(new Callback<FCMResponse>() {
                    @Override
                    public void onResponse(Call<FCMResponse> call, Response<FCMResponse> response) {
                        if (response.body().success == 1) {
                            Toast.makeText(CustomerCall.this, "Cancelled!", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {

                            Toast.makeText(CustomerCall.this, "Failed!", Toast.LENGTH_LONG).show();


                        }


                    }

                    @Override
                    public void onFailure(Call<FCMResponse> call, Throwable t) {


                    }
                });
    }

    private void initViews() {
        txtAddress = findViewById(R.id.txtAddress);
        txtDistance = findViewById(R.id.txtDistance);
        txtTime = findViewById(R.id.txtTime);
        btnAccept = findViewById(R.id.btnAccept);
        btnDecline = findViewById(R.id.btnDecline);

    }

    private void getDirection(double lat, double lng) {

        String requestApi = null;
        try {

            requestApi = "https://maps.googleapis.com/maps/api/directions/json?" +
                    "mode=driving&" +
                    "transit_routing_preference=less_driving&" +
                    "origin=" + Common.mLastLocation.getLatitude() + "," + Common.mLastLocation.getLongitude() + "&" +
                    "destination=" + lat + "," + lng + "&" +
                    "key=" + getResources().getString(R.string.google_directions_key);
            Log.d(TAG, "Request Api: " + requestApi);
            mService.getPath(requestApi)
                    .enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {

                            try {
                                JSONObject jsonObject = new JSONObject(response.body().toString());

                                JSONArray routes = jsonObject.getJSONArray("routes");

                                JSONObject object = routes.getJSONObject(0);
                                JSONArray legs = object.getJSONArray("legs");
                                JSONObject legsObject = legs.getJSONObject(0);

//                                Get Distancec
                                JSONObject distance = legsObject.getJSONObject("distance");
                                txtDistance.setText(distance.getString("text"));

//                                Get Time
                                JSONObject time = legsObject.getJSONObject("duration");
                                txtTime.setText(time.getString("text"));

//                                Get Address
                                String address = legsObject.getString("end_address");
                                txtAddress.setText(address);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            Toast.makeText(CustomerCall.this, "" + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStop() {
        mediaPlayer.release();
        super.onStop();

    }

    @Override
    protected void onPause() {
        mediaPlayer.release();
        super.onPause();

    }

    @Override
    protected void onResume() {
        mediaPlayer.start();
        super.onResume();
    }


}
