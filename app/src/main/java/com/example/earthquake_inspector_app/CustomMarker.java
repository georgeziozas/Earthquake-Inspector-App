package com.example.earthquake_inspector_app;

import android.content.Context;
import android.graphics.Bitmap;
import com.mapbox.mapboxsdk.annotations.IconFactory;
import android.graphics.BitmapFactory;
import android.graphics.ColorSpace;
import android.graphics.drawable.Drawable;

import com.mapbox.mapboxsdk.annotations.Icon;
import com.mapbox.mapboxsdk.annotations.IconFactory;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapboxMap;

import java.util.ArrayList;

public class CustomMarker {
    MapboxMap myMapbox;
    public static ArrayList<CustomMarker> Markers = new ArrayList<>();
    public com.mapbox.mapboxsdk.geometry.LatLng point;
    public double lat;
    public double longt;
    public int status = 0;
    public int answer_first_order;
    MarkerOptions options;

    public CustomMarker() {

    }

    public CustomMarker(Context context, com.mapbox.mapboxsdk.geometry.LatLng p, MapboxMap mapbox) {
        //Initializing variables
        myMapbox = mapbox;
        point = p;
        lat = p.getLatitude();
        longt = p.getLongitude();
        //Setting Options
        options = new MarkerOptions();
        options.position(p);
        //Adding the marker on the map
        mapbox.addMarker(options);

    }

    //re-draw the YELLOW marker this time , at the same position as the previous one was on the map
    public void setYellowMarker(Context context) {
        options = new MarkerOptions();
        options.position(point);
        Bitmap largeIcon = BitmapFactory.decodeResource(context.getResources(), R.drawable.mapbox_marker_icon_default_yellow);
        Icon lightningBoltIcon = IconFactory.recreate("Green",largeIcon);
        options.setIcon(lightningBoltIcon);
        //options.getIcon().getBitmap().setColorSpace(r);
        myMapbox.addMarker(options);

    }

    //re-draw the GREEN marker this time , at the same position as the previous one was on the map
    public void setGreenMarker(Context context) {
        options = new MarkerOptions();
        options.position(point);
        Bitmap largeIcon = BitmapFactory.decodeResource(context.getResources(), R.drawable.mapbox_marker_icon_default_green);
        Icon lightningBoltIcon = IconFactory.recreate("Green",largeIcon);
        options.setIcon(lightningBoltIcon);
        //options.getIcon().getBitmap().setColorSpace(r);
        myMapbox.addMarker(options);
    }


    public void addToArray() {
        Markers.add(this);
    }

    public double getLat() {
        return lat;
    }

    public double getLongt() {
        return longt;
    }

    public LatLng getPoint() {
        return point;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getAnswer_first_order() {
        return answer_first_order;
    }

    public void setAnswer_first_order(int answer_first_order) {
        this.answer_first_order = answer_first_order;
    }

    public void setOptions(MarkerOptions options) {
        this.options = options;
    }
}
