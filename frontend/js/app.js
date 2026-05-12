document.addEventListener('DOMContentLoaded', async () => {
    // 1. Ensure the user is authenticated before showing the dashboard
    await ensureAuthenticated();
    
    // 2. Fetch the data concurrently
    fetchDashboardData();
});

async function fetchDashboardData() {
    // Show loaders initially (handled in HTML by default)
    try {
        const [channelRes, videoRes, playlistRes] = await Promise.all([
            apiFetch('/api/youtube/channel'),
            apiFetch('/api/youtube/videos'),
            apiFetch('/api/youtube/uploadPlaylist')
        ]);

        if (channelRes.ok) {
            const channelData = await channelRes.json();
            renderChannelInfo(channelData);
        } else {
            document.getElementById('channel-content').innerHTML = '<p class="alert alert-error">Failed to load channel data.</p>';
        }

        if (videoRes.ok) {
            const videoData = await videoRes.json();
            renderVideoInfo(videoData);
        } else {
            document.getElementById('video-content').innerHTML = '<p class="alert alert-error">Failed to load video data.</p>';
        }

        if (playlistRes.ok) {
            const playlistData = await playlistRes.json();
            renderPlaylistInfo(playlistData);
        } else {
            document.getElementById('playlist-content').innerHTML = '<p class="alert alert-error">Failed to load playlist data.</p>';
        }

    } catch (error) {
        console.error('Error fetching dashboard data:', error);
        showAlert('An error occurred while fetching data from the server.');
    }
}

function renderChannelInfo(data) {
    // Attempting to render arbitrary channel info dynamically since we don't have the exact DTO structure locally,
    // we'll assume it has basic properties or we'll render it as JSON if it's complex.
    const container = document.getElementById('channel-content');
    
    // Fallback simple render:
    let html = '';
    for (const [key, value] of Object.entries(data)) {
        if(typeof value !== 'object') {
            html += `
            <div class="data-row">
                <span class="data-label">${formatKey(key)}</span>
                <span class="data-value">${value}</span>
            </div>`;
        }
    }
    
    container.innerHTML = html || `<pre style="white-space: pre-wrap; font-size: 0.85rem;">${JSON.stringify(data, null, 2)}</pre>`;
}

function renderVideoInfo(data) {
    const container = document.getElementById('video-content');
    container.innerHTML = `<pre style="white-space: pre-wrap; font-size: 0.85rem;">${JSON.stringify(data, null, 2)}</pre>`;
}

function renderPlaylistInfo(data) {
    const container = document.getElementById('playlist-content');
    container.innerHTML = `<pre style="white-space: pre-wrap; font-size: 0.85rem;">${JSON.stringify(data, null, 2)}</pre>`;
}

function formatKey(key) {
    return key.replace(/([A-Z])/g, ' $1').replace(/^./, str => str.toUpperCase());
}

function logout() {
    // Clear in-memory token and redirect
    accessToken = null;
    // To fully logout, we'd ideally tell the backend to delete the refresh_token cookie
    // But simply redirecting is enough for this frontend prototype.
    window.location.href = 'login.html';
}
