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

import org.keycloak.adapters.RefreshableKeycloakSecurityContext;
import org.keycloak.representations.AccessToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import za.co.absa.pangea.ops.auth.translator.PangeaAuthenticationTranslator;

/**
 * The Class KeycloakPangeaUserDetailsService.
 */
public class KeycloakPangeaUserDetailsService extends PangeaUserDetailsService {

	/* (non-Javadoc)
	 * @see org.springframework.security.core.userdetails.UserDetailsService#loadUserByUsername(java.lang.String)
	 */
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		PangeaUserDetails userDetails = userdetails.get(username);
		

		if (userDetails == null) {

			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			userDetails = new PangeaUserDetails();
			AccessToken token = ((RefreshableKeycloakSecurityContext) auth.getCredentials()).getToken();

			userDetails = new PangeaUserDetails();
			userDetails.setUserName(auth.getName());
			userDetails.getAuthorities().clear();
			
			userDetails.getAuthorities().addAll(PangeaAuthenticationTranslator.resolveRoles(auth));

			userDetails.setCountry((String) token.getOtherClaims().get("country"));
			userDetails.setGroup((String) token.getOtherClaims().get("group"));
			userDetails.setName(token.getName());
			userDetails.setGivenName(token.getGivenName());
			userDetails.setFamilyName(token.getFamilyName());
			userDetails.setClients(token.getAudience());
			userDetails.setRealm(token.getIssuedFor());
			userDetails.setPreferredUsername(token.getPreferredUsername());

			userDetails.setEmail(token.getEmail());
		}

		return userDetails;
	}
	
}
