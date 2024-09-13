package io.github.jfalves.cadastro.repository;

import io.github.jfalves.cadastro.model.TaskModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<TaskModel, Long> {

    @Query(value = "select * from tb_tasks where user_user_id = :userId", nativeQuery = true)
    List<TaskModel> findAllTasksUsers(@Param("userId")Long userId);

    Optional<TaskModel> findByTitle(String title);
}
