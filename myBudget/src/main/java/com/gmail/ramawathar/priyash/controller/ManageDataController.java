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

import com.gmail.ramawathar.priyash.businessLogic.CategoryParamPOJO;
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
	
	
	@RequestMapping(value="/assocNotificationToCat", method=RequestMethod.POST)
	public ResponseEntity<?> assocNotificationToCat(@Valid @RequestBody CategoryParamPOJO params) {

	        HttpHeaders responseHeaders = new HttpHeaders();	
	        ManageData md = new ManageData();
			
			params.setStatus("SUCCESS");
			params.setStatusDesc("");
			
			try {

				md.assocNotificationToCat(	bgt_notificationsRepository, 
											bgt_user_third_partyRepository, 
											bgt_categoriesRepository,
											bgt_trxnsRepository,
											params);
				
			}
			catch (Exception e){
				params.setStatus("ERROR");
				params.setStatusDesc("Critical issue: "+e.getMessage());
				throw e;
			}
	        return new ResponseEntity<>(params, responseHeaders, HttpStatus.CREATED);
	}	
	
	@RequestMapping(value="/createUpdateCat", method=RequestMethod.POST)
	public ResponseEntity<?> createUpdateCat(@Valid @RequestBody CategoryParamPOJO params) {

	        HttpHeaders responseHeaders = new HttpHeaders();	
	        ManageData md = new ManageData();
			
			params.setStatus("SUCCESS");
			params.setStatusDesc("");
			
			try {

				md.createUpdateCategory(	bgt_categoriesRepository,
											params);
				
			}
			catch (Exception e){
				params.setStatus("ERROR");
				params.setStatusDesc("Critical issue: "+e.getMessage());
				throw e;
			}
	        return new ResponseEntity<>(params, responseHeaders, HttpStatus.CREATED);
	}	
	
	//batches:
	
	@RequestMapping(value="/assocNotificationToCats", method=RequestMethod.POST)
	public ResponseEntity<?> assocNotificationToCats(@Valid @RequestBody ArrayList<CategoryParamPOJO> params) {

	        HttpHeaders responseHeaders = new HttpHeaders();	
	        ManageData md = new ManageData();
	        CategoryParamPOJO param = new CategoryParamPOJO();
	        int i = 0;
	        while (i < params.size()) {
	        	param = params.get(i);
	        	i++;
	        	md = new ManageData();
	        	
				param.setStatus("SUCCESS");
				param.setStatusDesc("");
				
				try {
	
					md.assocNotificationToCat(	bgt_notificationsRepository, 
												bgt_user_third_partyRepository, 
												bgt_categoriesRepository,
												bgt_trxnsRepository,
												param);
					
				}
				catch (Exception e){
					param.setStatus("ERROR");
					param.setStatusDesc("Critical issue: "+e.getMessage());
					params.get(i-1).setStatus("ERROR");
					params.get(i-1).setStatusDesc("Critical issue: "+e.getMessage());
					throw e;
				}
	        }
	        return new ResponseEntity<>(params, responseHeaders, HttpStatus.CREATED);
	}	
	
	@RequestMapping(value="/createUpdateCats", method=RequestMethod.POST)
	public ResponseEntity<?> createUpdateCats(@Valid @RequestBody ArrayList<CategoryParamPOJO> params) {

	        HttpHeaders responseHeaders = new HttpHeaders();	
	        ManageData md = new ManageData();

	        CategoryParamPOJO param = new CategoryParamPOJO();
	        int i = 0;
	        while (i < params.size()) {
	        	param = params.get(i);
	        	i++;
	        	md = new ManageData();
	        	
	        	param.setStatus("SUCCESS");
	        	param.setStatusDesc("");
				
				try {
	
					md.createUpdateCategory(	bgt_categoriesRepository,
												param);
					
				}
				catch (Exception e){
					param.setStatus("ERROR");
					param.setStatusDesc("Critical issue: "+e.getMessage());
					params.get(i-1).setStatus("ERROR");
					params.get(i-1).setStatusDesc("Critical issue: "+e.getMessage());
					throw e;
				}
	        }
	        return new ResponseEntity<>(params, responseHeaders, HttpStatus.CREATED);
	}	
}
