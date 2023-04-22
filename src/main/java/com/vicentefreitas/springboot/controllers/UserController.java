package com.vicentefreitas.springboot.controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.vicentefreitas.springboot.models.User;
import com.vicentefreitas.springboot.repositories.UserRepository;

@Controller
@RequestMapping("/usuarios")
public class UserController {
	
	@Autowired
	private UserRepository userRepository;

	@GetMapping("/novo")
	public String addUser(Model model) {
		model.addAttribute("user", new User());
		return "/publish-create-user";
	}
	
	@PostMapping("/salvar")
	public String saveUser(@Valid User user, BindingResult result, RedirectAttributes attributes) {
		 if(result.hasErrors()) {
			 return "publish-create-user";
		 }
		 userRepository.save(user);
		 attributes.addFlashAttribute("message", "Usuário salvo com sucesso!!");
		 return "redirect:/usuarios/novo";
		
	}
	
	@RequestMapping("/admin/listar")
	public String listUser(Model model) {
		model.addAttribute("users", userRepository.findAll());
		return "/auth/admin/admin-list-user";
	}
	
	@GetMapping("/admin/apagar/{id}")
	public String deleteUser(@PathVariable("id") long id, Model model) {
		User user = userRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Id inválido:" + id));
		userRepository.delete(user);
	    return "redirect:/usuarios/admin/listar";
	}

}
	