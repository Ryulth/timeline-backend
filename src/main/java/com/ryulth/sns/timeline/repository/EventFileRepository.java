package com.ryulth.sns.timeline.repository;

import com.ryulth.sns.timeline.entity.EventFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface EventFileRepository extends JpaRepository<EventFile, Long> {

    @Transactional
    @Modifying
    @Query("delete from EventFile f where f.id=?1")
    void deleteAllByEventId(Long eventId);

}
