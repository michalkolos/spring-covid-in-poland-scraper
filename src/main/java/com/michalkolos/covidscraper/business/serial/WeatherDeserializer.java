package com.michalkolos.covidscraper.business.serial;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.michalkolos.covidscraper.data.entity.WeatherDataPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.TimeZone;


public class WeatherDeserializer extends StdDeserializer<WeatherDataPoint> {
	private static final Logger log = LoggerFactory.getLogger(WeatherDeserializer.class);


	public WeatherDeserializer(){
		this(null);
	}
	protected WeatherDeserializer(Class<?> vc) {
		super(vc);
	}


	@Override
	public WeatherDataPoint deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {

		JsonNode jsonNode = deserializationContext.readTree(jsonParser);
		WeatherDataPoint weather = new WeatherDataPoint();

		weather.setSourceMark(jsonNode.get("weather").get(0).get("id").asInt());
		weather.setMain(jsonNode.get("weather").get(0).get("main").asText());
		weather.setDescription(jsonNode.get("weather").get(0).get("description").asText());
		weather.setTemperature(jsonNode.get("main").get("temp").asDouble());
		weather.setPerceivedTemperature(jsonNode.get("main").get("feels_like").asDouble());
		weather.setPressure(jsonNode.get("main").get("pressure").asInt());
		weather.setHumidity(jsonNode.get("main").get("humidity").asInt());
		weather.setWindSpeed(jsonNode.get("wind").get("speed").asDouble());
		weather.setClouds(jsonNode.get("clouds").get("all").asInt());

		LocalDateTime gatheredTime =
				LocalDateTime.ofInstant(Instant.ofEpochSecond(jsonNode.get("dt").asLong()),
						TimeZone.getDefault().toZoneId());

		weather.setGatheredTime(gatheredTime);


		return weather;
	}
}
