#!/bin/bash

# ProcureSense AI - Setup and Run Script
# This script automates the setup and startup process

set -e  # Exit on error

echo "======================================================"
echo "   ProcureSense AI - 24-Hour MVP"
echo "   Intelligent Procurement Decision Support"
echo "======================================================"
echo ""

# Check prerequisites
echo "🔍 Checking prerequisites..."

if ! command -v docker &> /dev/null; then
    echo "❌ Docker is not installed. Please install Docker."
    exit 1
fi
echo "✓ Docker installed"

if ! command -v docker-compose &> /dev/null; then
    echo "❌ Docker Compose is not installed. Please install Docker Compose."
    exit 1
fi
echo "✓ Docker Compose installed"

# Check if .env exists
if [ ! -f ".env" ]; then
    echo ""
    echo "⚠️  .env file not found. Creating from template..."
    cp .env.example .env
    echo "✓ Created .env file"
    echo ""
    echo "⚠️  IMPORTANT: Edit .env and add your Google Gemini API key!"
    echo "   Get free key at: https://makersuite.google.com/app/apikey"
    echo ""
    echo "Run the following command to edit:"
    echo "   nano .env"
    echo ""
    echo "Then run this script again."
    exit 0
fi

# Check if Gemini API key is set
if grep -q "your_google_gemini_api_key_here" .env; then
    echo "⚠️  Gemini API key not configured in .env"
    echo "   AI service will use mock responses."
    echo ""
    echo "To add real API key:"
    echo "   1. Get key at https://makersuite.google.com/app/apikey"
    echo "   2. Edit .env file"
    echo "   3. Set GEMINI_API_KEY=your_actual_key"
    echo "   4. Restart Docker Compose"
    echo ""
fi

# Check available ports
echo ""
echo "🔍 Checking required ports..."

check_port() {
    if netstat -tuln 2>/dev/null | grep -q ":$1 "; then
        echo "❌ Port $1 is already in use"
        return 1
    fi
    echo "✓ Port $1 available"
    return 0
}

if command -v netstat &> /dev/null; then
    check_port 3000 || exit 1
    check_port 8080 || exit 1
    check_port 8001 || exit 1
    check_port 5432 || exit 1
fi

echo ""
echo "🚀 Starting ProcureSense AI..."
echo ""
echo "This will:"
echo "  1. Build Docker images (2-3 minutes first time)"
echo "  2. Start PostgreSQL database"
echo "  3. Start Spring Boot backend"
echo "  4. Start Python AI service"
echo "  5. Start React frontend"
echo ""
echo "⏳ Please wait... (first build may take several minutes)"
echo ""

# Start Docker Compose
docker-compose up --build

echo ""
echo "======================================================"
echo "   ✓ ProcureSense AI is starting!"
echo "======================================================"
echo ""
echo "Access the application at:"
echo "  Frontend:   http://localhost:3000"
echo "  Backend:    http://localhost:8080/api/v1"
echo "  AI Service: http://localhost:8001"
echo ""
echo "Login with:"
echo "  Email:    analyst@procuresense.com"
echo "  Password: demo123"
echo ""
echo "For complete walkthrough, see: QUICKSTART.md"
echo ""
