package com.ryulth.sns.timeline.repository;

import com.ryulth.sns.timeline.entity.Event;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findByAuthorEmail(String authorEmail, Pageable pageable);
    List<Event> findByAuthorEmailIn(List<String> authorEmails,Pageable pageable);

    @Transactional
    @Modifying
    @Query("delete from Event r where r.id=?1")
    void deleteById(Long id);
}
