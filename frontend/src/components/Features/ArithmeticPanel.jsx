import React, { useState } from 'react';
import { quantityService } from '../../services/api';
import QtyTabs, { UNITS, UNIT_SYMBOLS, fmt, unitLabel } from '../Common/QtyTabs';

// Aapke backend OperationType enum ke hisaab se strictly configured keys:
const ARITHMETIC_OPS = [
  { label: "+", value: "ADD", title: "Addition" },
  { label: "−", value: "SUBTRACT", title: "Subtraction" },
  { label: "÷", value: "DIVIDE", title: "Division" } // <-- DIVIDE yahan add ho gaya hai!
];

export default function ArithmeticPanel() {
  const [qty, setQty] = useState("LENGTH");
  const [v1, setV1] = useState(""); 
  const [u1, setU1] = useState("FEET");
  const [v2, setV2] = useState(""); 
  const [u2, setU2] = useState("FEET");
  const [op, setOp] = useState("ADD");
  const [result, setResult] = useState(null);
  const [loading, setLoading] = useState(false);
  const [err, setErr] = useState("");

  const units = UNITS[qty];

  const handleQtyChange = (q) => { 
    setQty(q); 
    setResult(null); 
    setErr(""); 
    setU1(UNITS[q][0]); 
    setU2(UNITS[q][0]); 
  };

  const execute = async () => {
    // Temperature Unit absolute calculations block karta hai backend standard pe
    if (qty === "TEMPERATURE") {
      setErr("Temperature does not support arithmetic operations. Arithmetic operations are not meaningful for absolute temperatures.");
      return;
    }
    
    if (!v1 || !v2 || isNaN(parseFloat(v1)) || isNaN(parseFloat(v2))) {
      setErr("Enter valid numbers for both entry spaces.");
      return;
    }

    // Zero se division check karne ke liye validation
    if (op === "DIVIDE" && parseFloat(v2) === 0) {
      setErr("Cannot divide by zero. Please enter a valid non-zero value for Value B.");
      return;
    }

    setErr(""); 
    setLoading(true);

    try {
      // API call strictly mapping to your backend signature
      const data = await quantityService.performArithmetic(v1, u1, v2, u2, op, qty);
      setResult({ value: data.result ?? data.value ?? data, unit: u1 });
    } catch (e) {
      setErr(e.response?.data?.message || "Backend error evaluating mathematical operations.");
    } finally {
      setLoading(false);
    }
  };

  const opMeta = ARITHMETIC_OPS.find(o => o.value === op);

  return (
    <div>
      <div className="page-title">Arithmetic Operations</div>
      <div className="page-sub">Perform Add, Subtract, or Divide operations on measurement quantities</div>
      
      <QtyTabs qty={qty} setQty={handleQtyChange} />
      
      <div className="grid-2">
        <div style={{ display: "flex", flexDirection: "column", gap: "1.2rem" }}>
          
          {/* Operation Selector Panel */}
          <div className="card">
            <div className="card-title">Operation Selector</div>
            <div style={{ display: "flex", gap: "12px", justifyContent: "center", padding: "0.5rem 0" }}>
              {ARITHMETIC_OPS.map(o => (
                <button 
                  key={o.value} 
                  className={`op-btn${op === o.value ? " selected" : ""}`} 
                  title={o.title}
                  onClick={() => { setOp(o.value); setResult(null); }}
                >
                  {o.label}
                </button>
              ))}
            </div>
            <div style={{ textAlign: "center", fontSize: 12, color: "var(--text-muted)", marginTop: 8 }}>
              {opMeta?.title} Active
            </div>
          </div>

          {/* Value A Input Card */}
          <div className="card">
            <div className="card-title">Value A</div>
            <div className="form-group">
              <label>Amount</label>
              <input type="number" value={v1} onChange={e => { setV1(e.target.value); setResult(null); }} placeholder="First value…" />
            </div>
            <div className="form-group" style={{ marginBottom: 0 }}>
              <label>Unit</label>
              <select value={u1} onChange={e => { setU1(e.target.value); setResult(null); }}>
                {units.map(u => <option key={u} value={u}>{unitLabel(u)}</option>)}
              </select>
            </div>
          </div>

          {/* Value B Input Card */}
          <div className="card">
            <div className="card-title">Value B</div>
            <div className="form-group">
              <label>Amount</label>
              <input type="number" value={v2} onChange={e => { setV2(e.target.value); setResult(null); }} placeholder="Second value…" />
            </div>
            <div className="form-group" style={{ marginBottom: 0 }}>
              <label>Unit</label>
              <select value={u2} onChange={e => { setU2(e.target.value); setResult(null); }}>
                {units.map(u => <option key={u} value={u}>{unitLabel(u)}</option>)}
              </select>
            </div>
          </div>

          {err && <div className="error-msg">{err}</div>}
          
          <button className="btn btn-primary" onClick={execute} disabled={loading}>
            {loading ? "⏳ Calculating…" : "⚡ Execute Operation"}
          </button>
        </div>

        {/* Output Panel Card */}
        <div className="card" style={{ display: "flex", flexDirection: "column", gap: "1.2rem" }}>
          <div className="card-title">Result Summary</div>
          <div className="result-panel" style={{ flex: 1 }}>
            {result === null ? (
              <div className="result-empty">Awaiting operation calculation…</div>
            ) : (
              <>
                <div className="result-label" style={{ fontSize: 12, color: "var(--text-muted)", marginBottom: 12 }}>Result in {result.unit}</div>
                <div className="result-value">{fmt(result.value)}</div>
                <div className="result-unit">{UNIT_SYMBOLS[result.unit] || result.unit}</div>
              </>
            )}
          </div>

          {result !== null && (
            <div style={{ background: "rgba(10,22,40,0.5)", borderRadius: "10px", padding: "1rem", fontSize: 13 }}>
              <div style={{ color: "var(--text-muted)", marginBottom: 6, fontSize: 11, letterSpacing: "0.5px" }}>EXPRESSION</div>
              <div style={{ fontFamily: "'JetBrains Mono', monospace", color: "var(--text-secondary)" }}>
                {v1} {UNIT_SYMBOLS[u1] || u1} {opMeta?.label} {v2} {UNIT_SYMBOLS[u2] || u2}
              </div>
              <div style={{ fontFamily: "'JetBrains Mono', monospace", color: "var(--accent)", marginTop: 6 }}>
                = {fmt(result.value)} {UNIT_SYMBOLS[result.unit] || result.unit}
              </div>
            </div>
          )}
        </div>
      </div>
    </div>
  );
}