const themeToggle = document.getElementById('themeToggle');
const menuBtn = document.getElementById('menuBtn');
const sidebar = document.getElementById('sidebar');
const overlay = document.getElementById('overlay');
const pageTitle = document.getElementById('pageTitle');
const navLinks = document.querySelectorAll('.nav-link');
const dashboardContent = document.getElementById('dashboardContent');

// Theme handling
let isDark = localStorage.getItem('theme') === 'dark';

function toggleTheme() {
    isDark = !isDark;
    document.body.setAttribute('data-theme', isDark ? 'dark' : 'light');
    themeToggle.textContent = isDark ? '☀️' : '🌙';
    localStorage.setItem('theme', isDark ? 'dark' : 'light');
}

// Mobile menu handling
function toggleSidebar() {
    sidebar.classList.toggle('open');
    overlay.classList.toggle('active');
}

function closeSidebar() {
    sidebar.classList.remove('open');
    overlay.classList.remove('active');
}

// Page navigation
function changePage(pageName) {
    pageTitle.textContent = pageName;
    navLinks.forEach(link => link.classList.remove('active'));
    event.target.classList.add('active');
    
    dashboardContent.style.display = pageName === 'Dashboard' ? 'block' : 'none';
    
    if (window.innerWidth <= 768) {
        closeSidebar();
    }
}

// Initialize theme
function initTheme() {
    const savedTheme = localStorage.getItem('theme');
    isDark = savedTheme ? savedTheme === 'dark' : window.matchMedia('(prefers-color-scheme: dark)').matches;
    document.body.setAttribute('data-theme', isDark ? 'dark' : 'light');
    themeToggle.textContent = isDark ? '☀️' : '🌙';
}

// Event listeners
themeToggle.addEventListener('click', toggleTheme);
menuBtn.addEventListener('click', toggleSidebar);
overlay.addEventListener('click', closeSidebar);
navLinks.forEach(link => {
    link.addEventListener('click', (e) => {
        e.preventDefault();
        changePage(link.getAttribute('data-page'));
    });
});

// Initialize on load
document.addEventListener('DOMContentLoaded', initTheme);
