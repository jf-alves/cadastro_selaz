package io.github.jfalves.cadastro.controller;


import io.github.jfalves.cadastro.dto.TaskDto;
import io.github.jfalves.cadastro.model.TaskModel;
import io.github.jfalves.cadastro.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tasks")
@CrossOrigin(origins = "*", maxAge = 3600)
public class TaskController {

    @Autowired
    TaskService service;

    @Operation(description = "Listar todas as tarefas do usuário autenticado")
    @GetMapping
    public ResponseEntity<List<TaskModel>> getAllTasks() {
        return ResponseEntity.status(HttpStatus.OK).body(service.findAllTasks());
    }

    @Operation(description = "Filtrar tarefas por status.")
    @GetMapping//GET /api/tasks?status={status}: Filtrar tarefas por status.
    public ResponseEntity<List<TaskModel>> findTasksByStatus(@RequestParam(required = false) String status) {

        return null;
    }

    @Operation(description = "Ordenar tarefas por data de vencimento.")
    @GetMapping
    public ResponseEntity<Page<TaskModel>> getAllTaskOrderByDate(@PageableDefault(page = 0, size = 10, sort = "dueDate",
            direction = Sort.Direction.ASC) Pageable pePageable) {
        return ResponseEntity.status(HttpStatus.OK).body(service.findAllTaskOrderByDueDate(pePageable));
    }

    @Operation(description = "Listar todas as tarefas de um usuário específico.")
    @GetMapping("/{userId}/tasks")
    public ResponseEntity<List<TaskModel>> getAllTaskByUser(@PathVariable(value = "userId") Long userId) {
        return ResponseEntity.status(HttpStatus.OK).body(service.findAllTasksByUser(userId));
    }

    @Operation(description = "Criar uma nova tarefa.")
    @PostMapping
    public ResponseEntity<Object> createTAsk(@RequestBody @Valid TaskDto taskDto) {
        Optional<TaskModel> optionalTaskModel = service.findByTitle(taskDto.getTitle());
        if (optionalTaskModel.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(optionalTaskModel.get());
        }

        var taskModel = new TaskModel();
        BeanUtils.copyProperties(taskDto, taskModel);
        taskModel.setCreatedAt(LocalDateTime.now(ZoneId.of("UTC")));
        taskModel.setDueDate(LocalDateTime.now(ZoneId.of("UTC")));

        service.save(taskModel);

        return ResponseEntity.status(HttpStatus.CREATED).body(taskModel);
    }

    @Operation(description = "Atualizar uma tarefa existente.")
    @PutMapping("/{id}")
    public ResponseEntity<Object> updateTask(@PathVariable(value = "taskId") Long taskId,
                                             @RequestBody @Valid TaskDto taskDto) {
        Optional<TaskModel> optionalTaskModel = service.findByTaskId(taskId);
        if (optionalTaskModel.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Task já cadastrada");
        }
        var taskModel = new TaskModel();
        BeanUtils.copyProperties(taskDto, taskModel);
        taskModel.setCreatedAt(LocalDateTime.now(ZoneId.of("UTC")));
        taskModel.setDueDate(LocalDateTime.now(ZoneId.of("UTC")));

        return ResponseEntity.status(HttpStatus.OK).body(service.save(taskModel));
    }

    @Operation(description = "Deletar uma tarefa existente")
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteTask(@PathVariable(value = "taskId") Long taskId) {
        Optional<TaskModel> optionalTaskModel = service.findByTaskId(taskId);
        if (optionalTaskModel.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Task não encontrada");
        }
        service.delete((optionalTaskModel.get()));
        return ResponseEntity.status(HttpStatus.OK).body("Usuario deletado com sucesso");
    }

}
