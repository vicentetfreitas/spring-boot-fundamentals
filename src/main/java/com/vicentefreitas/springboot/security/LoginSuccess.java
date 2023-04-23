package com.vicentefreitas.springboot.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.vicentefreitas.springboot.models.Paper;
import com.vicentefreitas.springboot.models.User;
import com.vicentefreitas.springboot.repositories.UserRepository;

@Component
public class LoginSuccess extends SavedRequestAwareAuthenticationSuccessHandler {

	@Autowired
	private UserRepository usuarioRepository;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws ServletException, IOException {

		// pega o login do usuário logado
		String login = authentication.getName();
		// busca o usuário no banco pelo login
		User user = usuarioRepository.findByLogin(login);

		String redirectURL = "";
		if (hasAuthorization(user, "ADMIN")) {
			redirectURL = "/auth/admin/admin-index";
		} else if (hasAuthorization(user, "USER")) {
			redirectURL = "/auth/user/user-index";
		} else if (hasAuthorization(user, "BIBLIOTECARIO")) {
			redirectURL = "/auth/biblio/biblio-index";
		}
		response.sendRedirect(redirectURL);
	}

	/**
	 * Método que verifica qual papel o usuário tem na aplicação
	 */
	private boolean hasAuthorization(User user, String paper) {
		for (Paper pp : user.getPapers()) {
			if (pp.getPaper().equals(paper)) {
				return true;
			}
		}
		return false;
	}

}
