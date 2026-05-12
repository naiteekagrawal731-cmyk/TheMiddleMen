/**
 * Utility to display alerts
 */
function showAlert(message, type = 'error') {
    const alertBox = document.getElementById('alertBox');
    if (!alertBox) return;

    alertBox.textContent = message;
    alertBox.className = `alert alert-${type}`;
    alertBox.style.display = 'block';

    setTimeout(() => {
        alertBox.style.display = 'none';
    }, 5000);
}

/**
 * Handle user registration
 */
async function handleRegister(event) {
    event.preventDefault();
    const btn = event.target.querySelector('button[type="submit"]');
    const originalText = btn.textContent;
    btn.textContent = 'Registering...';
    btn.disabled = true;

    const username = document.getElementById('username').value;
    const password = document.getElementById('password').value;

    try {
        const response = await fetch(`${BASE_URL}/api/register`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ username, password })
        });

        if (response.ok) {
            const data = await response.json();
            showAlert(data.message || 'Registration Complete! Redirecting...', 'success');
            setTimeout(() => {
                window.location.href = 'login.html';
            }, 2000);
        } else {
            showAlert('Registration failed. Username might already exist.');
        }
    } catch (error) {
        console.error(error);
        showAlert('Network error. Please try again.');
    } finally {
        btn.textContent = originalText;
        btn.disabled = false;
    }
}

/**
 * Handle user login
 */
async function handleLogin(event) {
    event.preventDefault();
    const btn = event.target.querySelector('button[type="submit"]');
    const originalText = btn.textContent;
    btn.textContent = 'Logging in...';
    btn.disabled = true;

    const username = document.getElementById('username').value;
    const password = document.getElementById('password').value;

    try {
        // We use standard fetch here because apiFetch requires auth tokens 
        // and login is the starting point to get the refresh_token cookie
        const response = await fetch(`${BASE_URL}/api/login`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            credentials: 'include', // Needed so the server sets the refresh_token cookie in the browser
            body: JSON.stringify({ username, password })
        });

        if (response.ok) {
            // Success, the backend should have set a refresh_token cookie.
            // Now we can go to the dashboard.
            window.location.href = 'index.html';
        } else {
            showAlert('Invalid username or password.');
        }
    } catch (error) {
        console.error(error);
        showAlert('Network error. Please make sure the backend is running.');
    } finally {
        btn.textContent = originalText;
        btn.disabled = false;
    }
}

/**
 * Handle Google OAuth2 Login
 */
function handleGoogleLogin() {
    // Determine the dashboard URL to return to after successful authentication
    let dashboardUrl = window.location.href;
    
    // Replace current path (login.html or register.html) with index.html
    if (dashboardUrl.includes('login.html')) {
        dashboardUrl = dashboardUrl.replace('login.html', 'index.html');
    } else if (dashboardUrl.includes('register.html')) {
        dashboardUrl = dashboardUrl.replace('register.html', 'index.html');
    } else {
        // Fallback
        const urlParts = dashboardUrl.split('/');
        urlParts.pop(); // remove last segment
        dashboardUrl = urlParts.join('/') + '/index.html';
    }

    // URL encode the redirect destination
    const encodedRedirect = encodeURIComponent(dashboardUrl);

    // Set the cookie for the backend SuccessHandler to read
    // Note: If running locally without a proper domain, cookie settings might be restrictive.
    // Setting path=/ allows the backend to read it when redirecting.
    document.cookie = `frontend_redirect=${encodedRedirect}; path=/; max-age=300`;

    // Redirect the browser to the Spring Boot OAuth2 endpoint
    window.location.href = `${BASE_URL}/oauth2/authorization/google`;
}
