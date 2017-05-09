package za.co.absa.pangea.ops.service.auth;

import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Profile({"ci","default","!keycloak"})
public class PingController {

	@RequestMapping(path = "/pangea", method = RequestMethod.GET,produces= MediaType.TEXT_PLAIN_VALUE)
	public String ping() {
		return "success";
	}
}
