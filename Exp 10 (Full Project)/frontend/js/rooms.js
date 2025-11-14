document.addEventListener('DOMContentLoaded', () => {
    checkAuth();

    const userId = Auth.getUserId();

    // Logout button
    document.getElementById('logoutBtn').addEventListener('click', () => {
        Auth.logout();
    });

    // Tab switching
    const tabBtns = document.querySelectorAll('.tab-btn');
    tabBtns.forEach(btn => {
        btn.addEventListener('click', () => {
            tabBtns.forEach(b => b.classList.remove('active'));
            btn.classList.add('active');

            document.querySelectorAll('.tab-content').forEach(content => {
                content.classList.remove('active');
            });

            const tab = btn.dataset.tab;
            document.getElementById(tab).classList.add('active');

            if (tab === 'myRooms') {
                loadMyRooms();
            } else {
                loadPublicRooms();
            }
        });
    });

    // Load initial rooms
    loadPublicRooms();

    // Create room button
    document.getElementById('createRoomBtn').addEventListener('click', () => {
        document.getElementById('roomModal').style.display = 'block';
    });

    // Modal close
    document.querySelector('.close').addEventListener('click', () => {
        document.getElementById('roomModal').style.display = 'none';
    });

    document.getElementById('cancelRoomBtn').addEventListener('click', () => {
        document.getElementById('roomModal').style.display = 'none';
    });

    // Room form
    document.getElementById('roomForm').addEventListener('submit', async (e) => {
        e.preventDefault();

        const roomData = {
            name: document.getElementById('roomName').value,
            description: document.getElementById('roomDescription').value,
            roomType: document.getElementById('roomType').value,
            maxParticipants: parseInt(document.getElementById('maxParticipants').value),
            createdBy: userId
        };

        try {
            await API.rooms.create(roomData);
            document.getElementById('roomModal').style.display = 'none';
            document.getElementById('roomForm').reset();
            loadPublicRooms();
        } catch (error) {
            alert('Failed to create room: ' + error.message);
        }
    });

    async function loadPublicRooms() {
        try {
            const rooms = await API.rooms.getAll();
            const publicRooms = rooms.filter(r => r.roomType === 'PUBLIC');
            displayRooms(publicRooms, 'publicRoomsGrid');
        } catch (error) {
            console.error('Error loading public rooms:', error);
            document.getElementById('publicRoomsGrid').innerHTML =
                '<p class="empty-state">Failed to load rooms</p>';
        }
    }

    async function loadMyRooms() {
        try {
            const rooms = await API.rooms.getAll();
            const myRooms = rooms.filter(r => r.createdBy === userId);
            displayRooms(myRooms, 'myRoomsGrid');
        } catch (error) {
            console.error('Error loading my rooms:', error);
            document.getElementById('myRoomsGrid').innerHTML =
                '<p class="empty-state">Failed to load your rooms</p>';
        }
    }

    function displayRooms(rooms, containerId) {
        const container = document.getElementById(containerId);

        if (rooms.length === 0) {
            container.innerHTML = '<p class="empty-state">No rooms found</p>';
            return;
        }

        container.innerHTML = rooms.map(room => `
            <div class="room-card">
                <h3>${room.name}</h3>
                <p>${room.description || 'No description'}</p>
                <div class="room-meta">
                    <span>👥 ${room.currentParticipants || 0}/${room.maxParticipants}</span>
                    <span>🔒 ${room.roomType}</span>
                    <span class="status-badge ${room.status?.toLowerCase() || 'active'}">
                        ${room.status || 'ACTIVE'}
                    </span>
                </div>
                <div class="room-actions">
                    ${room.participants?.includes(userId) ?
            `<button class="btn btn-danger" onclick="leaveRoom('${room.id}')">Leave</button>` :
            `<button class="btn btn-primary" onclick="joinRoom('${room.id}')">Join</button>`
        }
                    ${room.createdBy === userId ?
            `<button class="btn btn-danger" onclick="deleteRoom('${room.id}')">Delete</button>` : ''
        }
                </div>
            </div>
        `).join('');
    }

    // Global functions
    window.joinRoom = async (id) => {
        try {
            await API.rooms.join(id);
            alert('Joined room successfully!');
            loadPublicRooms();
        } catch (error) {
            alert('Failed to join room: ' + error.message);
        }
    };

    window.leaveRoom = async (id) => {
        try {
            await API.rooms.leave(id);
            alert('Left room successfully!');
            loadPublicRooms();
        } catch (error) {
            alert('Failed to leave room: ' + error.message);
        }
    };

    window.deleteRoom = async (id) => {
        if (confirm('Are you sure you want to delete this room?')) {
            try {
                await API.rooms.delete(id);
                loadMyRooms();
            } catch (error) {
                alert('Failed to delete room: ' + error.message);
            }
        }
    };
});