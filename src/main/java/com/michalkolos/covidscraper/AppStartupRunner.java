package com.michalkolos.covidscraper;

import com.michalkolos.covidscraper.service.VirusScraperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class AppStartupRunner implements ApplicationRunner {

	VirusScraperService virusScraperService;

	@Autowired
	public AppStartupRunner(VirusScraperService virusScraperService){
		this.virusScraperService = virusScraperService;
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {

		virusScraperService.collectData();
	}
}
