package com.example.demo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import starter.Temperatura;

@RestController
public class IndexController {
	@Autowired
	private Temperatura temperatura;
	
	private Counter counterGrados;
	private Counter counterFar;
	private final static Logger logger= LoggerFactory.getLogger(IndexController.class);
	
	public IndexController(MeterRegistry meterRegistry) {
		this.counterGrados=Counter.builder("Invocaciones mas usadas").description("Total de invocaciones para Grados").register(meterRegistry);
		this.counterFar=Counter.builder("Invocaciones mas usadas").description("Total de invocaciones para Fahrenheit").register(meterRegistry);
	}
	@GetMapping("/tipo/{temp}")
	public ResponseEntity<String> index(@PathVariable int temp) {
		logger.info("Mostrando un tipo de temperatura");
		if(temperatura.tipoTemp(temp)>temp) {
			counterFar.increment();
			return new ResponseEntity<String>(HttpStatus.OK).ok("Grados Celcius "+temperatura.tipoTemp(temp));
		}else{
			counterGrados.increment();
			return new ResponseEntity<String>(HttpStatus.OK).ok("Grados Fahrenheit "+temperatura.tipoTemp(temp));
		}
	}
	@Value("${some.value}")
	private String myValue;
	
	@GetMapping(path="/myValue")
	public String myValue() {
		return this.temperatura.mostrarTemperaturas(Integer.parseInt(myValue));
	}

}
