package com.procuresense.config;

import com.procuresense.service.AuthService;
import com.procuresense.service.PurchaseRequestService;
import com.procuresense.service.SupplierService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private AuthService authService;

    @Autowired
    private SupplierService supplierService;

    @Autowired
    private PurchaseRequestService purchaseRequestService;

    @Override
    public void run(String... args) throws Exception {
        log.info("Initializing demo data...");
        
        try {
            // Create demo users
            authService.createDemoUsers();
            
            // Create demo product
            purchaseRequestService.createDemoProduct();
            
            // Create demo suppliers
            supplierService.createDemoSuppliers();
            
            log.info("Demo data initialization completed");
        } catch (Exception e) {
            log.error("Error during data initialization", e);
        }
    }
}
