"""
ProcureSense AI Service
Google Gemini-based quotation extraction and explanation generation
"""

import os
import json
import logging
from typing import Optional, Dict, Any
from contextlib import asynccontextmanager

from fastapi import FastAPI, HTTPException
from pydantic import BaseModel
import google.generativeai as genai

# ==================== CONFIGURATION ====================

logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)

# Initialize Gemini API
GEMINI_API_KEY = os.getenv("GEMINI_API_KEY", "")
if GEMINI_API_KEY:
    genai.configure(api_key=GEMINI_API_KEY)
else:
    logger.warning("GEMINI_API_KEY not set, using mock responses")

# ==================== DATA MODELS ====================

class ExtractionRequest(BaseModel):
    quotation_id: str
    file_path: str
    file_content_base64: Optional[str] = None
    supplier_name_hint: Optional[str] = None
    purchase_context: Optional[Dict[str, Any]] = None

class ExtractionResponse(BaseModel):
    quotation_id: str
    status: str
    extraction_confidence: float
    extracted_data: Dict[str, Any]
    field_confidence: Dict[str, float]
    warnings: list = []
    extraction_time_ms: int

class HealthResponse(BaseModel):
    status: str
    ai_model: str
    uptime_ms: int

# ==================== QUOTATION EXTRACTION ====================

def extract_quotation_with_gemini(file_path: str, supplier_hint: Optional[str] = None) -> Dict[str, Any]:
    """
    Use Google Gemini to extract quotation data from PDF
    """
    try:
        if not GEMINI_API_KEY:
            logger.warning("Using mock extraction - no Gemini API key")
            return get_mock_extraction()
        
        # In production, would read PDF from file_path and extract using Gemini
        # For MVP, using mock data
        model = genai.GenerativeModel("gemini-pro")
        
        prompt = f"""
        Extract quotation information from the provided document.
        
        Return ONLY valid JSON with these fields:
        - supplier_name: string
        - currency: string (3-letter code, e.g., INR, USD)
        - unit_price: number
        - quantity: number
        - total_price: number
        - unit: string (e.g., "per unit", "per 100 units")
        - delivery_days: number
        - payment_terms: string (e.g., "Net 30")
        - payment_terms_days: number
        - capacity: number (supplier capacity)
        - quality_certification: string
        - incoterms: string (e.g., FOB, CIF)
        
        Also include confidence scores (0-1) for each field:
        - supplier_name_confidence
        - currency_confidence
        - unit_price_confidence
        - quantity_confidence
        - delivery_days_confidence
        - payment_terms_confidence
        
        Document path: {file_path}
        {f'Supplier hint: {supplier_hint}' if supplier_hint else ''}
        """
        
        # For MVP, return mock (would call model.generate_content in production)
        return get_mock_extraction()
        
    except Exception as e:
        logger.error(f"Error in Gemini extraction: {e}")
        return get_mock_extraction()

def get_mock_extraction() -> Dict[str, Any]:
    """
    Mock quotation extraction for local development
    Demonstrates the expected data structure
    """
    return {
        "supplier_name": "ABC Steel Pvt Ltd",
        "currency": "INR",
        "unit_price": 100.0,
        "quantity": 10000,
        "total_price": 1000000.0,
        "unit": "per unit",
        "delivery_days": 12,
        "delivery_date": "2024-10-27",
        "payment_terms": "Net 30",
        "payment_terms_days": 30,
        "capacity": 15000,
        "quality_certification": "ISO 9001",
        "incoterms": "FOB",
        "notes": "Bulk discount available for orders > 5000 units",
        "supplier_name_confidence": 0.98,
        "currency_confidence": 0.95,
        "unit_price_confidence": 0.89,
        "quantity_confidence": 0.80,
        "delivery_days_confidence": 0.75,
        "payment_terms_confidence": 0.92,
        "capacity_confidence": 0.65
    }

# ==================== FASTAPI APPLICATION ====================

@asynccontextmanager
async def lifespan(app: FastAPI):
    """Application lifespan management"""
    logger.info("AI Service starting...")
    yield
    logger.info("AI Service shutting down...")

app = FastAPI(
    title="ProcureSense AI Service",
    description="AI-powered quotation extraction and explanation generation",
    version="1.0.0",
    lifespan=lifespan
)

# ==================== HEALTH ENDPOINT ====================

import time
start_time = time.time()

@app.get("/health", response_model=HealthResponse)
async def health():
    """Check AI service health"""
    uptime_ms = int((time.time() - start_time) * 1000)
    return HealthResponse(
        status="UP",
        ai_model="gemini-pro",
        uptime_ms=uptime_ms
    )

# ==================== QUOTATION EXTRACTION ENDPOINT ====================

