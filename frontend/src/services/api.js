import axios from 'axios';

const API_BASE_URL = "http://localhost:8080/api/v1/quantities";

const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => Promise.reject(error)
);

export const quantityService = {
  // ─── 1. COMPARISON ENDPOINT ───
  compare: async (value1, unit1, value2, unit2, qtyType) => {
    const response = await api.post('/compare', {
      thisQuantityDTO: { value: parseFloat(value1), unit: unit1, measurementType: qtyType }, // <-- quantityType ko measurementType kiya
      thatQuantityDTO: { value: parseFloat(value2), unit: unit2, measurementType: qtyType }  // <-- quantityType ko measurementType kiya
    });
    return response.data;
  },

  // ─── 2. ARITHMETIC ENDPOINTS ───
  performArithmetic: async (value1, unit1, value2, unit2, operation, qtyType) => {
    let endpoint = "/add";
    if (operation === "SUBTRACT") endpoint = "/subtract";
    if (operation === "DIVIDE") endpoint = "/divide";

    const response = await api.post(endpoint, {
      thisQuantityDTO: { value: parseFloat(value1), unit: unit1, measurementType: qtyType },
      thatQuantityDTO: { value: parseFloat(value2), unit: unit2, measurementType: qtyType },
      targetUnit: unit1
    });
    return response.data;
  },

  // ─── 3. CONVERSION ENDPOINT ───
  convert: async (value, fromUnit, toUnit, qtyType) => {
    const response = await api.post('/convert', {
      thisQuantityDTO: { value: parseFloat(value), unit: fromUnit, measurementType: qtyType },
      targetUnit: toUnit
    });
    return response.data;
  }
};

export default api;