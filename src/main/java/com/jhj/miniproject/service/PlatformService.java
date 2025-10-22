package com.jhj.miniproject.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.jhj.miniproject.entity.Platform;
import com.jhj.miniproject.repository.PlatformRepository;

@Service
public class PlatformService {

    @Autowired
    private PlatformRepository platformRepository;

    public List<Platform> getAll() {
        return platformRepository.findAll();
    }

    public Platform getById(Long id) {
        return platformRepository.findById(id).orElse(null);
    }

    public Platform save(Platform platform) {
        return platformRepository.save(platform);
    }

    public void delete(Long id) {
        platformRepository.deleteById(id);
    }
}
