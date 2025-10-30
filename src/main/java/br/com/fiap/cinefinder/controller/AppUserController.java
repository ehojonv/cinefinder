package br.com.fiap.cinefinder.controller;

import static org.springframework.http.HttpStatus.CREATED;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.fiap.cinefinder.dto.GetUserDto;
import br.com.fiap.cinefinder.filters.Specifications;
import br.com.fiap.cinefinder.filters.UserFilter;
import br.com.fiap.cinefinder.model.AppUser;
import br.com.fiap.cinefinder.service.AppUserService;

@RestController
@RequestMapping("users")
public class AppUserController {

    private final AppUserService service;

    public AppUserController(AppUserService appUserService) {
        this.service = appUserService;
    }

    @GetMapping
    public Page<GetUserDto> getAllUsers(UserFilter filter,
            @PageableDefault(size = 10, sort = "username", direction = Direction.DESC) Pageable pageable) {
        var specs = Specifications.buildUser(filter);
        return service.getAll(specs, pageable).map(GetUserDto::fromAppUser);
    }

    @GetMapping("/{id}")
    public EntityModel<GetUserDto> getUserById(@PathVariable Long id) {
        return service.getById(id);
    }

    @PostMapping
    @ResponseStatus(code = CREATED)
    public void createUser(@RequestBody AppUser user) {
        service.create(user);

    }



}
