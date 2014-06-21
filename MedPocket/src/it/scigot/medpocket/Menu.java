package it.scigot.medpocket;

import java.io.IOException;

import it.scigot.DB.DataBaseHelper;
import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class Menu extends FragmentActivity implements ActionBar.TabListener {

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the three primary sections of the app. We use a
	 * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
	 * will keep every loaded fragment in memory. If this becomes too memory
	 * intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	CollectionPagerAdapter mCollectionPagerAdapter;

	/**
	 * The {@link android.support.v4.view.ViewPager} that will display the
	 * object collection.
	 */
	ViewPager mViewPager;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// controlla se il DB è presente. In caso negativo, lo crea.
		checkDBExistence();
		
		setContentView(R.layout.activity_menu);
		// Create an adapter that when requested, will return a fragment
		// representing an object in
		// the collection.
		//
		// ViewPager and its adapters use support library fragments, so we must
		// use
		// getSupportFragmentManager.
		mCollectionPagerAdapter = new CollectionPagerAdapter(getSupportFragmentManager());

		// Set up action bar.
		final ActionBar actionBar = getActionBar();

		// Specify that the Home/Up button should not be enabled, since there is
		// no hierarchical
		// parent.
		try {
			actionBar.setHomeButtonEnabled(false);

			// Specify that we will be displaying tabs in the action bar.
			actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

			// Set up the ViewPager, attaching the adapter and setting up a
			// listener
			// for when the
			// user swipes between sections.
			mViewPager = (ViewPager) findViewById(R.id.pager);
			mViewPager.setAdapter(mCollectionPagerAdapter);
			mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
				@Override
				public void onPageSelected(int position) {
					// When swiping between different app sections, select
					// the corresponding tab.
					// We can also use ActionBar.Tab#select() to do this if
					// we have a reference to the
					// Tab.
					actionBar.setSelectedNavigationItem(position);
				}
			});

			// For each of the sections in the app, add a tab to the action bar.
			for (int i = 0; i < mCollectionPagerAdapter.getCount(); i++) {
				// Create a tab with text corresponding to the page title
				// defined by
				// the adapter.
				// Also specify this Activity object, which implements the
				// TabListener interface, as the
				// listener for when this tab is selected.
				actionBar.addTab(actionBar.newTab().setText(mCollectionPagerAdapter.getPageTitle(i)).setTabListener(this));
			}
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
	}

	public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
	}

	public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
		// When the given tab is selected, switch to the corresponding page in
		// the ViewPager.
		mViewPager.setCurrentItem(tab.getPosition());
	}

	public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
	}
	
	public void checkDBExistence() {
		DataBaseHelper dbh = new DataBaseHelper(getApplicationContext());
		try {
			dbh.createDataBase();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * A fragment that launches other parts of the application.
	 */
	public static class TabFragment extends Fragment {
		public static final String ARG_OBJECT = "object";

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			Bundle args = getArguments();
			int position = args.getInt(ARG_OBJECT);
			int tabLayout = 0;
			switch (position) {
			case 0:
				tabLayout = R.layout.armadietto_menu;
				break;
			case 1:
				tabLayout = R.layout.trovafarmacie_menu;
				break;
			case 2:
				tabLayout = R.layout.convertitore_menu;
				break;
			case 3:
				tabLayout = R.layout.promemoria_menu;
			}
			View rootView = inflater.inflate(tabLayout, container, false);
			if (position == 0) {
				Button armadiettobutton = (Button) rootView.findViewById(R.id.button_armadietto);
				armadiettobutton.setOnClickListener(new Button.OnClickListener() {

					public void onClick(View arg0) {
						Intent i = new Intent(arg0.getContext(), DetailArmadietto.class);
						startActivity(i);

					}

				});
			} else if (position == 1) {
				Button trovaFarmacieButton = (Button) rootView.findViewById(R.id.button_trovafarmacie);
				trovaFarmacieButton.setOnClickListener(new Button.OnClickListener() {
					public void onClick(View arg0) {
						Intent i = new Intent(arg0.getContext(), DetailTrovaFarmacie.class);
						startActivity(i);
					}

				});
			} else if (position == 2) {
				Button convertitoreButton = (Button) rootView.findViewById(R.id.button_convertitore);
				convertitoreButton.setOnClickListener(new Button.OnClickListener() {
					public void onClick(View arg0) {

						Intent i = new Intent(arg0.getContext(), DetailConvertitore.class);
						startActivity(i);
					}
				});
			} else if (position == 3) {
				Button promemoriaButton = (Button) rootView.findViewById(R.id.button_promemoria);
				promemoriaButton.setOnClickListener(new Button.OnClickListener() {

					public void onClick(View arg0) {
						Intent i = new Intent(arg0.getContext(), DetailPromemoria.class);
						startActivity(i);
					}
				});
			}
			return rootView;
		}
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the primary sections of the app.
	 */
	public class CollectionPagerAdapter extends FragmentPagerAdapter {

		final int NUM_ITEMS = 4; // number of tabs

		public CollectionPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int i) {
			Fragment fragment = new TabFragment();
			Bundle args = new Bundle();
			args.putInt(TabFragment.ARG_OBJECT, i);
			fragment.setArguments(args);
			return fragment;
		}

		@Override
		public int getCount() {
			return NUM_ITEMS;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			String tabLabel = null;
			switch (position) {
			case 0:
				tabLabel = getString(R.string.label1);
				break;
			case 1:
				tabLabel = getString(R.string.label2);
				break;
			case 2:
				tabLabel = getString(R.string.label3);
				break;
			case 3:
				tabLabel = getString(R.string.label4);
				break;
			}
			return tabLabel;
		}
	}
}
