import React, { useState, useEffect } from 'react';
import ComparisonPanel from './components/Features/ComparisonPanel';
import ArithmeticPanel from './components/Features/ArithmeticPanel';
import ConversionPanel from './components/Features/ConversionPanel';

export default function App() {
  const [activeTab, setActiveTab] = useState("comparison");
  const [token, setToken] = useState(localStorage.getItem('token'));

  // Backend handler redirect hokar jab token URL par layega, yeh use parse karega
  useEffect(() => {
    const urlParams = new URLSearchParams(window.location.search);
    const urlToken = urlParams.get('token');

    if (urlToken) {
      localStorage.setItem('token', urlToken);
      setToken(urlToken);
      // Clean query string parameters for safety
      window.history.replaceState({}, document.title, window.location.pathname);
    }
  }, []);

  const handleGoogleLogin = () => {
    // Agar localhost refuse kare, toh exact loopback IP use karna best hota hai
    window.location.href = "http://localhost:8080/oauth2/authorization/google";
  };

  const handleLogout = () => {
    localStorage.removeItem('token');
    setToken(null);
  };

  // Safe Authentication Guard Layer
  if (!token) {
    return (
      <div className="app" style={{ display: 'flex', justifyContent: 'center', alignItems: 'center', minHeight: '100vh' }}>
        <div className="card" style={{ maxWidth: '400px', width: '100%', textAlign: 'center', padding: '2.5rem' }}>
          <div className="logo-icon" style={{ margin: '0 auto 1.5rem auto', width: '50px', height: '50px', fontSize: '24px' }}>⚗️</div>
          <h2 className="page-title" style={{ fontSize: '24px', marginBottom: '8px' }}>QuantiCalc Secure</h2>
          <p className="page-sub" style={{ marginBottom: '2rem' }}>Please sign in via Google OAuth to access the measurement suite.</p>
          
          <button className="btn btn-primary" onClick={handleGoogleLogin} style={{ display: 'flex', gap: '10px', alignItems: 'center', justifyContent: 'center' }}>
            <svg width="18" height="18" viewBox="0 0 18 18">
              <path d="M17.64 9.2c0-.63-.06-1.25-.16-1.84H9v3.49h4.84c-.21 1.12-.84 2.07-1.79 2.7l2.76 2.13c1.62-1.49 2.55-3.69 2.55-6.48z" fill="#4285F4"/>
              <path d="M9 18c2.43 0 4.47-.8 5.96-2.18l-2.76-2.13c-.76.51-1.74.82-2.71.82-2.09 0-3.86-1.41-4.49-3.3H2.18v2.24C3.66 16.17 6.12 18 9 18z" fill="#34A853"/>
              <path d="M4.51 11.21c-.16-.48-.25-1-.25-1.54s.09-1.06.25-1.54V5.89H2.18C1.65 6.97 1.34 8.18 1.34 9.5s.31 2.53.84 3.61l2.33-1.9z" fill="#FBBC05"/>
              <path d="M9 3.58c1.32 0 2.5.45 3.44 1.35L15 2.3C13.46.86 11.43 0 9 0 6.12 0 3.66 1.83 2.18 4.54l2.33 1.9C5.14 4.99 6.91 3.58 9 3.58z" fill="#EA4335"/>
            </svg>
            Sign In with Google
          </button>
        </div>
      </div>
    );
  }

  return (
    <div className="app">
      <header className="header">
        <div className="logo">
          <div className="logo-icon">⚗️</div>
          <div>
            <div className="logo-text">QuantiCalc</div>
            <div className="logo-sub">Enterprise Hub</div>
          </div>
        </div>
        
        <nav className="nav">
          <button className={`nav-btn ${activeTab === 'comparison' ? 'active' : ''}`} onClick={() => setActiveTab('comparison')}>Comparison</button>
          <button className={`nav-btn ${activeTab === 'arithmetic' ? 'active' : ''}`} onClick={() => setActiveTab('arithmetic')}>Arithmetic</button>
          <button className={`nav-btn ${activeTab === 'conversion' ? 'active' : ''}`} onClick={() => setActiveTab('conversion')}>Conversion</button>
        </nav>

        <div style={{ display: 'flex', alignItems: 'center', gap: '15px' }}>
          <div className="status-pill status-online">
            <span className="status-dot"></span>OAuth 2.0 Active
          </div>
          <button onClick={handleLogout} className="btn" style={{ padding: '6px 12px', fontSize: '12px', background: 'rgba(194,75,74,0.15)', color: '#F09595', border: '1px solid rgba(194,75,74,0.3)' }}>
            Logout
          </button>
        </div>
      </header>

      <main className="main">
        {activeTab === 'comparison' && <ComparisonPanel />}
        {activeTab === 'arithmetic' && <ArithmeticPanel />}
        {activeTab === 'conversion' && <ConversionPanel />}
      </main>

      <footer className="footer">
        QuantiCalc Suite · JWT Security Active
      </footer>
    </div>
  );
}