document.addEventListener('DOMContentLoaded', () => {
    checkAuth();

    const userId = Auth.getUserId();
    const user = Auth.getUser();

    // Logout button
    document.getElementById('logoutBtn').addEventListener('click', () => {
        Auth.logout();
    });

    // Load profile data
    loadProfileData();

    // Update profile form
    document.getElementById('updateProfileForm').addEventListener('submit', async (e) => {
        e.preventDefault();

        const userData = {
            username: document.getElementById('updateUsername').value,
            email: document.getElementById('updateEmail').value
        };

        try {
            const updatedUser = await API.users.update(userId, userData);
            Auth.setUser(updatedUser);
            alert('Profile updated successfully!');
        } catch (error) {
            alert('Failed to update profile: ' + error.message);
        }
    });

    // Change password form
    document.getElementById('changePasswordForm').addEventListener('submit', async (e) => {
        e.preventDefault();

        const currentPassword = document.getElementById('currentPassword').value;
        const newPassword = document.getElementById('newPassword').value;
        const confirmNewPassword = document.getElementById('confirmNewPassword').value;

        if (newPassword !== confirmNewPassword) {
            showMessage('passwordMessage', 'Passwords do not match', 'error');
            return;
        }

        if (newPassword.length < 6) {
            showMessage('passwordMessage', 'Password must be at least 6 characters', 'error');
            return;
        }

        try {
            // Note: You'll need to implement this endpoint in your backend
            await API.users.changePassword(userId, {
                currentPassword,
                newPassword
            });

            showMessage('passwordMessage', 'Password changed successfully!', 'success');
            document.getElementById('changePasswordForm').reset();
        } catch (error) {
            showMessage('passwordMessage', 'Failed to change password: ' + error.message, 'error');
        }
    });

    async function loadProfileData() {
        try {
            // Load user data
            const userData = await API.users.getById(userId);

            // Set profile info
            document.getElementById('profileUsername').textContent = userData.username;
            document.getElementById('profileEmail').textContent = userData.email;
            document.getElementById('avatarInitial').textContent = userData.username.charAt(0).toUpperCase();

            // Set form values
            document.getElementById('updateUsername').value = userData.username;
            document.getElementById('updateEmail').value = userData.email;

            // Load stats
            const stats = await API.users.getStats(userId);
            document.getElementById('profileTotalSessions').textContent = stats.totalSessions || 0;
            document.getElementById('profileTotalTime').textContent = formatDuration(stats.totalFocusTime || 0);
            document.getElementById('profileStreak').textContent = stats.currentStreak || 0;
            document.getElementById('profilePoints').textContent = stats.totalPoints || 0;
        } catch (error) {
            console.error('Error loading profile data:', error);
        }
    }
});