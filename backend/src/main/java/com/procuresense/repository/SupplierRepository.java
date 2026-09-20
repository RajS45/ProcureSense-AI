package com.procuresense.repository;

import com.procuresense.model.Supplier;
import com.procuresense.model.SupplierStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SupplierRepository extends JpaRepository<Supplier, Long> {
    Optional<Supplier> findBySupplierId(String supplierId);
    List<Supplier> findByStatus(SupplierStatus status);
}
