package com.project.cars.Activity;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.project.cars.BookedCar;
import com.project.cars.Car;
import com.project.cars.R;
import com.project.cars.common.CacheImage;
import com.project.cars.common.ImageDownloader;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

public class CarDetailsActivity extends AppCompatActivity {

	Toolbar toolbar;
	int carId;
	Car car;

	CacheImage imageCache = CacheImage.getInstance();
	ImageDownloader imagedownloader = new ImageDownloader();

	private GoogleMap googlemap;

	ImageView carImage;
	TextView carName, carPrice, carSeater, carAC, sms, share;
	Button send, back, book;
	CardView smsView, bookCarView;
	EditText phoneNumber;
	RatingBar carRating;
	Button pickDate;

	SharedPreferences bookCarSavedData;
	Editor editor;

	private int year;
	private int month;
	private int day;
	Calendar date = Calendar.getInstance(), currentDate = Calendar
			.getInstance();
	SimpleDateFormat dateformat = new SimpleDateFormat("dd/MM/yyyy",
			Locale.getDefault());

	Gson gson;

	ArrayList<BookedCar> bookedCars = new ArrayList<BookedCar>();

	static final int DATE_DIALOG_ID = 999;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_car_detail);
		toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			carId = extras.getInt("carId");
		}

		car = Car.getCarById(carId);

		if (car != null) {

			initialize();

		}

	}

	private void initialize() {

		carImage = (ImageView) findViewById(R.id.carImage);
		carName = (TextView) findViewById(R.id.carName);
		carPrice = (TextView) findViewById(R.id.carPrice);
		carSeater = (TextView) findViewById(R.id.seater);
		carRating = (RatingBar) findViewById(R.id.carRating);
		carAC = (TextView) findViewById(R.id.ac);

		setUpCarInformation(car);
		setUpMap(car);

		sms = (TextView) findViewById(R.id.SMS);
		share = (TextView) findViewById(R.id.share);
		smsView = (CardView) findViewById(R.id.card_view4);
		send = (Button) findViewById(R.id.Send);
		back = (Button) findViewById(R.id.Back);
		phoneNumber = (EditText) findViewById(R.id.phoneNumber);

		shareAndSmsSetUp(car);

		bookCarView = (CardView) findViewById(R.id.card_view5);
		bookCarView.setCardBackgroundColor(getResources().getColor(
				R.color.Mariner));
		book = (Button) findViewById(R.id.book);
		pickDate = (Button) findViewById(R.id.pickDate);

		bookCarSavedData = getSharedPreferences(
				getResources().getString(R.string.bookedCarSharedPreference),
				MODE_PRIVATE);

		gson = new Gson();

		editor = bookCarSavedData.edit();

		bookCarSetUp();

	}

	private void bookCarSetUp() {
		// TODO Auto-generated method stub

		final Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH) + 1;
		int day = c.get(Calendar.DAY_OF_MONTH);
		String dateString = day + "/" + month + "/" + year;

		try {

			currentDate.setTime(dateformat.parse(dateString));
			date.setTime(dateformat.parse(dateString));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		pickDate.setText(dateString);

		book.setOnClickListener(new OnClickListener() {

			@SuppressWarnings("unchecked")
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				String bookedCar = bookCarSavedData.getString("bookedCars",
						null);

				if (bookedCar != null) {

					Type type2 = new TypeToken<ArrayList<BookedCar>>() {
					}.getType();

					Object obj = gson.fromJson(bookedCar, type2);

					bookedCars = (ArrayList<BookedCar>) obj;

					boolean booked = false;

					Log.d("Date", "" + date);

					for (BookedCar c : bookedCars) {

						if (c.getCarId() == carId && date.equals(c.getDate())) {

							booked = true;

							displayToast("The car is already booked on this date.");

							break;

						}
					}

					if (!booked) {

						bookCar();
						displayToast("Booked");

					}

				} else {

					bookCar();
					displayToast("Booked");
				}

			}
		});

	}

	protected void bookCar() {
		// TODO Auto-generated method stub

		bookedCars.add(new BookedCar(carId, date));

		editor.putString("bookedCars", gson.toJson(bookedCars));

		editor.commit();

	}

	public void displayToast(String s) {

		Toast t = Toast
				.makeText(CarDetailsActivity.this, s, Toast.LENGTH_SHORT);

		t.setGravity(Gravity.CENTER_HORIZONTAL, 0, 40);

		t.show();

	}

	private void setUpCarInformation(Car car) {
		// TODO Auto-generated method stub

		final Bitmap bitmap = imageCache.getBitmapFromMemCache(car.getCarId()
				+ "");
		if (bitmap != null) {
			carImage.setImageBitmap(bitmap);
		} else {

			imagedownloader.download(car.getCarImage(), carImage,
					car.getCarId() + "");

		}

		carName.setText(car.getCarName());

		carPrice.setText(car.getCarPrice() + " per hr");

		carSeater.setText("Seater: " + car.getSeater() + "+1");

		carRating.setRating(car.getCarRating());

		if (car.getAc().equalsIgnoreCase("1")) {

			carAC.setText("AC : YES");
		} else {

			carAC.setText("AC : NO");
		}

	}

	private void setUpMap(Car car) {

		if (isGooglePlay()) {

			MapFragment mapFragment = (MapFragment) getFragmentManager()
					.findFragmentById(R.id.map);

			// initialize google map . To perform ui operations on map .
			googlemap = mapFragment.getMap();

			displayMarkerOnMap(car.getCarLatlng());

		}

	}

	private void shareAndSmsSetUp(Car car) {
		// TODO Auto-generated method stub

		String location = "http://maps.google.com/maps?q="
				+ car.getCarLatlng().latitude + ","
				+ car.getCarLatlng().longitude + "&iwloc=A";

		final String messageText = "Name: " + car.getCarName() + '\n'
				+ "Rate per hour: " + car.getCarPrice() + '\n' + "Type: "
				+ car.getCarType() + '\n' + "Seater: " + car.getSeater() + "+1"
				+ '\n' + "ImageURL: " + car.getCarImage() + '\n'
				+ carAC.getText() + '\n' + "Location: " + location;

		send.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Toast t;
				if (phoneNumber.getText().length() == 10) {

					try {

						SmsManager sms = SmsManager.getDefault();

						sms.sendTextMessage(phoneNumber.getText().toString(),
								null, messageText, null, null);

					} catch (Exception e) {
						Toast.makeText(getApplicationContext(),
								"SMS faild, please try again.",
								Toast.LENGTH_LONG).show();
						e.printStackTrace();
					}

					smsView.setVisibility(View.GONE);

					t = Toast.makeText(CarDetailsActivity.this, "Sms Send",
							Toast.LENGTH_SHORT);

					t.setGravity(Gravity.CENTER_HORIZONTAL, 0, 40);
					t.show();

				} else {

					t = Toast.makeText(CarDetailsActivity.this,
							"Enter a Valid Number of 10 digits",
							Toast.LENGTH_SHORT);
					t.setGravity(Gravity.CENTER_HORIZONTAL, 0, 40);
					t.show();
				}
			}
		});
		sms.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				smsView.setVisibility(View.VISIBLE);

			}
		});

		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				smsView.setVisibility(View.GONE);
			}
		});

		share.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Intent sendIntent = new Intent();
				sendIntent.setAction(Intent.ACTION_SEND);
				sendIntent.putExtra(Intent.EXTRA_TEXT, messageText);
				sendIntent.setType("text/plain");
				startActivity(sendIntent);

			}
		});

	}

	private void displayMarkerOnMap(LatLng l) {
		// TODO Auto-generated method stub

		googlemap.clear();
		Geocoder geocoder = new Geocoder(getBaseContext(), Locale.getDefault());

		// try to get the address from the location and display
		// the marker
		try {
			String display = "";
			List<Address> address = geocoder.getFromLocation(l.latitude,
					l.longitude, 2);

			if (address.size() > 0) {

				for (int i = 0; i < address.get(0).getMaxAddressLineIndex(); i++) {

					display += address.get(0).getAddressLine(i) + "\n";
				}

			}

			DisplayMarker(l, display, "Car is Here");

			googlemap.animateCamera(CameraUpdateFactory.newLatLngZoom(l, 15));

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private boolean isGooglePlay() {
		int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
		if (status == ConnectionResult.SUCCESS) {

			return (true);

		} else {
			GooglePlayServicesUtil.getErrorDialog(status, this, 10).show();
			Toast.makeText(this, "google play services is not available",
					Toast.LENGTH_SHORT).show();

		}

		return (false);

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

			Intent i = new Intent(CarDetailsActivity.this,
					BookedCarsActivity.class);

			startActivity(i);
		}
		return super.onOptionsItemSelected(item);
	}

	private void DisplayMarker(LatLng l, String Address, String Title) {
		// TODO Auto-generated method stub
		googlemap.addMarker(new MarkerOptions()
				.position(l)
				.title(Title)
				.snippet(Address)
				.icon(BitmapDescriptorFactory
						.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
	}

	/*
	 * public static class DatePickerFragment extends DialogFragment implements
	 * DatePickerDialog.OnDateSetListener {
	 * 
	 * @Override public Dialog onCreateDialog(Bundle savedInstanceState) { //
	 * Use the current date as the default date in the picker final Calendar c =
	 * Calendar.getInstance(); int year = c.get(Calendar.YEAR); int month =
	 * c.get(Calendar.MONTH); int day = c.get(Calendar.DAY_OF_MONTH);
	 * 
	 * DatePickerDialog d = new DatePickerDialog(getActivity(), this, year,
	 * month, day); DatePicker dp = d.getDatePicker();
	 * dp.setMinDate(c.getTimeInMillis());
	 * 
	 * return d;
	 * 
	 * // Create a new instance of DatePickerDialog and return it
	 * 
	 * }
	 * 
	 * public void onDateSet(DatePicker view, int year, int month, int day) { //
	 * Do something with the date chosen by the user
	 * 
	 * } }
	 */

	private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {

		// when dialog box is closed, below method will be called.
		public void onDateSet(DatePicker view, int selectedYear,
				int selectedMonth, int selectedDay) {

			year = selectedYear;
			month = selectedMonth + 1;
			day = selectedDay;

			// set selected date into textview

			String dateString = day + "/" + month + "/" + year;

			try {

				date.setTime(dateformat.parse(dateString));

			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if (date.before(currentDate)) {

				displayToast("Past dates are not allowed");
			} else {

				pickDate.setText(dateString);

			}

		}
	};

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DATE_DIALOG_ID:
			// set date picker as current date

			final Calendar c = Calendar.getInstance();

			int year = c.get(Calendar.YEAR);
			int month = c.get(Calendar.MONTH);
			int day = c.get(Calendar.DAY_OF_MONTH);

			DatePickerDialog d = new DatePickerDialog(this, datePickerListener,
					year, month, day);

			DatePicker dp = d.getDatePicker();

			dp.setMinDate(c.getTimeInMillis());

			return d;

		}
		return null;
	}

	public void showDatePickerDialog(View v) {

		showDialog(DATE_DIALOG_ID);

	}

}