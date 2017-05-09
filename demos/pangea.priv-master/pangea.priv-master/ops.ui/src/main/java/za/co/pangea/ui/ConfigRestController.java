package za.co.pangea.ui;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ConfigRestController {

	@Autowired
	private Config config;
	
	@RequestMapping(value="/config", method= RequestMethod.GET, produces= MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Config config() {
		return config;
	}

}
