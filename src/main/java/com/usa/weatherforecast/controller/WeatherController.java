package com.usa.weatherforecast.controller;


import java.util.List;
import java.util.logging.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.usa.weatherforecast.service.TimeFrames;
import com.usa.weatherforecast.service.WeatherService;
import com.weatherforecast.utils.HelperUtil;

@Controller
public class WeatherController {
	private static final Logger LOGGER = Logger.getLogger(WeatherService.class.getName());
	
	@GetMapping("/")
	public String index() {
		return "index";
	}
	
	/*
	 * @Autowired(required=false) WeatherService weatherService;
	 */
	/*
	 * @Autowired HelperUtil helper;
	 */
	
	/*
	 * Controller class 
	 * 
	 * Takes Zip Code as Input 
	 * 
	 * Returns model attributes with tomorrow's coolest temperature.
	 * 
	 * WeatherUnlock API : Which returns 7 days weather-forecast along with 3 hour time frame temperature
	 * 
	 * Server class: parse the API response and fetch minimum temperature 
	 */
	
	@PostMapping("/weatherDetails")
	public String getWeatherData(@RequestParam("zipCode") String zipCode, Model model) {
		try {
			model.addAttribute("zipCode", zipCode);
			WeatherService weatherService = new WeatherService();
			HelperUtil helper = new HelperUtil();
			//returns list of time-frames for tomorrow- in 3 hour time frames
			List<TimeFrames> listOfTimeFrame  = weatherService.getWeatherData(zipCode);
			//get minimum time-frame from list of time-frame
			TimeFrames minTempOfDay = weatherService.getMinTimeFrame(listOfTimeFrame);
			//convert min in hh:mm
			String dateTime = helper.getHourMin(minTempOfDay.getTime());
			
			//send list of object into view
			model.addAttribute("listOfTimeframe", listOfTimeFrame);
			model.addAttribute("date", minTempOfDay.getDate());
			model.addAttribute("dateTime",dateTime );
			model.addAttribute("wx_desc",minTempOfDay.getWx_desc() );
			
			model.addAttribute("temperature", minTempOfDay.getTemp_f());
		}catch(Exception e) {
			LOGGER.info("Exception Occured - 500 " + e);
		}
			return "weatherDetails";
	}
}
