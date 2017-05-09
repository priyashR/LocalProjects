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

import java.util.concurrent.ExecutionException;

import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;

import za.co.absa.pangea.ops.auth.PangeaAuthenticationException;
import za.co.absa.pangea.ops.auth.user.BasicPangeaUserDetailsService;

/**
 * The Class PangeaAuthenticationBasicWebSecurityConfigurerAdapter.
 */
public abstract class PangeaAuthenticationBasicWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {
	
    /**
	 * Gets the user details service.
	 *
	 * @return the user details service
	 */
    @Bean(name="userDetailsService")
    public UserDetailsService getUserDetailsService(){
    	return new BasicPangeaUserDetailsService();
    }
    
    /**
	 * Configure global.
	 *
	 * @param auth
	 *            the auth
     * @throws ExecutionException 
	 * @throws Exception
	 *             the exception
	 */
    public void configureGlobal(AuthenticationManagerBuilder auth) throws PangeaAuthenticationException {
        String[] makerRoles = new String[]{"tradeops", "Maker", "Checker","Custom_Check_role_2","Custom_Check_role_1"};
        String[] checkerRoles = new String[]{"Checker","Custom_Check_role_2","Custom_Check_role_1"};

        try{
		auth
	        .inMemoryAuthentication()
	        	.withUser("john")
	        		.password("john")
	        		.roles(makerRoles)
	        		.and()
	        	.withUser("jack")
	        		.password("jack")
	        		.roles(checkerRoles)
	        		.and()
	            .withUser("veronica")
	        		.password("veron")
	        		.roles(makerRoles)
	        		.and()
	        	.withUser("betty")
	        		.password("betty")
	        		.roles(checkerRoles)
	       		;
        }catch(Exception e){
        	throw new PangeaAuthenticationException(e);
        }
	
    }

	/* (non-Javadoc)
	 * @see org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter#configure(org.springframework.security.config.annotation.web.builders.HttpSecurity)
	 */
    @Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.httpBasic().and()
	        .authorizeRequests().antMatchers(HttpMethod.OPTIONS).permitAll()
	        .antMatchers("/logout").permitAll()
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
	        .logout().logoutUrl("/logout").permitAll().and()
	        .csrf().disable();
	}

}