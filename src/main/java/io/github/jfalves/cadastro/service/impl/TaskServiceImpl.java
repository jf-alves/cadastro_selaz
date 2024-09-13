package io.github.jfalves.cadastro.service.impl;

import io.github.jfalves.cadastro.enums.EnumStatus;
import io.github.jfalves.cadastro.model.TaskModel;
import io.github.jfalves.cadastro.model.UserModel;
import io.github.jfalves.cadastro.repository.TaskRepository;
import io.github.jfalves.cadastro.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaskServiceImpl implements TaskService {
    @Autowired
    TaskRepository repository;

    @Override
    public void delete(TaskModel taskModel) {
        repository.delete(taskModel);
    }

    @Override
    public TaskModel save(TaskModel taskModel) {
        return repository.save(taskModel);
    }

    @Override
    public List<TaskModel> findAllTasks() {
        return repository.findAll();
    }

    @Override
    public Page<TaskModel> findAllTaskOrderByDueDate(Pageable pageable) {
        return repository.findAll(pageable);
    }

    //Todo verify
    @Override
    public Optional<TaskModel> findByTaskStatus(EnumStatus status) {
        return Optional.empty();
    }

    @Override
    public List<TaskModel> findAllTasksByUser(Long userId) {
        return repository.findAllTasksUsers(userId);
    }

    @Override
    public Optional<TaskModel> findByTitle(String title) {
        return repository.findByTitle(title);
    }

    @Override
    public Optional<TaskModel> findByTaskId(Long taskId) {
        return repository.findById(taskId);
    }
}
