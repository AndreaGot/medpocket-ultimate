package it.scigot.DB;

import it.scigot.medpocket.ScheduleClient;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

@SuppressWarnings("unused")
public class DataBaseHelper extends SQLiteOpenHelper {

	// The Android's default system path of your application database.
	private static String DB_PATH = "/data/data/it.scigot.medpocket/databases/";
	private static String DB_NAME = "MedPocket.sqlite";
	private final static String TABELLA_FARMACI = "farmaci_a";
	private final static String TABELLA_FARMACIE = "farmacie_trento";
	private final static String TABELLA_CALENDARIO = "evento";
	public SQLiteDatabase myDataBase;
	private final Context myContext;

	/**
	 * Constructor Takes and keeps a reference of the passed context in order to
	 * access to the application assets and resources.
	 * 
	 * @param context
	 */
	public DataBaseHelper(Context context) {
		super(context, DB_NAME, null, 1);
		this.myContext = context;
	}

	public void openDataBaseReadOnly() throws SQLException {
		// Open the database
		String myPath = DB_PATH + DB_NAME;
		myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
	}

	public void openDataBaseReadWrite() throws SQLException {
		// Open the database
		String myPath = DB_PATH + DB_NAME;
		myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
	}

	@Override
	public synchronized void close() {
		if (myDataBase != null)
			myDataBase.close();

		super.close();
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}

	// TODO: i seguenti metodi sono utilizzati solo all'inizio
	// dell'applicazione, per vedere se il database esiste, e quindi copiarlo se
	// non c'è
	// Valutare l'idea di renderli statici, in modo da non dover necessariamente
	// istanziare una classe per eseguire questa operazione

	/**
	 * Creates a empty database on the system and rewrites it with your own
	 * database.
	 * */
	public void createDataBase() throws IOException {
		boolean dbExist = checkDataBase();
		if (!dbExist) {
			// By calling this method and empty database will be created into
			// the default system path
			// of your application so we are gonna be able to overwrite that
			// database with our database.
			this.getReadableDatabase();
			try {
				copyDataBase();
			} catch (IOException e) {
				throw new Error("Error copying database");
			}
		}
	}

