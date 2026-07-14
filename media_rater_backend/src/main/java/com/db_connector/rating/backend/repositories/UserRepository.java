package com.db_connector.rating.backend.repositories;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.db_connector.rating.backend.model.AppUser;

@Repository
public interface UserRepository extends JpaRepository<AppUser, Integer>{
    Optional<AppUser> findByUsername(String username);

    boolean existsByUsername(String username);

    @Modifying
    @Transactional
    void deleteByUsername(String username);
}
