package com.michalkolos.covidscraper.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
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
public class WeatherGatherService {

	private final static String OPENWEATHERMAP_API_KEY = "216353d6d8f732836dc5f5cd45404903";
	public static final int RETRY_COUNT = 5;
	public static final String OPENWEATHERMAP_API_URL_STRING = "http://api.openweathermap.org/data/2.5/weather?lat=%s&lon=%s&units=metric&appid=%s";
	private static final Map<String, String[][]> VOIVO_COORDS = new HashMap<>();

	static {
//							                        lon         lat
		VOIVO_COORDS.put("t00", new String[][]{{"19.47997", "52.06898"}, {"Cała Polska"}});
		VOIVO_COORDS.put("t02", new String[][]{{"16.41069", "51.08950"}, {"dolnośląskie"}});
		VOIVO_COORDS.put("t04", new String[][]{{"18.48822", "53.07270"}, {"kujawsko-pomorskie"}});
		VOIVO_COORDS.put("t06", new String[][]{{"22.90027", "51.22072"}, {"lubelskie"}});
		VOIVO_COORDS.put("t08", new String[][]{{"15.34275", "52.19617"}, {"lubuskie"}});
		VOIVO_COORDS.put("t10", new String[][]{{"19.41760", "51.60487"}, {"łódzkie"}});
		VOIVO_COORDS.put("t12", new String[][]{{"20.26933", "49.85895"}, {"małopolskie"}});
		VOIVO_COORDS.put("t14", new String[][]{{"21.09645", "52.34576"}, {"mazowieckie"}});
		VOIVO_COORDS.put("t16", new String[][]{{"17.89988", "50.64711"}, {"opolskie"}});
		VOIVO_COORDS.put("t18", new String[][]{{"22.16912", "49.95367"}, {"podkarpackie"}});
		VOIVO_COORDS.put("t20", new String[][]{{"22.92931", "53.26452"}, {"podlaskie"}});
		VOIVO_COORDS.put("t22", new String[][]{{"17.98619", "54.15424"}, {"pomorskie"}});
		VOIVO_COORDS.put("t24", new String[][]{{"18.99410", "50.33108"}, {"śląskie"}});
		VOIVO_COORDS.put("t26", new String[][]{{"20.76909", "50.76339"}, {"świętokrzyskie"}});
		VOIVO_COORDS.put("t28", new String[][]{{"20.82493", "53.85721"}, {"warmińsko-mazurskie"}});
		VOIVO_COORDS.put("t30", new String[][]{{"17.24310", "52.33078"}, {"wielkopolskie"}});
		VOIVO_COORDS.put("t32", new String[][]{{"15.54329", "53.58476"}, {"zachodniopomorskie"}});
	}

	private static final Logger log = LoggerFactory.getLogger(WeatherGatherService.class);


	public Map<String, WeatherDataPoint> collectData() {
		Map<String, WeatherDataPoint> weatherMap = new HashMap<>();

		VOIVO_COORDS.forEach((voivoCode, coords)->{

			int retries = 0;
			WeatherDataPoint weatherDataPoint = null;
			while(weatherDataPoint == null && retries < RETRY_COUNT) {
				retries++;
				try {

					String weatherString = downloadWeather(coords[0][0], coords[0][1]);
					weatherDataPoint = deserializeWeather(weatherString);

				} catch (IOException e) {
					log.warn("({}) failed to get weather data for: {} ({})",
							retries,
							coords[coords.length - 1][0],
							e.getMessage());
				}
			}
			weatherMap.put(voivoCode, weatherDataPoint);
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
			if(VOIVO_COORDS.containsKey(voivoCode)){
				String[][] coords = VOIVO_COORDS.get(voivoCode);
				sb.append(String.format("%20s", coords[coords.length - 1][0]));
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
