document.addEventListener('DOMContentLoaded', () => {
    checkAuth();

    const user = Auth.getUser();
    const userId = Auth.getUserId();

    // Set username
    document.getElementById('username').textContent = user?.username || 'User';

    // Logout button
    document.getElementById('logoutBtn').addEventListener('click', () => {
        Auth.logout();
    });

    // Load dashboard data
    loadDashboardData();

    // Quick session form
    document.getElementById('quickSessionForm').addEventListener('submit', async (e) => {
        e.preventDefault();

        const title = document.getElementById('sessionTitle').value;
        const duration = parseInt(document.getElementById('sessionDuration').value);
        const tagsInput = document.getElementById('sessionTags').value;
        const tags = tagsInput ? tagsInput.split(',').map(t => t.trim()) : [];

        try {
            const sessionData = {
                title,
                duration,
                userId,
                tags,
                status: 'IN_PROGRESS',
                sessionType: 'FOCUS'
            };

            const session = await API.sessions.create(sessionData);
            await API.sessions.start(session.id);

            alert('Session started successfully!');
            e.target.reset();
            loadRecentSessions();
            loadStats();
        } catch (error) {
            alert('Failed to start session: ' + error.message);
        }
    });

    async function loadDashboardData() {
        try {
            await Promise.all([
                loadStats(),
                loadRecentSessions(),
                loadUserTags()
            ]);
        } catch (error) {
            console.error('Error loading dashboard data:', error);
        }
    }

    async function loadStats() {
        try {
            const stats = await API.users.getStats(userId);

            document.getElementById('totalSessions').textContent = stats.totalSessions || 0;
            document.getElementById('totalTime').textContent = formatDuration(stats.totalFocusTime || 0);
            document.getElementById('currentStreak').textContent = stats.currentStreak || 0;
            document.getElementById('totalPoints').textContent = stats.totalPoints || 0;
        } catch (error) {
            console.error('Error loading stats:', error);
        }
    }

    async function loadRecentSessions() {
        try {
            const sessions = await API.sessions.getUserSessions(userId);
            const recentSessions = sessions.slice(0, 5);

            const container = document.getElementById('recentSessions');

            if (recentSessions.length === 0) {
                container.innerHTML = '<p class="empty-state">No sessions yet. Start your first one!</p>';
                return;
            }

            container.innerHTML = recentSessions.map(session => `
                <div class="session-item">
                    <div class="session-item-info">
                        <h4>${session.title}</h4>
                        <p>${formatDuration(session.duration)} - ${formatDate(session.startTime)}</p>
                    </div>
                    <span class="status-badge ${session.status.toLowerCase().replace('_', '-')}">
                        ${session.status}
                    </span>
                </div>
            `).join('');
        } catch (error) {
            console.error('Error loading recent sessions:', error);
        }
    }

    async function loadUserTags() {
        try {
            const tags = await API.tags.getUserTags(userId);
            const container = document.getElementById('tagsContainer');

            if (tags.length === 0) {
                container.innerHTML = '<p class="empty-state">No tags yet.</p>';
                return;
            }

            container.innerHTML = tags.map(tag => `
                <div class="tag">
                    ${tag.name}
                    <span class="tag-count">${tag.usageCount || 0}</span>
                </div>
            `).join('');
        } catch (error) {
            console.error('Error loading tags:', error);
        }
    }
});