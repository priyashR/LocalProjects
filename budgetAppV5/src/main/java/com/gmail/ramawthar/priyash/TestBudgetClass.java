package com.gmail.ramawthar.priyash;

import java.sql.Timestamp;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import com.gmail.ramawthar.priyash.model.TRXN_original_SMS;
import com.gmail.ramawthar.priyash.service.OrgSMSService;

public class TestBudgetClass {
	public static void main(String[] args) {
		
		//Create Spring application context
		ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("classpath:/config.xml");
		
		//Get service from context. (service's dependency (ProductDAO) is autowired in ProductService)
		OrgSMSService smsService = ctx.getBean(OrgSMSService.class);
	
		smsService.add(new TRXN_original_SMS("TEST SMS message", "priyash.ramawthar@gmail.com", (new Timestamp(System.currentTimeMillis())).toString()) );
		System.out.println("listAll: " + smsService.listAll());
		ctx.close();
		
	}
}
