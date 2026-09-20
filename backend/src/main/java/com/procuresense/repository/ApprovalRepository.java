package com.procuresense.repository;

import com.procuresense.model.Approval;
import com.procuresense.model.Recommendation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApprovalRepository extends JpaRepository<Approval, Long> {
    List<Approval> findByRecommendation(Recommendation recommendation);
}
