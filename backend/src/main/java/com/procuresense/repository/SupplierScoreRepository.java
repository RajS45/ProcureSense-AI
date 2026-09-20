package com.procuresense.repository;

import com.procuresense.model.Quotation;
import com.procuresense.model.PurchaseRequest;
import com.procuresense.model.SupplierScore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SupplierScoreRepository extends JpaRepository<SupplierScore, Long> {
    List<SupplierScore> findByPurchaseRequest(PurchaseRequest request);
    Optional<SupplierScore> findByQuotation(Quotation quotation);
}
