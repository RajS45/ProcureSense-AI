package com.procuresense.repository;

import com.procuresense.model.PurchaseRequest;
import com.procuresense.model.Quotation;
import com.procuresense.model.QuotationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuotationRepository extends JpaRepository<Quotation, Long> {
    List<Quotation> findByPurchaseRequest(PurchaseRequest request);
    List<Quotation> findByStatus(QuotationStatus status);
}
