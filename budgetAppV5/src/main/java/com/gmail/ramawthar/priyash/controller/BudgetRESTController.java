package com.gmail.ramawthar.priyash.controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.gmail.ramawthar.priyash.model.Greeting;


@RestController
@EnableWebMvc
public class BudgetRESTController {
    @RequestMapping("/test")
    public @ResponseBody Greeting  getAllEmployees() 
    {
        
        //com.gmail.ramawthar.priyash.TestInsertSMS t = new com.gmail.ramawthar.priyash.TestInsertSMS();
        //t.testInsert("TEST SMS message", "priyash.ramawthar@gmail.com", (new Timestamp(System.currentTimeMillis())).toString());

        Greeting g = new Greeting(1,"hello");
        return g;
    }
}
 
