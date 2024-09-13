package io.github.jfalves.cadastro.controller;

import io.github.jfalves.cadastro.dto.UserDto;
import io.github.jfalves.cadastro.model.UserModel;
import io.github.jfalves.cadastro.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class UserControllerTest {
    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Deve retornar todos os usuários com sucesso")
    void testGetAllUsers_Success() {
        List<UserModel> mockUsers = List.of(new UserModel(), new UserModel());

        when(userService.findAllUsers()).thenReturn(mockUsers);

        ResponseEntity<List<UserModel>> response = userController.getAllUsers();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockUsers, response.getBody());
        verify(userService, times(1)).findAllUsers();
    }

    @Test
    @DisplayName("Deve criar um novo usuário com sucesso")
    void testCreateUser_Success() {
        UserDto userDto = new UserDto();
        UserModel userModel = new UserModel();
        BeanUtils.copyProperties(userDto, userModel);

        when(userService.save(any(UserModel.class))).thenReturn(userModel);

        ResponseEntity<Object> response = userController.createUser(userDto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(userModel, response.getBody());
        verify(userService, times(1)).save(any(UserModel.class));
    }

    @Test
    @DisplayName("Deve atualizar um usuário existente com sucesso")
    void testUpdateUser_Success() {
        Long userId = 1L;
        UserDto userDto = new UserDto();
        UserModel userModel = new UserModel();
        Optional<UserModel> userOptional = Optional.of(userModel);

        when(userService.findById(eq(userId))).thenReturn(userOptional);

        ResponseEntity<Object> response = userController.updateUser(userId, userDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Usuario atualizado com sucesso", response.getBody());
        verify(userService, times(1)).findById(eq(userId));
    }

    @Test
    @DisplayName("Deve retornar 204 quando o usuário para atualizar não for encontrado")
    void testUpdateUser_UserNotFound() {
        Long userId = 1L;
        UserDto userDto = new UserDto();

        when(userService.findById(eq(userId))).thenReturn(Optional.empty());

        ResponseEntity<Object> response = userController.updateUser(userId, userDto);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertEquals("Usuario não encontrado", response.getBody());
        verify(userService, times(1)).findById(eq(userId));
    }

    @Test
    @DisplayName("Deve deletar um usuário existente com sucesso")
    void testDeleteUser_Success() {
        Long userId = 1L;
        UserModel userModel = new UserModel();
        Optional<UserModel> userOptional = Optional.of(userModel);

        when(userService.findById(eq(userId))).thenReturn(userOptional);
        doNothing().when(userService).delete(userModel);

        ResponseEntity<Object> response = userController.deleteUser(userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Usuario deletado com sucesso", response.getBody());
        verify(userService, times(1)).findById(eq(userId));
        verify(userService, times(1)).delete(userModel);
    }

    @Test
    @DisplayName("Deve retornar 204 quando o usuário para deletar não for encontrado")
    void testDeleteUser_UserNotFound() {
        Long userId = 1L;

        when(userService.findById(eq(userId))).thenReturn(Optional.empty());

        ResponseEntity<Object> response = userController.deleteUser(userId);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertEquals("Usuario não encontrado", response.getBody());
        verify(userService, times(1)).findById(eq(userId));
    }
}

