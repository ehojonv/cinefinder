package br.com.fiap.cinefinder.controller;

import static org.springframework.http.HttpStatus.CREATED;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.fiap.cinefinder.dto.GetUserDto;
import br.com.fiap.cinefinder.dto.UserDto;
import br.com.fiap.cinefinder.filters.Specifications;
import br.com.fiap.cinefinder.filters.UserFilter;
import br.com.fiap.cinefinder.model.AppUser;
import br.com.fiap.cinefinder.service.AppUserService;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("users")
@Slf4j
public class AppUserController {

    private final AppUserService service;

    public AppUserController(AppUserService appUserService) {
        this.service = appUserService;
    }

    @GetMapping
    public Page<EntityModel<GetUserDto>> getAllUsers(UserFilter filter,
            @PageableDefault(size = 10, sort = "id", direction = Direction.DESC) Pageable pageable) {
        log.info("recuperando todos os usuarios com filtro: {}", filter);
        var specs = Specifications.buildUser(filter);
        return service.getAll(specs, pageable);
    }

    @GetMapping("{id}")
    public EntityModel<GetUserDto> getUserById(@PathVariable Long id) {
        log.info("recuperando usuário pelo id: {}", id);
        return service.getById(id);
    }

    @PostMapping
    @ResponseStatus(code = CREATED)
    public EntityModel<GetUserDto> createUser(@RequestBody UserDto user) {
        log.info("criando novo usuário: {}", user);
        return service.save(user);

    }

    @PutMapping("{id}")
    public EntityModel<GetUserDto> updateUser(@PathVariable Long id, @RequestBody UserDto updUser) {
        log.info("atualizando usuário id: {} com os dados: {}", id, updUser);
        return service.update(id, updUser);

    }

    @DeleteMapping("{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable Long id) {
        log.info("deletando usuário pelo id: {}", id);
        service.delete(id);
    }

}
