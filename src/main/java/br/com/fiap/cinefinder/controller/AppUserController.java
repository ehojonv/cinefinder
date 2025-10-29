package br.com.fiap.cinefinder.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.fiap.cinefinder.dto.GetUserDto;
import br.com.fiap.cinefinder.service.AppUserService;

@RestController
@RequestMapping("users")
public class AppUserController {

    private final AppUserService service;
    
    public AppUserController(AppUserService appUserService) {
        this.service = appUserService;
    }

    @GetMapping
    public List<GetUserDto> getAllUsers() {
        return service.getAll();
    }
    
}
