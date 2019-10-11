package com.usa.weatherforecast.service;

import java.util.List;

public class ForecastWeather {

	List<TimeFrames> timeframe;

	public List<TimeFrames> getTimeframe() {
		return timeframe;
	}

	public void setTimeframe(List<TimeFrames> timeframe) {
		this.timeframe = timeframe;
	}
}
