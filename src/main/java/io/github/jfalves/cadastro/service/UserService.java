package io.github.jfalves.cadastro.service;

import io.github.jfalves.cadastro.model.UserModel;

import java.util.List;
import java.util.Optional;

public interface UserService {

    UserModel save(UserModel user);

    List<UserModel> findAllUsers();

    void delete(UserModel user);

    Optional<UserModel> findById(Long userId);
}
