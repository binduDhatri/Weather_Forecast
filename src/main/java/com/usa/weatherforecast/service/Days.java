package com.usa.weatherforecast.service;

import java.util.List;

public class Days {

	List<TimeFrames> Timeframes;
	public List<TimeFrames> getTimeFrameObj() {
		return Timeframes;
	}

	public void setTimeFrameObj(List<TimeFrames> timeFrameObj) {
		this.Timeframes = timeFrameObj;
	}

	String date;

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	

	
	
}
