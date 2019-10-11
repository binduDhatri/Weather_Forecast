package com.weatherforecast.utils;

import org.springframework.stereotype.Component;
/**
 * 
 * @author bindu
 *
 */
@Component
public class HelperUtil {

	/**
	 * 
	 * @param timeStr
	 * @return Date in String formate hh:mm
	 */
	public String getHourMin(String timeStr){
		int t = Integer.parseInt(timeStr);
		int hours = t / 60; //since both are ints, you get an int
		int minutes = t % 60;
		System.out.printf("%d:%02d", hours, minutes);
	
		return hours+":"+minutes;
	}
}
