package cl.gambadiez.llamadosdeemergencia;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


public class MapaActivity extends ActionBarActivity {

    private Llamado llamado;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa);
        Intent intent = getIntent();
        llamado = intent.getParcelableExtra(LlamadosActivity.ID_EXTRA);
        TextView llamadoTextView = (TextView) findViewById(R.id.llamadoTextView);
        llamadoTextView.setText("Clave: " + llamado.getClave() + "\n Sector: " + llamado.getSector() + "\n Direccion: " + llamado.getDireccion() + "\n Unidades: " + llamado.getUnidades());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_mapa, menu);
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
}
