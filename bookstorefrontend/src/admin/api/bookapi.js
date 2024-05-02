import axios from 'axios';
import CookieService from './cookie';

const apiUrl = "http://localhost:8080/api/admin";

const bookApi = {
  getAll: async (params) => {
    try {
      const response = await requestWithToken(`${apiUrl}/book/getAll`, params);
      return response.data;
    } catch (error) {
      handleRequestError(error);
    }
  },
  getById: async (id) => {
    try {
      const response = await requestWithToken(`${apiUrl}/book/findById/${id}`);
      return response.data;
    } catch (error) {
      handleRequestError(error);
    }
  },
  addBook: async (bookData) => {
    try {
      const response = await requestWithToken(`${apiUrl}/book/add`, {}, 'post', bookData);
      return response.data;
    } catch (error) {
      handleRequestError(error);
    }
  },
  updateBook: async (id, bookData) => {
    try {
      const response = await requestWithToken(`${apiUrl}/book/update/${id}`, {}, 'patch', bookData);
      return response.data;
    } catch (error) {
      handleRequestError(error);
    }
  },
  deleteBook: async (id) => {
    try {
      const response = await requestWithToken(`${apiUrl}/book/delete/${id}`, {}, 'delete');
      return response.data;
    } catch (error) {
      handleRequestError(error);
    }
  }
};

const requestWithToken = async (url, params = {}, method = 'get', data = {}) => {
  const token = CookieService.getCookie('token');
  if (!token) {
    console.error("Token not found in cookie.");
    return Promise.reject("Token not found in cookie.");
  }
  const headers = { Authorization: `Bearer ${token}` };
  try {
    return await axios({ method, url, params, data, headers });
  } catch (error) {
    throw error.response.data || error.message;
  }
};

const handleRequestError = (error) => {
  console.error("Request error:", error);
  throw error;
};

export default bookApi;