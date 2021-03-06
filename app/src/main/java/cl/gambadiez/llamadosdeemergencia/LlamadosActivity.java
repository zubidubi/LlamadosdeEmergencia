package cl.gambadiez.llamadosdeemergencia;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;


public class LlamadosActivity extends ActionBarActivity {

    public final static String ID_EXTRA = "cl.gambadiez.llamadosdeemergencia._ID";
    private List<Llamado> llamados = new ArrayList<>();
    private ListView llamadosListview;
    private MySQLiteHelper db = new MySQLiteHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent i = getIntent();
        //Obtendo el mensaje de la notification
        Bundle extras = i.getExtras();
        String jsonMessage = extras != null ? extras.getString("com.parse.Data") : "";
        String message = "";
        JSONObject jObject;
        try {
            if (jsonMessage != null && !jsonMessage.equals("")) {
                jObject = new JSONObject(jsonMessage);
                message = jObject.getString("alert");

                Log.d("PUSH", "Mensaje = " + message);
                String[] splittedMsg = message.split("::");
                Llamado llamadoNot = new Llamado(splittedMsg[0], splittedMsg[1], splittedMsg[2], splittedMsg[3], new Date() );
                db.addLLamado(llamadoNot);
                Intent intent = new Intent(this.getBaseContext(), Map.class);
                intent.putExtra(ID_EXTRA, llamadoNot);
                startActivity(intent);
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }

        //"Clave 1::SECTOR PUERTO::Errazuriz, Valparaiso::81 - 51 - 21";


        setContentView(R.layout.activity_llamados);
        llamadosListview = (ListView) findViewById(R.id.llamadosListView);
        llamadosListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(view.getContext(), Map.class);
                intent.putExtra(ID_EXTRA, llamados.get(position));
                startActivity(intent);
            }
        });
        updateLlamadosList();
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

        return super.onOptionsItemSelected(item);
    }

    private void updateLlamadosList()
    {
        updateFromTwitter();

        llamados = db.getAllLlamados();
        //añadir llamado dummy si esta vacia la db
        for(Llamado ll : llamados)
        {
            db.deleteLlamado(ll);
        }
        /*if(llamados.isEmpty())
        {
            db.addLLamado(new Llamado("Clave 1","SECTOR PUERTO", "Errazuriz, Valparaiso","81 - 51 - 21", new Date()));
            db.addLLamado(new Llamado("Clave 1","SECTOR PUERTO", "Nueva York, Valparaiso","21", new Date()));
            db.addLLamado(new Llamado("Clave 2","SECTOR PUERTO", "Colon, Valparaiso","11", new Date()));
            db.addLLamado(new Llamado("Clave 5-1","SECTOR PUERTO", "Las Heras, Valparaiso","81 - 51 - 21", new Date()));
            llamados = db.getAllLlamados();
        }*/

        updateLlamadosListView();
    }

    private void updateFromTwitter()
    {
        //TODO:: obtener desde twetter
        //agregando llamados dummy
        //llamados.add(new Llamado("1","SECTOR PUERTO", "Playa Ancha, Pacífico con Río Frío","81 - 51 - 21", new Date()));
    }

    private void updateLlamadosListView()
    {
        ArrayAdapter<Llamado> adapter = new LLamadoArrayAdapter();
        llamadosListview.setAdapter(adapter);
    }

    private class LLamadoArrayAdapter extends ArrayAdapter<Llamado>
    {
        public LLamadoArrayAdapter()
        {
            super(LlamadosActivity.this,R.layout.llamado_item_view, llamados);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            View itemView = convertView;
            if(itemView == null)
                itemView = getLayoutInflater().inflate(R.layout.llamado_item_view,parent,false);

            Llamado currentLlamado = llamados.get(position);

            //set image
            ImageView imageView =(ImageView) itemView.findViewById(R.id.iconoImageView);
            imageView.setImageResource(currentLlamado.getIconResourceID());

            //set llamado
            TextView llamadoTextView = (TextView) itemView.findViewById(R.id.claveTextView);
            llamadoTextView.setText(currentLlamado.getClave());

            //set descripcion
            TextView descripcionTextView = (TextView) itemView.findViewById(R.id.descripcionTextView);
            descripcionTextView.setText("Direccion: " + currentLlamado.getDireccion() + ". Unidades: " + currentLlamado.getUnidades());

            //set timestamp
            Format formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");

            TextView horaTextView = (TextView) itemView.findViewById(R.id.horaTextView);
            horaTextView.setText(formatter.format(currentLlamado.getDate()));

            return itemView;
            //return super()
        }
    }
}
