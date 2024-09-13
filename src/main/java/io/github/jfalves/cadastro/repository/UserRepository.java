package io.github.jfalves.cadastro.repository;

import io.github.jfalves.cadastro.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserModel, Long> {
}
