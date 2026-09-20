package com.procuresense.controller;

import com.procuresense.dto.*;
import com.procuresense.security.JwtTokenProvider;
import com.procuresense.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            AuthResponse response = authService.login(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Login failed", e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}

@Slf4j
@RestController
@RequestMapping("/purchase-requests")
@CrossOrigin(origins = "*")
class PurchaseRequestController {

    @Autowired
    private PurchaseRequestService purchaseRequestService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @PostMapping
    public ResponseEntity<PurchaseRequestDTO> create(
            @RequestBody CreatePurchaseRequestDTO dto,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        Long userId = jwtTokenProvider.getUserIdFromToken(token.replace("Bearer ", ""));
        PurchaseRequestDTO result = purchaseRequestService.createPurchaseRequest(dto, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PurchaseRequestDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(purchaseRequestService.getPurchaseRequest(id));
    }

    @GetMapping
    public ResponseEntity<List<PurchaseRequestDTO>> getAll() {
        return ResponseEntity.ok(purchaseRequestService.getAllPurchaseRequests());
    }
}

@Slf4j
@RestController
@RequestMapping("/quotations")
@CrossOrigin(origins = "*")
class QuotationController {

    @Autowired
    private QuotationService quotationService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @PostMapping("/upload")
    public ResponseEntity<QuotationDTO> upload(
            @RequestParam Long requestId,
            @RequestParam(required = false) Long supplierId,
            @RequestParam String fileName,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        Long userId = jwtTokenProvider.getUserIdFromToken(token.replace("Bearer ", ""));
        QuotationDTO result = quotationService.createQuotation(requestId, supplierId, fileName, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<QuotationDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(quotationService.getQuotation(id));
    }

    @PostMapping("/{id}/extract")
    public ResponseEntity<Void> extract(@PathVariable Long id) {
        quotationService.extractQuotation(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/request/{requestId}")
    public ResponseEntity<List<QuotationDTO>> getForRequest(@PathVariable Long requestId) {
        return ResponseEntity.ok(quotationService.getQuotationsForRequest(requestId));
    }
}

@Slf4j
@RestController
@RequestMapping("/suppliers")
@CrossOrigin(origins = "*")
class SupplierController {

    @Autowired
    private SupplierService supplierService;

    @GetMapping
    public ResponseEntity<List<SupplierDTO>> getAll() {
        return ResponseEntity.ok(supplierService.getAllSuppliers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SupplierDetailDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(supplierService.getSupplierById(id));
    }
}

@Slf4j
@RestController
@RequestMapping("/evaluations")
@CrossOrigin(origins = "*")
class EvaluationController {

    @Autowired
    private ScoringService scoringService;

    @PostMapping
    public ResponseEntity<SupplierComparisonDTO> evaluate(@RequestParam Long requestId) {
        SupplierComparisonDTO result = scoringService.scoreAllSuppliersForRequest(requestId);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{requestId}/scores")
    public ResponseEntity<SupplierComparisonDTO> getScores(@PathVariable Long requestId) {
        SupplierComparisonDTO result = scoringService.scoreAllSuppliersForRequest(requestId);
        return ResponseEntity.ok(result);
    }
}

@Slf4j
@RestController
@RequestMapping("/recommendations")
@CrossOrigin(origins = "*")
class RecommendationController {

    @Autowired
    private RecommendationService recommendationService;

    @Autowired
    private ApprovalService approvalService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @PostMapping
    public ResponseEntity<RecommendationDTO> create(
            @RequestParam Long requestId,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        Long userId = jwtTokenProvider.getUserIdFromToken(token.replace("Bearer ", ""));
        RecommendationDTO result = recommendationService.createRecommendation(requestId, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<RecommendationDTO> getByRequestId(@PathVariable Long requestId) {
        return ResponseEntity.ok(recommendationService.getRecommendation(requestId));
    }

    @PostMapping("/{recommendationId}/approve")
    public ResponseEntity<Void> approve(
            @PathVariable Long recommendationId,
            @RequestBody ApprovalRequestDTO dto,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        Long userId = jwtTokenProvider.getUserIdFromToken(token.replace("Bearer ", ""));
        approvalService.approveRecommendation(recommendationId, userId, dto.getComments());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{recommendationId}/reject")
    public ResponseEntity<Void> reject(
            @PathVariable Long recommendationId,
            @RequestBody ApprovalRequestDTO dto,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        Long userId = jwtTokenProvider.getUserIdFromToken(token.replace("Bearer ", ""));
        approvalService.rejectRecommendation(recommendationId, userId, dto.getComments());
        return ResponseEntity.ok().build();
    }
}

@Slf4j
@RestController
@RequestMapping("/health")
@CrossOrigin(origins = "*")
class HealthController {

    @Autowired
    private AiExtractionService aiExtractionService;

    @GetMapping
    public ResponseEntity<HealthResponse> health() {
        Map<String, String> services = new HashMap<>();
        services.put("backend", "UP");
        services.put("ai-service", aiExtractionService.isAiServiceHealthy() ? "UP" : "DOWN");
        services.put("database", "UP");

        HealthResponse response = new HealthResponse("UP", services);
        return ResponseEntity.ok(response);
    }
}
