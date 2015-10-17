package com.project.cars;

import java.util.Calendar;

public class BookedCar {

	int carId;
	Calendar date;

	public int getCarId() {
		return carId;
	}

	public void setCarId(int carId) {
		this.carId = carId;
	}

	public Calendar getDate() {
		return date;
	}

	public void setDate(Calendar date) {
		this.date = date;
	}

	public BookedCar(int carId, Calendar date) {
		super();
		this.carId = carId;
		this.date = date;
	}

}
