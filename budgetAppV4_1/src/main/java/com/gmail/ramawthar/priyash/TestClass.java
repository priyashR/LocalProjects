package com.gmail.ramawthar.priyash;

//import com.gmail.ramawthar.priyash.model.Game;
import com.gmail.ramawthar.priyash.repo.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
//import java.util.ArrayList;
//import java.util.List;

public class TestClass {

	 @Autowired
	 GameRepository gameRepository;
	 
	 public void testLoadGames() {
	    // List<Game> games = (ArrayList<Game>) gameRepository.findAll();
	 }
	/*public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("hi");
		TestClass t = new TestClass();
		
		t.testLoadGames();
		System.out.println("bye");
		
	}*/

}
