package com.procuresense.repository;

import com.procuresense.model.PurchaseRequest;
import com.procuresense.model.RequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PurchaseRequestRepository extends JpaRepository<PurchaseRequest, Long> {
    Optional<PurchaseRequest> findByRequestCode(String requestCode);
    List<PurchaseRequest> findByStatus(RequestStatus status);
}
