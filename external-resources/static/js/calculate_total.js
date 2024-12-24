document.addEventListener('DOMContentLoaded', async () => {
    console.log('DOMContentLoaded event fired');
    let basket = JSON.parse(localStorage.getItem('basket')) || [];
    console.log('Initial basket:', basket);

    let orderSummary = document.querySelector('#order-summary');
    let customerDetailsContainer = document.querySelector('#customer-details');
    let productDetailsContainer = document.querySelector('#product-details');
    let shippingCostContainer = document.querySelector('#shipping-cost');
    let products = [];
    let subtotal = 0;
    let totalWeight = 0;

    if (!orderSummary || !shippingCostContainer || !customerDetailsContainer || !productDetailsContainer) {
        console.error('Required DOM elements not found');
        return;
    }

    const customerDetails = JSON.parse(localStorage.getItem('customerDetails')) || null;
    if (!customerDetails) {
        console.error('Customer details not found in localStorage');
        customerDetailsContainer.innerHTML = '<p>Τα στοιχεία του πελάτη δεν βρέθηκαν.</p>';
    } else {
        customerDetailsContainer.innerHTML = `
            <p><strong>Όνομα:</strong> ${customerDetails.firstName || 'Άγνωστο'} ${customerDetails.lastName || 'Άγνωστο'}</p>
            <p><strong>Εταιρία:</strong> ${customerDetails.company || 'Άγνωστο'}</p>
            <p><strong>Χώρα:</strong> ${customerDetails.country || 'Άγνωστο'}</p>
            <p><strong>Διεύθυνση:</strong> ${customerDetails.address1 || 'Άγνωστο'}</p>
            <p><strong>Διαμέρισμα:</strong> ${customerDetails.address2 || 'Άγνωστο'}</p>
            <p><strong>Πόλη:</strong> ${customerDetails.city || 'Άγνωστο'}</p>
            <p><strong>Περιφέρεια:</strong> ${customerDetails.state || 'Άγνωστο'}</p>
            <p><strong>Ταχυδρομικός κώδικας:</strong> ${customerDetails.zip || 'Άγνωστο'}</p>
            <p><strong>Τηλέφωνο:</strong> ${customerDetails.phone || 'Άγνωστο'}</p>
            <p><strong>Email:</strong> ${customerDetails.email || 'Άγνωστο'}</p>
            <p><strong>Σημειώσεις παραγγελίας:</strong> ${customerDetails.orderNotes || 'Άγνωστο'}</p>
        `;
    }

    try {
        console.log('Fetching products...');
        let response = await fetch('/products');
        products = await response.json();
        console.log('Fetched products:', products);

        basket.forEach(item => {
            let position = products.findIndex(value => value.id == item.product_id);
            let info = products[position];
            if (info) {
                let orderItem = document.createElement('div');
                orderItem.innerHTML = `${info.name} - ${info.price}€ × ${item.quantity} = ${info.price * item.quantity}€`;
                orderSummary.appendChild(orderItem);

                let productItem = document.createElement('div');
                productItem.innerHTML = `<p><strong>${info.name}</strong> - Μέγεθος: ${item.size} - Ποσότητα: ${item.quantity}</p>`;
                productDetailsContainer.appendChild(productItem);

                subtotal += info.price * item.quantity;
                totalWeight += info.weight * item.quantity;
            } else {
                console.warn('No product info found for item:', item);
            }
        });

        let summary = document.createElement('div');
        summary.innerHTML = `<p>Μερικό σύνολο: ${subtotal.toFixed(2)}€</p>`;
        orderSummary.appendChild(summary);

        await calculateShipping(subtotal, totalWeight);
    } catch (error) {
        console.error('Error fetching products:', error);
    }

    async function calculateShipping(subtotal, totalWeight) {
        if (customerDetails && customerDetails.zip) {
            const shippingData = {
                postalCode: customerDetails.zip,
                weight: totalWeight,
            };

            try {
                let response = await fetch('/api/shipping/calculate', {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify(shippingData),
                });

                let shippingCost = await response.json();

                let isGiftCardOnly = basket.every(item => {
                    let product = products.find(p => p.id == item.product_id);
                    return product?.giftCard === true;
                });

                if (isGiftCardOnly) {
                    shippingCost = 0;
                }

                let finalTotal = subtotal + shippingCost;
                if (shippingCostContainer) {
                    shippingCostContainer.innerHTML = `
                        <p>Κόστος Αποστολής: ${shippingCost.toFixed(2)}€</p>
                        <p>Συνολικό Κόστος: ${finalTotal.toFixed(2)}€</p>
                    `;
                    await handlePayment(finalTotal);
                }
            } catch (error) {
                console.error('Shipping calculation error:', error);
            }
        }
    }

    async function handlePayment(finalTotal) {
        try {
            const orderId = generateOrderId(); // Δημιουργία μοναδικού Order ID
        
            // Προετοιμασία των productDetails από το καλάθι
            let allProducts = basket.map(item => {
                let product = products.find(p => p.id == item.product_id);
                return {
                    product_id: item.product_id,
                    quantity: item.quantity,
                    size: item.size,
                    giftCard: product?.giftCard || false,
                };
            });
        
            // Δημιουργία του PaymentIntent μέσω POST
            const clientSecretResponse = await fetch('/api/payment-intent', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({
                    productDetails: JSON.stringify(allProducts), // Στέλνουμε το καλάθι
                    orderId: orderId, // Στέλνουμε και το orderId
                    customerEmail: customerDetails.email,
                    postalCode: customerDetails.postalCode, // Αν χρειάζεται και ο ταχυδρομικός κώδικας
                }),
            });
        
            if (!clientSecretResponse.ok) {
                throw new Error('Failed to create PaymentIntent');
            }
        
            const { clientSecret, totalAmount } = await clientSecretResponse.json(); // Παίρνουμε το clientSecret και το totalAmount
            const stripe = Stripe('pk_test_');
            const elements = stripe.elements();
            const cardElement = elements.create('card', { style: { base: { fontSize: '16px', color: '#32325d' } } });
            cardElement.mount('#payment-element');
        
            const paymentForm = document.getElementById('payment-form');
            paymentForm.addEventListener('submit', async function (event) {
                event.preventDefault();
        
                const { error, paymentIntent } = await stripe.confirmCardPayment(clientSecret, {
                    payment_method: {
                        card: cardElement,
                        billing_details: { name: `${customerDetails.firstName} ${customerDetails.lastName}` },
                    },
                });
        
                if (error) {
                    console.error('Error during payment confirmation:', error);
                    return;
                }
        
                console.log('Payment confirmed:', paymentIntent);
        
                // Στέλνουμε την επιβεβαίωση της πληρωμής στον server
                const paymentConfirmationResponse = await fetch('/api/confirm-payment', {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify({
                        paymentIntentId: paymentIntent.id, // Το paymentIntentId που επιστρέφει το Stripe
                        orderId: orderId, // Order ID
                        customerEmail: customerDetails.email,
                        productDetails: JSON.stringify(allProducts), // Στέλνουμε τα προϊόντα
                    }),
                });
        
                if (!paymentConfirmationResponse.ok) {
                    throw new Error('Failed to confirm payment');
                }
    
                console.log('Payment confirmation sent to backend');
                await sendSuccessEmail(customerDetails, basket);
                // Redirect σε success page
window.location.href = '/successorder';

            });

            
        
            function generateOrderId() {
                return 'ORDER-' + Date.now();
            }

            
        
        } catch (error) {
            console.error('Error in payment flow:', error);
        }
        
        async function sendSuccessEmail(customerDetails, basket) {

        try {
            await fetch('/api/send-success-email', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({ customerDetails, basket }),
            });
        } catch (error) {
            console.error('Error sending success email:', error);
        }
    }

        
    }
});
