
https://github.com/RajS45/ProcureSense-AI/edit/main/README.md
match this style https://github.com/RajS45/GigsUp for procureSense AI


Worked for 22s
Got it. You want your ProcureSense AI README to follow the same clean, professional structure and formatting as your GigsUp repository, while preserving ProcureSense AI's actual project details.

I've located the README draft we prepared earlier. It includes:

Professional project introduction and feature highlights

Quick Start instructions with Docker Compose

Demo scenario and supplier ranking example

Architecture and technology stack

The eight-factor supplier scoring model and formulas

Project structure and API endpoint tables

AI service, JWT security, testing, troubleshooting, and deployment notes

Future enhancements and license information

Download the formatted README here:

README_ProcureSense_GigsUp_Format.md

Markdown · Ready for GitHub

Download the formatted README

How to apply it
Open ProcureSense AI — Edit README.

Open the downloaded .md file in VS Code or Notepad.

Copy all its contents into GitHub's README editor.

Preview the formatting, then click Commit changes.

Note: I've prepared the formatted file, but haven't changed the GitHub repository itself. The GitHub connection option is available if you'd prefer to connect it and have me help publish the change directly.


README_ProcureSense_GigsUp_Format.md
Document
User attachment
abey aisa de procure sense ai ke liye bhi

Worked for 27s
Haan bhai 😄 ab GigsUp jaisa hi README header banaya hai — centered title, colorful technology badges, bold tagline, navigation links, emoji headings aur professional project introduction, ProcureSense AI ke liye.

Download ProcureSense AI README.md

GitHub par apply karne ke steps
ProcureSense AI README editor kholo.

Downloaded file ko VS Code mein open karo.

Ctrl + A → Ctrl + C karo.

GitHub editor mein purana content replace karke paste karo.

Preview tab mein formatting check karo.

Commit changes par click karo.

Ismein GigsUp wali visual style ke saath tumhare project ke AI extraction, eight-factor supplier scoring, Spring Boot, FastAPI, Gemini, PostgreSQL aur JWT authentication ko highlight kiya hai. Baaki technical documentation bhi included hai.


README_ProcureSense_GigsUp_Format.md
Document

Pasted text(20261023-201536).txt
Document
acha format nhi h bhai ache se bana bhai
Bhai ab samjha 😅 Tumhe sirf content nahi, GigsUp screenshot jaisa proper GitHub README design chahiye tha. Is baar badges ko 2 rows mein group kiya hai, header aur tagline improve ki hai, aur tables ko GitHub-compatible Markdown mein fix kiya hai.

ProcureSense AI — Improved README

Markdown file · Refined header, badges, tables and sections

Download the improved README.md

Ab kya karna hai?
File download karke VS Code mein kholo.

Ctrl + A, phir Ctrl + C.

GitHub README editor mein purana content replace karo.

Preview mein check karke commit kar do.

Is version mein tumhara procurement project content preserve kiya hai aur demo scenario, scoring model aur API tables ko bhi clean kiya hai. GitHub par actual changes abhi commit nahi hue hain.


ProcureSense_AI_README_Improved.md
Document
<div align="center">
🤖 ProcureSense AI — Intelligent Procurement Decision Support
Turning Supplier Quotations into Smarter Procurement Decisions
<p> <img src="https://img.shields.io/badge/JAVA-21-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white" alt="Java 21" /> <img src="https://img.shields.io/badge/SPRING_BOOT-BACKEND-6DB33F?style=for-the-badge&logo=springboot&logoColor=white" alt="Spring Boot Backend" /> <img src="https://img.shields.io/badge/PYTHON-FASTAPI-009688?style=for-the-badge&logo=fastapi&logoColor=white" alt="Python FastAPI" /> <img src="https://img.shields.io/badge/GEMINI-AI_EXTRACTION-4285F4?style=for-the-badge&logo=googlegemini&logoColor=white" alt="Gemini AI Extraction" /> </p> <p> <img src="https://img.shields.io/badge/POSTGRESQL-DATABASE-4169E1?style=for-the-badge&logo=postgresql&logoColor=white" alt="PostgreSQL Database" /> <img src="https://img.shields.io/badge/JWT-AUTHENTICATION-111111?style=for-the-badge&logo=jsonwebtokens&logoColor=white" alt="JWT Authentication" /> <img src="https://img.shields.io/badge/DOCKER-CONTAINERIZED-2496ED?style=for-the-badge&logo=docker&logoColor=white" alt="Docker" /> <img src="https://img.shields.io/badge/SAP-MOCK_INTEGRATION-0070F2?style=for-the-badge&logo=sap&logoColor=white" alt="Mock SAP Integration" /> </p>
📦 AI-Powered Procurement · ⚖️ Multi-Factor Supplier Scoring · 👥 Human-in-the-Loop Approval

