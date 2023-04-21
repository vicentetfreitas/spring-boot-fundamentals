package com.vicentefreitas.springboot.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {
			
	@RequestMapping("/")
	public String index(Model model) {
	model.addAttribute("msnWelcome", "Bem vindo à biblioteca!");
	return "publish-index";
	
	}

}
