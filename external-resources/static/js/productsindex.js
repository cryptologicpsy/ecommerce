import basket from "./basket.js";

let app = document.getElementById('app');
let temporaryContent = document.getElementById('temporaryContent');

// Φόρτωση του προτύπου (template)
const loadTemplate = () => {
    fetch('/template.html')
        .then(response => response.text())
        .then(html => {
            document.getElementById('app').innerHTML = html;
            let contentTab = document.getElementById('contentTab');
            if (contentTab && temporaryContent) {
                contentTab.innerHTML = temporaryContent.innerHTML;
                temporaryContent.innerHTML = null;
            }
            initApp();  // Αν υπάρχει αυτή η συνάρτηση, εκτέλεσέ την
            basket();   // Αν υπάρχει αυτή η συνάρτηση, εκτέλεσέ την
            initNavbarToggler();  // Προσθέτουμε τη συνάρτηση αρχικοποίησης του toggler button
        })
        .catch(error => {
            console.error('Error loading template:', error);
        });
};

// Συνάρτηση αρχικοποίησης του toggler button
function initNavbarToggler() {
    let toggler = document.querySelector('.navbar-toggler');
    let navbarNav = document.querySelector('#navbarNav');

    if (toggler && navbarNav) {
        toggler.removeEventListener('click', toggleNavbar);
        toggler.addEventListener('click', toggleNavbar);
    }
}

// Συνάρτηση που ελέγχει το toggle του navbar
function toggleNavbar() {
    let navbarNav = document.querySelector('#navbarNav');
    if (navbarNav) {
        navbarNav.classList.toggle('show');
    }
}

loadTemplate();

const initApp = () => {
    // load list product
    let listProductHTML = document.querySelector('.listProduct');
    listProductHTML.innerHTML = null;

    fetch('/products')
    .then(response => response.json())
    .then(products => {
        products.forEach(product => {
            let newProduct = document.createElement('div');
            newProduct.classList.add('item');
            
            // Use the first photo URL as the thumbnail
            let thumbnailUrl = product.photoUrls && product.photoUrls.length > 0 
                ? product.photoUrls[0] 
                : product.photoUrl; // Fallback to main photo if no photo list

            // Check if the product is out of stock
            let outOfStockTag = product.outOfStock ? `<div class="outOfStockTag">Out of Stock</div>` : '';

            // Calculate the old price if a discount exists
            let discount = product.discount || 0; // Default discount to 0 if undefined
            let price = parseFloat(product.price) || 0; // Parse price as float
            let oldPrice = discount > 0 ? Math.round(price / (1 - discount / 100)) : null; // Round only old price

            newProduct.innerHTML = 
            `<a href="/detail.html?id=${product.id}">
                <img src="${thumbnailUrl}" alt="${product.name}">
                ${outOfStockTag}
            </a>
            <h2>${product.name}</h2>
            <div class="price">
                ${oldPrice ? `<span class="old-price">€${oldPrice}</span>` : ''}
                €${price.toFixed(2)} + ΦΠΑ
            </div>
            <button 
                class="viewDetailsButton" 
                data-id='${product.id}'>
                    View Details
            </button>`;
            listProductHTML.appendChild(newProduct);
        });
            // Προσθήκη event listener στα κουμπιά "View Details"
            document.querySelectorAll('.viewDetailsButton').forEach(button => {
                button.addEventListener('click', function() {
                    const productId = this.getAttribute('data-id');
                    window.location.href = `/detail.html?id=${productId}`;
                });
            });
        })
        .catch(error => {
            console.error('Error fetching products:', error);
        });
};
