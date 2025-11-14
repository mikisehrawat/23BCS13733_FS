
// API Configuration
const API_CONFIG = {
    BASE_URL: 'http://localhost:8080/api',
    TIMEOUT: 10000,
    ENDPOINTS: {
        AUTH: {
            LOGIN: '/auth/login',
            SIGNUP: '/auth/signup'
        },
        USERS: {
            BASE: '/users',
            ME: '/users/me',
            STATS: '/users/{id}/stats'
        },
        SESSIONS: {
            BASE: '/sessions',
            USER: '/sessions/user/{userId}',
            ROOM: '/sessions/room/{roomId}',
            START: '/sessions/{id}/start',
            PAUSE: '/sessions/{id}/pause',
            RESUME: '/sessions/{id}/resume',
            COMPLETE: '/sessions/{id}/complete',
            CANCEL: '/sessions/{id}/cancel'
        },
        ROOMS: {
            BASE: '/rooms',
            JOIN: '/rooms/{id}/join',
            LEAVE: '/rooms/{id}/leave',
            PARTICIPANTS: '/rooms/{id}/participants'
        },
        TAGS: {
            BASE: '/tags',
            USER: '/tags/user/{userId}'
        },
        LEADERBOARD: {
            GLOBAL: '/leaderboard/global',
            ROOM: '/leaderboard/room/{roomId}'
        }
    }
};

// Storage keys
const STORAGE_KEYS = {
    TOKEN: 'timetrail_token',
    USER: 'timetrail_user',
    USER_ID: 'timetrail_user_id'
};

// Helper functions
const Storage = {
    set: (key, value) => {
        localStorage.setItem(key, JSON.stringify(value));
    },
    get: (key) => {
        const item = localStorage.getItem(key);
        return item ? JSON.parse(item) : null;
    },
    remove: (key) => {
        localStorage.removeItem(key);
    },
    clear: () => {
        localStorage.clear();
    }
};

// Auth helpers
const Auth = {
    isAuthenticated: () => {
        return !!Storage.get(STORAGE_KEYS.TOKEN);
    },
    getToken: () => {
        return Storage.get(STORAGE_KEYS.TOKEN);
    },
    setToken: (token) => {
        Storage.set(STORAGE_KEYS.TOKEN, token);
    },
    getUserId: () => {
        return Storage.get(STORAGE_KEYS.USER_ID);
    },
    setUserId: (userId) => {
        Storage.set(STORAGE_KEYS.USER_ID, userId);
    },
    getUser: () => {
        return Storage.get(STORAGE_KEYS.USER);
    },
    setUser: (user) => {
        Storage.set(STORAGE_KEYS.USER, user);
    },
    logout: () => {
        Storage.clear();
        window.location.href = 'index.html';
    }
};

// Check authentication on protected pages
const checkAuth = () => {
    if (!Auth.isAuthenticated()) {
        window.location.href = 'index.html';
    }
};

// Format duration (minutes to readable format)
const formatDuration = (minutes) => {
    if (minutes < 60) {
        return `${minutes}m`;
    }
    const hours = Math.floor(minutes / 60);
    const mins = minutes % 60;
    return mins > 0 ? `${hours}h ${mins}m` : `${hours}h`;
};

// Format date
const formatDate = (dateString) => {
    const date = new Date(dateString);
    return date.toLocaleDateString('en-US', {
        year: 'numeric',
        month: 'short',
        day: 'numeric',
        hour: '2-digit',
        minute: '2-digit'
    });
};

// Show message
const showMessage = (elementId, message, type = 'info') => {
    const element = document.getElementById(elementId);
    if (element) {
        element.textContent = message;
        element.className = `message ${type}`;
        element.style.display = 'block';
        setTimeout(() => {
            element.style.display = 'none';
        }, 5000);
    }
};