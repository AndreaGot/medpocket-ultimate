package it.scigot.medpocket;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

public class DetailTrovaFarmacie extends FragmentActivity {

	private GoogleMap mMap;
	static final LatLng PERTH = new LatLng(-31.90, 115.86);
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail_trova_farmacie);
		setUpMapIfNeeded();

		
		 mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(PERTH, 10));	
		
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
		mMap = ((SupportMapFragment) getSupportFragmentManager()
				.findFragmentById(R.id.map)).getMap();
		if (mMap == null) {
			return;
		}
		// Initialize map options. For example:
		// mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		// NON TOGLIERE IL COMMENTO
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

	public void get_coordinates() { // TODO: inserire al posto di void LatLng e
									// ritornare centerpoint

		LocationManager mymanager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

		Location location = mymanager
				.getLastKnownLocation(LocationManager.GPS_PROVIDER);

		// LatLng center_point = new LatLng(lat, longi);
		// return center_point;
	}

}
