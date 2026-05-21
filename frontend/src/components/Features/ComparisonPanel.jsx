import React, { useState } from 'react';
import { quantityService } from '../../services/api';
import QtyTabs, { UNITS, UNIT_SYMBOLS, unitLabel } from '../Common/QtyTabs';

// Base conversion rates exactly matching your backend factors for absolute safety fallback
const CONVERSION_FACTORS = {
  FEET: 1.0,
  INCH: 1.0 / 12,
  YARDS: 3.0,
  CENTIMETERS: 1.0 / 30.48,
  KILOGRAM: 1.0,
  GRAM: 0.001,
  POUND: 1.0 / 2.20462,
  LITRE: 1.0,
  MILLILITRE: 0.001,
  GALLON: 3.78541,
};

export default function ComparisonPanel() {
  const [qty, setQty] = useState("LENGTH");
  const [v1, setV1] = useState("");
  const [u1, setU1] = useState("FEET");
  const [v2, setV2] = useState("");
  const [u2, setU2] = useState("INCH");
  const [result, setResult] = useState(null);
  const [loading, setLoading] = useState(false);
  const [err, setErr] = useState("");

  const units = UNITS[qty];

  const handleQtyChange = (q) => {
    setQty(q); setResult(null); setErr("");
    setU1(UNITS[q][0]); setU2(UNITS[q][1] || UNITS[q][0]);
  };

  const execute = async () => {
    if (!v1 || !v2 || isNaN(parseFloat(v1)) || isNaN(parseFloat(v2))) {
      setErr("Enter valid numbers for both values.");
      return;
    }
    setErr(""); 
    setLoading(true);

    try {
      // Direct controller request execution
      const data = await quantityService.compare(v1, u1, v2, u2, qty);
      console.log("Incoming Server Payload Data Log:", data);

      // ─── THE ULTIMATE SMART PARSER LOGIC ───
      let finalVerdict = "";

      // Check 1: Agar backend direct plain text string bhej raha hai
      if (data && typeof data === 'string') {
        finalVerdict = data.toUpperCase();
      } 
      // Check 2: Agar response me custom string field exist karta ho
      else if (data && (data.result || data.status || data.message)) {
        const val = data.result || data.status || data.message;
        if (typeof val === 'string') finalVerdict = val.toUpperCase();
      }

      // Check 3 (Failsafe Backup): Agar upar kuch nahi mila, toh custom absolute client math use hoga
      if (!finalVerdict || (!finalVerdict.includes("GREAT") && !finalVerdict.includes("LESS") && !finalVerdict.includes("EQUAL"))) {
        
        // Temperature Absolute Scaling
        if (qty === "TEMPERATURE") {
          let t1 = parseFloat(v1);
          let t2 = parseFloat(v2);
          
          // Base transformation to Celsius
          if (u1 === "FAHRENHEIT") t1 = (t1 - 32) * 5 / 9;
          if (u1 === "KELVIN") t1 = t1 - 273.15;
          if (u2 === "FAHRENHEIT") t2 = (t2 - 32) * 5 / 9;
          if (u2 === "KELVIN") t2 = t2 - 273.15;

          if (Math.abs(t1 - t2) < 1e-4) finalVerdict = "EQUAL";
          else finalVerdict = t1 > t2 ? "GREATER" : "LESS";
        } 
        // Standard Metric Scaling using exact enum factor matching formulas
        else {
          const factor1 = CONVERSION_FACTORS[u1] || 1.0;
          const factor2 = CONVERSION_FACTORS[u2] || 1.0;
          
          const baseValue1 = parseFloat(v1) * factor1;
          const baseValue2 = parseFloat(v2) * factor2;

          // Precision delta comparison scaling boundary
          if (Math.abs(baseValue1 - baseValue2) < 1e-4) {
            finalVerdict = "EQUAL";
          } else {
            finalVerdict = baseValue1 > baseValue2 ? "GREATER" : "LESS";
          }
        }
      }

      setResult(finalVerdict);

    } catch (e) {
      console.error(e);
      setErr(e.response?.data?.message || "Error processing synchronization over network standard channels.");
    } finally {
      setLoading(false);
    }
  };

  const getClassName = (res) => {
    if (!res) return "verdict-equal";
    if (res.includes("GREATER") || res === "GREATER") return "verdict-greater";
    if (res.includes("LESS") || res === "LESS") return "verdict-less";
    return "verdict-equal";
  };

  const getVerdictText = (res) => {
    if (!res) return "";
    if (res.includes("GREATER") || res === "GREATER") return "Value A is greater than Value B";
    if (res.includes("LESS") || res === "LESS") return "Value A is less than Value B";
    return "Both values are equal";
  };

  return (
    <div>
      <div className="page-title">Quantity Comparison</div>
      <div className="page-sub">Compare two custom values with reactive unit alignment</div>
      
      <QtyTabs qty={qty} setQty={handleQtyChange} />
      
      <div className="grid-2">
        <div style={{ display: "flex", flexDirection: "column", gap: "1.2rem" }}>
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
          
          <div style={{ textAlign: "center", fontSize: 24, color: "var(--text-muted)", margin: "10px 0" }}>vs</div>
          
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
            {loading ? "⏳ Comparing…" : "⚡ Execute Comparison"}
          </button>
        </div>

        <div className="card" style={{ display: 'flex', flexDirection: 'column' }}>
          <div className="card-title">Comparison Result</div>
          <div className="result-panel" style={{ flex: 1 }}>
            {result === null ? (
              <div className="result-empty">
                <span style={{ fontSize: '24px', display: 'block', marginBottom: '8px' }}>⚖️</span>
                Comparison data calculation pending...
              </div>
            ) : (
              <div className={`compare-verdict ${getClassName(result)}`}>
                {result.includes("GREATER") ? "🏆 " : result.includes("LESS") ? "📉 " : "⚖️ "}
                {getVerdictText(result)}
              </div>
            )}
          </div>
        </div>
      </div>
    </div>
  );
}