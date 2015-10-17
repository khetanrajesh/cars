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

	public View getView(int position, View convertView, ViewGroup parent) {

		// assign the view we are converting to a local variable
		View v = convertView;

		// first check to see if the view is null. if so, we have to inflate it.
		// to inflate it basically means to render, or show, the view.
		if (v == null) {
			LayoutInflater inflater = (LayoutInflater) getContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = inflater.inflate(R.layout.car_list_item, null);
		}

		/*
		 * Recall that the variable position is sent in as an argument to this
		 * method. The variable simply refers to the position of the current
		 * object in the list. (The ArrayAdapter iterates through the list we
		 * sent it)
		 * 
		 * Therefore, i refers to the current Item object.
		 */
		Car i = objects.get(position);

		if (i != null) {

			// This is how you obtain a reference to the TextViews.
			// These TextViews are created in the XML files we defined.

			TextView carName = (TextView) v.findViewById(R.id.carName);
			ImageView carImage = (ImageView) v.findViewById(R.id.carImage);
			TextView carPrice = (TextView) v.findViewById(R.id.carPrice);

			RatingBar carRating = (RatingBar) v.findViewById(R.id.carRating);
			

			// check to see if each individual textview is null.
			// if not, assign some text!
			if (carName != null) {
				carName.setText(i.getCarName());
			}

			if (carPrice != null) {
				carPrice.setText("₹" + i.getCarPrice() + " per hr");
			}
			if (carImage != null) {

				final Bitmap bitmap = ci.getBitmapFromMemCache(i.getCarId()
						+ "");
				if (bitmap != null) {

					Log.d("ParcelListAdapter",
							"Getting from cache image " + i.getCarId());
					carImage.setImageBitmap(bitmap);
				} else {

					imagedownloader.download(i.getCarImage(), carImage,
							i.getCarId() + "");
					
				}
			}
			
			if(carRating!=null){
				
				carRating.setRating(i.getCarRating());
			}
			
			
		}

		// the view must be returned to our activity
		return v;

	}

}
