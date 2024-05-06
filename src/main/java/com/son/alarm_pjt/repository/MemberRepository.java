package com.son.alarm_pjt.repository;

import com.son.alarm_pjt.domain.Cleaning;
import com.son.alarm_pjt.domain.Member;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends MongoRepository<Member, String> {
    Member findByName(String name);
}
