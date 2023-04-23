package com.vicentefreitas.springboot.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vicentefreitas.springboot.models.Paper;

public interface PaperRepository extends JpaRepository<Paper, Long>{
	Paper findByPaper(String paper);

}
