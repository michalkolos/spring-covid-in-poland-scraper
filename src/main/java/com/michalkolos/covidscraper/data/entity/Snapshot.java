package com.michalkolos.covidscraper.data.entity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "SNAPSHOT")
public class Snapshot {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="SNAPSHOT_ID")
	private long id;

	@Column(name = "DATE_TIME")
	LocalDateTime dateTime;


//	@OneToMany(mappedBy = "snapshot", cascade = {CascadeType.ALL})
	@OneToMany(mappedBy = "snapshot")
	private Set<VirusDataPoint> virusDataPoints = new HashSet<>();






	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public LocalDateTime getDateTime() {
		return dateTime;
	}

	public void setDateTime(LocalDateTime dateTime) {
		this.dateTime = dateTime;
	}

	public Set<VirusDataPoint> getVirusDataPoints() {
		return virusDataPoints;
	}

	public void setVirusDataPoints(Set<VirusDataPoint> virusDataPoints) {
		this.virusDataPoints = virusDataPoints;
	}
}
