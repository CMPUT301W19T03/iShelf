package ca.ualberta.ishelf;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Locale;

import ca.ualberta.ishelf.Models.Database;
import ca.ualberta.ishelf.Models.Request;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private String TAG = "MapsActivity";

    private static final int REQUEST_LOCATION_PERMISSION = 1;
    private Request request;
    private LatLng bookLocation;
    private String ownerUsername;
    private String borrowerUsername;
    private String bookName;
    private String currentUsername;
    private Marker marker;
    private Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // pass in to here the Request object
        // use the database function to edit the request
        //request = new Request();
        //request.setOwner("Tom");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Intent intent = getIntent();
        saveButton = findViewById(R.id.mapButton);


        // retrieve the passed in Request
        if (intent.hasExtra("Request")) {
            request = intent.getParcelableExtra("Request");
        } else {
            Log.d(TAG, "onCreate: Has no Request passed in");
            finish(); // return to previous activity because we have no request
        }

            // get the signed-in username so we can use it later to determine if we are bookowner or not
            currentUsername = getSharedPreferences("UserPreferences", Context.MODE_PRIVATE).getString("username", null);

            // get the requested book's owner's username
            ownerUsername = request.getOwner();

            // get the LatLng bookLocation from the passed in request
            if (request.hasLocation()) {
                bookLocation = request.getLocation();
            }
            // retrieve the book name?

            if (ownerUsername.equals(currentUsername)){
                saveButton.setText("Save");
            } else {
                saveButton.setVisibility(View.GONE);
            }

        Log.d(TAG, "onCreate: logged in username:" + currentUsername + " book owner's username:" + ownerUsername);

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.moveCamera(CameraUpdateFactory.zoomTo(14));

        // if location is already set, initially send the view there
        if (request != null && request.hasLocation()){
            mMap.moveCamera(CameraUpdateFactory.newLatLng(bookLocation));
        } else {
            // if not set just center on edmonton
            LatLng edmonton = new LatLng(53.530410, -113.511956);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(edmonton));
        }

        // Add a marker in Sydney and move the camera
        //LatLng sydney = new LatLng(-34, 151);
        //mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        enableMyLocation();

        if (request != null && request.hasLocation()) {
            marker = mMap.addMarker(new MarkerOptions().position(bookLocation));
            // Set the name of the pin - could change this to the name of the book?
            marker.setTitle("Swap Spot");
            if (currentUsername.equals(ownerUsername)){
                marker.setDraggable(true);
            } else {
                marker.setDraggable(false);
            }
        }

        // Check if we are viewing a map or editing a map

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                String snippet = String.format(Locale.getDefault(),
                        "Lat: %1$.5f, Long: %2$.5f",
                        latLng.latitude,
                        latLng.longitude);
                Log.d(TAG, "onMapClick: ");
                //Toast.makeText(snippet, Toast.LENGTH_LONG).show();
            }
        });

        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                // only allow the owner to add a pin or move the pin
                if (currentUsername.equals(ownerUsername)) {
                    if (bookLocation == null) {
                        String snippet = String.format(Locale.getDefault(),
                                "Lat: %1$.5f, Long: %2$.5f",
                                latLng.latitude,
                                latLng.longitude);
                        Log.d(TAG, "onMapLongClick: " + snippet);
                        mMap.addMarker(new MarkerOptions()
                                .position(latLng)).setDraggable(true);
                        bookLocation = latLng;
                    } else {
                        Log.d(TAG, "onMapLongClick: pin already placed");
                    }
                }
            }
        });

        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {
            }

            @Override
            public void onMarkerDrag(Marker marker) {

            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                if (currentUsername.equals(ownerUsername)) {
                    bookLocation = marker.getPosition();
                }
            }
        });
    }

    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        } else {
            ActivityCompat.requestPermissions(this, new String[]
                            {Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION_PERMISSION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        // Check if location permissions are granted and if so enable the
        // location data layer.
        switch (requestCode) {
            case REQUEST_LOCATION_PERMISSION:
                if (grantResults.length > 0
                        && grantResults[0]
                        == PackageManager.PERMISSION_GRANTED) {
                    enableMyLocation();
                    break;
                }
        }
    }

    public void onButtonHit(View v){
        Log.d(TAG, "onButtonHit: " + String.format(Locale.getDefault(),
                "Lat: %1$.5f, Long: %2$.5f",
                bookLocation.latitude,
                bookLocation.longitude));
        // Save the new LatLong and return to next activity
        request.setLocation(bookLocation);
        Database db = new Database(this);
        db.editRequest(request);
        finish();
    }
}
