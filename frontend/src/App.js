import React, { useState, useEffect } from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import { Container, Nav, Navbar, Button, Alert } from 'react-bootstrap';
import axios from 'axios';
import './App.css';

const API_URL = process.env.REACT_APP_API_URL || 'http://localhost:8080/api/v1';

// ==================== API CLIENT ====================

const apiClient = axios.create({
  baseURL: API_URL,
  headers: {
    'Content-Type': 'application/json',
  }
});

apiClient.interceptors.request.use((config) => {
  const token = localStorage.getItem('token');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

// ==================== LOGIN PAGE ====================

function LoginPage() {
  const [email, setEmail] = useState('analyst@procuresense.com');
  const [password, setPassword] = useState('demo123');
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);

  const handleLogin = async (e) => {
    e.preventDefault();
    setLoading(true);
    try {
      const response = await apiClient.post('/auth/login', { email, password });
      localStorage.setItem('token', response.data.token);
      localStorage.setItem('user', JSON.stringify(response.data.user));
      window.location.href = '/dashboard';
    } catch (err) {
      setError(err.response?.data?.message || 'Login failed');
    }
    setLoading(false);
  };

  return (
    <div className="login-container">
      <div className="login-box">
        <h1>ProcureSense AI</h1>
        <p className="subtitle">Intelligent Procurement Decision Support</p>
        
        {error && <Alert variant="danger">{error}</Alert>}
        
        <form onSubmit={handleLogin}>
          <div className="mb-3">
            <input
              type="email"
              className="form-control"
              placeholder="Email"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              required
            />
          </div>
          <div className="mb-3">
            <input
              type="password"
              className="form-control"
              placeholder="Password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              required
            />
          </div>
          <button type="submit" className="btn btn-primary w-100" disabled={loading}>
            {loading ? 'Logging in...' : 'Login'}
          </button>
        </form>

        <hr />
        <p className="small text-muted">Demo Credentials:</p>
        <p className="small">Analyst: analyst@procuresense.com / demo123</p>
        <p className="small">Manager: manager@procuresense.com / demo123</p>
      </div>
    </div>
  );
}

// ==================== DASHBOARD ====================

function Dashboard() {
  const [requests, setRequests] = useState([]);
  const [loading, setLoading] = useState(true);
  const user = JSON.parse(localStorage.getItem('user') || '{}');

  useEffect(() => {
    fetchRequests();
  }, []);

  const fetchRequests = async () => {
    try {
      const response = await apiClient.get('/purchase-requests');
      setRequests(response.data);
    } catch (err) {
      console.error('Failed to fetch requests', err);
    }
    setLoading(false);
  };

  return (
    <div>
      <h2>Dashboard</h2>
      <p>Welcome, {user.fullName}!</p>
      
      <div className="mb-3">
        <a href="/create-requirement" className="btn btn-success">+ New Purchase Requirement</a>
      </div>

      <h4>Recent Procurement Requests</h4>
      {loading ? (
        <p>Loading...</p>
      ) : requests.length === 0 ? (
        <Alert variant="info">No purchase requests yet</Alert>
      ) : (
        <div className="table-responsive">
          <table className="table table-striped">
            <thead>
              <tr>
                <th>Request Code</th>
                <th>Product</th>
                <th>Quantity</th>
                <th>Required By</th>
                <th>Status</th>
                <th>Actions</th>
              </tr>
            </thead>
            <tbody>
              {requests.map((req) => (
                <tr key={req.id}>
                  <td><strong>{req.requestCode}</strong></td>
                  <td>{req.productName}</td>
                  <td>{req.quantity}</td>
                  <td>{new Date(req.requiredByDate).toLocaleDateString()}</td>
                  <td><span className={`badge bg-${getStatusColor(req.status)}`}>{req.status}</span></td>
                  <td>
                    <a href={`/requirement/${req.id}`} className="btn btn-sm btn-info">View</a>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}
    </div>
  );
}

function getStatusColor(status) {
  const colors = {
    DRAFT: 'secondary',
    QUOTATION_PENDING: 'warning',
    EVALUATION: 'info',
    APPROVED: 'success',
    REJECTED: 'danger',
    COMPLETED: 'success'
  };
  return colors[status] || 'secondary';
}

// ==================== CREATE REQUIREMENT ====================

function CreateRequirement() {
  const [formData, setFormData] = useState({
    productId: 1,
    quantity: 10000,
    requiredByDate: '2024-10-20',
    budget: 1200000,
    currentInventory: 300,
    priority: 'MEDIUM'
  });
  const [loading, setLoading] = useState(false);
  const [success, setSuccess] = useState('');
  const [error, setError] = useState('');

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({
      ...formData,
      [name]: name === 'quantity' || name === 'budget' || name === 'currentInventory' ? parseInt(value) : value
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    try {
      const response = await apiClient.post('/purchase-requests', formData);
      setSuccess(`Created requirement: ${response.data.requestCode}`);
      setTimeout(() => {
        window.location.href = `/requirement/${response.data.id}`;
      }, 1000);
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to create requirement');
    }
    setLoading(false);
  };

  return (
    <div>
      <h2>Create Purchase Requirement</h2>
      
      {error && <Alert variant="danger">{error}</Alert>}
      {success && <Alert variant="success">{success}</Alert>}

      <form onSubmit={handleSubmit} className="needs-validation">
        <div className="mb-3">
          <label>Product</label>
          <select name="productId" className="form-control" value={formData.productId} onChange={handleChange}>
            <option value="1">Steel Coils (1 MT)</option>
          </select>
        </div>

        <div className="mb-3">
          <label>Quantity Required</label>
          <input
            type="number"
            name="quantity"
            className="form-control"
            value={formData.quantity}
            onChange={handleChange}
            required
          />
        </div>

        <div className="mb-3">
          <label>Required By Date</label>
          <input
            type="date"
            name="requiredByDate"
            className="form-control"
            value={formData.requiredByDate}
            onChange={handleChange}
            required
          />
        </div>

        <div className="mb-3">
          <label>Budget (₹)</label>
          <input
            type="number"
            name="budget"
            className="form-control"
            value={formData.budget}
            onChange={handleChange}
          />
        </div>

        <div className="mb-3">
          <label>Current Inventory</label>
          <input
            type="number"
            name="currentInventory"
            className="form-control"
            value={formData.currentInventory}
            onChange={handleChange}
          />
        </div>

        <div className="mb-3">
          <label>Priority</label>
          <select name="priority" className="form-control" value={formData.priority} onChange={handleChange}>
            <option value="LOW">Low</option>
            <option value="MEDIUM">Medium</option>
            <option value="HIGH">High</option>
            <option value="CRITICAL">Critical</option>
          </select>
        </div>

        <button type="submit" className="btn btn-primary" disabled={loading}>
          {loading ? 'Creating...' : 'Create Requirement'}
        </button>
      </form>
    </div>
  );
}

// ==================== REQUIREMENT DETAIL & UPLOAD ====================

function RequirementDetail({ id }) {
  const [requirement, setRequirement] = useState(null);
  const [quotations, setQuotations] = useState([]);
  const [scores, setScores] = useState(null);
  const [recommendation, setRecommendation] = useState(null);
  const [loading, setLoading] = useState(true);
  const [activeTab, setActiveTab] = useState('overview');

  useEffect(() => {
    fetchData();
  }, [id]);

  const fetchData = async () => {
    try {
      const reqRes = await apiClient.get(`/purchase-requests/${id}`);
      setRequirement(reqRes.data);

      const quotRes = await apiClient.get(`/quotations/request/${id}`);
      setQuotations(quotRes.data);

      try {
        const recRes = await apiClient.get(`/recommendations/${id}`);
        setRecommendation(recRes.data);
      } catch (e) {
        // Recommendation may not exist yet
      }
    } catch (err) {
      console.error('Failed to fetch data', err);
    }
    setLoading(false);
  };

  const handleUploadQuotation = async () => {
    const fileName = prompt('Enter quotation file name (e.g., supplier_a.pdf)');
    if (!fileName) return;

    try {
      await apiClient.post('/quotations/upload', null, {
        params: {
          requestId: id,
          fileName: fileName
        }
      });
      alert('Quotation uploaded successfully');
      fetchData();
    } catch (err) {
      alert('Failed to upload quotation');
    }
  };

  const handleExtractAll = async () => {
    for (const quot of quotations) {
      try {
        await apiClient.post(`/quotations/${quot.id}/extract`);
      } catch (err) {
        console.error('Failed to extract', err);
      }
    }
    alert('Extraction completed');
    fetchData();
  };

  const handleEvaluate = async () => {
    try {
      const res = await apiClient.post('/evaluations', null, {
        params: { requestId: id }
      });
      setScores(res.data);
      setActiveTab('scoring');
    } catch (err) {
      alert('Failed to evaluate suppliers');
    }
  };

  const handleCreateRecommendation = async () => {
    try {
      const res = await apiClient.post('/recommendations', null, {
        params: { requestId: id }
      });
      setRecommendation(res.data);
      setActiveTab('recommendation');
    } catch (err) {
      alert('Failed to create recommendation');
    }
  };

  const handleApprove = async () => {
    try {
      await apiClient.post(`/recommendations/${recommendation.id}/approve`, {
        comments: 'Approved by manager'
      });
      alert('Recommendation approved');
      setRecommendation({ ...recommendation, status: 'APPROVED' });
    } catch (err) {
      alert('Failed to approve');
    }
  };

  if (loading) return <p>Loading...</p>;
  if (!requirement) return <Alert variant="danger">Requirement not found</Alert>;

  return (
    <div>
      <h2>{requirement.requestCode}</h2>
      <p><strong>Product:</strong> {requirement.productName}</p>
      <p><strong>Quantity:</strong> {requirement.quantity}</p>
      <p><strong>Required By:</strong> {new Date(requirement.requiredByDate).toLocaleDateString()}</p>
      <p><strong>Inventory Days:</strong> {requirement.inventoryDays}</p>

      <ul className="nav nav-tabs mb-3">
        <li className="nav-item"><a className={`nav-link ${activeTab === 'overview' ? 'active' : ''}`} href="#" onClick={() => setActiveTab('overview')}>Overview</a></li>
        <li className="nav-item"><a className={`nav-link ${activeTab === 'quotations' ? 'active' : ''}`} href="#" onClick={() => setActiveTab('quotations')}>Quotations</a></li>
        <li className="nav-item"><a className={`nav-link ${activeTab === 'scoring' ? 'active' : ''}`} href="#" onClick={() => setActiveTab('scoring')}>Supplier Comparison</a></li>
        <li className="nav-item"><a className={`nav-link ${activeTab === 'recommendation' ? 'active' : ''}`} href="#" onClick={() => setActiveTab('recommendation')}>Recommendation</a></li>
      </ul>

      {activeTab === 'overview' && (
        <div>
          <h4>Procurement Status</h4>
          <p>Current Status: <span className={`badge bg-${getStatusColor(requirement.status)}`}>{requirement.status}</span></p>
          <div className="mb-3">
            <button className="btn btn-primary me-2" onClick={handleUploadQuotation}>Upload Quotation</button>
            <button className="btn btn-info me-2" onClick={handleExtractAll} disabled={quotations.length === 0}>Extract All</button>
            <button className="btn btn-success me-2" onClick={handleEvaluate} disabled={quotations.length === 0}>Evaluate Suppliers</button>
          </div>
        </div>
      )}

      {activeTab === 'quotations' && (
        <div>
          <h4>Quotations ({quotations.length})</h4>
          {quotations.length === 0 ? (
            <Alert variant="info">No quotations uploaded yet</Alert>
          ) : (
            <div className="table-responsive">
              <table className="table">
                <thead>
                  <tr>
                    <th>Supplier</th>
                    <th>File</th>
                    <th>Status</th>
                    <th>Confidence</th>
                    <th>Price (₹)</th>
                  </tr>
                </thead>
                <tbody>
                  {quotations.map((q) => (
                    <tr key={q.id}>
                      <td>{q.supplierName}</td>
                      <td>{q.fileName}</td>
                      <td><span className={`badge bg-${getStatusColor(q.status)}`}>{q.status}</span></td>
                      <td>{q.extractionConfidence ? (q.extractionConfidence * 100).toFixed(0) + '%' : '-'}</td>
                      <td>{q.item?.totalPriceInr?.toLocaleString('en-IN') || '-'}</td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          )}
        </div>
      )}

      {activeTab === 'scoring' && scores && (
        <div>
          <h4>Supplier Comparison & Scores</h4>
          <Alert variant="info">
            <strong>Recommended:</strong> {scores.recommendedSupplierName}
          </Alert>
          <div className="table-responsive">
            <table className="table table-sm">
              <thead>
                <tr>
                  <th>Supplier</th>
                  <th>Cost</th>
                  <th>Quality</th>
                  <th>Delivery</th>
                  <th>Reliability</th>
                  <th>Capacity</th>
                  <th>Final Score</th>
                  <th>Rank</th>
                </tr>
              </thead>
              <tbody>
                {scores.suppliers.map((s) => (
                  <tr key={s.id} className={s.ranking === 1 ? 'table-success' : ''}>
                    <td><strong>{s.supplierName}</strong></td>
                    <td>{s.costScore?.toFixed(0)}</td>
                    <td>{s.qualityScore?.toFixed(0)}</td>
                    <td>{s.deliveryScore?.toFixed(0)}</td>
                    <td>{s.reliabilityScore?.toFixed(0)}</td>
                    <td>{s.capacityScore?.toFixed(0)}</td>
                    <td><strong>{s.finalWeightedScore?.toFixed(1)}</strong></td>
                    <td>#{s.ranking}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
          <button className="btn btn-success mt-3" onClick={handleCreateRecommendation}>Create Recommendation</button>
        </div>
      )}

      {activeTab === 'recommendation' && recommendation && (
        <div>
          <h4>Recommendation</h4>
          <Alert variant="success">
            <h5>{recommendation.preferredSupplierName}</h5>
            <p><strong>Score:</strong> {recommendation.preferredScore?.toFixed(1)}/100</p>
            <p>{recommendation.reasoning}</p>
          </Alert>
          {recommendation.status !== 'APPROVED' && (
            <button className="btn btn-success" onClick={handleApprove}>Approve Recommendation</button>
          )}
          {recommendation.status === 'APPROVED' && (
            <Alert variant="success" className="mt-3">✓ Recommendation Approved</Alert>
          )}
        </div>
      )}
    </div>
  );
}

// ==================== NAVIGATION & MAIN APP ====================

function Navigation({ isLoggedIn, user }) {
  const handleLogout = () => {
    localStorage.removeItem('token');
    localStorage.removeItem('user');
    window.location.href = '/';
  };

  if (!isLoggedIn) return null;

  return (
    <Navbar bg="dark" expand="lg" sticky="top">
      <Container>
        <Navbar.Brand href="/dashboard">🎯 ProcureSense AI</Navbar.Brand>
        <Navbar.Toggle />
        <Navbar.Collapse>
          <Nav className="ms-auto">
            <Nav.Link href="/dashboard">Dashboard</Nav.Link>
            <Nav.Link href="/create-requirement">New Requirement</Nav.Link>
            <span className="nav-link me-2">User: {user?.fullName}</span>
            <Button variant="outline-light" size="sm" onClick={handleLogout}>Logout</Button>
          </Nav>
        </Navbar.Collapse>
      </Container>
    </Navbar>
  );
}

function App() {
  const [isLoggedIn, setIsLoggedIn] = useState(!!localStorage.getItem('token'));
  const user = JSON.parse(localStorage.getItem('user') || '{}');

  return (
    <Router>
      <Navigation isLoggedIn={isLoggedIn} user={user} />
      <Container className="mt-4">
        <Routes>
          <Route path="/" element={isLoggedIn ? <Navigate to="/dashboard" /> : <LoginPage />} />
          <Route path="/dashboard" element={isLoggedIn ? <Dashboard /> : <Navigate to="/" />} />
          <Route path="/create-requirement" element={isLoggedIn ? <CreateRequirement /> : <Navigate to="/" />} />
          <Route path="/requirement/:id" element={isLoggedIn ? <RequirementDetailRoute /> : <Navigate to="/" />} />
        </Routes>
      </Container>
    </Router>
  );
}

function RequirementDetailRoute() {
  const { id } = new URLSearchParams(window.location.search);
  const pathId = window.location.pathname.split('/').pop();
  return <RequirementDetail id={pathId} />;
}

export default App;
