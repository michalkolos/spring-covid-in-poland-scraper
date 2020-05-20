package com.michalkolos.covidscraper.web;

import com.michalkolos.covidscraper.data.entity.Voivo;
import com.michalkolos.covidscraper.service.DataProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
public class RestServerController {


	DataProviderService dataProviderService;

	@Autowired
	public RestServerController(DataProviderService dataProviderService) {
		this.dataProviderService = dataProviderService;
	}

	@GetMapping(
			path = "/test")
	public String process(){

		return "Request Received!";
	}

	@GetMapping(
			path = "/data",
			params = {"rows"})
	public List<Voivo> process(@RequestParam("rows") int rows){

		if(rows < 1){
			return new ArrayList<Voivo>();
		}

		return dataProviderService.getDataRows(rows);
	}

}
