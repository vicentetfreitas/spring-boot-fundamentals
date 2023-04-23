package com.vicentefreitas.springboot.security;

import java.util.HashSet;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.vicentefreitas.springboot.models.Paper;
import com.vicentefreitas.springboot.repositories.UserRepository;

@Service
@Transactional
public class DetailUserService implements UserDetailsService {

	private UserRepository userRepository;
	
	public DetailUserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		com.vicentefreitas.springboot.models.User usr = userRepository.findByLogin(username);
		
		if(usr != null && usr.isActive()) {
			Set<GrantedAuthority> userRoles = new HashSet<GrantedAuthority>();
			for(Paper paper: usr.getPapers()) {
				GrantedAuthority pp = new SimpleGrantedAuthority(paper.getPaper());
				userRoles.add(pp);
			}			
			User user = new User(usr.getLogin(), usr.getPassword(), userRoles);
			return user;
		} else {
			throw new UsernameNotFoundException("Usuário não encontrado");
		}
	}

}
