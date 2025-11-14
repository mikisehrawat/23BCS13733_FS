document.addEventListener('DOMContentLoaded', () => {
    checkAuth();

    const userId = Auth.getUserId();

    // Logout button
    document.getElementById('logoutBtn').addEventListener('click', () => {
        Auth.logout();
    });

    // Filters
    document.getElementById('timeFilter').addEventListener('change', loadLeaderboard);
    document.getElementById('categoryFilter').addEventListener('change', loadLeaderboard);

    // Load initial leaderboard
    loadLeaderboard();

    async function loadLeaderboard() {
        try {
            const timeFilter = document.getElementById('timeFilter').value;
            const category = document.getElementById('categoryFilter').value;

            const params = {
                period: timeFilter,
                sortBy: category
            };

            const leaderboard = await API.leaderboard.getGlobal(params);

            displayPodium(leaderboard.slice(0, 3));
            displayLeaderboardTable(leaderboard);
        } catch (error) {
            console.error('Error loading leaderboard:', error);
            document.getElementById('leaderboardBody').innerHTML =
                '<tr><td colspan="6" class="empty-state">Failed to load leaderboard</td></tr>';
        }
    }

    function displayPodium(topThree) {
        // First place
        if (topThree[0]) {
            document.getElementById('rank1').innerHTML = `
                <div class="rank-medal">🥇</div>
                <div class="rank-info">
                    <h3>${topThree[0].username || topThree[0].userId}</h3>
                    <p>${topThree[0].totalPoints || 0} pts</p>
                </div>
            `;
        }

        // Second place
        if (topThree[1]) {
            document.getElementById('rank2').innerHTML = `
                <div class="rank-medal">🥈</div>
                <div class="rank-info">
                    <h3>${topThree[1].username || topThree[1].userId}</h3>
                    <p>${topThree[1].totalPoints || 0} pts</p>
                </div>
            `;
        }

        // Third place
        if (topThree[2]) {
            document.getElementById('rank3').innerHTML = `
                <div class="rank-medal">🥉</div>
                <div class="rank-info">
                    <h3>${topThree[2].username || topThree[2].userId}</h3>
                    <p>${topThree[2].totalPoints || 0} pts</p>
                </div>
            `;
        }
    }

    function displayLeaderboardTable(leaderboard) {
        const tbody = document.getElementById('leaderboardBody');

        if (leaderboard.length === 0) {
            tbody.innerHTML = '<tr><td colspan="6" class="empty-state">No data available</td></tr>';
            return;
        }

        tbody.innerHTML = leaderboard.map((entry, index) => `
            <tr ${entry.userId === userId ? 'class="current-user"' : ''}>
                <td>${index + 1}</td>
                <td>${entry.username || entry.userId}</td>
                <td>${entry.totalPoints || 0}</td>
                <td>${entry.totalSessions || 0}</td>
                <td>${formatDuration(entry.totalFocusTime || 0)}</td>
                <td>${entry.currentStreak || 0} 🔥</td>
            </tr>
        `).join('');
    }
});