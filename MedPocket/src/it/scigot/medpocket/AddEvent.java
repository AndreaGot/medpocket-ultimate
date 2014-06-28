package it.scigot.medpocket;

import it.scigot.DB.DataBaseHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class AddEvent extends Activity {

	private DataBaseHelper db = null;
	private HashMap<String, Integer> mapFarmaci = null;
	private ArrayList<String> nomiFarmaci = null;
	private AutoCompleteTextView actv = null;
	private EditText nome = null;
	private EditText dataInizio = null;
	private CheckBox repeat = null;
	private EditText giorni = null;
	private EditText ora = null;
	private EditText oreDist = null;
	private EditText numPastiglie = null;
	private Spinner ripetizione = null;
	private Button salva = null;

	private Integer giorniRip = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_event);
		db = new DataBaseHelper(this);

		nomiFarmaci = db.getNomiFarmaci();
		mapFarmaci = db.getFarmaciMap();

		nome = (EditText) findViewById(R.id.nome);
		dataInizio = (EditText) findViewById(R.id.dataInizio);
		giorni = (EditText) findViewById(R.id.giorni);
		ora = (EditText) findViewById(R.id.ora);
		oreDist = (EditText) findViewById(R.id.ore);
		numPastiglie = (EditText) findViewById(R.id.pastiglie);
		ripetizione = (Spinner) findViewById(R.id.ripetizione);
		repeat = (CheckBox) findViewById(R.id.repeat);
		actv = (AutoCompleteTextView) findViewById(R.id.farmaciac);
		salva = (Button) findViewById(R.id.salva);

		repeat.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if (repeat.isChecked()) {
					ripetizione.setEnabled(true);
					numPastiglie.setEnabled(true);
					giorni.setEnabled(true);
				} else {
					ripetizione.setEnabled(false);
					numPastiglie.setEnabled(false);
					giorni.setEnabled(false);
				}
			}
		});

		ripetizione.setEnabled(false);
		numPastiglie.setEnabled(false);
		giorni.setEnabled(false);

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, nomiFarmaci);
		actv.setAdapter(adapter);

		List<String> spinnerArray = new ArrayList<String>();
		spinnerArray.add("Più volte al giorno");
		spinnerArray.add("Giornaliero");
		spinnerArray.add("Ogni due giorni");
		spinnerArray.add("Settimanale");

		ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerArray);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		ripetizione.setAdapter(spinnerAdapter);
		ripetizione.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				switch (arg2) {
				case (0):
					giorniRip = 0;
					giorni.setHint("Per quante volte?");
					oreDist.setEnabled(true);
					break;
				case (1):
					giorniRip = 1;
					giorni.setHint("Per quanti giorni?");
					oreDist.setEnabled(false);
					break;
				case (2):
					giorniRip = 2;
					giorni.setHint("Per quante volte?");
					oreDist.setEnabled(false);
					break;
				case (3):
					giorniRip = 7;
					giorni.setHint("Per quante settimane?");
					oreDist.setEnabled(false);
					break;

				}

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}

		});

		salva.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Boolean esito = false;
				Integer righe = 0;
				if (repeat.isChecked()) {
					righe = db.addEvents(nome.getText().toString().trim(), dataInizio.getText().toString(), ora.getText().toString(), mapFarmaci.get(actv.getText().toString()), giorniRip, giorni.getText().toString().trim(), oreDist.getText().toString().trim());
				} else {
					esito = db.addSingleEvent(nome.getText().toString().trim(), dataInizio.getText().toString(), ora.getText().toString(), mapFarmaci.get(actv.getText().toString()));
				}

				if (esito || righe > 0) {
					Toast.makeText(getApplicationContext(), "Evento inserito!", Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(getApplicationContext(), "ERRORE!", Toast.LENGTH_LONG).show();
				}
				
				if (righe >0) {
					System.out.println("inserite " + righe + "righe");
				}
				
				finish();
			}

		});

	}
}
