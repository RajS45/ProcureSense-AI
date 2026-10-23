
procuresense-ai/
├── backend/                         # Spring Boot backend (Java)
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/procuresense/
│   │   │   │   ├── controller/      # REST API endpoints
│   │   │   │   ├── service/         # Business logic
│   │   │   │   ├── repository/      # Database access
│   │   │   │   ├── model/           # Domain objects
│   │   │   │   ├── dto/             # Data transfer objects
│   │   │   │   ├── scoring/         # Eight-factor scoring engine
│   │   │   │   └── config/          # Spring configuration
│   │   │   └── resources/           # Application configuration
│   │   └── test/                    # Backend tests, if present
│   ├── pom.xml
│   └── Dockerfile
│
├── frontend/                        # React frontend
│   ├── src/
│   │   ├── pages/                   # Main pages
│   │   ├── components/              # Reusable UI components
│   │   ├── api/                     # API client
│   │   ├── redux/                   # State management
│   │   └── App.js
│   ├── package.json
│   └── Dockerfile
│
├── ai-service/                      # Python FastAPI service
│   ├── main.py                      # Service entry point
│   ├── routes/                      # Extraction and health endpoints
│   ├── services/                    # LLM extraction logic
│   ├── requirements.txt
│   └── Dockerfile
│
├── database/
│   ├── migrations/                  # SQL migration scripts
│   └── seed-data.sql                # Demo data
│
└── docker-compose.yml               # Service orchestration
