package com.project.cars;

import java.util.ArrayList;
import java.util.Calendar;
import com.google.gson.Gson;
import com.project.cars.common.CacheImage;
import com.project.cars.common.ImageDownloader;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class BookedCarListAdapter extends ArrayAdapter<BookedCar> {

	private ArrayList<BookedCar> objects;

	SharedPreferences bookCarSavedData;
	Editor editor;

	Gson gson;

	ImageDownloader imagedownloader = new ImageDownloader();

	CacheImage ci = CacheImage.getInstance();

	public BookedCarListAdapter(Context context, int textViewResourceId,
			ArrayList<BookedCar> objects) {

		super(context, textViewResourceId, objects);
		this.objects = objects;

	}

	public View getView(final int position, View v, ViewGroup parent) {

		ViewHolder holder;

		bookCarSavedData = getContext().getSharedPreferences(
				getContext().getResources().getString(
						R.string.bookedCarSharedPreference),
				getContext().MODE_PRIVATE);

		gson = new Gson();

		editor = bookCarSavedData.edit();

		if (v == null) {
			LayoutInflater inflater = (LayoutInflater) getContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = inflater.inflate(R.layout.booked_car_list_item, null);

			holder = new ViewHolder();

			holder.carName = (TextView) v.findViewById(R.id.carName);

			holder.cancel = (Button) v.findViewById(R.id.cancel);

			v.setTag(holder);

		} else {

			holder = (ViewHolder) v.getTag();
		}

		BookedCar i = objects.get(position);

		if (i != null) {

			String name = Car.getCarById(i.getCarId()).getCarName();

			Calendar d = i.getDate();

			Calendar now = Calendar.getInstance();

			String dateString = d.get(Calendar.DAY_OF_MONTH) + "/"
					+ (d.get(Calendar.MONTH) + 1) + "/" + d.get(Calendar.YEAR);

			holder.carName.setText(name + " (" + dateString + ")");

			if (d.before(now)
					&& !(d.get(Calendar.DAY_OF_MONTH) == now
							.get(Calendar.DAY_OF_MONTH) && d
							.get(Calendar.MONTH) == now.get(Calendar.MONTH))
					&& d.get(Calendar.YEAR) == now.get(Calendar.YEAR)) {

				holder.cancel.setText("Completed");
				holder.cancel.setEnabled(false);

			}

			else {

				holder.cancel.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub

						objects.remove(position);

						editor.putString("bookedCars", gson.toJson(objects));

						editor.commit();

						Toast.makeText(getContext(), "Booking Cancelled",
								Toast.LENGTH_SHORT).show();

						notifyDataSetChanged();

					}
				});
			}

		}

		return v;

	}

	static class ViewHolder {

		TextView carName;

		Button cancel;

	}
}
