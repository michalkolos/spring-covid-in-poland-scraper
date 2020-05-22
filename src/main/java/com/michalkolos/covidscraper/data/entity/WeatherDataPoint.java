package com.michalkolos.covidscraper.data.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "WEATHER")
public class WeatherDataPoint implements Comparable {

	@JsonIgnore
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="WEATHER_ID")
	private long id;

	@Column(name = "SOURCE_MARK")
	private int sourceMark;

	@Column(name = "MAIN")
	private String main;

	@Column(name = "DESCRIPTION")
	private String description;

	@Column(name = "TEMPERATURE")
	private double temperature;

	@Column(name = "PERCEIVED_TEMPERATURE")
	private double perceivedTemperature;

	@Column(name = "PRESSURE")
	private int pressure;

	@Column(name = "HUMIDITY")
	private int humidity;

	@Column(name = "WIND_DEG")
	private int windDeg;

	@Column(name = "WIND_SPEED")
	private double windSpeed;

	@Column(name = "CLOUDS")
	private int clouds;

	@Column(name = "GATHERED_TIME")
	private LocalDateTime gatheredTime;


	@JsonIgnore
	@ManyToOne
	@JoinColumn
	private Voivo voivo;



	@Override
	public int compareTo(Object o) {
		LocalDateTime compDate = ((WeatherDataPoint)o).getGatheredTime();

		return this.gatheredTime.compareTo(compDate);
	}




	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getSourceMark() {
		return sourceMark;
	}

	public void setSourceMark(int sourceMark) {
		this.sourceMark = sourceMark;
	}

	public String getMain() {
		return main;
	}

	public void setMain(String main) {
		this.main = main;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public double getTemperature() {
		return temperature;
	}

	public void setTemperature(double temperature) {
		this.temperature = temperature;
	}

	public double getPerceivedTemperature() {
		return perceivedTemperature;
	}

	public void setPerceivedTemperature(double perceivedTemperature) {
		this.perceivedTemperature = perceivedTemperature;
	}

	public int getPressure() {
		return pressure;
	}

	public void setPressure(int pressure) {
		this.pressure = pressure;
	}

	public int getHumidity() {
		return humidity;
	}

	public void setHumidity(int humidity) {
		this.humidity = humidity;
	}

	public int getWindDeg() {
		return windDeg;
	}

	public void setWindDeg(int windDeg) {
		this.windDeg = windDeg;
	}

	public double getWindSpeed() {
		return windSpeed;
	}

	public void setWindSpeed(double windSpeed) {
		this.windSpeed = windSpeed;
	}

	public int getClouds() {
		return clouds;
	}

	public void setClouds(int clouds) {
		this.clouds = clouds;
	}

	public LocalDateTime getGatheredTime() {
		return gatheredTime;
	}

	public void setGatheredTime(LocalDateTime gatheredTime) {
		this.gatheredTime = gatheredTime;
	}



	public Voivo getVoivo() {
		return voivo;
	}

	public void setVoivo(Voivo voivo) {
		this.voivo = voivo;
	}


}
