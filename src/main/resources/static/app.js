const API_BASE = 'http://localhost:8080/api';

const loginSection = document.getElementById('login-section');
const registerSection = document.getElementById('register-section');
const dashboardSection = document.getElementById('dashboard-section');
const changePasswordSection = document.getElementById('change-password-section');

const loginBtn = document.getElementById('login-btn');
const registerBtn = document.getElementById('register-btn');
const oauthBtn = document.getElementById('oauth-btn');
const logoutBtn = document.getElementById('logout-btn');

const goRegisterBtn = document.getElementById('go-register-btn');
const goLoginBtn = document.getElementById('go-login-btn');

const goChangePasswordBtn = document.getElementById('go-change-password-btn');
const backDashboardBtn = document.getElementById('back-dashboard-btn');

const widgetsContainer = document.getElementById('widgets');
const videosContainer = document.getElementById("videos-container");

const changePasswordBtn = document.getElementById("change-password-btn");

let accessToken = null;
let refreshPromise = null;

/* ---------------- UI ---------------- */

function showLogin() {
  loginSection.style.display = "block";
  registerSection.style.display = "none";
  dashboardSection.style.display = "none";
  if(changePasswordSection) changePasswordSection.style.display = "none";
}

function showRegister() {
  loginSection.style.display = "none";
  registerSection.style.display = "block";
  dashboardSection.style.display = "none";
  if(changePasswordSection) changePasswordSection.style.display = "none";
}

function showDashboardShell() {
  loginSection.style.display = "none";
  registerSection.style.display = "none";
  dashboardSection.style.display = "block";
  if(changePasswordSection) changePasswordSection.style.display = "none";
}

/* NEW: show change password page */

function showChangePasswordPage() {
  loginSection.style.display = "none";
  registerSection.style.display = "none";
  dashboardSection.style.display = "none";
  changePasswordSection.style.display = "block";
}

function formatNumber(num) {
  num = Number(num || 0);
  if (num >= 1_000_000_000) return (num / 1_000_000_000).toFixed(1) + "B";
  if (num >= 1_000_000) return (num / 1_000_000).toFixed(1) + "M";
  if (num >= 1_000) return (num / 1_000).toFixed(1) + "K";
  return num.toString();
}

/* ---------------- Fetch Wrapper ---------------- */

async function requestWithToken(url, options = {}, retry = true) {
  const headers = options.headers || {};

  if (accessToken) {
    headers["Authorization"] = `Bearer ${accessToken}`;
  }

  const res = await fetch(`${API_BASE}${url}`, {
    credentials: "include",
    headers: { "Content-Type": "application/json", ...headers },
    ...options
  });

  if (res.status === 401 && retry) {
    const newToken = await refreshAccessToken();
    if (!newToken) {
      accessToken = null;
      showLogin();
      return null;
    }
    return requestWithToken(url, options, false);
  }

  if (!res.ok) return null;

  const data = await res.json().catch(() => null);
  return { status: res.status, data };
}

/* ---------------- Token Refresh ---------------- */

async function refreshAccessToken() {
  if (!refreshPromise) {
    refreshPromise = fetch(`${API_BASE}/auth/token`, {
      method: "GET",
      credentials: "include"
    })
    .then(async res => {
      refreshPromise = null;
      if (!res.ok) return null;
      const data = await res.json();
      accessToken = data?.accessToken || null;
      return accessToken;
    })
    .catch(() => {
      refreshPromise = null;
      return null;
    });
  }
  return refreshPromise;
}

/* ---------------- Render Channel ---------------- */

function renderChannelWidget(channelResponse) {

  widgetsContainer.innerHTML = "";

  const channels = channelResponse?.data?.items || [];

  if (!channels.length) {
    widgetsContainer.innerHTML = "<div class='widget'>No channel data</div>";
    return;
  }

  const channel = channels[0];

  const title = channel?.snippet?.title || "Channel";
  const description = channel?.snippet?.description || "";

  const subscribers = formatNumber(channel?.statistics?.subscriberCount);
  const views = formatNumber(channel?.statistics?.viewCount);
  const videos = formatNumber(channel?.statistics?.videoCount);

  const widget = document.createElement("div");
  widget.className = "widget";

  widget.innerHTML = `
    <div class="widget-title">YouTube Channel</div>

    <div class="widget-channel">
      <div class="widget-channel-title">${title}</div>
      <div class="widget-description">${description}</div>
    </div>

    <div class="widget-stats">
      <div class="stat-block">
        <span>${subscribers}</span>
        <label>Subscribers</label>
      </div>
      <div class="stat-block">
        <span>${views}</span>
        <label>Views</label>
      </div>
      <div class="stat-block">
        <span>${videos}</span>
        <label>Videos</label>
      </div>
    </div>
  `;

  widgetsContainer.appendChild(widget);
}

