package it.scigot.medpocket;

import it.scigot.DB.DataBaseHelper;
import it.scigot.DB.Farmaci_a;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class DetailConvertitore extends Activity {

	// TODO: valutare se è possibile effettuare le query in base alla scrittura
	// dell'utente. Se si fanno con textchanged listener sulla EditText diventa
	// inutilizzabile.

	EditText txtFarmaci = null;
	ListView lstFarmaci = null;
	Button btnVai = null;
	ArrayList<Farmaci_a> result_farmaci = null;
	ArrayList<HashMap<String, String>> datiFarmaci = null;
	DataBaseHelper db = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail_convertitore);
		txtFarmaci = (EditText) findViewById(R.id.txtFarmaci);
		lstFarmaci = (ListView) findViewById(R.id.lstFarmaci);
		btnVai = (Button) findViewById(R.id.btnVai);

		db = new DataBaseHelper(this);

		btnVai.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				String value = txtFarmaci.getText().toString();
				datiFarmaci = db.getAllFarmaciWhere("denominazione", value);
				updateList();
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

	public void updateList() {
		final String[] from = { "descr", "princ" };
		final int[] toLayoutId = new int[] { android.R.id.text1, android.R.id.text2 };
		SimpleAdapter adapter = new SimpleAdapter(this, datiFarmaci, android.R.layout.simple_list_item_2, from, toLayoutId);
		lstFarmaci.setAdapter(adapter);
	}
}
