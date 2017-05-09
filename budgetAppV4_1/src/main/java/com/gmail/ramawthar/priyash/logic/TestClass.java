package com.gmail.ramawthar.priyash.logic;

//import com.gmail.ramawthar.priyash.model.Game;
import com.gmail.ramawthar.priyash.repo.GameRepository;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.stereotype.Controller;

import com.gmail.ramawthar.priyash.BudgetAppV41Application;
import com.gmail.ramawthar.priyash.model.Game;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Controller
public class TestClass {

	 @Autowired
	 GameRepository gameRepository;
	 
	 public void testLoadGames() {
	    List<Game> games = (ArrayList<Game>) gameRepository.findAll();
	    
	    Iterator<Game> iterator = games.iterator();
	    
        while (iterator.hasNext()) { 
            System.out.println(iterator.next().getName()); 
        } 
	 }
	 
	 /*public static void main(String[] args){
		 
		 System.out.println("lets go...");
		 TestClass t = new TestClass();
		 t.testLoadGames();
	 }*/
	 

}
