package com.michalkolos.covidscraper.scheduled;

import com.michalkolos.covidscraper.data.entity.VirusDataPoint;
import com.michalkolos.covidscraper.data.entity.Voivo;
import com.michalkolos.covidscraper.data.entity.WeatherDataPoint;
import com.michalkolos.covidscraper.business.service.DataPersistenceService;
import com.michalkolos.covidscraper.business.service.VirusDataService;
import com.michalkolos.covidscraper.business.service.WeatherDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class ScheduledTasks {

	private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);

	VirusDataService virusDataService;
	WeatherDataService weatherDataService;
	DataPersistenceService dataPersistenceService;

	@Autowired
	public ScheduledTasks(
			VirusDataService virusDataService,
			WeatherDataService weatherDataService,
			DataPersistenceService dataPersistenceService) {

		this.virusDataService = virusDataService;
		this.weatherDataService = weatherDataService;
		this.dataPersistenceService = dataPersistenceService;
	}


//	@Scheduled(fixedRate = 1000)
//	public void scheduledTest(){
//		log.info("Test scheduled task fired!");
//	}

	@Scheduled(fixedRate = 240 * 60000) // Check virus data every 4hrs
	public void logCovid(){

		log.info("Logging virus data");

		List<Voivo> voivos = virusDataService.collectVoivos();

		Map<String, VirusDataPoint> virusMap = virusDataService.collectData();
		virusDataService.printToLogAllVirusData(virusMap);

		dataPersistenceService.syncVoivos(voivos);
		dataPersistenceService.saveVirusDataPoints(virusMap);
	}


	@Scheduled(fixedRate = 60 * 60000) // Check weather data every hour
	public void logWeather(){

		log.info("Logging weather data");

		Map<String, WeatherDataPoint> weatherMap = weatherDataService.collectData();
		weatherDataService.printToLogAllWeatherData(weatherMap);

		dataPersistenceService.saveWeatherDataPoints(weatherMap);
	}

}
