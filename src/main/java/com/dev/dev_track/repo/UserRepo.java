package com.dev.dev_track.repo;

import com.dev.dev_track.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<UserEntity,Long> {
     int countByEmail(String email);

    Optional<UserEntity> findByUserId(Long id);
}
