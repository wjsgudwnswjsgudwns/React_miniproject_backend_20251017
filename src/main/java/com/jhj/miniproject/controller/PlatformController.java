package com.jhj.miniproject.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.jhj.miniproject.entity.Platform;
import com.jhj.miniproject.service.PlatformService;

@RestController
@RequestMapping("/api/platforms")
public class PlatformController {

    @Autowired
    private PlatformService platformService;

    @GetMapping
    public List<Platform> getAll() {
        return platformService.getAll();
    }

    @GetMapping("/{id}")
    public Platform getById(@PathVariable Long id) {
        return platformService.getById(id);
    }

    @PostMapping
    public Platform create(@RequestBody Platform platform) {
        return platformService.save(platform);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        platformService.delete(id);
    }
}
