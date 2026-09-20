# ProcureSense AI - 8-Factor Scoring Formula

## Overview

ProcureSense evaluates suppliers on **8 deterministic business factors** instead of price alone.

**Key Principle:** Same input + Same configuration = Always same score. This is NOT AI-driven scoring. The LLM explains the score but does NOT calculate it.

---

## The 8 Factors

### 1. TOTAL COST (Weight: 20%)

**Definition**: Evaluate total procurement cost, not blindly lowest unit price.

**Inputs**:
- Quotation total price
- Budget availability
- Payment terms impact
- Incoterms (FOB vs CIF)

**Formula**:
```
costScore = ((MAX_COST - supplier_cost) / (MAX_COST - MIN_COST)) × 100
```

**Scoring Logic**:
- Well under budget (≤80% of budget) → 100
- Within budget (≤100%) → 85
- Slightly over (≤120%) → 60
- Significantly over (>120%) → 30

**Example**:
```
Budget: ₹1,200,000
Supplier A: ₹1,000,000 (83% of budget) → Score: 100 ✓
Supplier B: ₹1,100,000 (92% of budget) → Score: 85
Supplier C: ₹1,300,000 (108% of budget) → Score: 60 ✗
```

---

### 2. QUALITY (Weight: 15%)

**Definition**: Supplier quality/performance based on historical or certification data.

**Inputs**:
- Certifications (ISO 9001, ISO 14001, etc.)
- Historical defect rate
- Quality metrics from supplier database
- Quotation quality claims

**Scoring Logic**:
```
IF has strong certification + low defect history:
  qualityScore = 90-100
ELSE IF has basic certification:
  qualityScore = 75-89
ELSE IF no certification OR poor history:
  qualityScore = 0-59
ELSE IF no data available:
  qualityScore = 75 (neutral, marked for review)
```

**Example**:
```
Supplier A: ISO 9001, 2% defect rate → 90
Supplier B: ISO 9001 + ISO 14001, 1% defect rate → 98 ✓
Supplier C: No certification, unknown quality → 75 (requires review)
```

---

### 3. DELIVERY (Weight: 15%)

**Definition**: Can supplier meet required delivery date/time?

**Context-Aware**: If current inventory is low, delivery becomes MORE critical.

**Inputs**:
- Required delivery date (from purchase requirement)
- Quoted delivery days (from quotation)
- Current inventory days
- Safety margin requirement

**Formula**:
```
quoted_delivery_date = today + quoted_delivery_days
days_margin = required_date - quoted_delivery_date

IF days_margin >= 2:
  deliveryScore = 100 (safe margin)
ELSE IF days_margin >= 0:
  deliveryScore = 80 (tight but OK)
ELSE:
  deliveryScore = 0 (FAILED - cannot meet deadline)
```

**Example** (Required: 5 days, Inventory: 3 days):
```
Supplier A: 12-day delivery → deadline miss by 7 days → Score: 0 ✗ FAILED
Supplier B: 4-day delivery → 1 day safety margin → Score: 80
Supplier C: 3-day delivery → no margin → Score: 80
```

**Important**: Delivery failure (score 0) means supplier cannot fulfill requirement.

---

### 4. RELIABILITY (Weight: 15%)

**Definition**: Historical consistency - does supplier deliver as promised?

**NOT to be confused with**:
- Delivery ability (Factor 3): Can they meet THIS date?
- Dependency (Factor 8): How much do WE rely on them?

**Inputs**:
- Historical on-time delivery percentage
- Past order fulfillment records
- Late delivery frequency
- Consistent performance track record

**Formula**:
```
reliabilityScore = on_time_delivery_percentage

Example:
95% on-time record → Score: 95
80% on-time record → Score: 80
60% on-time record → Score: 60
No data → Score: 70 (neutral)
```

**Example**:
```
Supplier A: 85% on-time history → Score: 85
Supplier B: 96% on-time history → Score: 96 ✓ Strong
Supplier C: 72% on-time history → Score: 72
```

---

### 5. CAPACITY (Weight: 10%)

**Definition**: Does supplier have sufficient production capacity for requested quantity?

**Inputs**:
- Requested quantity
- Supplier quoted/available capacity
- Historical max orders
- Production constraints

