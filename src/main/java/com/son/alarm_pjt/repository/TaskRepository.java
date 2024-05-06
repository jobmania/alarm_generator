package com.son.alarm_pjt.repository;

import com.son.alarm_pjt.domain.Task;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TaskRepository extends MongoRepository<Task, String> {
    Task findByName(String name);
}
