package com.michalkolos.covidscraper.data.entity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Voivo {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="VOIVO_ID")
	private long id;

	@Column(name = "SOURCE_ID")
	private int sourceId;

	@Column(name = "NAME")
	private String name;

	@OneToMany(mappedBy = "voivo", cascade = CascadeType.ALL)
	private Set<Snapshot> snapshots = new HashSet<>();

	@OneToMany(mappedBy = "voivo", cascade = CascadeType.ALL)
	private Set<VirusDataPoint> virusDataPoints = new HashSet<>();
}
