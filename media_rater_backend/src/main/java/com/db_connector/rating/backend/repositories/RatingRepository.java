package com.db_connector.rating.backend.repositories;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.db_connector.rating.backend.model.Rating;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Integer>{
    List<Rating> findByUserId(int userId);

    List<Rating> findByMediaId(String mediaId);

    Optional<Rating> findByUserIdAndMediaId(int userId, String mediaId);

    @Modifying
    @Transactional
    void deleteByUserId(int userId);
}