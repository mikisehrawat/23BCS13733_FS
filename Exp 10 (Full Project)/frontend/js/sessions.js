
document.addEventListener('DOMContentLoaded', () => {
    checkAuth();

    const userId = Auth.getUserId();
    let allSessions = [];

    // Logout button
    document.getElementById('logoutBtn').addEventListener('click', () => {
        Auth.logout();
    });

    // Load sessions
    loadSessions();

    // Filters
    document.getElementById('statusFilter').addEventListener('change', applyFilters);
    document.getElementById('dateFilter').addEventListener('change', applyFilters);
    document.getElementById('searchFilter').addEventListener('input', applyFilters);

    // New session button
    document.getElementById('newSessionBtn').addEventListener('click', () => {
        openModal();
    });

    // Modal close
    document.querySelector('.close').addEventListener('click', closeModal);
    document.getElementById('cancelBtn').addEventListener('click', closeModal);

    // Session form
    document.getElementById('sessionForm').addEventListener('submit', handleSessionSubmit);

    async function loadSessions() {
        try {
            allSessions = await API.sessions.getUserSessions(userId);
            displaySessions(allSessions);
        } catch (error) {
            console.error('Error loading sessions:', error);
            document.getElementById('sessionsGrid').innerHTML =
                '<p class="empty-state">Failed to load sessions</p>';
        }
    }

    function displaySessions(sessions) {
        const container = document.getElementById('sessionsGrid');

        if (sessions.length === 0) {
            container.innerHTML = '<p class="empty-state">No sessions found</p>';
            return;
        }

        container.innerHTML = sessions.map(session => `
            <div class="session-card">
                <h3>${session.title}</h3>
                <div class="session-meta">
                    <span>⏱️ ${formatDuration(session.duration)}</span>
                    <span>📅 ${formatDate(session.startTime)}</span>
                    <span class="status-badge ${session.status.toLowerCase().replace('_', '-')}">
                        ${session.status}
                    </span>
                    ${session.tags?.length ? `<div class="tags-container">
                        ${session.tags.map(tag => `<span class="tag">${tag}</span>`).join('')}
                    </div>` : ''}
                </div>
                <div class="session-actions">
                    ${session.status === 'IN_PROGRESS' ?
            `<button class="btn btn-primary" onclick="pauseSession('${session.id}')">Pause</button>
                         <button class="btn btn-success" onclick="completeSession('${session.id}')">Complete</button>` : ''}
                    ${session.status === 'PAUSED' ?
            `<button class="btn btn-primary" onclick="resumeSession('${session.id}')">Resume</button>
                         <button class="btn btn-success" onclick="completeSession('${session.id}')">Complete</button>` : ''}
                    <button class="btn btn-danger" onclick="deleteSession('${session.id}')">Delete</button>
                </div>
            </div>
        `).join('');
    }

    function applyFilters() {
        const statusFilter = document.getElementById('statusFilter').value;
        const dateFilter = document.getElementById('dateFilter').value;
        const searchFilter = document.getElementById('searchFilter').value.toLowerCase();

        let filtered = allSessions;

        if (statusFilter) {
            filtered = filtered.filter(s => s.status === statusFilter);
        }

        if (dateFilter) {
            filtered = filtered.filter(s => {
                const sessionDate = new Date(s.startTime).toISOString().split('T')[0];
                return sessionDate === dateFilter;
            });
        }

        if (searchFilter) {
            filtered = filtered.filter(s =>
                s.title.toLowerCase().includes(searchFilter)
            );
        }

        displaySessions(filtered);
    }

    function openModal(session = null) {
        const modal = document.getElementById('sessionModal');
        const form = document.getElementById('sessionForm');

        if (session) {
            document.getElementById('modalTitle').textContent = 'Edit Session';
            document.getElementById('sessionId').value = session.id;
            document.getElementById('title').value = session.title;
            document.getElementById('duration').value = session.duration;
            document.getElementById('sessionType').value = session.sessionType;
            document.getElementById('tags').value = session.tags?.join(', ') || '';
        } else {
            document.getElementById('modalTitle').textContent = 'New Session';
            form.reset();
            document.getElementById('sessionId').value = '';
        }

        modal.style.display = 'block';
    }

    function closeModal() {
        document.getElementById('sessionModal').style.display = 'none';
    }

    async function handleSessionSubmit(e) {
        e.preventDefault();

        const sessionId = document.getElementById('sessionId').value;
        const sessionData = {
            title: document.getElementById('title').value,
            duration: parseInt(document.getElementById('duration').value),
            sessionType: document.getElementById('sessionType').value,
            tags: document.getElementById('tags').value.split(',').map(t => t.trim()).filter(t => t),
            userId,
            status: 'PLANNED'
        };

        try {
            if (sessionId) {
                await API.sessions.update(sessionId, sessionData);
            } else {
                await API.sessions.create(sessionData);
            }

            closeModal();
            loadSessions();
        } catch (error) {
            alert('Failed to save session: ' + error.message);
        }
    }

    // Global functions for session actions
    window.pauseSession = async (id) => {
        try {
            await API.sessions.pause(id);
            loadSessions();
        } catch (error) {
            alert('Failed to pause session: ' + error.message);
        }
    };

    window.resumeSession = async (id) => {
        try {
            await API.sessions.resume(id);
            loadSessions();
        } catch (error) {
            alert('Failed to resume session: ' + error.message);
        }
    };

    window.completeSession = async (id) => {
        try {
            await API.sessions.complete(id);
            loadSessions();
        } catch (error) {
            alert('Failed to complete session: ' + error.message);
        }
    };

    window.deleteSession = async (id) => {
        if (confirm('Are you sure you want to delete this session?')) {
            try {
                await API.sessions.delete(id);
                loadSessions();
            } catch (error) {
                alert('Failed to delete session: ' + error.message);
            }
        }
    };
});