Extract quotation data with AI. Compare suppliers fairly. Make explainable, deadline-aware decisions.

Overview • Features • Architecture • Tech Stack • Setup • Scoring • API Docs • Roadmap

</div>
📍 About ProcureSense AI
ProcureSense AI is an intelligent procurement decision-support platform that helps teams compare supplier quotations using AI-assisted document extraction and a deterministic eight-factor scoring model. It is designed to help procurement teams look beyond the lowest price and consider delivery commitments, quality, reliability, capacity, payment terms, supplier risk, and supplier dependency.

The platform combines a Spring Boot backend, a Python FastAPI AI service, Google Gemini, and PostgreSQL to support a structured supplier evaluation workflow. AI helps extract information and explain results; the weighted scoring engine calculates the supplier scores so the evaluation remains consistent and reviewable.

🔄 How ProcureSense AI Works
Create a purchase requirement — define the requested quantity and delivery deadline.
Upload supplier quotations — provide supplier quotation PDFs for processing.
Extract quotation details — the AI service identifies relevant fields and returns structured data.
Evaluate suppliers — the scoring engine assesses suppliers across eight weighted factors.
Compare recommendations — review supplier rankings, scores, and explanations.
Review and approve — a manager reviews the recommendation before approval.
✨ At a Glance
🤖 AI-powered extraction from supplier quotation documents
⚖️ Eight-factor supplier scoring with explicit weights
📊 Supplier comparison and ranking
🧠 Explainable recommendations with relevant risks and context
👥 Human-in-the-loop approvals and role-based access
🔐 JWT authentication for protected API access
🔌 Mock SAP integration for demonstrating an integration workflow
🚀 Quick Start
Prerequisites
Docker and Docker Compose
Google Gemini API key: Google AI
Studio
At least 4 GB of free disk space
1. Clone and configure
git clone https://github.com/RajS45/ProcureSense-AI.git
cd ProcureSense-AI
cp .env.example .env
Add your Gemini API key to .env:

GEMINI_API_KEY=your_key_here
2. Start the application
docker-compose up --build
Docker Compose starts the PostgreSQL database, Spring Boot backend,
Python AI service, and React frontend, and loads demo data
automatically. Allow 60–90 seconds for services to become healthy.

3. Open the application
Service	Local URL
Frontend	http://localhost:3000
Backend API	http://localhost:8080/api/v1
AI service	http://localhost:8001
4. Sign in
Email: analyst@procuresense.com
Password: demo123
These are demo credentials from the project README. Replace demo
credentials and secrets before using any deployment outside a controlled
demo environment.

🧪 Demo Scenario
Goal: Purchase 10,000 steel units with a required delivery timeline of five days.

