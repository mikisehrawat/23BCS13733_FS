
// API Service
const API = {
    // Generic request method
    request: async (endpoint, options = {}) => {
        const url = `${API_CONFIG.BASE_URL}${endpoint}`;
        const token = Auth.getToken();

        const headers = {
            'Content-Type': 'application/json',
            ...options.headers
        };

        if (token) {
            headers['Authorization'] = `Bearer ${token}`;
        }

        try {
            const response = await fetch(url, {
                ...options,
                headers
            });

            if (response.status === 401) {
                Auth.logout();
                return;
            }

            const data = await response.json();

            if (!response.ok) {
                throw new Error(data.message || 'Request failed');
            }

            return data;
        } catch (error) {
            console.error('API Error:', error);
            throw error;
        }
    },

    // Auth endpoints
    auth: {
        login: (credentials) => {
            return API.request(API_CONFIG.ENDPOINTS.AUTH.LOGIN, {
                method: 'POST',
                body: JSON.stringify(credentials)
            });
        },
        signup: (userData) => {
            return API.request(API_CONFIG.ENDPOINTS.AUTH.SIGNUP, {
                method: 'POST',
                body: JSON.stringify(userData)
            });
        }
    },

    // User endpoints
    users: {
        getMe: () => {
            return API.request(API_CONFIG.ENDPOINTS.USERS.ME);
        },
        getById: (id) => {
            return API.request(`${API_CONFIG.ENDPOINTS.USERS.BASE}/${id}`);
        },
        update: (id, userData) => {
            return API.request(`${API_CONFIG.ENDPOINTS.USERS.BASE}/${id}`, {
                method: 'PUT',
                body: JSON.stringify(userData)
            });
        },
        getStats: (id) => {
            const endpoint = API_CONFIG.ENDPOINTS.USERS.STATS.replace('{id}', id);
            return API.request(endpoint);
        }
    },

    // Session endpoints
    sessions: {
        create: (sessionData) => {
            return API.request(API_CONFIG.ENDPOINTS.SESSIONS.BASE, {
                method: 'POST',
                body: JSON.stringify(sessionData)
            });
        },
        getById: (id) => {
            return API.request(`${API_CONFIG.ENDPOINTS.SESSIONS.BASE}/${id}`);
        },
        getUserSessions: (userId) => {
            const endpoint = API_CONFIG.ENDPOINTS.SESSIONS.USER.replace('{userId}', userId);
            return API.request(endpoint);
        },
        getRoomSessions: (roomId) => {
            const endpoint = API_CONFIG.ENDPOINTS.SESSIONS.ROOM.replace('{roomId}', roomId);
            return API.request(endpoint);
        },
        start: (id) => {
            const endpoint = API_CONFIG.ENDPOINTS.SESSIONS.START.replace('{id}', id);
            return API.request(endpoint, { method: 'POST' });
        },
        pause: (id) => {
            const endpoint = API_CONFIG.ENDPOINTS.SESSIONS.PAUSE.replace('{id}', id);
            return API.request(endpoint, { method: 'POST' });
        },
        resume: (id) => {
            const endpoint = API_CONFIG.ENDPOINTS.SESSIONS.RESUME.replace('{id}', id);
            return API.request(endpoint, { method: 'POST' });
        },
        complete: (id) => {
            const endpoint = API_CONFIG.ENDPOINTS.SESSIONS.COMPLETE.replace('{id}', id);
            return API.request(endpoint, { method: 'POST' });
        },
        cancel: (id) => {
            const endpoint = API_CONFIG.ENDPOINTS.SESSIONS.CANCEL.replace('{id}', id);
            return API.request(endpoint, { method: 'POST' });
        },
        delete: (id) => {
            return API.request(`${API_CONFIG.ENDPOINTS.SESSIONS.BASE}/${id}`, {
                method: 'DELETE'
            });
        }
    },

    // Room endpoints
    rooms: {
        getAll: () => {
            return API.request(API_CONFIG.ENDPOINTS.ROOMS.BASE);
        },
        getById: (id) => {
            return API.request(`${API_CONFIG.ENDPOINTS.ROOMS.BASE}/${id}`);
        },
        create: (roomData) => {
            return API.request(API_CONFIG.ENDPOINTS.ROOMS.BASE, {
                method: 'POST',
                body: JSON.stringify(roomData)
            });
        },
        update: (id, roomData) => {
            return API.request(`${API_CONFIG.ENDPOINTS.ROOMS.BASE}/${id}`, {
                method: 'PUT',
                body: JSON.stringify(roomData)
            });
        },
        delete: (id) => {
            return API.request(`${API_CONFIG.ENDPOINTS.ROOMS.BASE}/${id}`, {
                method: 'DELETE'
            });
        },
        join: (id) => {
            const endpoint = API_CONFIG.ENDPOINTS.ROOMS.JOIN.replace('{id}', id);
            return API.request(endpoint, { method: 'POST' });
        },
        leave: (id) => {
            const endpoint = API_CONFIG.ENDPOINTS.ROOMS.LEAVE.replace('{id}', id);
            return API.request(endpoint, { method: 'POST' });
        },
        getParticipants: (id) => {
            const endpoint = API_CONFIG.ENDPOINTS.ROOMS.PARTICIPANTS.replace('{id}', id);
            return API.request(endpoint);
        }
    },

    // Tag endpoints
    tags: {
        getUserTags: (userId) => {
            const endpoint = API_CONFIG.ENDPOINTS.TAGS.USER.replace('{userId}', userId);
            return API.request(endpoint);
        },
        create: (tagData) => {
            return API.request(API_CONFIG.ENDPOINTS.TAGS.BASE, {
                method: 'POST',
                body: JSON.stringify(tagData)
            });
        }
    },

    // Leaderboard endpoints
    leaderboard: {
        getGlobal: (params) => {
            const queryString = params ? `?${new URLSearchParams(params)}` : '';
            return API.request(`${API_CONFIG.ENDPOINTS.LEADERBOARD.GLOBAL}${queryString}`);
        },
        getRoom: (roomId, params) => {
            const endpoint = API_CONFIG.ENDPOINTS.LEADERBOARD.ROOM.replace('{roomId}', roomId);
            const queryString = params ? `?${new URLSearchParams(params)}` : '';
            return API.request(`${endpoint}${queryString}`);
        }
    }
};