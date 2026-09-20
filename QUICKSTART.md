# ProcureSense AI - Quick Start Guide

## ⚡ Get Started in 5 Minutes

### Step 1: Prerequisites
- Docker and Docker Compose installed
- Google Gemini API key (get free at https://makersuite.google.com/app/apikey)
- 4GB free disk space
- Port 3000, 8080, 8001, 5432 available

### Step 2: Configure Environment
```bash
cd procuresense-ai

# Copy environment template
cp .env.example .env

# Edit .env and add your Gemini API key
nano .env
# Set: GEMINI_API_KEY=your_key_here
```

### Step 3: Start the Application
```bash
# Start all services (backend, AI, frontend, database)
docker-compose up --build

# First time may take 2-3 minutes to build images
# Wait until all services show "healthy"
```

### Step 4: Access the Application
- **Frontend:** http://localhost:3000
- **Backend API:** http://localhost:8080/api/v1
- **AI Service:** http://localhost:8001

### Step 5: Login
Use any of these demo accounts:

| Role     | Email                        | Password |
|----------|------------------------------|----------|
| Analyst  | analyst@procuresense.com     | demo123  |
| Manager  | manager@procuresense.com     | demo123  |
| Admin    | admin@procuresense.com       | demo123  |

---

## 🎯 Complete Demo Scenario (5-10 minutes)

### Scenario
Your company needs to purchase 10,000 steel units with a 5-day delivery requirement. Current inventory is 3 days worth. Three suppliers submitted quotations.

### What Happens
1. **Supplier A:** Lowest price (₹100/unit), but 12-day delivery ❌ Too slow
2. **Supplier B:** Medium price (₹105/unit), 4-day delivery ✅ RECOMMENDED
3. **Supplier C:** Higher price (₹110/unit), 3-day delivery, limited capacity

### Walk Through
1. Go to http://localhost:3000
2. Login as `analyst@procuresense.com` / `demo123`
3. Click "+ New Purchase Requirement"
4. Fill in:
   - Product: Steel Coils (1 MT)
   - Quantity: 10000
   - Required By: 5 days from now
   - Budget: ₹1,200,000
   - Current Inventory: 300 units
   - Priority: MEDIUM
5. Click "Create Requirement"
6. Click "Upload Quotation" → Enter `supplier_a.pdf`
7. Click "Upload Quotation" → Enter `supplier_b.pdf`
8. Click "Upload Quotation" → Enter `supplier_c.pdf`
9. Click "Extract All" (AI extracts quotation data)
10. Click "Evaluate Suppliers" (system calculates 8-factor scores)
11. Review the **Supplier Comparison** table:
    - Supplier A: 62/100 (delivery failure)
    - **Supplier B: 89/100** ← Recommended ✅
    - Supplier C: 76/100 (capacity limited)
12. Click "Create Recommendation"
13. Review the recommendation explanation
14. **Logout** and login as `manager@procuresense.com`
15. Click "View" on the requirement
16. Go to "Recommendation" tab
17. Click "Approve Recommendation"
18. ✅ Workflow complete!

### Key Insight
**ProcureSense chose Supplier B despite not being the cheapest.**

Why?
- **Context:** Inventory is only 3 days, need delivery in 5 days
- **Risk:** Supplier A delivers in 12 days → Stockout!
- **Score:** Delivery is critical (15% weight), Reliability is critical (15% weight)
- **Recommendation:** Supplier B's 96% on-time delivery + 4-day lead time = Safe choice

This demonstrates the core value: **Supplier selection based on business context, not just price.**

---

## 🔧 Troubleshooting

### Services Not Starting
```bash
# Check logs
docker-compose logs -f backend
docker-compose logs -f ai-service
docker-compose logs -f frontend
```

### Database Connection Issues
```bash
# Restart database
docker-compose down
docker volume prune
docker-compose up --build
```

### Gemini API Key Issues
- Verify key in .env file
- Test with: `curl http://localhost:8001/health`
- Check free tier limits at Google AI Studio

### Port Already In Use
```bash
# Change docker-compose.yml ports section:
# ports:
#   - "3001:80"  # Changed from 3000
#   - "8081:8080" # Changed from 8080
```

### Slow First Build
First docker build may take 3-5 minutes. Subsequent builds are much faster.

---

## 📚 API Documentation

All endpoints require JWT token in Authorization header:
```
Authorization: Bearer {token}
```

### Authentication
```
POST /auth/login
Body: { email, password }
Response: { token, user }
```

### Create Requirement
```
POST /purchase-requirements
Body: { productId, quantity, requiredByDate, budget, priority }
```

### Upload Quotation
```
POST /quotations/upload?requestId=1&fileName=supplier_a.pdf
```

### Extract Quotation
```
POST /quotations/1/extract
```

### Evaluate Suppliers
```
POST /evaluations?requestId=1
Response: { suppliers: [...], recommendedSupplierName }
```

### Create Recommendation
```
POST /recommendations?requestId=1
```

### Approve Recommendation
```
POST /recommendations/1/approve
Body: { decision: "APPROVED", comments: "" }
```

---

## 🐳 Docker Compose Services

| Service    | Port  | Technology  | Purpose |
|-----------|-------|-----------|---------|
| frontend  | 3000  | React     | Web UI |
| backend   | 8080  | Java/Spring| REST API |
| ai-service| 8001  | Python/FastAPI | AI extraction |
| postgres  | 5432  | PostgreSQL | Database |

---

## 📖 Configuration

### Scoring Weights (can be modified in future)
```
Cost:        20%
Quality:     15%
Delivery:    15%
Reliability: 15%
Capacity:    10%
Payment:     10%
Risk:        10%
Dependency:   5%
```

### Demo Data
- 3 suppliers pre-loaded (Supplier A, B, C)
- 1 product pre-loaded (Steel Coils)
- 3 demo users (analyst, manager, admin)

---

## 🚀 Production Deployment

This is MVP for local development. For production:

1. Use real SAP integration (modify SapProcurementDataProvider)
2. Add real authentication (LDAP/SSO)
3. Enable HTTPS/TLS
4. Use managed database (RDS, Cloud SQL)
5. Add comprehensive monitoring & logging
6. Implement audit trails
7. Scale horizontally with load balancer

---

## 💡 What's Next

- [ ] Integrate with real SAP systems
- [ ] Add multi-level approval workflows
- [ ] Historical supplier scorecards
- [ ] Advanced risk analytics
- [ ] Supplier alerts & notifications
- [ ] Procurement analytics dashboard
- [ ] Mobile app

---

## 📞 Support

1. Check logs: `docker-compose logs -f`
2. Verify API health: `curl http://localhost:8080/api/v1/health`
3. Check AI service: `curl http://localhost:8001/health`
4. Review environment variables in .env

---

**Built with ❤️ in 24 hours | Production-ready MVP**
