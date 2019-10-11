package com.usa.weatherforecast.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import org.apache.tomcat.util.json.JSONParser;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.weatherforecast.utils.HelperUtil;

/**
 * 
 * @author bindu
 * 
 * The WeatherService class 
 * 
 * consists methods: 
 * getWeatherData(zipcode) returns List of TimeFrames Object for tomorrow. 
 * 
 * 
 *
 */
@Component
public class WeatherService {
	
	@Autowired
	HelperUtil helper;
	
	private static final Logger LOGGER = Logger.getLogger(WeatherService.class.getName());

	/**
	 * 
	 * @param zipCode
	 * @return List<TimeFrames>
	 * 
	 * Calls createHttpRequests : methods which creates the url and send request for API then returns response 
	 * Calls parseJson : method which parse the response and collects required fields
	 * 
	 */
	public List<TimeFrames> getWeatherData(String zipCode) {
		URL url = createUrl(zipCode);
		String jsonResponse = null;
		List<TimeFrames> listOfTimeFrame = null;
		System.out.println(url);
		try {
			//send request and get response from API
			jsonResponse = createHttpRequest(url);
			//parse the response and collect List of Time-frames for tomorrow
			listOfTimeFrame = parseJson(jsonResponse);
		} catch (Exception e) {
			LOGGER.warning("exception " + e);
			System.out.println("error " + e);
		}
		return listOfTimeFrame;
	}

	/**
	 * 
	 * @param jsonResponse
	 * @return
	 * @throws JsonProcessingException
	 * @throws IOException
	 */
	private List<TimeFrames> parseJson(String jsonResponse) throws JsonProcessingException, IOException {
		
		List<TimeFrames> listOfTimeFrame = null;
		JSONObject obj = new JSONObject(jsonResponse);
		JSONArray listOfJson = (JSONArray) obj.get("Days");

		Gson g = new Gson();
		//list of days 
		List<Days> listOfDays = g.fromJson(listOfJson.toString(), new TypeToken<List<Days>>() {
		}.getType());
		
		//get tomorrow's date + get a date to represent "today" + add one day to the date/calendar
		Calendar calendar = Calendar.getInstance();
	    Date today = calendar.getTime();
	    System.out.println("today:    " + today);
	    calendar.add(Calendar.DAY_OF_YEAR, 1);
	    // get "tomorrow"
	    Date tomorrowDate = calendar.getTime();
	    System.out.println("tomorrow: " + tomorrowDate);
		
	    //convert date to localdate
	    //1. Convert Date -> Instant
	    ZoneId defaultZoneId = ZoneId.systemDefault();
        Instant instant = tomorrowDate.toInstant();
      //  System.out.println("instant : " + instant); //Zone : UTC+0

        //2. Instant + system default time zone + toLocalDate() = LocalDate
        LocalDate localDate = instant.atZone(defaultZoneId).toLocalDate();
        
	    String tomorrowDateStr =
	   		  DateTimeFormatter.ofPattern("dd/MM/yyyy").format(localDate);
	    
	//    System.out.println("Tomorrow date is "+ tomorrowDateStr);
		//Collections.
		
	    //fetch tomorrow's weather object 
	    Days tomorrowDayObj = listOfDays.stream()
	    		  .filter(dateStr -> tomorrowDateStr.equals(dateStr.getDate()))
	    		  .findAny()
	    		  .orElse(null);
	    if(tomorrowDayObj!=null) {
	    	listOfTimeFrame = tomorrowDayObj.getTimeFrameObj();

	    }
	    
		return listOfTimeFrame;

	}
	
	/**
	 * 
	 * @param listOfTimeFrame
	 * @return minimum timeframe object consist of coolest temperature in list of tomorrow's weatherforecast's timeframes. 
	 */
	public TimeFrames getMinTimeFrame(List<TimeFrames> listOfTimeFrame ){
		TimeFrames minTimeFrame = null;
		if(listOfTimeFrame!=null) {
	    	minTimeFrame = Collections.min(listOfTimeFrame, Comparator.comparing(s -> s.getTemp_f()));
			System.out.println("The min temperature is " + minTimeFrame.getTemp_f() + "" + minTimeFrame.getDate() + ""
					+ minTimeFrame.getTime() +" "+minTimeFrame.getWx_desc());
		
	    }
		return minTimeFrame;
	}

	/**
	 * 
	 * @param zipCode
	 * @return actual URL to send request for WeatherUnlocked API
	 */
	private URL createUrl(String zipCode) {
		// create url
		URL url = null;
		//remove hard code value 
		String app_Id = "bde4ecae";
		String app_key = "bf9ea0ec59dae4587e263ee0ffe74ce9";
		try {
				url = new URL("http://api.weatherunlocked.com/api/forecast/us." + zipCode + "?app_id=" + app_Id
					+ "&app_key=" + app_key + "");
		} catch (MalformedURLException e) {
			LOGGER.warning("Problem building the URL " + e);
			System.out.println("error " + e);
		}

		return url;
	}

	/**
	 * 
	 * @param url
	 * @return response from Weatherunlocked API
	 * @throws IOException
	 */
	private String createHttpRequest(URL url) throws IOException {
		String jsonResponse = "";
		if (url != null) {
			HttpURLConnection urlConnection = null;
			InputStream inputStream = null;
			try {
				urlConnection = (HttpURLConnection) url.openConnection();
				urlConnection.setRequestMethod("GET");
				urlConnection.setReadTimeout(10000);
				urlConnection.setConnectTimeout(15000);
				urlConnection.connect();
				if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
					inputStream = urlConnection.getInputStream();
					jsonResponse = readFromStream(inputStream);
				} else {

					LOGGER.info("QueryUtils: Error response code: " + urlConnection.getResponseCode());
				}
			} catch (IOException e) {

				LOGGER.warning("QueryUtils: Problem retrieving the JSON results." + e);
			} finally {
				if (urlConnection != null)
					urlConnection.disconnect();
				if (inputStream != null)
					inputStream.close();
			}
		}
		return jsonResponse;
	}

	/**
	 * 
	 * @param inputStream
	 * @return
	 * @throws IOException
	 */
	private String readFromStream(InputStream inputStream) throws IOException {
		StringBuilder output = new StringBuilder();
		if (inputStream != null) {
			InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
			BufferedReader reader = new BufferedReader(inputStreamReader);
			String line;
			do {
				line = reader.readLine();
				output.append(line);
			} while (line != null);
		}
		return output.toString();
	}
}
