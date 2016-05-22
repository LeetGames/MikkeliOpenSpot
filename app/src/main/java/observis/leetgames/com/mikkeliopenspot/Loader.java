package observis.leetgames.com.mikkeliopenspot;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class Loader extends AppCompatActivity {


    private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 234;
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION = 312;
    private static final int MY_PERMISSIONS_REQUEST_INTERNET = 42;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loader);

//        dbWork();
      //  Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
      //  setSupportActionBar(toolbar);

        Procedure(getIntent().getExtras().getInt("procedureKey"));


    }

  /*  private void dbWork(){

        ParkingRepo repo = new ParkingRepo(this);
        repo.populate();
    }
*/
    private void Procedure(int k){
        //getClosestPark();

        switch (k){
            case 0:
                Intent im = new Intent(this,MapsActivity.class);
                LatLng closest = getClosestPark();
                im.putExtra("closestData",closest.latitude + "," + closest.longitude);

                startActivity(im);
                this.finish();
                break;
            case 1:
                Decode d = new Decode(this);
                    d.execute(getIntent().getExtras().getString("addressData"));

                break;
        }

    }


   private LatLng getClosestPark(){
        LatLng first = determineFirst();
        GetDistance g = new GetDistance();
        LatLng closest;
        double closestpath = 0;
        LatLng[] parklist = {new LatLng(61.688601, 27.267618),
                new LatLng(61.687946, 27.262668),
                new LatLng(61.690711, 27.270823),
                new LatLng(61.690996, 27.276413),
                new LatLng(61.680284, 27.258517),
                new LatLng(61.684178, 27.249317)};
        closest = parklist[0];
        closestpath = g.executing(first, parklist[0]);
        for(int z = 0; z < parklist.length-1;z++){
                if(closestpath > g.executing(first,parklist[z])){
                    closestpath = g.executing(first,parklist[z]);
                    closest = parklist[z];

                }

            }
        return closest;
    }

    private LatLng getClosestPark(LatLng a){
        LatLng first = determineFirst(a);
        GetDistance g = new GetDistance();
        LatLng closest;
        double closestpath = 0;
        LatLng[] parklist = {new LatLng(61.687946, 27.262668),
                new LatLng(61.688601, 27.267618),
                new LatLng(61.690711, 27.270823),
                new LatLng(61.690996, 27.276413),
                new LatLng(61.680675, 27.258515)};
        closest = parklist[0];
        closestpath = g.executing(first, parklist[0]);
        for(LatLng z : parklist){
            if(closestpath > g.executing(first,z)){
                closestpath = g.executing(first,z);
                closest =z;
            }

        }
        return closest;
    }


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

    private LatLng determineFirst(LatLng a){
        Location location;
        double lat = 0;
        double lon = 0 ;
        location = null;
        boolean k = getIntent().getExtras().getBoolean("routingOption");

            if(a != null) {
                lat = a.latitude;
                lon = a.longitude;
            }


        LatLng f = new LatLng(lat,lon);
        return f;
    }



    private class LongOperation extends AsyncTask<String, Void, Location> {
        LocationManager mLocationManager;
        public Activity activity;

        public LongOperation(Activity a) {
            this.activity = a;
        }

        @Override
        protected Location doInBackground(String... params) {


            mLocationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
            List<String> providers = mLocationManager.getProviders(true);
            Location bestLocation = null;
            if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
            } else {
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
    }


    public class Decode extends AsyncTask<String, Void, LatLng> {
        LocationManager mLocationManager;
        public Activity activity;

        public Decode(Activity a)
        {
            this.activity = a;


        }

        @Override
        protected LatLng doInBackground(String... params) {
            String a = params[0];
            LatLng decoded;
            decoded = null;
            Geocoder coder = new Geocoder(activity);
            List<Address> address;
            LatLng p1 = null;

            try {
                address = coder.getFromLocationName(a,5,61.501734, 26.903089,61.849022, 27.678999);
                if (address==null) {
                    return null;
                }
                Address location=address.get(0);
                location.getLatitude();
                location.getLongitude();


                decoded = new LatLng(location.getLatitude(),location.getLongitude());
            } catch (IOException e) {
                e.printStackTrace();
            }

            return decoded;
        }

        @Override
        protected void onPostExecute(LatLng result) {
            Intent i = new Intent(activity,MapsActivity.class);
            LatLng closest = getClosestPark(result);
            i.putExtra("targetData",result.latitude + "," + result.longitude);
            i.putExtra("closestData",closest.latitude + "," + closest.longitude);
            i.putExtra("routingOption",getIntent().getExtras().getBoolean("routingOption"));
            startActivity(i);
            activity.finish();

        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }


    }

}
