package com.gmail.ramawthar.priyash;

import java.sql.Timestamp;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import com.gmail.ramawthar.priyash.model.TRXN_original_SMS;
import com.gmail.ramawthar.priyash.service.OrgSMSService;

public class TestInsertSMS {

	public void testInsert(String msg, String user, String timestamp){
		//Create Spring application context
		ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("classpath:/config.xml");
		
		//Get service from context. (service's dependency (ProductDAO) is autowired in ProductService)
		OrgSMSService smsService = ctx.getBean(OrgSMSService.class);
	
		smsService.add(new TRXN_original_SMS(msg, user, timestamp));
		System.out.println("listAll: " + smsService.listAll());
		ctx.close();
	}
}
