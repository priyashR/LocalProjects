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

import org.keycloak.adapters.spi.KeycloakAccount;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;

/**
 * The Class PangeaAuthentication.
 */
public class PangeaAuthentication extends KeycloakAuthenticationToken {

	private static final long serialVersionUID = 8790183452467538974L;

	/**
	 * Instantiates a new pangea authentication.
	 *
	 * @param account
	 *            the account
	 */
	public PangeaAuthentication(KeycloakAccount account) {
		super(account);
	}
	
}