/* ---------------- Render Videos ---------------- */

function renderVideos(videoResponse) {
  const videos = videoResponse?.data?.items || [];
  videosContainer.innerHTML = "";

  if (!videos.length) {
    videosContainer.innerHTML = "<p>No videos found</p>";
    return;
  }

  const validVideos = videos.filter(video => video?.id && video?.snippet);

  if (!validVideos.length) {
    videosContainer.innerHTML = "<p>No valid videos to show</p>";
    return;
  }

  const seen = new Set();

  validVideos.forEach(video => {

    if (seen.has(video.id)) return;
    seen.add(video.id);

    const title = video.snippet.title || "Untitled";

    const thumbnail =
      video.snippet.thumbnails?.high?.url ||
      video.snippet.thumbnails?.medium?.url ||
      "";

    const views = formatNumber(video.statistics?.viewCount);
    const likes = formatNumber(video.statistics?.likeCount);
    const comments = formatNumber(video.statistics?.commentCount);

    const card = document.createElement("div");
    card.className = "video-card";

    card.innerHTML = `
      <img src="${thumbnail}" class="video-thumb"/>
      <div class="video-info">
        <div class="video-title">${title}</div>
        <div class="video-meta">
          ${views} views • ${likes} likes • ${comments} comments
        </div>
      </div>
    `;

    videosContainer.appendChild(card);
  });
}

/* ---------------- Dashboard ---------------- */

async function showDashboard() {

  showDashboardShell();

  widgetsContainer.innerHTML = "<div class='widget'>Loading channel...</div>";
  videosContainer.innerHTML = "Loading videos...";

  const token = await refreshAccessToken();

  if (!token) {
    showLogin();
    return;
  }

  const [channelResponse, videoResponse] = await Promise.all([
    requestWithToken('/youtube/channel'),
    requestWithToken('/youtube/videos')
  ]);

  if (channelResponse) {
    renderChannelWidget(channelResponse);
  } else {
    widgetsContainer.innerHTML = "<div class='widget'>Failed to load channel</div>";
  }

  if (videoResponse) {
    renderVideos(videoResponse);
  } else {
    videosContainer.innerHTML = "<p>Unable to load videos</p>";
  }
}

/* ---------------- Auth ---------------- */

loginBtn?.addEventListener("click", async () => {

  const username = document.getElementById('login-username').value.trim();
  const password = document.getElementById('login-password').value.trim();

  const res = await fetch(`${API_BASE}/login`, {
    method: "POST",
    credentials: "include",
    body: JSON.stringify({ username, password }),
    headers: { "Content-Type": "application/json" }
  });

  if (!res.ok) return alert("Login failed");

  const token = await refreshAccessToken();
  if (!token) return alert("Token error");

  showDashboard();
});

registerBtn?.addEventListener("click", async () => {

  const username = document.getElementById('register-username').value.trim();
  const password = document.getElementById('register-password').value.trim();

  const res = await fetch(`${API_BASE}/register`, {
    method: "POST",
    body: JSON.stringify({ username, password }),
    headers: { "Content-Type": "application/json" }
  });

  if (!res.ok) return alert("Registration failed");

  alert("Registration successful");
  showLogin();
});

/* ---------------- OAuth2 ---------------- */

oauthBtn?.addEventListener("click", () => {

  let frontendRedirect;

  if (window.location.hostname === "localhost" && window.location.port === "63342") {
    frontendRedirect = "http://localhost:3000/";
  } else {
    frontendRedirect = window.location.origin + "/";
  }

  document.cookie = `frontend_redirect=${encodeURIComponent(frontendRedirect)}; path=/; SameSite=Lax;`;

  window.location.href = `${API_BASE}/auth/oauth2/authorize/google`;
});

logoutBtn?.addEventListener("click", async () => {
  await requestWithToken('/auth/logout', { method: "POST" });
  accessToken = null;
  showLogin();
});

/* ---------------- Navigation ---------------- */

goRegisterBtn?.addEventListener("click", showRegister);
goLoginBtn?.addEventListener("click", showLogin);

goChangePasswordBtn?.addEventListener("click", showChangePasswordPage);
backDashboardBtn?.addEventListener("click", showDashboard);

window.addEventListener("DOMContentLoaded", async () => {
  const token = await refreshAccessToken();
  token ? showDashboard() : showLogin();
});

/* ---------------- Change Password ---------------- */

changePasswordBtn?.addEventListener("click", async () => {

  const newPassword = document.getElementById("new-password").value.trim();

  if (!newPassword) {
    alert("Enter new password");
    return;
  }

  const res = await requestWithToken('/changepassword', {
    method: "POST",
    body: JSON.stringify({
      newPassword: newPassword
    })
  });

  if (!res) {
    alert("Password change failed");
    return;
  }

  alert("Password updated successfully");

  document.getElementById("new-password").value = "";
});