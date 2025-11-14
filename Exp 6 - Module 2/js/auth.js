
document.addEventListener('DOMContentLoaded', () => {
    // Redirect if already logged in
    if (Auth.isAuthenticated()) {
        window.location.href = 'dashboard.html';
        return;
    }

    // Tab switching
    const tabs = document.querySelectorAll('.tab');
    const loginForm = document.getElementById('loginForm');
    const signupForm = document.getElementById('signupForm');

    tabs.forEach(tab => {
        tab.addEventListener('click', () => {
            tabs.forEach(t => t.classList.remove('active'));
            tab.classList.add('active');

            const tabName = tab.dataset.tab;
            if (tabName === 'login') {
                loginForm.classList.add('active');
                signupForm.classList.remove('active');
            } else {
                signupForm.classList.add('active');
                loginForm.classList.remove('active');
            }
        });
    });

    // Login form submission
    loginForm.addEventListener('submit', async (e) => {
        e.preventDefault();

        const email = document.getElementById('loginEmail').value;
        const password = document.getElementById('loginPassword').value;

        try {
            const response = await API.auth.login({ email, password });

            // Store token and user data
            Auth.setToken(response.token);
            Auth.setUserId(response.userId);
            Auth.setUser(response.user);

            showMessage('loginMessage', 'Login successful!', 'success');

            // Redirect to dashboard
            setTimeout(() => {
                window.location.href = 'dashboard.html';
            }, 1000);
        } catch (error) {
            showMessage('loginMessage', error.message || 'Login failed', 'error');
        }
    });

    // Signup form submission
    signupForm.addEventListener('submit', async (e) => {
        e.preventDefault();

        const username = document.getElementById('signupUsername').value;
        const email = document.getElementById('signupEmail').value;
        const password = document.getElementById('signupPassword').value;
        const confirmPassword = document.getElementById('signupConfirmPassword').value;

        if (password !== confirmPassword) {
            showMessage('signupMessage', 'Passwords do not match', 'error');
            return;
        }

        if (password.length < 6) {
            showMessage('signupMessage', 'Password must be at least 6 characters', 'error');
            return;
        }

        try {
            const response = await API.auth.signup({ username, email, password });

            // Store token and user data
            Auth.setToken(response.token);
            Auth.setUserId(response.userId);
            Auth.setUser(response.user);

            showMessage('signupMessage', 'Account created successfully!', 'success');

            // Redirect to dashboard
            setTimeout(() => {
                window.location.href = 'dashboard.html';
            }, 1000);
        } catch (error) {
            showMessage('signupMessage', error.message || 'Signup failed', 'error');
        }
    });
});