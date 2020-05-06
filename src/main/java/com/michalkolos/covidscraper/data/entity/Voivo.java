package com.michalkolos.covidscraper.data.entity;

import javax.persistence.*;
import java.util.HashSet;
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
	private Set<VirusDataPoint> virusDataPoints = new HashSet<>();








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



	public Set<VirusDataPoint> getVirusDataPoints() {
		return virusDataPoints;
	}

	public void setVirusDataPoints(Set<VirusDataPoint> virusDataPoints) {
		this.virusDataPoints = virusDataPoints;
	}
}