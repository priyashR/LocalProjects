package com.gmail.ramawthar.priyash.controller;
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

import com.gmail.ramawthar.priyash.domain.Bgt_categories;
import com.gmail.ramawthar.priyash.repository.Bgt_categoriesRepository;
import com.gmail.ramawthar.priyash.repository.Price_data_stageRepository;
import com.gmail.ramawthar.priyash.upload.InstrumentData;
import com.gmail.ramawthar.priyash.upload.ProcessedInstrumentData;
import com.gmail.ramawthar.priyash.upload.UploadData;

@RestController
public class AnalyzerController {
	
	@Inject
	private Bgt_categoriesRepository bgt_categoriesRepository;
	
	@Inject
	private Price_data_stageRepository price_data_stageRepository;
	
	/*protected void verifySMS(Long smsId) throws ResourceNotFoundException {
		Orig_SMS sms = orig_SMSRepository.findOne(smsId);
        if(sms == null) {
                throw new ResourceNotFoundException("Sms with id " + smsId + " not found");
        }
	}	*/
	
	@RequestMapping(value="/allCategories", method=RequestMethod.GET)
	public ResponseEntity<Iterable<Bgt_categories>> getAllSMSs() {
	        return new ResponseEntity<>(bgt_categoriesRepository.findAll(), HttpStatus.OK);
	}	
	
	@RequestMapping(value="/uploadData", method=RequestMethod.POST)
	public ResponseEntity<?> recieveUploadData(@Valid @RequestBody ArrayList<ProcessedInstrumentData> instrumentData) {
		
		ProcessedInstrumentData insData = new ProcessedInstrumentData();
		

        int i = 0;
        while (i < instrumentData.size()) {

        	insData = instrumentData.get(i);
        	i++;
        	UploadData uploader = new UploadData(insData);
        	uploader.uploadToDatabase(price_data_stageRepository);
        }
		
		
        return new ResponseEntity<>("Success", HttpStatus.ACCEPTED);
	}		
}
