package com.ryulth.sns.timeline.repository;

import com.ryulth.sns.timeline.entity.EventFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventFileRepository extends JpaRepository<EventFile, Long> {
}
