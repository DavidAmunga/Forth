package com.labs.buttercell.forth;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

/**
 * Created by amush on 16-Dec-17.
 */

public class CustomInfoWindow implements GoogleMap.InfoWindowAdapter {
    View mView;

    public CustomInfoWindow(Context context) {
        mView = LayoutInflater.from(context)
                .inflate(R.layout.custom_rider_info, null);
    }

    @Override
    public View getInfoWindow(Marker marker) {
        TextView txtPickupTitle = mView.findViewById(R.id.txtPickupInfo);
        txtPickupTitle.setText(marker.getTitle());


        TextView txtPickupSnippet = mView.findViewById(R.id.txtPickupSnippet);
        txtPickupSnippet.setText(marker.getSnippet());

        return mView;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }
}
