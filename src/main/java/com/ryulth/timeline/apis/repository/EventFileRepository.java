package com.ryulth.timeline.apis.repository;

import com.ryulth.timeline.apis.entity.EventFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventFileRepository extends JpaRepository<EventFile, Long> {
}
