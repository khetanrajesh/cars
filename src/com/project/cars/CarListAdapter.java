package com.project.cars;

import java.util.ArrayList;

import com.project.cars.common.CacheImage;
import com.project.cars.common.ImageDownloader;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

public class CarListAdapter extends ArrayAdapter<Car> {

	private ArrayList<Car> objects;

	ImageDownloader imagedownloader = new ImageDownloader();

	CacheImage ci = CacheImage.getInstance();

	public CarListAdapter(Context context, int textViewResourceId,
			ArrayList<Car> objects) {

		super(context, textViewResourceId, objects);
		this.objects = objects;
	}

	public View getView(int position, View v, ViewGroup parent) {

		ViewHolder holder;

		if (v == null) {
			LayoutInflater inflater = (LayoutInflater) getContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = inflater.inflate(R.layout.car_list_item, null);

			holder = new ViewHolder();

			holder.carName = (TextView) v.findViewById(R.id.carName);
			holder.carImage = (ImageView) v.findViewById(R.id.carImage);
			holder.carPrice = (TextView) v.findViewById(R.id.carPrice);

			holder.carRating = (RatingBar) v.findViewById(R.id.carRating);

			v.setTag(holder);
		} else {

			holder = (ViewHolder) v.getTag();

		}

		Car i = objects.get(position);

		if (i != null) {

			holder.carName.setText(i.getCarName());

			holder.carPrice.setText("₹" + i.getCarPrice() + " per hr");

			final Bitmap bitmap = ci.getBitmapFromMemCache(i.getCarId() + "");
			if (bitmap != null) {

				Log.d("ParcelListAdapter",
						"Getting from cache image " + i.getCarId());
				holder.carImage.setImageBitmap(bitmap);
			} else {

				imagedownloader.download(i.getCarImage(), holder.carImage,
						i.getCarId() + "");

			}

			holder.carRating.setRating(i.getCarRating());
		}

		return v;

	}

	static class ViewHolder {

		TextView carName;
		ImageView carImage;
		TextView carPrice;

		RatingBar carRating;

	}

}
