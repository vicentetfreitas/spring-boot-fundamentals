package com.vicentefreitas.springboot.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.vicentefreitas.springboot.models.Paper;
import com.vicentefreitas.springboot.models.User;
import com.vicentefreitas.springboot.repositories.PaperRepository;
import com.vicentefreitas.springboot.repositories.UserRepository;

@Controller
@RequestMapping("/usuarios")
public class UserController {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PaperRepository paperRepository;

	@GetMapping("/novo")
	public String addUser(Model model) {
		model.addAttribute("user", new User());
		return "/publish-create-user";
	}

	@PostMapping("/salvar")
	public String saveUser(@Valid User user, BindingResult result, Model model, RedirectAttributes attributes) {

		if (result.hasErrors()) {

			return "publish-create-user";
		}

		User usr = userRepository.findByLogin(user.getLogin());

		if (usr != null) {
			model.addAttribute("loginExist", "Login já cadastrado");

			return "publish-create-user";
		}

		Paper paper = paperRepository.findByPaper("USER");
		List<Paper> papers = new ArrayList<Paper>();
		papers.add(paper);
		user.setPapers(papers);
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

		User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Id inválido:" + id));
		userRepository.delete(user);

		return "redirect:/usuarios/admin/listar";

	}

	@GetMapping("/editar/{id}")
	public String editUser(@PathVariable("id") long id, Model model) {

		Optional<User> userOld = userRepository.findById(id);

		if (!userOld.isPresent()) {
			throw new IllegalArgumentException("Usuário inválido:" + id);
		}

		User user = userOld.get();
		model.addAttribute("user", user);

		return "/auth/user/user-alter-user";

	}

	@PostMapping("/editar/{id}")
	public String editUser(@PathVariable("id") long id, @Valid User user, BindingResult result) {

		if (result.hasErrors()) {
			user.setId(id);

			return "/auth/user/admin-list-user";
		}

		userRepository.save(user);

		return "redirect:/usuarios/admin/listar";
	}
	
	@GetMapping("/editarPapel/{id}")
	public String selectPaper(@PathVariable("id") Long id, Model model) throws IllegalAccessException {
		
		Optional<User> userOld = userRepository.findById(id);
		
		if(!userOld.isPresent()) {
			throw new IllegalAccessException("Usuário inválido " + id);
		}
		
		User user = userOld.get();
		model.addAttribute("user", user);
		model.addAttribute("listPapers", paperRepository.findAll());
		return "/auth/admin/admin-edit-paper-user";
	}
	

	@PostMapping("/editarPapel/{id}")
	public String atribuirPapel(@PathVariable("id") long idUser, 
								@RequestParam(value = "pps", required=false) int[] pps, 
								User user, 
								RedirectAttributes attributes) {
		if (pps == null) {
			user.setId(idUser);
			attributes.addFlashAttribute("message", "Pelo menos um papel deve ser informado");
			return "redirect:/usuarios/editarPapel/" + idUser;
		} else {
			//Obtém a lista de papéis selecionada pelo usuário do banco
			List<Paper> papers = new ArrayList<Paper>();			 
			for (int i = 0; i < pps.length; i++) {
				long idPaper = pps[i];
				Optional<Paper> paperOptional = paperRepository.findById(idPaper);
				if (paperOptional.isPresent()) {
					Paper paper = paperOptional.get();
					papers.add(paper);
		        }
			}
			Optional<User>userOptional = userRepository.findById(idUser);
			if (userOptional.isPresent()) {
				User usr = userOptional.get();
				usr.setPapers(papers); // relaciona papéis ao usuário
				usr.setActive(user.isActive());
				userRepository.save(usr);
	        }			
		}		
	    return "redirect:/usuarios/admin/listar";
	}


}
