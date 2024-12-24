import basket from "./basket.js";

let app = document.getElementById('app');
let temporaryContent = document.getElementById('temporaryContent');

// Φόρτωση του προτύπου (template)
const loadTemplate = () => {
    fetch('/template.html')
        .then(response => response.text())
        .then(html => {
            app.innerHTML = html;
            let contentTab = document.getElementById('contentTab');
            contentTab.innerHTML = temporaryContent.innerHTML;
            temporaryContent.innerHTML = null;
            initApp();
            basket();
        })
        .catch(error => {
            console.error('Error loading template:', error);
        });
};

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
            });
        })
        .catch(error => {
            console.error('Error fetching products:', error);
        });

    // Προσθήκη λειτουργικότητας για την εξαφάνιση του navbar
    const nav = document.querySelector(".navbar");
    if (nav) {
        let lastScrollY = window.scrollY;
        window.addEventListener("scroll", () => {
            if (lastScrollY < window.scrollY) {
                nav.classList.add("navbar--hidden");
            } else {
                nav.classList.remove("navbar--hidden");
            }
            lastScrollY = window.scrollY;
        });
    } else {
        console.error("Navbar element not found.");
    }
};
