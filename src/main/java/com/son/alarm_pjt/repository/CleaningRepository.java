package com.son.alarm_pjt.repository;

import com.son.alarm_pjt.domain.Cleaning;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CleaningRepository extends MongoRepository<Cleaning, String> {
    List<Cleaning> findAllByDateAfter(String date);
}
