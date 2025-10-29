package br.com.fiap.cinefinder.service;

import java.util.List;

import org.springframework.stereotype.Service;

import br.com.fiap.cinefinder.dto.GetUserDto;
import br.com.fiap.cinefinder.repository.AppUserRepo;

@Service
public class AppUserService {

    private final AppUserRepo repo;

    public AppUserService(AppUserRepo repo) {
        this.repo = repo;
    }

    public List<GetUserDto> getAll() {
        return GetUserDto.fromAppUserList(repo.findAll());
    }
    
}
