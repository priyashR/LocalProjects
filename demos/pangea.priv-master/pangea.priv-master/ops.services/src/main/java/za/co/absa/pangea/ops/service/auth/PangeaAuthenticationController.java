package za.co.absa.pangea.ops.service.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import za.co.absa.pangea.ops.auth.user.PangeaUserDetails;

/**
 * @author hannes
 * @since 06/02/2016
 */
public class PangeaAuthenticationController {
	
	private static final String template = "Hello, %s! Your id is: %s";
	
	@Autowired
	private UserDetailsService userdetailsService;
	
	@RequestMapping(path = "/pangea", method = RequestMethod.GET,produces= MediaType.TEXT_PLAIN_VALUE)
	public @ResponseBody String sayHello(@RequestParam(value = "name", required = false, defaultValue = "Stranger") String name) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		PangeaUserDetails userDetails = (PangeaUserDetails) userdetailsService.loadUserByUsername(auth.getName());
	    return String.format(template, name,userDetails.getGivenName());
	}

}
