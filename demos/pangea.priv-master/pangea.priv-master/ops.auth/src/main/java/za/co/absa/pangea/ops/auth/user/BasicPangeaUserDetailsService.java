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

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * The Class BasicPangeaUserDetailsService.
 */
public class BasicPangeaUserDetailsService extends PangeaUserDetailsService {

	/* (non-Javadoc)
	 * @see org.springframework.security.core.userdetails.UserDetailsService#loadUserByUsername(java.lang.String)
	 */
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		PangeaUserDetails userDetails = userdetails.get(username);
		
		if (userDetails == null) {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			if (auth.getPrincipal() instanceof UserDetails){
			User user = (User)auth.getPrincipal();
			
			userDetails = new PangeaUserDetails();
			userDetails.setPassword(user.getPassword());
			userDetails.setUserName(user.getUsername());
			userDetails.setAuthorities(user.getAuthorities());
			userDetails.setAccountNonExpired(user.isAccountNonExpired());
			userDetails.setAccountNonLocked(user.isAccountNonLocked());
			userDetails.setCredentialsNonExpired(user.isCredentialsNonExpired());
			userDetails.setEnabled(user.isEnabled());
			
			userDetails.setCountry("");
			userDetails.setGroup("");
			userDetails.setName(userDetails.getUserName());
			userDetails.setGivenName(userDetails.getUserName());
			userDetails.setFamilyName("");
			userDetails.setClients(new String[]{""});
			userDetails.setRealm("");
			userDetails.setPreferredUsername(userDetails.getUserName());
			userDetails.setEmail("");
			}

			
			
			
		}

		return userDetails;
	}
}
