package com.db_connector.rating.backend.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.db_connector.rating.backend.model.Rating;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Integer>{
    List<Rating> findByUserId(int userId);

    List<Rating> findByMediaId(String mediaId);

    Optional<Rating> findByUserIdAndMediaId(int userId, String mediaId);

    boolean existsByUserIdAndMediaId(int userId, String mediaId);

    @Query("SELECT AVG(r.stars) FROM Rating r WHERE r.mediaId = :mediaId")
    Double findAverageStarsByMediaId(@Param("mediaId") String mediaId);

    @Modifying
    @Query("DELETE FROM Rating r WHERE r.userId = :userId")
    void deleteByUserId(int userId);

    
}