package com.procuresense.repository;

import com.procuresense.model.Quotation;
import com.procuresense.model.QuotationItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface QuotationItemRepository extends JpaRepository<QuotationItem, Long> {
    Optional<QuotationItem> findByQuotation(Quotation quotation);
}
