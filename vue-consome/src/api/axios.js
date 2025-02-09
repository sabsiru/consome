import axios from "axios";

const token = localStorage.getItem("access_token");

if (token) {
  axios.defaults.headers.common["Authorization"] = `Bearer ${token}`;
}

export default axios;