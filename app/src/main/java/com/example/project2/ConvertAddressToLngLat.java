package com.example.project2;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

//class to convert a street address into a LngLat object
public class ConvertAddressToLngLat {

    public static String convert(Context context, String address) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        List<Address> addressList;

        try {
            addressList = geocoder.getFromLocationName(address, 1);
            if (addressList != null && addressList.size() > 0) {
                double latitude = addressList.get(0).getLatitude();
                double longitude = addressList.get(0).getLongitude();

                return latitude + "," + longitude;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}

