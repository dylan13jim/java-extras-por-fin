package com.tcs.retomicroservices2.repository;

import com.tcs.retomicroservices2.entity.Motion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MotionRepository extends JpaRepository<Motion, Long> {
    List<Motion> findByAccountId(Long accountId);
    List<Motion> findByAccountIdOrderByDatemotionDesc(Long accountId);
}