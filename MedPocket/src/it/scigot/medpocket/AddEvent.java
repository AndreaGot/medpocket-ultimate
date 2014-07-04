package it.scigot.medpocket;

import it.scigot.DB.DataBaseHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnFocusChangeListener;
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
	private Spinner ripetizione = null;
	private Button salva = null;

	private ScheduleClient scheduleClient;

	private Integer giorniRip = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_event);
		setTitle("Aggiungi Evento");
		db = new DataBaseHelper(this);

		nomiFarmaci = db.getNomiFarmaci();
		mapFarmaci = db.getFarmaciMap();

		nome = (EditText) findViewById(R.id.nome);
		dataInizio = (EditText) findViewById(R.id.dataInizio);
		giorni = (EditText) findViewById(R.id.giorni);
		ora = (EditText) findViewById(R.id.ora);
		oreDist = (EditText) findViewById(R.id.ore);
		ripetizione = (Spinner) findViewById(R.id.ripetizione);
		repeat = (CheckBox) findViewById(R.id.repeat);
		actv = (AutoCompleteTextView) findViewById(R.id.farmaciac);
		salva = (Button) findViewById(R.id.salva);

		Intent intent = getIntent();
		if (intent != null) {
			String strdata = intent.getExtras().getString("provenienza");
			if (strdata.equals("DetailArmadietto")) {
				actv.setText(intent.getExtras().getString("medicinale"));
			}
		}
		System.out.println("ciao");
		scheduleClient = new ScheduleClient(this);
		scheduleClient.doBindService();
		dataInizio.setText("05-07-2014");
		dataInizio.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (null == dataInizio.getText() || "".equals(dataInizio.getText()) || dataInizio.getText().toString().indexOf("-") != -1) {
					return;
				}
				if (!hasFocus) {
					String[] part = null;
					String stringa = dataInizio.getText().toString();
					if (stringa.indexOf("/") != -1) {
						part = stringa.split("/");
					} else if (stringa.indexOf(".") != -1) {
						part = stringa.split("\\.");
					}
					if (part[0].length() == 1) {
						part[0] = "0" + part[0];
					}
					// scrivi nella textbox

					dataInizio.setText(part[0] + "-" + part[1] + "-" + part[2]);
				}
			}
		});

		repeat.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if (repeat.isChecked()) {
					ripetizione.setEnabled(true);
					giorni.setEnabled(true);
				} else {
					ripetizione.setEnabled(false);
					giorni.setEnabled(false);
				}
			}
		});

		ripetizione.setEnabled(false);
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
					righe = addEvents(nome.getText().toString().trim(), dataInizio.getText().toString(), ora.getText().toString(), mapFarmaci.get(actv.getText().toString()), giorniRip, giorni.getText().toString().trim(), oreDist.getText().toString()
							.trim());
				} else {
					Calendar c = setCustomDateAndTime(dataInizio.getText().toString(), ora.getText().toString());
					scheduleClient.setAlarmForNotification(c);
					System.err.println("Allarme inserito");
					scheduleClient.doUnbindService();
					esito = addSingleEvent(nome.getText().toString().trim(), dataInizio.getText().toString(), ora.getText().toString(), mapFarmaci.get(actv.getText().toString()));
				}

				if (esito || righe > 0) {
					Toast.makeText(getApplicationContext(), "Evento inserito!", Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(getApplicationContext(), "ERRORE!", Toast.LENGTH_LONG).show();
				}

				if (righe > 0) {
					System.out.println("inserite " + righe + "righe");
				}

				finish();
			}

		});

	}

	private Integer addEvents(String nome, String data, String ora, Integer idMedicina, Integer ripetizione, String step, String oreDist) {
		Integer interval = 0;
		Calendar cal = setCustomDateAndTime(data, ora);
		;
		Date singleDate = null;
		String stringDate = "";
		SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
		SimpleDateFormat hf = new SimpleDateFormat("HH:mm");
		singleDate = cal.getTime();
		if (ripetizione == 0) {
			stringDate = df.format(singleDate);
			String stringHour = hf.format(singleDate);
			addSingleEvent(nome, stringDate, stringHour, idMedicina);
			for (Integer i = 0; i < Integer.parseInt(step) - 1; i++) {
				cal.add(Calendar.HOUR_OF_DAY, Integer.parseInt(oreDist));
				singleDate = cal.getTime();
				stringDate = df.format(singleDate);
				stringHour = hf.format(singleDate);
				addSingleEvent(nome, stringDate, stringHour, idMedicina);
			}
		} else {

			stringDate = df.format(singleDate);
			addSingleEvent(nome, stringDate, ora, idMedicina);
			for (Integer i = 0; i < Integer.parseInt(step) - 1; i++) {
				cal.add(Calendar.DAY_OF_MONTH, ripetizione);
				singleDate = cal.getTime();
				stringDate = df.format(singleDate);
				addSingleEvent(nome, stringDate, ora, idMedicina);
			}

		}
		return Integer.parseInt(step);
	}

	public boolean addSingleEvent(String nome, String data, String ora, Integer idMedicina) {
		db.openDataBaseReadWrite();
		ContentValues insertValues = new ContentValues();
		insertValues.put("nome", nome);
		insertValues.put("data", data);
		insertValues.put("ora", ora);
		insertValues.put("medicinale", idMedicina);
		if (db.myDataBase.insert("Evento", null, insertValues) != -1) {
			// Calendar c = setCustomDateAndTime(data, ora);
			System.out.println("INSERITI");
			db.myDataBase.close();
			return true;
		} else {
			System.err.println("ERRORE");
			db.myDataBase.close();
			return false;
		}

	}

	private Calendar setCustomDateAndTime(String data, String ora) {
		String[] dataPart = data.split("-");
		String[] oraPart = ora.split(":");
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, Integer.parseInt(dataPart[2]));
		// i mesi vanno ridotti di 1 (0 è gennaio)
		cal.set(Calendar.MONTH, Integer.parseInt(dataPart[1]) - 1);
		cal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dataPart[0]));
		cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(oraPart[0]));
		cal.set(Calendar.MINUTE, Integer.parseInt(oraPart[1]));
		return cal;
	}

}
