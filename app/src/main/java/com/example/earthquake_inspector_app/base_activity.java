package com.example.earthquake_inspector_app;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.Icon;
import com.mapbox.mapboxsdk.annotations.IconFactory;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.plugins.locationlayer.modes.CameraMode;

import org.w3c.dom.Document;

import java.util.List;

public class base_activity extends AppCompatActivity implements OnMapReadyCallback, MapboxMap.OnMarkerClickListener, PermissionsListener {

    private static final String TAG = "";
    int apantisi_protovathmias;
    double marker_sintetagmenes_lat;
    double marker_sintetagmenes_long;
    private MapView mapView;
    public MapboxMap mapBox;
    private PermissionsManager permissionsManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, getString(R.string.mapbox_access_token));
        setContentView(R.layout.activity_base_activity);
        //Drawable d = ResourcesCompat.getDrawable(getResources(), R.drawable.mapbox_marker_icon_default, null);
        /* Map: This represents the map in the application. */
        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull MapboxMap mapboxMap) {

                mapBox = mapboxMap;

                mapboxMap.setStyle(new Style.Builder().fromUri("mapbox://styles/mapbox/satellite-streets-v11"), new Style.OnStyleLoaded() {
                    @Override
                    public void onStyleLoaded(@NonNull Style style) {
                       // enableLocationComponent(style); //Calling geolocation component
                        mapboxMap.addOnMapClickListener(new MapboxMap.OnMapClickListener() {
                            @Override
                            public boolean onMapClick(@NonNull LatLng point) {
                                CustomMarker Mymarker = new CustomMarker(getApplicationContext(), point, mapboxMap);
                                CustomMarker.Markers.add(Mymarker);
                                System.out.println(Mymarker.Markers.size());
                                //Push Marker to database
                                FirebaseFirestore db = FirebaseFirestore.getInstance();
                                //Push data to the server
                                db.collection("Markers")
                                        .add(Mymarker)
                                        .addOnSuccessListener(documentReference -> {
                                            String TAG = "";
                                            Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                                        })
                                        .addOnFailureListener(e -> {
                                            String TAG = "";
                                            Log.w(TAG, "Error adding document", e);
                                        });

                                mapboxMap.setOnMarkerClickListener(new MapboxMap.OnMarkerClickListener() {
                                    @Override
                                    public boolean onMarkerClick(@NonNull Marker marker) {
                                        //show on the marker's window info box , the exact coordinates and helper text for the user
                                        String marker_info = "ΣΥΝΤΕΤΑΓΜΕΝΕΣ:" + point.toString() + "\n" + "ΜΕΤΑΦΕΡΕΣΤΕ ΣΤΙΣ ΔΙΑΘΕΣΙΜΕΣ ΛΕΙΤΟΥΡΓΊΕΣ ΤΟΥ ΚΤΗΡΙΟΥ";
                                        marker.setSnippet(marker_info);
                                        //
                                        for (int i = 0; i < CustomMarker.Markers.size(); i++) {
                                            if (marker.getPosition().equals(CustomMarker.Markers.get(i).point) && CustomMarker.Markers.get(i).status == 0) {
                                                double marker_sintetagmenes_lat = marker.getPosition().getLatitude();
                                                double marker_sintetagmenes_long = marker.getPosition().getLongitude();


                                                SystemClock.sleep(2000);
                                                Intent intent = new Intent(base_activity.this, FirstOrderCheck.class);
                                                intent.putExtra("marker_sintetagmenes_lat", marker_sintetagmenes_lat);
                                                intent.putExtra("marker_sintetagmenes_long", marker_sintetagmenes_long);
                                                intent.putExtra("katastasi_protovathmias", apantisi_protovathmias);
                                                startActivityForResult(intent, 1);


                                                //PROTOVATHMIA
                                            } else if (marker.getPosition().equals(CustomMarker.Markers.get(i).point) && CustomMarker.Markers.get(i).status == 1) {
                                                //DEUTEROVATHMIA
                                            }
                                        }
                                        return false;
                                    }
                                });
                                return true;
                            }
                        });
                    }
                });
            }
        });
    }

    @SuppressLint("WrongConstant")
    @SuppressWarnings({"MissingPermission"})

    private void enableLocationComponent(@NonNull Style loadedMapStyle) {
        // Check if permissions are enabled and if not request
        if (PermissionsManager.areLocationPermissionsGranted(this)) {
            // Get an instance of the component
            LocationComponent locationComponent = mapBox.getLocationComponent();
            // Activate with options
            locationComponent.activateLocationComponent(
                    LocationComponentActivationOptions.builder(this, loadedMapStyle).build());
            // Enable to make component visible
            locationComponent.setLocationComponentEnabled(true);
            // Set the component's camera mode
            locationComponent.setCameraMode(CameraMode.TRACKING);
            // Set the component's render mode
            locationComponent.setRenderMode(RenderMode.COMPASS);
        } else {
            permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public void onExplanationNeeded(List<String> permissionsToExplain) {
        Toast.makeText(this, R.string.user_location_permission_explanation, Toast.LENGTH_LONG).show();
    }

    public void onPermissionResult(boolean granted) {
        if (granted) {
            mapBox.getStyle(new Style.OnStyleLoaded() {
                @Override
                public void onStyleLoaded(@NonNull Style style) {
                    enableLocationComponent(style);
                }
            });
        } else {
            Toast.makeText(this, R.string.user_location_permission_not_granted, Toast.LENGTH_LONG).show();
            finish();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onMapReady(@NonNull MapboxMap mapboxMap) {
    }

    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            // This request code is set by startActivityForResult(intent, 1) method.
            case 1:

                if (resultCode == 1) {
                    double sintetagmenesReturn_lat = data.getDoubleExtra("message_return_sintetagmenes_lat", 0);
                    double sintetagmenesReturn_long = data.getDoubleExtra("message_return_sintetagmenes_long", 0);
                    int teliki_apantisi_protovathmiasReturn = data.getIntExtra("teliki_apantisi_protovathmias",100);

                    //Delete the last clicked marker to replace it with a new one with different colour depending on 'FirstOrder's" form answer.
                    for (int i = 0; i < CustomMarker.Markers.size(); i++) {
                        if (CustomMarker.Markers.get(i).options.getPosition().getLatitude()==sintetagmenesReturn_lat
                                &&
                                CustomMarker.Markers.get(i).options.getPosition().getLongitude() == sintetagmenesReturn_long) {

                            Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.empty);
                            Icon lightningBoltIcon = IconFactory.recreate("Green",largeIcon);
                            CustomMarker.Markers.get(i).options.setIcon(lightningBoltIcon);
                            CustomMarker.Markers.remove(i);


                        }
                    }



                        //Deleting from firebase the old marker's record
                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        CollectionReference itemsRef = db.collection("Markers");
                        Query query = itemsRef.
                                whereEqualTo("lat", sintetagmenesReturn_lat).
                                whereEqualTo("longt", sintetagmenesReturn_long);
                        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (DocumentSnapshot document : task.getResult()) {
                                        itemsRef.document(document.getId()).delete();
                                    }
                                } else {
                                    Log.d(TAG, "Error getting documents: ", task.getException());
                                }
                            }
                        });


                        //Create the new one with same coordinates and choose the desired color according to FirstOrderCheck answer.
                        com.mapbox.mapboxsdk.geometry.LatLng a = new com.mapbox.mapboxsdk.geometry.LatLng();
                        a.setLatitude(sintetagmenesReturn_lat);
                        a.setLongitude(sintetagmenesReturn_long);
                        CustomMarker replaceMarker = new CustomMarker(getApplicationContext(), a, mapBox);
                        if (teliki_apantisi_protovathmiasReturn == 0) {
                            replaceMarker.setAnswer_first_order(1); //First Order check has happened on this marker.
                            replaceMarker.setGreenMarker(getApplicationContext());
                        } else if (teliki_apantisi_protovathmiasReturn == 1) {
                            replaceMarker.setAnswer_first_order(1); //First Order check has happened on this marker.
                            replaceMarker.setYellowMarker(getApplicationContext());
                        }


                        //Push new marker  to the database
                        db.collection("Markers")
                                .add(replaceMarker)
                                .addOnSuccessListener(documentReference -> {
                                    String TAG = "";
                                    Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                                })
                                .addOnFailureListener(e -> {
                                    String TAG = "";
                                    Log.w(TAG, "Error adding document", e);
                                });

                    }
                }
        }
    }


