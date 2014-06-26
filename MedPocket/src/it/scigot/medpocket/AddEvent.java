package it.scigot.medpocket;

import it.scigot.DB.DataBaseHelper;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;


public class AddEvent extends Activity {
	
	private DataBaseHelper db = null;
	private HashMap<Integer, String> mapFarmaci = null;
	private ArrayList<String> nomiFarmaci = null;
	private AutoCompleteTextView actv = null;
	
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);
        
		db = new DataBaseHelper(this);

		nomiFarmaci = db.getNomiFarmaci();
		mapFarmaci = db.getFarmaciMap();

		actv = (AutoCompleteTextView) findViewById(R.id.farmaciac);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, nomiFarmaci);
		actv.setAdapter(adapter);
        
    }
    
}
