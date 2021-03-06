package com.pruebas.spring.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled =  true)
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth
			.inMemoryAuthentication()
				.withUser("user").password("password").roles("USER").and()
				.withUser("admin").password("admin").roles("USER", "ADMIN");
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		http.authorizeRequests()
				.antMatchers("/login").permitAll()
				.antMatchers("/webresources/**").permitAll()
				.antMatchers("/usuariodiregeper/**").permitAll()
				.antMatchers("/admin/**").access("hasAnyRole('DIREJEPER','SEGUROS')")
			.and()
				.formLogin()
					.loginPage("/a_metodo1")
						.failureUrl("/a_metodo1?error")
						.defaultSuccessUrl("/a_metodo1")
					.usernameParameter("username")
					.passwordParameter("password")
					.and()
				.logout()
					.permitAll()
					.deleteCookies("remove")
					.logoutUrl("/logout")
					.logoutRequestMatcher(new AntPathRequestMatcher("/logout", "GET"))
					.logoutSuccessUrl("/login?logout")
					.invalidateHttpSession(false)
			.and()
				.exceptionHandling()
					.accessDeniedPage("/accesorestringido");
		
	}
	
	@Bean
	public PasswordEncoder passwordEncoder(){
	    PasswordEncoder encoder = new BCryptPasswordEncoder();
	    return encoder;
	}
	
}
