package io.github.jfalves.cadastro.service;

import io.github.jfalves.cadastro.enums.EnumStatus;
import io.github.jfalves.cadastro.model.TaskModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface TaskService {

    void delete(TaskModel taskModel);//Deletar uma tarefa.

    TaskModel save(TaskModel taskModel);

    Optional<TaskModel> findByTaskStatus(EnumStatus status);// Filtrar tarefas por status.

    List<TaskModel> findAllTasksByUser(Long userId);

    List<TaskModel>findAllTasks();

    Page<TaskModel> findAllTaskOrderByDueDate(Pageable pageable);

    Optional<TaskModel> findByTitle(String title);

    Optional<TaskModel> findByTaskId(Long taskId);
}
