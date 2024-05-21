package com.son.alarm_pjt.repository;

import com.son.alarm_pjt.domain.Cleaning;
import com.son.alarm_pjt.domain.Member;
import com.son.alarm_pjt.domain.Task;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface CleaningRepository extends MongoRepository<Cleaning, String> {

    List<Cleaning> findAllByTaskNameInAndDateAfter(List<String> taskName, String date);

    Optional<Member> findByTaskNameAndMemberNameAndDateAfter(String taskName,String meberName, String date);

    List<Cleaning> findAllByDateAfter(String date);
}
