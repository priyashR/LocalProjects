package com.gmail.ramawthar.priyash;

import java.sql.Timestamp;
import java.util.Arrays;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.dao.DataAccessException;

import com.gmail.ramawthar.priyash.model.*;
import com.gmail.ramawthar.priyash.dao.*;
import com.gmail.ramawthar.priyash.service.*;

public class GameTestClass {
	public static void main(String[] args) {
		
		//Create Spring application context
		ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("classpath:/config.xml");
		
		//Get service from context. (service's dependency (ProductDAO) is autowired in ProductService)
		GameService gameService = ctx.getBean(GameService.class);
		//OrgSMSService smsService = ctx.getBean(OrgSMSService.class);
		
		//Do some data operation
		
		gameService.add(new Game1("Test1", "Test description 1"));
		gameService.add(new Game1("Test2", "Test description 2"));
		
		System.out.println("listAll: " + gameService.listAll());
		
		//Test transaction rollback (duplicated key)
		/*
		try {
			gameService.addAll(Arrays.asList(new Product(3, "Book"), new Product(4, "Soap"), new Product(1, "Computer")));
		} catch (DataAccessException dataAccessException) {
		}*/
		
		//Test element list after rollback
		System.out.println("listAll: " + gameService.listAll());
		
		//smsService.add(new TRXN_original_SMS("TEST SMS message", "priyash.ramawthar@gmail.com", (new Timestamp(System.currentTimeMillis())).toString()) );
		//System.out.println("listAll: " + smsService.listAll());
		ctx.close();
		
	}
}