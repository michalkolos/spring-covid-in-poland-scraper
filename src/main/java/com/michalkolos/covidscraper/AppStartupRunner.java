package com.michalkolos.covidscraper;

import com.michalkolos.covidscraper.virus.MszScraperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class AppStartupRunner implements ApplicationRunner {

	MszScraperService mszScraperService;

	@Autowired
	public AppStartupRunner(MszScraperService mszScraperService){
		this.mszScraperService = mszScraperService;
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {

		mszScraperService.collectData();
	}
}
