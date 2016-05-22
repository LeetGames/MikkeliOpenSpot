package observis.leetgames.com.mikkeliopenspot;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 234;
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION = 312;
    private static final int MY_PERMISSIONS_REQUEST_INTERNET = 42;

      public void openMap(View view) {
        // Do something in response to button
          EditText t = (EditText) findViewById(R.id.editTextAddress);

          if(t.getText() != null) {
              if (t.getText().toString().equals("")) {
                  Intent loader = new Intent(this, Loader.class);
                  Bundle data = new Bundle();
                  data.putInt("procedureKey", 0);
                  loader.putExtras(data);
                  startActivity(loader);
              } else {
                  Switch s = (Switch) findViewById(R.id.routing_switch);

                  Intent loader = new Intent(this, Loader.class);
                  Bundle data = new Bundle();
                  data.putInt("procedureKey", 1);
                  data.putString("addressData", t.getText().toString());
                  data.putBoolean("routingOption", s.isChecked());
                  loader.putExtras(data);
                  startActivity(loader);

              }
          }
      }

    public void  decodeAddr(View view){
        EditText t = (EditText) findViewById(R.id.editTextAddress);
        Switch s = (Switch) findViewById(R.id.routing_switch);

        Intent loader = new Intent(this,Loader.class);
        Bundle data = new Bundle();
        data.putInt("procedureKey",1);
        data.putString("addressData",t.getText().toString());
        data.putBoolean("routingOption",s.isChecked());
        loader.putExtras(data);
        startActivity(loader);


    }

    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        Toast.makeText(this, "The Switch is " + (isChecked ? "on" : "off"),
                Toast.LENGTH_SHORT).show();
        if(isChecked) {
            //do stuff when Switch is ON
        } else {
            //do stuff when Switch if OFF
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Spinner spinner = (Spinner) findViewById(R.id.time_spinner);
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.time_array, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        if (ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

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
    }







}

