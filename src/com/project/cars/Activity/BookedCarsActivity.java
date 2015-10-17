package com.project.cars.Activity;

import java.lang.reflect.Type;
import java.util.ArrayList;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.project.cars.BookedCar;
import com.project.cars.BookedCarListAdapter;
import com.project.cars.Car;
import com.project.cars.R;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;

public class BookedCarsActivity extends AppCompatActivity {

	Toolbar toolbar;

	ListView carList;

	ArrayList<Car> cars = new ArrayList<Car>();
	ArrayList<BookedCar> bookedCars = new ArrayList<BookedCar>();
	BookedCarListAdapter adapter;

	SharedPreferences bookCarSavedData;
	Editor editor;
	Gson gson;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_booked_cars);
		toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		initialize();

	}

	@SuppressWarnings("unchecked")
	private void initialize() {
		// TODO Auto-generated method stub
		bookCarSavedData = getSharedPreferences(
				getResources().getString(R.string.bookedCarSharedPreference),
				MODE_PRIVATE);

		gson = new Gson();

		editor = bookCarSavedData.edit();

		String bookedCar = bookCarSavedData.getString("bookedCars", null);

		if (bookedCar != null) {

			Type type2 = new TypeToken<ArrayList<BookedCar>>() {
			}.getType();

			Object obj = gson.fromJson(bookedCar, type2);

			bookedCars = (ArrayList<BookedCar>) obj;

			carList = (ListView) findViewById(R.id.bookedCarList);
			adapter = new BookedCarListAdapter(BookedCarsActivity.this,
					R.layout.booked_car_list_item, bookedCars);

			carList.setAdapter(adapter);

		}

	}

}
