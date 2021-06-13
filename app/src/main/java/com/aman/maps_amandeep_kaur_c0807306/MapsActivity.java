package com.aman.maps_amandeep_kaur_c0807306;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import com.google.android.gms.maps.model.CustomCap;
import com.google.android.gms.maps.model.Dash;
import com.google.android.gms.maps.model.Dot;
import com.google.android.gms.maps.model.Gap;
import com.google.android.gms.maps.model.JointType;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Dash;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PatternItem;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.content.Context;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback , GoogleMap.OnPolylineClickListener,
        GoogleMap.OnPolygonClickListener {

    private GoogleMap mMap;

    private static final int REQUEST_CODE = 1;
    private LatLng currentLatLong;
    private Marker homeMarker;
    private Marker destMarker;
    List<Marker> latLngArrayListPolygon = new ArrayList<Marker>();
    ArrayList<Double> distancesFromMidPointsOfPolygonEdges = new ArrayList<>();
    List<String> allCityNames = new ArrayList<String>();
    List<Integer> doesCityExist = new ArrayList<Integer>();

    Polygon shape;
    private static final int POLYGON_SIDES = 4;
    List<Marker> markerList = new ArrayList();
    List<Polyline> lineList = new ArrayList();
    Polygon polygon1;
    // location with location manager and listener
    LocationManager locationManager;
    LocationListener locationListener;

    private static final String TAG = "c0807306::MapsActivity";
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean locationPermissionGranted;
    // The geographical location where the device is currently located. That is, the last-known
    // location retrieved by the Fused Location Provider.
    private Location lastKnownLocation;
    private boolean mUpdatePostLocationPermissionRequest = false;

    // [START maps_poly_activity_style_polygon]  A6 65  59 35
    private static final int COLOR_WHITE_ARGB = 0x5900ff00;
    private static final int COLOR_GREEN_ARGB = 0xff388E3C;
    private static final int COLOR_PURPLE_ARGB = 0xff81C784;
    private static final int COLOR_ORANGE_ARGB = 0xffF57F17;
    private static final int COLOR_BLUE_ARGB = 0xffF9A825;

    // [START maps_poly_activity_style_polyline]
    private static final int COLOR_BLACK_ARGB = 0xff000000;
    private static final int POLYLINE_STROKE_WIDTH_PX = 12;

    private static final int POLYGON_STROKE_WIDTH_PX = 8;
    private static final int PATTERN_DASH_LENGTH_PX = 20;
    private static final PatternItem DASH = new Dash(PATTERN_DASH_LENGTH_PX);
    // [START maps_poly_activity_on_polyline_click]
    private static final int PATTERN_GAP_LENGTH_PX = 20;
    private static final PatternItem DOT = new Dot();
    private static final PatternItem GAP = new Gap(PATTERN_GAP_LENGTH_PX);

    // Create a stroke pattern of a gap followed by a dot.
    private static final List<PatternItem> PATTERN_POLYLINE_DOTTED = Arrays.asList(GAP, DOT);
    // Create a stroke pattern of a gap followed by a dash.
    private static final List<PatternItem> PATTERN_POLYGON_ALPHA = Arrays.asList(GAP, DASH);
    // Create a stroke pattern of a dot followed by a gap, a dash, and another gap.
    private static final List<PatternItem> PATTERN_POLYGON_BETA =
            Arrays.asList(DOT, GAP, DASH, GAP);
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

        Log.d(TAG, "onMapReady" + googleMap);
        LatLng sydney = new LatLng(45.631062, -122.671570);
        /*googleMap.addMarker(new MarkerOptions()
                .position(sydney)
                .title("Marker in Vancuover")); */
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            mUpdatePostLocationPermissionRequest = true;
        }
        getLocationPermission();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Log.d(TAG,"still no permission");
            Toast.makeText(getApplicationContext(),"No location permission", Toast.LENGTH_LONG).show();
            return;
        }
        setMapEventListeners();



    }

    @SuppressLint("MissingPermission")
    private void setMapEventListeners() {
        if (mMap == null) {
            Log.d(TAG,"setMapEventListeners map is null");
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
                int order = -1;
                if (marker.getTitle() == "A" ) {
                    order = 0;
                } else if (marker.getTitle() == "B" ) {
                    order = 1;
                } else if (marker.getTitle() == "C" ) {
                    order = 2;
                } else if (marker.getTitle() == "D" ) {
                    order = 3;
                }
                //markerList.remove(order);
                //clearMap();
                //markerList.add(order, marker);
                //refreshPoints();
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
                Log.d(TAG,"onMapLongClick "+latLng);
                setMarker(latLng);

            }
        });
        mMap.setOnPolygonClickListener(MapsActivity.this);
        mMap.setOnPolylineClickListener(MapsActivity.this);
        mUpdatePostLocationPermissionRequest = false;
    }
    /**
     * Prompts the user for permission to use the device location.
     */
    // [START maps_current_place_location_permission]
    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            locationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }
    // [END maps_current_place_location_permission]

    // [START maps_current_place_on_request_permissions_result]
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        locationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    locationPermissionGranted = true;
                    if (mUpdatePostLocationPermissionRequest) {
                        setMapEventListeners();
                    }
                }
            }
        }
        updateLocationUI();
    }
    // [END maps_current_place_on_request_permissions_result]

    // [START maps_current_place_update_location_ui]
    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }
        try {
            if (locationPermissionGranted) {
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                mMap.setMyLocationEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                lastKnownLocation = null;
                getLocationPermission();
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }
    // [END maps_current_place_update_location_ui]

    public double mesaureLengthBetweenPoints(LatLng latLng1, LatLng latLng2) {
        Location point1 = new Location("point1");
        point1.setLatitude(latLng1.latitude);
        point1.setLongitude(latLng1.longitude);

        Location point2 = new Location("point2");
        point2.setLatitude(latLng2.latitude);
        point2.setLongitude(latLng2.longitude);

        double distance = Double.parseDouble(String.format("%.2f", point2.distanceTo(point1) / 1610));

        return distance;
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
                    .draggable(true)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.a))
                    .snippet("Distance " + String.valueOf(Math.round(distance(currentLatLong,latLng)) + "m"));

            markerList.add(mMap.addMarker(options));

        } else if (markerList.size() == 1) {
            MarkerOptions options = new MarkerOptions().position(latLng)
                    .title("B")
                    .draggable(true)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.b))
                    //.snippet(Toast.makeText(MapsActivity.this,  "Address: " +Address.getAddress(options, getBaseContext()), Toast.LENGTH_LONG).show())
                    .snippet("Distance " + String.valueOf(Math.round(distance(currentLatLong,latLng)) + "m"));;
            markerList.add(mMap.addMarker(options));
            Polyline polyline2 = mMap.addPolyline(new PolylineOptions()
                    .clickable(true)
                    .add(
                            markerList.get(0).getPosition(),
                            markerList.get(1).getPosition()));
            polyline2.setTag("AB");
            lineList.add(polyline2);
        } else if (markerList.size() == 2) {
            MarkerOptions options = new MarkerOptions().position(latLng)
                    .title("C")
                    .draggable(true)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.c))
                    .snippet("Distance " + String.valueOf(Math.round(distance(currentLatLong,latLng)) + "m"));;
            markerList.add(mMap.addMarker(options));
            Polyline polyline3 = mMap.addPolyline(new PolylineOptions()
                    .clickable(true)
                    .add(
                            markerList.get(1).getPosition(),
                            markerList.get(2).getPosition()));
            polyline3.setTag("BC");
            lineList.add(polyline3);
        } else if (markerList.size() == 3) {
            MarkerOptions options = new MarkerOptions().position(latLng)
                    .title("D")
                    .draggable(true)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.d))
                    .snippet("Distance " + String.valueOf(Math.round(distance(currentLatLong,latLng)) + "m"));;
            markerList.add(mMap.addMarker(options));
            Polyline polyline4 = mMap.addPolyline(new PolylineOptions()
                    .clickable(true)
                    .add(
                            markerList.get(2).getPosition(),
                            markerList.get(3).getPosition()));
            polyline4.setTag("CD");
            Polyline polyline1 = mMap.addPolyline(new PolylineOptions()
                    .clickable(true)
                    .add(
                            markerList.get(3).getPosition(),
                            markerList.get(0).getPosition()));
            polyline1.setTag("DA");
            lineList.add(polyline4);
            lineList.add(polyline1);
            drawShape();
        }
    }


    private void drawShape() {
        // Add polygons to indicate areas on the map.
        for (int i = 0; i < POLYGON_SIDES; i++) {
            Log.d(TAG, "premcheck "+markerList.get(i).getPosition());

        }
        polygon1 = mMap.addPolygon(new PolygonOptions()
                .clickable(true)
                .add(
                        markerList.get(0).getPosition(),
                        markerList.get(1).getPosition(),
                        markerList.get(2).getPosition(),
                        markerList.get(3).getPosition()));
        // Store a data object with the polygon, used here to indicate an arbitrary type.
        polygon1.setTag("alpha");
        // [END maps_poly_activity_add_polygon]
        // Style the polygon.
        stylePolygon(polygon1);
        /*
        PolygonOptions options = new PolygonOptions()
                .fillColor(0x33000000)
                .strokeColor(Color.RED)
                .strokeWidth(10);

        for (int i = 0; i < POLYGON_SIDES; i++) {
            options.add(markerList.get(i).getPosition());

        }
        shape = mMap.addPolygon(options);

         */
        //addListenersOnPolygon(shape);
    }

    /**
     * Styles the polygon, based on type.
     * @param polygon The polygon object that needs styling.
     */
    private void stylePolygon(Polygon polygon) {
        String type = "";
        // Get the data object stored with the polygon.
        if (polygon.getTag() != null) {
            type = polygon.getTag().toString();
        }

        List<PatternItem> pattern = null;
        int strokeColor = COLOR_BLACK_ARGB;
        int fillColor = COLOR_WHITE_ARGB;

        switch (type) {
            // If no type is given, allow the API to use the default.
            case "alpha":
                // Apply a stroke pattern to render a dashed line, and define colors.
                pattern = PATTERN_POLYGON_ALPHA;
                strokeColor = COLOR_GREEN_ARGB;
                fillColor = COLOR_PURPLE_ARGB;
                break;
            case "beta":
                // Apply a stroke pattern to render a line of dots and dashes, and define colors.
                pattern = PATTERN_POLYGON_BETA;
                strokeColor = COLOR_ORANGE_ARGB;
                fillColor = COLOR_BLUE_ARGB;
                break;
        }

        polygon.setStrokePattern(pattern);
        polygon.setStrokeWidth(POLYGON_STROKE_WIDTH_PX);
        polygon.setStrokeColor(strokeColor);
        polygon.setFillColor(fillColor);
    }
    // [END maps_poly_activity_style_polygon]

    private void refreshPoints() {
        for (Marker marker : markerList) {

            setMarker(marker.getPosition());
        }

    }
    private void clearMap() {
        for (Marker marker : markerList)
            marker.remove();

        markerList.clear();

        for (Polyline line : lineList) {
            line.remove();
        }
        lineList.clear();

        polygon1.remove();
        //shape.remove();
        //shape = null;
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

    @SuppressLint("LongLogTag")
    private void setHomeMarker(Location location) {
        Log.d(TAG,"setHomeMarker "+location);
        LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());

        MarkerOptions options = new MarkerOptions().position(userLocation)
                .title("You are here")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.current))
                .snippet("Your Location");
        homeMarker = mMap.addMarker(options);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 15));
    }

    @Override
    public void onPolygonClick(@NonNull Polygon polygon) {
        Log.d(TAG,"onPolygonClick "+polygon);
        LatLng latLng1 = markerList.get(0).getPosition();
        LatLng latLng2 = markerList.get(1).getPosition();
        LatLng latLng3 = markerList.get(2).getPosition();
        LatLng latLng4 = markerList.get(3).getPosition();
        double length1 = mesaureLengthBetweenPoints(latLng1, latLng2);
        double length2 = mesaureLengthBetweenPoints(latLng2, latLng3);
        double length3 = mesaureLengthBetweenPoints(latLng3, latLng4);
        double length4 = mesaureLengthBetweenPoints(latLng4, latLng1);

        double totalLength = Double.parseDouble(String.format("%.2f", length1 + length2 + length3 + length4));

        Toast.makeText(MapsActivity.this, "A-B-C-D: " + totalLength + "Mile", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPolylineClick(@NonNull Polyline polyline) {
        Log.d(TAG,"onPolylineClick"+polyline.getTag());
        List<LatLng> list = polyline.getPoints();
        if (list.size() ==2 ) {
            mesaureLengthBetweenPoints(list.get(0), list.get(1));
            Toast.makeText(MapsActivity.this,"distance between points "+polyline.getTag()+" is "+mesaureLengthBetweenPoints(list.get(0), list.get(1)),Toast.LENGTH_LONG).show();
        }
    }
/*
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (REQUEST_CODE == requestCode) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0, locationListener);
            }
        }
    }

 */
}