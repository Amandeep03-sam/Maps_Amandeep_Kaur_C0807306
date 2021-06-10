package com.aman.maps_amandeep_kaur_c0807306;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;
import android.content.Context;
public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    private static final int REQUEST_CODE = 1;
      private LatLng currentLatLong;
    private Marker homeMarker;
    private Marker destMarker;


    Polygon shape;
    private static final int POLYGON_SIDES = 4;
    List<Marker> markerList = new ArrayList();


    // location with location manager and listener
    LocationManager locationManager;
    LocationListener locationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        mMap.setMyLocationEnabled(true);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                setHomeMarker(location);
                currentLatLong = new LatLng(location.getLatitude(),location.getLongitude());

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };


        if (!hasLocationPermission())
            requestLocationPermission();
        else
            startUpdateLocation();


mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
    @Override
    public void onMarkerDragStart(@NonNull Marker marker) {
        LatLng position=marker.getPosition();
    }

    @Override
    public void onMarkerDrag(@NonNull Marker marker) {
        LatLng position=marker.getPosition();
    }

    @Override
    public void onMarkerDragEnd(@NonNull Marker marker) {
        LatLng position=marker.getPosition();
    }
});
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(@NonNull Marker marker) {
                Toast.makeText(MapsActivity.this,  "Address: " +Address.getAddress(marker, getBaseContext()), Toast.LENGTH_LONG).show();
                 return  false;
            }
        });
        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(@NonNull LatLng latLng) {
                setMarker(latLng);

            }
        });

    }





    public static double distance(LatLng start, LatLng end){
        try {
            Location location1 = new Location("locationA");
            location1.setLatitude(start.latitude);
            location1.setLongitude(start.longitude);
            Location location2 = new Location("locationB");
            location2.setLatitude(end.latitude);
            location2.setLongitude(end.longitude);
            double distance = location1.distanceTo(location2);
            return distance;
        } catch (Exception e) {

            e.printStackTrace();

        }
        return 0;
    }





    private void setMarker(LatLng latLng) {


        if (markerList.size() == POLYGON_SIDES)
            clearMap();

        if (markerList.size() == 0) {
            MarkerOptions options = new MarkerOptions().position(latLng)
                    .title("A")
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.a))
                    .snippet("Distance " + String.valueOf(Math.round(distance(currentLatLong,latLng)) + "m"));
            markerList.add(mMap.addMarker(options));

        } else if (markerList.size() == 1) {
            MarkerOptions options = new MarkerOptions().position(latLng)
                    .title("B")
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.b))
                    //.snippet(Toast.makeText(MapsActivity.this,  "Address: " +Address.getAddress(options, getBaseContext()), Toast.LENGTH_LONG).show())
                    .snippet("Distance " + String.valueOf(Math.round(distance(currentLatLong,latLng)) + "m"));;
            markerList.add(mMap.addMarker(options));
        } else if (markerList.size() == 2) {
            MarkerOptions options = new MarkerOptions().position(latLng)
                    .title("C")
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.c))
                    .snippet("Distance " + String.valueOf(Math.round(distance(currentLatLong,latLng)) + "m"));;
            markerList.add(mMap.addMarker(options));
        } else if (markerList.size() == 3) {
            MarkerOptions options = new MarkerOptions().position(latLng)
                    .title("D")
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.d))
                    .snippet("Distance " + String.valueOf(Math.round(distance(currentLatLong,latLng)) + "m"));;
            markerList.add(mMap.addMarker(options));
            drawShape();
        }



    }


    private void drawShape() {

        PolygonOptions options = new PolygonOptions()
                .fillColor(0x33000000)
                .strokeColor(Color.RED)
                .strokeWidth(5);

        for (int i = 0; i < POLYGON_SIDES; i++) {
            options.add(markerList.get(i).getPosition());

        }

        shape = mMap.addPolygon(options);

    }

    private void clearMap() {
        for (Marker marker : markerList)
            marker.remove();

        markerList.clear();
        shape.remove();
        shape = null;
    }



    private void requestLocationPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
    }

    private void startUpdateLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0, locationListener);
    }

    private boolean hasLocationPermission() {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    private void setHomeMarker(Location location) {
        LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());

        MarkerOptions options = new MarkerOptions().position(userLocation)
                .title("You are here")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.current))
                .snippet("Your Location");
        homeMarker = mMap.addMarker(options);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 15));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (REQUEST_CODE == requestCode) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0, locationListener);
            }
        }
    }
}