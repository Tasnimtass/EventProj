import axios from 'axios';

// ✅ Crée une instance d'axios
const axiosInstance = axios.create({
  baseURL: 'http://localhost:1111',
});

// ✅ Ajoute un intercepteur pour ajouter le token à chaque requête
axiosInstance.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token'); 
    console.log("Token utilisé:", token); // Vérification
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

export default axiosInstance;
