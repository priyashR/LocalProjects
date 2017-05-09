/*********************************************
 * Copyright 2016 Absa ©
 * 29 Jun 2016
 * @author Eon van Tonder
 * @auther Nakedi Mabusela
 * @author Abigail Munzhelele
 * @author Jannie Pieterse
 * 
 * All rights reserved
 *********************************************/
package za.co.absa.pangea.ops.auth.user;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * The Class PangeaUserDetailsService.
 */
public abstract class PangeaUserDetailsService implements UserDetailsService{

	protected Map<String, PangeaUserDetails> userdetails = new HashMap<>();

	/**
	 * Instantiates a new pangea user details service.
	 */
	public PangeaUserDetailsService() {
		super();
	}
	
	/**
	 * Checks if is user in role.
	 *
	 * @param role
	 *            the role
	 * @return true, if is user in role
	 */
	public boolean isUserInRole(String role){
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		PangeaUserDetails userDetails = (PangeaUserDetails)loadUserByUsername(auth.getName());
		if (userDetails != null){
			for (GrantedAuthority grantedAuthority : userDetails.getAuthorities()) {
				if (role.equals(grantedAuthority.getAuthority())) {
					return true;
				}
			}
		}
	
		return false;
	}
	
}