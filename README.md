# ProcureSense AI - Intelligent Procurement Decision Support

**Enterprise procurement platform combining AI-powered document processing with deterministic 8-factor supplier evaluation.**

⚡ **24-Hour MVP** | 🔄 **Multi-Factor Scoring** | 🤖 **AI-Powered Extraction** | ✅ **Human-in-Loop Approval**

---

## 🚀 QUICK START (5 MINUTES)

### Prerequisites
- Docker & Docker Compose installed
- Google Gemini API Key (free tier available at [Google AI Studio](https://makersuite.google.com/app/apikey))
- 4GB free disk space

### 1. Clone & Setup
```bash
cd procuresense-ai

# Copy environment template
cp .env.example .env

# Add your Google Gemini API key to .env
# Edit .env and set: GEMINI_API_KEY=your_key_here
```

### 2. Start Application (Single Command)
```bash
docker-compose up --build
```

This will:
- ✅ Start PostgreSQL database
- ✅ Run Spring Boot backend (port 8080)
- ✅ Run Python AI service (port 8001)
- ✅ Build & run React frontend (port 3000)
- ✅ Load demo data automatically

**Wait 60-90 seconds for all services to be healthy.**

### 3. Access Application
- **Frontend**: http://localhost:3000
- **Backend API**: http://localhost:8080/api/v1
- **AI Service**: http://localhost:8001

### 4. Login
```
Email: analyst@procuresense.com
Password: demo123
```

---

## 📋 DEMO SCENARIO (Complete in 5 minutes)

This MVP demonstrates a complete procurement workflow:

**Scenario**: Purchase 10,000 steel units with 5-day delivery requirement

**Suppliers**:
- **Supplier A**: Lowest price (₹100/unit), but 12-day delivery ❌ Too slow
- **Supplier B**: Medium price (₹105/unit), 4-day delivery ✅ RECOMMENDED
- **Supplier C**: Higher price (₹110/unit), 3-day delivery (but limited capacity)

**8-Factor Scoring**:
```
Supplier A: Cost=95 | Quality=75 | Delivery=0 | Reliability=80 | ... → Final: 62 (RANKED #2)
Supplier B: Cost=85 | Quality=90 | Delivery=100 | Reliability=95 | ... → Final: 89 (RANKED #1) ✅
Supplier C: Cost=70 | Quality=80 | Delivery=95 | Reliability=75 | Capacity=60 | ... → Final: 76 (RANKED #3)
```

**Why B is recommended (not the cheapest)**:
- Inventory days: 3 | Required delivery: 5 days
- Delivery is CRITICAL → heavily weighted
- Supplier B meets deadline with safety margin (4 days vs 5 required)
- Strong reliability (96% on-time history)
- Better quality certification

**Walk-through Steps**:
1. ✅ Login to dashboard
2. ✅ Create purchase requirement (10,000 units, 5-day delivery)
3. ✅ Upload supplier quotations (3 PDF files)
4. ✅ AI extracts quotation data automatically
5. ✅ System calculates 8-factor scores
6. ✅ View supplier comparison table
7. ✅ See recommendation with explanation
8. ✅ Manager approves recommendation
9. ✅ SAP integration status shown
10. ✅ Demo complete!

---

## 🏗️ ARCHITECTURE

```
React Frontend (Port 3000)
    ↓
Spring Boot Backend (Port 8080)
    ↓ ┌─────────────────────┬─────────────────┐
    │                       │                 │
PostgreSQL DB       Python AI Service    Mock SAP
(Port 5432)        (Port 8001 - Gemini)   Services
```

### Key Features (24-hr MVP)
- ✅ **Purchase Requirement Management** - Create & track procurement requests
- ✅ **Quotation Upload** - PDF document upload & storage
- ✅ **AI-Powered Extraction** - Google Gemini extracts quotation fields
- ✅ **8-Factor Deterministic Scoring** - Cost, Quality, Delivery, Reliability, Capacity, Payment, Risk, Dependency
- ✅ **Supplier Comparison** - Visual ranking table
- ✅ **AI Explanation** - Why was this supplier recommended?
- ✅ **Risk Analysis** - Identify delivery/capacity risks
- ✅ **Alternative Scenarios** - What if preferred supplier unavailable?
- ✅ **Human Approval Workflow** - Manager review & approval
- ✅ **Role-Based Access** - Analyst, Manager, Admin roles
- ✅ **JWT Authentication** - Secure API access
- ✅ **Mock SAP Integration** - Ready for real SAP connectivity

---

## 🎯 BUSINESS LOGIC

### The Problem
Procurement teams often select suppliers based on **price alone**, missing:
- Delivery timeline risks
- Quality inconsistencies
- Supplier reliability
- Capacity constraints
- Dependency concentration

### The Solution
**ProcureSense** evaluates suppliers on **8 business factors**:

| Factor | Weight | Meaning |
|--------|--------|---------|
| Total Cost | 20% | Evaluate total procurement cost (not just unit price) |
| Quality | 15% | Historical quality/certifications |
| Delivery | 15% | Can supplier meet required delivery date? |
| Reliability | 15% | Consistent on-time delivery history |
| Capacity | 10% | Sufficient production capacity? |
| Payment Terms | 10% | Business impact of payment conditions |
| Supplier Risk | 10% | Financial/operational/geographic risks |
| Dependency | 5% | Avoid over-concentration with single supplier |

**All factors are CONFIGURED, NOT hard-coded.**

### Scoring is DETERMINISTIC
```
Final Score = Σ(Factor_Score × Weight)

Same Input + Same Configuration → Always Same Score

This is NOT an AI decision.
AI explains the decision, but does NOT make it.
```

---

## 📁 PROJECT STRUCTURE

```
procuresense-ai/
├── backend/                    # Spring Boot (Java)
│   ├── src/
│   │   ├── main/java/com/procuresense/
│   │   │   ├── controller/     # REST endpoints
│   │   │   ├── service/        # Business logic
│   │   │   ├── repository/     # Database access
│   │   │   ├── model/          # Domain objects
│   │   │   ├── dto/            # Data transfer objects
│   │   │   ├── scoring/        # 8-factor scoring engine
│   │   │   └── config/         # Spring configuration
│   │   └── resources/          # Application config
│   ├── pom.xml                 # Maven dependencies
│   └── Dockerfile
│
├── frontend/                   # React
│   ├── src/
│   │   ├── pages/              # 5 main pages
│   │   ├── components/         # React components
│   │   ├── api/                # API client
│   │   ├── redux/              # State management
│   │   └── App.js
│   ├── package.json
│   └── Dockerfile
│
├── ai-service/                 # Python FastAPI
│   ├── main.py                 # FastAPI app
│   ├── routes/                 # /extract, /health endpoints
│   ├── services/               # LLM extraction logic
│   ├── requirements.txt
│   └── Dockerfile
│
├── database/                   # Database schema
│   ├── migrations/             # SQL migration scripts
│   └── seed-data.sql           # Demo data
│
└── docker-compose.yml          # Orchestrate all services
```

---

## 📊 8-FACTOR SCORING FORMULAS

### Factor 1: Total Cost (20%)
```
costScore = ((MAX_COST - supplier_cost) / (MAX_COST - MIN_COST)) × 100

Logic: Lower cost → Higher score (inverse)
Example: If costs are ₹100K, ₹105K, ₹110K
  Supplier at ₹100K = 100 (best)
  Supplier at ₹105K = 50 (middle)
  Supplier at ₹110K = 0 (worst)
```

### Factor 2: Quality (15%)
```
qualityScore = (quality_certifications + historical_quality_metrics) / 2

Scale: 0-100 (higher is better)
Example: ISO 9001 + 98% defect-free history = 99
```

### Factor 3: Delivery (15%)
```
IF (required_date - quoted_date) >= 2 days:
  deliveryScore = 100 (safe margin)
ELSE IF (required_date - quoted_date) >= 0:
  deliveryScore = 80 (tight but OK)
ELSE:
  deliveryScore = 0 (CANNOT MEET - failed)

Context-aware: If inventory is low, delivery becomes MORE critical
```

### Factor 4: Reliability (15%)
```
reliabilityScore = historical_ontime_delivery_percentage

Logic: If supplier delivered on-time 95% of the time → score = 95
```

### Factor 5: Capacity (10%)
```
IF supplier_capacity >= requested_quantity:
  capacityScore = 100
ELSE IF supplier_capacity >= 80% of quantity:
  capacityScore = 85
ELSE:
  capacityScore = 0 (CANNOT FULFILL)
```

### Factor 6: Payment Terms (10%)
```
Net 30-60 days → 100 (preferred)
Net 15-29 days → 90 (good)
Advance 50% → 75 (acceptable)
Advance 100% → 30 (difficult)
```

### Factor 7: Supplier Risk (10%)
```
Risk combines: financial health, geopolitical, quality issues
riskScore = (1 - aggregate_risk_level) × 100

Higher score = Lower risk
```

### Factor 8: Dependency (5%)
```
IF supplier_concentration <= 5%:
  dependencyScore = 100 (no concern)
ELSE IF concentration <= 30%:
  dependencyScore = 70
ELSE:
  dependencyScore = 20 (critical concentration)
```

### FINAL SCORE
```
Final = (cost × 0.20) + (quality × 0.15) + (delivery × 0.15) 
      + (reliability × 0.15) + (capacity × 0.10) 
      + (payment × 0.10) + (risk × 0.10) + (dependency × 0.05)

Range: 0-100
Ranking: Highest score = Recommended supplier
```

---

## 🔌 API ENDPOINTS

### Authentication
```
POST /api/v1/auth/login
  Body: { email, password }
  Returns: { token, user }
```

### Purchase Requirements
```
POST /api/v1/purchase-requirements
  Create new requirement

GET /api/v1/purchase-requirements/{id}
  Retrieve requirement
```

### Quotations
```
POST /api/v1/quotations/upload
  Upload quotation PDF(s)
  
GET /api/v1/quotations/{id}
  Get quotation with extracted data
```

### Evaluation
```
POST /api/v1/evaluations
  Trigger scoring for a requirement
  
GET /api/v1/evaluations/{id}/scores
  Get all supplier scores
```

### Recommendations
```
GET /api/v1/recommendations/{requestId}
  Get recommendation & AI explanation
  
POST /api/v1/recommendations/{requestId}/approve
  Manager approves recommendation
  
POST /api/v1/recommendations/{requestId}/alternative-scenario
  Simulate preferred supplier disruption
```

---

## 🤖 AI SERVICE (Google Gemini)

The Python AI service uses Google Gemini to:

1. **Extract quotation data** from PDFs
   - Supplier name, price, quantity, delivery date
   - Payment terms, capacity, certifications
   - Returns structured JSON

2. **Generate explanations**
   - Why was Supplier B recommended?
   - Risk factors identified
   - Context-specific reasoning

**Important**: AI does NOT determine the score. It extracts data and explains decisions made by deterministic business logic.

---

## 🔐 Security

- **JWT Authentication** - Secure token-based API access
- **Role-Based Authorization** - Analyst, Manager, Admin
- **Password Hashing** - Bcrypt
- **CORS Configured** - Frontend ↔ Backend communication
- **Environment Secrets** - Never commit API keys

**Demo Credentials**:
```
Analyst: analyst@procuresense.com / demo123
Manager: manager@procuresense.com / demo123
Admin: admin@procuresense.com / demo123
```

---

## 🧪 TESTING

### Run Tests
```bash
# Backend unit tests
cd backend
./mvnw test

# Frontend tests
cd frontend
npm test

# AI service tests
cd ai-service
pytest
```

### Manual Testing
API endpoints can be tested with Postman or curl:
```bash
# Get health status
curl http://localhost:8080/api/v1/health

# Login
curl -X POST http://localhost:8080/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"analyst@procuresense.com","password":"demo123"}'
```

---

## 🐛 TROUBLESHOOTING

### Services not starting?
```bash
# Check logs
docker-compose logs -f backend
docker-compose logs -f ai-service
docker-compose logs -f frontend
```

### Database not connecting?
```bash
# Restart database
docker-compose down
docker volume prune  # Warning: removes data
docker-compose up --build
```

### Gemini API key issues?
- Verify key in .env file
- Get free key at https://makersuite.google.com/app/apikey
- Test: `curl http://localhost:8001/health`

### Port conflicts?
If ports 3000, 8080, 8001 are in use:
```bash
# Edit docker-compose.yml ports section
# Change "3000:80" to "3001:80" etc.
```

---

## 📈 FUTURE ENHANCEMENTS (Post-MVP)

- ✅ Real SAP integration (BAPIs, OData)
- ✅ Multi-level approval workflow
- ✅ Context-aware factor weighting
- ✅ Historical supplier scorecards
- ✅ Alerts for supplier risk changes
- ✅ Integration with procurement analytics
- ✅ Advanced reporting & dashboards
- ✅ Batch evaluation of multiple requirements

---

## 📄 LICENSE

Proprietary - ProcureSense AI

---

## 📞 SUPPORT

For issues or questions:
1. Check logs: `docker-compose logs -f`
2. Verify Gemini API key
3. Ensure Docker Compose is up-to-date
4. Check network connectivity

---


