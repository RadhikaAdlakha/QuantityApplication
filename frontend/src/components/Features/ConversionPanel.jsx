import React, { useState } from 'react';
import { quantityService } from '../../services/api';
import QtyTabs, { UNITS, UNIT_SYMBOLS, fmt, unitLabel } from '../Common/QtyTabs';

export default function ConversionPanel() {
  const [qty, setQty] = useState("LENGTH");
  const [value, setValue] = useState("");
  const [fromUnit, setFromUnit] = useState("FEET");
  const [toUnits, setToUnits] = useState([]); 
  const [results, setResults] = useState([]);
  const [loading, setLoading] = useState(false);
  const [err, setErr] = useState("");

  const units = UNITS[qty];

  const toggleTo = (u) => {
    setResults([]);
    setErr("");
    if (toUnits.includes(u)) {
      setToUnits(prev => prev.filter(x => x !== u));
    } else {
      setToUnits(prev => [...prev, u]);
    }
  };

  const handleQtyChange = (q) => {
    setQty(q); 
    setResults([]); 
    setErr("");
    setFromUnit(UNITS[q][0]); 
    setToUnits([]); 
  };

  const execute = async () => {
    if (!value || isNaN(parseFloat(value))) { 
      setErr("Please enter a valid base value."); 
      return; 
    }
    if (toUnits.length === 0) { 
      setErr("Choose target units."); 
      return; 
    }
    
    setErr(""); 
    setLoading(true);
    const outputList = [];

    try {
      for (const tu of toUnits) {
        const data = await quantityService.convert(value, fromUnit, tu, qty);
        const outputValue = data.value !== undefined ? data.value : (data.result !== undefined ? data.result : data);
        
        outputList.push({ 
          unit: tu, 
          value: typeof outputValue === 'object' ? outputValue.value : outputValue 
        });
      }
      setResults(outputList);
    } catch (e) {
      setErr(e.response?.data?.message || "Conversion failed inside API channels.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div>
      <div className="page-title">Unit Conversion</div>
      <div className="page-sub">Convert raw measurement scales to single or multiple metrics instantly</div>
      
      <QtyTabs qty={qty} setQty={handleQtyChange} />
      
      <div className="grid-2">
        <div>
          <div className="card" style={{ marginBottom: "1.2rem" }}>
            <div className="card-title">Source Configuration</div>
            <div className="form-group">
              <label>Value to Convert</label>
              <input 
                type="number" 
                value={value} 
                onChange={e => { setValue(e.target.value); setResults([]); }} 
                placeholder="Enter value…" 
              />
            </div>
            <div className="form-group" style={{ marginBottom: 0 }}>
              <label>From Unit</label>
              <select value={fromUnit} onChange={e => { setFromUnit(e.target.value); setToUnits([]); setResults([]); }}>
                {units.map(u => <option key={u} value={u}>{unitLabel(u)}</option>)}
              </select>
            </div>
          </div>

          <div className="card">
            <div className="card-title">CONVERT TO</div>
            <div className="btn-group" style={{ display: 'flex', gap: '10px', flexWrap: 'wrap', marginBottom: '1.5rem' }}>
              {units.filter(u => u !== fromUnit).map(u => {
                const isSelected = toUnits.includes(u);
                return (
                  <button 
                    key={u} 
                    type="button"
                    onClick={() => toggleTo(u)}
                    style={{
                      minWidth: "54px",
                      height: "42px",
                      padding: "4px 12px",
                      fontSize: "14px",
                      fontWeight: "600",
                      borderRadius: "10px",
                      transition: "all 0.2s ease",
                      cursor: "pointer",
                      background: isSelected ? "rgba(15, 110, 86, 0.15)" : "transparent",
                      border: isSelected ? "2px solid #1D9E75" : "1px solid rgba(93, 202, 165, 0.25)",
                      color: isSelected ? "#5DCAA5" : "rgba(138, 181, 165, 0.7)",
                      boxShadow: isSelected ? "0 0 8px rgba(29, 158, 117, 0.3)" : "none"
                    }}
                  >
                    {UNIT_SYMBOLS[u] || u.toLowerCase()}
                  </button>
                );
              })}
            </div>

            {err && <div className="error-msg" style={{ marginBottom: '1rem' }}>{err}</div>}
            
            <hr className="divider" style={{ border: 'none', borderTop: '1px solid var(--border)', margin: '1.2rem 0' }} />
            
            <button className="btn btn-primary" onClick={execute} disabled={loading}>
              {loading ? "⏳ Converting..." : "⚡ Execute Conversion"}
            </button>
          </div>
        </div>

        <div className="card" style={{ display: 'flex', flexDirection: 'column' }}>
          <div className="card-title">Calculations Matrix</div>
          {results.length === 0 ? (
            <div className="result-panel" style={{ flex: 1 }}>
              <div className="result-empty">Target metrics evaluation summary empty.</div>
            </div>
          ) : (
            <div style={{ display: "flex", flexDirection: "column", gap: "12px", flex: 1 }}>
              {results.map(r => (
                <div 
                  key={r.unit} 
                  style={{ 
                    display: 'flex', 
                    justifyContent: 'space-between', 
                    alignItems: 'center',
                    padding: '1.2rem 1.5rem', 
                    background: 'linear-gradient(135deg, rgba(15,110,86,0.1), rgba(10,22,40,0.4))',
                    border: '1px solid var(--border-strong)',
                    borderRadius: '12px'
                  }}
                >
                  <div>
                    <div style={{ fontSize: 12, color: "var(--text-muted)", marginBottom: 4 }}>{r.unit}</div>
                    <div style={{ fontFamily: "'JetBrains Mono', monospace", fontSize: 24, fontWeight: 500, color: 'var(--accent)' }}>
                      {fmt(r.value)}
                    </div>
                  </div>
                  <div style={{ fontSize: 24, opacity: 0.6, fontWeight: 700 }}>{UNIT_SYMBOLS[r.unit]}</div>
                </div>
              ))}
            </div>
          )}
        </div>
      </div>
    </div>
  );
}