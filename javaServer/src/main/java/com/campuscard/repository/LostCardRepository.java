package com.campuscard.repository;

import com.campuscard.entity.LostCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface LostCardRepository extends JpaRepository<LostCard, String> {
    List<LostCard> findByCardId(String cardId);
    List<LostCard> findByUserId(String userId);
    List<LostCard> findByCardIdAndStatus(String cardId, String status);
} 