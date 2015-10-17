package com.project.cars.Activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONObject;

import com.google.android.gms.maps.model.LatLng;
import com.project.cars.Car;
import com.project.cars.CarListAdapter;
import com.project.cars.CarsAPI;
import com.project.cars.R;
import com.project.cars.common.Utility;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

	static Toolbar toolbar;

	AutoCompleteTextView searchBox;

	ListView carList;

	CardView searchView, loadingView, listView, countView, sortTextView,
			sortView;

	TextView processingText, apiHits, totalCars, sortByName, sortByPrice,
			sortByRating;

	// private static final String LOG_TAG = "mainActivity";

	ArrayList<Car> carArrayLocal = new ArrayList<Car>();

	ImageView clearButton;

	JSONObject object = null;

	CarListAdapter adapter;

	int numberOfCars;
	String apiHit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		initialize();

	}

	private void initialize() {
		// TODO Auto-generated method stub

		searchBox = (AutoCompleteTextView) findViewById(R.id.search);

		searchView = (CardView) findViewById(R.id.card_view1);
		loadingView = (CardView) findViewById(R.id.card_view2);
		listView = (CardView) findViewById(R.id.card_view3);
		countView = (CardView) findViewById(R.id.card_view4);
		sortTextView = (CardView) findViewById(R.id.card_view5);
		sortView = (CardView) findViewById(R.id.card_view6);

		searchView.setVisibility(View.GONE);
		listView.setVisibility(View.GONE);
		countView.setVisibility(View.GONE);
		sortTextView.setVisibility(View.GONE);
		sortView.setVisibility(View.GONE);

		processingText = (TextView) findViewById(R.id.processingText);
		apiHits = (TextView) findViewById(R.id.apiHits);
		totalCars = (TextView) findViewById(R.id.totalCars);
		totalCars = (TextView) findViewById(R.id.totalCars);
		sortByName = (TextView) findViewById(R.id.sortByName);
		sortByPrice = (TextView) findViewById(R.id.sortByPrice);
		sortByRating = (TextView) findViewById(R.id.sortByRating);

		carList = (ListView) findViewById(R.id.carList);

		clearButton = (ImageView) findViewById(R.id.clear);

		setListAdapter(Car.carArray);

		setUpSearchBar();

		if (Utility.isNetworkAvailable(getApplicationContext())) {

			AsynGetCars object = new AsynGetCars();

			object.execute();
		} else {

			processingText.setText("Connection Error");

		}

		carList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view,
					int position, long id) {

				Intent intent = new Intent(MainActivity.this,
						CarDetailsActivity.class);

				Car item = (Car) adapterView.getItemAtPosition(position);

				intent.putExtra("carId", item.getCarId());
				startActivity(intent);

			}
		});

		sortByName.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Collections.sort(carArrayLocal);

				setListAdapter(carArrayLocal);

			}
		});

		sortByPrice.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Collections.sort(carArrayLocal, Car.CarPriceComparator);

				setListAdapter(carArrayLocal);

			}
		});

		sortByRating.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Collections.sort(Car.carArray, Car.CarRatingComparator);

				setListAdapter(Car.carArray);

			}
		});

	}

	private void setUpSearchBar() {
		// TODO Auto-generated method stub

		searchBox.setOnFocusChangeListener(new OnFocusChangeListener() {

			public void onFocusChange(View v, boolean hasFocus) {
				if (!hasFocus) {

					clearButton.setVisibility(View.GONE);

				}

				if (hasFocus) {

					clearButton.setVisibility(View.VISIBLE);

					clearButton.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							searchBox.setText("");

						}
					});

				}
			}
		});

		searchBox.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

				ArrayList<Car> carArrayfiltered = new ArrayList<Car>();

				for (Car item : Car.carArray) {
					if (item.getCarName() != null
							&& (item.getCarName().toUpperCase(Locale
									.getDefault())).contains(s.toString()
									.toUpperCase(Locale.getDefault()))) {

						carArrayfiltered.add(item);

					}
					// something here
				}

				setListAdapter(carArrayfiltered);

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub

			}
		});

	}

	private boolean getCars() {
		// TODO Auto-generated method stub

		CarsAPI parcelsAPI = new CarsAPI();
		JSONArray cars = null;
		String carImage;
		LatLng carLatlng;
		String carPrice, carName, carType, ac, seater, carRating;

		try {

			object = parcelsAPI.getCars();

			cars = object.getJSONArray("cars");

			numberOfCars = cars.length();

			Car.carArray.clear();

			for (int i = 0; i < cars.length(); i++) {

				carName = (String) ((JSONObject) cars.get(i)).get("name");

				carPrice = (String) ((JSONObject) cars.get(i))
						.get("hourly_rate");
				carImage = (String) ((JSONObject) cars.get(i)).get("image");

				carType = (String) ((JSONObject) cars.get(i)).get("type");

				ac = (String) ((JSONObject) cars.get(i)).get("ac");

				seater = (String) ((JSONObject) cars.get(i)).get("seater");

				carRating = (String) ((JSONObject) cars.get(i)).get("rating");

				carLatlng = new LatLng(
						(double) ((JSONObject) ((JSONObject) cars.get(i))
								.get("location")).getDouble("latitude"),
						(double) ((JSONObject) ((JSONObject) cars.get(i))
								.get("location")).getDouble("longitude"));

				Car.carArray.add(new Car(carImage, carLatlng, carPrice,
						carName, carType, (int) Double.parseDouble(carRating),
						ac, seater, i));
			}

			carArrayLocal = Car.carArray;

		} catch (Exception E) {

			Log.e("Stack Trace", "Error Parsing JSON", E);

			return false;

		}
		return true;

	}

	private void getApiHits() {
		// TODO Auto-generated method stub

		CarsAPI parcelsAPI = new CarsAPI();

		try {

			object = parcelsAPI.getAPIHits();

			apiHit = (String) (object.get("api_hits"));

		} catch (Exception E) {

			Log.e("Stack Trace", "Error Parsing JSON", E);

		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
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

		if (id == R.id.booked_history) {

			Intent i = new Intent(MainActivity.this, BookedCarsActivity.class);

			startActivity(i);
		}
		return super.onOptionsItemSelected(item);
	}

	private class AsynGetCars extends AsyncTask<String, Integer, JSONObject> {

		boolean success;

		@Override
		protected void onPreExecute() {

			listView.setVisibility(View.GONE);
			searchView.setVisibility(View.GONE);
			countView.setVisibility(View.GONE);
			sortView.setVisibility(View.GONE);
			sortTextView.setVisibility(View.GONE);

			super.onPreExecute();
		}

		@Override
		protected void onPostExecute(JSONObject result) {

			super.onPostExecute(result);

			if (this.success) {

				listView.setVisibility(View.VISIBLE);
				searchView.setVisibility(View.VISIBLE);
				countView.setVisibility(View.VISIBLE);
				sortView.setVisibility(View.VISIBLE);
				sortTextView.setVisibility(View.VISIBLE);

				loadingView.setVisibility(View.GONE);

				adapter.notifyDataSetChanged();

				totalCars.setText("Total Cars: " + numberOfCars);

				apiHits.setText("API Hits: " + apiHit);

			} else {

				processingText.setText("An error occured");

			}

		}

		@Override
		protected JSONObject doInBackground(String... query) {

			this.success = getCars();

			getApiHits();

			return null;

		}

	};

	private void setListAdapter(ArrayList<Car> carArray) {
		// TODO Auto-generated method stub

		adapter = new CarListAdapter(MainActivity.this, R.layout.car_list_item,
				carArray);

		carList.setAdapter(adapter);
	}
}
