package io.github.jfalves.cadastro.controller;


import io.github.jfalves.cadastro.dto.UserDto;
import io.github.jfalves.cadastro.model.UserModel;
import io.github.jfalves.cadastro.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*", maxAge = 3600)
public class UserController {

    @Autowired
    UserService service;

    @Operation(description = "Listar todos os usuários.")
    @GetMapping
    public ResponseEntity<List<UserModel>> getAllUsers(){
        return ResponseEntity.status(HttpStatus.OK).body(service.findAllUsers());
    }

    @Operation(description = "Criar um novo usuário.")
    @PostMapping
    public ResponseEntity<Object> createUser(@RequestBody @Valid UserDto userDto){

        var user = new UserModel();
        BeanUtils.copyProperties(userDto, user);

        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(user));
    }

    @Operation(description = "Atualiza um usuário existente.")
    @PutMapping("/{userId}")
    public ResponseEntity<Object> updateUser(@PathVariable(value = "userId") Long userId,
                                             @RequestBody @Valid UserDto userDto){
        Optional<UserModel> userModelOptional = service.findById(userId);
        if (userModelOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Usuario não encontrado");
        }
        return ResponseEntity.status(HttpStatus.OK).body("Usuario atualizado com sucesso");
    }

    @Operation(description = "Deletar um usuário existente.")
    @DeleteMapping("/{userId}")
    public ResponseEntity<Object> deleteUser(@PathVariable(value = "userId")Long userId){
        Optional<UserModel> userModelOptional = service.findById(userId);
        if(userModelOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Usuario não encontrado");
        }
        service.delete(userModelOptional.get());
        return ResponseEntity.status(HttpStatus.OK).body("Usuario deletado com sucesso");
    }

}
