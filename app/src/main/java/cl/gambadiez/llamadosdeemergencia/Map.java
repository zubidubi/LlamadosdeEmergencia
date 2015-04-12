package cl.gambadiez.llamadosdeemergencia;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Debug;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONObject;
import org.w3c.dom.Document;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class Map extends ActionBarActivity {

    public GPSTracker gps;

    public double latitude = 0;
    public double longitude = 0;

    private Llamado llamado;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.e("llamados","onCreate");
        Log.d("documento", "xx1");
        Intent intent = getIntent();
        Log.d("documento", "xx2");
        llamado = intent.getParcelableExtra(LlamadosActivity.ID_EXTRA);
        Log.d("documento", "xx3");

        gps = new GPSTracker(Map.this);
        // current location
        Log.d("documento", "xx4");

        // check if GPS enabled
        if(gps.canGetLocation()){
            latitude = gps.getLatitude();
            longitude = gps.getLongitude();

            Log.e("llamados", "xx5 gps latitude: " + String.valueOf(latitude) + " longitude " + String.valueOf(longitude));

            // \n is for new line
            Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
        }else{
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
        }

        setContentView(R.layout.activity_map);

        setUpMapIfNeeded();
        Log.d("documento", "xx6");
/*
         TextView llamadoTextView = (TextView) findViewById(R.id.textoEnMapa);
        llamadoTextView.setText("Clave: " + llamado.getClave() + "\n Sector: " + llamado.getSector() + "\n Direccion: " + llamado.getDireccion() + "\n Unidades: " + llamado.getUnidades());
*/

        buscar(llamado.getDireccion()+",Chile");
    }
    public void DrawRuta(PolylineOptions rectLine)
    {

        mMap.addPolyline(rectLine);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_map, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.



    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }


    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.

        Log.e("llamados", "SETUPMAP 00");
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            //mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
               //     .getMap();
            Log.e("llamados", "SETUPMAP 11");
            mMap  = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                Log.e("llamados", "SETUPMAP 22");
                setUpMap();
            }
        }
    }
    private final LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(final Location location) {
            //your code here
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    };

    //
     // This is where we can add markers or lines, add listeners or move the camera. In this case, we
     // just add a marker near Africa.
     // <p/>
     // This should only be called once and when we are sure that {@link #mMap} is not null.
     //
    private void setUpMap() {
        //mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
    }

    // Setting click event listener for the find button
    private void buscar(String location){

        if(location==null || location.equals("")){
            Toast.makeText(getBaseContext(), "No Place is entered", Toast.LENGTH_SHORT).show();
            return;
        }

        String url = "https://maps.googleapis.com/maps/api/geocode/json?";

        try {
            // encoding special characters like space in the user input place
            location = URLEncoder.encode(location, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String address = "address=" + location;

        String sensor = "sensor=false";

        // url , from where the geocoding data is fetched
        url = url + address + "&" + sensor;

        // Instantiating DownloadTask to get places from Google Geocoding service
        // in a non-ui thread
        Log.e("llamados", "BUSCANDO NUEVA POSICION");
        DownloadTask downloadTask = new DownloadTask();

        // Start downloading the geocoding places
        downloadTask.execute(url);
    }
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try{
            URL url = new URL(strUrl);
            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while( ( line = br.readLine()) != null){
                sb.append(line);
            }

            data = sb.toString();
            br.close();

        }catch(Exception e){
            Log.d("Exception downloading", e.toString());
        }finally{
            iStream.close();
            urlConnection.disconnect();
        }

        return data;
    }
    // A class, to download Places from Geocoding webservice
    private class DownloadTask extends AsyncTask<String, Integer, String> {

        String data = null;

        // Invoked by execute() method of this object
        @Override
        protected String doInBackground(String... url) {
            try{
                data = downloadUrl(url[0]);
            }catch(Exception e){
                Log.d("Background Task", e.toString());
            }

            Log.e("llamados", "BUSCANDO NUEVA POSICION 22");
            return data;
        }

        // Executed after the complete execution of doInBackground() method
        @Override
        protected void onPostExecute(String result){

            // Instantiating ParserTask which parses the json data from Geocoding webservice
            // in a non-ui thread
            ParserTask parserTask = new ParserTask();

            // Start parsing the places in JSON format
            // Invokes the "doInBackground()" method of the class ParseTask
            Log.e("llamados", "BUSCANDO NUEVA POSICION 33");
            parserTask.execute(result);

        }
    }

    MarkerOptions currentLocation = new MarkerOptions();
    // A class to parse the Geocoding Places in non-ui thread
    class ParserTask extends AsyncTask<String, Integer, List<HashMap<String,String>>>{

        JSONObject jObject;

        // Invoked by execute() method of this object
        @Override
        protected List<HashMap<String,String>> doInBackground(String... jsonData) {

            List<HashMap<String, String>> places = null;
            GeocodeJSONParser parser = new GeocodeJSONParser();

            try{
                jObject = new JSONObject(jsonData[0]);

                // Getting the parsed data as a an ArrayList
                places = parser.parse(jObject);

            }catch(Exception e){
                Log.d("Exception",e.toString());
            }
            Log.e("llamados", "BUSCANDO NUEVA POSICION 44");
            return places;
        }
        GPSTracker gps;
        // Executed after the complete execution of doInBackground() method
        @Override
        protected void onPostExecute(List<HashMap<String,String>> list){

            Log.e("llamados", "BUSCANDO NUEVA POSICION 55");
            // Clears all the existing markers
            mMap.clear();

            for(int i=0;i<list.size();i++){

                // Creating a marker
                MarkerOptions markerOptions = new MarkerOptions();

                // Getting a place from the places list
                HashMap<String, String> hmPlace = list.get(i);

                // Getting latitude of the place
                double lat = Double.parseDouble(hmPlace.get("lat"));

                // Getting longitude of the place
                double lng = Double.parseDouble(hmPlace.get("lng"));

                // Getting name
                String name = hmPlace.get("formatted_address");

                LatLng latLng = new LatLng(lat, lng);
                Log.e("llamados", "ZZZZ: " + String.valueOf(latLng.latitude) + " " + String.valueOf(latLng.longitude));
                // Setting the position for the marker
                markerOptions.position(latLng);

                // Setting the title for the marker
                markerOptions.title(name).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));

                // Placing a marker on the touched position
                mMap.addMarker(markerOptions);






                LatLng ll = new LatLng(latitude, longitude);
                currentLocation.position(ll);
                // Setting the title for the marker
/*                currentLocation.title("Estás aquí");
                currentLocation.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)).alpha(0.3f);

                // Placing a marker on the touched position
                mMap.addMarker(currentLocation);
*/

                RetrieveFeedTask executer = new RetrieveFeedTask();
                executer.execute(ll, latLng);
                //drawPrimaryLinePath(ll, latLng);

                // Locate the first location
                if(i==0) {
                    CameraPosition cp = new CameraPosition.Builder().target(latLng).zoom(14).build();
                    //mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                    mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cp));
                    Log.e("llamados", "MOVIENDO CAMARA A POS: " + String.valueOf(latLng.latitude));
                }
            }
        }
        class RetrieveFeedTask extends AsyncTask<LatLng, Void, PolylineOptions> {

            GMapV2Direction md;
            @Override
            protected void onPostExecute(PolylineOptions result) {
                DrawRuta(result);
            }
            @Override
            protected PolylineOptions doInBackground(LatLng... latLngs) {
                md = new GMapV2Direction();
                //mMap = ((SupportMapFragment) getSupportFragmentManager()
                //      .findFragmentById(R.id.map)).getMap();

                Log.d("documento", "a1");
                Document doc = md.getDocument(latLngs[0] , latLngs[1],
                        GMapV2Direction.MODE_DRIVING);

                Log.d("documento", "a2");
                Log.d("documento", String.valueOf(doc.getElementsByTagName("step").getLength()));
                Log.d("documento", "a3");

                ArrayList<LatLng> directionPoint = md.getDirection(doc);
            PolylineOptions rectLine = new PolylineOptions().width(3).color(
                    Color.RED);
            Log.d("documento", "a4");
            for (int i = 0; i < directionPoint.size(); i++) {
                rectLine.add(directionPoint.get(i));
            }//31:54.199

            //mMap.addPolyline(rectLine);
                //Map.DrawRuta(rectLine);
                return rectLine;
            }


        }




    }

}
