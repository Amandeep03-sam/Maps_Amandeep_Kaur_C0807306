package com.aman.maps_amandeep_kaur_c0807306;

import android.content.Context;
import android.location.Geocoder;

import com.google.android.gms.maps.model.Marker;

import java.io.IOException;
import java.util.List;

public class Address {
    public static String getAddress(Marker marker, Context context){
        String address= "";
        Geocoder geocoder = new Geocoder(context);
        try{

            List<android.location.Address> addressList = geocoder.getFromLocation(marker.getPosition().latitude, marker.getPosition().longitude,1);
            if(addressList != null && addressList.size() > 0){
                address = "\n";
                // Street name, postal code, city and province

                if(addressList.get(0).getThoroughfare() != null)
                address += addressList.get(0).getThoroughfare() + "\n";
                if(addressList.get(0).getLocality() != null)
                    address += addressList.get(0).getLocality() + "\n";
                if(addressList.get(0).getPostalCode() != null)
                    address += addressList.get(0).getPostalCode() + "\n";
                if(addressList.get(0).getAdminArea() != null)
                    address += addressList.get(0).getAdminArea() + "\n";
                if(addressList.get(0).getCountryName() != null)
                    address += addressList.get(0).getCountryName() + "\n";


            }
        } catch (IOException e){
            e.printStackTrace();
        }
        return address;
    }
}
