package com.vicentefreitas.springboot.components;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.vicentefreitas.springboot.models.Paper;
import com.vicentefreitas.springboot.repositories.PaperRepository;

@Component
public class LoadData implements CommandLineRunner {

	@Autowired
	private PaperRepository paperRepository;
	
	@Override
	public void run(String... args) throws Exception {

		String[] papers = {"ADMIN", "USER", "LIBRARIAN"};
		
		for(String paperString: papers) {
			Paper paper = paperRepository.findByPaper(paperString);
			if( paper == null) {
				paper = new Paper(paperString);
				paperRepository.save(paper);
			}
		}
		

	}

}
