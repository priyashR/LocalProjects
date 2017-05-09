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
package za.co.absa.pangea.ops.auth;

import static org.junit.Assert.assertEquals;

import java.security.Principal;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.adapters.OidcKeycloakAccount;
import org.keycloak.adapters.RefreshableKeycloakSecurityContext;
import org.keycloak.adapters.spi.KeycloakAccount;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.springframework.security.core.Authentication;

import za.co.absa.pangea.ops.auth.translator.PangeaAuthenticationTranslator;

/**
 * The Class PangeaAuthenticationTests.
 */
public class PangeaAuthenticationTest {
	
	/**
	 * Test auth init.
	 *
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void testAuthInit() throws Exception {
		
		KeycloakAccount account = getAccount();
		PangeaAuthentication pangeaAuthentication = new PangeaAuthentication(account);
		
		assertEquals(true, pangeaAuthentication instanceof Authentication);
		assertEquals(pangeaAuthentication.getPrincipal().toString(),"TEST");
		
	}

	/**
	 * Test translator.
	 *
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void testTranslator() throws Exception {
		PangeaAuthenticationTranslator translator = new PangeaAuthenticationTranslator(new KeycloakAuthenticationToken(getAccount()));
		
		assertEquals(translator.getAuthentication().getPrincipal().toString(),"TEST");
	}

	/**
	 * Gets the account.
	 *
	 * @return the account
	 */
	private OidcKeycloakAccount getAccount(){
		return new OidcKeycloakAccount() {
			
			@Override
			public Set<String> getRoles() {
				return new HashSet<String>();
			}
			
			@Override
			public Principal getPrincipal() {
				return new KeycloakPrincipal<KeycloakSecurityContext>("TEST", new KeycloakSecurityContext());
			}

			@Override
			public KeycloakSecurityContext getKeycloakSecurityContext() {
				return new RefreshableKeycloakSecurityContext();
			}
		};

	}
	
}
