# EasyShop E-Commerce Application

[![Java](https://img.shields.io/badge/Java-17+-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.0+-green.svg)](https://spring.io/projects/spring-boot)
[![MySQL](https://img.shields.io/badge/MySQL-8.0+-blue.svg)](https://www.mysql.com/)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

A full-stack e-commerce web application built with Spring Boot backend and vanilla JavaScript frontend. EasyShop provides a seamless shopping experience with user authentication, product browsing, shopping cart management, and secure checkout functionality.

---

## 📋 Table of Contents

- [Features](#-features)
- [Technology Stack](#-technology-stack)
- [Architecture](#-architecture)
- [Getting Started](#-getting-started)
  - [Prerequisites](#prerequisites)
  - [Installation](#installation)
  - [Database Setup](#database-setup)
  - [Configuration](#configuration)
  - [Running the Application](#running-the-application)
- [API Documentation](#-api-documentation)
- [Project Structure](#-project-structure)
- [Screenshots](#-screenshots)
- [Development Phases](#-development-phases)
- [Contributing](#-contributing)
- [License](#-license)
- [Contact](#-contact)

---

## ✨ Features

### User Management
- 🔐 **User Registration & Authentication** - Secure JWT-based authentication
- 👤 **User Profiles** - Create and update personal information
- 🔒 **Role-Based Access Control** - Admin and user roles with different permissions

### Product Management
- 📦 **Product Catalog** - Browse products by category
- 🔍 **Advanced Search & Filtering** - Search by category, price range, and subcategory
- 📝 **Product Details** - View comprehensive product information
- 🛡️ **Admin Controls** - Create, update, and delete products (Admin only)

### Shopping Cart
- 🛒 **Cart Management** - Add, update, and remove items
- ✅ **Selective Checkout** - Choose specific items to purchase
- 💾 **Persistent Cart** - Cart data saved across sessions
- 📊 **Real-time Totals** - Dynamic price calculations

### Checkout & Orders
- 💳 **Secure Checkout** - Convert cart to orders seamlessly
- 📦 **Order Processing** - Create orders with line items
- 🏠 **Address Integration** - Use profile address for shipping
- ✉️ **Order Confirmation** - Receive order ID upon successful purchase

### Category Management
- 📂 **Category Browsing** - Organize products by categories
- 🔧 **Admin Category Management** - Create, update, and delete categories (Admin only)

---

## 🛠 Technology Stack

### Backend
- **Java 17** - Programming language
- **Spring Boot 3.0+** - Application framework
- **Spring Security** - Authentication and authorization
- **Spring Data JDBC** - Database access
- **JWT (JSON Web Tokens)** - Stateless authentication
- **Maven** - Dependency management
- **MySQL** - Relational database

### Frontend
- **HTML5** - Markup
- **CSS3** - Styling with Bootstrap
- **JavaScript (ES6+)** - Client-side logic
- **Axios** - HTTP client
- **Mustache.js** - Template engine

### Tools & Libraries
- **IntelliJ IDEA** - IDE
- **Postman** - API testing
- **Git** - Version control
- **MySQL Workbench** - Database management

---

## 🏗 Architecture

EasyShop follows a **three-tier architecture**:

```
┌─────────────────────────────────────────────┐
│          Presentation Layer (Frontend)       │
│    HTML + CSS + JavaScript + Bootstrap      │
└─────────────────────────────────────────────┘
                     ↕ HTTP/REST
┌─────────────────────────────────────────────┐
│         Application Layer (Backend)          │
│      Controllers → Services → DAOs           │
│         Spring Boot + Spring Security        │
└─────────────────────────────────────────────┘
                     ↕ JDBC
┌─────────────────────────────────────────────┐
│          Data Layer (Database)               │
│              MySQL Database                  │
└─────────────────────────────────────────────┘
```

### Key Design Patterns
- **MVC (Model-View-Controller)** - Separation of concerns
- **DAO (Data Access Object)** - Database abstraction
- **Repository Pattern** - Data persistence
- **RESTful API** - Stateless communication
- **JWT Authentication** - Secure token-based auth

---

## 🚀 Getting Started

### Prerequisites

Before you begin, ensure you have the following installed:

- **Java Development Kit (JDK) 17+**
  ```bash
  java -version
  ```

- **Maven 3.6+**
  ```bash
  mvn -version
  ```

- **MySQL 8.0+**
  ```bash
  mysql --version
  ```

- **Git**
  ```bash
  git --version
  ```

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/tesfahunfola/Easy-Shop---Capstone_3.git
   cd Easy-Shop---Capstone_3
   ```

2. **Navigate to the project directory**
   ```bash
   cd capstone-api-starter
   ```

### Database Setup

1. **Start MySQL Server**
   ```bash
   # Windows
   net start MySQL80
   
   # Mac/Linux
   sudo service mysql start
   ```

2. **Create the database and tables**
   ```bash
   mysql -u root -p < database/create_database_easyshop.sql
   ```

3. **Verify database creation**
   ```sql
   mysql -u root -p
   USE easyshop;
   SHOW TABLES;
   ```

### Configuration

1. **Update database credentials** in `src/main/resources/application.properties`:
   ```properties
   datasource.url=jdbc:mysql://localhost:3306/easyshop
   datasource.username=root
   datasource.password=YOUR_PASSWORD
   
   jwt.secret=YOUR_SECRET_KEY
   jwt.token-timeout-seconds=108000
   ```

2. **Configure JWT Secret** (Important for security):
   - Generate a strong secret key for production
   - Keep the secret secure and never commit it to version control

### Running the Application

1. **Build the project**
   ```bash
   mvn clean install
   ```

2. **Run the Spring Boot application**
   ```bash
   mvn spring-boot:run
   ```
   
   Or run the main class:
   ```bash
   java -jar target/easyshop-0.0.1-SNAPSHOT.jar
   ```

3. **Access the application**
   - Frontend: `http://localhost:8080`
   - Backend API: `http://localhost:8080/api`

4. **Default Credentials**
   ```
   Admin User:
   Username: admin
   Password: password
   
   Regular User:
   Username: user
   Password: password
   ```

---

## 📚 API Documentation

### Authentication Endpoints

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| POST | `/register` | Register new user | No |
| POST | `/login` | User login | No |

### Category Endpoints

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| GET | `/categories` | Get all categories | No |
| GET | `/categories/{id}` | Get category by ID | No |
| GET | `/categories/{id}/products` | Get products in category | No |
| POST | `/categories` | Create category | Admin |
| PUT | `/categories/{id}` | Update category | Admin |
| DELETE | `/categories/{id}` | Delete category | Admin |

### Product Endpoints

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| GET | `/products` | Get all products (with filters) | No |
| GET | `/products/{id}` | Get product by ID | No |
| POST | `/products` | Create product | Admin |
| PUT | `/products/{id}` | Update product | Admin |
| DELETE | `/products/{id}` | Delete product | Admin |

**Query Parameters for GET /products:**
- `cat` - Filter by category ID
- `minPrice` - Minimum price filter
- `maxPrice` - Maximum price filter
- `subCategory` - Filter by subcategory

### Shopping Cart Endpoints

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| GET | `/cart` | Get user's cart | User |
| POST | `/cart/products/{productId}` | Add product to cart | User |
| PUT | `/cart/products/{productId}` | Update product quantity | User |
| DELETE | `/cart` | Clear entire cart | User |
| DELETE | `/cart/products/{productId}` | Remove specific product | User |

### Profile Endpoints

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| GET | `/profile` | Get user profile | User |
| PUT | `/profile` | Update user profile | User |

### Order Endpoints

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| POST | `/orders` | Checkout and create order | User |

---

## 📁 Project Structure

```
capstone-api-starter/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── org/yearup/
│   │   │       ├── configurations/
│   │   │       │   └── DatabaseConfig.java
│   │   │       ├── controllers/
│   │   │       │   ├── AuthenticationController.java
│   │   │       │   ├── CategoriesController.java
│   │   │       │   ├── ProductsController.java
│   │   │       │   ├── ShoppingCartController.java
│   │   │       │   ├── ProfileController.java
│   │   │       │   └── OrdersController.java
│   │   │       ├── data/
│   │   │       │   ├── CategoryDao.java
│   │   │       │   ├── ProductDao.java
│   │   │       │   ├── ShoppingCartDao.java
│   │   │       │   ├── ProfileDao.java
│   │   │       │   ├── OrderDao.java
│   │   │       │   ├── OrderLineItemDao.java
│   │   │       │   └── mysql/
│   │   │       │       ├── MySqlCategoryDao.java
│   │   │       │       ├── MySqlProductDao.java
│   │   │       │       ├── MySqlShoppingCartDao.java
│   │   │       │       ├── MySqlProfileDao.java
│   │   │       │       ├── MySqlOrderDao.java
│   │   │       │       └── MySqlOrderLineItemDao.java
│   │   │       ├── models/
│   │   │       │   ├── Category.java
│   │   │       │   ├── Product.java
│   │   │       │   ├── ShoppingCart.java
│   │   │       │   ├── ShoppingCartItem.java
│   │   │       │   ├── Profile.java
│   │   │       │   ├── Order.java
│   │   │       │   ├── OrderLineItem.java
│   │   │       │   └── User.java
│   │   │       ├── security/
│   │   │       │   └── jwt/
│   │   │       │       └── JWTTokenFilter.java
│   │   │       └── EasyshopApplication.java
│   │   └── resources/
│   │       ├── application.properties
│   │       └── static/
│   │           ├── index.html
│   │           ├── css/
│   │           ├── js/
│   │           │   ├── application.js
│   │           │   ├── config.js
│   │           │   └── services/
│   │           │       ├── products-service.js
│   │           │       ├── categories-service.js
│   │           │       ├── shoppingcart-service.js
│   │           │       ├── profile-service.js
│   │           │       └── user-service.js
│   │           ├── images/
│   │           └── templates/
│   └── test/
├── database/
│   └── create_database_easyshop.sql
├── pom.xml
└── README.md
```

---

## 📸 Screenshots

### Home Page
![Home Page](screenshots/home-page.png)
*Browse products by category with intuitive navigation*

### Product Catalog
![Product Catalog](screenshots/product-catalog.png)
*Filter products by category, price range, and subcategory*

### Product Details
![Product Details](screenshots/product-details.png)
*View detailed product information and add to cart*

### Shopping Cart
![Shopping Cart](screenshots/shopping-cart.png)
*Manage cart items with selective checkout options*

### Checkout Page
![Checkout Page](screenshots/checkout-page.png)
*Review selected items before confirming order*

### User Profile
![User Profile](screenshots/user-profile.png)
*Update personal information and shipping address*

### Admin Dashboard
![Admin Dashboard](screenshots/admin-dashboard.png)
*Manage products and categories (Admin only)*

### Login/Register
![Authentication](screenshots/login-register.png)
*Secure user authentication with JWT tokens*

---

## 🏗 Development Phases

### Phase 1: Category & Product Management
- ✅ Implemented REST endpoints for categories
- ✅ Created product search with advanced filters
- ✅ Fixed product search bugs (price range filtering)
- ✅ Fixed product update bug (duplicate products)
- ✅ Added admin-only permissions for modifications

### Phase 2: Bug Fixes
- ✅ Fixed search filter logic for price ranges
- ✅ Resolved product update creating duplicates
- ✅ Corrected SQL queries in DAO layer

### Phase 3: Shopping Cart
- ✅ Implemented persistent shopping cart
- ✅ Added cart management (add, update, remove)
- ✅ Created selective item checkout
- ✅ Integrated cart with user sessions
- ✅ Added real-time cart updates

### Phase 4: User Profile
- ✅ Created profile management endpoints
- ✅ Implemented profile view and update
- ✅ Integrated profile with user registration
- ✅ Added profile address for shipping

### Phase 5: Checkout & Orders
- ✅ Implemented order creation system
- ✅ Created order line items from cart
- ✅ Added checkout confirmation page
- ✅ Integrated profile address for orders
- ✅ Auto-clear cart after successful checkout
- ✅ Added selective item checkout functionality

---

## 🤝 Contributing

Contributions are welcome! Please follow these steps:

1. **Fork the repository**
2. **Create a feature branch**
   ```bash
   git checkout -b feature/AmazingFeature
   ```
3. **Commit your changes**
   ```bash
   git commit -m 'Add some AmazingFeature'
   ```
4. **Push to the branch**
   ```bash
   git push origin feature/AmazingFeature
   ```
5. **Open a Pull Request**

### Coding Standards
- Follow Java naming conventions
- Write meaningful commit messages
- Add comments for complex logic
- Write unit tests for new features
- Update documentation as needed

---

## 📝 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

## 📧 Contact

**Tesfahun Fola**
- GitHub: [@tesfahunfola](https://github.com/tesfahunfola)
- Email: folatesfahun@example.com
- LinkedIn: [Tesfahun Fola](https://linkedin.com/in/tesfahun-fola)

**Project Link:** [https://github.com/tesfahunfola/Easy-Shop---Capstone_3](https://github.com/tesfahunfola/Easy-Shop---Capstone_3)

---

## 🙏 Acknowledgments
- 🧑‍🏫 Instructor: Maaike — thank you for the guidance, inspiration, and support throughout this LTCA journey! 🙏✨
- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [MySQL Documentation](https://dev.mysql.com/doc/)
- [Bootstrap](https://getbootstrap.com/)
- [Font Awesome](https://fontawesome.com/)
- [Axios](https://axios-http.com/)
- Pluralsight for the project foundation

---

## 📊 Project Statistics

- **Total Lines of Code:** ~5,000+
- **Languages:** Java, JavaScript, SQL, HTML, CSS
- **API Endpoints:** 20+
- **Database Tables:** 8
- **Development Time:** 5 Phases

---

<div align="center">

### ⭐ Star this repository if you find it helpful!

Made with ❤️ by Tesfahun Fola

</div>
