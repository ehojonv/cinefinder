package br.com.fiap.cinefinder.service;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.hateoas.EntityModel;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import br.com.fiap.cinefinder.controller.AppUserController;
import br.com.fiap.cinefinder.dto.GetUserDto;
import br.com.fiap.cinefinder.model.AppUser;
import br.com.fiap.cinefinder.repository.AppUserRepo;

@Service
public class AppUserService {

    private final AppUserRepo repo;
    private final PasswordEncoder encoder;

    public AppUserService(AppUserRepo repo, PasswordEncoder encoder) {
        this.repo = repo;
        this.encoder = encoder;
    }

    public Page<AppUser> getAll(Specification<AppUser> specs, Pageable pageable) {
        return repo.findAll(specs, pageable);
    }

    public EntityModel<GetUserDto> getById(Long id) {
        var resource = EntityModel.of(
                GetUserDto.fromAppUser(
                        findByIdOrThrow(id)));

        resource.add(
                linkTo(
                        methodOn(AppUserController.class)
                                .getUserById(id))
                        .withSelfRel(),
                linkTo(
                        methodOn(AppUserController.class)
                                .getAllUsers(null, Pageable.unpaged()))
                        .withRel("all-users"));

        return resource;

    }

    private AppUser findByIdOrThrow(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "User not found"));
    }

    public void create(AppUser user) {
        user.setPassword(encoder.encode(user.getPassword()));
        repo.save(user);
    }

}
