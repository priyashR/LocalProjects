/*********************************************
 * Copyright 2016 Absa ©
 * 30 Jun 2016
 * @author Eon van Tonder
 * @auther Nakedi Mabusela
 * @author Abigail Munzhelele
 * @author Jannie Pieterse
 * 
 * All rights reserved
 *********************************************/
package za.co.absa.pangea.ops.workflow;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class App {

	protected App() {
		super();
	}

	/**
	 * The main method.
	 *
	 * @param args
	 *            the arguments
	 */
	public static void main(String[] args) {
		SpringApplication.run(App.class, args);
	}
}
