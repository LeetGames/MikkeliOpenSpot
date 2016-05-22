package observis.leetgames.com.mikkeliopenspot;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationUtils;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 234;
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION = 312;
    private static final int MY_PERMISSIONS_REQUEST_INTERNET = 42;
    private GoogleMap mMap;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = getLayoutInflater();
        View v = inflater.inflate(R.layout.activity_maps,null,true);
        v.startAnimation(AnimationUtils.loadAnimation(this,android.R.anim.slide_out_right));
        setContentView(v);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);


        mapFragment.getMapAsync(this);

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
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

       // LongOperation LO = new LongOperation(this);
      //  LO.execute();





        LatLng first;
        LatLng second;

        first = determineFirst();


            String[] divideList = getIntent().getExtras().getString("closestData").split(",");
           second = new LatLng(Double.parseDouble(divideList[0]),Double.parseDouble(divideList[1]));

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);

        }
        else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                // Provide an additional rationale to the user if the permission was not granted
                // and the user would benefit from additional context for the use of the permission.
                // For example if the user has previously denied the permission.

            } else {

                // Camera permission has not been granted yet. Request it directly.
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
            }
        }
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(61.681263, 27.2554154), 11.73f));
        mMap.setMyLocationEnabled(true);

        moveCamera(first,second);




    }


   /* private LatLng determineSecond(){

        LatLng f = new LatLng();
        return f;
    }*/


    private LatLng determineFirst(){
        Location location;
        double lat = 0;
        double lon = 0 ;
        location = null;
        boolean k = getIntent().getExtras().getBoolean("routingOption");

            if(getIntent().getExtras().getBoolean("routingOption") != true) {
                if(getIntent().getExtras().getString("targetData")!=null) {
                    String[] divideList = getIntent().getExtras().getString("targetData").split(",");
                    lat = Double.parseDouble(divideList[0]);
                    lon = Double.parseDouble(divideList[1]);
                    mMap.addMarker(new MarkerOptions()
                            //.anchor(0.0f, 1.0f) // Anchors the marker on the bottom left
                            .position(new LatLng(lat,lon)));
                }
                else {
                    try {
                        location = new LongOperation(this).execute().get();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                    if(location!=null) {
                        lat = location.getLatitude();
                        lon = location.getLongitude();
                    }
                    else{
                        lat = 61.681263;
                        lon = 27.2554154;
                    }
                }
        }else{
                try {
                    location = new LongOperation(this).execute().get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                if(location!=null) {
                    lat = location.getLatitude();
                    lon = location.getLongitude();
                }
                else{
                    lat = 61.681263;
                    lon = 27.2554154;
                }
        }

        LatLng f = new LatLng(lat,lon);

        return f;
    }

    private void moveCamera(LatLng f,LatLng s){

        if (f != null && s != null) {


            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            builder.include(f);
            builder.include(s);

            LatLngBounds bounds = builder.build();

            Display display = getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            int width = size.x;
            int height = size.y;
            int padding = (width + height) / 20;

            mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds,width,height,padding));

          /*  CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(f)      // Sets the center of the map to location user
                    .zoom(15)                   // Sets the zoom
                    .build();                   // Creates a CameraPosition from the builder
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));*/

            Drawing draw = new Drawing(mMap,width,height);
            draw.Executing(f,s);

            if(mMap!=null) {
                mMap.addMarker(new MarkerOptions()
                        //.anchor(0.0f, 1.0f) // Anchors the marker on the bottom left
                        .position(s)
                        .icon(BitmapDescriptorFactory.fromAsset("1p.png")));
            }


        }

    }

    private Bitmap resizeMapIcons(String iconName,int width, int height){
        Bitmap imageBitmap = BitmapFactory.decodeResource(getResources(),getResources().getIdentifier(iconName, "drawable", getPackageName()));
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(imageBitmap, width, height, false);
        return resizedBitmap;
    }

   /* public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mMap.setMyLocationEnabled(true);
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
            case MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mMap.setMyLocationEnabled(true);
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
*/






    private class LongOperation extends AsyncTask<String, Void, Location> {
        LocationManager mLocationManager;
        public Activity activity;

        public LongOperation(Activity a)
        {
            this.activity = a;
        }

        @Override
        protected Location doInBackground(String... params) {


            mLocationManager = (LocationManager)getApplicationContext().getSystemService(LOCATION_SERVICE);
            List<String> providers = mLocationManager.getProviders(true);
            Location bestLocation = null;
            if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
            }
            else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                        Manifest.permission.ACCESS_FINE_LOCATION)) {
                    // Provide an additional rationale to the user if the permission was not granted
                    // and the user would benefit from additional context for the use of the permission.
                    // For example if the user has previously denied the permission.

                } else {

                    // Camera permission has not been granted yet. Request it directly.
                    ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
                }
            }
            for (String provider : providers) {
                Location l = mLocationManager.getLastKnownLocation(provider);
                if (l == null) {
                    continue;
                }
                if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                    // Found best last known location: %s", l);
                    bestLocation = l;
                }
            }
            return bestLocation;

        }

        @Override
        protected void onPostExecute(Location location) {
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }


    }
}

