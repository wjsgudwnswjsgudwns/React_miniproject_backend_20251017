package com.jhj.miniproject.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jhj.miniproject.entity.Edition;
import com.jhj.miniproject.repository.EditionRepository;

@Service
public class EditionService {

	@Autowired
	private EditionRepository editionRepository;
	
	public List<Edition> getAll() {
        return editionRepository.findAll();
    }

    public Edition getById(Long id) {
        return editionRepository.findById(id).orElse(null);
    }

    public Edition save(Edition edition) {
        return editionRepository.save(edition);
    }

    public void delete(Long id) {
        editionRepository.deleteById(id);
    }
}
