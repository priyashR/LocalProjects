package za.co.absa.pangea.ops.workflow.auth;

import za.co.absa.pangea.ops.auth.user.PangeaUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

@RestController
@Profile("keycloak")
public class PangeaAuthenticationControler {

	private static final String MESSAGE_TEMPLATE = "Hello, %s! Your id is: %s";
	
	@Autowired
	private UserDetailsService userdetailsService;

	@RequestMapping(path = "/pangea", method = RequestMethod.GET,produces= MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody String sayHello(@RequestParam(value = "name", required = false, defaultValue = "Stranger") String name) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		PangeaUserDetails userDetails = (PangeaUserDetails) userdetailsService.loadUserByUsername(auth.getName());
	    return String.format(MESSAGE_TEMPLATE, name,userDetails.getGivenName());
	}
	
}
