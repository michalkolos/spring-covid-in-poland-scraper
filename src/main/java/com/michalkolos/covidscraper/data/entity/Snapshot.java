package com.michalkolos.covidscraper.data.entity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Snapshot {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="SNAPSHOT_ID")
	private long id;

	@Column(name = "DATE_TIME")
	LocalDateTime dateTime;

	@ManyToOne
	@JoinColumn
	private Voivo voivo;

	@OneToMany(mappedBy = "snapshot", cascade = CascadeType.ALL)
	private Set<VirusDataPoint> virusDataPoints = new HashSet<>();
}