	/**
	 * Check if the database already exist to avoid re-copying the file each
	 * time you open the application.
	 * 
	 * @return true if it exists, false if it doesn't
	 */
	private boolean checkDataBase() {
		SQLiteDatabase checkDB = null;
		try {
			String myPath = DB_PATH + DB_NAME;
			checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);

		} catch (SQLiteException e) {
			// database does't exist yet.
		}
		if (checkDB != null) {
			checkDB.close();
		}
		return checkDB != null ? true : false;
	}

	/**
	 * Copies your database from your local assets-folder to the just created
	 * empty database in the system folder, from where it can be accessed and
	 * handled. This is done by transfering bytestream.
	 * */
	private void copyDataBase() throws IOException {
		// Open your local db as the input stream
		InputStream myInput = myContext.getAssets().open(DB_NAME);
		// Path to the just created empty db
		String outFileName = DB_PATH + DB_NAME;
		// Open the empty db as the output stream
		OutputStream myOutput = new FileOutputStream(outFileName);

		// transfer bytes from the inputfile to the outputfile
		byte[] buffer = new byte[1024];
		int length;
		while ((length = myInput.read(buffer)) > 0) {
			myOutput.write(buffer, 0, length);
		}
		// Close the streams
		myOutput.flush();
		myOutput.close();
		myInput.close();
	}

	public ArrayList<HashMap<String, String>> getAllFarmaciWhere(String campo, String valore) {
		HashMap<String, String> farmaco = null;
		ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>(2);
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABELLA_FARMACI + " WHERE denominazione LIKE '%" + valore + "%' OR principio_attivo LIKE '%" + valore + "%' ";
		this.openDataBaseReadOnly();
		Cursor cursor = myDataBase.rawQuery(selectQuery, null);
		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				farmaco = new HashMap<String, String>();
				farmaco.put("descr", cursor.getString(2));
				farmaco.put("princ", cursor.getString(1));
				list.add(farmaco);
			} while (cursor.moveToNext());
		} else {
			farmaco = new HashMap<String, String>();
			farmaco.put("descr", "Nessun farmaco trovato!");
			farmaco.put("princ", "Per favore, riprova!");
			list.add(farmaco);
		}
		cursor.close();
		myDataBase.close();
		return list;
	}

	public HashMap<String, Integer> getFarmaciMap() {
		HashMap<String, Integer> farmaci = new HashMap<String, Integer>();
		String selectQuery = "SELECT  * FROM " + TABELLA_FARMACI;
		this.openDataBaseReadOnly();
		Cursor cursor = myDataBase.rawQuery(selectQuery, null);
		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				farmaci.put(cursor.getString(2), cursor.getInt(0));
			} while (cursor.moveToNext());
		}
		cursor.close();
		myDataBase.close();
		return farmaci;
	}

	public HashMap<Integer, String> getFarmaciMapWithIntegerKey() {
		HashMap<Integer, String> farmaci = new HashMap<Integer, String>();
		String selectQuery = "SELECT  * FROM " + TABELLA_FARMACI;
		this.openDataBaseReadOnly();
		Cursor cursor = myDataBase.rawQuery(selectQuery, null);
		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				farmaci.put(cursor.getInt(0), cursor.getString(2));
			} while (cursor.moveToNext());
		}
		cursor.close();
		myDataBase.close();
		return farmaci;
	}

	public ArrayList<String> getNomiFarmaci() {
		ArrayList<String> farmaci = new ArrayList<String>();
		String selectQuery = "SELECT  * FROM " + TABELLA_FARMACI;
		this.openDataBaseReadOnly();
		Cursor cursor = myDataBase.rawQuery(selectQuery, null);
		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				farmaci.add(cursor.getString(2));
			} while (cursor.moveToNext());
		}
		cursor.close();
		return farmaci;
	}

	public Integer getNomiFarmaci(String nome) {
		String selectQuery = "SELECT  * FROM " + TABELLA_FARMACI + " WHERE denominazione LIKE '%" + nome + "%'";
		Integer result = -1;
		this.openDataBaseReadOnly();
		Cursor cursor = myDataBase.rawQuery(selectQuery, null);
		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			result = cursor.getInt(0);
		}
		cursor.close();
		myDataBase.close();
		return result;
	}



	public HashMap<String, Integer> findEventsByMonth(int year, int month) {
		HashMap<String, Integer> result = new HashMap<String, Integer>();
		String stringMonth = null;
		if (month < 10) {
			stringMonth = "0" + String.valueOf(month);
		} else {
			stringMonth = String.valueOf(month);
		}
		String data = "%-" + stringMonth + "-" + String.valueOf(year);
		String selectQuery = "SELECT * FROM " + TABELLA_CALENDARIO + " WHERE data LIKE '" + data + "'";
		this.openDataBaseReadOnly();
		Cursor cursor = myDataBase.rawQuery(selectQuery, null);
		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				String[] dataPart = cursor.getString(2).split("-");
				dataPart[0] = dataPart[0].startsWith("0") ? dataPart[0].substring(1) : dataPart[0];
				if (result.containsKey(dataPart[0])) {
					result.put(dataPart[0], result.get(dataPart[0]) + 1);
				} else {
					result.put(dataPart[0], 1);
				}
			} while (cursor.moveToNext());
		}
		cursor.close();
		myDataBase.close();
		return result;
	}

	// Getting All Contacts
	public ArrayList<HashMap<String, String>> getAllEventiWhere(String data) {
		HashMap<String, String> evento = null;
		ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>(2);
		if (data.indexOf("-") == 1) {
			data = "0" + data;
		}
		String selectQuery = "SELECT C.nome, C.ora, F.denominazione FROM " + TABELLA_CALENDARIO + " C INNER JOIN " + TABELLA_FARMACI + " F ON C.medicinale = F._id   WHERE data LIKE '" + data + "' ";
		this.openDataBaseReadOnly();
		Cursor cursor = myDataBase.rawQuery(selectQuery, null);
		// looping through all rows and adding to list
		HashMap<Integer, String> farmaciMap = this.getFarmaciMapWithIntegerKey();
		if (cursor.moveToFirst()) {
			do {
				evento = new HashMap<String, String>();
				evento.put("descr", cursor.getString(0));
				String sotto = cursor.getString(1) + " " + cursor.getString(2);
				evento.put("sotto", sotto);
				list.add(evento);
			} while (cursor.moveToNext());
		} else {
			evento = new HashMap<String, String>();
			evento.put("descr", "Nessun evento trovato!");
			evento.put("sotto", "Per favore, riprova!");
			list.add(evento);
		}
		cursor.close();
		myDataBase.close();
		return list;
	}

	public Boolean deleteEvent(String data, String valore) {
		String ora = valore.substring(0, 5);
		if (data.indexOf("-") == 1) {
			data = "0" + data;
		}
		String farmaco = valore.substring(5).trim();
		Integer idFarmaco = this.getNomiFarmaci(farmaco);
		openDataBaseReadWrite();
		if (myDataBase.delete(TABELLA_CALENDARIO, "data " + " LIKE '" + data + "' AND ora LIKE '" + ora + "' AND medicinale=" + String.valueOf(idFarmaco), null) > 0) {
			System.out.println("ELIMINATO");
			return true;
		} else {
			System.err.println("ERRORE");
			return false;
		}
	}

	public String[] showFarmaco(String descrizione) {
		String selectQuery = "SELECT  * FROM " + TABELLA_FARMACI + " WHERE denominazione LIKE '%" + descrizione + "%'";
		this.openDataBaseReadOnly();
		String[] result = null;
		Cursor cursor = myDataBase.rawQuery(selectQuery, null);
		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			result = new String[]{cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(4)};
		}
		cursor.close();
		myDataBase.close();

		return result;
	}
	
	public ArrayList<HashMap<String, String>> getAllEventiOnce() {
		HashMap<String, String> evento = null;
		ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>(2);
		// Select All Query
		String selectQuery = "SELECT  F.denominazione, Count(*) FROM " + TABELLA_CALENDARIO +  " C INNER JOIN " + TABELLA_FARMACI + " F ON C.medicinale = F._id GROUP BY F.denominazione";
		this.openDataBaseReadOnly();
		Cursor cursor = myDataBase.rawQuery(selectQuery, null);
		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				evento = new HashMap<String, String>();
				evento.put("evento", cursor.getString(0));
				evento.put("numero", "Assunto " + cursor.getString(1) + " volte");
				list.add(evento);
			} while (cursor.moveToNext());
		} else {
			evento = new HashMap<String, String>();
			evento.put("evento", "Nessun farmaco trovato!");
			evento.put("numero", "Per favore, riprova!");
			list.add(evento);
		}
		cursor.close();
		myDataBase.close();
		return list;
	}
}
