package cl.gambadiez.llamadosdeemergencia;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.util.Timer;
import java.util.TimerTask;


public class Llamados extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_llamados);

        timer = new Timer();
        timer.scheduleAtFixedRate(timerTask, 0, 6000);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_llamados, menu);

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

        Intent myIntent  = new Intent(Llamados.this, Map.class);
        startActivity(myIntent);
        return super.onOptionsItemSelected(item);
    }

    private Timer timer;
    int i = 0;
    private TimerTask timerTask = new TimerTask() {

        @Override
        public void run() {
            switch(i) {
                case 0:
                Log.d("llamados", "CLAVE 1 :: SECTOR PUERTO :: Playa Ancha, Pacífico\n" +
                        "con Río Frío :: Unidades 81 - 51 - 21");
                    break;
                case 1:
                Log.d("llamados", "CLAVE 2 :: [ALM] RODELILLO VILLA RAPA NUI CALLE HANGA ROA :: "+
                        "UNIDAD 151.");
                    break;
                case 2:
                Log.d("llamados", "CLAVE 15 :: [ALM] SAN IGNACIO HOSPITAL CARLOS VAN BUREN :: "
                        +"UNIDAD 101.");
                    //Intent myIntent  = new Intent(Llamados.this, Map.class);
                    //startActivity(myIntent);
                    break;

            }
            i++;
        }
    };

}
