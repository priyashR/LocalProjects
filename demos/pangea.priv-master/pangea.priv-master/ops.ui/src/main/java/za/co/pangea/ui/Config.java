/*********************************************
 * Copyright 2016 Absa ©
 * 27 May 2016
 * @author Jannie
 * 
 * All rights reserved
 *********************************************/
package za.co.pangea.ui;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The Class Config.
 */
@Component
public class Config {

	@Value("${config.baseServicesUrl}")
	private String baseServicesUrl;
	@Value("${config.baseWorkFlowUrl}")
	private String baseWorkFlowUrl;
	
	// Versioning
	@Value("${config.applicationTitle}")
	private String applicationTitle;
	@Value("${config.applicationVersion}")
	private String applicationVersion;
	@Value("${config.buildNumber}")
	private String buildNumber;
	@Value("${config.env}")
	private String env;
	@Value("${config.jenBuildId}")	
	private String jenBuildId;
	@Value("${config.jenBuildNum}")	
	private String jenBuildNum;
	@Value("${config.gitCommit}")	
	private String gitCommit;
		
	@Value("${config.developerTesting:false}")
	private String developerTesting;
	
	@Value("${config.keycloak.realm:}")
	private String realm;
	@Value("${config.keycloak.realm-public-key:}")
	private String realmPublicKey;
	@Value("${config.keycloak.auth-server-url:}")
	private String authServerUrl;
	@Value("${config.keycloak.ssl-required:}")
	private String sslRequired;
	@Value("${config.keycloak.resource:}")
	private String resource;
	@Value("${config.keycloak.public-client:}")
	private String publicClient;
	@Value("${config.keycloak.use-resource-role-mappings:}")
	private String useResourceRoleMappings;
	@Value("${config.keycloak.enable-cors:}")
	private String enableCors;

	/**
	 * Gets the keycloak.
	 *
	 * @return the keycloak
	 */
	@Autowired
	public KeycloakConfig getKeycloak() {
		return new KeycloakConfig(realm, realmPublicKey, authServerUrl, sslRequired, resource, publicClient,
				useResourceRoleMappings, enableCors);
	}

	/**
	 * Gets the base services url.
	 *
	 * @return the base services url
	 */
	public String getBaseServicesUrl() {
		return ipResolver(baseServicesUrl);
	}

	/**
	 * Gets the application title.
	 *
	 * @return the application title
	 */
	public String getApplicationTitle() {
		return applicationTitle;
	}

	/**
	 * Gets the application version.
	 *
	 * @return the application version
	 */
	public String getApplicationVersion() {
		return applicationVersion;
	}
	
	/**
	 * Gets the Build number of this version of the application.
	 * @return the build number of the deliverable.
	 */
	public String getBuildNumber() {
		return buildNumber;
	}

	/**
	 * Gets the developer testing.
	 *
	 * @return the developer testing
	 */
	public String getDeveloperTesting() {
		return developerTesting;
	}

	/**
	 * Gets the base work flow url.
	 *
	 * @return the base work flow url
	 */
	public String getBaseWorkFlowUrl() {
		return ipResolver(baseWorkFlowUrl);
	}
	
	
	public String getEnv() {
		return env;
	}

	public String getJenBuildId() {
		return jenBuildId;
	}

	public String getJenBuildNum() {
		return jenBuildNum;
	}

	public String getGitCommit() {
		return gitCommit;
	}


	/**
	 * The Class KeycloakConfig.
	 */
	public class KeycloakConfig {

		/**
		 * Instantiates a new keycloak config.
		 *
		 * @param realm
		 *            the realm
		 * @param realmPublicKey
		 *            the realm public key
		 * @param authServerUrl
		 *            the auth server url
		 * @param sslRequired
		 *            the ssl required
		 * @param resource
		 *            the resource
		 * @param publicClient
		 *            the public client
		 * @param useResourceRoleMappings
		 *            the use resource role mappings
		 * @param enableCors
		 *            the enable cors
		 */
		public KeycloakConfig(String realm, String realmPublicKey, String authServerUrl, String sslRequired,
				String resource, String publicClient, String useResourceRoleMappings, String enableCors) {
			super();
			this.realm = realm;
			this.realmPublicKey = realmPublicKey;
			this.authServerUrl = authServerUrl;
			this.sslRequired = sslRequired;
			this.resource = resource;
			this.publicClient = publicClient;
			this.useResourceRoleMappings = useResourceRoleMappings;
			this.enableCors = enableCors;
		}

		@JsonProperty(value = "realm")
		private String realm;
		@JsonProperty(value = "realm-public-key")
		private String realmPublicKey;
		@JsonProperty(value = "url")
		private String authServerUrl;
		@JsonProperty(value = "ssl-required")
		private String sslRequired;
		@JsonProperty(value = "clientId")
		private String resource;
		@JsonProperty(value = "public-client")
		private String publicClient;
		@JsonProperty(value = "use-resource-role-mappings")
		private String useResourceRoleMappings;
		@JsonProperty(value = "enable-cors")
		private String enableCors;

		/**
		 * Gets the realm.
		 *
		 * @return the realm
		 */
		public String getRealm() {
			return realm;
		}

		/**
		 * Gets the realm public key.
		 *
		 * @return the realm public key
		 */
		public String getRealmPublicKey() {
			return realmPublicKey;
		}

		/**
		 * Gets the auth server url.
		 *
		 * @return the auth server url
		 */
		public String getAuthServerUrl() {
			return Config.ipResolver(authServerUrl);
		}

		/**
		 * Checks if is ssl required.
		 *
		 * @return the string
		 */
		public String isSslRequired() {
			return sslRequired;
		}

		/**
		 * Gets the resource.
		 *
		 * @return the resource
		 */
		public String getResource() {
			return resource;
		}

		/**
		 * Gets the public client.
		 *
		 * @return the public client
		 */
		public String getPublicClient() {
			return publicClient;
		}

		/**
		 * Gets the use resource role mappings.
		 *
		 * @return the use resource role mappings
		 */
		public String getUseResourceRoleMappings() {
			return useResourceRoleMappings;
		}

		/**
		 * Gets the enable cors.
		 *
		 * @return the enable cors
		 */
		public String getEnableCors() {
			return enableCors;
		}

	}

	/**
	 * Ip resolver.
	 *
	 * @param host
	 *            the host
	 * @return the string
	 */
	private static String ipResolver(String host) {
		if (host.contains("localhost")) {
			try {
				String ipAddress = InetAddress.getLocalHost().getHostAddress();
				host = host.replaceAll("localhost", ipAddress);
			} catch (UnknownHostException e) {
				// TODO - JHP need to define the exception
				e.printStackTrace();
			}
		}
		return host;

	}

}
