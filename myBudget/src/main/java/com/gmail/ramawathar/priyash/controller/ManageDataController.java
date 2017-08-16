package com.gmail.ramawathar.priyash.controller;

import java.net.URI;
import java.util.ArrayList;

import javax.inject.Inject;
import javax.validation.Valid;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.gmail.ramawathar.priyash.businessLogic.AssociationsHandler;
import com.gmail.ramawathar.priyash.businessLogic.ManageData;
import com.gmail.ramawathar.priyash.businessLogic.ProcessSms;
import com.gmail.ramawathar.priyash.domain.Bgt_notifications;
import com.gmail.ramawathar.priyash.domain.Bgt_trxns;
import com.gmail.ramawathar.priyash.domain.Orig_SMS;
import com.gmail.ramawathar.priyash.repository.Bgt_categoriesRepository;
import com.gmail.ramawathar.priyash.repository.Bgt_notificationsRepository;
import com.gmail.ramawathar.priyash.repository.Bgt_trxnsRepository;
import com.gmail.ramawathar.priyash.repository.Bgt_user_third_partyRepository;

@RestController
public class ManageDataController {
	
	@Inject
	private Bgt_trxnsRepository bgt_trxnsRepository;
	@Inject
	private Bgt_notificationsRepository bgt_notificationsRepository;
	@Inject
	private Bgt_user_third_partyRepository bgt_user_third_partyRepository;
	@Inject
	private Bgt_categoriesRepository bgt_categoriesRepository;
	
	
	@RequestMapping(value="/assocNotification", method=RequestMethod.POST)
	public ResponseEntity<?> createSMS(@Valid @RequestBody AssociationsHandler params) {

			//o_SMS = orig_SMSRepository.save(o_SMS);
			Bgt_notifications n = new Bgt_notifications();
	        HttpHeaders responseHeaders = new HttpHeaders();	
	        ManageData md = new ManageData();

	        //init notifications
			n.setUser_email("PRIYASH.RAMAWTHAR@GMAIL.COM");
			
			n.setNotification_type("INFO");
			n.setNotification_desc("Updating notification category");
			n.setNotification_action("NONE");
			n.setNotification_status("SUCCESS");
			
			try {

				md.assocNotificationToCat(bgt_notificationsRepository, bgt_user_third_partyRepository, bgt_categoriesRepository, params, n);
				
			}
			catch (Exception e){
				n.setNotification_type("ERROR");
				n.setNotification_desc("Critical issue: "+e.getMessage());
				n.setNotification_action("INVESTIGATE");
				// -> not required, just use the structure n = bgt_notificationsRepository.save(n);
				throw e;
			}
	        return new ResponseEntity<>(n, responseHeaders, HttpStatus.CREATED);
	}	
}
