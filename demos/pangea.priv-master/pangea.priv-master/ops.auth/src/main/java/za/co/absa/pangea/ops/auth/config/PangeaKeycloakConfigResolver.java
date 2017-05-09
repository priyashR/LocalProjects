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
package za.co.absa.pangea.ops.auth.config;

import org.jboss.logging.Logger;
import org.keycloak.adapters.KeycloakConfigResolver;
import org.keycloak.adapters.KeycloakDeployment;
import org.keycloak.adapters.rotation.HardcodedPublicKeyLocator;
import org.keycloak.adapters.spi.HttpFacade.Request;
import org.keycloak.common.enums.SslRequired;
import org.keycloak.common.util.PemUtils;
import org.keycloak.representations.adapters.config.AdapterConfig;
import org.springframework.beans.factory.annotation.Value;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.PublicKey;

/**
 * The Class PangeaKeycloakConfigResolver.
 */
public class PangeaKeycloakConfigResolver implements KeycloakConfigResolver {

	private static final Logger logger = Logger.getLogger(PangeaKeycloakConfigResolver.class);
	
	@Value("${config.keycloak.realm:}")
    private String realm;
	@Value("${config.keycloak.realm-public-key:}")
	private String realmKey;
	@Value("${config.keycloak.auth-server-url:}")
	private String authServerBaseUrl;
	@Value("${config.keycloak.resource:}")
	private String resourceName;
	@Value("${config.keycloak.bearer-only:false}")
	private boolean bearerOnly;
	@Value("${config.keycloak.public-client:false}")
	private boolean publicClient;
	@Value("${config.keycloak.ssl-required:}")
	private String sslRequired;
	@Value("${config.keycloak.use-resource-role-mappings:false}")
	private boolean useResourceRoleMappings;
	@Value("${config.keycloak.enable-cors:false}")
	private boolean cors;
	
	private KeycloakDeployment deployment=null;
	
	/* (non-Javadoc)
	 * @see org.keycloak.adapters.KeycloakConfigResolver#resolve(org.keycloak.adapters.spi.HttpFacade.Request)
	 */
	@Override
	public KeycloakDeployment resolve(Request facade) {
		if(deployment == null) {
			deployment =  new KeycloakDeployment();
			deployment.setRealm(realm);

			//TODO - JHP - PublicKey self discovery - Does not seem to work can investigate later
			//JWKPublicKeyLocator pkLocator = new JWKPublicKeyLocator();
			//deployment.setPublicKeyLocator(pkLocator);

			PublicKey pk = PemUtils.decodePublicKey(realmKey);
			deployment.setPublicKeyLocator(new HardcodedPublicKeyLocator(pk));

			deployment.setAuthServerBaseUrl(resolveURL(authServerBaseUrl));
			deployment.setResourceName(resourceName);
			deployment.setBearerOnly(bearerOnly);
			deployment.setPublicClient(publicClient);
			deployment.setSslRequired(resolveSSL(sslRequired));
			deployment.setUseResourceRoleMappings(useResourceRoleMappings);
			deployment.setCors(cors);
		}
		return deployment;
	}
	
	/**
	 * Resolve ssl.
	 *
	 * @param sslRequired
	 *            the ssl required
	 * @return the ssl required
	 */
	private SslRequired resolveSSL(String sslRequired){
		return SslRequired.valueOf(sslRequired.toUpperCase());
	}
	
	/**
	 * Resolve url.
	 *
	 * @param url
	 *            the url
	 * @return the adapter config
	 */
	private AdapterConfig resolveURL(String url){
		AdapterConfig config = new AdapterConfig();
		String effectiveUrl = url;
		
		if(url.contains("localhost")){
			try {
				String ipAddress = InetAddress.getLocalHost().getHostAddress();
				effectiveUrl = url.replaceAll("localhost", ipAddress);
			} catch (UnknownHostException e) {
				logger.error(e);
			}
		}
		config.setAuthServerUrl(effectiveUrl);
		return config;
	}
	
	/**
	 * Resolve key.
	 *
	 * @param key
	 *            the key
	 * @return the public key
	 */
	private PublicKey resolveKey(String key){
		PublicKey localRealmKey = null;
        if (key != null) {
            try {
            	localRealmKey = PemUtils.decodePublicKey(key);
            } catch (Exception e) {
            	logger.error(e);
            }
        }
        return localRealmKey;
	}

}
