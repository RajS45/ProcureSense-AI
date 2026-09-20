package com.procuresense.repository;

import com.procuresense.model.PurchaseRequest;
import com.procuresense.model.Recommendation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RecommendationRepository extends JpaRepository<Recommendation, Long> {
    Optional<Recommendation> findByPurchaseRequest(PurchaseRequest request);
}