**Formula**:
```
IF capacity >= requested_quantity:
  capacityScore = 100
ELSE IF capacity >= 80% of requested:
  capacityScore = 85
ELSE IF capacity >= 50% of requested:
  capacityScore = 60
ELSE:
  capacityScore = 0 (CANNOT FULFILL)
```

**Example** (Required: 10,000 units):
```
Supplier A: 15,000 capacity → Score: 100 ✓
Supplier B: 12,000 capacity → Score: 100 ✓
Supplier C: 8,000 capacity (80% of 10,000) → Score: 85
Supplier D: 4,000 capacity → Score: 0 ✗ Cannot fulfill
```

---

### 6. PAYMENT TERMS (Weight: 10%)

**Definition**: Evaluate payment conditions and business cash flow impact.

**Inputs**:
- Payment term (Net 30, Net 60, Advance 50%, etc.)
- Days to payment
- Cash flow requirements
- Payment risk

**Formula**:
```
Preferred: Net 30-60 → 100 (working capital friendly)
Good: Net 15-29 → 90
Neutral: Advance 50% / Mixed → 75
Difficult: Advance 100% (full prepayment) → 50
High Risk: Non-standard, COD → 30
```

**Example**:
```
Supplier A: Net 30 → Score: 100 ✓ Ideal
Supplier B: Net 45 → Score: 100 ✓ Ideal
Supplier C: Net 60 → Score: 100 ✓ Ideal (longer float)
Supplier D: Advance 100% → Score: 50 ✗ Requires upfront cash
```

---

### 7. SUPPLIER RISK (Weight: 10%)

**Definition**: Operational, financial, geographic, or strategic risk indicators.

**NOT to be confused with**:
- Delivery risk: Can they deliver on time?
- Dependency: How much do we rely on them?
- Risk here = Supplier might become unavailable/insolvent

**Inputs**:
- Financial health score
- Geopolitical risk (country/region)
- Quality risk indicators
- Operational risk flags
- Compliance/regulatory risk

**Formula**:
```
riskScore = 85 (default)

IF financial_health < 50:
  riskScore -= 30 (high financial risk)
ELSE IF financial_health < 70:
  riskScore -= 15 (moderate financial risk)

IF geopolitical_risk == "HIGH":
  riskScore -= 25
ELSE IF geopolitical_risk == "MEDIUM":
  riskScore -= 10

Result in range: 0-100 (higher is better/lower risk)
```

**Example**:
```
Supplier A: Good financial (85), Low geo-risk → Score: 85 ✓
Supplier B: Excellent financial (95), Low geo-risk → Score: 95 ✓ Strong
Supplier C: Poor financial (40), Medium geo-risk → Score: 40 ✗ Risky
```

---

### 8. SUPPLIER DEPENDENCY (Weight: 5%)

**Definition**: Measure organizational reliance on this supplier. High concentration = single point of failure.

**IMPORTANT**: Higher score = LOWER dependency (less risky). Scoring is inverted.

**NOT to be confused with**:
- Reliability (Factor 4): How consistently they perform
- Risk (Factor 7): Whether they'll stay in business
- Dependency here = How much of our total purchases come from them

**Inputs**:
- Percentage of total purchase volume from this supplier
- Annual value concentration
- Number of alternative suppliers available
- Criticality of supply

**Formula**:
```
IF concentration <= 5%:
  dependencyScore = 100 (no concentration risk)
ELSE IF concentration <= 15%:
  dependencyScore = 85 (low concentration)
ELSE IF concentration <= 30%:
  dependencyScore = 70 (moderate concentration)
ELSE IF concentration <= 50%:
  dependencyScore = 50 (high concentration - risky)
ELSE (> 50%):
  dependencyScore = 20 (critical concentration - very risky)
```

**Example** (Company total steel purchases = ₹10,000,000/year):
```
Supplier A: ₹800K/year (8% concentration) → Score: 85
Supplier B: ₹500K/year (5% concentration) → Score: 100 ✓ Diversified
Supplier C: ₹3,000K/year (30% concentration) → Score: 70 ⚠ Becoming concentrated
```

---

## FINAL WEIGHTED SCORE

After calculating all 8 factor scores, apply weights:

