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
package za.co.absa.pangea.ops.auth.translator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.keycloak.adapters.RefreshableKeycloakSecurityContext;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.keycloak.representations.AccessToken;
import org.keycloak.representations.AccessToken.Access;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * The Class PangeaAuthenticationTranslator.
 */
public class PangeaAuthenticationTranslator {

	@Autowired
	UserDetailsService userDetailsService;

	private Authentication authentication;

	/**
	 * Instantiates a new pangea authentication translator.
	 *
	 * @param authentication
	 *            the authentication
	 */
	public PangeaAuthenticationTranslator(Authentication authentication) {
		translate(authentication);
	}

	/**
	 * Translate.
	 *
	 * @param authentication
	 *            the authentication
	 */
	private void translate(Authentication authentication) {
		if (authentication instanceof KeycloakAuthenticationToken) {
			KeycloakAuthenticationToken auth = (KeycloakAuthenticationToken) authentication;
			this.authentication = new KeycloakAuthenticationToken(auth.getAccount(), resolveRoles(authentication));
		}
	}

	/**
	 * Gets the authentication.
	 *
	 * @return the authentication
	 */
	public Authentication getAuthentication() {
		return authentication;
	}

	/**
	 * Resolve roles.
	 *
	 * @param authentication
	 *            the authentication
	 * @return the collection<? extends granted authority>
	 */
	public static Collection<? extends GrantedAuthority> resolveRoles(Authentication authentication) {

		AccessToken token = ((RefreshableKeycloakSecurityContext) authentication.getCredentials()).getToken();

		List<SimpleGrantedAuthority> resultAuthorities = new ArrayList<>();

		if (token != null) {
			for (GrantedAuthority ga : authentication.getAuthorities()) {
				resultAuthorities.add(new SimpleGrantedAuthority(ga.getAuthority()));
			}
			if (token.getRealmAccess() != null) {
				for (String role : token.getRealmAccess().getRoles()) {
					resultAuthorities.add(new SimpleGrantedAuthority(role));
				}
			}
			if (token.getResourceAccess() != null) {
				for (Access access : token.getResourceAccess().values()) {
					for (String role : access.getRoles()) {
						resultAuthorities.add(new SimpleGrantedAuthority(role));
					}
				}
			}
		}
		return resultAuthorities;
	}

}
