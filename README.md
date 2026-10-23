<h1 align="left">🤖 ProcureSense AI — Intelligent Procurement Decision Support</h1>

<p align="center">
  <img src="https://img.shields.io/badge/JAVA-21-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white" alt="Java 21">
  <img src="https://img.shields.io/badge/SPRING_BOOT-BACKEND-6DB33F?style=for-the-badge&logo=springboot&logoColor=white" alt="Spring Boot Backend">
  <img src="https://img.shields.io/badge/PYTHON-FASTAPI-009688?style=for-the-badge&logo=fastapi&logoColor=white" alt="Python FastAPI">
  <img src="https://img.shields.io/badge/GOOGLE_GEMINI-AI_EXTRACTION-4285F4?style=for-the-badge&logo=googlegemini&logoColor=white" alt="Google Gemini">
</p>

<p align="center">
  <img src="https://img.shields.io/badge/POSTGRESQL-DATABASE-4169E1?style=for-the-badge&logo=postgresql&logoColor=white" alt="PostgreSQL">
  <img src="https://img.shields.io/badge/JWT-AUTHENTICATION-000000?style=for-the-badge&logo=jsonwebtokens&logoColor=white" alt="JWT Authentication">
  <img src="https://img.shields.io/badge/DOCKER-CONTAINERIZED-2496ED?style=for-the-badge&logo=docker&logoColor=white" alt="Docker">
  <img src="https://img.shields.io/badge/PROCUREMENT-AUTOMATION-7B61FF?style=for-the-badge" alt="Procurement Automation">
</p>

<p align="center">
  <b>📦 Turning supplier quotations into smarter, explainable procurement decisions.</b>
</p>

<p align="center">
  <i>Extract quotation data with AI. Compare suppliers with weighted scoring. Keep humans in control of approvals.</i>
</p>

<p align="center">
  <a href="#-about-procuresense-ai">Overview</a> •
  <a href="#-key-features">Features</a> •
  <a href="#-architecture">Architecture</a> •
  <a href="#-technology-stack">Tech Stack</a> •
  <a href="#-quick-start">Setup</a> •
  <a href="#-eight-factor-supplier-evaluation">Scoring</a> •
  <a href="#-api-endpoints">API Docs</a> •
  <a href="#-future-enhancements">Roadmap</a>
</p>

📍 About ProcureSense AI
ProcureSense AI is an intelligent procurement decision-support platform that helps teams compare supplier quotations using AI-assisted document extraction and a deterministic eight-factor scoring model. It is designed to help procurement teams look beyond the lowest price and consider delivery commitments, quality, reliability, capacity, payment terms, supplier risk, and supplier dependency.

The platform combines a Spring Boot backend, a Python FastAPI AI service, Google Gemini, and PostgreSQL to support a structured supplier evaluation workflow. AI helps extract information and explain results; the weighted scoring engine calculates the supplier scores so the evaluation remains consistent and reviewable.
🔄 How ProcureSense AI Works
1. Create a purchase requirement — define the requested quantity and delivery deadline.
2. Upload supplier quotations — provide supplier quotation PDFs for processing.
3. Extract quotation details — the AI service identifies relevant fields and returns structured data.
4. Evaluate suppliers — the scoring engine assesses suppliers across eight weighted factors.
5. Compare recommendations — review supplier rankings, scores, and explanations.
6. Review and approve — a manager reviews the recommendation before approval.
✨ At a Glance
- 🤖 AI-powered extraction from supplier quotation documents
- ⚖️ Eight-factor supplier scoring with explicit weights
- 📊 Supplier comparison and ranking
- 🧠 Explainable recommendations with relevant risks and context
- 👥 Human-in-the-loop approvals and role-based access
- 🔐 JWT authentication for protected API access
- 🔌 Mock SAP integration for demonstrating an integration workflow
🚀 Quick Start
Prerequisites
- Docker and Docker Compose
- Google Gemini API key: Google AI
  Studio
