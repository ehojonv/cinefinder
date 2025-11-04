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
import br.com.fiap.cinefinder.dto.UserDto;
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

        public Page<EntityModel<GetUserDto>> getAll(Specification<AppUser> specs, Pageable pageable) {
                return repo.findAll(specs, pageable).map(AppUserService::toModel);
        }

        public EntityModel<GetUserDto> getById(Long id) {
                return toModel(findByIdOrThrow(id));
        }

        public EntityModel<GetUserDto> update(Long id,  UserDto upd) {
                var existing = findByIdOrThrow(id);
                existing.setUsername(upd.username() != null ? upd.username() : existing.getUsername());
                existing.setEmail(upd.email() != null ? upd.email() : existing.getEmail());
                existing.setDateOfBirth(
                                upd.dateOfBirth() != null ? upd.dateOfBirth() : existing.getDateOfBirth());
                existing.setPassword(upd.password() != null ? upd.password() : existing.getPassword());
                return toModel(repo.save(existing));
        }

        public EntityModel<GetUserDto> save( UserDto nUser) {
                var user = AppUser.builder()
                                .username(nUser.username())
                                .email(nUser.email())
                                .dateOfBirth(nUser.dateOfBirth())
                                .password(encoder.encode(nUser.password()))
                                .build();
                return toModel(repo.save(user));
        }

        public void delete(Long id) {
                repo.deleteById(id);
        }

        public AppUser findByIdOrThrow(Long id) {
                return repo.findById(id)
                                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Usuário não encontrado"));
        }

        private static EntityModel<GetUserDto> toModel(AppUser user) {
                var resource = EntityModel.of(GetUserDto.fromAppUser(user));

                resource.add(
                                linkTo(
                                                methodOn(AppUserController.class)
                                                                .getUserById(user.getId()))
                                                .withSelfRel(),
                                linkTo(
                                                methodOn(AppUserController.class)
                                                                .getAllUsers(null, Pageable.unpaged()))
                                                .withRel("all-users"));

                return resource;
        }

}
