/*const BASE_URL = "http://localhost:8080/api";
//const BASE_URL = "https://smart-traffic-system-y862.onrender.com";
// STORAGE
function saveToken(token) { localStorage.setItem("token", token); }
function getToken() { return localStorage.getItem("token"); }

function saveUser(user) {
  localStorage.setItem("username", user);
}

function saveRole(role) {
  localStorage.setItem("role", role);
}
function getRole() {
  return localStorage.getItem("role");
}

function logout() {
  localStorage.clear();
  window.location.href = "/login.html";
}

// AUTH CHECK
function requireAuth(role) {
  const token = localStorage.getItem("token");
  const userRole = localStorage.getItem("role");

  // no token → redirect
  if (!token) {
    window.location.replace("/login.html"); // 🔥 replace instead of href
    return;
  }

  // role mismatch → redirect (NOT logout)
  if (role && userRole !== role && !(role === "USER" && userRole === "ADMIN")) {
    alert("Access denied");
    window.location.replace("/login.html");
  }
}

// REQUEST
async function request(url, method = "GET", body = null) {
  const token = getToken();

  const res = await fetch(BASE_URL + url, {
    method,
    headers: {
      "Content-Type": "application/json",
      "Authorization": "Bearer " + token
    },
    body: body ? JSON.stringify(body) : null
  });

  if (res.status === 401 || res.status === 403) {
    console.warn("Unauthorized request");
    return null;   // ✅ DO NOT logout automatically
  }

  return await res.json();
}



// AUTH
const Auth = {
  login: async (username, password) => {
    const res = await request("/auth/login", "POST", { username, password });

    if (res && res.data) {
      saveToken(res.data.token);
      saveUser(res.data.username);
      saveRole(res.data.role);
    }

    return res;
  },

  // ✅ ADD THIS
  register: async (username, password) => {
    return await request("/auth/register", "POST", {
      username,
      password
    });
  }
};

// APIs
// ================= TRAFFIC =================
const Traffic = {
  getAll: () => request("/traffic"),
  create: (data) => request("/traffic", "POST", data),
  update: (id, data) => request(`/traffic/${id}`, "PUT", data),
  delete: (id) => request(`/traffic/${id}`, "DELETE"),
};

// ================= ALERTS =================
const Alerts = {
  getAll: () => request("/alerts"),

  create: (data) => request("/alerts", "POST", data),

  delete: (id) => request(`/alerts/${id}`, "DELETE"),

  resolve: (id) => request(`/alerts/${id}/resolve`, "PUT")   // ✅ ADD THIS
};

// ================= DASHBOARD =================
const Dashboard = {
  getStats: () => request("/dashboard/stats"),
};
*/


// ================= BASE URL (AUTO FIX) =================

// Automatically uses Render URL in production
const BASE_URL = window.location.origin + "/api";

// ================= STORAGE =================
function saveToken(token) { localStorage.setItem("token", token); }
function getToken() { return localStorage.getItem("token"); }

function saveUser(user) {
  localStorage.setItem("username", user);
}

function saveRole(role) {
  localStorage.setItem("role", role);
}

function getRole() {
  return localStorage.getItem("role");
}

function logout() {
  localStorage.clear();
  window.location.href = "/login.html";
}

// ================= AUTH CHECK =================
function requireAuth(role) {
  const token = getToken();
  const userRole = getRole();

  if (!token) {
    window.location.replace("/login.html");
    return;
  }

  if (role && userRole !== role && !(role === "USER" && userRole === "ADMIN")) {
    alert("Access denied");
    window.location.replace("/login.html");
  }
}

// ================= REQUEST =================
async function request(url, method = "GET", body = null) {
  const token = getToken();

  try {
    const res = await fetch(BASE_URL + url, {
      method,
      headers: {
        "Content-Type": "application/json",
        ...(token && { Authorization: "Bearer " + token })
      },
      body: body ? JSON.stringify(body) : null
    });

    const data = await res.json();

    // ❗ Handle server errors properly
    if (!res.ok) {
      console.error("API ERROR:", data);
      return data;
    }

    return data;

  } catch (err) {
    console.error("NETWORK ERROR:", err);
    return {
      status: 500,
      message: "Network error",
      data: null
    };
  }
}

// ================= AUTH =================
const Auth = {

  login: async (username, password) => {
    const res = await request("/auth/login", "POST", { username, password });

    if (res && res.data) {
      saveToken(res.data.token);
      saveUser(res.data.username);
      saveRole(res.data.role);
    }

    return res;
  },

  register: async (username, email, password) => {
    return await request("/auth/register", "POST", {
      username,
      email,
      password
    });
  }
};

// ================= TRAFFIC =================
const Traffic = {
  getAll: () => request("/traffic"),
  create: (data) => request("/traffic", "POST", data),
  update: (id, data) => request(`/traffic/${id}`, "PUT", data),
  delete: (id) => request(`/traffic/${id}`, "DELETE"),
};

// ================= ALERTS =================
const Alerts = {
  getAll: () => request("/alerts"),
  create: (data) => request("/alerts", "POST", data),
  delete: (id) => request(`/alerts/${id}`, "DELETE"),
  resolve: (id) => request(`/alerts/${id}/resolve`, "PUT")
};

// ================= DASHBOARD =================
const Dashboard = {
  getStats: () => request("/dashboard/stats"),
};
