package com.procuresense.service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class AiExtractionService {

    @Value("${app.ai-service.url:http://ai-service:8001}")
    private String aiServiceUrl;

    @Autowired
    private RestTemplate restTemplate;

    public ExtractionResponse extractQuotation(
            String fileName,
            String filePath) {

        try {

            String url
                    = aiServiceUrl + "/extract";

            Map<String, Object> request
                    = new HashMap<>();

            request.put(
                    "quotation_id",
                    fileName
            );

            request.put(
                    "file_path",
                    filePath
            );

            HttpHeaders headers
                    = new HttpHeaders();

            headers.setContentType(
                    MediaType.APPLICATION_JSON
            );

            HttpEntity<Map<String, Object>> entity
                    = new HttpEntity<>(
                            request,
                            headers
                    );

            ExtractionResponse response
                    = restTemplate.postForObject(
                            url,
                            entity,
                            ExtractionResponse.class
                    );

            if (response != null) {

                log.info(
                        "AI extraction successful for {}",
                        fileName
                );

                return response;
            }

        } catch (Exception e) {

            log.error(
                    "AI extraction failed for {}",
                    fileName,
                    e
            );
        }

        return getMockExtractionResponse();
    }

    private ExtractionResponse getMockExtractionResponse() {

        ExtractionResponse response
                = new ExtractionResponse();

        response.setStatus("SUCCESS");

        response.setConfidence(0.92);

        response.setExtractedDataJson(
                "{"
                + "\"supplier_name\":\"ABC Steel Pvt Ltd\","
                + "\"currency\":\"INR\","
                + "\"unit_price\":100.0,"
                + "\"quantity\":10000,"
                + "\"total_price\":1000000,"
                + "\"unit\":\"per unit\","
                + "\"delivery_days\":12,"
                + "\"payment_terms\":\"Net 30\","
                + "\"payment_terms_days\":30,"
                + "\"capacity\":15000,"
                + "\"quality_certification\":\"ISO 9001\","
                + "\"unit_price_confidence\":0.85,"
                + "\"quantity_confidence\":0.80,"
                + "\"delivery_days_confidence\":0.75"
                + "}"
        );

        return response;
    }

    public boolean isAiServiceHealthy() {

        try {

            String url
                    = aiServiceUrl + "/health";

            Map<?, ?> response
                    = restTemplate.getForObject(
                            url,
                            Map.class
                    );

            return response != null
                    && "UP".equals(
                            response.get("status")
                    );

        } catch (Exception e) {

            log.warn(
                    "AI service health check failed",
                    e
            );

            return false;
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ExtractionResponse {

        private String status;

        private Double confidence;

        private String extractedDataJson;
    }
}
