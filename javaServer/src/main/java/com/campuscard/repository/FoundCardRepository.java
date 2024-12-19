package com.campuscard.repository;

import com.campuscard.entity.FoundCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface FoundCardRepository extends JpaRepository<FoundCard, String> {
    List<FoundCard> findByCardId(String cardId);
    List<FoundCard> findByStatus(String status);
} 