```
FINAL_SCORE = 
  (cost_score × 0.20) +
  (quality_score × 0.15) +
  (delivery_score × 0.15) +
  (reliability_score × 0.15) +
  (capacity_score × 0.10) +
  (payment_score × 0.10) +
  (risk_score × 0.10) +
  (dependency_score × 0.05)

Range: 0-100
Ranking: Highest score = Recommended supplier
```

---

## REAL-WORLD EXAMPLE

**Scenario**: Purchasing 10,000 steel units, 5-day delivery required, ₹1,200,000 budget

### Supplier A (Cheapest)
| Factor | Score | Weight | Contribution |
|--------|-------|--------|--------------|
| Cost | 95 | 20% | 19.0 |
| Quality | 75 | 15% | 11.3 |
| Delivery | **0** | 15% | **0** ← FAILED |
| Reliability | 80 | 15% | 12.0 |
| Capacity | 100 | 10% | 10.0 |
| Payment | 90 | 10% | 9.0 |
| Risk | 85 | 10% | 8.5 |
| Dependency | 75 | 5% | 3.8 |
| **FINAL SCORE** | | | **73.6** |

**Problem**: 12-day delivery when 5-day needed → Cannot fulfill

### Supplier B (Recommended)
| Factor | Score | Weight | Contribution |
|--------|-------|--------|--------------|
| Cost | 78 | 20% | 15.6 |
| Quality | 90 | 15% | 13.5 |
| Delivery | **100** | 15% | **15.0** ← CRITICAL |
| Reliability | 95 | 15% | 14.3 |
| Capacity | 100 | 10% | 10.0 |
| Payment | 85 | 10% | 8.5 |
| Risk | 88 | 10% | 8.8 |
| Dependency | 80 | 5% | 4.0 |
| **FINAL SCORE** | | | **89.7** ✓ |

**Advantage**: Meets delivery requirement with 4-day lead time, strong reliability

### Supplier C (Limited Capacity)
| Factor | Score | Weight | Contribution |
|--------|-------|--------|--------------|
| Cost | 60 | 20% | 12.0 |
| Quality | 80 | 15% | 12.0 |
| Delivery | 80 | 15% | 12.0 |
| Reliability | 75 | 15% | 11.3 |
| Capacity | **60** | 10% | **6.0** ← LIMITED |
| Payment | 75 | 10% | 7.5 |
| Risk | 78 | 10% | 7.8 |
| Dependency | 85 | 5% | 4.25 |
| **FINAL SCORE** | | | **72.8** |

**Problem**: Limited capacity (8,000 units vs 10,000 needed)

---

## RECOMMENDATION

✅ **SUPPLIER B (Score: 89.7)** wins because:
1. ✓ Meets delivery deadline (4 days vs 5 required)
2. ✓ Strong reliability (95% on-time history)
3. ✓ Good quality (ISO certified)
4. ✓ Sufficient capacity
5. ✓ Reasonable payment terms

Supplier A cannot meet deadline (delivery score = 0). Supplier C has insufficient capacity.

The slight price premium for Supplier B (₹105 vs ₹100 per unit) is justified by:
- Risk mitigation (on-time delivery essential given low inventory)
- Capacity assurance (can fulfill entire order)
- Quality consistency (96% on-time history)

---

## CONFIGURATION

All weights are configurable in the database:
- `SCORING_CONFIGURATION` table
- Can be changed by admin users
- Must total 100% (1.0)
- Changes are versioned for audit trail

---

## EDGE CASES

| Scenario | Handling |
|----------|----------|
| Missing data | Use neutral score (75 or middle value) |
| All suppliers same price | Use other factors for differentiation |
| Delivery date in past | Delivery score = 0 (cannot meet) |
| Zero capacity | Capacity score = 0 (cannot fulfill) |
| Multiple failures | Risk flags identify issues |

---

## SYSTEM GUARANTEES

1. ✓ **Deterministic**: Same input = same score always
2. ✓ **Reproducible**: Audit trail shows why score was calculated
3. ✓ **Transparent**: All factors visible to user
4. ✓ **Explainable**: AI explains but doesn't modify scores
5. ✓ **Context-Aware**: Considers business urgency (inventory days)
6. ✓ **Configurable**: Weights can be adjusted for different procurement strategies
7. ✓ **Business-Driven**: Score is LOGIC-driven, not ML/LLM-driven

---

**Scoring Engine: Pure deterministic Java logic with business rule validation**
