package com.project.cars;

import java.util.ArrayList;
import java.util.Comparator;

import com.google.android.gms.maps.model.LatLng;

public class Car implements Comparable<Car> {

	public static ArrayList<Car> carArray = new ArrayList<Car>();

	private String carImage;

	private LatLng carLatlng;

	private String carPrice, carName, carType, ac, seater;

	private int carId, carRating;

	public Car(String carImage, LatLng carLatlng, String carPrice,
			String carName, String carType, int carRating, String ac,
			String seater, int carId) {
		super();
		this.carImage = carImage;
		this.carLatlng = carLatlng;
		this.carPrice = carPrice;
		this.carName = carName;
		this.carType = carType;
		this.carRating = carRating;
		this.ac = ac;
		this.seater = seater;
		this.carId = carId;
	}

	public String getCarImage() {
		return carImage;
	}

	public void setCarImage(String carImage) {
		this.carImage = carImage;
	}

	public LatLng getCarLatlng() {
		return carLatlng;
	}

	public void setCarLatlng(LatLng carLatlng) {
		this.carLatlng = carLatlng;
	}

	public String getCarPrice() {
		return carPrice;
	}

	public void setCarPrice(String carPrice) {
		this.carPrice = carPrice;
	}

	public String getCarName() {
		return carName;
	}

	public void setCarName(String carName) {
		this.carName = carName;
	}

	public String getCarType() {
		return carType;
	}

	public void setCarType(String carType) {
		this.carType = carType;
	}

	public int getCarRating() {
		return carRating;
	}

	public void setCarRating(int carRating) {
		this.carRating = carRating;
	}

	public String getAc() {
		return ac;
	}

	public void setAc(String ac) {
		this.ac = ac;
	}

	public String getSeater() {
		return seater;
	}

	public void setSeater(String seater) {
		this.seater = seater;
	}

	public int getCarId() {
		return carId;
	}

	public void setCarId(int carId) {
		this.carId = carId;
	}

	public int compareTo(Car compareCar) {

		String compareCarName = ((Car) compareCar).getCarName();

		// ascending order
		return this.carName.compareTo(compareCarName);

		// descending order
		// return compareQuantity - this.quantity;

	}

	public static Comparator<Car> CarPriceComparator = new Comparator<Car>() {

		@Override
		public int compare(Car item1, Car item2) {

			return (int) (Integer.parseInt(item1.carPrice) - Integer
					.parseInt(item2.carPrice));

		}

	};

	public static Comparator<Car> CarRatingComparator = new Comparator<Car>() {

		@Override
		public int compare(Car item1, Car item2) {

			return (item1.carRating - item2.carRating);

		}

	};

	public static Car getCarById(int carId) {

		for (Car car : carArray) {

			if (car.getCarId() == carId) {

				return car;
			}
		}
		return null;

	}
}
