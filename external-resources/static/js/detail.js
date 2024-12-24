import basket from "./basket.js";

let app = document.getElementById('app');
let temporaryContent = document.getElementById('temporaryContent');
let produ = []; // Η μεταβλητή που θα αποθηκεύσει τα προϊόντα από το endpoint

// Φόρτωση του προτύπου (template)
const loadTemplate = () => {
    fetch('./template.html')
        .then(response => response.text())
        .then(html => {
            app.innerHTML = html;
            let contentTab = document.getElementById('contentTab');
            contentTab.innerHTML = temporaryContent.innerHTML;
            temporaryContent.innerHTML = null;

            basket();

            fetch('/products')
                .then(response => response.json())
                .then(data => {
                    produ = data; // Αποθηκεύουμε τα δεδομένα στη μεταβλητή products
                    initApp();
                    initNavbarToggler();  // Προσθέτουμε τη συνάρτηση αρχικοποίησης του toggler button

                })
                .catch(error => {
                    console.error('Error fetching products:', error);
                });
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
    let idProduct = new URLSearchParams(window.location.search).get('id');
    let info = produ.filter((value) => value.id == idProduct)[0];
    console.log(info);
    if (!info) {
        window.location.href = '/';
    }

    let detail = document.querySelector('.detail');
    // Εμφάνιση όλων των εικόνων του προϊόντος σε Fancybox
    let imageContainer = detail.querySelector('.image');
    imageContainer.innerHTML = ""; // Καθαρίζουμε το container

    if (info.photoUrls && info.photoUrls.length > 0) {
        let firstImage = document.createElement('a');
        firstImage.href = info.photoUrls[0];
        firstImage.dataset.fancybox = "gallery";
        firstImage.innerHTML = `<img src="${info.photoUrls[0]}" alt="${info.name}" style="width: 100%; max-width: 500px;">`;
        imageContainer.appendChild(firstImage);

        info.photoUrls.slice(1).forEach(photoUrl => {
            let anchor = document.createElement('a');
            anchor.href = photoUrl;
            anchor.dataset.fancybox = "gallery";
            anchor.style.display = "none"; // Hide other images initially
            imageContainer.appendChild(anchor);
        });
    } else {
        let img = document.createElement('img');
        img.src = info.photoUrl; // Εάν δεν υπάρχει λίστα, χρησιμοποιούμε το κύριο URL
        img.alt = info.name;
        img.style.width = "100%";
        img.style.maxWidth = "500px";
        imageContainer.appendChild(img);
    }

    detail.querySelector('.name').innerText = info.name;
    detail.querySelector('.price').innerText = '€' + info.price + "  (vat included)";
    detail.querySelector('.description').innerText = info.description;
    let addCartButton = detail.querySelector('.addCart');
    addCartButton.dataset.id = idProduct;

    // Εμφάνιση μηνύματος "This product is out of stock" και απενεργοποίηση του κουμπιού αν είναι out of stock
    if (info.outOfStock) {
        let outOfStockMessage = document.createElement('div');
        outOfStockMessage.classList.add('outOfStockMessage');
        outOfStockMessage.innerText = 'This product is out of stock';
        detail.querySelector('.price').appendChild(outOfStockMessage);
        addCartButton.disabled = true;
    }

    // Προσθήκη dropdown για μεγέθη αν υπάρχουν
    if (info.sizes && info.sizes.length > 0) {
        let sizeSelectHTML = '';
        info.sizes.forEach(size => {
            sizeSelectHTML += `<option value="${size}">${size.charAt(0).toUpperCase() + size.slice(1)}</option>`;
        });
        document.getElementById('sizeSelect').innerHTML = sizeSelectHTML;
    } else {
        // Αποκρύπτουμε το πεδίο με τα μεγέθη αν δεν υπάρχουν
        document.querySelector('.sizes').style.display = 'none';
    }

    let listProductHTML = document.querySelector('.listProduct');
    listProductHTML.innerHTML = null;

    fetch('/products')
    .then(response => response.json())
    .then(products => {
        products.filter((value) => value.id != idProduct).forEach(product => {
            let newProduct = document.createElement('div');
            newProduct.classList.add('item');

            // Χρήση της πρώτης φωτογραφίας από το photoUrls, αν υπάρχει
            let productImage = product.photoUrls && product.photoUrls.length > 0
                ? product.photoUrls[0] // Πρώτη φωτογραφία
                : product.photoUrl; // Εναλλακτική αν δεν υπάρχει λίστα photoUrls

            newProduct.innerHTML =
                `<a href="/detail.html?id=${product.id}">
                    <img src="${productImage}" alt="${product.name}">
                </a>
                <h2>${product.name}</h2>
                <div class="price">€${product.price}</div>`;
            
            // Εμφάνιση ένδειξης "Out of Stock" στη λίστα των προϊόντων αν είναι out of stock
            if (product.outOfStock) {
                let outOfStockTag = document.createElement('div');
                outOfStockTag.classList.add('outOfStockTag');
                outOfStockTag.innerText = 'Out of Stock';
                newProduct.querySelector('a').appendChild(outOfStockTag);
            }

            listProductHTML.appendChild(newProduct);
        });
    });

    // Προσθήκη event listener για το κουμπί "Add To Cart"
    addCartButton.addEventListener('click', () => {
        let selectedSize = document.getElementById('sizeSelect') ? document.getElementById('sizeSelect').value : null;
        let cartItems = JSON.parse(localStorage.getItem('basket')) || [];
        cartItems.push({
            product_id: idProduct,
            size: selectedSize,
            quantity: 1 // Μπορείς να το κάνεις δυναμικό αν χρειάζεται
        });
        localStorage.setItem('basket', JSON.stringify(cartItems));
        alert('Product added to cart' );
    });
};
