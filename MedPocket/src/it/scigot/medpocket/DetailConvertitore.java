package it.scigot.medpocket;

import it.scigot.DB.DataBaseHelper;
import it.scigot.DB.Farmaci_a;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
/**
 * 
 * @author Andrea
 *
 */
public class DetailConvertitore extends Activity {

	// TODO: valutare se è possibile effettuare le query in base alla scrittura
	// dell'utente. Se si fanno con textchanged listener sulla EditText diventa
	// inutilizzabile.

	EditText txtFarmaci = null;
	ListView lstFarmaci = null;
	ArrayList<Farmaci_a> result_farmaci = null;
	ArrayList<HashMap<String, String>> datiFarmaci = null;
	DataBaseHelper db = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail_convertitore);
		setTitle("Convertitore Farmaci");
		txtFarmaci = (EditText) findViewById(R.id.txtFarmaci);
		lstFarmaci = (ListView) findViewById(R.id.lstFarmaci);

		db = new DataBaseHelper(this);

		txtFarmaci.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			private Timer timer = new Timer();
			private final long DELAY = 500; // in ms

			
			//Aspetto 500 ms prima di procedere con la ricerca. Così si evita di lanciare il metodo ad ogni lettera premuta
			@Override
			public void afterTextChanged(final Editable s) {
				timer.cancel();
				timer = new Timer();
				timer.schedule(new TimerTask() {
					@Override
					public void run() {
						DetailConvertitore.this.runOnUiThread(new Runnable() {
							public void run() {
								String value = txtFarmaci.getText().toString();
								datiFarmaci = db.getAllFarmaciWhere("denominazione", value);
								updateList();
							}
						});
					}
				}, DELAY);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.detail_convertitore, menu);
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
	
	//aggiorna la lista in base ai farmaci risultanti dal convertitore

	public void updateList() {
		final String[] from = { "descr", "princ" };
		final int[] toLayoutId = new int[] { android.R.id.text1, android.R.id.text2 };
		final SimpleAdapter adapter = new SimpleAdapter(this, datiFarmaci, android.R.layout.simple_list_item_2, from, toLayoutId);
		lstFarmaci.setAdapter(adapter);
		lstFarmaci.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> av, View view, int i, long l) {
				final HashMap<String, String> value = (HashMap<String, String>) adapter.getItem(i);
				String[] dialog = db.showFarmaco(value.get("descr"));
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(DetailConvertitore.this);

				// set title
				alertDialogBuilder.setTitle(dialog[1]);

				// set dialog message
				alertDialogBuilder.setMessage(Html.fromHtml("<b> Principio attivo:</b> <br><pre>    " + dialog[0] + "</pre><br><b> Ditta: </b><br><pre>    " + dialog[3] + "</pre><br><b> Prezzo: </b><br><pre>    €" + dialog[2] + "</pre>"))
						.setCancelable(true).setPositiveButton("OK", new DialogInterface.OnClickListener() {
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
}