Supplier	Unit price	Delivery	Other context	Outcome
Supplier A	₹100	12 days	Lowest price, but misses the deadline	Rank #2
Supplier B	₹105	4 days	Strong reliability and quality certification	Rank #1 — Recommended
Supplier C	₹110	3 days	Limited capacity	Rank #3
Example scores
Supplier A: Cost=95 | Quality=75 | Delivery=0 | Reliability=80 | ... → Final: 62 (Rank #2)
Supplier B: Cost=85 | Quality=90 | Delivery=100 | Reliability=95 | ... → Final: 89 (Rank #1)
Supplier C: Cost=70 | Quality=80 | Delivery=95 | Reliability=75 | Capacity=60 | ... → Final: 76 (Rank #3)
Supplier B is recommended even though it is not the cheapest: it meets the deadline with a one-day safety margin, has strong on-time delivery history (96%), and has better quality certification.

End-to-end walkthrough
Sign in to the dashboard.
Create a purchase requirement for 10,000 units with a five-day delivery requirement.
Upload the three supplier quotation PDFs.
Let the AI service extract quotation data.
Run the deterministic eight-factor evaluation.
Compare supplier scores in the ranking table.
Review the recommendation and its explanation.
Have a manager approve the recommendation.
Review the displayed SAP integration status.
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
Layer	Technology	Responsibility
Frontend	React	Procurement dashboard and user workflow
Backend	Java, Spring Boot, Maven	REST API, business rules, authorization
Database	PostgreSQL	Application and demo data
AI service	Python, FastAPI, Google Gemini	Extract quotation data and generate explanations
Integration	Mock SAP services	Demonstrate SAP integration status
✨ Main Capabilities
Purchase requirement management
PDF quotation upload and storage
Gemini-powered quotation field extraction
Deterministic supplier scoring across eight factors
Supplier comparison and ranking
AI-generated explanations of recommendations
Delivery and capacity risk analysis
Alternative supplier scenarios
Manager review and approval
Analyst, Manager, and Admin roles
JWT-protected API access
Mock SAP integration, intended as a starting point for real
connectivity
⚖️ Business Logic
🎯 The Problem
Choosing suppliers on price alone can overlook delivery risks, quality inconsistencies, reliability, capacity constraints, and dependency on a single supplier.

📊 Eight-Factor Supplier Evaluation
Factor	Weight	What it evaluates
Total Cost	20%	Total procurement cost, not only unit price
Quality	15%	Historical quality and certifications
Delivery	15%	Ability to meet the required delivery date
Reliability	15%	Historical on-time delivery
Capacity	10%	Ability to fulfil the requested quantity
Payment Terms	10%	Business impact of payment conditions
Supplier Risk	10%	Financial, operational, and geographic risks
Dependency	5%	Supplier concentration and dependency
Weights are configurable rather than hard-coded**, according to the
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
Scale: 0–100, where higher is better. The source example gives ISO 9001
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
6. Payment Terms — 10%
Payment terms	Example score
Net 30–60 days	100
Net 15–29 days	90
50% advance	75
100% advance	30
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
The stated score range is 0–100. The highest-scoring supplier ranks
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
🔌 API Endpoints
Base path: /api/v1

Method	Endpoint	Purpose
POST	/auth/login	Authenticate and return token/user
POST	/purchase-requirements	Create a purchase requirement
GET	/purchase-requirements/{id}	Retrieve a requirement
POST	/quotations/upload	Upload quotation PDF(s)
GET	/quotations/{id}	Retrieve quotation and extracted data
POST	/evaluations	Trigger evaluation for a requirement
GET	/evaluations/{id}/scores	Retrieve supplier scores
GET	/recommendations/{requestId}	Retrieve recommendation and AI explanation
POST	/recommendations/{requestId}/approve	Approve a recommendation as a manager
POST	/recommendations/{requestId}/alternative-scenario	Simulate preferred-supplier disruption
Example login request:

curl -X POST http://localhost:8080/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"analyst@procuresense.com","password":"demo123"}'
The original README also lists a health-check example at
http://localhost:8080/api/v1/health.

🤖 AI Service
The Python FastAPI service uses Google Gemini to:

Extract supplier name, price, quantity, delivery date, payment
terms, capacity, and certifications from quotation PDFs.
Return extracted fields as structured JSON.
Generate explanations for recommendations, including identified
risks and context-specific reasoning.
Decision boundary: Gemini supports extraction and explanation. The
deterministic scoring logic calculates supplier scores.

🔐 Security
JWT authentication for API access
Role-based authorization for Analyst, Manager, and Admin
Bcrypt password hashing
Configured frontend/backend CORS
Secrets supplied through environment configuration
Demo accounts
Analyst: analyst@procuresense.com / demo123
Manager: manager@procuresense.com / demo123
Admin:   admin@procuresense.com / demo123
Use these only in the demo environment. Do not use demo passwords or
commit API keys in production.

🧪 Testing
Backend
cd backend
./mvnw test
Frontend
cd frontend
npm test
AI service
cd ai-service
pytest
Manual API checks
curl http://localhost:8080/api/v1/health
API endpoints can also be tested with Postman or curl.

🛠️ Troubleshooting
Services do not start
docker-compose logs -f backend
docker-compose logs -f ai-service
docker-compose logs -f frontend
Database connection issues
docker-compose down
docker-compose up --build
The original instructions also mention docker volume prune as a
possible reset step. Warning: pruning volumes can permanently remove
stored database data; use it only if you intentionally want to remove
that data.

Gemini API key issues
Check that GEMINI_API_KEY is set in .env.
Obtain a key through Google AI
Studio.
Check the AI service health endpoint:
curl http://localhost:8001/health.
Port conflicts
If ports 3000, 8080, or 8001 are already in use, edit the port
mappings in docker-compose.yml. For example, change the host-side
frontend port from 3000 to 3001 as appropriate for the configured
container port.

🚢 Deployment Notes
The source README documents a Docker Compose-based local deployment and
a Mock SAP integration. It does not provide a verified production
deployment procedure or claim that a live SAP connection is configured.
Configure secrets, replace demo credentials, and validate
environment-specific settings before deploying beyond a demo
environment.

🗺️ Future Enhancements
The source README lists the following post-MVP items:

Real SAP integration (BAPIs, OData)
Multi-level approval workflow
Context-aware factor weighting
Historical supplier scorecards
Alerts for supplier risk changes
Integration with procurement analytics
Advanced reporting and dashboards
Batch evaluation of multiple requirements
📄 License
Proprietary --- ProcureSense AI

💬 Support
For troubleshooting:

Check service logs with docker-compose logs -f.
Verify the Gemini API key in .env.
Ensure Docker Compose is up to date.
Check network connectivity.


