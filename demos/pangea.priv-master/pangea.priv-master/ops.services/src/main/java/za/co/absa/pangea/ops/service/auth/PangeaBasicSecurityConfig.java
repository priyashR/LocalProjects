package za.co.absa.pangea.ops.service.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import za.co.absa.pangea.ops.auth.PangeaAuthentication;
import za.co.absa.pangea.ops.auth.PangeaAuthenticationException;
import za.co.absa.pangea.ops.auth.config.PangeaAuthenticationBasicWebSecurityConfigurerAdapter;

@Configuration
@ComponentScan(basePackageClasses = PangeaAuthentication.class)
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Order(Ordered.HIGHEST_PRECEDENCE)
@ConditionalOnProperty(prefix="config", name = "developerTesting",havingValue = "true", matchIfMissing = false)
public class PangeaBasicSecurityConfig extends PangeaAuthenticationBasicWebSecurityConfigurerAdapter {

	@Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws PangeaAuthenticationException{
		super.configureGlobal(auth);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception
    {
        super.configure(http);
        http.headers().frameOptions().disable();
    }
    
}
