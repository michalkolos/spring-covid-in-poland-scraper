package com.michalkolos.covidscraper.business.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.michalkolos.covidscraper.business.VoivoSpecs;
import com.michalkolos.covidscraper.business.serial.WeatherDeserializer;
import com.michalkolos.covidscraper.data.entity.WeatherDataPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
public class WeatherDataService {

	private final static String OPENWEATHERMAP_API_KEY = "216353d6d8f732836dc5f5cd45404903";
	public static final int RETRY_COUNT = 5;
	public static final String OPENWEATHERMAP_API_URL_STRING = "http://api.openweathermap.org/data/2.5/weather?lat=%s&lon=%s&units=metric&appid=%s";


	private static final Logger log = LoggerFactory.getLogger(WeatherDataService.class);


	public Map<String, WeatherDataPoint> collectData() {
		Map<String, WeatherDataPoint> weatherMap = new HashMap<>();


		Set<String> voivoIds = VoivoSpecs.getKeys();

		voivoIds.forEach(id->{
			int retries = 0;
			WeatherDataPoint weatherDataPoint = null;
			while(weatherDataPoint == null && retries < RETRY_COUNT) {
				retries++;
				try {

					String weatherString = downloadWeather(VoivoSpecs.getLon(id,0), VoivoSpecs.getLat(id,0));
					weatherDataPoint = deserializeWeather(weatherString);

				} catch (IOException e) {
					log.warn("({}) failed to get weather data for: {} ({})",
							retries,
							VoivoSpecs.getName(id),
							e.getMessage());
				}
			}
			weatherMap.put(id, weatherDataPoint);
		});


		if(weatherMap.size() == 0){
			log.error("Error getting weather data.");
		}

		return weatherMap;
	}


	public void printToLogAllWeatherData(Map<String, WeatherDataPoint> weatherMap){
		StringBuilder sb = new StringBuilder();

		sb.append("Weather data for all regions:").append(System.lineSeparator());

		weatherMap.forEach((voivoCode, weatherDataPoint)->{
			if(VoivoSpecs.exists(voivoCode)){
				sb.append(String.format("%20s", VoivoSpecs.getName(voivoCode)));
			}else{
				sb.append("Unknown code: ");
				sb.append(String.format("%6s", voivoCode));
			}

			sb.append(" - ");

			if(weatherDataPoint != null) {
				sb.append(String.format("%10s", weatherDataPoint.getMain()));
				sb.append(" ");
				sb.append(String.format("%25s","(" + weatherDataPoint.getDescription()));
				sb.append("), t: ");
				sb.append(String.format("%5s", weatherDataPoint.getTemperature()));
				sb.append("), pt: ");
				sb.append(String.format("%5s", weatherDataPoint.getPerceivedTemperature()));
				sb.append(", ws: ");
				sb.append(String.format("%5s", weatherDataPoint.getWindSpeed()));
				sb.append(", cc: ");
				sb.append(String.format("%5s", weatherDataPoint.getClouds()));
				sb.append(", p: ");
				sb.append(String.format("%5s", weatherDataPoint.getPressure()));
				sb.append(", h: ");
				sb.append(String.format("%5s", weatherDataPoint.getHumidity()));
				sb.append(", time: ");
				sb.append(String.format("%10s", weatherDataPoint.getGatheredTime()));
			}else{
				sb.append(" No weather data");
			}
			sb.append(System.lineSeparator());


		});

		log.info(sb.toString());
	}


	private String downloadWeather(String lon, String lat) throws IOException {
		String apiUrlString = String.format(OPENWEATHERMAP_API_URL_STRING,
				lat, lon, OPENWEATHERMAP_API_KEY);

		URL apiUrl = new URL(apiUrlString);

		Scanner scanner = new Scanner(apiUrl.openStream(), StandardCharsets.UTF_8.toString());
		scanner.useDelimiter("\\A");

		return scanner.hasNext() ? scanner.next() : "";
	}



	private WeatherDataPoint deserializeWeather(String weatherString) throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		SimpleModule module = new SimpleModule();
		module.addDeserializer(WeatherDataPoint.class, new WeatherDeserializer());
		mapper.registerModule(module);
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

		return mapper.readValue(weatherString, WeatherDataPoint.class);
	}
}
