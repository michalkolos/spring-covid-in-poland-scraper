package com.michalkolos.covidscraper.data.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "VOIVO")
public class Voivo {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="VOIVO_ID")
	private long id;

	@Column(name = "SOURCE_MARK")
	private String sourceId;

	@Column(name = "NAME")
	private String name;


	@OneToMany(mappedBy = "voivo", cascade = {CascadeType.ALL})
	private List<VirusDataPoint> virusDataPoints = new ArrayList<>();


	@OneToMany(mappedBy = "voivo", cascade = {CascadeType.ALL})
	private List<WeatherDataPoint> weatherDataPoints = new ArrayList<>();





	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getSourceId() {
		return sourceId;
	}

	public void setSourceId(String sourceId) {
		this.sourceId = sourceId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}



	public List<VirusDataPoint> getVirusDataPoints() {
		return virusDataPoints;
	}

	public void setVirusDataPoints(List<VirusDataPoint> virusDataPoints) {
		this.virusDataPoints = virusDataPoints;
	}

	public List<WeatherDataPoint> getWeatherDataPoints() {
		return weatherDataPoints;
	}

	public void setWeatherDataPoints(List<WeatherDataPoint> weatherDataPoints) {
		this.weatherDataPoints = weatherDataPoints;
	}
}

