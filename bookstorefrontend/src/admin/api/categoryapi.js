import axios from 'axios';
import CookieService from './cookie'; // Import service để làm việc với cookie

const apiUrl = "http://localhost:8080/api/admin";

const categoryApi = {
  getAllCategories: async () => {
    try {
      const headers = await getRequestHeaders();
      const response = await axios.get(`${apiUrl}/categories`, { headers });
      return response.data;
    } catch (error) {
      console.error(error);
      throw error;
    }
  },

  getBooksByCategoryId: async (id) => {
    try {
      const headers = await getRequestHeaders();
      const response = await axios.get(`${apiUrl}/categories/${id}/books`, { headers });
      return response.data;
    } catch (error) {
      console.error(error);
      throw error;
    }
  },
  
  createCategory: async (categoryData) => {
    try {
      const headers = await getRequestHeaders();
      const response = await axios.post(`${apiUrl}/categories`, categoryData, { headers });
      return response.data;
    } catch (error) {
      console.error(error);
      throw error;
    }
  },

  deleteCategory: async (categoryId) => {
    try {
      const headers = await getRequestHeaders();
      const response = await axios.delete(`${apiUrl}/categories/delete?id=${categoryId}`, { headers });
      return response.data;
    } catch (error) {
      console.error(error);
      throw error;
    }
  }
};

const getRequestHeaders = async () => {
  const token = CookieService.getCookie('token');
  if (!token) {
    console.error("Token not found in cookie.");
    throw new Error("Token not found in cookie.");
  }
  return { Authorization: `Bearer ${token}` };
};

export default categoryApi;