@app.post("/extract", response_model=ExtractionResponse)
async def extract_quotation(request: ExtractionRequest):
    """
    Extract quotation data from uploaded PDF using Gemini LLM
    
    Returns structured quotation information with confidence scores
    """
    import time
    start = time.time()
    
    logger.info(f"Extracting quotation: {request.quotation_id}")
    
    try:
        # Extract data using Gemini
        extracted = extract_quotation_with_gemini(
            request.file_path,
            request.supplier_name_hint
        )
        
        # Calculate overall confidence
        confidence_scores = {
            k: v for k, v in extracted.items() 
            if k.endswith("_confidence")
        }
        
        avg_confidence = (
            sum(confidence_scores.values()) / len(confidence_scores)
            if confidence_scores else 0.85
        )
        
        # Extract main data fields
        extracted_data = {
            k: v for k, v in extracted.items()
            if not k.endswith("_confidence")
        }
        
        extraction_time = int((time.time() - start) * 1000)
        
        # Identify low-confidence fields
        warnings = []
        for field, conf in confidence_scores.items():
            if conf < 0.70:
                field_name = field.replace("_confidence", "")
                warnings.append(
                    f"Low confidence on '{field_name}' ({conf:.0%}) - recommend review"
                )
        
        return ExtractionResponse(
            quotation_id=request.quotation_id,
            status="SUCCESS" if avg_confidence >= 0.70 else "PARTIAL",
            extraction_confidence=avg_confidence,
            extracted_data=extracted_data,
            field_confidence=confidence_scores,
            warnings=warnings,
            extraction_time_ms=extraction_time
        )
        
    except Exception as e:
        logger.error(f"Extraction failed: {e}")
        extraction_time = int((time.time() - start) * 1000)
        raise HTTPException(
            status_code=400,
            detail=f"Extraction failed: {str(e)}"
        )

# ==================== EXPLANATION ENDPOINT ====================

class ExplanationRequest(BaseModel):
    recommendation_id: str
    supplier_scores: Dict[str, Any]
    purchase_context: Dict[str, Any]
    risk_factors: list = []

class ExplanationResponse(BaseModel):
    recommendation_id: str
    explanation: str
    alternative_explanation: Optional[str] = None
    key_factors: list
    risk_warnings: list = []

@app.post("/explain", response_model=ExplanationResponse)
async def generate_explanation(request: ExplanationRequest):
    """
    Generate human-readable explanation for recommendation
    
    Explains WHY a supplier was chosen based on business factors
    """
    logger.info(f"Generating explanation for: {request.recommendation_id}")
    
    try:
        preferred = request.supplier_scores.get("preferred", {})
        supplier_name = preferred.get("supplier_name", "Unknown")
        final_score = preferred.get("final_score", 0)
        factors = preferred.get("factor_scores", {})
        
        # Generate explanation using Gemini (or mock for MVP)
        explanation = generate_explanation_text(
            supplier_name,
            final_score,
            factors,
            request.purchase_context,
            request.risk_factors
        )
        
        return ExplanationResponse(
            recommendation_id=request.recommendation_id,
            explanation=explanation,
            key_factors=identify_key_factors(factors),
            risk_warnings=request.risk_factors
        )
        
    except Exception as e:
        logger.error(f"Explanation generation failed: {e}")
        raise HTTPException(
            status_code=400,
            detail=f"Explanation generation failed: {str(e)}"
        )

def generate_explanation_text(
    supplier_name: str,
    final_score: float,
    factors: dict,
    context: dict,
    risk_factors: list
) -> str:
    """
    Generate explanation text for recommendation
    """
    inventory_days = context.get("inventory_days", 0)
    required_days = context.get("required_delivery_days", 0)
    delivery_score = factors.get("delivery", 0)
    reliability_score = factors.get("reliability", 0)
    cost_score = factors.get("cost", 0)
    
    explanation = (
        f"{supplier_name} is recommended with a score of {final_score:.1f}/100 because: "
        f"Strong delivery capability ({delivery_score:.0f}/100) and excellent reliability "
        f"({reliability_score:.0f}/100). "
    )
    
    if inventory_days < required_days:
        explanation += (
            f"Given current inventory of {inventory_days} days versus "
            f"{required_days} required delivery days, delivery reliability is critical. "
        )
    
    if cost_score < 80:
        explanation += (
            f"Although the quoted price is not the lowest, the total cost-of-ownership "
            f"is favorable considering the strong on-time delivery record. "
        )
    
    explanation += (
        "This supplier best meets the procurement requirements given the current business context."
    )
    
    return explanation

def identify_key_factors(factors: dict) -> list:
    """
    Identify the top positive factors contributing to recommendation
    """
    factors_list = [
        ("Cost", factors.get("cost", 0)),
        ("Quality", factors.get("quality", 0)),
        ("Delivery", factors.get("delivery", 0)),
        ("Reliability", factors.get("reliability", 0)),
        ("Capacity", factors.get("capacity", 0)),
    ]
    
    # Sort by score and return top 3
    factors_list.sort(key=lambda x: x[1], reverse=True)
    return [
        f"{name} is strong ({score:.0f}/100)"
        for name, score in factors_list[:3]
    ]

# ==================== STARTUP EVENT ====================

@app.on_event("startup")
async def startup_event():
    logger.info("ProcureSense AI Service initialized")
    logger.info(f"Gemini API configured: {'Yes' if GEMINI_API_KEY else 'No (using mock)'}")

if __name__ == "__main__":
    import uvicorn
    uvicorn.run(app, host="0.0.0.0", port=8001)
