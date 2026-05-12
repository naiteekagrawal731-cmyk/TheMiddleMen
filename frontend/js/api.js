const BASE_URL = 'http://localhost:8080';

// Global variable to hold our in-memory access token
let accessToken = null;

/**
 * Enhanced fetch wrapper that automatically handles access tokens
 * and attempts to refresh them if a 401 response is received.
 */
async function apiFetch(endpoint, options = {}) {
    const url = `${BASE_URL}${endpoint}`;
    
    // Default options
    const fetchOptions = {
        ...options,
        headers: {
            'Content-Type': 'application/json',
            ...options.headers,
        },
        // Very important for sending/receiving the refresh_token cookie
        credentials: 'include', 
    };

    // If we have an access token, attach it
    if (accessToken) {
        fetchOptions.headers['Authorization'] = `Bearer ${accessToken}`;
    }

    let response = await fetch(url, fetchOptions);

    // If unauthorized, it might mean our access token expired (or we don't have one)
    if (response.status === 401 || response.status === 403) {
        // Attempt to refresh the token using the refresh_token cookie
        const refreshSuccess = await refreshAccessToken();
        
        if (refreshSuccess) {
            // Retry original request with new token
            fetchOptions.headers['Authorization'] = `Bearer ${accessToken}`;
            response = await fetch(url, fetchOptions);
        } else {
            // Refresh failed, user is actually logged out or token is invalid
            // Redirect to login page if we aren't already there
            if (!window.location.pathname.includes('login.html') && !window.location.pathname.includes('register.html')) {
                window.location.href = 'login.html';
            }
        }
    }

    return response;
}

/**
 * Calls the backend to exchange the refresh_token cookie for a new access token.
 */
async function refreshAccessToken() {
    try {
        const response = await fetch(`${BASE_URL}/api/auth/token`, {
            method: 'GET',
            credentials: 'include'
        });

        if (response.ok) {
            const data = await response.json();
            if (data.accessToken) {
                accessToken = data.accessToken;
                return true;
            }
        }
        return false;
    } catch (error) {
        console.error('Failed to refresh token:', error);
        return false;
    }
}

// Ensure the token is fetched on app load if we are on a protected page
async function ensureAuthenticated() {
    if (!accessToken) {
        const hasToken = await refreshAccessToken();
        if (!hasToken) {
            window.location.href = 'login.html';
        }
    }
}
