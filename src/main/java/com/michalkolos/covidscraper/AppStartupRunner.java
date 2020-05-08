package com.michalkolos.covidscraper;

import com.michalkolos.covidscraper.data.entity.WeatherDataPoint;
import com.michalkolos.covidscraper.service.VirusScraperService;
import com.michalkolos.covidscraper.service.WeatherGatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class AppStartupRunner implements ApplicationRunner {

	VirusScraperService virusScraperService;
	WeatherGatherService weatherGatherService;

	@Autowired
	public AppStartupRunner(VirusScraperService virusScraperService, WeatherGatherService weatherGatherService) {
		this.virusScraperService = virusScraperService;
		this.weatherGatherService = weatherGatherService;
	}




	@Override
	public void run(ApplicationArguments args) throws Exception {

		virusScraperService.collectData();

		Map<String, WeatherDataPoint> weatherMap = weatherGatherService.getAllWeather();
		weatherGatherService.printToLogAllWeatherData(weatherMap);
	}
}
