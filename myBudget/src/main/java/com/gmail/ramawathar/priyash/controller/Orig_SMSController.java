package com.gmail.ramawathar.priyash.controller;
import java.net.URI;

import javax.inject.Inject;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.gmail.ramawathar.priyash.domain.Orig_SMS;
import com.gmail.ramawathar.priyash.repository.Orig_SMSRepository;

@RestController
public class Orig_SMSController {
	
	@Inject
	private Orig_SMSRepository orig_SMSRepository;
	
	@RequestMapping(value="/allSms", method=RequestMethod.GET)
	public ResponseEntity<Iterable<Orig_SMS>> getAllPolls() {
	        Iterable<Orig_SMS> allPolls = orig_SMSRepository.findAll();
	        return new ResponseEntity<>(orig_SMSRepository.findAll(), HttpStatus.OK);
	}	
	
	@RequestMapping(value="/orig_SMS", method=RequestMethod.POST)
	public ResponseEntity<?> createPoll(@RequestBody Orig_SMS o_SMS) {

		o_SMS = orig_SMSRepository.save(o_SMS);

	        // Set the location header for the newly created resource
	        HttpHeaders responseHeaders = new HttpHeaders();
	        URI newPollUri = ServletUriComponentsBuilder
	                                              .fromCurrentRequest()
	                                              .path("/{id}")
	                                              .buildAndExpand(o_SMS.getSms_id())
	                                              .toUri();
	        responseHeaders.setLocation(newPollUri);

	        return new ResponseEntity<>(null, responseHeaders, HttpStatus.CREATED);
	}	
	
	
	@RequestMapping(value="/orig_SMS/{smsId}", method=RequestMethod.PUT)
	public ResponseEntity<?> updateSMS(@RequestBody Orig_SMS o_SMS, @PathVariable Long smsId) {
	        // Save the entity
		Orig_SMS p = orig_SMSRepository.save(o_SMS);
	        return new ResponseEntity<>(HttpStatus.OK);
	}

	@RequestMapping(value="/orig_SMS/{smsId}", method=RequestMethod.DELETE)
	public ResponseEntity<?> deleteSMS(@PathVariable Long smsId) {
		orig_SMSRepository.delete(smsId);
	        return new ResponseEntity<>(HttpStatus.OK);
	}
	

}
