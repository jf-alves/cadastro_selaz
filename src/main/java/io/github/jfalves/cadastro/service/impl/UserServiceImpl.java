package io.github.jfalves.cadastro.service.impl;

import io.github.jfalves.cadastro.model.UserModel;
import io.github.jfalves.cadastro.repository.UserRepository;
import io.github.jfalves.cadastro.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository repository;

    public List<UserModel> findAllUsers(){
        return repository.findAll();
    }

    public UserModel save(UserModel userModel){
        return repository.save(userModel);
    }

    public void delete(UserModel userModel){
        repository.delete(userModel);
    }

    @Override
    public Optional<UserModel> findById(Long userId) {
        return repository.findById(userId);
    }

}