- At least 4 GB of free disk space
1. Clone and configure
cd procuresense-ai
cp .env.example .env
Add your Gemini API key to .env:
GEMINI_API_KEY=your_key_here
2. Start the application
docker-compose up --build
Docker Compose starts the PostgreSQL database, Spring Boot backend,
Python AI service, and React frontend, and loads demo data
automatically. Allow 60--90 seconds for services to become healthy.
3. Open the application
  Service       Local URL
  Frontend      http://localhost:3000
  Backend API   http://localhost:8080/api/v1
  AI service    http://localhost:8001
4. Sign in
Email: analyst@procuresense.com
Password: demo123
These are demo credentials from the project README. Replace demo
credentials and secrets before using any deployment outside a controlled
demo environment.
🧪 Demo Scenario
Goal: Purchase 10,000 steel units with a required delivery timeline
of five days.
  Supplier           Unit price         Delivery Other context   Outcome
  Supplier A               ₹100          12 days Lowest price,   Rank #2
                                                 misses the
                                                 deadline        

  Supplier B               ₹105           4 days Strong          Rank #1 ---
                                                 reliability and Recommended
                                                 quality
                                                 certification   

  Supplier C               ₹110           3 days Limited         Rank #3
                                             capacity        
Example scores
Supplier A: Cost=95 | Quality=75 | Delivery=0 | Reliability=80 | ... → Final: 62 (Rank #2)
Supplier B: Cost=85 | Quality=90 | Delivery=100 | Reliability=95 | ... → Final: 89 (Rank #1)
Supplier C: Cost=70 | Quality=80 | Delivery=95 | Reliability=75 | Capacity=60 | ... → Final: 76 (Rank #3)
Supplier B is recommended even though it is not the cheapest: it meets
the deadline with a one-day safety margin, has strong on-time delivery
history (96%), and has better quality certification.
End-to-end walkthrough
1. Sign in to the dashboard.
2. Create a purchase requirement for 10,000 units with a five-day
   delivery requirement.
3. Upload the three supplier quotation PDFs.
4. Let the AI service extract quotation data.
5. Run the deterministic 8-factor evaluation.
6. Compare supplier scores in the ranking table.
7. Review the recommendation and its explanation.
8. Have a manager approve the recommendation.
9. Review the displayed SAP integration status.
🏗️ Architecture
React Frontend (Port 3000)
          |
          v
Spring Boot Backend (Port 8080)
          |
          +-------------------+-------------------+
          |                   |                   |
          v                   v                   v
    PostgreSQL DB       Python AI Service      Mock SAP
     Port 5432          Port 8001 / Gemini     Services
🧰 Technology Stack
  Layer                   Technology              Responsibility
  Frontend                React                   Procurement dashboard
                                                  and user workflow

  Backend                 Java, Spring Boot,      REST API, business
                          Maven                   rules, authorization

  Database                PostgreSQL              Application data and
                                                  demo data

  AI service              Python, FastAPI, Google Extract quotation data
                          Gemini                  and generate
                                                  explanations

  Integration             Mock SAP services       Demonstrate SAP
                                              integration status
✨ Main Capabilities
- Purchase requirement management
- PDF quotation upload and storage
- Gemini-powered quotation field extraction
- Deterministic supplier scoring across eight factors
- Supplier comparison and ranking
- AI-generated explanations of recommendations
- Delivery and capacity risk analysis
- Alternative supplier scenarios
- Manager review and approval
- Analyst, Manager, and Admin roles
- JWT-protected API access
- Mock SAP integration, intended as a starting point for real
  connectivity
⚖️ Business Logic
🎯 The Problem
Choosing suppliers on price alone can overlook delivery risks, quality
inconsistencies, reliability, capacity constraints, and dependency on a
single supplier.
📊 Eight-Factor Supplier Evaluation
  Factor            Weight What it evaluates
  Total Cost           20% Total procurement cost, not only unit price
  Quality              15% Historical quality and certifications
  Delivery             15% Ability to meet the required delivery date
  Reliability          15% Historical on-time delivery
  Capacity             10% Ability to fulfil the requested quantity
  Payment Terms        10% Business impact of payment conditions
  Supplier Risk        10% Financial, operational, and geographic risks
  Dependency            5% Supplier concentration and dependency

Weights are configurable rather than hard-coded, according to the
source README.
Deterministic scoring
Final Score = Σ(Factor Score × Factor Weight)

Same input + same configuration → same score
AI extracts information from quotations and explains the result; the
scoring engine applies configured business logic to calculate scores. AI
does not make the supplier-selection decision.
Scoring formulas
1. Total Cost --- 20%
costScore = ((MAX_COST - supplier_cost) / (MAX_COST - MIN_COST)) × 100
Lower cost receives a higher score. For example, among costs of ₹100K,
₹105K, and ₹110K, the example scores are 100, 50, and 0 respectively.
2. Quality --- 15%
qualityScore = (quality_certifications + historical_quality_metrics) / 2
Scale: 0--100, where higher is better. The source example gives ISO 9001
certification plus 98% defect-free history a score of 99.
3. Delivery --- 15%
IF (required_date - quoted_date) >= 2 days:
    deliveryScore = 100
ELSE IF (required_date - quoted_date) >= 0:
    deliveryScore = 80
ELSE:
    deliveryScore = 0
A two-day or greater margin receives 100; meeting the deadline with less
margin receives 80; missing the deadline receives 0. Delivery becomes
more critical when inventory is low.
4. Reliability --- 15%
reliabilityScore = historical_ontime_delivery_percentage
For example, a supplier with 95% historical on-time delivery receives
95.
5. Capacity --- 10%
IF supplier_capacity >= requested_quantity:
    capacityScore = 100
ELSE IF supplier_capacity >= 80% of quantity:
    capacityScore = 85
ELSE:
    capacityScore = 0
6. Payment Terms --- 10%
  Payment terms       Example score
  Net 30--60 days               100
  Net 15--29 days                90
  50% advance                    75
  100% advance                   30
7. Supplier Risk --- 10%
riskScore = (1 - aggregate_risk_level) × 100
Risk combines financial health, geopolitical factors, and quality
issues. A higher score means lower risk.
8. Dependency --- 5%
IF supplier_concentration <= 5%:
    dependencyScore = 100
ELSE IF concentration <= 30%:
    dependencyScore = 70
ELSE:
    dependencyScore = 20
Final weighted score
Final =
    (cost × 0.20)
  + (quality × 0.15)
  + (delivery × 0.15)
  + (reliability × 0.15)
  + (capacity × 0.10)
  + (payment × 0.10)
  + (risk × 0.10)
  + (dependency × 0.05)
The stated score range is 0--100. The highest-scoring supplier ranks
first.
📁 Project Structure
procuresense-ai/
├── backend/                    # Spring Boot (Java)
│   ├── src/
│   │   ├── main/java/com/procuresense/
│   │   │   ├── controller/     # REST endpoints
│   │   │   ├── service/        # Business logic
│   │   │   ├── repository/     # Database access
│   │   │   ├── model/          # Domain objects
│   │   │   ├── dto/             # Data transfer objects
│   │   │   ├── scoring/        # 8-factor scoring engine
│   │   │   └── config/         # Spring configuration
│   │   └── resources/          # Application config
│   ├── pom.xml
│   └── Dockerfile
├── frontend/                   # React
│   ├── src/
│   │   ├── pages/              # Main pages
│   │   ├── components/         # React components
│   │   ├── api/                # API client
│   │   ├── redux/              # State management
│   │   └── App.js
│   ├── package.json
│   └── Dockerfile
├── ai-service/                 # Python FastAPI
│   ├── main.py
│   ├── routes/                 # /extract and /health endpoints
│   ├── services/               # LLM extraction logic
│   ├── requirements.txt
│   └── Dockerfile
├── database/
│   ├── migrations/             # SQL migration scripts
│   └── seed-data.sql           # Demo data
└── docker-compose.yml          # Service orchestration
