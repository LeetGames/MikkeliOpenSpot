package observis.leetgames.com.mikkeliopenspot;


import android.app.ProgressDialog;
import android.location.Location;
import android.os.AsyncTask;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class GetDistance {

    public double executing(LatLng a,LatLng b){
        LatLng first = a;
        LatLng second = b;
        String url = makeURL(first,second);
        String k = null;
        //JSONParser pars = new JSONParser();
        //  String json = pars.getJSONFromUrl(url);
        try{
         k = new calcTask(url).execute().get();
    } catch (InterruptedException e) {
        e.printStackTrace();
    } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return getPathLength(k);

    }

    public double getPathLength(String result){
        double distance = 0;

        try {
            //Tranform the string into a json object

            JSONObject json = new JSONObject(result);
            JSONArray routeArray = json.getJSONArray("routes");
            JSONObject routes = routeArray.getJSONObject(0);
            JSONObject overviewPolylines = routes.getJSONObject("overview_polyline");
            String encodedString = overviewPolylines.getString("points");
            List<LatLng> list = decodePoly(encodedString);

            for (int z = 0; z < list.size(); z++) {
                LatLng point = list.get(z);
                if(z != list.size()-1 ) {
                    LatLng point1 = list.get(z+1);
                    Location location = new Location("");
                    Location location1 = new Location("");
                    location.setLatitude(point.latitude);
                    location.setLongitude(point.longitude);
                    location1.setLatitude(point1.latitude);
                    location1.setLongitude(point1.longitude);
                    distance = distance + location.distanceTo(location1);

                }
            }


        } catch (JSONException e) {

        }
        return distance;
    }

    private String makeURL (LatLng first, LatLng second ){
        double sourcelat = first.latitude ;
        double sourcelog = first.longitude ;
        double destlat = second.latitude ;
        double destlog = second.longitude ;
        StringBuilder urlString = new StringBuilder();
        urlString.append("https://maps.googleapis.com/maps/api/directions/json");
        urlString.append("?origin=");// from
        urlString.append(Double.toString(sourcelat));
        urlString.append(",");
        urlString
                .append(Double.toString( sourcelog));
        urlString.append("&destination=");// to
        urlString
                .append(Double.toString( destlat));
        urlString.append(",");
        urlString.append(Double.toString( destlog));
        urlString.append("&sensor=false&mode=driving&alternatives=true");
        urlString.append("&key=AIzaSyBbg_NKsrP_kFMfxcW6vK27ECc7Ne_D23o");
        return urlString.toString();
    }

    private List<LatLng> decodePoly(String encoded) {

        List<LatLng> poly = new ArrayList<LatLng>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((((double) lat / 1E5)),
                    (((double) lng / 1E5)));
            poly.add(p);
        }

        return poly;
    }

    private class calcTask extends AsyncTask<Void, Void, String> {
        String url;

        calcTask(String urlPass) {
            url = urlPass;
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... params) {
            JSONParser jParser = new JSONParser();
            String json = jParser.getJSONFromUrl(url);
            return json;
        }

        @Override
        protected void onPostExecute(String result) {

        }
    }
}
