package it.scigot.medpocket;

import it.scigot.DB.DataBaseHelper;

import java.util.HashMap;
import java.util.Iterator;

import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class DetailTrovaFarmacie extends FragmentActivity implements
		LocationListener {

	private GoogleMap mMap;
	private String provider;
	private LocationManager locationManager;
	Marker startPerc = null;
	DataBaseHelper db = null;
	HashMap<Integer,HashMap<String, String>> coordinate = null;

	static final LatLng TRENTO = new LatLng(46.0793, 11.1302);

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail_trova_farmacie);
		setUpMapIfNeeded();

		LocationManager service = (LocationManager) getSystemService(LOCATION_SERVICE);
		boolean enabled = service
				.isProviderEnabled(LocationManager.GPS_PROVIDER);

		// check if enabled and if not send user to the GSP settings
		// Better solution would be to display a dialog and suggesting to
		// go to the settings
		if (!enabled) {
			Intent intent = new Intent(
					android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
			startActivity(intent);
		}

		// Define the criteria how to select the locatioin provider -> use
		// default
		Criteria criteria = new Criteria();
		provider = service.getBestProvider(criteria, false);
		Location location = service.getLastKnownLocation(provider);

		// Initialize the location fields
		if (location != null) {
			System.out.println("Provider " + provider + " has been selected.");
			onLocationChanged(location);

		} else {
			db = new DataBaseHelper(this);

			coordinate = db.getCoordinate();
			
			Iterator<Integer> keySetIterator = coordinate.keySet().iterator();

			while(keySetIterator.hasNext()){
			  Integer key = keySetIterator.next();
			 HashMap<String,String> prova=coordinate.get(key);
			
			 //LatLng lol = convertiCoordinate;
			
			}
			
			startPerc = mMap.addMarker(new MarkerOptions()
			//.position(coordinate)
			.title("Farmacia")
			.icon(BitmapDescriptorFactory
					.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
			
			
			
			
			mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(TRENTO, 10.5f));
		}

	}

	public void onLocationChanged(Location loc) {

		double lat = loc.getLatitude();
		double lng = loc.getLongitude();

		LatLng coordinate = new LatLng(lat, lng);

		startPerc = mMap.addMarker(new MarkerOptions()
				.position(coordinate)
				.title("Current Location")
				.icon(BitmapDescriptorFactory
						.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));

		mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(coordinate, 18.0f));
	}

	@Override
	protected void onResume() {
		super.onResume();
		setUpMapIfNeeded();

		// locationManager.requestLocationUpdates(provider, (long)400, (float)1,
		// (android.location.LocationListener) this );
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


	
}
