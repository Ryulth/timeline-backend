package com.ryulth.timeline.account.repository;

import com.ryulth.timeline.account.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByEmail(String email);
    Boolean existsByEmail(String email);
    List<User> findAllByState(String state);
}
