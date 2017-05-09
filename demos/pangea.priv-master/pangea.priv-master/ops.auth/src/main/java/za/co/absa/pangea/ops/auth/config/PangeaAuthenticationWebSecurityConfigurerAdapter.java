/*********************************************
 * Copyright 2016 Absa ©
 * 05 Jul 2016
 * @author Eon van Tonder
 * @auther Nakedi Mabusela
 * @author Abigail Munzhelele
 * @author Jannie Pieterse
 * 
 * All rights reserved
 *********************************************/
package za.co.absa.pangea.ops.auth.config;

import org.keycloak.adapters.KeycloakConfigResolver;
import org.keycloak.adapters.springsecurity.client.KeycloakClientRequestFactory;
import org.keycloak.adapters.springsecurity.client.KeycloakRestTemplate;
import org.keycloak.adapters.springsecurity.config.KeycloakWebSecurityConfigurerAdapter;
import org.keycloak.adapters.springsecurity.filter.KeycloakAuthenticationProcessingFilter;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.NullAuthoritiesMapper;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.security.web.header.HeaderWriterFilter;

import za.co.absa.pangea.ops.auth.PangeaAuthenticationException;
import za.co.absa.pangea.ops.auth.filter.PangeaAuthenticationPostAuthActionsFilter;
import za.co.absa.pangea.ops.auth.filter.PangeaCORSFilter;
import za.co.absa.pangea.ops.auth.provider.PangeaAuthenticationProvider;
import za.co.absa.pangea.ops.auth.user.KeycloakPangeaUserDetailsService;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * The Class PangeaAuthenticationWebSecurityConfigurerAdapter.
 */
public abstract class PangeaAuthenticationWebSecurityConfigurerAdapter extends KeycloakWebSecurityConfigurerAdapter {
	
	private KeycloakClientRequestFactory keycloakClientRequestFactory = new KeycloakClientRequestFactory();
	
	/**
	 * Gets the keycloak config resolver.
	 *
	 * @return the keycloak config resolver
	 */
	@Bean
	public KeycloakConfigResolver getKeycloakConfigResolver(){
		return new PangeaKeycloakConfigResolver();
	}
	
    /**
	 * Gets the user details service.
	 *
	 * @return the user details service
	 */
    @Bean(name="userDetailsService")
    public UserDetailsService getUserDetailsService(){
    	return new KeycloakPangeaUserDetailsService();
    }
    
	/**
	 * Custom authentication provider.
	 *
	 * @return the authentication provider
	 */
	@Bean(name = "customAuthenticationProvider")
	public AuthenticationProvider customAuthenticationProvider() {
		PangeaAuthenticationProvider ap = new PangeaAuthenticationProvider();
		ap.setGrantedAuthoritiesMapper(new GrantedAuthoritiesMapper() {

			private GrantedAuthoritiesMapper chain = new NullAuthoritiesMapper();
			@Override
			public Collection<? extends GrantedAuthority> mapAuthorities(Collection<? extends GrantedAuthority> authorities) {

				String rolePrefix = "ROLE_";

				Set<GrantedAuthority> out = new HashSet<GrantedAuthority>();

				for (GrantedAuthority authority : authorities){
					String role = authority.getAuthority();
					if (rolePrefix != null && role != null && !role.startsWith(rolePrefix)) {
						role = rolePrefix + role;
					}
					out.add(new SimpleGrantedAuthority(role));
				}
				return chain.mapAuthorities(out);
			}
		});
		return ap;
	}

	/**
	 * Post authentication processing filter.
	 *
	 * @return the pangea authentication post auth actions filter
	 */
	@Bean
	protected PangeaAuthenticationPostAuthActionsFilter postAuthenticationProcessingFilter() {
		return new PangeaAuthenticationPostAuthActionsFilter();
	}

	/**
	 * Keycloak rest template.
	 *
	 * @return the keycloak rest template
	 */
	@Bean
	@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
	public KeycloakRestTemplate keycloakRestTemplate() {
		return new KeycloakRestTemplate(getKeycloakClientRequestFactory());
	}
	
    /* (non-Javadoc)
     * @see org.keycloak.adapters.springsecurity.config.KeycloakWebSecurityConfigurerAdapter#sessionAuthenticationStrategy()
     */
    @Bean
    @Override
    protected SessionAuthenticationStrategy sessionAuthenticationStrategy() {
        return new RegisterSessionAuthenticationStrategy(new SessionRegistryImpl());
    }
	
    /**
	 * Configure global.
	 *
	 * @param auth
	 *            the auth
	 * @throws PangeaAuthenticationException
	 *             the pangea authentication exception
	 */
    public void configureGlobal(AuthenticationManagerBuilder auth) throws PangeaAuthenticationException {
    	try{
    		auth.authenticationProvider(customAuthenticationProvider());
    	}catch(Exception e){
    		throw new PangeaAuthenticationException(e);
    	}
    }
    
    /**
	 * Csrf token repository.
	 *
	 * @return the csrf token repository
	 */
    @Bean
    protected CsrfTokenRepository csrfTokenRepository() {
        HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository();
        repository.setHeaderName("X-XSRF-TOKEN");
        return repository;
    }

	/* (non-Javadoc)
	 * @see org.keycloak.adapters.springsecurity.config.KeycloakWebSecurityConfigurerAdapter#configure(org.springframework.security.config.annotation.web.builders.HttpSecurity)
	 */
    @Override
	protected void configure(HttpSecurity http) throws Exception {
		super.configure(http);
		http
        .authorizeRequests()
        .antMatchers(HttpMethod.OPTIONS).permitAll()
		.antMatchers("/setup").permitAll()
		.antMatchers("/restart").permitAll()
		.antMatchers("/health").permitAll()
		.antMatchers("/mappings").permitAll()
		.antMatchers("/metrics").permitAll()
		.antMatchers("/env").permitAll()
		.antMatchers("/trace").permitAll()
		.antMatchers("/beans").permitAll()
		.antMatchers("/dump").permitAll()
		.antMatchers("/info").permitAll()
		.antMatchers("/error").permitAll()
		.antMatchers("/swagger").permitAll()
		.antMatchers("/v2/api-docs").permitAll()
        .anyRequest().authenticated().and()
		.addFilterAfter(postAuthenticationProcessingFilter(), KeycloakAuthenticationProcessingFilter.class)
		.addFilterAfter(new PangeaCORSFilter(), HeaderWriterFilter.class)
		.csrf().disable();
	}

	/**
	 * Gets the keycloak client request factory.
	 *
	 * @return the keycloak client request factory
	 */
	public KeycloakClientRequestFactory getKeycloakClientRequestFactory() {
		return keycloakClientRequestFactory;
	}

	/**
	 * Sets the keycloak client request factory.
	 *
	 * @param keycloakClientRequestFactory
	 *            the new keycloak client request factory
	 */
	public void setKeycloakClientRequestFactory(KeycloakClientRequestFactory keycloakClientRequestFactory) {
		this.keycloakClientRequestFactory = keycloakClientRequestFactory;
	}

}