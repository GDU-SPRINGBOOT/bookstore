import axios from "axios";
import CookieService from "./cookie";

const apiUrl = "http://localhost:8080/api/admin";

const orderApi = {
  getAllOrders: async () => {
    try {
      const headers = await getRequestHeaders();
      const response = await axios.get(`${apiUrl}/order/getAll`, { headers });
      return response.data;
    } catch (error) {
      console.error(error);
      throw error;
    }
  },

  getOrderById: async (orderId) => {
    try {
      const headers = await getRequestHeaders();
      const response = await axios.get(`${apiUrl}/order/${orderId}`, {
        headers,
      });
      return response.data;
    } catch (error) {
      console.error(error);
      throw error;
    }
  },

  getUserOrders: async (userID) => {
    try {
      const headers = await getRequestHeaders();
      const response = await axios.get(`${apiUrl}/order/user?id=${userID}`, {
        headers,
      });
      return response.data;
    } catch (error) {
      console.error(error);
      throw error;
    }
  },

  getOrderStatus: async () => {
    try {
      const headers = await getRequestHeaders();
      const response = await axios.get(`${apiUrl}/order/status`, {
        headers,
      });
      return response.data;
    } catch (error) {
      console.error(error);
      throw error;
    }
  },

  updateOrderStatus: async (orderId, status) => {
    try {
      const headers = await getRequestHeaders();
      const response = await axios.post(
        `${apiUrl}/order/update/${orderId}?s=${status}`,
        {},
        { headers }
      );
      return response.data;
    } catch (error) {
      console.error(error);
      throw error;
    }
  },
};

const getRequestHeaders = async () => {
  const token = CookieService.getCookie("token");
  if (!token) {
    console.error("Token not found in cookie.");
    throw new Error("Token not found in cookie.");
  }
  return { Authorization: `Bearer ${token}` };
};

export default orderApi;
