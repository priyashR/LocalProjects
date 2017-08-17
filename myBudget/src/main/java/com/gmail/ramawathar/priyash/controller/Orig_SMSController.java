package com.gmail.ramawathar.priyash.controller;
import java.net.URI;
import java.util.ArrayList;

import javax.inject.Inject;
import javax.validation.Valid;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.gmail.ramawathar.priyash.businessLogic.ProcessSms;
import com.gmail.ramawathar.priyash.domain.Bgt_notifications;
import com.gmail.ramawathar.priyash.domain.Bgt_trxns;
import com.gmail.ramawathar.priyash.domain.Orig_SMS;
import com.gmail.ramawathar.priyash.exception.ResourceNotFoundException;
import com.gmail.ramawathar.priyash.repository.Bgt_notificationsRepository;
import com.gmail.ramawathar.priyash.repository.Bgt_trxnsRepository;
import com.gmail.ramawathar.priyash.repository.Bgt_user_third_partyRepository;
import com.gmail.ramawathar.priyash.repository.Orig_SMSRepository;

@RestController
public class Orig_SMSController {
	
	@Inject
	private Orig_SMSRepository orig_SMSRepository;
	@Inject
	private Bgt_trxnsRepository bgt_trxnsRepository;
	@Inject
	private Bgt_notificationsRepository bgt_notificationsRepository;
	@Inject
	private Bgt_user_third_partyRepository bgt_user_third_partyRepository;
	
	protected void verifySMS(Long smsId) throws ResourceNotFoundException {
		Orig_SMS sms = orig_SMSRepository.findOne(smsId);
        if(sms == null) {
                throw new ResourceNotFoundException("Sms with id " + smsId + " not found");
        }
	}	
	
	@RequestMapping(value="/allSms", method=RequestMethod.GET)
	public ResponseEntity<Iterable<Orig_SMS>> getAllSMSs() {
	        return new ResponseEntity<>(orig_SMSRepository.findAll(), HttpStatus.OK);
	}	
	
	@RequestMapping(value="/allTrxns", method=RequestMethod.GET)
	public ResponseEntity<Iterable<Bgt_trxns>> getAlltrxns() {
	        return new ResponseEntity<>(bgt_trxnsRepository.findAll(), HttpStatus.OK);
	}
	
	@RequestMapping(value="/allNotifications", method=RequestMethod.GET)
	public ResponseEntity<Iterable<Bgt_notifications>> getAllnotifications() {
	        return new ResponseEntity<>(bgt_notificationsRepository.findAll(), HttpStatus.OK);
	}
	
	@RequestMapping(value="/orig_SMS", method=RequestMethod.POST)
	public ResponseEntity<?> createSMS(@Valid @RequestBody Orig_SMS o_SMS) {

			//o_SMS = orig_SMSRepository.save(o_SMS);
			Bgt_notifications n = new Bgt_notifications();
	        HttpHeaders responseHeaders = new HttpHeaders();	
	        Orig_SMS s = orig_SMSRepository.save(o_SMS);
			ProcessSms p;
			ArrayList<Bgt_trxns> t;

			//init notifications

			n.setUser_email(o_SMS.getUser_email());
			n.setNotification_orig_sms(o_SMS.getMessage());
			
			n.setNotification_type("INFO");
			n.setNotification_desc("Original message processed successfully");
			n.setNotification_action("NONE");
			n.setNotification_status("NEW");
			
			Bgt_trxns lastTrxn = new Bgt_trxns();
			try {
	        // Set the location header for the newly created resource
		        URI newPollUri = ServletUriComponentsBuilder
		                                              .fromCurrentRequest()
		                                              .path("/{id}")
		                                              .buildAndExpand(o_SMS.getSms_id())
		                                              .toUri();
		        responseHeaders.setLocation(newPollUri);	
				p = new ProcessSms(s,bgt_user_third_partyRepository);
				t = p.process(n);
				if (!(n.getNotification_type().equalsIgnoreCase("ERROR"))) {
					int i = 0;
					while (i < t.size()) {
						//System.out.println(crunchifyList.get(i));
						lastTrxn = bgt_trxnsRepository.save(t.get(i));
						i++;
					}
					
				}
				n.setTrxn_id(lastTrxn.getTrxnId());
				n = bgt_notificationsRepository.save(n);
			}
			catch (Exception e){
				n.setNotification_type("ERROR");
				n.setNotification_desc("Critical issue: "+e.getMessage());
				n.setNotification_action("INVESTIGATE");
				n = bgt_notificationsRepository.save(n);
				throw e;
			}
	        return new ResponseEntity<>(t, responseHeaders, HttpStatus.CREATED);
	}	
	
	
	@RequestMapping(value="/orig_SMS/{smsId}", method=RequestMethod.PUT)
	public ResponseEntity<?> updateSMS(@RequestBody Orig_SMS o_SMS, @PathVariable Long smsId) {
		verifySMS(smsId);
		Orig_SMS p = orig_SMSRepository.save(o_SMS);
	        return new ResponseEntity<>(HttpStatus.OK);
	}

	@RequestMapping(value="/orig_SMS/{smsId}", method=RequestMethod.DELETE)
	public ResponseEntity<?> deleteSMS(@PathVariable Long smsId) {
		verifySMS(smsId);
		orig_SMSRepository.delete(smsId);
	        return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@RequestMapping(value="/orig_SMS/{smsId}", method=RequestMethod.GET)
	public ResponseEntity<?> getSMS(@PathVariable Long smsId) {
		verifySMS(smsId);
		Orig_SMS s = orig_SMSRepository.findOne(smsId);
        return new ResponseEntity<> (s, HttpStatus.OK);
	}	
	

}
