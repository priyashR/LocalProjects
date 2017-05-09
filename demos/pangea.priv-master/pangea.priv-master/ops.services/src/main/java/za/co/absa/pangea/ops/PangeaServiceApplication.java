package za.co.absa.pangea.ops;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author hannes
 * @since 03/02/2016
 */

@SpringBootApplication
@ComponentScan(basePackages = "za.co.absa.pangea.ops")
public class PangeaServiceApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(PangeaServiceApplication.class, args);
	}

}
