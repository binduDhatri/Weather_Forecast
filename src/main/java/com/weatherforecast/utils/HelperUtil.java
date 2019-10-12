package com.weatherforecast.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.logging.Logger;

import org.springframework.stereotype.Component;

import com.usa.weatherforecast.service.WeatherService;
/**
 * 
 * @author bindu
 *
 */
@Component
public class HelperUtil {

	private static final Logger LOGGER = Logger.getLogger(HelperUtil.class.getName());

	/**
	 * 
	 * @param zipCode
	 * @return actual URL to send request for WeatherUnlocked API
	 */
	public URL createUrl(String zipCode) {
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
	
	/**
	 * 
	 * @param url
	 * @return response from Weatherunlocked API
	 * @throws IOException
	 */
	public String createHttpRequest(URL url) throws IOException {
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
