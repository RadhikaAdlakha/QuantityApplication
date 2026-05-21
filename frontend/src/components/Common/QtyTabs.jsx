import React from 'react';

export const UNITS = {
  LENGTH: ["FEET", "INCH", "YARDS", "CENTIMETERS"],
  WEIGHT: ["KILOGRAM", "GRAM", "POUND"],
  VOLUME: ["LITRE", "MILLILITRE", "GALLON"],
  TEMPERATURE: ["CELSIUS", "FAHRENHEIT", "KELVIN"],
};

export const UNIT_SYMBOLS = {
  FEET: "ft", INCH: "in", YARDS: "yd", CENTIMETERS: "cm",
  KILOGRAM: "kg", GRAM: "g", POUND: "lb",
  LITRE: "L", MILLILITRE: "mL", GALLON: "gal",
  CELSIUS: "°C", FAHRENHEIT: "°F", KELVIN: "K",
};

export const QTY_TYPES = ["LENGTH", "WEIGHT", "VOLUME", "TEMPERATURE"];
export const QTY_ICONS = { LENGTH: "📏", WEIGHT: "⚖️", VOLUME: "🧪", TEMPERATURE: "🌡️" };

export function fmt(num) {
  if (num === null || num === undefined || isNaN(num)) return "—";
  const n = parseFloat(num);
  if (Math.abs(n) >= 1e9 || (Math.abs(n) < 0.0001 && n !== 0)) return n.toExponential(4);
  return parseFloat(n.toPrecision(8)).toString();
}

export function unitLabel(u) { return `${u} (${UNIT_SYMBOLS[u] || u})`; }

export default function QtyTabs({ qty, setQty }) {
  return (
    <div className="qty-tabs">
      {QTY_TYPES.map(q => (
        <button key={q} className={`qty-tab${qty === q ? " active" : ""}`} onClick={() => setQty(q)}>
          {QTY_ICONS[q]} {q[0] + q.slice(1).toLowerCase()}
        </button>
      ))}
    </div>
  );
}