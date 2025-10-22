package com.jhj.miniproject.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.jhj.miniproject.entity.Edition;
import com.jhj.miniproject.service.EditionService;

@RestController
@RequestMapping("/api/editions")
public class EditionController {

    @Autowired
    private EditionService editionService;

    @GetMapping
    public List<Edition> getAll() {
        return editionService.getAll();
    }

    @GetMapping("/{id}")
    public Edition getById(@PathVariable Long id) {
        return editionService.getById(id);
    }

    @PostMapping
    public Edition create(@RequestBody Edition edition) {
        return editionService.save(edition);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        editionService.delete(id);
    }
}
