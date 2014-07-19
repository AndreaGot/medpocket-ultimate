package it.scigot.medpocket;

import it.scigot.DB.DataBaseHelper;
import it.scigot.DB.Farmaci_a;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

/**
 * In questa classe vengono visualizzati tutti gli eventi in corso presenti sull'applicazione, ed è possibile crearne di nuovi.
 * 
 * @author Antonio Sciarretta
 *
 */
public class DetailArmadietto extends Activity {
	
	ListView eventi = null;
	DataBaseHelper db = null;
	ArrayList<Farmaci_a> dati_evento = null;
	ArrayList<HashMap<String, String>> datiEventi = null;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_armadietto);
        setTitle("Armadietto Farmaci");
        
        eventi = (ListView) findViewById(R.id.lstArmadietto);
        db = new DataBaseHelper(this);
        
        datiEventi = db.getAllEventiOnce();
		final String[] from = { "evento", "numero" };
		final int[] toLayoutId = new int[] { android.R.id.text1, android.R.id.text2 };
		final SimpleAdapter adapter = new SimpleAdapter(this, datiEventi, android.R.layout.simple_list_item_2, from, toLayoutId);
		eventi.setAdapter(adapter);
		eventi.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> av, View view, int i, long l) {
				final HashMap<String, String> value = (HashMap<String, String>) adapter.getItem(i);
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(DetailArmadietto.this);

				// set title
				alertDialogBuilder.setTitle("Inserisci nuovo evento");

				// set dialog message
				alertDialogBuilder.setMessage("Vuoi inserire un nuovo evento con i dati di questo farmaco?")
						.setCancelable(true).setPositiveButton("Inserisci", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								//Porta alla pagina di inserimento evento
								Intent intent = new Intent(DetailArmadietto.this, AddEvent.class); 
							    intent.putExtra("medicinale", value.get("evento"));
							    intent.putExtra("provenienza", "DetailArmadietto");
							    startActivity(intent);
							}
						}).setNegativeButton("Annulla", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
							}
						}).setIcon(R.drawable.medpocket_main_dialog);
				// create alert dialog
				AlertDialog alertDialog = alertDialogBuilder.create();
				// show it
				alertDialog.show();
			}
		});

	}
    


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.detail_armadietto, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    
}
