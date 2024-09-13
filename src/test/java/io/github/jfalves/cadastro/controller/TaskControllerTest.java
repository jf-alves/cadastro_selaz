package io.github.jfalves.cadastro.controller;

import io.github.jfalves.cadastro.dto.TaskDto;
import io.github.jfalves.cadastro.enums.EnumStatus;
import io.github.jfalves.cadastro.model.TaskModel;
import io.github.jfalves.cadastro.model.UserModel;
import io.github.jfalves.cadastro.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TaskControllerTest {

    @InjectMocks
    private TaskController taskController;

    @Mock
    private TaskService taskService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Deve retornar todas as tarefas com sucesso")
    void testGetAllTasks_Success() {
        List<TaskModel> mockTasks = List.of(new TaskModel(), new TaskModel());
        when(taskService.findAllTasks()).thenReturn(mockTasks);

        ResponseEntity<List<TaskModel>> response = taskController.getAllTasks();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockTasks, response.getBody());
        verify(taskService, times(1)).findAllTasks();
    }

//    @Test
//    @DisplayName("Deve retornar tarefas filtradas por status")
//    void testFindTasksByStatus_Success() {
//        List<TaskModel> mockTasks = List.of(new TaskModel());
//        when(taskService.findTasksByStatus("COMPLETED")).thenReturn(mockTasks);
//
//        ResponseEntity<List<TaskModel>> response = taskController.findTasksByStatus("COMPLETED");
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals(mockTasks, response.getBody());
//        verify(taskService, times(1)).findTasksByStatus("COMPLETED");
//    }

    @Test
    @DisplayName("Deve ordenar tarefas por data de vencimento com sucesso")
    void testGetAllTaskOrderByDate_Success() {
        Pageable pageable = PageRequest.of(0, 10);
        List<TaskModel> mockTasks = List.of(new TaskModel(), new TaskModel());
        Page<TaskModel> mockPage = new PageImpl<>(mockTasks, pageable, mockTasks.size());

        when(taskService.findAllTaskOrderByDueDate(pageable)).thenReturn(mockPage);

        ResponseEntity<Page<TaskModel>> response = taskController.getAllTaskOrderByDate(pageable);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockPage, response.getBody());
        verify(taskService, times(1)).findAllTaskOrderByDueDate(pageable);
    }

    @Test
    @DisplayName("Deve retornar todas as tarefas de um usuário específico")
    void testGetAllTaskByUser_Success() {
        List<TaskModel> mockTasks = List.of(new TaskModel());
        Long userId = 1L;
        when(taskService.findAllTasksByUser(userId)).thenReturn(mockTasks);

        ResponseEntity<List<TaskModel>> response = taskController.getAllTaskByUser(userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockTasks, response.getBody());
        verify(taskService, times(1)).findAllTasksByUser(userId);
    }

    @Test
    @DisplayName("Deve criar uma nova tarefa com sucesso")
    void testCreateTask_Success() {
        EnumStatus status = EnumStatus.EM_ANDAMENTO;
        UserModel user = new UserModel();
        TaskDto taskDto = new TaskDto("Nova Tarefa", "Descrição da tarefa", status, user);

        TaskModel taskModel = new TaskModel();
        taskModel.setTitle(taskDto.getTitle());
        taskModel.setDescription(taskDto.getDescription());
        taskModel.setCreatedAt(LocalDateTime.now(ZoneId.of("UTC")));
        taskModel.setDueDate(LocalDateTime.now(ZoneId.of("UTC")));

        when(taskService.findByTitle(taskDto.getTitle())).thenReturn(Optional.empty());
        when(taskService.save(any(TaskModel.class))).thenReturn(taskModel);

        ResponseEntity<Object> response = taskController.createTAsk(taskDto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(taskModel, response.getBody());
        verify(taskService, times(1)).save(any(TaskModel.class));
    }

    @Test
    @DisplayName("Deve atualizar uma tarefa existente com sucesso")
    void testUpdateTask_Success() {
        Long taskId = 1L;

        EnumStatus status = EnumStatus.EM_ANDAMENTO;
        UserModel user = new UserModel();
        user.setUserId(1L);

        TaskDto taskDto = new TaskDto("Ler Livro", "Ler livro de historia", status, user);

        TaskModel taskModel = new TaskModel();
        taskModel.setTaskId(taskId);
        taskModel.setTitle(taskDto.getTitle());
        taskModel.setDescription(taskDto.getDescription());
        taskModel.setCreatedAt(LocalDateTime.now(ZoneId.of("UTC")));
        taskModel.setDueDate(LocalDateTime.now(ZoneId.of("UTC")));

        when(taskService.findByTaskId(taskId)).thenReturn(Optional.of(new TaskModel()));
        when(taskService.save(any(TaskModel.class))).thenReturn(taskModel);

        ResponseEntity<Object> response = taskController.updateTask(taskId, taskDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(taskModel, response.getBody());
        verify(taskService, times(1)).save(any(TaskModel.class));
    }

    @Test
    @DisplayName("Deve deletar uma tarefa existente com sucesso")
    void testDeleteTask_Success() {
        Long taskId = 1L;
        TaskModel taskModel = new TaskModel();
        taskModel.setTaskId(taskId);

        when(taskService.findByTaskId(taskId)).thenReturn(Optional.of(taskModel));
        doNothing().when(taskService).delete(taskModel);

        ResponseEntity<Object> response = taskController.deleteTask(taskId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Usuario deletado com sucesso", response.getBody());
        verify(taskService, times(1)).delete(taskModel);
    }

    @Test
    @DisplayName("Deve retornar erro ao tentar deletar uma tarefa não existente")
    void testDeleteTask_NotFound() {
        Long taskId = 1L;
        when(taskService.findByTaskId(taskId)).thenReturn(Optional.empty());

        ResponseEntity<Object> response = taskController.deleteTask(taskId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Task não encontrada", response.getBody());
        verify(taskService, never()).delete(any(TaskModel.class));
    }
}