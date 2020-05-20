package com.michalkolos.covidscraper.scheduled;

import com.michalkolos.covidscraper.data.entity.VirusDataPoint;
import com.michalkolos.covidscraper.data.entity.Voivo;
import com.michalkolos.covidscraper.data.entity.WeatherDataPoint;
import com.michalkolos.covidscraper.service.DataPersistenceService;
import com.michalkolos.covidscraper.service.VirusScraperService;
import com.michalkolos.covidscraper.service.WeatherGatherService;
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

	VirusScraperService virusScraperService;
	WeatherGatherService weatherGatherService;
	DataPersistenceService dataPersistenceService;

	@Autowired
	public ScheduledTasks(
			VirusScraperService virusScraperService,
			WeatherGatherService weatherGatherService,
			DataPersistenceService dataPersistenceService) {

		this.virusScraperService = virusScraperService;
		this.weatherGatherService = weatherGatherService;
		this.dataPersistenceService = dataPersistenceService;
	}

	@Scheduled(fixedRate = 360 * 60000) // Check virus data every 6hrs
	public void logCovid(){

		log.info("Logging virus data");

		List<Voivo> voivos = virusScraperService.collectVoivos();

		Map<String, VirusDataPoint> virusMap = virusScraperService.collectData();
		virusScraperService.printToLogAllVirusData(virusMap);

		dataPersistenceService.syncVoivos(voivos);
		dataPersistenceService.saveVirusDataPoints(virusMap);
	}


	@Scheduled(fixedRate = 60 * 60000) // Check weather data every hour
	public void logWeather(){

		log.info("Logging weather data");

		Map<String, WeatherDataPoint> weatherMap = weatherGatherService.collectData();
		weatherGatherService.printToLogAllWeatherData(weatherMap);

		dataPersistenceService.saveWeatherDataPoints(weatherMap);
	}

}
