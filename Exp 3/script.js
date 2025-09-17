// Grocery product dataset
let products = [
    {
        id: 1,
        name: "Bananas",
        category: "fruits",
        price: 1.99,
        description: "Fresh ripe bananas, sold per kg",
        rating: 4.7,
        icon: "🍌",
        img: "https://encrypted-tbn1.gstatic.com/images?q=tbn:ANd9GcQjRrX2K7EXd413E0gnEVjrxTjaQ_CqzxeSIbW0VGPoDijmHREfdT3zQe1RoYpHYsBaZPIV_CK-Mn_cqDrVJ7w6EQg2oSmuB7kqkk9o4axonQ"
    },
    {
        id: 2,
        name: "Tomatoes",
        category: "vegetables",
        price: 2.49,
        description: "Juicy red tomatoes, sold per kg",
        rating: 4.5,
        icon: "🍅",
    },
    {
        id: 3,
        name: "Milk",
        category: "dairy",
        price: 1.29,
        description: "1 litre of fresh cow milk",
        rating: 4.8,
        icon: "🥛",
    },
    {
        id: 4,
        name: "Bread",
        category: "bakery",
        price: 2.99,
        description: "Whole wheat bread loaf",
        rating: 4.6,
        icon: "🍞",
    },
    {
        id: 5,
        name: "Orange Juice",
        category: "beverages",
        price: 3.49,
        description: "1 litre of pure orange juice",
        rating: 4.4,
        icon: "🧃",
    },
    {
        id: 6,
        name: "Potato Chips",
        category: "snacks",
        price: 1.99,
        description: "Crispy salted potato chips, 150g pack",
        rating: 4.3,
        icon: "🍟",
    },
];

// DOM elements
const categoryFilter = document.getElementById("categoryFilter");
const productsGrid = document.getElementById("productsGrid");
const resultCount = document.getElementById("resultCount");
const noResults = document.getElementById("noResults");

// Current filtered products
let filteredProducts = [...products];

// Initialize the application
function init() {
    renderProducts(products);
    updateResultCount(products.length);

    // Add event listener
    categoryFilter.addEventListener("change", handleFilterChange);
}

// Handle filter change event
function handleFilterChange(event) {
    const selectedCategory = event.target.value;
    filterProducts(selectedCategory);
}

// Filter products based on category
function filterProducts(category) {
    if (category === "all") {
        filteredProducts = [...products];
    } else {
        filteredProducts = products.filter(
            (product) => product.category === category
        );
    }

    renderProducts(filteredProducts);
    updateResultCount(filteredProducts.length);
    toggleNoResultsMessage(filteredProducts.length === 0);
}

// Render products to the DOM
function renderProducts(productsToRender) {
    // Clear previous products
    productsGrid.innerHTML = "";

    if (productsToRender.length === 0) {
        return;
    }

    // Create product cards
    productsToRender.forEach((product) => {
        const productCard = createProductCard(product);
        productsGrid.appendChild(productCard);
    });
}

// Create individual product card
function createProductCard(product) {
    const card = document.createElement("div");
    card.className = "product-card";

    card.innerHTML = `
        <div class="product-image">${product.icon}</div>
        <div class="product-name">${product.name}</div>
        <div class="product-category">${capitalizeFirst(product.category)}</div>
        <div class="product-price">$${product.price}</div>
        <div class="product-description">${product.description}</div>
        <div class="product-rating">Rating: ${product.rating}/5</div>
    `;

    return card;
}

// Update result count display
function updateResultCount(count) {
    if (count === products.length) {
        resultCount.textContent = `Showing all ${count} products`;
    } else {
        resultCount.textContent = `Showing ${count} of ${products.length} products`;
    }
}

// Toggle no results message
function toggleNoResultsMessage(show) {
    noResults.style.display = show ? "block" : "none";
    productsGrid.style.display = show ? "none" : "grid";
}

// Utility function to capitalize first letter
function capitalizeFirst(str) {
    return str.charAt(0).toUpperCase() + str.slice(1);
}

// Initialize application when DOM is loaded
document.addEventListener("DOMContentLoaded", () => {
    init();
});
