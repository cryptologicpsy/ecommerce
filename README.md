# E-Commerce Project with Java Spring Boot and Stripe Integration

## **Overview**
This is a custom e-commerce project built using **Java Spring Boot**, **SQL**, and **JavaScript** for dynamic data management on the frontend. The application provides a complete solution for managing products, orders, and payments while implementing Stripe's **PaymentIntent API** to ensure secure and efficient transactions.

---

## **Features**

### **Product Management**
- Full **CRUD functionality** (Create, Read, Update, Delete) for products.
- Supports both **physical** and **virtual products**.
- Ability to manually set products as **in stock** or **out of stock**.
- Option to add **discounts** to product prices.
- Upload and manage **product images**.
- Add **pricing**, **weight**, and **dimensions** for each product.
- Implements full logic for calculating **shipping costs** based on product attributes.

### **Admin Panel**
- A custom admin panel for:
  - Managing the product catalog.
  - Overseeing the database.
  - Monitoring orders and inventory.

### **Payment Integration**
- Payment processing via **Stripe**, implementing the **PaymentIntent API** for secure and reliable transactions.
- Ensures safe handling of payment data and compliance with security standards.

### **Other Features**
- Fully functional backend with **Spring Boot**.
- Frontend dynamically updates data using **JavaScript**.
- Clean and responsive design for easy usability.

---

## **Technologies Used**
- **Backend:** Spring Boot, SQL
- **Frontend:** JavaScript
- **Payment Processing:** Stripe PaymentIntent API

---

## **Setup Instructions**

To run this project locally, follow these steps:

1. **Clone the Repository:**
   ```bash
   git clone https://github.com/cryptologicpsy/ecommerce.git
   ```

2. **Navigate to the Project Directory:**
   ```bash
   cd ecommerce
   ```

3. **Set Up the `application.properties` File:**
   - Configure your database connection settings (e.g., URL, username, password).
   - Add your Stripe API keys for payment processing.

4. **Run the Application:**
   ```bash
   mvn spring-boot:run
   ```

5. **Access the Application:**
   - Open your browser and navigate to `http://localhost:8080` to view the e-commerce site.
   - Use the admin panel for product and shop management.

---

## **How to Use**
1. Log in to the admin panel to manage your product catalog.
2. Add new products with images, pricing, and other details.
3. Set up discounts, shipping costs, and stock status.
4. Allow customers to browse the store, add items to the cart, and complete secure payments through Stripe.

---

## **Contributing**
If you'd like to contribute to this project, feel free to open a pull request or submit an issue. Any feedback is welcome!

---
