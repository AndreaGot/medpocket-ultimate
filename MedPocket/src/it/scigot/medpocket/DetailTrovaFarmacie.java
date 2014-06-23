package it.scigot.medpocket;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.*;


public class DetailTrovaFarmacie extends FragmentActivity {

	
	
private GoogleMap mMap;

@Override
public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_detail_trova_farmacie);
    setUpMapIfNeeded();
}

@Override
protected void onResume() {
    super.onResume();
    setUpMapIfNeeded();
}

private void setUpMapIfNeeded() {
    if (mMap != null) {
        return;
    }
    mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
    if (mMap == null) {
        return;
    }
    // Initialize map options. For example:
    // mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
}
	
	
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_detail_trova_farmacie);
//	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
//		 Inflate the menu; this adds items to the action bar if it is present. NON TOGLIERE IL COMMENTO
		getMenuInflater().inflate(R.menu.detail_trova_farmacie, menu);
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
