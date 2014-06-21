package it.scigot.DB;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
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
	private static String TABELLA_FARMACI = "farmaci_a";
	private static String TABELLA_FARMACIE = "farmacie_trento";
	private static String TABELLA_CALENDARIO = "";
	private SQLiteDatabase myDataBase;
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

	// Getting All Contacts
	public ArrayList<HashMap<String, String>>  getAllFarmaciWhere(String campo, String valore) {
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
		}
		else {
			farmaco = new HashMap<String, String>();
			farmaco.put("descr", "Nessun farmaco trovato!");
			farmaco.put("princ", "Per favore, riprova!");
			list.add(farmaco);
		}

		// return contact list
		return list;
	}
	// Add your public helper methods to access and get content from the
	// database.
	// You could return cursors by doing "return myDataBase.query(....)" so it'd
	// be easy
	// to you to create adapters for your views.
}
