package com.vicentefreitas.springboot.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.vicentefreitas.springboot.repositories.UserRepository;

@SuppressWarnings("deprecation")
@Configuration
@EnableWebSecurity
public class ConfigurationSecurity extends WebSecurityConfigurerAdapter{
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private LoginSuccess loginSuccess;
	
	@Bean
	public BCryptPasswordEncoder generateEncryption() {
		BCryptPasswordEncoder criptcryptography = new BCryptPasswordEncoder();
		return criptcryptography;
	}
	
	@Override
	public UserDetailsService userDetailsServiceBean() throws Exception {
		DetailUserService detailUserService = new DetailUserService(userRepository);
		return detailUserService;
	}

	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
		.antMatchers("/").permitAll()
		.antMatchers("/auth/user/*").hasAnyAuthority("USER","ADMIN","BIBLIOTECARIO")
		.antMatchers("/auth/admin/*").hasAnyAuthority("ADMIN")
		.antMatchers("/auth/biblio/*").hasAnyAuthority("BIBLIOTECARIO")
		.antMatchers("/usuarios/admin/*").hasAnyAuthority("ADMIN")
		.and()
		.exceptionHandling().accessDeniedPage("/auth/auth-access-denied")
		.and()
		.formLogin().successHandler(loginSuccess)
		.loginPage("/login").permitAll()
		.and()
		.logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
		.logoutSuccessUrl("/").permitAll();
	}
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		// O objeto que vai obter os detalhes do usuário
		UserDetailsService userDetail = userDetailsServiceBean();
		// Objeto para criptografia
		BCryptPasswordEncoder criptcryptography = generateEncryption();
		
		auth.userDetailsService(userDetail).passwordEncoder(criptcryptography);
	}

}
