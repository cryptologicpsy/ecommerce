const basket = () => {
    let iconCart = document.querySelector('.icon-cart');
    let body = document.querySelector('body');
    let closeCart = document.querySelector('.close');
    let checkOutButton = document.querySelector('.checkOut');
    let basket = [];
    let products = []; // Η μεταβλητή που θα αποθηκεύσει τα προϊόντα από το endpoint

    iconCart.addEventListener('click', () => {
        body.classList.toggle('activeTabCart');
    });
    closeCart.addEventListener('click', () => {
        body.classList.toggle('activeTabCart');
    });

    const setProductInCart = (idProduct, quantity, size = null) => {
        let existingProduct = basket.find(item => item.product_id === idProduct && item.size === size);

        if (quantity <= 0) {
            // Remove product from basket if quantity is zero or less
            if (existingProduct) {
                basket = basket.filter(item => !(item.product_id === idProduct && item.size === size));
            }
        } else {
            if (!existingProduct) {
                // Add new product to basket
                basket.push({
                    product_id: idProduct,
                    quantity: quantity,
                    size: size
                });
            } else {
                // Update quantity of existing product in basket
                existingProduct.quantity = quantity;
            }
        }

        localStorage.setItem('basket', JSON.stringify(basket));
        addCartToHTML();
    };

    const addCartToHTML = () => {
        let listHTML = document.querySelector('.listCart');
        let totalHTML = document.querySelector('.icon-cart span');
        let totalQuantity = 0;
        listHTML.innerHTML = '';
        
        // Consolidate items in basket by idProduct and size
        let consolidatedBasket = [];
        basket.forEach(item => {
            let existingIndex = consolidatedBasket.findIndex(prod => prod.product_id === item.product_id && prod.size === item.size);
            if (existingIndex !== -1) {
                consolidatedBasket[existingIndex].quantity += item.quantity;
            } else {
                consolidatedBasket.push({ ...item });
            }
        });

        consolidatedBasket.forEach(item => {
            totalQuantity += item.quantity;
            let info = products.find(product => product.id == item.product_id);
            if (info) {
                let newItem = document.createElement('div');
                newItem.classList.add('item');
        
                // Χρήση της πρώτης φωτογραφίας από το photoUrls, αν υπάρχει
                let productImage = info.photoUrls && info.photoUrls.length > 0
                    ? info.photoUrls[0] // Πρώτη φωτογραφία
                    : info.photoUrl; // Εναλλακτική αν δεν υπάρχει λίστα photoUrls
        
                newItem.innerHTML = `
                    <div class="image">
                        <img src="${productImage}" alt="${info.name}" />
                    </div>
                    <div class="name">
                        ${info.name}
                    </div>
                    ${item.size ? `<div class="size">Size: ${item.size}</div>` : ''}
                    <div class="totalPrice">€${info.price * item.quantity}</div>
                    <div class="quantity">
                        <span class="minus" data-id="${info.id}" data-size="${item.size}">-</span>
                        <span>${item.quantity}</span>
                        <span class="plus" data-id="${info.id}" data-size="${item.size}">+</span>
                    </div>
                `;
                listHTML.appendChild(newItem);
            } else {
                listHTML.innerHTML += `<div class="error-message">Product information not available</div>`;
            }
        });
        
        totalHTML.innerText = totalQuantity;
    };

    // Fetch product data from endpoint
    fetch('/products')
        .then(response => response.json())
        .then(data => {
            products = data; // Store products data in variable products
            addCartToHTML(); // Call addCartToHTML function to update shopping cart
        })
        .catch(error => {
            console.error('Error fetching products:', error);
        });

    // Event listener for clicks
    document.addEventListener('click', (event) => {
        let buttonClick = event.target;
        if (buttonClick.classList.contains('addCart')) {
            let idProduct = buttonClick.dataset.id;
            let size = document.getElementById('sizeSelect') ? document.getElementById('sizeSelect').value : null;
            let positionProductInCart = basket.findIndex(item => item.product_id == idProduct && item.size == size);
            let quantity = (positionProductInCart < 0) ? 1 : basket[positionProductInCart].quantity + 1;
            setProductInCart(idProduct, quantity, size);
        } else if (buttonClick.classList.contains('minus') || buttonClick.classList.contains('plus')) {
            let idProduct = buttonClick.dataset.id;
            let size = buttonClick.dataset.size;
            let positionProductInCart = basket.findIndex(item => item.product_id == idProduct && item.size == size);
            let quantity = basket[positionProductInCart].quantity;
            if (buttonClick.classList.contains('minus')) {
                quantity = Math.max(0, quantity - 1);
            } else if (buttonClick.classList.contains('plus')) {
                quantity++;
            }
            setProductInCart(idProduct, quantity, size);
        }
    });

    // Event listener for "Check Out" button
    checkOutButton.addEventListener('click', () => {
        localStorage.setItem('basket', JSON.stringify(basket));
        window.location.href = 'checkout';
    });

    const initApp = () => {
        if (localStorage.getItem('basket')) {
            basket = JSON.parse(localStorage.getItem('basket'));
        }
        addCartToHTML();
    };

    initApp();
};

export default basket;
