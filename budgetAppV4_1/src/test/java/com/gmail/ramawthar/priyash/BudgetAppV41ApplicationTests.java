package com.gmail.ramawthar.priyash;

import com.gmail.ramawthar.priyash.model.Game;
import com.gmail.ramawthar.priyash.repo.GameRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import static org.junit.Assert.assertEquals;

import com.gmail.ramawthar.priyash.BudgetAppV41Application;
import com.gmail.ramawthar.priyash.logic.TestClass;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = BudgetAppV41Application.class)
public class BudgetAppV41ApplicationTests {

	 @Autowired
	 GameRepository gameRepository;
	 
	 @Before
	 public void setUp() throws Exception {
	  gameRepository.deleteAll();
	  Game pandemic = new Game("Pandemic", "Co-op game for  ");
	  Game werewolf = new Game("Werewolf", "You must find  ");
	  Game camelUp = new Game("Camel Up", "A high stak ");
	  gameRepository.save(pandemic);
	  gameRepository.save(werewolf);
	  gameRepository.save(camelUp);
	 }	 
	 
	 @Test
	 public void testLoadGames() {
/*
		  gameRepository.deleteAll();
		  Game pandemic = new Game("Pandemic", "Co-op game  ");
		  Game werewolf = new Game("Werewolf", "You must f ");
		  Game camelUp = new Game("Camel Up", "A high sta ");
		  gameRepository.save(pandemic);
		  gameRepository.save(werewolf);
		  gameRepository.save(camelUp);
	     List<Game> games = (ArrayList<Game>) gameRepository.findAll();
	     assertEquals("Did not get all games", 3, games.size());
	     */
		 System.out.println("lets go...");
		 //TestClass t = new TestClass();
		 //t.testLoadGames();
		 
		 List<Game> games = (ArrayList<Game>) gameRepository.findAll();
		    
		    Iterator<Game> iterator = games.iterator();
		    
	        while (iterator.hasNext()) { 
	            System.out.println(iterator.next().getName()); 
	        } 
	 }


}
